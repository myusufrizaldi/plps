package backend;

public class Mahasiswa extends Akademika {
    private int semesterTerakhir;
    
    public Mahasiswa(String nim, String nama, int semesterTerakhir, String password) {
        this.nomorInduk = nim;
        this.nama = nama;
        this.semesterTerakhir = semesterTerakhir;
        this.password = password;
    }
    
    @Override
    public void updateData(String nim, String nama, int semesterTerakhir, String password) {
        if(password.equals(this.password)){
            this.nomorInduk = nim;
            this.nama = nama;
            this.semesterTerakhir = semesterTerakhir;
        }
        
    }
}
