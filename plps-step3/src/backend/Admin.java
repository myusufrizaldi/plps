package backend;

public class Admin extends Akademika{
    int idFokus;
    
    public Admin(String nia, String nama, String password) {
        super(nia, nama, password);
//        this.idFokus = idFokus;
    }
    
    @Override
    public void updatePassword(String passwordLama, String passwordBaru) {
        if(passwordLama.equals(this.password)){
            try {
                this.db.runQuery("UPDATE admin SET password=" + MySQLUtils.quote(this.db.getConnection(), passwordBaru) + " WHERE nia=" + MySQLUtils.quote(this.db.getConnection(), this.nomorInduk) + " AND password=" + MySQLUtils.quote(this.db.getConnection(), passwordLama) + ";");
                this.password = passwordBaru;
                
                System.out.println("Password berhasil diupdate");
            } catch(Exception ex) {
                System.out.println("Password gagal diupdate");
            }
            
        }
    }
    
    @Override
    public void updateData(String nia, String nama, int idFokus, String password) {
        if(password.equals(this.password)){
            try {
                this.db.runQuery("UPDATE admin SET nia=" + MySQLUtils.quote(this.db.getConnection(), this.nomorInduk) + ", nama=" + MySQLUtils.quote(this.db.getConnection(), nama) + ", id_fokus=" + MySQLUtils.mysql_real_escape_string(this.db.getConnection(), String.valueOf(idFokus) + " WHERE nia=" + MySQLUtils.quote(this.db.getConnection(), nia) + " AND password=" + MySQLUtils.quote(this.db.getConnection(), password) + ";"));
                this.nomorInduk = nia;
                this.nama = nama;
                this.idFokus = idFokus;
                System.out.println("Sukses mengupdate");
            } catch (Exception ex){
                System.out.println("Gagal mengupdate");
            }
            
        }
    }
    
}
