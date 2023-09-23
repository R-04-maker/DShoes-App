package Kasir;

import DBConnect.ConnectionDB;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class KlaimGuaranty {
    public JPanel JPKlaimGuaranty;
    private JPanel PanelKanan;
    private JPanel PanelKiri;
    private JTable table1;
    private JTextField txtSearchID;
    private JPanel jpCalendarDateIn;
    private JPanel jpCalendarDateOut;
    private JButton btSave;
    private JButton btCancel;
    private JButton btGuarantyStatus;
    private JTextField txtShoesID;
    private JTextField txtSoesName;
    private JTextField txtGuarantyDesc;
    private JTextArea txtDesc;
    private JButton searchButton;

    DefaultTableModel model1 = new DefaultTableModel();
    ConnectionDB connection = new ConnectionDB();
    JDateChooser dateChooserDateIn = new JDateChooser();
    JDateChooser dateChooserDateOut = new JDateChooser();
    String id;
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String tgl_transaksi;
    Date trsDate, date;
    java.util.Date dateNow = new java.util.Date();


    public KlaimGuaranty(String[] value){
        jpCalendarDateIn.add(dateChooserDateIn);
        jpCalendarDateOut.add(dateChooserDateOut);
        table1.setModel(model1);
        addColomn();
        dateChooserDateIn.setDate(dateNow);
        dateChooserDateOut.setDate(dateNow);


        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = table1.getSelectedRow();
                if(i == -1){
                    return;
                }
                try {
                    trsDate = formatter.parse(tgl_transaksi);
                    date = new Date();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                System.out.println("TrsDate"+trsDate);
                System.out.println("date"+date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(trsDate);
                cal.add(Calendar.DAY_OF_MONTH,30);
                trsDate = cal.getTime();
                System.out.println("cal"+cal.getTime());
                System.out.println("trsDate"+trsDate);
                System.out.println("date"+date);
                if(trsDate.compareTo(date) == -1){
                    JOptionPane.showMessageDialog(null, "Sorry, your warranty period has expired !");
                }else {
                    txtSearchID.setText((String) model1.getValueAt(i,0));
                    txtShoesID.setText((String) model1.getValueAt(i,1));
                    txtSoesName.setText((String) model1.getValueAt(i,2));
                    txtGuarantyDesc.setText((String) model1.getValueAt(i,3));
                }
            }
        });
        btSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtSearchID.getText().equals("") || txtShoesID.getText().equals("") || txtDesc.getText().equals("") || dateChooserDateIn.getDate().equals("") || dateChooserDateOut.getDate().equals("")){
                    JOptionPane.showMessageDialog(null, "Please, fill in all data!");
                }
                else if(dateChooserDateOut.getDate().compareTo(dateChooserDateIn.getDate()) == -1){
                    System.out.println("date Out" + dateChooserDateOut.getDate());
                    JOptionPane.showMessageDialog(null, "The out date must be after in date !");
                }else {
                    try{
                        connection.stat = connection.conn.createStatement();
                        String sql = "SELECT * FROM tblTransaksiKlaimGaransi ORDER BY id_trsGaransi DESC";
                        connection.result = connection.stat.executeQuery(sql);
                        if(connection.result.next()){
                            id = connection.result.getString("id_trsGaransi").substring(4);
                            System.out.println("id trs garansi "+id);

                            String temp = "" + (Integer.parseInt(id) + 1);
                            String nol = "";

                            if(temp.length() == 1) nol = "000";
                            else if (temp.length() == 2) nol = "00";
                            else if (temp.length() == 3) nol = "0";
                            else if (temp.length() == 4) nol = "";

                            id = "TRSG" + nol + temp;
                        }
                        else {
                            id = "TRSG0001";
                        }
                        String query= "EXEC sp_InsertTRSGaransi @id_trsGaransi=?,@id_transaksi=?,@id_sepatu=?,@tgl_masuk=?,@tgl_keluar=?,@keterangan=?,@status=?,@id_pegawai=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1,id);
                        connection.pstat.setString(2,txtSearchID.getText());
                        connection.pstat.setString(3,txtShoesID.getText());
                        connection.pstat.setString(4,formatter.format(dateChooserDateIn.getDate()));
                        connection.pstat.setString(5,formatter.format(dateChooserDateOut.getDate()));
                        connection.pstat.setString(6,txtDesc.getText());
                        connection.pstat.setString(7,"1");
                        connection.pstat.setString(8,value[5]);

                        connection.stat.close();
                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                        JOptionPane.showMessageDialog(null, "Data saved successfully!");
                        model1.getDataVector().removeAllElements();
                    }catch (Exception ex){
                        System.out.println("error while saving the data :"+ex);
                    }
                }
            }
        });
        btCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clear();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model1.getDataVector().removeAllElements(); //menghapus semua data ditampilkan
                model1.fireTableDataChanged(); //memberitahu data telah kosong
                try{
                    connection.stat = connection.conn.createStatement();
                        String query = "EXEC sp_LoadDetilShoes @id_trs='"+txtSearchID.getText()+"'";
                    connection.result = connection.stat.executeQuery(query);
                    while(connection.result.next()){
                        Object[] obj = new Object[6];
                        obj[0]=connection.result.getString("id_transaksi");
                        obj[1]=connection.result.getString("id_sepatu");
                        obj[2]=connection.result.getString("nama_sepatu");
                        obj[3]=connection.result.getString("jenis_garansi");
                        obj[4]=connection.result.getString("tgl_transaksi");
                        obj[5]=connection.result.getString("status");
                        model1.addRow(obj);
                        tgl_transaksi = (String) obj[4];
                    }
                    connection.stat.close();
                    connection.result.close();
                }catch (Exception ex){
                    System.out.println("error while load data transaction"+ex);
                }
            }
        });
        btGuarantyStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    JPKlaimGuaranty.removeAll();
                    JPKlaimGuaranty.revalidate();
                    JPKlaimGuaranty.repaint();
                    StatusSepatuGaransi form = new StatusSepatuGaransi();
                    form.JPStatus.setVisible(true);
                    JPKlaimGuaranty.revalidate();
                    JPKlaimGuaranty.repaint();
                    JPKlaimGuaranty.add(form.JPStatus);
                }catch (Exception ex){
                    System.out.println(""+ex);
                }
            }
        });
    }
    //Membuat Colomn Table Item
    public void addColomn() {
        try {
            model1.addColumn("Transaction ID");
            model1.addColumn("Shoes ID");
            model1.addColumn("Shoes Name");
            model1.addColumn("Guaranty Description");
        } catch (Exception e) {
            System.out.println("Error while add column header" + e);
        }
    }
    public void Clear(){
        model1.getDataVector().removeAllElements();
        model1.fireTableDataChanged();
        txtSearchID.setText("");
        txtShoesID.setText("");
        txtSoesName.setText("");
        txtGuarantyDesc.setText("");
        txtDesc.setText("");
    }
}
