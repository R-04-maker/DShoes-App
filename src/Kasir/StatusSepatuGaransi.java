package Kasir;

import DBConnect.ConnectionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class StatusSepatuGaransi {
    public JPanel JPStatus;
    private JTable table1;
    private JButton endButton;
    DefaultTableModel model = new DefaultTableModel();
    ConnectionDB connection = new ConnectionDB();
    String idtrsGr,idTrs,idSepatu;

    public StatusSepatuGaransi(){
        table1.setModel(model);
        addColoumn();
        LoadData();
        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //sp_UpdateStatusGaransi
                try{
                    String query = "EXEC sp_UpdateStatusGaransi @id_TrsGaransi=?,@id_Transaksi=?,@id_Sepatu=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1,idtrsGr);
                    connection.pstat.setString(2,idTrs);
                    connection.pstat.setString(3,idSepatu);
                    connection.pstat.execute();
                    connection.pstat.close();
                    JOptionPane.showMessageDialog(null, "Data updated successfully!");
                    LoadData();
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null,"Error while update data "+ex);
                }
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = table1.getSelectedRow();
                idtrsGr = model.getValueAt(i,0).toString();
                idTrs = model.getValueAt(i,1).toString();
                idSepatu = model.getValueAt(i,2).toString();
            }
        });
    }
    public void LoadData(){
        try{
            model.getDataVector().removeAllElements();
            model.fireTableDataChanged();

            String query = "EXEC sp_LoadGaransiStatus";
            connection.pstat = connection.conn.prepareStatement(query);
            connection.result = connection.pstat.executeQuery();
            while(connection.result.next()){
                Object[] obj = new Object[5];
                obj[0] = connection.result.getString("id_trsGaransi");
                obj[1] = connection.result.getString("id_transaksi");
                obj[2] = connection.result.getString("id_sepatu");
                obj[3] = connection.result.getDate("tgl_masuk");
                obj[4] = connection.result.getDate("tgl_keluar");
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"An error occured while loading data : "+e);
        }
    }
    public void addColoumn(){
        model.addColumn("Guaranty ID");
        model.addColumn("Transaction ID");
        model.addColumn("Shoe ID");
        model.addColumn("Date In");
        model.addColumn("Date Out");
    }
}
