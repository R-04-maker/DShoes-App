package Kasir;

import DBConnect.ConnectionDB;
import Kasir.CRUDCustomer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CRUDCustomer extends JFrame{
    private JTextField txtNama;
    private JTextField txtHp;
    private JTextArea txtAlamat;
    private JTextField txtEmail;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnHapus;
    private JButton btnCancel;
    private JTable tblCust;
    private JTextField txtSearch;
    private JButton btnSearch;
    public JPanel JPCustomer;

    DefaultTableModel model = new DefaultTableModel();
    ConnectionDB connection = new ConnectionDB();
    String id;
    String nama;
    String nohp;
    String alamat;
    String email;

    public CRUDCustomer() {
        setContentPane(JPCustomer);
        addColumn();
        loadData();
        txtNama.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && (c != KeyEvent.VK_BACK_SPACE) && (c != KeyEvent.VK_SPACE)) {
                    e.consume();
                }
            }
        });
        txtHp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) || txtHp.getText().length() >= 13) {
                    e.consume();
                }
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*Pattern ptn = Pattern.compile("^[\\w-\\.+]*[\\w-\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
                Matcher matcher = ptn.matcher(txtEmail.getText());
                if (matcher.matches()) {

                } else {
                    JOptionPane.showMessageDialog(null, "Email is invalid!");
                    txtEmail.requestFocus();
                    return;
                }*/
                if (txtNama.getText().equals("") || txtHp.getText().equals("") || txtAlamat.getText().equals("") || txtEmail.getText().equals(""))
                    JOptionPane.showMessageDialog(null, "Please, fill in all data!");
                else{
                    try {
                        Pattern ptn = Pattern.compile("^[\\w-\\.+]*[\\w-\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
                        Matcher matcher = ptn.matcher(txtEmail.getText());
                        if (matcher.matches()) {

                        } else {
                            JOptionPane.showMessageDialog(null, "Email is invalid!");
                            txtEmail.requestFocus();
                            return;
                        }
                        nama = txtNama.getText();
                        nohp = txtHp.getText();
                        alamat = txtAlamat.getText();
                        email = txtEmail.getText();

                        connection.stat = connection.conn.createStatement();
                        String sql = "SELECT * FROM tblCustomer ORDER BY id_customer DESC";
                        connection.result = connection.stat.executeQuery(sql);

                        if(connection.result.next()){
                            id = connection.result.getString("id_customer").substring(3);

                            String temp = "" + (Integer.parseInt(id) + 1);
                            String nol = "";

                            if(temp.length() == 1) nol = "000";
                            else if (temp.length() == 2) nol = "00";
                            else if (temp.length() == 3) nol = "0";
                            else if (temp.length() == 4) nol = "";

                            id = "CSR" + nol + temp;
                        }
                        else {
                            id = "CSR0001";
                        }
                        String query = "EXEC sp_InsertCustomer @id_customer=?, @nama=?, @no_hp=?, @alamat=?, @email=?, @status=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);
                        connection.pstat.setString(2, nama);
                        connection.pstat.setString(3, nohp);
                        connection.pstat.setString(4, alamat);
                        connection.pstat.setString(5, email);
                        connection.pstat.setString(6, "1");

                        connection.stat.close();
                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        clear();
                        JOptionPane.showMessageDialog(null, "Data saved successfully!");
                        loadData();
                    } catch (NumberFormatException nex){
                        JOptionPane.showMessageDialog(null, "Please, enter the valid number ." +nex.getMessage());
                    } catch (Exception e1){
                        System.out.println("an error occurred while saving data into the database.\n" + e1);
                    }
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtNama.getText().equals("") || txtHp.getText().equals("") || txtAlamat.getText().equals("") || txtEmail.getText().equals(""))
                    JOptionPane.showMessageDialog(null, "Please, fill in all data!");
                else{
                    try {
                        Pattern ptn = Pattern.compile("^[\\w-\\.+]*[\\w-\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
                        Matcher matcher = ptn.matcher(txtEmail.getText());
                        if (matcher.matches()) {

                        } else {
                            JOptionPane.showMessageDialog(null, "Email is invalid!");
                            txtEmail.requestFocus();
                            return;
                        }
                        int i = tblCust.getSelectedRow();
                        if(i == -1) return;
                        id = String.valueOf(model.getValueAt(i, 0));
                        nama = txtNama.getText();
                        nohp = txtHp.getText();
                        alamat = txtAlamat.getText();
                        email = txtEmail.getText();

                        String query = "UPDATE tblCustomer SET nama=?, no_hp=?, alamat=?, " +
                                "email=? WHERE id_customer=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(5, id);
                        connection.pstat.setString(1, nama);
                        connection.pstat.setString(2, String.valueOf(nohp));
                        connection.pstat.setString(3, alamat);
                        connection.pstat.setString(4, email);

                        connection.stat.close();
                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        clear();
                        JOptionPane.showMessageDialog(null, "Data updated successfully!");
                        loadData();
                    } catch (NumberFormatException nex){
                        JOptionPane.showMessageDialog(null, "Please, enter the valid number.");
                    } catch (Exception e1){
                        System.out.println("an error occurred while updating data into the database.\n" + e1);
                    }
                }
            }
        });
        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtNama.getText().equals("") || txtHp.getText().equals("") || txtAlamat.getText().equals("") || txtEmail.getText().equals(""))
                    JOptionPane.showMessageDialog(null, "Please, fill in all data!");
                else{
                    try {
                        int i = tblCust.getSelectedRow();
                        if(i == -1) return;
                        id = String.valueOf(model.getValueAt(i, 0));

                        String query = "UPDATE tblCustomer SET status='0' WHERE id_customer=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);

                        connection.stat.close();
                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        clear();
                        JOptionPane.showMessageDialog(null, "Data deleted successfully!");
                        loadData();
                    } catch (NumberFormatException nex){
                        JOptionPane.showMessageDialog(null, "Please, enter the valid number.");
                    } catch (Exception e1){
                        System.out.println("an error occurred while deleting data into the database.\n" + e1);
                    }
                }
            }
        });
        tblCust.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblCust.getSelectedRow();
                if(i == -1) {
                    return;
                }
                txtNama.setText((String) model.getValueAt(i,1));
                txtHp.setText((String) model.getValueAt(i,2));
                txtAlamat.setText((String) model.getValueAt(i,3));
                txtEmail.setText((String) model.getValueAt(i,4));
                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnHapus.setEnabled(true);
            }
        });
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.getDataVector().removeAllElements();
                model.fireTableDataChanged();
                try{
                    ConnectionDB connection = new ConnectionDB();
                    connection.stat = connection.conn.createStatement();
                    String query = "SELECT * FROM tblCustomer WHERE nama LIKE '%"+txtSearch.getText()+"%' AND status='1'";
                    connection.result = connection.stat.executeQuery(query);
                    while(connection.result.next()){
                        Object[] obj = new Object[7];
                        obj[0] = connection.result.getString("id_customer");
                        obj[1] = connection.result.getString("nama");
                        obj[2] = connection.result.getString("no_hp");
                        obj[3] = connection.result.getString("alamat");
                        obj[4] = connection.result.getString("email");
                        model.addRow(obj);
                    }
                    if (model.getRowCount() == 0){
                        JOptionPane.showMessageDialog(null, "Data not found");
                    }
                    connection.stat.close();
                    connection.result.close();
                } catch (Exception ex){
                    System.out.println("an error occurred while searching for data" + e);
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
    }

    public void addColumn(){
        tblCust.setModel(model);
        model.addColumn("ID Customer");
        //model.addColumn("ID Member");
        model.addColumn("Name");
        model.addColumn("Phone Number");
        model.addColumn("Address");
        model.addColumn("Email");
        //model.addColumn("Status");
    }


    public void loadData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try{
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblCustomer WHERE status='1'";
            connection.result = connection.stat.executeQuery(query);

            while(connection.result.next()){
                Object[] obj = new Object[7];
                obj[0] = connection.result.getString("id_customer");
                obj[1] = connection.result.getString("nama");
                obj[2] = connection.result.getString("no_hp");
                obj[3] = connection.result.getString("alamat");
                obj[4] = connection.result.getString("email");
                //obj[1] = connection.result.getString("id_member");
                /*if (connection.result.getString("status").equals("1"))
                    obj[6] = "Aktif";
                else obj[6] = "Tidak Aktif";*/
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
            btnSave.setEnabled(true);
            btnUpdate.setEnabled(false);
            btnHapus.setEnabled(false);
        }
        catch(Exception e){
            System.out.println("an error occurred while loading for data.\n" + e);
        }
    }

    public void clear(){
        txtNama.setText("");
        txtHp.setText("");
        txtAlamat.setText("");
        txtEmail.setText("");
        txtSearch.setText("");
        loadData();
    }

    public static void main(String[] args){
        JFrame frame = new CRUDCustomer();
        frame.setContentPane(new CRUDCustomer().JPCustomer);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
