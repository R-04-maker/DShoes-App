package Admin;

import DBConnect.ConnectionDB;
import Admin.CRUDJabatan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class CRUDJabatan extends JFrame {
    private JTextField txtJobTitle;
    private JButton btnDelete;
    private JButton btnClear;
    private JTable tblJob;
    private JButton btnUpdate;
    private JButton btnSave;
    public JPanel JPJabatan;
    private JTextField txtSearch;
    private JButton btnCari;

    //table
    private DefaultTableModel model;

    //connection database
    ConnectionDB connection = new ConnectionDB();

    String kode;
    String jobtitle;

    public CRUDJabatan() {
        model = new DefaultTableModel();
        tblJob.setModel(model);

        addColomn();
        clear();
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //String title = txtJobTitle.getText();
                jobtitle = txtJobTitle.getText();
                boolean found = false;
                // validasi tidak boleh sama
                Object[] obj = new Object[2];
                obj[0] = kode;
                obj[1] = jobtitle;

                int j = tblJob.getModel().getRowCount();
                for(int k = 0; k<j; k++){
                    if(obj[1].toString().toLowerCase().equals(model.getValueAt(k, 1).toString().toLowerCase())){
                        found = true;
                    }
                }
                if(found){
                    JOptionPane.showMessageDialog(null,"Role already exist!", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        if (txtJobTitle.getText().equals("")) {
                            JOptionPane.showMessageDialog(null, "Please, fill in all data!");
                        } else {
                            try {
                                jobtitle = txtJobTitle.getText();

                                connection.stat = connection.conn.createStatement();
                                String sql = "SELECT * FROM tblJabatan ORDER BY id_jabatan DESC";
                                connection.result = connection.stat.executeQuery(sql);

                                if (connection.result.next()) {
                                    kode = connection.result.getString("id_jabatan").substring(3);

                                    String temp = "" + (Integer.parseInt(kode) + 1);
                                    String nol = "";

                                    if (temp.length() == 1) nol = "000";
                                    else if (temp.length() == 2) nol = "00";
                                    else if (temp.length() == 3) nol = "0";
                                    else if (temp.length() == 4) nol = "";

                                    kode = "JMB" + nol + temp;
                                } else {
                                    kode = "JMB0001";
                                }
                                String query = "INSERT INTO tblJabatan VALUES (?, ?, ?)";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, kode);
                                connection.pstat.setString(2, jobtitle);
                                connection.pstat.setString(3, "1");

                                connection.stat.close();
                                connection.pstat.executeUpdate();
                                connection.pstat.close();

                                clear();
                                JOptionPane.showMessageDialog(null, "Data saved successfully!");
                                loadData();
                            } catch (NumberFormatException nex) {
                                JOptionPane.showMessageDialog(null, "Please, enter a valid number!.");
                            } catch (Exception e1) {
                                JOptionPane.showMessageDialog(null, "an error occurred while saving data into the database.\n" + e1);
                            }
                        }
                    } catch(Exception e1){

                    }
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //String title = txtJobTitle.getText();
                jobtitle = txtJobTitle.getText();
                boolean found = false;
                // validasi tidak boleh sama
                Object[] obj = new Object[2];
                obj[0] = kode;
                obj[1] = jobtitle;

                int j = tblJob.getModel().getRowCount();
                for(int k = 0; k<j; k++){
                    if(obj[1].toString().toLowerCase().equals(model.getValueAt(k, 1).toString().toLowerCase())){
                        found = true;

                    }
                }
                if(found){
                    JOptionPane.showMessageDialog(null,"Role already exist!", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        if (txtJobTitle.getText().equals("")) {
                            JOptionPane.showMessageDialog(null, "Please, fill in all data!");
                        } else {
                            try {
                                int i = tblJob.getSelectedRow();
                                if (i == -1) return;
                                kode = String.valueOf(model.getValueAt(i, 0));
                                jobtitle = txtJobTitle.getText();

                                String query = "UPDATE tblJabatan SET nama_jabatan=? WHERE id_jabatan=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(2, kode);
                                connection.pstat.setString(1, jobtitle);

                                connection.stat.close();
                                connection.pstat.executeUpdate();
                                connection.pstat.close();

                                clear();
                                JOptionPane.showMessageDialog(null, "Data updated successfully!");
                                loadData();
                            } catch (NumberFormatException nex) {
                                JOptionPane.showMessageDialog(null, "Please, enter a valid number!.");
                            } catch (Exception e1) {
                                JOptionPane.showMessageDialog(null, "an error occurred while updating data into the database.\n" + e1);
                            }
                        }
                    } catch (Exception e1) {

                    }
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opsi;
                if (txtJobTitle.getText().equals("") )
                    JOptionPane.showMessageDialog(null, "Please, fill in all data!");
                else{
                    try {
                        opsi = JOptionPane.showConfirmDialog(null, "Are you sure delete this data?",
                                "Confirmation", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (opsi != 0) {
                            JOptionPane.showMessageDialog(null, "Data failed to delete");
                        } else {
                            int i = tblJob.getSelectedRow();
                            if (i == -1) return;
                            kode = String.valueOf(model.getValueAt(i, 0));

                            String query = "UPDATE tblJabatan SET status='0' WHERE id_jabatan=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, kode);

                            connection.stat.close();
                            connection.pstat.executeUpdate();
                            connection.pstat.close();

                            clear();
                            JOptionPane.showMessageDialog(null, "Data deleted successfully!");
                            loadData();
                        }
                    } catch (NumberFormatException nex){
                        JOptionPane.showMessageDialog(null, "Please, enter the valid number.");
                    } catch (Exception e1){
                        JOptionPane.showMessageDialog(null, "an error occurred while deleting data into the database.\n" + e1);
                    }
                }
            }
        });
        tblJob.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblJob.getSelectedRow();
                kode = ((String) model.getValueAt(i, 0));
                txtJobTitle.setText((String) model.getValueAt(i, 1));
                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        txtJobTitle.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z')) && (c != KeyEvent.VK_BACK_SPACE)
                        && (c != KeyEvent.VK_SPACE) && (c != KeyEvent.VK_PERIOD)) {
                    e.consume();
                }
            }
        });
    }

    //header table
    public void addColomn(){
        model.addColumn("ID Job");
        model.addColumn("Job Title");
    }

    //load data from database
    public void loadData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try{
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblJabatan WHERE status='1'";
            connection.result = connection.stat.executeQuery(query);

            while(connection.result.next()){
                Object[] obj = new Object[6];
                obj[0] = connection.result.getString("id_jabatan");
                obj[1] = connection.result.getString("nama_jabatan");
                if (connection.result.getString("status").equals("1"))
                    obj[2] = "Aktif";
                else obj[2] = "Tidak Aktif";
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
            btnSave.setEnabled(true);
            btnDelete.setEnabled(false);
            btnUpdate.setEnabled(false);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "an error occurred while loading data .\n" + e);
        }
    }

    //method clear
    public void clear(){
        txtJobTitle.setText(null);
        loadData();
        btnDelete.setEnabled(false);
        btnUpdate.setEnabled(false);
    }

    //validasi Jika Kosong
    public boolean validasiNull(){
        if(jobtitle.isEmpty()){
            return true;
        }else {
            return false;
        }
    }


    /*public static void main(String[] args) {
        JFrame frame = new CRUDJabatan();
        frame.setContentPane(new CRUDJabatan().JPJabatan);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }*/
}
