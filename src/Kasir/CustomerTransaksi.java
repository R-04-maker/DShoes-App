package Kasir;

import DBConnect.ConnectionDB;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static java.lang.Double.parseDouble;


public class CustomerTransaksi {
    private JButton Tombol_Refresh;
    private JTextField Textbox_Pencarian;
    private JButton Tombol_Pencarian;
    private JComboBox Combobox_Filter;
    private JLabel Label_Gambar;
    private JTextField Textbox_ID;
    private JTextField Textbox_Nama_Barang;
    private JTextField Textbox_Stock;
    private JTextField Textbox_Harga;
    private JButton Tombol_Tambah_Pesanan;
    private JButton Tombol_Hapus_Pesanan;
    private JTable Tabel_Data;
    private JTextField Textbox_Tunai;
    private JTextField Label_Total;
    private JLabel Label_Tanggal;
    private JButton Tombol_Batalkan;
    private JTable Tabel_Item;
    private JRadioButton rbYes;
    private JRadioButton rbNo;
    private JTextField txtSearchMemberID;
    private JButton btSearchIdMember;
    private JTextField txtIdCustomer;
    private JTextField txtCustomerName;
    private JButton btSearchCustName;
    private JTextField Label_MemberDisc;
    private JComboBox cmbPaymentMethod;
    public JPanel JPTransaksiCustomer;
    private JButton Tombol_Bayar;
    private JTextField Textbox_Kembalian;
    private JButton btCheckOut;
    private JTextField Textbox_Warranty;
    private JTextField Textbox_Jumlah_Pesanan;
    private JTextField txtFileName;
    private JTextField txtSearchCustName;
    private JTextField Label_TotalPay;
    private JButton Tombol_Batal_Pesanan;
    private JPanel jptranscust;
    private JPanel Panel_Tengah;
    private JPanel Panel_Template;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Kanan;
    private JPanel Panel_Konten;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Form;
    private JPanel Panel_Gambar;
    private JPanel Pembatas_Gambar_Atas;
    private JPanel Pembatas_Gambar_Bawah;
    private JPanel Pembatas_Gambar_Kanan;
    private JPanel Panel_Konten_Gambar;
    private JPanel Panel_Attribut;
    private JPanel Pembatas_Attribut_Atas;
    private JPanel Pembatas_Attribut_Tengah;
    private JPanel Panel_Tabel;
    private JPanel Panel_Konten2;
    private JPanel Panel_Kontrol2;
    private JPanel Panel_Attribut2;
    private JPanel Pembatas_Tengah_Attribut2;
    private JPanel Panel_Tabel2;

    //table
    DefaultTableModel model1 = new DefaultTableModel(); //table buat data sepatu
    DefaultTableModel model2 = new DefaultTableModel();  //table buat data item transaksi

    //connection database
    ConnectionDB connection = new ConnectionDB();

    //tanggal sekarang
    java.util.Date dateNow = new java.util.Date();
    JDateChooser datechose = new JDateChooser();

    //variabel global
    String tglexpired;
    String idTrs;
    String idMember,namaMember;
    String idCust;
    String idSupplier;
    String idPegawai;
    String idSepatu;
    String idJnsPembayaran;
    String jmlhSepatu;
    double totalBeli;
    String tglTrs;
    double totalTrs;
    double tunai;
    double diskon;
    double kembalian;
    double hargaTemp = 0.0;
    double itemdiskon;
    String tglExpGrs;
    String selectedImagePath = "";
    String image;
    String gambar = "NULL";
    Date expired,now;
    String disc = "0";
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();

    /*------------------------------------------------------------------------*/
    //--------------------DATA-------------------------//

    //Menampilkan table data
    public void loadTableData(int indexFilter, String value) {
        model1 = new DefaultTableModel();
        Tabel_Data.setModel(model1);
        addColomn1();
        dataTableData(indexFilter, value);
    }

    //Membuat Colomn Table Data
    public void addColomn1() {
        try {
            model1.addColumn("Shoes ID");
            model1.addColumn("Shoes Name");
            model1.addColumn("Category");
            model1.addColumn("Merk");
            model1.addColumn("Guaranty Type");
            model1.addColumn("Stock");
            model1.addColumn("Purchase Price");
            model1.addColumn("Selling Price");
        } catch (Exception e) {
            System.out.println("Error while add column header" + e);
        }
    }

    //Menampilkan table item
    public void loadTableItem() {
        model2 = new DefaultTableModel();
        Tabel_Item.setModel(model2);
        addColomn2();
        model2.getDataVector().removeAllElements();
        model2.fireTableDataChanged();
    }

    //Membuat Colomn Table Item
    public void addColomn2() {
        try {
            model2.addColumn("Shoes ID");
            model2.addColumn("Shoes Name");
            model2.addColumn("Selling Price");
            model2.addColumn("Quantity");
        } catch (Exception e) {
            System.out.println("Error while add column header" + e);
        }
    }

    //Mengambil data dari database
    public void dataTableData(int indexFilter, String value) {
        model1.getDataVector().removeAllElements();
        model1.fireTableDataChanged();

        //String query = "select id_sepatu,nama_sepatu,stock,jenis_garansi,harga_beli,harga_jual,id_supplier from tblSepatu inner join  tblGaransi on tblSepatu.id_garansi = tblGaransi.id_garansi  \n WHERE tblSepatu.status ='1'";
        String query = "EXEC sp_LoadSepatu";
        String filter = "Filter";

        switch (indexFilter) {
            case 1:
                filter = "t1.nama_sepatu";
                break;
            case 2:
                filter = "t1.stock";
                break;
            case 3:
                filter = "t1.harga_jual";
                break;
            case 4:
                filter = "t3.merk";
                break;
        }

        if (filter != "Filter" && value != "") {
            query = "SELECT t1.id_sepatu, t1.nama_sepatu, t2.deskripsi_kategori, t3.merk,t4.jenis_garansi ,t1.stock, t1.harga_jual, t1.harga_beli,t1.pathfile FROM tblSepatu AS t1 JOIN " +
                    "tblKategoriSepatu AS t2 ON t1.id_kategori =t2.id_kategori JOIN tblSupplier AS t3 ON t1.id_supplier = t3.id_supplier" +
                    " JOIN tblGaransi AS t4 ON t1.id_garansi = t4.id_garansi" +
                    " WHERE "+filter+" LIKE '%"+value+"%' AND t1.status = '1'";
        }

        try {
            connection.pstat = connection.conn.prepareStatement(query);
            connection.result = connection.pstat.executeQuery();
            while (connection.result.next()) {
                Object[] obj = new Object[8];
                obj[0] = connection.result.getString("id_sepatu");
                obj[1] = connection.result.getString("nama_sepatu");
                obj[2] = connection.result.getString("deskripsi_kategori");
                obj[3] = connection.result.getString("merk");
                obj[4] = connection.result.getString("jenis_garansi");
                obj[5] = connection.result.getString("stock");
                obj[6] = formatrupiah(connection.result.getInt("harga_beli"));
                obj[7] = formatrupiah(connection.result.getInt("harga_jual"));
                model1.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception e) {
            System.out.println("An error occured while loading data : " + e);
        }
    }

    /*-----------------------------------------------------------------------*/
    /*------------VALIDASI & FORMATTING-----------------*/

    //Membuat ID Transaksi Customer dengan Otomatis
    public void autoKode() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT * FROM tblTransaksiPenjualan ORDER BY id_transaksi DESC";
            connection.result = connection.stat.executeQuery(sql);

            if (connection.result.next()) {
                idTrs = connection.result.getString("id_transaksi").substring(3);

                String temp = "" + (Integer.parseInt(idTrs) + 1);
                String nol = "";

                if (temp.length() == 1) nol = "000";
                else if (temp.length() == 2) nol = "00";
                else if (temp.length() == 3) nol = "0";
                else if (temp.length() == 4) nol = "";

                idTrs = "TRS" + nol + temp;
            } else {
                idTrs = "TRS0001";
            }
            System.out.println(idTrs);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error while load Transaction ID :" + ex);
        }
    }

    //mengambil data base nama_jenis pembayaran dari tabel jenis pembayaran
    public void fillcombo() {
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT nama_jnsPembayaran FROM tblJenisPembayaran WHERE status = 1";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                cmbPaymentMethod.addItem(connection.result.getString("nama_jnsPembayaran"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error while load combo box payment method :" + e);
        }
    }

    //Menampilkan gambar item
    public void gambar(String id) {
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT pathfile FROM tblSepatu WHERE id_sepatu = '" + id + "'";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()) {
                txtFileName.setText((String) connection.result.getString("pathfile"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        ImageIcon ii = new ImageIcon(txtFileName.getText());
        Image image = ii.getImage().getScaledInstance(Label_Gambar.getWidth(),Label_Gambar.getHeight(),Image.SCALE_SMOOTH);
        Label_Gambar.setIcon(new ImageIcon(image));
    }

    //Mencari index data pesanan
    public int findIndexData(DefaultTableModel model, String id) {

        for (int i = 0; i < model.getRowCount(); i++) {
            if (id.equals(model.getValueAt(i, 0).toString())) {
                return i;
            }
        }
        return -1;
    }

    //Melakukan validasi untuk setiap aksi tambah pesanan , hapus pesanan , dan bayar pesanan
    public String[] validasi(String action) {
        String found = "false";
        String msg = "";
        try{
            if (action == "tambahpesanan") {
                if (Textbox_ID.getText().isEmpty() || Textbox_Jumlah_Pesanan.getText().isEmpty()) {
                    msg = "Fill the data correctly !!";
                } else if (findIndexData(model2, Textbox_ID.getText()) != -1) {
                    msg = "Data has been added !";
                } else if ((Integer.parseInt(Textbox_Stock.getText()) == 1) && (Integer.parseInt(Textbox_Jumlah_Pesanan.getText()) == 1)) {
                    msg = "Shoe stock is not enough!";
                } else if(Integer.parseInt(Textbox_Jumlah_Pesanan.getText()) > Integer.parseInt(Textbox_Stock.getText())){
                    msg = "Shoe stock is not enough!";
                }else if(Textbox_Jumlah_Pesanan.getText().equals("0")){
                    msg = "Fill in the quantity of Shoe !";
                }
            }

            if (action == "hapuspesanan") {
                if (Textbox_ID.getText().isEmpty()) {
                    msg = "Fill the data correctly !!";
                } else if (findIndexData(model2, Textbox_ID.getText()) == -1) {
                    msg = "Data not found!";
                }
            }
            if (msg == "") {
                found = "true";
            }
        }catch (Exception ex){
            System.out.println(""+ex);
        }
        return new String[]{found, msg};
    }

    // validasi formatrupiah
    private String formatrupiah(int value){
        DecimalFormat formater = new DecimalFormat("#,###,###");
        DecimalFormatSymbols symbol = formater.getDecimalFormatSymbols();
        symbol.setMonetaryDecimalSeparator(',');
        symbol.setGroupingSeparator(',');
        formater.setDecimalFormatSymbols(symbol);
        return  formater.format(value);
    }

    /*-----------------------------------------------------------------------*/
    /*-------------------CLEAR------------------------*/

    public void clearKeranjang() {
        Textbox_ID.setText(null);
        Textbox_Nama_Barang.setText(null);
        Textbox_Stock.setText(null);
        Textbox_Harga.setText(null);
        Textbox_Warranty.setText(null);
        Textbox_Jumlah_Pesanan.setText(null);
    }

    public void clearBayar() {
        Label_MemberDisc.setText("0.0");
        Label_Total.setText("0");
        Label_Gambar.setText(null);
        cmbPaymentMethod.setSelectedItem(null);
        Textbox_Tunai.setText(null);
        Textbox_Kembalian.setText(null);
        model2.setRowCount(0);
    }

    public void clearCheckOut() {
        Label_MemberDisc.setText("0");
        Label_Total.setText("0");
        Label_TotalPay.setText("0");
    }

    /*-----------------------------------------------------------------------*/


    public CustomerTransaksi(String[] value) {
        loadTableData(Combobox_Filter.getSelectedIndex(), Textbox_Pencarian.getText());
        addColomn2();
        Tabel_Item.setModel(model2);
        fillcombo();
        Label_Tanggal.setText(formatter.format(calendar.getTime()));
        Label_Gambar.setIcon(null);

        Tombol_Tambah_Pesanan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[]value = validasi("tambahpesanan");
                Boolean valid = Boolean.parseBoolean(value[0]);
                if (valid) {
                    Object[] obj = new Object[4];
                    obj[0] = Textbox_ID.getText();
                    obj[1] = Textbox_Nama_Barang.getText();
                    obj[2] = Textbox_Harga.getText();
                    obj[3] = Textbox_Jumlah_Pesanan.getText();

                    model2.addRow(obj);
                    clearkiri();
                }else {
                    JOptionPane.showMessageDialog(JPTransaksiCustomer,value[1],"Information",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        Tombol_Hapus_Pesanan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] value = validasi("hapuspesanan");
                Boolean valid = Boolean.parseBoolean(value[0]);

                if (valid) {
                    model2.removeRow(findIndexData(model2, Textbox_ID.getText()));
                } else {
                    JOptionPane.showMessageDialog(JPTransaksiCustomer, value[1], "Information", JOptionPane.WARNING_MESSAGE);
                }
                clearKeranjang();
            }
        });
        Tombol_Pencarian.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTableData(Combobox_Filter.getSelectedIndex(), Textbox_Pencarian.getText());
            }
        });
        Tombol_Refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTableData(0,"");
                Textbox_Pencarian.setText("");
            }
        });
        btSearchIdMember.addActionListener(new  ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String temp = txtSearchMemberID.getText();
                try {
                    now = new Date();
                    connection.stat = connection.conn.createStatement();
                    String query = "EXEC sp_CariMemberIDTrs @id_member='"+temp+"'";
                    connection.result = connection.stat.executeQuery(query);
                    while(connection.result.next()){
                        Object[] obj = new Object[4];
                        obj[0] = connection.result.getString("tgl_expired");
                        obj[1] = connection.result.getString("id_customer");
                        obj[2] = connection.result.getString("nama");
                        obj[3] = connection.result.getString("diskon");
                        tglexpired = (String) obj[0];
                        idMember = (String) obj[1];
                        namaMember = (String) obj[2];
                        disc = (String) obj[3];
                    }
                    expired = formatter.parse(tglexpired);
                    if(expired.compareTo(now) == -1 ){
                        JOptionPane.showMessageDialog(null, "Member has expired since "+formatter.format(expired)+"");
                        txtCustomerName.setText("");
                        txtIdCustomer.setText("");
                        disc = "0";
                    }else {
                        txtIdCustomer.setText(idMember);
                        txtCustomerName.setText(namaMember);
                    }
                    connection.stat.close();
                    connection.pstat.close();
                } catch (Exception ex) {
                    System.out.println("Error while search member: " + ex);
                }
            }
        });
        btSearchCustName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    connection.stat = connection.conn.createStatement();
                    String query = "SELECT * FROM tblCustomer WHERE  nama LIKE '%" + txtSearchCustName.getText() + "%'";
                    connection.result = connection.stat.executeQuery(query);
                    if (connection.result.next()) {
                        txtIdCustomer.setText(connection.result.getString("id_customer"));
                        txtCustomerName.setText(connection.result.getString("nama"));
                        Label_MemberDisc.setText("0");
                        diskon = 0;
                    } else {
                        JOptionPane.showMessageDialog(null, "Customer Data not found!");
                    }
                } catch (Exception ex) {
                    System.out.println("Error while search Customer " + ex);
                }
            }
        });
        btCheckOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    double temp = 0.0;
                    String harga;
                    double hrgjual = 0.0;
                    int i = Tabel_Item.getModel().getRowCount();
                    if (Objects.equals(txtIdCustomer.getText(),"") || Objects.equals(txtCustomerName.getText(),"")){
                        JOptionPane.showMessageDialog(null, "Fill the data Correctly!!", "Information",
                                JOptionPane.WARNING_MESSAGE);
                        clearCheckOut();
                    } else {
                        if (i == -1) { //jika tidak ada baris terseleksi
                            return;
                        } else {
                            //mengetahui berapa banyak baris tabelBuku di layar
                            int j = Tabel_Item.getModel().getRowCount();
                            diskon = parseDouble(disc);
                            //menghitung Total = sum of (harga*jumlah)
                            for (int k = 0; k < i; k++) {
                                harga = (String) model2.getValueAt(k,2);
                                hrgjual = Double.parseDouble(harga.replace(",",""));
                                temp = (parseDouble((String) model2.getValueAt(k, 3))) * hrgjual;
                                System.out.println("temp "+temp );
                                hargaTemp = temp + hargaTemp;
                                totalBeli = hargaTemp;
                                System.out.println("hargatemp : "+hargaTemp);
                                System.out.println("diskon : "+diskon);
                                System.out.println("total beli : "+totalBeli);
                            }
                            itemdiskon = (diskon/100)*totalBeli;
                            totalTrs = totalBeli - itemdiskon;
                            System.out.println("diskon Akhir : "+itemdiskon);
                            System.out.println("total beli Akhir: "+totalBeli);
                            System.out.println("total beli Akhir: "+totalTrs);

                            DecimalFormat df = new DecimalFormat("#,###,###");
                            if (totalTrs > 999) {
                                Label_TotalPay.setText(String.valueOf(df.format(totalTrs)));
                            }
                            if (totalBeli > 999) {
                                Label_Total.setText(String.valueOf(df.format(totalBeli)));
                            }
                            if (itemdiskon > 999) {
                                Label_MemberDisc.setText(String.valueOf(df.format(itemdiskon)));
                            }
                            totalBeli = 0.0;
                        }
                    }
                }catch (Exception ex){
                    System.out.println(""+ex);
                }
            }
        });
        Tombol_Bayar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int j = Tabel_Item.getModel().getRowCount();
                if (Textbox_Tunai.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Fill the data correctly ! ", "Information",
                            JOptionPane.WARNING_MESSAGE);
                } else if (parseDouble(Textbox_Tunai.getText().replace(",","")) < parseDouble(Label_TotalPay.getText().replace(",",""))) {
                    JOptionPane.showMessageDialog(null, "Not enough money !", "Information", JOptionPane.WARNING_MESSAGE);
                    Textbox_Tunai.setText("0");
                    Textbox_Kembalian.setText("0");
                } else {
                    Double totalBeli = (parseDouble(Label_TotalPay.getText().replace(",","")));
                    idJnsPembayaran = (String) cmbPaymentMethod.getSelectedItem();
                    tglTrs = Label_Tanggal.getText();
                    idCust = txtIdCustomer.getText();
                    try {

                        connection.stat = connection.conn.createStatement();
                        String query = "SELECT id_jnsPembayaran FROM tblJenisPembayaran WHERE status = 1 AND nama_jnsPembayaran=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1,idJnsPembayaran);
                        connection.result = connection.pstat.executeQuery();

                        while (connection.result.next()) {
                            idJnsPembayaran = (connection.result.getString("id_jnsPembayaran"));
                        }
                        connection.stat.close();
                        connection.result.close();

                        connection.stat = connection.conn.createStatement();
                        //INSERT ke tabel master
                        String sql2 = "INSERT INTO tblTransaksiPenjualan VALUES (?,?,?,?,?,?,?)";
                        connection.pstat = connection.conn.prepareStatement(sql2);
                        autoKode();
                        connection.pstat.setString(1, idTrs);
                        connection.pstat.setString(2, idCust);
                        connection.pstat.setString(3, value[5]);
                        connection.pstat.setString(4, tglTrs);
                        connection.pstat.setString(5, String.valueOf(diskon));
                        connection.pstat.setString(6, (String.valueOf(totalBeli)));
                        connection.pstat.setString(7, idJnsPembayaran);
                        connection.pstat.executeUpdate(); //insert ke tabel master

                        //Input Ke detail Transaksi
                        for (int k = 0; k < j; k++) {
                            //String idTrs  = (String) model2.getValueAt(k, 0);
                            String idSepatu = (String) model2.getValueAt(k, 0);
                            //String idTrs = (String) model1.getValueAt(k, 0);;
                            String jmlhSepatu = (String) model2.getValueAt(k, 3);

                            String sql3 = "INSERT INTO tblDetilTransaksiPenjualan VALUES (?,?,?,?)";
                            connection.pstat = connection.conn.prepareStatement(sql3);
                            connection.pstat.setString(1, idTrs);
                            connection.pstat.setString(2, idSepatu); //id sepatu
                            connection.pstat.setString(3, (String.valueOf(jmlhSepatu))); // jumlah sepatu
                            connection.pstat.setString(4,"2");
                            connection.pstat.executeUpdate(); //update tabel detil transaksi

                        }
                    } catch (SQLException ex) {
                        System.out.println(ex);
                    }
                    JOptionPane.showMessageDialog(null, "Data saved successfully with ID " + idTrs, "Customer Transaction",
                            JOptionPane.INFORMATION_MESSAGE);
                    clearBayar();
                    clearkiri();
                    clearkanan();
                    loadTableData(0,"");
                }
            }
        });
        Tombol_Batalkan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearkanan();
                diskon = 0.0;
            }
        });
        Tabel_Data.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = Tabel_Data.getSelectedRow();
                Textbox_ID.setText((String) model1.getValueAt(i, 0));
                Textbox_Nama_Barang.setText((String) model1.getValueAt(i, 1));
                Textbox_Stock.setText((String) model1.getValueAt(i, 5));
                Textbox_Harga.setText((String) model1.getValueAt(i, 7));
                Textbox_Warranty.setText((String) model1.getValueAt(i, 4));
                gambar(Textbox_ID.getText());
            }
        });
        Tabel_Item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = Tabel_Item.getSelectedRow();
                Textbox_ID.setText((String) model2.getValueAt(i, 0));
                Textbox_Nama_Barang.setText((String) model1.getValueAt(i, 1));
                Textbox_Stock.setText((String) model1.getValueAt(i, 2));
                Textbox_Harga.setText((String) model1.getValueAt(i, 5));
                Textbox_Warranty.setText((String) model1.getValueAt(i, 3));
                Textbox_Jumlah_Pesanan.setText(model2.getValueAt(i, 3).toString());
            }
        });
        rbYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rbNo.setSelected(false);
                txtSearchCustName.setEnabled(false);
                txtSearchMemberID.setEnabled(true);
                btSearchIdMember.setEnabled(true);
                btSearchCustName.setEnabled(false);
            }
        });
        rbNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rbYes.setSelected(false);
                txtSearchMemberID.setEnabled(false);
                txtSearchCustName.setEnabled(true);
                btSearchIdMember.setEnabled(false);
                btSearchCustName.setEnabled(true);
            }
        });

        Tombol_Batal_Pesanan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearkiri();
            }
        });
        Textbox_Tunai.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try{
                    String sbyr = Textbox_Tunai.getText().replaceAll("\\,", "");
                    String hartot = Label_TotalPay.getText().replaceAll("\\,", "");
                    long dblByr = Long.parseLong(sbyr);
                    long dblhartot = Long.parseLong(hartot);
                    long kembalian =  dblByr - dblhartot;
                    DecimalFormat df = new DecimalFormat("#,###,###");
                    if (dblByr > 999 || kembalian > 999) {
                        Textbox_Tunai.setText(df.format(dblByr));
                        Textbox_Kembalian.setText(df.format(kembalian));
                    }
                    System.out.println(""+kembalian);
                }catch (Exception ex){

                }
            }
        });
        Textbox_Jumlah_Pesanan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
    }
    public double RptoDOuble(String Price) throws ParseException {
        Locale myIndonesianLocale = new Locale("in", "ID");
        Number number = NumberFormat.getCurrencyInstance(myIndonesianLocale).parse(Price);
        double nilai = number.doubleValue();
        String temp = String.format("%.2f", nilai);
        double dblFormat = Double.parseDouble(temp);
        return dblFormat;
    }
    public String convertRupiah(double dbPrice)throws ParseException {
        Locale localId = new Locale("in", "ID");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(localId);
        String strFormat = formatter.format(dbPrice);
        return strFormat;
    }
    public void clearkanan(){
        Label_Total.setText("0");
        Label_MemberDisc.setText("0");
        Label_TotalPay.setText("0");
        txtIdCustomer.setText(null);
        txtCustomerName.setText(null);
        txtSearchMemberID.setText(null);
        txtSearchCustName.setText(null);
        rbYes.setSelected(false);
        rbNo.setSelected(false);
        btSearchCustName.setEnabled(false);
        btSearchIdMember.setEnabled(false);
        DefaultTableModel model = (DefaultTableModel) Tabel_Item.getModel();
        model.setRowCount(0);
    }
    public void clearkiri(){
        Textbox_ID.setText(null);
        Textbox_Nama_Barang.setText(null);
        Textbox_Stock.setText(null);
        Textbox_Harga.setText(null);
        Textbox_Warranty.setText(null);
        Textbox_Jumlah_Pesanan.setText(null);
        Label_Gambar.setIcon(null);
    }
}