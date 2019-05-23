package backend;

public class Dosen extends Akademika{
    int idFokus;
    
    public Dosen(String nip, String nama, int idFokus, String password) {
        super(nip, nama, password);
        this.idFokus = idFokus;
    }
    
    @Override
    public void updatePassword(String passwordLama, String passwordBaru) {
        if(passwordLama.equals(this.password)){
            try {
                this.db.runQuery("UPDATE dosen SET password=" + MySQLUtils.quote(this.db.getConnection(), passwordBaru) + " WHERE nip=" + MySQLUtils.quote(this.db.getConnection(), this.nomorInduk) + " AND password=" + MySQLUtils.quote(this.db.getConnection(), passwordLama) + ";");
                this.password = passwordBaru;
                
                System.out.println("Password berhasil diupdate");
            } catch(Exception ex) {
                System.out.println("Password gagal diupdate");
            }
            
        }
    }
    
    @Override
    public void updateData(String nip, String nama, int idFokus, String password) {
        if(password.equals(this.password)){
            try {
                this.db.runQuery("UPDATE dosen SET nip=" + MySQLUtils.quote(this.db.getConnection(), this.nomorInduk) + ", nama=" + MySQLUtils.quote(this.db.getConnection(), nama) + ", id_fokus=" + MySQLUtils.mysql_real_escape_string(this.db.getConnection(), String.valueOf(idFokus) + " WHERE nip=" + MySQLUtils.quote(this.db.getConnection(), nip) + " AND password=" + MySQLUtils.quote(this.db.getConnection(), password) + ";"));
                this.nomorInduk = nip;
                this.nama = nama;
                this.idFokus = idFokus;
                System.out.println("Sukses mengupdate");
            } catch (Exception ex){
                System.out.println("Gagal mengupdate");
            }
            
        }
    }
    
}
