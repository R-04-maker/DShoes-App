package Admin;

import DBConnect.ConnectionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class CRUDKategoriSepatu extends JFrame{

    public JPanel JPCategory;
    private JTextField txtCategoryName;
    private JButton btnHapus;
    private JTable tblKategori;
    private JButton btSave;
    private JButton btUpdate;
    private JButton btnCancel;

    DefaultTableModel model = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }
    };
    ConnectionDB connection = new ConnectionDB();
    String id;
    String nama;
    public CRUDKategoriSepatu(){
        addColumn();
        loadData();
        btUpdate.setEnabled(false);
        btnHapus.setEnabled(false);
        btSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nama  =txtCategoryName.getText();
                boolean found = false;
                // validasi tidak boleh sama
                Object[] obj = new Object[2];
                obj[0] = id;
                obj[1] = nama;

                int j = tblKategori.getModel().getRowCount();
                for(int k=0; k<j; k++){
                    if(obj[1].toString().toLowerCase().equals((model.getValueAt(k, 1).toString().toLowerCase()))){
                        found = true;
                    }
                }
                if(found) {
                    JOptionPane.showMessageDialog(null, "Shoe Category already exist!", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else{
                    try {
                        if (txtCategoryName.getText().equals(""))
                            JOptionPane.showMessageDialog(null, "Please, fill in all data!");
                        else {
                            try {
                                nama = txtCategoryName.getText();
                                //JOptionPane.showMessageDialog(null, "test");

                                connection.stat = connection.conn.createStatement();
                                String query = "SELECT * FROM tblKategoriSepatu ORDER BY id_kategori DESC";
                                connection.result = connection.stat.executeQuery(query);

                                if (connection.result.next()) {
                                    id = connection.result.getString("id_kategori").substring(3);

                                    String temp = "" + (Integer.parseInt(id) + 1);
                                    String nol = "";

                                    if (temp.length() == 1) nol = "000";
                                    else if (temp.length() == 2) nol = "00";
                                    else if (temp.length() == 3) nol = "0";
                                    else if (temp.length() == 4) nol = "";

                                    id = "JNS" + nol + temp;
                                } else {
                                    id = "JNS0001";
                                }
                                String sql = "EXEC sp_InsertKategoriSepatu @id_kategori=?, @deskripsi_kategori=?, @status=?";
                                connection.pstat = connection.conn.prepareStatement(sql);
                                connection.pstat.setString(1, id);
                                connection.pstat.setString(2, nama);
                                connection.pstat.setString(3, "1");


                                connection.stat.close();
                                connection.pstat.executeUpdate();
                                connection.pstat.close();

                                clear();
                                JOptionPane.showMessageDialog(null, "Data saved successfully!");
                                loadData();
                            } catch (NumberFormatException nex) {
                                JOptionPane.showMessageDialog(null, "Please, enter the valid number ." + nex.getMessage());
                            } catch (Exception e1) {
                                JOptionPane.showMessageDialog(null, "an error occurred while saving data into the database.\n" + e1);
                            }
                        }
                    }catch(Exception e1){

                    }
                }
            }
        });
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opsi;
                if(txtCategoryName.getText().equals("") ){
                    JOptionPane.showMessageDialog(null,"Please, fill in all data!");
                }else {
                    try {
                        opsi = JOptionPane.showConfirmDialog(null, "Are you sure delete this data?",
                                "Confirmation", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (opsi != 0) {
                            JOptionPane.showMessageDialog(null, "Data failed to delete");
                        } else {
                            int i = tblKategori.getSelectedRow();
                            if (i == -1) return;
                            id = String.valueOf(model.getValueAt(i, 0));

                            String query = "UPDATE tblKategoriSepatu SET status ='0' WHERE id_kategori=?";
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
                        JOptionPane.showMessageDialog(null, "an error occured while deleting data : "+e1);
                    }
                }
            }
        });
        btUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nama  =txtCategoryName.getText();
                boolean found = false;
                // validasi tidak boleh sama
                Object[] obj = new Object[2];
                obj[0] = id;
                obj[1] = nama;

                int j = tblKategori.getModel().getRowCount();
                for(int k=0; k<j; k++){
                    if(obj[1].toString().toLowerCase().equals((model.getValueAt(k, 1).toString().toLowerCase()))){
                        found = true;
                    }
                }
                if(found) {
                    JOptionPane.showMessageDialog(null, "Shoe Category already exist!", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else{
                    try {
                        if (txtCategoryName.getText().equals(""))
                            JOptionPane.showMessageDialog(null, "Please, fill in all data!");
                        else {
                            try {
                                int i = tblKategori.getSelectedRow();
                                if (i == -1) return;
                                id = String.valueOf(model.getValueAt(i, 0));
                                nama = txtCategoryName.getText();

                                String query = "EXEC sp_UpdateKategoriSepatu @id_kategori=?, @deskripsi_kategori=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, id);
                                connection.pstat.setString(2, nama);

                                connection.stat.close();
                                connection.pstat.executeUpdate();
                                connection.pstat.close();

                                clear();
                                JOptionPane.showMessageDialog(null, "Data updated successfully!");
                                loadData();
                            } catch (NumberFormatException nex) {
                                JOptionPane.showMessageDialog(null, "Please, enter the valid number.");
                            } catch (Exception e1) {
                                JOptionPane.showMessageDialog(null, "an error occurred while updating data into the database.\n" + e1);
                            }
                        }
                    } catch(Exception e1){

                    }
                }
            }
        });
        tblKategori.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblKategori.getSelectedRow();
                if(i == -1){
                    return;
                }
                txtCategoryName.setText((String) model.getValueAt(i,1));
                btSave.setEnabled(false);
                btUpdate.setEnabled(true);
                btnHapus.setEnabled(true);
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
                loadData();
            }
        });
        txtCategoryName.addKeyListener(new KeyAdapter() {
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
    public void addColumn(){
        tblKategori.setModel(model);
        model.addColumn("ID Category");
        model.addColumn("Category Name");
    }
    public void loadData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try{
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadKategoriSepatu";
            connection.result = connection.stat.executeQuery(query);

            while(connection.result.next()){
                Object[] obj = new Object[2];
                obj[0] = connection.result.getString("id_kategori");
                obj[1] = connection.result.getString("deskripsi_kategori");
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
            btSave.setEnabled(true);
            btnHapus.setEnabled(false);
            btUpdate.setEnabled(false);
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null, "Eror while loading for data : "+ex);
        }
    }
    public void clear(){
        txtCategoryName.setText("");
        btnHapus.setEnabled(false);
        btUpdate.setEnabled(false);
        loadData();
    }
}
