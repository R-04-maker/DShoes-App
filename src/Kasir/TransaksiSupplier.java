package Kasir;


import javax.swing.*;

//import Admin.CRUDMemberType;
import DBConnect.ConnectionDB;

import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TransaksiSupplier extends JFrame{
    public JPanel TsSupplier;
    private JTable tblItem;
    private JButton btnAdd;
    private JComboBox cbSupplier;
    private JButton btnCancel;
    private JTable tblCart;
    private JTextField txtTotal;
    private JButton btnSave;
    private JButton btnCancel2;
    private JTextField txtItem;
    private JTextField txtStck;
    private JTextField txtQty;
    private JTextField txtID;
    private JButton deleteButton;
    private JTextField txtUang;
    private JTextField txtKembalian;
    private JTextField txtStock;
    private JTextField txtMerk;
    private JComboBox cbShoes;
    private JTextField txtHarga;
    private JTextField txtLastHarga;
    private JTextField txtTgl;
    //private JTextField txtMoney;

    private DefaultTableModel model;
    ConnectionDB connection = new ConnectionDB();
    DefaultTableModel modelcart = new DefaultTableModel();
    //DefaultTableModel modelitem = new DefaultTableModel();
    //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate now = LocalDate.now();
    String Autokode,ID,iditem,kategori,nama,merk,item1,harga,hargatotal,qty,IDTRANS,idkar,date,tgl;

    public TransaksiSupplier(String[] value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        tgl = (String) LocalDate.now().format(formatter);
        tblCart.setModel(modelcart);
        fillcmb();
        autokode();
        addColumn();
        setContentPane(TsSupplier);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        cbShoes.setEnabled(false);

        cbSupplier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cbShoes.removeAllItems();
                nama = (String) cbSupplier.getSelectedItem();
                String query = "SELECT * FROM tblSupplier WHERE nama_supplier = '"+nama+"'";
                try {
                    connection.stat = connection.conn.createStatement();
                    connection.result = connection.stat.executeQuery(query);
                    connection.result.next();
                    ID = connection.result.getString("id_supplier");
                    merk = connection.result.getString("merk");
                    String sql = "SELECT * FROM tblSepatu where id_supplier = '"+ID+"'and status = 1";
                    connection.stat = connection.conn.createStatement();
                    connection.result = connection.stat.executeQuery(sql);
                    while(connection.result.next()){
                        cbShoes.addItem(connection.result.getString("nama_sepatu"));
                        cbShoes.setSelectedIndex(0);
                    }
                    connection.stat.close();
                    connection.result.close();
                    cbSupplier.setEnabled(false);
                    cbShoes.setEnabled(true);

                }catch(SQLException ex){
                    //ex.printStackTrace();
                }
            }
        });
        cbShoes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try{
                    item1 =(String) cbShoes.getSelectedItem();
                    iditem = "";
                    if(!item1.isEmpty()){
                        txtQty.setText(null);
                        txtLastHarga.setText(null);
                    }
                    String query = "SELECT * FROM tblSepatu WHERE nama_sepatu = '"+item1+"'";
                    connection.stat = connection.conn.createStatement();
                    connection.result = connection.stat.executeQuery(query);
                    connection.result.next();
                    txtHarga.setText(convertRupiah(Double.parseDouble(connection.result.getString("harga_beli"))));
                    txtStock.setText(connection.result.getString("stock"));
                    iditem = connection.result.getString("id_sepatu");
                    txtMerk.setText(merk);
                    connection.stat.close();
                    connection.result.close();
                }catch (Exception e1){
                    JOptionPane.showMessageDialog(null, "error cb Shoes" + e1);
                }
            }
        });

        txtQty.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    double qty = Double.parseDouble(txtQty.getText());
                    double sisastock = Double.parseDouble(txtStock.getText());
                    double harga = 0;
                    try {
                        harga = RptoDOuble(txtHarga.getText());
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    if(txtQty.getText().equals("")){
                        txtLastHarga.setText("");
                        return;
                    }else{
                        double tempharga = harga * qty;
                        txtLastHarga.setText(convertRupiah(tempharga));
                    }
                }
            }
        });
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtQty.getText().isEmpty() || txtLastHarga.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null,"Fill the data Correctly !");
                }else {
                    double HT = 0.0;
                    double tempharga = 0;
                    try {
                        tempharga = RptoDOuble(txtHarga.getText());
                    } catch (ParseException ex) {

                    }
                    String harga = convertRupiah(tempharga);
                    int col;
                    try{
                        Object[] item = new Object[6];
                        item[0] = iditem;
                        item[1] = cbShoes.getSelectedItem();
                        item[2] = txtMerk.getText();
                        item[3] = harga;
                        item[4] = txtQty.getText();
                        item[5] = txtLastHarga.getText();

                        if(checkexist(iditem) == true){
                            JOptionPane.showMessageDialog(null, "Data has been added!");
                            txtStock.setText("");
                            txtMerk.setText("");
                            txtHarga.setText("");
                            txtQty.setText("");
                            txtLastHarga.setText("");
                        }else {
                            modelcart.addRow(item);
                            int row = modelcart.getRowCount();
                            for(int i = 0; i<row; i++) {
                                String temp = (String) modelcart.getValueAt(i, 5);
                                double HT1 = RptoDOuble(temp);
                                HT = HT1 + HT;
                            }
                            txtTotal.setText(convertRupiah(HT));
                            cleartxt();
                        }
                    }catch(Exception e1){
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(null, " An error occurred when adding a table"+e1);
                    }
                }
            }
        });
        txtHarga.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                try{
                    String strTotalPay = txtHarga.getText().replaceAll("\\,","");
                    double dblTotalPay = Double.parseDouble(strTotalPay);
                    DecimalFormat df = new DecimalFormat("#,###,###");
                    if(dblTotalPay > 999){
                        txtHarga.setText(df.format(dblTotalPay));
                    }
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null,"error total payment"+e);
                }
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tblCart.getModel().getRowCount();
                System.out.println("ROW :"+row);
                    try{
                        double hartotal = RptoDOuble(txtTotal.getText());
                        hargatotal = String.valueOf(hartotal);
                        try{
                            CallableStatement myCall = connection.conn.prepareCall("{call sp_InsertTrsBeli(?,?,?,?,?,?)}");
                            myCall.setString(1,IDTRANS);
                            myCall.setString(2,ID);
                            myCall.setString(3,value[5]);
                            myCall.setString(4,tgl);
                            myCall.setDouble(5,hartotal);
                            myCall.setString(6,"0");
                            myCall.execute();
                            for(int i = 0; i < row;i++){
                                System.out.println("ROW LOOPING : "+row);
                                String idShoes = (String) tblCart.getValueAt(i,0);
                                System.out.println("ID SHOES KE DETAIL : "+idShoes);
                                String Qty = (String) tblCart.getValueAt(i,4);
                                CallableStatement myCall1 = connection.conn.prepareCall("{call sp_UpdateShoeStockPemb(?,?,?,?)}");
                                myCall1.setString(1,IDTRANS);
                                myCall1.setString(2,idShoes);
                                myCall1.setString(3,Qty);
                                myCall1.setString(4,"0");
                                try{
                                    myCall1.execute();
                                    JOptionPane.showMessageDialog(null, "Transaction completed!!");
                                }catch(Exception e1){
                                    JOptionPane.showMessageDialog(null, "Update stock failed!!"+e1);
                                }
                                clearTable();
                            }
                        }catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }

            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String amountS = "";
                if(tblCart.getSelectedRow() != -1){
                    modelcart.removeRow(tblCart.getSelectedRow());
                    JOptionPane.showMessageDialog(null, "Selected row deleted successfully!");
                }
            }
        });
        tblCart.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                hargatotal = "";
                harga = "";
                qty = "";
                double harga1 = 0.0;
                double HT = 0.0;
                double HT1 = 0.0;
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    int row = modelcart.getRowCount();
                    for(int i = 0;i<row;i++) {
                        harga = (String) modelcart.getValueAt(i, 3);

                        try {
                            harga1 = RptoDOuble(harga);

                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                        qty = (String) modelcart.getValueAt(i, 4);
                        double qty1 = Double.parseDouble(qty);
                        double temp = harga1 * qty1;
                        hargatotal = convertRupiah(temp);
                        modelcart.setValueAt(hargatotal, i, 5);
                        String temp1 = (String) modelcart.getValueAt(i, 5);
                        try {
                            HT1 = RptoDOuble(temp1);
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                        HT = HT1 + HT;
                        txtTotal.setText(convertRupiah(HT));
                    }
                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFormKiri();
            }
        });
        btnCancel2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTable();
                btnAdd.setEnabled(true);
                btnCancel.setEnabled(true);
            }
        });
        txtQty.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
    }

    public void clearTable(){
        try {
            clearFormKiri();
            txtTotal.setText("");
            txtUang.setText("");
            txtKembalian.setText("");

        }catch (Exception e1){

        }
    }

    public void clearFormKiri() {
        try {
            cbSupplier.setSelectedIndex(-1);
            cbSupplier.setEnabled(true);
            txtQty.setText("");
            txtHarga.setText("");
            txtMerk.setText("");
            txtLastHarga.setText("");
            txtStock.setText("");
        }catch (Exception e1){

        }
    }

    public void cleartxt(){
        txtQty.setText("");
        txtHarga.setText("");
        txtMerk.setText("");
        txtLastHarga.setText("");
        txtStock.setText("");
    }

    public void autokode(){
        try{
            String sql = "SELECT * FROM tblTransaksiPembelian ORDER BY id_transaksi desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                Autokode= connection.result.getString("id_transaksi").substring(4);
                String AN = "" + (Integer.parseInt(Autokode) + 1);
                String nol = "";

                if (AN.length() == 1) {
                    nol = "000";
                } else if (AN.length() == 2) {
                    nol = "00";
                } else if (AN.length() == 3) {
                    nol = "0";
                }
                IDTRANS = "TRSP"+ nol + AN;

            }else {
                IDTRANS = "TRSP0001";
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception e1){
            JOptionPane.showMessageDialog(null, "An occured error" + e1);
        }
    }

    public double RptoDOuble(String Price) throws ParseException {
        Locale myIndonesianLocale = new Locale("in", "ID");
        Number number = NumberFormat.getCurrencyInstance(myIndonesianLocale).parse(Price);
        double nilai = number.doubleValue();
        String temp = String.format("%.2f", nilai);
        double dblFormat = Double.parseDouble(temp);
        return dblFormat;
    }

    public String convertRupiah(double dbPrice) {
        Locale localId = new Locale("in", "ID");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(localId);
        String strFormat = formatter.format(dbPrice);
        return strFormat;
    }

    public boolean checkexist(String check){
        int row = modelcart.getRowCount();
        int col;
        String temp;
        for(int i = 0; i<row;i++){
            temp = (String) modelcart.getValueAt(i,0);
            if(temp.equals(check)){
                return true;
            }else {

            }
        }
        return false;
    }

    private String formatrupiah(double tempharga){
        DecimalFormat formater = new DecimalFormat("#,###,###");
        DecimalFormatSymbols symbol = formater.getDecimalFormatSymbols();
        symbol.setMonetaryDecimalSeparator(',');
        symbol.setGroupingSeparator(',');
        formater.setDecimalFormatSymbols(symbol);
        return  formater.format(tempharga);
    }

    public void fillcmb(){
        try {
            String query = "SELECT * FROM tblSupplier";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                cbSupplier.addItem(connection.result.getString("nama_supplier"));
                cbSupplier.setSelectedIndex(-1);
            }
            connection.stat.close();
            connection.result.close();

        }catch(Exception e1){

        }
    }
    public void addColumn() {
        modelcart.addColumn("Shoes ID");
        modelcart.addColumn("Shoes Name");
        modelcart.addColumn("Merk");
        modelcart.addColumn("Price");
        modelcart.addColumn("Quantity");
        modelcart.addColumn("Sub Total");
    }
}