package backend;
import frontend.DaftarDosenForm;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;

public class Database {
    
    private HashMap<String, String> tabelMahasiswa;
    private HashMap<String, String> tabelDosen;
    private HashMap<String, String> tabelAdmin;
    private Connection connect;
    private Statement statement;
    private ResultSet result;
    
    private Security hashing;
    
    public Database () {
        try {
            //Membuat koneksi database
//            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/plps","root",""); 
            this.statement = this.connect.createStatement();
            
            //Membuat objek untuk hashing password
            this.hashing = new Security();
            
            //Mengimpor tabel mahasiswa dan dosen utk login
            this.tabelMahasiswa = new HashMap();
            this.tabelDosen = new HashMap();
            this.tabelAdmin = new HashMap();
            this.importTabelDosen();
            this.importTabelMahasiswa();
            this.importTabelAdmin();
            
        } catch ( SQLException ex){
            JOptionPane.showMessageDialog(null, ex.toString());
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
    
    public boolean execute (String query) {
        boolean success = false;
        try {
            success = this.statement.execute(query);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        
        return success;
    }
    
    public ResultSet getResult () {
        return this.result;
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
    
     
    private void importTabelAdmin() {
        try {
            this.runQuery("SELECT * FROM admin;");
            
            while(this.result.next()){
                String nia = this.result.getString("nia");
                String password = this.result.getString("password");
                this.tabelAdmin.put(nia, password);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
    
    public ArrayList<MataKuliah> importTabelMataKuliah(){
        ArrayList<MataKuliah> mataKuliah = new ArrayList();
        this.runQuery("SELECT * FROM matkul");
        try {
            while(this.result.next()){
                mataKuliah.add(new MataKuliah(this.result.getString("id_matkul"), this.result.getString("nama"), Integer.parseInt(this.result.getString("sks")), Integer.parseInt(this.result.getString("semester")), ((this.result.getString("wajib").equals("1"))), Integer.parseInt(this.result.getString("prioritas")), Double.parseDouble(this.result.getString("logic_point_rate")), Double.parseDouble(this.result.getString("math_point_rate")), Double.parseDouble(this.result.getString("memory_point_rate"))));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
        
        return mataKuliah;
    }
    
    public HashMap<String, MataKuliah> importHashMapMataKuliah(){
        HashMap<String, MataKuliah> mataKuliah = new HashMap();
        this.runQuery("SELECT * FROM matkul");
        try {
            while(this.result.next()){
                mataKuliah.put(this.result.getString("id_matkul"), new MataKuliah(this.result.getString("id_matkul"), this.result.getString("nama"), Integer.parseInt(this.result.getString("sks")), Integer.parseInt(this.result.getString("semester")), ((this.result.getString("wajib").equals("1"))), Integer.parseInt(this.result.getString("prioritas")), Double.parseDouble(this.result.getString("logic_point_rate")), Double.parseDouble(this.result.getString("math_point_rate")), Double.parseDouble(this.result.getString("memory_point_rate"))));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
        
        return mataKuliah;
    }
    
    public ReturnLoginJSON login (String nomorInduk, String password) {
        try{         
            
            password = this.hashing.getHashed(this.hashing.getHashed(password));
                     System.out.println("noinduk:"+tabelMahasiswa.get(nomorInduk)+" pass:"+password);
            if(this.tabelMahasiswa.containsKey(nomorInduk)){
                if(this.tabelMahasiswa.get(nomorInduk).equals(password)){
                    this.runQuery("SELECT * FROM mahasiswa WHERE nim='" + nomorInduk + "' AND password='" + password + "';");
                    if(this.result.next()){
                        ReturnLoginJSON json = new ReturnLoginJSON(true, "Selamat datang " + this.result.getString("nama") + "!", new Mahasiswa(this.result.getString("nim"), this.result.getString("nama"), Integer.parseInt(this.result.getString("semester_terakhir")), this.result.getString("password"), Double.parseDouble(this.result.getString("logic_point")), Double.parseDouble(this.result.getString("math_point")), Double.parseDouble(this.result.getString("memory_point"))));
                        return json;
                    }else{
                            ReturnLoginJSON json = new ReturnLoginJSON(false, "Maaf, NIM atau Password salah.", new Mahasiswa("0", "", 0, "", 0.0, 0.0, 0.0));
                            return json;
                        }
                }else{
                    ReturnLoginJSON json = new ReturnLoginJSON(false, "Maaf, NIM atau Password salah.", new Mahasiswa("0", "", 0, "", 0.0, 0.0, 0.0));
                    return json;
                }       
            }else if (this.tabelDosen.containsKey(nomorInduk)){
                if(this.tabelDosen.containsKey(nomorInduk)){
                    if(this.tabelDosen.get(nomorInduk).equals(password)){
                        this.runQuery("SELECT * FROM dosen WHERE nip='" + nomorInduk + "' AND password='" + password + "';");
                        
                        if(this.result.next()){
                            ReturnLoginJSON json = new ReturnLoginJSON(true, "Selamat datang Bapak/Ibu " + this.result.getString("nama") + "!", new Dosen(this.result.getString("nip"), this.result.getString("nama"), Integer.parseInt(this.result.getString("id_fokus")), this.result.getString("password")));
                            return json;
                        }else{
                            ReturnLoginJSON json = new ReturnLoginJSON(false, "Maaf, NIP atau Password salah.", new Dosen("0", "", 0, ""));
                            return json;
                        }
                    }else{
                        ReturnLoginJSON json = new ReturnLoginJSON(false, "Maaf, NIP atau Password salah.", new Dosen("0", "", 0, ""));
                        return json;
                    }
                }else{
                    ReturnLoginJSON json = new ReturnLoginJSON(false, "Maaf, Nomor Induk tidak terdaftar.", new Dosen("0", "", 0, ""));
                    return json;
                }
            }
            else{
//                System.out.println("json");
                 if(this.tabelAdmin.containsKey(nomorInduk)){
                     System.out.println("noinduk:"+tabelAdmin.get(nomorInduk)+" pass:"+password);
                    if(this.tabelAdmin.get(nomorInduk).equals(password)){
                        this.runQuery("SELECT * FROM admin WHERE nia='" + nomorInduk + "' AND password='" + password + "';");
                        
                        if(this.result.next()){
                            ReturnLoginJSON json = new ReturnLoginJSON(true, "Selamat datang Admin " + this.result.getString("nama") + "!", new Admin(this.result.getString("nia"), this.result.getString("nama"),this.result.getString("password")));
                            return json;
                        }else{
                            ReturnLoginJSON json = new ReturnLoginJSON(false, "Maaf, NIA atau Password salah.", new Admin("0", "", ""));
                            return json;
                        }
                    }else{
                        ReturnLoginJSON json = new ReturnLoginJSON(false, "Maaf, NIA atau Password salah.", new Admin("0", "", ""));
                        return json;
                    }
                }else{
                    ReturnLoginJSON json = new ReturnLoginJSON(false, "Maaf, Nomor Induk tidak terdaftar.", new Admin("0", "", ""));
                    return json;
                }
            }
            
        } catch (SQLException ex) {
            ReturnLoginJSON json = new ReturnLoginJSON(false, "Oops, kesalahan telah terjadi pada sistem: Login", new Admin("0", "", ""));
            return json;
        }
    }
    
    public ReturnLoginJSON daftar(String nama, String nim, String password) {
        if(nama.length() > 1 && nim.length() >= 8 && password.length() >= 8){
            try {
                this.execute("INSERT INTO mahasiswa (nim, nama, semester_terakhir, password, logic_point, math_point, memory_point) VALUES (" + MySQLUtils.quote(this.getConnection(), nim) + ", " + MySQLUtils.quote(this.getConnection(), nama) + ", 0, " + MySQLUtils.quote(this.getConnection(), this.hashing.getHashed(this.hashing.getHashed(password))) + ", 0.0, 0.0, 0.0);");
                ReturnLoginJSON json = new ReturnLoginJSON(true, "Berhasil mendaftar!", new Mahasiswa(nim, nama, 0, password, 0.0, 0.0, 0.0));
                return json;
            } catch(Exception ex) {
                ReturnLoginJSON json = new ReturnLoginJSON(false, "Oops, ada kesalahan di dalam sistem, silahkan coba lagi.", new Mahasiswa("", "", 0, "", 0.0, 0.0, 0.0));
                return json;
            }
        }else{
            ReturnLoginJSON json = new ReturnLoginJSON(false, "Maaf, data tidak memenuhi syarat mendaftar. (Nama minimal 1 karakter, nim minimal 8 karakter, password minimal 8 karakter)", new Mahasiswa(nim, nama, 0, password, 0.0, 0.0, 0.0));
            return json;
        }
    }
    
    public ReturnLoginJSON daftarDosen(String nama, String nip, String password,int id_fokus) throws Exception {
        System.out.println(nip);
        if(nama.length() > 1 && nip.length() >= 8 && password.length() >= 8){
            try {
                this.execute("INSERT INTO dosen (nip, nama, id_fokus, password) VALUES (" + MySQLUtils.quote(this.getConnection(), nip) + ", " + MySQLUtils.quote(this.getConnection(), nama) + ", " +id_fokus+", " + MySQLUtils.quote(this.getConnection(), this.hashing.getHashed(this.hashing.getHashed(password)))+");" );
                ReturnLoginJSON json = new ReturnLoginJSON(true, "Berhasil mendaftar!", new Dosen(nip, nama, 0, password));
                return json;
            } catch(SQLException ex) {
                ReturnLoginJSON json = new ReturnLoginJSON(false, "Oops, ada kesalahan di dalam sistem, silahkan coba lagi.", new Dosen("", "", 0, ""));
                return json;
            }
        }else{
            ReturnLoginJSON json = new ReturnLoginJSON(false, "Maaf, data tidak memenuhi syarat mendaftar. (Nama minimal 1 karakter, nim minimal 8 karakter, password minimal 8 karakter)", new Dosen(nip, nama, 0, password));
            return json;
        }
    }
    
    public void showComboFokusDaftarDosen(DaftarDosenForm combo){
         String query = "select nama_fokus from fokus";
        
         try{
         Statement stat= connect.createStatement();
         ResultSet hasil= stat.executeQuery(query);
         
         while(hasil.next()){
             Object[] obj = new Object[10];
             obj[0]=hasil.getString("nama_fokus");
             
             
             combo.CmbFokus.addItem(obj[0]);
         }
         hasil.close();
         }
         catch(Exception e){
             JOptionPane.showMessageDialog(null, e);
         }
    }
    
    
}
