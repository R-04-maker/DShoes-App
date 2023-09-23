package Login;

import Admin.HalamanAdmin;
import DBConnect.ConnectionDB;
import Kasir.HalamanKasir;
import Manajer.HalamanManajer;
import com.sun.jdi.Value;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame{
    private JPanel Panel_Tengah;
    private JPanel Panel_Login;
    private JLabel Label_Username;
    private JTextField Textbox_Username;
    private JPasswordField Textbox_Password;
    private JLabel Label_Password;
    private JCheckBox Checkbox_Password;
    private JPanel Panel_Tombol;
    private JButton Tombol_Login;
    private JPanel Panel_Judul;
    private JLabel Label_Judul;
    private JPanel Panel_Bantuan;
    private JLabel Label_Gambar;
    private JPanel panelKiri;
    private JPanel PanelSepatu;
    private JPanel PanelKanan;
    private JPanel PanelBawah;
    private JPanel PanelAtas;
    private JPanel PanelBawahlogin1;
    private JPanel PanelBawahlogin2;
    private JPanel PanelAtaslogin1;
    private JPanel PanelAtaslogin2;
    private JPanel PanelUtama;
    private JButton Tombol_Keluar;
    String nama,username,password,jabatan,id;

    public void FrameConfig(){
        add(this.PanelUtama);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    public String[] validasi(){
        if(Textbox_Username.getText().isEmpty() || Textbox_Password.getText().isEmpty()){
            JOptionPane.showMessageDialog(PanelUtama,"Username / Password Kosong","Peringatan",JOptionPane.WARNING_MESSAGE);
        }else {
            try{
                // Instantiasi koneksi database
                ConnectionDB connection = new ConnectionDB();

                // Mempersiapakan query
                connection.stat = connection.conn.createStatement();

                // Isi query
                String query = "SELECT t1.Username,t1.Password,t2.nama_jabatan,t1.Nama_Pegawai,t1.id_pegawai FROM tblPegawai AS t1 JOIN tblJabatan AS t2 ON t1.id_jabatan=t2.id_jabatan WHERE username = '" +Textbox_Username.getText()+"' and password = '" +Textbox_Password.getText()+"'";

                // Mengekseskusi query
                connection.result = connection.stat.executeQuery(query);

                // Mengecheck isi query , apabila isi query kosong maka akan dilakukan perintah dibawahnya
                if(!connection.result.next()){
                    //Membuat kesalahan yang baru pada struktur try-catch
                    throw new Exception("User Not Found!");
                }

                // Mengambil isi query pada index ke - n
                username = connection.result.getString(1);
                password = connection.result.getString(2);
                nama = connection.result.getString(4);
                jabatan = connection.result.getString(3);//nama jabatan
                id = connection.result.getString(5); //id pegawai

                // Mengembalikan nilai validasi
                return new String[] {"true",username,password,nama,jabatan,id};

            }catch (Exception ex){
                //Menampilkan dialog pesan
                System.out.println(".."+ex.getMessage());
                JOptionPane.showMessageDialog(PanelUtama,ex.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
            }
        }
        // Mengembalikan nilai validasi
        return new String[] {"false"};
    }
    public void saveLogin(){

    }
    public Login() {
        FrameConfig();
        Tombol_Login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mengisi array value dengan nilai yang dikembalikan dari metode validasi()
                String[] value = validasi();
                // Mengisi boolean valid dengan nilai dari index ke 0 value
                Boolean valid = Boolean.parseBoolean(value[0]);
                // Melakukan pengecheckan
                if(valid){
                    // Menampilkan dialog pesan
                    JOptionPane.showMessageDialog(PanelUtama,"Welcome to D'Shoes","Information",JOptionPane.INFORMATION_MESSAGE);
                    System.out.println(value[4]);
                    System.out.println(value[5]); //id Pegawai
                    System.out.println(value[3]); // nama pegawai

                    if(value[4].equals("Manager")){
                        // menutup form yang sekarang
                        dispose();

                        // instantiasi form baru
                        HalamanManajer form = new HalamanManajer(value);

                        // menampilkan form
                        form.setVisible(true);
                    }else if(value[4].equals("Admin")){
                        // menutup form yang sekarang
                        dispose();

                        // instantiasi form baru
                        HalamanAdmin form = new HalamanAdmin(value);

                        // menampilkan form
                        form.setVisible(true);
                    }else if(value[4].equals("Cashier")){
                        // menutup form yang sekarang
                        dispose();

                        // instantiasi form baru
                        HalamanKasir form = new HalamanKasir(value);

                        // menampilkan form
                        form.setVisible(true);
                    }
                }
            }
        });
        Tombol_Keluar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        Checkbox_Password.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Checkbox_Password.isSelected() == true){
                    Textbox_Password.setEchoChar('\0');
                }else {
                    Textbox_Password.setEchoChar('*');
                }
            }
        });
    }
}