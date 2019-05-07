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
    
    public void convertnilai (){
        double score;
        if( nilai == "A"){
            score = 4;
        } else if( nilai == "AB" ){
            score = 3.5;
        } else if( nilai == "B" ){
            score = 3;
        } else if( nilai == "BC" ){
            score = 2.5;
        } else if( nilai == "C" ){
            score = 2;
        } else if( nilai == "D" ){
            score = 1;        
        } else if( nilai == "E" ){
            score = 0;
        }
    }
    
    public void calculatePoint() {
        //this.logicPoint = (double)this.nilai + this.mataKuliah.getLogicPointRate();
    }
}
