package backend;

import java.util.HashSet;

public class Mahasiswa extends Akademika {
    private int semesterTerakhir;
    private double logicPoint;
    private double mathPoint;
    private double memoryPoint;
    private HashSet<AmbilMataKuliah> ambilMataKuliah;
    
    private Security security;
    
    public Mahasiswa(String nim, String nama, int semesterTerakhir, String password, double logicPoint, double mathPoint, double memoryPoint) {
        super(nim, nama, password);
        this.semesterTerakhir = semesterTerakhir;
        this.logicPoint = logicPoint;
        this.mathPoint = mathPoint;
        this.memoryPoint = memoryPoint;
    }
    
    @Override
    public void updatePassword(String passwordLama, String passwordBaru) {
        if(passwordLama.equals(this.password)){
            try {
                this.db.runQuery("UPDATE dosen SET password=" + MySQLUtils.quote(this.db.getConnection(), passwordBaru) + " WHERE nim=" + MySQLUtils.quote(this.db.getConnection(), this.nomorInduk) + " AND password=" + MySQLUtils.quote(this.db.getConnection(), passwordLama) + ";");
                this.password = passwordBaru;
                
                System.out.println("Password berhasil diupdate");
            } catch(Exception ex) {
                System.out.println("Password gagal diupdate");
            }
            
        }
    }
    
    @Override
    public void updateData(String nim, String nama, int semesterTerakhir, String password) {
        if(password.equals(this.password)){
            try {
                this.db.runQuery("UPDATE mahasiswa SET nim=" + MySQLUtils.quote(this.db.getConnection(), this.nomorInduk) + ", nama=" + MySQLUtils.quote(this.db.getConnection(), nama) + ", semester_terakhir=" + MySQLUtils.mysql_real_escape_string(this.db.getConnection(), String.valueOf(semesterTerakhir) + " WHERE nim=" + MySQLUtils.quote(this.db.getConnection(), nim) + " AND password=" + MySQLUtils.quote(this.db.getConnection(), password) + ";"));
                this.nomorInduk = nim;
                this.nama = nama;
                this.semesterTerakhir = semesterTerakhir;
                System.out.println("Sukses mengupdate");
            } catch (Exception ex){
                System.out.println("Gagal mengupdate");
            }
            
        }
    }
}
