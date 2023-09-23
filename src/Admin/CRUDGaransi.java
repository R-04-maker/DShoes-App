package Admin;

import DBConnect.ConnectionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class CRUDGaransi {
    private JTextField txtWarrantyType;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JButton btnCancel;
    private JTextArea txtWarrantyDescription;
    private JTable tblGaransi;
    public JPanel JPWarranty;

    DefaultTableModel model = new DefaultTableModel();
    ConnectionDB connection = new ConnectionDB();
    String id;
    String type;
    String desc;
    int status;

    public CRUDGaransi(){
        addColumn();
        loadData();
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                type = txtWarrantyType.getText();
                desc = txtWarrantyDescription.getText();
                status = 1;
                Boolean found = false;

                //validasi jika memasukkan tipe garansi yang sama
                Object[] obj = new Object[3];
                obj[0] = id;
                obj[1] = type;
                obj[2] = desc;

                int j = tblGaransi.getModel().getRowCount();
                for(int k = 0; k < j; k++) {
                    if(obj[1].toString().toLowerCase().equals(model.getValueAt(k, 1).toString().toLowerCase()))
                    {
                        found = true;
                    }
                }
                if(found) {
                    JOptionPane.showMessageDialog(null, "Warranty Type already exist!", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try{
                        if (validasiNull()) {
                            throw new Exception("Please, fill in all data!");
                        } else{
                            try {
                                type = txtWarrantyType.getText();
                                desc = txtWarrantyDescription.getText();

                                connection.stat = connection.conn.createStatement();
                                String sql = "SELECT * FROM tblGaransi ORDER BY id_garansi DESC";
                                connection.result = connection.stat.executeQuery(sql);

                                if(connection.result.next()){
                                    id = connection.result.getString("id_garansi").substring(2);

                                    String temp = "" + (Integer.parseInt(id) + 1);
                                    String nol = "";

                                    if(temp.length() == 1) nol = "000";
                                    else if (temp.length() == 2) nol = "00";
                                    else if (temp.length() == 3) nol = "0";
                                    else if (temp.length() == 4) nol = "";

                                    id = "GR" + nol + temp;
                                }
                                else {
                                    id = "GR0001";
                                }
                                String query = "INSERT INTO tblGaransi VALUES (?, ?, ?, ?)";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, id);
                                connection.pstat.setString(2, type);
                                connection.pstat.setString(3, desc);
                                connection.pstat.setString(4, "1");

                                connection.stat.close();
                                connection.pstat.executeUpdate();
                                connection.pstat.close();

                                clear();
                                JOptionPane.showMessageDialog(null, "Data saved successfully!");
                                loadData();
                            }catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "an error occurred while saving data into the database.\n" + ex);
                            }
                            clear();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtWarrantyDescription.getText().equals("") || txtWarrantyType.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please, fill in all data!");
                }else{
                    try {
                        int i = tblGaransi.getSelectedRow();
                        if(i == -1) return;
                        id = String.valueOf(model.getValueAt(i, 0));
                        type = txtWarrantyType.getText();
                        desc = txtWarrantyDescription.getText();

                        String query = "EXEC sp_UpdateGaransi @id_garansi=?, @jenis_garansi=?,@deskripsi=? ";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);
                        connection.pstat.setString(2, type);
                        connection.pstat.setString(3, desc);

                        connection.stat.close();
                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        clear();
                        JOptionPane.showMessageDialog(null, "Data updated successfully!");
                        loadData();
                    } catch (NumberFormatException nex){
                        JOptionPane.showMessageDialog(null, "Please, enter the valid number.");
                    } catch (Exception e1){
                        JOptionPane.showMessageDialog(null, "an error occurred while updating data into the database.\n" + e1);
                    }
                }
            }
        });
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int opsi;
                    opsi = JOptionPane.showConfirmDialog(null, "Are you sure delete this data?",
                            "Confirmation", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                    if(opsi != 0) {
                        JOptionPane.showMessageDialog(null, "Data failed to delete");
                    }else {

                        int i = tblGaransi.getSelectedRow();
                        if (i == -1) return;
                        id = String.valueOf(model.getValueAt(i, 0));

                        String query = "UPDATE tblGaransi SET status ='0' WHERE id_garansi=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);

                        connection.stat.close();
                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        clear();
                        JOptionPane.showMessageDialog(null, "Data deleted successfully");
                        loadData();
                    }
                }catch (NumberFormatException nex){
                    JOptionPane.showMessageDialog(null,"Please, enter the valid number");
                }catch (Exception e1){
                    JOptionPane.showMessageDialog(null,"an error occured while deleting data : "+e1);
                }
            }
        });
        tblGaransi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblGaransi.getSelectedRow();
                if(i == -1){
                    return;
                }
                txtWarrantyType.setText((String) model.getValueAt(i,1));
                txtWarrantyDescription.setText((String) model.getValueAt(i,2));
                btnUpdate.setEnabled(true);
                btnHapus.setEnabled(true);
                btnSave.setEnabled(false);
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        txtWarrantyType.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && (c != KeyEvent.VK_BACK_SPACE)
                        && (c != KeyEvent.VK_SPACE) && (c != KeyEvent.VK_PERIOD)) {
                    e.consume();
                }
            }
        });
    }

    public boolean validasiNull(){
        if(type.isEmpty()){
            return true;
        }
        else if(desc.isEmpty()) {
            return true;
        }else{
            return false;
        }
    }
    public void loadData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try{
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblGaransi WHERE status='1'";
            connection.result = connection.stat.executeQuery(query);

            while(connection.result.next()){
                Object[] obj = new Object[6];
                obj[0] = connection.result.getString("id_garansi");
                obj[1] = connection.result.getString("jenis_garansi");
                obj[2] = connection.result.getString("deskripsi");

                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
            btnSave.setEnabled(true);
            btnHapus.setEnabled(false);
            btnUpdate.setEnabled(false);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "an error occurred while loading data.\n" + e);
        }
    }
    public void clear(){
        txtWarrantyDescription.setText("");
        txtWarrantyType.setText("");
        loadData();
        btnHapus.setEnabled(false);
        btnUpdate.setEnabled(false);
//        btnHapus.setEnabled(false);
//        btnUpdate.setEnabled(false);
    }
    public void addColumn(){
        tblGaransi.setModel(model);
        model.addColumn("ID Warranty");
        model.addColumn("Warranty Type");
        model.addColumn("Description");
    }
}
