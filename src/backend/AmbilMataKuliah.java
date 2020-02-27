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
        this.calculatePoint();
    }
    
    public static double convertNilai (String nilai){
        double score = 0;
        if(nilai.equals("A")){
            score = 4;
        } else if(nilai.equals("AB") ){
            score = 3.5;
        } else if(nilai.equals("B")){
            score = 3;
        } else if(nilai.equals("BC") ){
            score = 2.5;
        } else if(nilai.equals("C") ){
            score = 2;
        } else if(nilai.equals("D") ){
            score = 1;        
        }
        
        return score;
    }
    
    public void calculatePoint() {
        this.logicPoint = this.convertNilai(this.nilai) * this.mataKuliah.getLogicPointRate();
        this.mathPoint = this.convertNilai(this.nilai) * this.mataKuliah.getMathPointRate();
        this.memoryPoint = this.convertNilai(this.nilai) * this.mataKuliah.getMemoryPointRate();
    }
}
