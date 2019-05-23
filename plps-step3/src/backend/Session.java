package backend;

public class Session {
    private Mahasiswa mahasiswa;
    private Dosen dosen;
    private Admin admin;
    
    public Session() {
        
    }
    
    public Session(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
    }
    
    public Session(Dosen dosen) {
        this.dosen = dosen;
    }
    
    public Session(Admin admin) {
        this.admin = admin;
    }
    
    public int getStatus() {
        if(this.mahasiswa == null && this.dosen == null && this.admin == null) return 0;
        else if(this.mahasiswa != null) return 1;
        else if(this.dosen != null) return 2;
        else return 3;
    }
    
    public Mahasiswa getMahasiswa() {
        return this.mahasiswa;
    }
    
    public Dosen getDosen() {
        return this.dosen;
    }
    
    public Admin getadmin(){
        return this.admin;
    }
}
