package backend;

public class AmbilMataKuliah {
    private MataKuliah mataKuliah;
    private String nilai;
    private double logicPoint;
    private double mathPoint;
    private double memoryPoint;
    
    public AmbilMataKuliah(MataKuliah mataKuliah, String nilai) {
        this.mataKuliah = mataKuliah;
        this.nilai = nilai;
    }
    
    
    
    public void calculatePoint() {
        //this.logicPoint = (double)this.nilai + this.mataKuliah.getLogicPointRate();
    }
}
