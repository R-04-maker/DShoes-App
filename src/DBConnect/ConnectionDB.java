package DBConnect;
import java.sql.*;
public class ConnectionDB {
    public Connection conn;
    public Statement stat;
    public ResultSet result;
    public PreparedStatement pstat;

    public ConnectionDB(){
        try{
            String url = "jdbc:sqlserver://localhost;database=PRG3_KEL09;encrypt=false;user=sa;password=12345678";
            //String url = "jdbc:sqlserver://localhost;database=PRG2_KEL09;encrypt=false;user=sa;password=polman";
            conn = DriverManager.getConnection(url);
            stat = conn.createStatement();
            System.out.println("Connection Berhasil "+stat);
        }catch (Exception e){
            System.out.println("Error saat connect Database: "+e);
        }
    }    public static  void  main (String[] args){
        ConnectionDB connect = new ConnectionDB();
    }

}
