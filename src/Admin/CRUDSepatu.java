package Admin;

import DBConnect.ConnectionDB;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import static java.sql.Types.NULL;

public class CRUDSepatu extends JFrame{
    public JPanel PanelSepatu ;
    private JButton Tombol_Tambah;
    private JButton Tombol_Browse;
    private JLabel Label_Gambar;
    private JTextField txtNama;
    private JTextField txtStock;
    private JComboBox cmbGuaranty;
    private JTable Tabel_Data;
    private JComboBox cmbCategory;
    private JComboBox cmbSupplier;
    private JTextField txtHargaBeli;
    private JTextField txtHargaJual;
    private JTextField Textbox_Pencarian;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnCari;
    private JComboBox cmbWarranty;
    private JButton btRefresh;
    private JTextField txtFileName;
    private JPanel Panel_Template;
    private JPanel Pembatas_Kiri;
    private JPanel Pembatas_Kanan;
    private JPanel Pembatas_Atas;
    private JPanel Pembatas_Bawah;
    private JPanel Panel_Konten;
    private JPanel Panel_Kontrol;
    private JPanel Panel_Form;
    private JPanel Panel_Gambar;
    private JPanel Pembatas_Gambar_Atas;
    private JPanel Pembatas_Gambar_Bawah;
    private JPanel Pembatas_Gambar_Kanan;
    private JPanel Pembatas_Gambar_Kiri;
    private JPanel Panel_Konten_Gambar;
    private JPanel Panel_Attribut;
    private JPanel Panel_Pembatas_Bantuan;
    private JPanel Panel_Tabel;
    ConnectionDB connection = new ConnectionDB();
    JFileChooser jfc = new JFileChooser();
    File f;
    String filepath;
    DefaultTableModel model = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }
    };
    String idCategory;
    String idSepatu;
    String idSupplier;
    String idGaransi;
    int status;
    String merk;
    int stock;
    String harga_beli, harga_jual;
    String selectedImagePath = "";
    String image;
    String gambar = "NULL";

    public void clear(){
        loadData();
        Label_Gambar.setIcon(null);
        txtNama.setText(null);
        txtStock.setText(null);
        txtHargaJual.setText(null);
        txtHargaBeli.setText(null);
        txtFileName.setText(null);
    }

    public CRUDSepatu() {
        Tabel_Data.setModel(model);
        setContentPane(PanelSepatu);
        addColumn();
        loadData();
        tampilCategory();
        tampilMerk();
        tampilWarranty();
        txtFileName.setVisible(false);
        btnDelete.setEnabled(false);
        btnUpdate.setEnabled(false);
//        tampilCategory();
        Tombol_Browse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser browseImageFile = new JFileChooser();
                //Filter extensions
                FileNameExtensionFilter fnef = new FileNameExtensionFilter("IMAGES" , "png","jpg","jpeg");
                browseImageFile.addChoosableFileFilter(fnef);
                int showOpenDialogue = browseImageFile.showOpenDialog(null);

                if (showOpenDialogue == JFileChooser.APPROVE_OPTION){
                    File selectedImageFile = browseImageFile.getSelectedFile();
                    selectedImagePath = selectedImageFile.getAbsolutePath();
                    JOptionPane.showMessageDialog(null, "Insert Image succesfully");
                    //Display image on jlable
                    ImageIcon ii = new ImageIcon(selectedImagePath);
                    //Resize image to fit jlabel
                    Image image = ii.getImage().getScaledInstance(Label_Gambar.getWidth(), Label_Gambar.getHeight(), Image.SCALE_SMOOTH);
                    Label_Gambar.setIcon(new ImageIcon(image));
                    txtFileName.setText(selectedImagePath);
                }
            }
        });
        Tabel_Data.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = Tabel_Data.getSelectedRow();
                idSepatu = model.getValueAt(i,0).toString();
                txtNama.setText((String) model.getValueAt(i,1));
                cmbCategory.setSelectedItem((String) model.getValueAt(i,2));
                cmbSupplier.setSelectedItem((String) model.getValueAt(i,3));
                cmbWarranty.setSelectedItem((String) model.getValueAt(i,4));
                txtStock.setText((String) model.getValueAt(i,5));
                txtHargaBeli.setText((String) model.getValueAt(i,6));
                txtHargaJual.setText((String) model.getValueAt(i,7));
                try{
                    connection.stat = connection.conn.createStatement();
                    String query = "SELECT pathfile FROM tblSepatu WHERE id_sepatu = '"+model.getValueAt(i,0)+"'";
                    connection.result = connection.stat.executeQuery(query);
                    while(connection.result.next()){
                        txtFileName.setText((String) connection.result.getString("pathfile"));
                    }
                    connection.stat.close();
                    connection.result.close();
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null,ex.toString());
                }
                //Display image on jlable
                ImageIcon ii = new ImageIcon(txtFileName.getText());
                //Resize image to fit jlabel
                Image image = ii.getImage().getScaledInstance(Label_Gambar.getWidth(), Label_Gambar.getHeight(), Image.SCALE_SMOOTH);
                Label_Gambar.setIcon(new ImageIcon(image));
                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
            }
        });
        btRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
                Label_Gambar.setIcon(null);
                txtNama.setText(null);
                txtStock.setText(null);
                txtHargaJual.setText(null);
                txtHargaBeli.setText(null);
                txtFileName.setText(null);
                Textbox_Pencarian.setText(null);
                btnSave.setEnabled(true);
                btnDelete.setEnabled(false);
                btnUpdate.setEnabled(false);
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AutoID();
                String.valueOf(cmbCategory.getSelectedItem());
                saveIDcmbCategory();
                String.valueOf(cmbSupplier.getSelectedItem());
                saveIDcmbSupplier();
                String.valueOf(cmbWarranty.getSelectedItem());
                saveIDcmbGaransi();
                status = 1;
                int minimStock = 1;
                image = txtFileName.getText();
                String hargaJual = txtHargaJual.getText();
                String hargaBeli = txtHargaBeli.getText();
                Boolean found = false;
                //validasi jika memasukkan tiap member yang sama
                Object[] obj = new Object[10];
                obj[0] = txtNama.getText();
                int j = Tabel_Data.getModel().getRowCount();
                for(int k = 0; k < j; k++){
                    if(obj[0].equals(model.getValueAt(k,1))){
                        found = true;
                    }
                };
                if(found){
                    JOptionPane.showMessageDialog(null,"Shoes Name already exists!","Information",JOptionPane.INFORMATION_MESSAGE);
                }else if(txtNama.getText().isEmpty() || txtStock.getText().isEmpty() || txtHargaJual.getText().isEmpty() || txtHargaBeli.getText().isEmpty() || txtStock.getText().equals("0")){
                    JOptionPane.showMessageDialog(null,"Fill the data Correctly !","Information",JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    double hrgjual = Double.parseDouble(hargaJual.replace(",",""));
                    double hrgbeli = Double.parseDouble(hargaBeli.replace(",",""));
                    if(hrgjual <= hrgbeli){
                        JOptionPane.showMessageDialog(null,"Selling price must be more than the purchase price !","Information",JOptionPane.INFORMATION_MESSAGE);
                    }
                    else if(txtStock.getText().equals("0")){
                        JOptionPane.showMessageDialog(null,"Stock cannot be empty or zero !","Information",JOptionPane.INFORMATION_MESSAGE);
                    }
                    else if(validasi("save")){
                        try {
                            String query = "EXEC sp_InsertSepatu @id_sepatu=?, @id_kategori=?, @id_supplier=?, @id_garansi=?, @nama_sepatu=?, @stock=?, @harga_beli=?, @harga_jual=?,@pathfile=?,@status=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1,idSepatu);
                            connection.pstat.setString(2,idCategory);
                            connection.pstat.setString(3,idSupplier);
                            connection.pstat.setString(4,idGaransi);
                            connection.pstat.setString(5,txtNama.getText());
                            connection.pstat.setString(6,txtStock.getText());
                            connection.pstat.setString(7,hargaBeli);
                            connection.pstat.setString(8, hargaJual);
                            connection.pstat.setString(9, image);
                            connection.pstat.setInt(10, status);
                            connection.pstat.execute();
                            connection.pstat.close();
                            JOptionPane.showMessageDialog(null, "Data saved successfully!");
                            clear();
                        }catch (Exception ex){
                            JOptionPane.showMessageDialog(null,"Error while saving data "+ex);
                        }
                    }else {
                        JOptionPane.showMessageDialog(PanelSepatu,"Failed to save data","Information",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        txtHargaBeli.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try{
                    String sbyr = txtHargaBeli.getText().replaceAll("\\,", "");
                    double dblByr = Double.parseDouble(sbyr);
                    DecimalFormat df = new DecimalFormat("#,###,###");
                    if (dblByr > 999) {
                        txtHargaBeli.setText(df.format(dblByr));
                    }
                }catch (Exception ex){

                }
            }
        });
        txtHargaJual.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try{
                    String sbyr = txtHargaJual.getText().replaceAll("\\,", "");
                    double dblByr = Double.parseDouble(sbyr);
                    DecimalFormat df = new DecimalFormat("#,###,###");
                    if (dblByr > 999) {
                        txtHargaJual.setText(df.format(dblByr));
                    }
                }catch (Exception ex){

                }
            }
        });
        txtStock.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
        txtHargaBeli.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
        txtHargaJual.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opsi;
                try {
                    opsi = JOptionPane.showConfirmDialog(null, "Are you sure delete this data?",
                            "Confirmation", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (opsi != 0) {
                        JOptionPane.showMessageDialog(null, "Data failed to delete");
                    } else {
                        int i = Tabel_Data.getSelectedRow();
                        if (i == -1) return;
                        idSepatu = String.valueOf(model.getValueAt(i, 0));

                        String query = "UPDATE tblSepatu SET status='0' WHERE id_sepatu=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, idSepatu);

                        connection.stat.close();
                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        clear();
                        JOptionPane.showMessageDialog(null, "Data deleted successfully!");
                        loadData();
                        btnUpdate.setEnabled(false);
                        btnDelete.setEnabled(false);
                        btnSave.setEnabled(true);
                    }
                } catch(Exception e1){
                    JOptionPane.showMessageDialog(null,"an error occurred while deleting data into the database.\n" + e1);
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String.valueOf(cmbCategory.getSelectedItem());
                    saveIDcmbCategory();
                    String.valueOf(cmbSupplier.getSelectedItem());
                    saveIDcmbSupplier();
                    String.valueOf(cmbWarranty.getSelectedItem());
                    saveIDcmbGaransi();
                    String hargajual = txtHargaJual.getText();
                    String hargabeli = txtHargaBeli.getText();
                    double jual = Double.parseDouble(hargajual.replace(",",""));
                    double beli = Double.parseDouble(hargabeli.replace(",",""));
                    if(jual <= beli){
                        JOptionPane.showMessageDialog(null,"Selling price must be more than the purchase price !","Information",JOptionPane.INFORMATION_MESSAGE);
                    }else if(txtStock.getText().equals("0")){
                        JOptionPane.showMessageDialog(null,"Stock cannot be empty or zero !","Information",JOptionPane.INFORMATION_MESSAGE);
                    }
                    else if(validasi("update")){
                        try {
                            int i = Tabel_Data.getSelectedRow();
                            idSepatu = String.valueOf(model.getValueAt(i, 0));
                            if(i==-1) return;
                            String query = "EXEC sp_UpdateSepatu @id_sepatu=?,@id_kategori=?,@id_supplier=?,@id_garansi=?,@nama_sepatu=?,@stock=?,@harga_beli=?,@harga_jual=?,@pathfile=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1,idSepatu);
                            connection.pstat.setString(2,idCategory);
                            connection.pstat.setString(3,idSupplier);
                            connection.pstat.setString(4,idGaransi);
                            connection.pstat.setString(5,txtNama.getText());
                            connection.pstat.setString(6,txtStock.getText());
                            connection.pstat.setString(7,txtHargaBeli.getText());
                            connection.pstat.setString(8,txtHargaJual.getText());
                            connection.pstat.setString(9,txtFileName.getText());
                            try{
                                connection.pstat.execute();
                                connection.pstat.close();
                                JOptionPane.showMessageDialog(null, "Data updated successfully!");
                            }catch (Exception exx){
                                JOptionPane.showMessageDialog(null,"Error Execute sql query"+exx);
                            }
                            btnUpdate.setEnabled(false);
                            btnDelete.setEnabled(false);
                            btnSave.setEnabled(true);
                            clear();
                            loadData();
                        }catch (Exception ex){
                            JOptionPane.showMessageDialog(null,"Error while update data :"+ex);
                        }
                    }else {
                        JOptionPane.showMessageDialog(null,"Validation Failed");
                    }
                }catch (Exception ex){
                    System.out.println(""+ex);
                }
            }
        });
        btnCari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Tabel_Data.setModel(model);
                datatable();
            }
        });
        btnCari.addMouseListener(new MouseAdapter() {
        });
    }
    public void datatable(){
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
                obj[8] = connection.result.getString("pathfile");
                model.addRow(obj);
            }
            if (model.getRowCount() == 0){
                JOptionPane.showMessageDialog(null, "Data not found");
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Error while load search data "+ex);
        }
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
                Object[] obj = new Object[9];
                obj[0] = connection.result.getString("id_sepatu");
                obj[1] = connection.result.getString("nama_sepatu");
                obj[2] = connection.result.getString("deskripsi_kategori");
                obj[3] = connection.result.getString("merk");
                obj[4] = connection.result.getString("jenis_garansi");
                obj[5] = connection.result.getString("stock");
                obj[6] = formatrupiah(connection.result.getInt("harga_beli"));
                obj[7] = formatrupiah(connection.result.getInt("harga_jual"));
                obj[8] = connection.result.getString("pathfile");

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
                cmbCategory.addItem(connection.result.getString("deskripsi_kategori"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,"Error while load data category: " + ex);
        }
    }
    public void tampilMerk(){
        try{
            connection.stat = connection.conn.createStatement();
            String merk = "SELECT merk FROM tblSupplier WHERE status=1";
            connection.result = connection.stat.executeQuery(merk);
            while (connection.result.next()) {
                cmbSupplier.addItem(connection.result.getString("merk"));
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Error while load data merk: " + ex);
        }
    }
    public void tampilWarranty(){
        try{
            connection.stat = connection.conn.createStatement();
            String warranty = "SELECT jenis_garansi FROM tblGaransi WHERE status=1";
            connection.result = connection.stat.executeQuery(warranty);
            while (connection.result.next()) {
                cmbWarranty.addItem(connection.result.getString("jenis_garansi"));
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Error while load data Warranty: " + ex);
        }
    }
    //Melakukan validasi untuk setiap aksi create , update , delete yang terjadi
    public boolean validasi(String action){
        Boolean[] check = {false,false,false,false,false};

        if(!txtNama.getText().isEmpty()){
            check[0] = true;
        }

        if(!txtStock.getText().isEmpty()){
            check[1] = true;
        }

        if(!txtHargaJual.getText().isEmpty()){
            check[2] = true;
        }

        if(!txtHargaBeli.getText().isEmpty()){
            check[3] = true;
        }

        if(Label_Gambar.getIcon() != null){
            check[4] = true;
        }

        if(action == "save" && check[1] && check[2] && check[3] && check[4]){
            return true;
        }

        if(action == "update" && check[0] && check[1] && check[2] && check[3] && check[4]){
            return true;
        }

        if(action == "delete" && check[0]){
            return true;
        }

        return false;
    }
    public void saveIDcmbCategory(){
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT id_kategori FROM tblKategoriSepatu WHERE deskripsi_kategori LIKE '%" +
                    String.valueOf(cmbCategory.getSelectedItem())+"%'";
            connection.result = connection.stat.executeQuery(sql);

            while(connection.result.next()){
                idCategory = (connection.result.getString("id_kategori"));
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Terjadi error saat load data id category"+e);
        }
    }
    public void saveIDcmbSupplier(){
        try {
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT id_supplier FROM tblSupplier WHERE merk LIKE '%" +
                    String.valueOf(cmbSupplier.getSelectedItem())+"%'";
            connection.result = connection.stat.executeQuery(sql1);

            while(connection.result.next()){
                idSupplier = (connection.result.getString("id_supplier"));
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Terjadi error saat load data id supplier"+e);
        }
    }
    public void saveIDcmbGaransi(){
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT id_garansi FROM tblGaransi WHERE jenis_garansi LIKE '%" +
                    String.valueOf(cmbWarranty.getSelectedItem())+"%'";
            connection.result = connection.stat.executeQuery(sql);

            while(connection.result.next()){
                idGaransi = (connection.result.getString("id_garansi"));
            }
            if(validasi(""))
            connection.stat.close();
            connection.result.close();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Terjadi error saat load data id supplier"+e);
        }
    }
    public void AutoID(){
        try{
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT * FROM tblSepatu ORDER BY id_sepatu DESC";
            connection.result = connection.stat.executeQuery(sql);

            if(connection.result.next()){
                idSepatu = connection.result.getString("id_sepatu").substring(3);

                String temp = "" + (Integer.parseInt(idSepatu) + 1);
                String nol = "";

                if(temp.length() == 1) nol = "000";
                else if (temp.length() == 2) nol = "00";
                else if (temp.length() == 3) nol = "0";
                else if (temp.length() == 4) nol = "";

                idSepatu = "SP" + nol + temp;
            }else {
                idSepatu = "SP0001";
            }
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null,"error while create new otomatis id"+ex);
        }
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
    public double RptoDOuble(String Price) throws ParseException {
        Locale myIndonesianLocale = new Locale("in", "ID");
        Number number = NumberFormat.getCurrencyInstance(myIndonesianLocale).parse(Price);
        double nilai = number.doubleValue();
        String temp = String.format("%.2f", nilai);
        double dblFormat = Double.parseDouble(temp);
        return dblFormat;
    }
}
