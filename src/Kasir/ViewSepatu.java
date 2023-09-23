package Kasir;

import DBConnect.ConnectionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class ViewSepatu {
    private JPanel PanelForm;
    private JTextField Textbox_Pencarian;
    private JComboBox cbCategory;
    private JTable Table_data;
    private JButton cancelButton;
    private JButton searchButton;
    public JPanel ViewSepatu;

    DefaultTableModel model = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }
    };
    ConnectionDB connection = new ConnectionDB();
    String category;

    public ViewSepatu() {
        Table_data.setModel(model);
        addColumn();
        loadData();
        tampilCategory();
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.getDataVector().removeAllElements();
                model.fireTableDataChanged();
                String search = Textbox_Pencarian.getText();
                String query = "EXEC sp_CariSepatu @nama_sepatu='"+Textbox_Pencarian.getText()+"'";
                try{
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.result = connection.pstat.executeQuery();
                    while(connection.result.next()){
                        Object[] obj = new Object[9];
                        obj[0] = connection.result.getString("id_sepatu");
                        obj[1] = connection.result.getString("nama_sepatu");
                        obj[2] = connection.result.getString("deskripsi_kategori");
                        obj[3] = connection.result.getString("merk");
                        obj[4] = connection.result.getString("jenis_garansi");
                        obj[5] = connection.result.getString("stock");
                        obj[6] = formatrupiah(connection.result.getInt("harga_beli"));
                        obj[7] = formatrupiah(connection.result.getInt("harga_jual"));
                        model.addRow(obj);
                    }
                    if (model.getRowCount() == 0){
                        JOptionPane.showMessageDialog(null, "Data not found");
                        loadData();
                    }
                    connection.stat.close();
                    connection.result.close();
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null,"Error while load search data "+ex);
                }
            }
        });
        cbCategory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    model.getDataVector().removeAllElements();
                    model.fireTableDataChanged();
                    category = (String) cbCategory.getSelectedItem();
                    String query = "EXEC sp_CariSepatubyKategori @category='"+category+"'";
                    connection.stat = connection.conn.createStatement();
                    connection.result = connection.stat.executeQuery(query);
                    while (connection.result.next()){
                        Object[] obj = new Object[9];
                        obj[0] = connection.result.getString("id_sepatu");
                        obj[1] = connection.result.getString("nama_sepatu");
                        obj[2] = connection.result.getString("deskripsi_kategori");
                        obj[3] = connection.result.getString("merk");
                        obj[4] = connection.result.getString("jenis_garansi");
                        obj[5] = connection.result.getString("stock");
                        obj[6] = formatrupiah(connection.result.getInt("harga_beli"));
                        obj[7] = formatrupiah(connection.result.getInt("harga_jual"));

                        model.addRow(obj);
                    }
                    if (model.getRowCount() == 0){
                        JOptionPane.showMessageDialog(null, "Data not found");
                        loadData();
                    }
                    connection.stat.close();
                    connection.result.close();

                }catch(SQLException ex){
                    System.out.println("Erroe : "+ex);
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
                Textbox_Pencarian.setText(null);
            }
        });
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
    public void addColumn(){
        try {
            model.addColumn("Shoes ID");
            model.addColumn("Shoes Name");
            model.addColumn("Category Name");
            model.addColumn("Merk");
            model.addColumn("Warranty Type");
            model.addColumn("Stock");
            model.addColumn("Purchase Price");
            model.addColumn("Selling Price");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Error while add column header"+e);
        }
    }
    public void loadData(){
        try{
            model.getDataVector().removeAllElements();
            model.fireTableDataChanged();

            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadSepatu";
            connection.result = connection.stat.executeQuery(query);
            while (connection.result.next()){
                Object[] obj = new Object[8];
                obj[0] = connection.result.getString("id_sepatu");
                obj[1] = connection.result.getString("nama_sepatu");
                obj[2] = connection.result.getString("deskripsi_kategori");
                obj[3] = connection.result.getString("merk");
                obj[4] = connection.result.getString("jenis_garansi");
                obj[5] = connection.result.getString("stock");
                obj[6] = formatrupiah(connection.result.getInt("harga_beli"));
                obj[7] = formatrupiah(connection.result.getInt("harga_jual"));

                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"An error occured while loading data : "+e);
        }
    }
    public void tampilCategory() {
        try {
            connection.stat = connection.conn.createStatement();
            String sqlCategory = "SELECT deskripsi_kategori FROM tblKategoriSepatu WHERE status=1";
            connection.result = connection.stat.executeQuery(sqlCategory);
            while (connection.result.next()) {
                cbCategory.addItem(connection.result.getString("deskripsi_kategori"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Error while load data category: " + ex);
        }
    }
}
