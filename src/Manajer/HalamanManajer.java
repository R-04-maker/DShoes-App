package Manajer;

import Kasir.CRUDCustomer;
import Login.Login;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HalamanManajer extends JFrame{
    public JPanel ManajerMenu;
    private JPanel Manajer;
    private JPanel PanelKiri;
    private JPanel PanelKiriAtas;
    private JLabel label_logo;
    private JPanel Panel_Bantuan_Akun;
    private JLabel Label_Gambar_2;
    private JLabel Label_Nama;
    private JLabel Label_Jabatan;
    private JPanel PanelButton;
    private JPanel Panel_Bantuan;
    private JButton customerTransactionReportButton;
    private JButton supplierTransactionReportButton;
    private JButton memberTransactionReportButton;
    private JPanel Panel_Tengah;
    private JLabel Label_Salam;
    private JPanel PanelForm;
    private JLabel label_nama;
    private JButton btLogOut;

    public HalamanManajer(String[] data){
        FrameConfig();
        Label_Nama.setText(data[3]);
        Label_Jabatan.setText(data[4]);
        btLogOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login form = new Login();
                form.setVisible(true);
            }
        });
        customerTransactionReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("CUSTOMER TRANSACTION REPORT");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                LaporanTransaksiCustomer show = new LaporanTransaksiCustomer();
                show.ManajerMenu.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.ManajerMenu);
            }
        });
        supplierTransactionReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("SUPPLIER TRANSACTION REPORT");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                LaporanTransaksiSupplier show = new LaporanTransaksiSupplier();
                show.ManajerMenu.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.ManajerMenu);
            }
        });
        memberTransactionReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("MEMBER TRANSACTION REPORT");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                LaporanTransaksiMember show = new LaporanTransaksiMember();
                show.ManajerMenu.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.ManajerMenu);
            }
        });
    }
    public void FrameConfig(){
        add(this.ManajerMenu);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
