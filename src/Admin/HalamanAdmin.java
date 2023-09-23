package Admin;

import Login.Login;

import javax.swing.*;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HalamanAdmin extends JFrame {

    private JPanel Panel_Bantuan_Akun;
    private JLabel Label_Gambar_2;
    private JLabel Label_Nama;
    private JLabel Label_Jabatan;
    private JPanel Panel_Tengah;
    private JLabel Label_Salam;
    private JPanel Panel_Bantuan;
    private JButton shoesButton;
    private JButton supplierButton;
    private JButton memberTypeButton;
    private JButton payMethodButton;
    private JButton WarrantyButton;
    private JPanel AdminMenu;
    private JPanel PanelKiri;
    private JPanel PanelKiriAtas;
    private JPanel PanelButton;
    private JPanel PanelForm;
    private JButton categoryButton;
    private JLabel label_logo;
    private JLabel label_nama;
    private JButton employeeButton;
    private JButton btLogOut;
    private JButton empType;

    public void FrameConfig(){
        add(this.AdminMenu);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    public HalamanAdmin(String[] value){
        FrameConfig();
        Label_Nama.setText(value[3]);
        Label_Jabatan.setText(value[4]);
        categoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("SHOE CATEGORY MENU");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                CRUDKategoriSepatu show = new CRUDKategoriSepatu();
                show.JPCategory.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPCategory);
            }
        });
        supplierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("SUPPLIER MENU");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                CRUDSupplier show = new CRUDSupplier();
                show.JPSupplier.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPSupplier);
            }
        });
        memberTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("MEMBER TYPE MENU");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                CRUDMemberType show = new CRUDMemberType();
                show.JPMemberType.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPMemberType);
            }
        });
        payMethodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("PAYMENT METHOD MENU");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                CRUDJenisPembayaran show = new CRUDJenisPembayaran();
                show.JPPay.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPPay);
            }
        });
        WarrantyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("WARRANTY MENU");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                CRUDGaransi show = new CRUDGaransi();
                show.JPWarranty.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPWarranty);
            }
        });
        shoesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("SHOES MENU");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                CRUDSepatu show = new CRUDSepatu();
                show.PanelSepatu.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.PanelSepatu);
            }
        });
        employeeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("EMPLOYEE MENU");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                CRUDPegawai show = new CRUDPegawai();
                show.JPPegawai.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPPegawai);
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
        empType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label_nama.setText("EMPLOYEE TYPE MENU");
                PanelForm.removeAll();
                PanelForm.revalidate();
                PanelForm.repaint();
                CRUDJabatan show = new CRUDJabatan();
                show.JPJabatan.setVisible(true);
                PanelForm.revalidate();
                PanelForm.setLayout(new java.awt.BorderLayout());
                PanelForm.add(show.JPJabatan);
            }
        });
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame("HalamanKasir");
//        frame.setContentPane(new HalamanAdmin());
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);
//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//    }
}
