package Admin;

import DBConnect.ConnectionDB;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class CRUDJenisPembayaran extends JFrame {
    private JPanel JPForm;
    private JTextField txtPay;
    private JButton btnHapus;
    private JButton btnCancel;
    private JTable tblPay;
    private JButton btnSave;
    private JButton btnUpdate;
    public JPanel JPPay;
    private JTextField txtNo;

    DefaultTableModel model = new DefaultTableModel();
    ConnectionDB connection = new ConnectionDB();
    String payment;
    String nomor;
    String id;
    int status;

    public CRUDJenisPembayaran() {
        setContentPane(JPPay);
        addColumn();
        loadData();
        btnUpdate.setEnabled(false);
        btnHapus.setEnabled(false);


        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                payment = txtPay.getText();
                nomor = txtNo.getText();
                status = 1;
                Boolean found = false;

                // validasi jika memasukkan jenis yang sama
                Object[] obj = new Object[3];
                obj[0] = id;
                obj[1] = txtPay.getText();
                obj[2] = txtNo.getText();

                int j = tblPay.getModel().getRowCount();
                for(int k = 0; k < j; k++) {
                    if(obj[1].toString().toLowerCase().equals(model.getValueAt(k, 1).toString().toLowerCase()))
                    {
                        found = true;
                    }
                }
                if(found) {
                    JOptionPane.showMessageDialog(null, "Payment method already exist!", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else{
                    try {
                        if (validasiNull()) {
                            throw new Exception("Please, fill in all data!");
                        } else {
                            try {
                                payment = txtPay.getText();
                                nomor = txtNo.getText();

                                connection.stat = connection.conn.createStatement();
                                String sql = "SELECT * FROM tblJenisPembayaran ORDER BY id_jnsPembayaran DESC";
                                connection.result = connection.stat.executeQuery(sql);

                                if(connection.result.next()){
                                    id = connection.result.getString("id_jnsPembayaran").substring(3);

                                    String temp = "" + (Integer.parseInt(id) + 1);
                                    String nol = "";

                                    if(temp.length() == 1) nol = "000";
                                    else if (temp.length() == 2) nol = "00";
                                    else if (temp.length() == 3) nol = "0";
                                    else if (temp.length() == 4) nol = "";

                                    id = "PAY" + nol + temp;
                                }
                                else {
                                    id = "PAY0001";
                                }
                                String query = "INSERT INTO tblJenisPembayaran VALUES (?, ?, ?, ?)";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, id);
                                connection.pstat.setString(2, payment);
                                connection.pstat.setString(3, nomor);
                                connection.pstat.setString(4, "1");

                                connection.stat.close();
                                connection.pstat.executeUpdate();
                                connection.pstat.close();

                                clear();
                                JOptionPane.showMessageDialog(null, "Data saved successfully!");
                                loadData();
                            }catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, "an error occurred while saving data into the database" + ex);
                            }
                            clear();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                }
            }
        });
        txtNo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                payment = txtPay.getText();
                nomor = txtNo.getText();
                status = 1;
                try {
                    if (validasiNull()) {
                        throw new Exception("Please, fill in all data!");
                    } else {
                        try {
                            String query = "UPDATE tblJenisPembayaran SET nama_jnsPembayaran=?, no_pembayaran=? WHERE id_jnsPembayaran=?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(3, id);
                            connection.pstat.setString(1, payment);
                            connection.pstat.setString(2, String.valueOf(nomor));

                            connection.stat.close();
                            connection.pstat.executeUpdate();
                            connection.pstat.close();

                            clear();
                            JOptionPane.showMessageDialog(null, "Data updated successfully!");
                            loadData();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "an error occurred while updating data into the database" + ex);
                        }
                        clear();
                        loadData();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });

        btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opsi;
                try {
                    opsi = JOptionPane.showConfirmDialog(null, "Are you sure delete this data?",
                            "Confirmation", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (opsi != 0) {
                        JOptionPane.showMessageDialog(null, "Data failed to delete");
                    } else {
                        int i = tblPay.getSelectedRow();
                        if (i == -1) return;
                        id = String.valueOf(model.getValueAt(i, 0));

                        String query = "UPDATE tblJenisPembayaran SET status='0' WHERE id_jnsPembayaran=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);

                        connection.stat.close();
                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        clear();
                        JOptionPane.showMessageDialog(null, "Data deleted successfully!");
                        loadData();
                    }
                } catch (Exception e1){
                    JOptionPane.showMessageDialog(null, "an error occurred while deleting data into the database.\n" + e1);
                }
            }
        });
        tblPay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblPay.getSelectedRow();
                if(i == -1) {
                    return;
                }
                id = (String) model.getValueAt(i, 0);
                txtPay.setText((String) model.getValueAt(i, 1));
                txtNo.setText((String) model.getValueAt(i, 2));
                btnSave.setEnabled(false);
                btnHapus.setEnabled(true);
                btnUpdate.setEnabled(true);
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        txtPay.addKeyListener(new KeyAdapter() {
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
        txtNo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
    }

    public boolean validasiNull(){
        if(payment.isEmpty()){
            return true;
        }
        else if(nomor.isEmpty()) {
            return true;
        } else{
            return false;
        }
    }

    public void addColumn() {
        tblPay.setModel(model);
        model.addColumn("ID Payment Method");
        model.addColumn("Payment Method");
        model.addColumn("Virtual Account");
    }

    public void loadData() {
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try{
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblJenisPembayaran WHERE status='1'";
            connection.result = connection.stat.executeQuery(query);

            while(connection.result.next()){
                Object[] obj = new Object[4];
                obj[0] = connection.result.getString("id_jnsPembayaran");
                obj[1] = connection.result.getString("nama_jnsPembayaran");
                obj[2] = connection.result.getString("no_pembayaran");
                model.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
            btnSave.setEnabled(true);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "an error occurred while loading data.\n" + e);
        }
    }

    public void clear() {
        txtPay.setText("");
        txtNo.setText("");
        loadData();
        btnHapus.setEnabled(false);
        btnUpdate.setEnabled(false);
    }

    public static void main(String[] args){
        JFrame frame = new CRUDJenisPembayaran();
        frame.setContentPane(new CRUDJenisPembayaran().JPForm);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
