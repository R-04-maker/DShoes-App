package Admin;

import javax.swing.*;
import Admin.CRUDPegawai;

import DBConnect.ConnectionDB;
import com.toedter.calendar.JDateChooser;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.Date;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class CRUDPegawai extends JFrame{
    private JTextField txtEmpName;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnSave;
    private JComboBox cmbRole;
    private JTextField txtNIK;
    private JRadioButton rbLaki;
    private JRadioButton rbPerempuan;
    private JPanel jpCalendar;
    private JTextField txtPhoneNumber;
    private JTextField txtUsername;
    private JTextField txtPassword;
    private JTable tableEmployee;
    private JTextField txtSearch;
    private JButton btnClear;
    public JPanel JPPegawai;
    private JComboBox cmbStatus;

    //table
    private final DefaultTableModel model;

    //connection database
    ConnectionDB connection = new ConnectionDB();

    //tanggal sekarang
    java.util.Date dateNow = new java.util.Date();
    JDateChooser datechose = new JDateChooser();

    //variabel global
    String kode;
    String jabatan;
    String namapegawai;
    String nik;
    String ttl;
    String jenkil;
    String noTelepon;
    String username;
    String password;
    //String status;

    public CRUDPegawai() {
        model = new DefaultTableModel();
        tableEmployee.setModel(model);
        jpCalendar.add(datechose);
        addColomn();
        clear();
        fillcombo();
        tampilJabatan();
        String.valueOf(cmbRole.getSelectedItem());
        datechose.setDate(dateNow);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        //cmbRole.setSelectedIndex(-1);
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //String role = cmbRole.getSelectedItem().toString();
                if (Objects.equals(txtEmpName.getText(), "") || txtNIK.getText().equals("") || (datechose.getDate() == dateNow || datechose.getDate() == null)
                        || Objects.equals(txtUsername.getText(), "") || cmbRole.getSelectedItem() == "" || Objects.equals(txtPhoneNumber.getText(), "") || Objects.equals(txtPassword.getText(), "")){
                    JOptionPane.showMessageDialog(null, "Please, fill in all data!");
                }
                else if (txtNIK.getText().length() != 16){
                    JOptionPane.showMessageDialog(null, "NIK must be 16 digits!");
                }else if(datechose.getDate().compareTo(dateNow) > 0 ) {
                    JOptionPane.showMessageDialog(null, "Cannot choose a date later than today!");
                    datechose.setDate(dateNow);
                }else {
                    namapegawai = txtEmpName.getText();
                    jabatan = String.valueOf(cmbRole.getSelectedItem());
                    //JOptionPane.showMessageDialog(null, jabatan);
                    nik = txtNIK.getText();
                    //JRadioButton rbLaki = new JRadioButton("Male");
                    ttl = formatter.format(datechose.getDate());
                    if (rbLaki.isSelected()){
                        jenkil = "Male";
                    }
                    if  (rbPerempuan.isSelected()){
                        jenkil = "Female";
                    }
                    noTelepon = txtPhoneNumber.getText();
                    username = txtUsername.getText();
                    password = txtPassword.getText();

                    try {
                        connection.stat = connection.conn.createStatement();
                        String sql2 = "SELECT * FROM tblJabatan WHERE nama_jabatan=?";
                        connection.pstat = connection.conn.prepareStatement(sql2);
                        connection.pstat.setString(1, jabatan);
                        connection.result = connection.pstat.executeQuery();

                        while (connection.result.next()){
                            jabatan = (connection.result.getString("id_jabatan"));
                        }
                        //JOptionPane.showMessageDialog(null, jabatan);

                        connection.stat.close();
                        connection.result.close();

                        connection.stat = connection.conn.createStatement();
                        String sql = "SELECT * FROM tblPegawai ORDER BY id_pegawai DESC";
                        connection.result = connection.stat.executeQuery(sql);

                        if(connection.result.next()){
                            kode = connection.result.getString("id_pegawai").substring(3);

                            String temp = "" + (Integer.parseInt(kode) + 1);
                            String nol = "";

                            if(temp.length() == 1) nol = "000";
                            else if (temp.length() == 2) nol = "00";
                            else if (temp.length() == 3) nol = "0";
                            else if (temp.length() == 4) nol = "";

                            kode = "PGI" + nol + temp;
                        }
                        else {
                            kode = "PGI0001";
                        }
                        String sp = "EXEC sp_InsertPegawai @Id_pegawai=?,@id_jabatan=?,@Nama_pegawai=?,@NIK=?,@TTL=?,@Jenis_kelamin=?," +
                                "@No_telp=?,@Username=?,@Password=?,@Status=?";
                        connection.pstat = connection.conn.prepareStatement(sp);
                        connection.pstat.setString(1, kode);
                        connection.pstat.setString(2, jabatan);
                        connection.pstat.setString(3, namapegawai);
                        connection.pstat.setString(4, String.valueOf(nik));
                        connection.pstat.setString(5, ttl);
                        connection.pstat.setString(6, jenkil);
                        connection.pstat.setString(7, noTelepon);
                        connection.pstat.setString(8, username);
                        connection.pstat.setString(9, password);
                        connection.pstat.setString(10, "1");

                        connection.stat.close();
                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        clear();
                        JOptionPane.showMessageDialog(null, "Data saved successfully!");
                        loadData();
                    } catch (NumberFormatException nex){
                        JOptionPane.showMessageDialog(null, "Please, enter the valid number ."+nex.getMessage());
                    } catch (Exception e1){
                        JOptionPane.showMessageDialog(null, "an error occurred while saving data into the database.\n" + e1);
                    }
                    }
                }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(txtEmpName.getText(), "") && Objects.equals(txtNIK.getText(), "") && (datechose.getDate() == dateNow || datechose.getDate() == null)
                        && Objects.equals(txtUsername.getText(), "") && cmbRole.getSelectedItem() == "" && Objects.equals(txtPhoneNumber.getText(), "") && Objects.equals(txtPassword.getText(), "")) {
                    JOptionPane.showMessageDialog(null, "Please, fill in all data!");
                }else if (txtNIK.getText().length() != 16) {
                    JOptionPane.showMessageDialog(null, "NIK must be 16 digits!");
                }else if(datechose.getDate().compareTo(dateNow) > 0) {
                    JOptionPane.showMessageDialog(null, "Cannot choose a date later than today!");
                    datechose.setDate(dateNow);
                }else{
                    namapegawai = txtEmpName.getText();
                    jabatan = String.valueOf(cmbRole.getSelectedItem());
                    //JOptionPane.showMessageDialog(null, jabatan);
                    nik = txtNIK.getText();
                    //JRadioButton rbLaki = new JRadioButton("Male");
                    ttl = formatter.format(datechose.getDate());
                    if (rbLaki.isSelected()){
                        jenkil = "Male";
                    }
                    if  (rbPerempuan.isSelected()){
                        jenkil = "Female";
                    }
                    noTelepon = txtPhoneNumber.getText();
                    username = txtUsername.getText();
                    password = txtPassword.getText();

                    try {
                        connection.stat = connection.conn.createStatement();
                        String sql2 = "SELECT * FROM tblJabatan WHERE nama_jabatan=?";
                        connection.pstat = connection.conn.prepareStatement(sql2);
                        connection.pstat.setString(1, jabatan);
                        connection.result = connection.pstat.executeQuery();

                        while (connection.result.next()){
                            jabatan = (connection.result.getString("id_jabatan"));
                        }
                        //JOptionPane.showMessageDialog(null, jabatan);

                        connection.stat.close();
                        connection.result.close();

                        connection.stat = connection.conn.createStatement();
                        String sp = "EXEC sp_UpdatePegawai @Id_jabatan=?,@Nama_pegawai=?,@NIK=?,@TTL=?,@Jenis_kelamin=?," +
                                "@No_telp=?,@Username=?,@Password=?,@Status=?,@Id_pegawai=?";
                        connection.pstat = connection.conn.prepareStatement(sp);
                        connection.pstat.setString(10, kode);
                        connection.pstat.setString(1, jabatan);
                        connection.pstat.setString(2, namapegawai);
                        connection.pstat.setString(3, nik);
                        connection.pstat.setString(4, ttl);
                        connection.pstat.setString(5, jenkil);
                        connection.pstat.setString(6, noTelepon);
                        connection.pstat.setString(7, username);
                        connection.pstat.setString(8, password);
                        connection.pstat.setString(9, "1");

                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                    } catch (NumberFormatException nex){
                        JOptionPane.showMessageDialog(null, "Please, enter the valid number ."+nex.getMessage());
                    } catch (Exception e1){
                        JOptionPane.showMessageDialog(null, "an error occurred while updating data into the database.\n" + e1);
                    }
                    clear();
                    JOptionPane.showMessageDialog(null, "Data updated successfully!");
                    loadData();
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opsi;
                if (Objects.equals(txtEmpName.getText(), "") && Objects.equals(txtNIK.getText(), "") && (datechose.getDate() == dateNow || datechose.getDate() == null)
                        && Objects.equals(txtUsername.getText(), "") && cmbRole.getSelectedItem() == "" && Objects.equals(txtPhoneNumber.getText(), "") && Objects.equals(txtPassword.getText(), "")) {
                    JOptionPane.showMessageDialog(null, "Please, fill in all data!");
                }else{
                    try {
                        opsi = JOptionPane.showConfirmDialog(null, "Are you sure delete this data?",
                                "Confirmation", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (opsi != 0) {
                            JOptionPane.showMessageDialog(null, "Data failed to delete");
                        } else {
                            String query = "sp_DeletePegawai @Id_pegawai=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, kode);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                        }
                    } catch (NumberFormatException nex){
                        JOptionPane.showMessageDialog(null, "Please, enter the valid number ."+nex.getMessage());
                    } catch (Exception e1){
                        JOptionPane.showMessageDialog(null, "an error occurred while deleting data into the database.\n" + e1);
                    }
                    clear();
                    JOptionPane.showMessageDialog(null, "Data deleted successfully!");
                    loadData();
                }
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rbLaki.setSelected(false);
                rbPerempuan.setSelected(false);
                clear();
                datechose.setDate(dateNow);
            }
        });
        tableEmployee.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tableEmployee.getSelectedRow();
                if(i == -1){
                    return;
                }
                kode = (String) model.getValueAt(i,0);
                cmbRole.setSelectedItem(model.getValueAt(i,2));
                txtEmpName.setText((String) model.getValueAt(i, 1));
                txtNIK.setText((String) model.getValueAt(i, 3));
                datechose.setDate(Date.valueOf(model.getValueAt(i,4).toString()));
                jenkil = (String) model.getValueAt(i, 5);
                if (jenkil.equals("Male")){
                    rbLaki.setSelected(true);
                    rbPerempuan.setSelected(false);
                }
                else{
                    rbLaki.setSelected(false);
                    rbPerempuan.setSelected(true);
                }
                txtPhoneNumber.setText((String) model.getValueAt(i, 6));
                txtUsername.setText((String) model.getValueAt(i, 7));
                txtPassword.setText((String) model.getValueAt(i, 8));
                String status = (String) model.getValueAt(i,9);
                if(Objects.equals(status, "Active")){
                    cmbStatus.setSelectedItem("Active");
                }else {
                    cmbStatus.setSelectedItem("Inactive");
                }
                btnDelete.setEnabled(true);
                btnUpdate.setEnabled(true);
                btnSave.setEnabled(false);
            }
        });
        rbLaki.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rbPerempuan.setSelected(false);
                rbLaki.setSelected(true);
            }
        });
        rbPerempuan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rbLaki.setSelected(false);
                rbPerempuan.setSelected(true);
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                //menghapus seluruh data ditampilkan(jika ada) untuk tampilan pertama
                model.getDataVector().removeAllElements();
                //memberi tahu data yang kosong
                model.fireTableDataChanged();

                try{
                    connection.stat = connection.conn.createStatement();
                    String query = "SELECT * FROM tblPegawai INNER JOIN tblJabatan "+
                            "ON tblPegawai.id_jabatan = tblJabatan.id_jabatan WHERE tblPegawai.nama_pegawai LIKE '%" + txtSearch.getText() +"%' AND tblPegawai.status = 1";
                    connection.result = connection.stat.executeQuery(query);

                    while (connection.result.next()){
                        String temp = connection.result.getString("password");

                        Object[] obj = new Object[10];
                        obj[0] = connection.result.getString("id_pegawai");
                        obj[1] = connection.result.getString("id_jabatan");
                        obj[2] = connection.result.getString("nama_pegawai");
                        obj[3] = connection.result.getString("NIK");
                        obj[4] = connection.result.getString("TTL");
                        obj[5] = connection.result.getString("jenis_kelamin");
                        obj[6] = connection.result.getString("no_telp");
                        obj[7] = connection.result.getString("username");
                        obj[8] = mask(temp.length());
                        if (connection.result.getString("status").equals("1"))
                            obj[9] = "Active";
                        else obj[9] = "Inactive";
                        model.addRow(obj);
                    }
                    connection.stat.close();
                    connection.result.close();
                }
                catch(Exception ex){
                    JOptionPane.showMessageDialog(null,"An occured error while loading data \n" + ex);
                }
            }
        });
        txtEmpName.addKeyListener(new KeyAdapter() {
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
        txtNIK.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) || txtNIK.getText().length() >= 16 ) {
                    e.consume();
                }
            }
        });
        txtPhoneNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) || txtPhoneNumber.getText().length() >= 13) {
                    e.consume();
                }
            }
        });
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

            }
        });
    }

    //header table
    public void addColomn(){
        model.addColumn("ID Employee");
        model.addColumn("Employee Name");
        model.addColumn("Employee Type");
        model.addColumn("NIK");
        model.addColumn("Date of Birth");
        model.addColumn("Gender");
        model.addColumn("Phone Number");
        model.addColumn("Username");
        model.addColumn("Password");
        model.addColumn("Status");
    }

    //get data from database
    public void tampilJabatan() {
        try {
            ConnectionDB connection = new ConnectionDB();
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT * FROM tblJabatan";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                cmbRole.setSelectedItem(connection.result.getString("nama_jabatan"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "an error occurred while loading data.\n" + ex);
        }
    }

    //load data from database
    public void loadData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try{
            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadPegawai";
            connection.result = connection.stat.executeQuery(query);

            while(connection.result.next()){
                String temp = connection.result.getString("password");

                Object[] obj = new Object[10];
                obj[0] = connection.result.getString("id_pegawai");
                obj[1] = connection.result.getString("nama_pegawai");
                obj[2] = connection.result.getString("nama_jabatan");
                obj[3] = connection.result.getString("NIK");
                obj[4] = connection.result.getString("ttl");
                obj[5] = connection.result.getString("jenis_kelamin");
                obj[6] = connection.result.getString("no_telp");
                obj[7] = connection.result.getString("username");
                obj[8] = mask(temp.length());
                obj[9] = connection.result.getString("status");
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
            btnSave.setEnabled(true);
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "an error occurred while loading data.\n" + e);
        }
    }

    //method clear
    public void clear(){
        txtSearch.setText("");
        txtEmpName.setText(null);
        cmbRole.setSelectedItem(null);
        txtNIK.setText(null);
        jpCalendar.getToolTipText(null);
        rbLaki.setText(null);
        rbPerempuan.setText(null);
        txtPhoneNumber.setText(null);
        txtUsername.setText(null);
        txtPassword.setText(null);
        loadData();
        btnDelete.setEnabled(false);
        btnUpdate.setEnabled(false);
    }

    private String mask(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append('\u25CF');
        }
        return new String(sb);
    }

    //validasi Jika Kosong
    public boolean validasiNull(){
        if(namapegawai.isEmpty() || jabatan.isEmpty() || nik.isEmpty() || ttl.isEmpty() || jenkil.isEmpty() || noTelepon.isEmpty() || username.isEmpty() || password.isEmpty()){
            return true;
        }else {
            return false;
        }
    }

    public void fillcombo(){
        try{
            connection.stat = connection.conn.createStatement();
            String query = "SELECT nama_jabatan FROM tblJabatan WHERE status = 1";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                cmbRole.addItem(connection.result.getString("nama_jabatan"));
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "an error occurred while loading data.\n" + e);
        }
    }

    /*public static void main(String[] args) {
        JFrame frame = new CRUDPegawai();
        frame.setContentPane(new CRUDPegawai().JPPegawai);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(1920,1080);
        frame.setVisible(true);
    }*/
}