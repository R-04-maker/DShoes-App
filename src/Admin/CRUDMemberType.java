package Admin;

import DBConnect.ConnectionDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CRUDMemberType extends JFrame{
    private JTextField txtValPer;
    private JTextField txtDiskon;
    private JTextField txtMember;
    private JTextField txtPrice;
    private JButton btnSave;
    private JButton btnHapus;
    private JButton btnUpdate;
    private JTable tblMember;
    public JPanel JPMemberType;
    private JButton btnCancel;

    DefaultTableModel model = new DefaultTableModel();
    ConnectionDB connection = new ConnectionDB();
    String id;
    String jenis;
    String diskon;
    String masaberlaku;
    String harga;
    int status;

    public CRUDMemberType() {
        setContentPane(JPMemberType);
        addColumn();
        loadData();
        btnUpdate.setEnabled(false);
        btnHapus.setEnabled(false);
        txtDiskon.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE) || txtDiskon.getText().length() >= 2) {
                    e.consume();
                }
            }
        });
        txtValPer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
        txtPrice.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jenis = txtMember.getText();
                diskon = txtDiskon.getText();
                masaberlaku = txtValPer.getText();
                harga = txtPrice.getText();
                status = 1;
                Boolean found = false;

                //validasi jika memasukkan tipe member yang sama
                Object[] obj = new Object[5];
                obj[0] = id;
                obj[1] = jenis;
                obj[2] = diskon;
                obj[3] = masaberlaku;
                obj[4] = harga;

                int j = tblMember.getModel().getRowCount();
                for(int k = 0; k < j; k++) {
                    if(obj[1].toString().toLowerCase().equals(model.getValueAt(k, 1).toString().toLowerCase()))
                    {
                        found = true;
                    }
                }
                if(found) {
                    JOptionPane.showMessageDialog(null, "Member type already exist!", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    try{
                        if (validasiNull()) {
                            throw new Exception("Please, fill in all data!");
                        } else if(txtDiskon.getText().equals("0") ||txtValPer.getText().equals("0") || txtPrice.getText().equals("0") ){
                            JOptionPane.showMessageDialog(null, "Stock cannot be empty or zero !");
                        }
                        else{
                            try {
                                jenis = txtMember.getText();
                                diskon = txtDiskon.getText();
                                masaberlaku = txtValPer.getText();
                                harga = txtPrice.getText();

                                connection.stat = connection.conn.createStatement();
                                String sql = "SELECT * FROM tblTipeMember ORDER BY id_member DESC";
                                connection.result = connection.stat.executeQuery(sql);

                                if(connection.result.next()){
                                    id = connection.result.getString("id_member").substring(3);

                                    String temp = "" + (Integer.parseInt(id) + 1);
                                    String nol = "";

                                    if(temp.length() == 1) nol = "000";
                                    else if (temp.length() == 2) nol = "00";
                                    else if (temp.length() == 3) nol = "0";
                                    else if (temp.length() == 4) nol = "";

                                    id = "JMB" + nol + temp;
                                }
                                else {
                                    id = "JMB0001";
                                }
                                String query = "INSERT INTO tblTipeMember VALUES (?, ?, ?, ?, ?, ?)";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(1, id);
                                connection.pstat.setString(2, jenis);
                                connection.pstat.setString(3, diskon);
                                connection.pstat.setString(4, masaberlaku);
                                connection.pstat.setString(5, harga);
                                connection.pstat.setString(6, "1");

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
                        int i = tblMember.getSelectedRow();
                        if (i == -1) return;
                        id = String.valueOf(model.getValueAt(i, 0));

                        String query = "UPDATE tblTipeMember SET status='0' WHERE id_member=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);

                        connection.stat.close();
                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        clear();
                        JOptionPane.showMessageDialog(null, "Data deleted successfully!");
                        loadData();
                    }
                } catch(Exception e1){
                    JOptionPane.showMessageDialog(null, "an error occurred while deleting data into the database.\n" + e1);
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jenis = txtMember.getText();
                diskon = txtDiskon.getText();
                masaberlaku = txtValPer.getText();
                harga = txtPrice.getText();
                status = 1;
                Boolean found = false;

                //validasi jika memasukkan tipe member yang sama
                Object[] obj = new Object[5];
                obj[0] = id;
                obj[1] = jenis;
                obj[2] = diskon;
                obj[3] = masaberlaku;
                obj[4] = harga;
                int j = tblMember.getModel().getRowCount();
                for(int k = 0; k < j; k++) {
                    if(obj[1].toString().toLowerCase().equals(model.getValueAt(k, 1).toString().toLowerCase()))
                    {
                        found = true;
                    }
                }
                if(found) {
                    JOptionPane.showMessageDialog(null, "Member type already exist!", "Warning",
                          JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        if (validasiNull()) {
                            throw new Exception("Please, fill in all data!");
                        } else if(txtDiskon.getText().equals("0") ||txtValPer.getText().equals("0") || txtPrice.getText().equals("0") ){
                            JOptionPane.showMessageDialog(null, "Stock cannot be empty or zero !");
                        }
                        else {
                            try {
                                int i = tblMember.getSelectedRow();
                                if(i == -1) return;
                                id = String.valueOf(model.getValueAt(i, 0));
                                jenis = txtMember.getText();
                                diskon = txtDiskon.getText();
                                masaberlaku = txtValPer.getText();
                                harga = txtPrice.getText();

                                String query = "UPDATE tblTipeMember SET jenis_member=?, diskon=?, masa_berlaku=?, " +
                                        "harga_member=? WHERE id_member=?";
                                connection.pstat = connection.conn.prepareStatement(query);
                                connection.pstat.setString(5, id);
                                connection.pstat.setString(1, jenis);
                                connection.pstat.setString(2, diskon);
                                connection.pstat.setString(3, masaberlaku);
                                connection.pstat.setString(4, harga);

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
            }
        });
        tblMember.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tblMember.getSelectedRow();
                if(i == -1){
                    return;
                }
                txtMember.setText((String) model.getValueAt(i, 1));
                txtDiskon.setText((String) model.getValueAt(i, 2));
                txtValPer.setText((String) model.getValueAt(i, 3));
                txtPrice.setText((String) model.getValueAt(i, 4));
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
        txtPrice.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                try{
                    String sbyr = txtPrice.getText().replaceAll("\\,", "");
                    double dblByr = Double.parseDouble(sbyr);
                    DecimalFormat df = new DecimalFormat("#,###,###");
                    if (dblByr > 999) {
                        txtPrice.setText(df.format(dblByr));
                    }
                }catch (Exception ex){

                }
            }
        });
        txtMember.addKeyListener(new KeyAdapter() {
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
        txtDiskon.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
        txtValPer.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
                    e.consume();
                }
            }
        });
        txtPrice.addKeyListener(new KeyAdapter() {
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

    // validasi formatrupiah
    private String formatrupiah(int value){
        DecimalFormat formater = new DecimalFormat("#,###,###");
        DecimalFormatSymbols symbol = formater.getDecimalFormatSymbols();
        symbol.setMonetaryDecimalSeparator(',');
        symbol.setGroupingSeparator(',');
        formater.setDecimalFormatSymbols(symbol);
        return  formater.format(value);
    }

    public boolean validasiNull(){
        if(jenis.isEmpty()){
            return true;
        }
        else if(diskon.isEmpty()) {
            return true;
        }
        else if(masaberlaku.isEmpty()) {
            return true;
        }
        else if(harga.isEmpty()) {
            return true;
        }else{
            return false;
        }
    }

    public void addColumn(){
        tblMember.setModel(model);
        model.addColumn("ID Member");
        model.addColumn("Member Type");
        model.addColumn("Discount(%)");
        model.addColumn("Validity Period");
        model.addColumn("Member Price");
    }

    public void loadData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        try{
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblTipeMember WHERE status='1'";
            connection.result = connection.stat.executeQuery(query);

            while(connection.result.next()){
                Object[] obj = new Object[6];
                obj[0] = connection.result.getString("id_member");
                obj[1] = connection.result.getString("jenis_member");
                obj[2] = connection.result.getString("diskon");
                obj[3] = connection.result.getString("masa_berlaku");
                obj[4] = formatrupiah(connection.result.getInt("harga_member"));
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

    public void clear(){
        txtDiskon.setText("");
        txtMember.setText("");
        txtValPer.setText("");
        txtPrice.setText("");
        loadData();
        btnHapus.setEnabled(false);
        btnUpdate.setEnabled(false);
    }

    /*public static void main(String[] args){
        JFrame frame = new CRUDMemberType();
        frame.setContentPane(new CRUDMemberType().JPMemberType);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }*/
}
