package backend;

public class Session {
    private Mahasiswa mahasiswa;
    private Dosen dosen;
    
    public Session() {
        
    }
    
    public Session(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
    }
    
    public Session(Dosen dosen) {
        this.dosen = dosen;
    }
    
    public int getStatus() {
        if(this.mahasiswa == null && this.dosen == null) return 0;
        else if(this.mahasiswa != null) return 1;
        else return 2;
    }
    
    public Mahasiswa getMahasiswa() {
        return this.mahasiswa;
    }
    
    public Dosen getDosen() {
        return this.dosen;
    }
}
