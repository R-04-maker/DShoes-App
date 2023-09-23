package Admin;

import DBConnect.ConnectionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class CRUDSupplier extends JFrame {
    private JTable tblSupplier;
    private JTextField txtSupplierName;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnSave;
    public JPanel JPSupplier;
    private JTextField txtNotel;
    private JTextArea txtAddress;
    private JTextField txtMerk;
    private JButton btnCancel;
    private JTextField txtSearch;
    private JButton btnSearch;

    DefaultTableModel model = new DefaultTableModel();
    ConnectionDB connection = new ConnectionDB();
    String id;
    String nama;
    String merk;
    String no_telepon;
    String alamat;
    int status;

    public CRUDSupplier(){
        setContentPane(JPSupplier);
        addColumn();
        loadData();
        btnDelete.setEnabled(false);
        btnUpdate.setEnabled(false);
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nama = txtSupplierName.getText();
                no_telepon = txtNotel.getText();
                alamat = txtAddress.getText();
                merk = txtMerk.getText();
                status = 1;
                Boolean found = false;
                // validasi jika memasukkan data yang sama
                Object[] obj = new Object[5];
                obj[0] = id;
                obj[1] = nama;
                obj[2] = no_telepon;
                obj[3] = merk;
                obj[4] = alamat;

                int j = tblSupplier.getModel().getRowCount();
                for(int k=0; k<j; k++){
                    if(obj[1].toString().toLowerCase().equals(model.getValueAt(k, 1).toString().toLowerCase())){
                        found = true;
                    }
                }
                if(found){
                    JOptionPane.showMessageDialog(null, "Supplier already exist!", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }else{
                    try{
                        if (txtSupplierName.getText().equals("") || txtMerk.getText().equals("") || txtAddress.getText().equals(""))
                            JOptionPane.showMessageDialog(null, "Please, fill in all data!");
                        else if (txtNotel.getText().length() > 13 || txtNotel.getText().length() < 8){
                            JOptionPane.showMessageDialog(null, "Length of the phone number must be 8 - 13 digits!");
                        }else{
                            try {
                                nama = txtSupplierName.getText();
                                no_telepon = txtNotel.getText();
                                alamat = txtAddress.getText();
                                merk = txtMerk.getText();

                                connection.stat = connection.conn.createStatement();
                                String sql = "SELECT * FROM tblSupplier ORDER BY id_supplier DESC";
                                connection.result = connection.stat.executeQuery(sql);

                                if(connection.result.next()){
                                    id = connection.result.getString("id_supplier").substring(3);

                                    String temp = "" + (Integer.parseInt(id) + 1);
                                    String nol = "";

                                    if(temp.length() == 1) nol = "000";
                                    else if (temp.length() == 2) nol = "00";
                                    else if (temp.length() == 3) nol = "0";
                                    else if (temp.length() == 4) nol = "";

                                    id = "SUP" + nol + temp;
                                }
                                else {
                                    id = "SUP0001";
                                }
                                //String query = /*"INSERT INTO tblCustomer VALUES (?, ?, ?, ?, ?, ?, ?)"*/
                                String query = "EXEC sp_InsertSupplier @id_supplier=?, @nama_supplier=?, @no_telepon=?, @alamat=?, @merk=?, @status=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, id);
                                connection.pstat.setString(2, nama);
                                connection.pstat.setString(3, no_telepon);
                                connection.pstat.setString(4, alamat);
                                connection.pstat.setString(5, merk);
                                connection.pstat.setString(6, "1");

                                connection.stat.close();
                                connection.pstat.executeUpdate();
                                connection.pstat.close();

                                clear();
                                JOptionPane.showMessageDialog(null, "Data saved successfully!");
                                loadData();
                            } catch (NumberFormatException nex){
                                JOptionPane.showMessageDialog(null, "Please, enter the valid number ." +nex.getMessage());
                            }
                        }
                    } catch (Exception e1){
                        JOptionPane.showMessageDialog(null,"an error occurred while saving data into the database.\n" + e1);
                    }
                }
            }
        });
        tblSupplier.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblSupplier.getSelectedRow();
                if(i == -1){
                    return;
                }
                txtSupplierName.setText((String) model.getValueAt(i,1));
                txtMerk.setText((String) model.getValueAt(i,2));
                txtNotel.setText((String) model.getValueAt(i,3));
                txtAddress.setText((String) model.getValueAt(i,4));
                btnSave.setEnabled(false);
                btnDelete.setEnabled(true);
                btnUpdate.setEnabled(true);
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opsi;
                if(txtSupplierName.getText().equals("") || txtMerk.getText().equals("") || txtAddress.getText().equals("") || txtNotel.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"Please, fill in all data!");
                }else {
                    try {
                        opsi = JOptionPane.showConfirmDialog(null, "Are you sure delete this data?",
                                "Confirmation", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (opsi != 0) {
                            JOptionPane.showMessageDialog(null, "Data failed to delete");
                        } else {
                            int i = tblSupplier.getSelectedRow();
                            if (i == -1) return;
                            id = String.valueOf(model.getValueAt(i, 0));

                            String query = "UPDATE tblSupplier SET status ='0' WHERE id_supplier=?";
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
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtSupplierName.getText().equals("") || txtMerk.getText().equals("") || txtAddress.getText().equals("") || txtNotel.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please, fill in all data!");
                }else if (txtNotel.getText().length() > 13 || txtNotel.getText().length() < 8){
                    JOptionPane.showMessageDialog(null, "Length of the phone number must be 8 - 13 digits!");
                }else{
                    try {
                        int i = tblSupplier.getSelectedRow();
                        if(i == -1) return;
                        id = String.valueOf(model.getValueAt(i, 0));
                        nama = txtSupplierName.getText();
                        no_telepon = txtNotel.getText();
                        alamat = txtAddress.getText();
                        merk = txtMerk.getText();

                        String query = "EXEC sp_UpdateSupplier @id_supplier=?, @nama_supplier=?, @no_telepon=?, @alamat=?, @merk=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);
                        connection.pstat.setString(2, nama);
                        connection.pstat.setString(3, String.valueOf(no_telepon));
                        connection.pstat.setString(4, alamat);
                        connection.pstat.setString(5, merk);

                        connection.stat.close();
                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        clear();
                        JOptionPane.showMessageDialog(null, "Data updated successfully!");
                        loadData();
                    } catch (NumberFormatException nex){
                        JOptionPane.showMessageDialog(null, "Please, enter the valid number.");
                    } catch (Exception e1){
                        JOptionPane.showMessageDialog(null,"an error occurred while updating data into the database.\n" + e1);
                    }
                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
                loadData();
            }
        });
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.getDataVector().removeAllElements();
                model.fireTableDataChanged();

                try{
                    connection.stat = connection.conn.createStatement();
                    String query = "SELECT id_supplier,nama_supplier,merk,no_telepon,alamat FROM tblSupplier WHERE nama_supplier LIKE '%"+txtSearch.getText()+"%' AND status = '1'";
                    connection.result = connection.stat.executeQuery(query);

                    while(connection.result.next()){
                        Object[] obj = new Object[6];
                        obj[0] = connection.result.getString("id_supplier");
                        obj[1] = connection.result.getString("nama_supplier");
                        obj[2] = connection.result.getString("merk");
                        obj[3] = connection.result.getString("no_telepon");
                        obj[4] = connection.result.getString("alamat");
                        model.addRow(obj);
                    }
                    connection.stat.close();
                    connection.result.close();
                    if (model.getRowCount() == 0){
                        JOptionPane.showMessageDialog(null, "Data not found");
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null,"Error while loading for data : "+ex);
                }
            }
        });
        txtSupplierName.addKeyListener(new KeyAdapter() {
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
        txtMerk.addKeyListener(new KeyAdapter() {
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
        txtNotel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if(((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) || txtNotel.getText().length() >= 13){
                    e.consume();
                }
            }
        });
    }
    public void addColumn(){
        tblSupplier.setModel(model);
        model.addColumn("ID Supplier");
        model.addColumn("Name");
        model.addColumn("Merk");
        model.addColumn("Phone Number");
        model.addColumn("Address");
    }
    public void loadData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try{
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblSupplier WHERE status = 1";
            connection.result = connection.stat.executeQuery(query);

            while(connection.result.next()){
                Object[] obj = new Object[6];
                obj[0] = connection.result.getString("id_supplier");
                obj[1] = connection.result.getString("nama_supplier");
                obj[2] = connection.result.getString("merk");
                obj[3] = connection.result.getString("no_telepon");
                obj[4] = connection.result.getString("alamat");
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
            btnSave.setEnabled(true);
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Eror while loading for data : "+ex);
        }
    }
    public void clear(){
        txtSupplierName.setText("");
        txtMerk.setText("");
        txtNotel.setText("");
        txtAddress.setText("");
        txtSearch.setText("");
        loadData();
    }

}