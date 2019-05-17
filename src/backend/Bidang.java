package backend;

public class Bidang {
    
    private String namaBidang;
    private String deskripsi;
    private int point;
    private int selisih;
    private double logicPoint;
    private double mathPoint;
    private double memoryPoint;
    
    public Bidang(String namaBidang, String deskripsi, double logicPoint, double mathPoint, double memoryPoint, int point, int selisih) {
        this.namaBidang = namaBidang;
        this.deskripsi = deskripsi;
        this.point = point;
        this.selisih = selisih;
        this.logicPoint = logicPoint;
        this.mathPoint = mathPoint;
        this.memoryPoint = memoryPoint;
    }

    public String getNamaBidang() {
        return this.namaBidang;
    }

    public int getPoint() {
        return this.point;
    }

    public int getSelisih() {
        return this.selisih;
    }

    public double getLogicPoint() {
        return this.logicPoint;
    }

    public double getMathPoint() {
        return this.mathPoint;
    }

    public double getMemoryPoint() {
        return this.memoryPoint;
    }

    public String getDeskripsi() {
        return this.deskripsi;
    }
    
    
    
}
