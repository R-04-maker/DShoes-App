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

public class SupplierStatus {
    public JPanel JPSupplierStatus;
    private JTable table1;
    private JTable table2;
    private JTable table3;
    private JButton button2;
    private JButton arrivedButton;
    DefaultTableModel model1 = new DefaultTableModel();
    DefaultTableModel model2 = new DefaultTableModel();
    DefaultTableModel model3 = new DefaultTableModel();
    ConnectionDB connection = new ConnectionDB();
    String idTrs;

    public SupplierStatus(){
        addColoumn1();
        addColoumn2();
        addColoumn3();
        LoadData1();
        LoadData2();
        table1.setModel(model1);
        table2.setModel(model2);
        table3.setModel(model3);
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = table1.getSelectedRow();
                if(i == -1){
                    return;
                }
                idTrs = (String) model1.getValueAt(i,0);
                model3.getDataVector().removeAllElements();
                model3.fireTableDataChanged();
            try{
                connection.stat = connection.conn.createStatement();
                String query = "EXEC sp_LoadDetilPemb @id_trs='"+idTrs+"'";
                connection.result = connection.stat.executeQuery(query);
                while(connection.result.next()){
                    Object[] obj = new Object[3];
                    obj[0] = connection.result.getString(1);
                    obj[1] = connection.result.getString(2);
                    obj[2] = connection.result.getString(3);
                    model3.addRow(obj);
                }
                connection.stat.close();
                connection.result.close();
            }catch (Exception ex){
                JOptionPane.showMessageDialog(null,"Error load data 3 "+ex);
            }
            }
        });
        arrivedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idSepatu;
                int j = table3.getModel().getRowCount();
                try {
                    connection.stat = connection.conn.createStatement();
                    //INSERT ke tabel master
                    String sql2 = "EXEC sp_UpdateStatusTrsPembelian @id_transaksi=?";
                    connection.pstat = connection.conn.prepareStatement(sql2);
                    connection.pstat.setString(1, idTrs);
                    connection.pstat.executeUpdate();

                    //update Ke detail Transaksi
                    for (int k = 0; k < j; k++) {
                        String idShoes = (String) model3.getValueAt(k, 1);
                        String qty = (String) model3.getValueAt(k, 2);

                        String sql3 = "EXEC sp_UpdateStatusDetil @id_transaksi=?,@id_sepatu=?";
                        connection.pstat = connection.conn.prepareStatement(sql3);
                        connection.pstat.setString(1, idTrs);
                        connection.pstat.setString(2, idShoes);
                        connection.pstat.executeUpdate();

                    }
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                JOptionPane.showMessageDialog(null, "Data berhasil disimpan dengan ID " + idTrs, "Transaksi Penjualan",
                        JOptionPane.INFORMATION_MESSAGE);
                LoadData1();
                LoadData2();
            }
        });
    }
    public void LoadData1(){
        try{
            model1.getDataVector().removeAllElements();
            model1.fireTableDataChanged();

            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadTrsPembOnProggress";
            connection.result = connection.stat.executeQuery(query);
            while(connection.result.next()){
                Object[] obj = new Object[7];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                obj[4] = formatrupiah(connection.result.getInt(6));
                obj[5] = connection.result.getString(7);

                model1.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Error load data 1 "+ex);
        }
    }
    public void LoadData2(){
        try{
            model2.getDataVector().removeAllElements();
            model2.fireTableDataChanged();

            connection.stat = connection.conn.createStatement();
            String query = "EXEC sp_LoadTrsPembArrived";
            connection.result = connection.stat.executeQuery(query);
            while(connection.result.next()){
                Object[] obj = new Object[7];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                obj[4] = connection.result.getString(5);
                obj[5] = formatrupiah(connection.result.getInt(6));
                obj[6] = connection.result.getString(7);

                model2.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Error load data 2 "+ex);
        }
    }

    public void addColoumn1(){
        model1.addColumn("Transaction ID");
        model1.addColumn("Supplier Name");
        model1.addColumn("Employee Name");
        model1.addColumn("Order Date");
        model1.addColumn("Total Price");
        model1.addColumn("Status");
    }
    public void addColoumn2(){
        model2.addColumn("Transaction ID");
        model2.addColumn("Supplier Name");
        model2.addColumn("Employee Name");
        model2.addColumn("Order Date");
        model2.addColumn("Arrived Date");
        model2.addColumn("Total Price");
        model2.addColumn("Status");
    }
    public void addColoumn3(){
        model3.addColumn("Transaction ID");
        model3.addColumn("Shoe ID");
        model3.addColumn("Quantity");
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
}
