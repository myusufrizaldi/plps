package stringProcessing;

public class InputDatabaseMatkulJSON extends JSON {
    
    private String idMatkul;
    private String namaMatkul;
    private boolean wajib;
    private int sks;
    private int semester;
    private int prioritas;
    private double logicPointRate;
    private double mathPointRate;
    private double memoryPointRate;
    
    public InputDatabaseMatkulJSON (boolean accepted, int lastValidState, String idMatkul, String namaMatkul, boolean wajib, String sks, String semester, String prioritas, String logicPointRate, String mathPointRate, String memoryPointRate) {
        super(accepted, lastValidState);
        this.idMatkul = idMatkul;
        this.namaMatkul = namaMatkul;
        this.wajib = wajib;
        
        try{
            this.sks = Integer.parseInt(sks);
        } catch (Exception ex) {
            this.sks = 0;
        }
        try{
            this.semester = Integer.parseInt(semester);
        } catch (Exception ex) {
            this.semester = 0;
        }
        try{
            this.prioritas = Integer.parseInt(prioritas);
        } catch (Exception ex) {
            this.prioritas = 0;
        }
        try{
            this.logicPointRate = Double.parseDouble(logicPointRate);
        } catch (Exception ex) {
            this.logicPointRate = 0;
        }
        try{
            this.mathPointRate = Double.parseDouble(mathPointRate);
        } catch (Exception ex) {
            this.mathPointRate = 0;
        }
        try{
            this.memoryPointRate = Double.parseDouble(memoryPointRate);
        } catch (Exception ex) {
            this.memoryPointRate = 0;
        }
    }

    public String getIdMatkul() {
        return this.idMatkul;
    }

    public String getNamaMatkul() {
        return this.namaMatkul;
    }

    public boolean isWajib() {
        return this.wajib;
    }

    public int getSks() {
        return this.sks;
    }

    public int getSemester() {
        return this.semester;
    }

    public int getPrioritas() {
        return this.prioritas;
    }

    public double getLogicPointRate() {
        return this.logicPointRate;
    }

    public double getMathPointRate() {
        return this.mathPointRate;
    }

    public double getMemoryPointRate() {
        return this.memoryPointRate;
    }
    
    
}
