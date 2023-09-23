package Kasir;

import DBConnect.ConnectionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TransaksiMember extends JFrame {
    private JPanel PanelForm;
    private JTextField txtTotalPayment;
    private JComboBox cmbCustomerID;
    private JComboBox cmbMemberType;
    private JTable table_data;
    public JPanel TransaksiMember;
    private JTextField txtRegisDate;
    private JTextField txtExpiredDate;
    private JTextField txtPayment;
    private JTextField txtChange;
    private JButton btnSave;
    private JButton btnClear;
    String idMemberType,hargamember,idMember,idtipemember;
    int masaBerlaku;
    ConnectionDB connection = new ConnectionDB();
    DefaultTableModel model = new DefaultTableModel();
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date currentDate = new Date();
    Date expired;


    public TransaksiMember(String[] value){
        table_data.setModel(model);
        setContentPane(TransaksiMember);
        clear();
        addColumn();
        LoadDataMember();
        fillcmbCust();
        fillTypeMember();
        cmbMemberType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                try {
                    connection.stat = connection.conn.createStatement();
                    String sql = "SELECT id_member,masa_berlaku,harga_member FROM tblTipeMember WHERE jenis_member LIKE" +
                            "'%"+String.valueOf(cmbMemberType.getSelectedItem())+"%'";
                    connection.result = connection.stat.executeQuery(sql);
                    while (connection.result.next()){
                        idMemberType = (connection.result.getString("id_member"));
                        masaBerlaku = (connection.result.getInt("masa_berlaku"));
                        Calendar c  = Calendar.getInstance();
                        c.setTime(currentDate);
                        c.add(Calendar.DATE,masaBerlaku);
                        expired = c.getTime();
                        txtExpiredDate.setText(sdf.format(expired));
                        txtTotalPayment.setText(formatrupiah(connection.result.getInt("harga_member")));
                    }
                    connection.stat.close();
                    connection.result.close();
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null,"Error while selcted member type : "+ex);
                }
            }
        });

        btnSave.addActionListener(new   ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double totalPay = Double.parseDouble(txtTotalPayment.getText().replace(",",""));
                double payment = Double.parseDouble(txtPayment.getText().replace(",",""));
                if(txtExpiredDate.getText() == null || txtPayment.getText() == null){
                    JOptionPane.showMessageDialog(null, "Please Fill Data Correctly !!");
                }else if (totalPay > payment){
                    JOptionPane.showMessageDialog(null, "Not enough money !!");
                }else{
                    AutoID();
                    String.valueOf(cmbMemberType.getSelectedItem());
                    saveIDcmbMemberType();
                    try{
                        String query = "EXEC sp_InsertTransaksiMemberPRG3 @id_member=?,@id_customer=?,@id_jnsmember=?,@id_pegawai=?,@tgldaftar=?,@tgl_expired=?,@biaya=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1,idMember);
                        connection.pstat.setString(2, String.valueOf(cmbCustomerID.getSelectedItem()));
                        connection.pstat.setString(3,idMemberType);
                        connection.pstat.setString(4,value[5]);
                        connection.pstat.setDate(5, java.sql.Date.valueOf((sdf.format(new Date()))));
                        connection.pstat.setDate(6, java.sql.Date.valueOf((sdf.format(expired))));
                        connection.pstat.setString(7,txtTotalPayment.getText());
                        connection.pstat.execute();
                        connection.pstat.close();
                        JOptionPane.showMessageDialog(null, "Insert data berhasil");
                        clear();
                        LoadDataMember();
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null, "Please Fill Data Correctly !!");
                    }
                }
            }
        });
        txtTotalPayment.addKeyListener(new  KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        txtPayment.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try{
                    String strTotalPay = txtPayment.getText().replaceAll("\\,","");
                    double dblTotalPay = Double.parseDouble(strTotalPay);
                    DecimalFormat df = new DecimalFormat("#,###,###");
                    if(dblTotalPay > 999){
                        txtPayment.setText(df.format(dblTotalPay));
                    }
                }catch (Exception ex){
                    //JOptionPane.showMessageDialog(null,"error total payment"+e);
                }
            }
        });
    }
    public void saveIDcmbMemberType(){
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT id_member FROM tblTipeMember WHERE jenis_member LIKE '%" +
                    String.valueOf(cmbMemberType.getSelectedItem())+"%'";
            connection.result = connection.stat.executeQuery(sql);

            while(connection.result.next()){
                idtipemember = (connection.result.getString("id_member"));
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error while load member type id"+e);
        }
    }
    public void AutoID(){
        try{
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT * FROM tblTransaksiMember ORDER BY id_member DESC";
            connection.result = connection.stat.executeQuery(sql);

            if(connection.result.next()){
                idMember = connection.result.getString("id_member").substring(3);

                String temp = "" + (Integer.parseInt(idMember) + 1);
                String nol = "";

                if(temp.length() == 1) nol = "000";
                else if (temp.length() == 2) nol = "00";
                else if (temp.length() == 3) nol = "0";
                else if (temp.length() == 4) nol = "";

                idMember = "MB" + nol + temp;
            }else {
                idMember = "MB0001";
            }
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null,"error while create new otomatis id"+ex);
        }
    }
    public void fillcmbCust(){
        try {
            connection.stat = connection.conn.createStatement();
            String sqlCategory = "SELECT id_customer FROM tblCustomer WHERE status=1";
            connection.result = connection.stat.executeQuery(sqlCategory);
            while (connection.result.next()) {
                cmbCustomerID.addItem(connection.result.getString("id_customer"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Error while load data Customer: " + ex);
        }
    }
    public void fillTypeMember(){
        try {
            connection.stat = connection.conn.createStatement();
            String sqlCategory = "SELECT jenis_member FROM tblTipeMember WHERE status=1";
            connection.result = connection.stat.executeQuery(sqlCategory);
            while (connection.result.next()) {
                cmbMemberType.addItem(connection.result.getString("jenis_member"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Error while load data Member Type: " + ex);
        }
    }
    public void addColumn(){
        try{
            model.addColumn("Member ID");
            model.addColumn("Customer Name");
            model.addColumn("Member Type");
            model.addColumn("Expired Date");
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Error while add column header"+ex);
        }
    }
    public void LoadDataMember(){
        try{
            model.getDataVector().removeAllElements();
            model.fireTableDataChanged();

            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadMember ";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                Object[] obj = new Object[4];
                obj[0] = connection.result.getString("id_member");
                obj[1] = connection.result.getString("nama");
                obj[2] = connection.result.getString("jenis_member");
                obj[3] = connection.result.getString("tgl_expired");

                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "An error occured while loading data : "+e);
        }
    }
    public void clear(){
//        cmbMemberType.setSelectedItem(null);
//        cmbCustomerID.setSelectedIndex(0);
        txtRegisDate.setText(sdf.format(new Date()));
        txtExpiredDate.setText(null);
        txtTotalPayment.setText(null);
        txtPayment.setText(null);

    }
    private String formatrupiah(int value){
        DecimalFormat formater = new DecimalFormat("#,###,###");
        DecimalFormatSymbols symbol = formater.getDecimalFormatSymbols();
        symbol.setMonetaryDecimalSeparator(',');
        symbol.setGroupingSeparator(',');
        formater.setDecimalFormatSymbols(symbol);
        return  formater.format(value);
    }
}