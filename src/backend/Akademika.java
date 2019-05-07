package backend;

public abstract class Akademika {
    protected String nomorInduk;
    protected String nama;
    protected String password;
    protected Database db;
    
    public String getNomorInduk() {
        return this.nomorInduk;
    }
    
    public String getNama() {
        return this.nama;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void updatePassword(String passwordLama, String passwordBaru) {
        if(passwordLama.equals(this.password)){
            this.password = passwordBaru;
        }
    }
    
    public abstract void updateData(String nomorInduk, String nama, int c, String password);
}
