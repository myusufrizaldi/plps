package backend;

public abstract class Akademika implements Account {
    protected String nomorInduk;
    protected String nama;
    protected String password;
    protected Database db;
    
    public Akademika(String nomorInduk, String nama, String password) {
        this.db = new Database();
        this.nomorInduk = nomorInduk;
        this.nama = nama;
        this.password = password;
    }
    
    public String getNomorInduk() {
        return this.nomorInduk;
    }
    
    public String getNama() {
        return this.nama;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public abstract void updateData(String nomorInduk, String nama, int c, String password);
}
