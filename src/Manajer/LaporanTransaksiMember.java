package Manajer;

import DBConnect.ConnectionDB;
import com.toedter.calendar.JDateChooser;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class LaporanTransaksiMember extends JFrame{
    public JPanel ManajerMenu;
    private JPanel from;
    private JPanel until;
    private JButton printButton;
    private JPanel PanelForm;

    ConnectionDB connection = new ConnectionDB();
    JDateChooser dateawal = new JDateChooser();
    JDateChooser dateakhir = new JDateChooser();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public LaporanTransaksiMember() {
        setContentPane(ManajerMenu);
        until.add(dateakhir);
        from.add(dateawal);
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JasperPrint JPrint;
                boolean validasi = true;
                try{
                    if(dateawal.getDate() == null || dateakhir.getDate() == null){
                        validasi = false;
                    }else {
                        validasi = true;
                    }
                }catch (Exception ex){
                    validasi = false;
                }
                if(validasi == false){
                    JOptionPane.showMessageDialog(null,"Please input date !");
                }else {
                    String dariTgl = formatter.format(dateawal.getDate());
                    String sampaiTgl = formatter.format(dateakhir.getDate());
                    Map param = new HashMap();
                    param.put("awal", dariTgl);
                    param.put("akhir", sampaiTgl);

                    try {
                        JPrint = JasperFillManager.fillReport("src\\Manajer\\laporanTransaksiMember.jasper", param, connection.conn);
                        JasperViewer viewer = new JasperViewer(JPrint, false);
                        //viewer.setTitle("");
                        viewer.setVisible(true);
                    } catch (JRException ex) {
                        JOptionPane.showMessageDialog(null, "Error report : " + ex.getMessage());
                    }
                }
            }
        });
    }
}