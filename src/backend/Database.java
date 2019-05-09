package backend;
import backend.Security;
import frontend.MsgBox;
import java.sql.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Database {
    
    private HashMap<String, String> tabelMahasiswa;
    private HashMap<String, String> tabelDosen;
    private Connection connect;
    private Statement statement;
    private ResultSet result;
    
    private Security hashing;
    
    public Database () {
        try {
            //Membuat koneksi database
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/plps","root",""); 
            this.statement = this.connect.createStatement();
            
            //Membuat objek untuk hashing password
            this.hashing = new Security();
            
            //Mengimpor tabel mahasiswa dan dosen utk login
            this.tabelMahasiswa = new HashMap();
            this.tabelDosen = new HashMap();
            this.importTabelDosen();
            this.importTabelMahasiswa();
            
        } catch (ClassNotFoundException | SQLException ex){
            JOptionPane.showMessageDialog(null, "Inisiasi Database Error");
        }
    }
   
    public Connection getConnection() {
        return this.connect;
    }
    
    public void runQuery (String query) {
        try {
            result = this.statement.executeQuery(query);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public void execute (String query) {
        try {
            this.statement.execute(query);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    private void importTabelDosen() {
        try {
            //TIDAK BISA, BENARKAN!!!11!!1!!!
            this.runQuery("SELECT * FROM dosen;");
            
            while(this.result.next()){
                String nip = this.result.getString("nip");
                String password = this.result.getString("password");
                this.tabelDosen.put(nip, password);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
    
    private void importTabelMahasiswa() {
        try {
            this.runQuery("SELECT * FROM mahasiswa;");
            
            while(this.result.next()){
                String nim = this.result.getString("nim");
                String password = this.result.getString("password");
                this.tabelMahasiswa.put(nim, password);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
    
    public ReturnJSON login (String nomorInduk, String password) {
        try{         
            
            password = this.hashing.getHashed(this.hashing.getHashed(password));
            if(this.tabelMahasiswa.containsKey(nomorInduk)){
                if(this.tabelMahasiswa.get(nomorInduk).equals(password)){
                    this.runQuery("SELECT * FROM mahasiswa WHERE nim='" + nomorInduk + "' AND password='" + password + "';");
                    if(this.result.next()){
                        ReturnJSON json = new ReturnJSON(true, "Selamat datang " + this.result.getString("nama") + "!");
                        return json;
                    }else{
                            ReturnJSON json = new ReturnJSON(false, "Maaf, NIM atau Password salah.");
                            return json;
                        }
                }else{
                    ReturnJSON json = new ReturnJSON(false, "Maaf, NIM atau Password salah.");
                    return json;
                }       
            }else{
                if(this.tabelDosen.containsKey(nomorInduk)){
                    if(this.tabelDosen.get(nomorInduk).equals(password)){
                        this.runQuery("SELECT * FROM dosen WHERE nip='" + nomorInduk + "' AND password='" + password + "';");
                        
                        if(this.result.next()){
                            ReturnJSON json = new ReturnJSON(true, "Selamat datang Bapak/Ibu " + this.result.getString("nama") + "!");
                            return json;
                        }else{
                            ReturnJSON json = new ReturnJSON(false, "Maaf, NIP atau Password salah.");
                            return json;
                        }
                    }else{
                        ReturnJSON json = new ReturnJSON(false, "Maaf, NIP atau Password salah.");
                        return json;
                    }
                }else{
                    ReturnJSON json = new ReturnJSON(false, "Maaf, Nomor Induk tidak terdaftar.");
                    return json;
                }
            }
            
        } catch (SQLException ex) {
            ReturnJSON json = new ReturnJSON(false, "Oops, kesalahan telah terjadi pada sistem: Login");
            return json;
        }
    }
    
    public ReturnJSON daftar(String nama, String nim, String password) {
        if(nama.length() > 1 && nim.length() >= 8 && password.length() >= 8){
            try {
                this.execute("INSERT INTO mahasiswa (nim, nama, semester_terakhir, password) VALUES (" + MySQLUtils.quote(this.getConnection(), nim) + ", " + MySQLUtils.quote(this.getConnection(), nama) + ", 0, " + MySQLUtils.quote(this.getConnection(), this.hashing.getHashed(this.hashing.getHashed(password))) + ");");
                ReturnJSON json = new ReturnJSON(true, "Berhasil mendaftar!");
                return json;
            } catch(Exception ex) {
                ReturnJSON json = new ReturnJSON(false, "Oops, ada kesalahan di dalam sistem, silahkan coba lagi.");
                return json;
            }
        }else{
            ReturnJSON json = new ReturnJSON(false, "Maaf, data tidak memenuhi syarat mendaftar. (Nama minimal 1 karakter, nim minimal 8 karakter, password minimal 8 karakter)");
            return json;
        }
    }
}
