package Kasir;

import Login.Login;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HalamanKasir extends JFrame{
    private JPanel KasirMenu;
    private JPanel KasirMenu1;
    private JPanel PanelKiri;
    private JPanel PanelKiriAtas;
    private JLabel label_logo;
    private JPanel Panel_Bantuan_Akun;
    private JLabel Label_Gambar_2;
    private JLabel Label_Nama;
    private JLabel Label_Jabatan;
    private JPanel PanelButton;
    private JPanel Panel_Bantuan;
    private JButton customerButton;
    private JButton ShoesButton;
    private JButton customerTransactionButton;
    private JButton memberTransactionButton;
    private JButton supplierTransactionButton;
    private JPanel Panel_Tengah;
    private JLabel Label_Salam;
    private JPanel PanelForm;
    private JLabel label_nama;
    private JButton btLogOut;
    private JButton btKlaimGuaranty;
    private JButton btSupplierStatus;

    public HalamanKasir(String[] value){
        FrameConfig();
        Label_Nama.setText(value[3]);
        Label_Jabatan.setText(value[4]);
        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("VIEW CUSTOMER");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                CRUDCustomer show = new CRUDCustomer();
                show.JPCustomer.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPCustomer);
            }
        });
        ShoesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("VIEW SHOES");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                ViewSepatu show = new ViewSepatu();
                show.ViewSepatu.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.ViewSepatu);
            }
        });
        customerTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("CUSTOMER TRANSACTION");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                CustomerTransaksi show = new Kasir.CustomerTransaksi(value);
                show.JPTransaksiCustomer.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPTransaksiCustomer);
            }
        });
        memberTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("MEMBER TRANSACTION");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                TransaksiMember show = new TransaksiMember(value);
                show.TransaksiMember.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.TransaksiMember);
            }
        });
        supplierTransactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("SUPPLIER TRANSACTION");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                TransaksiSupplier show = new TransaksiSupplier(value);
                show.TsSupplier.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.TsSupplier);
            }
        });
        btLogOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login form = new Login();
                form.setVisible(true);
            }
        });
        btKlaimGuaranty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("Klaim Guaranty");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                KlaimGuaranty show = new KlaimGuaranty(value);
                show.JPKlaimGuaranty.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPKlaimGuaranty);
            }
        });
        btSupplierStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("Supplier Status");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                SupplierStatus show = new SupplierStatus();
                show.JPSupplierStatus.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPSupplierStatus);

            }
        });
    }
    public void FrameConfig(){
        add(this.KasirMenu);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
