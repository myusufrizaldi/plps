package backend;

import java.util.ArrayList;

public class MataKuliah {
    private String idMatkul;
    private String namaMatkul;
    private int sks;
    private int semester;
    private boolean wajib;
    private int prioritas;
    private double logicPointRate;
    private double mathPointRate;
    private double memoryPointRate;
    
    public MataKuliah (String idMatkul, String namaMatkul, int sks, int semester, boolean wajib, int prioritas, double logicPointRate, double mathPointRate, double memoryPointRate) {
        this.idMatkul = idMatkul;
        this.namaMatkul = namaMatkul;
        this.sks = sks;
        this.semester = semester;
        this.wajib = wajib;
        this.prioritas = prioritas;
        this.logicPointRate = logicPointRate;
        this.mathPointRate = mathPointRate;
        this.memoryPointRate = memoryPointRate;
    }

    public String getIdMatkul() {
        return this.idMatkul;
    }

    public String getNamaMatkul() {
        return this.namaMatkul;
    }

    public int getSks() {
        return this.sks;
    }
    
    public int getSemester() {
        return this.semester;
    }

    public boolean isWajib() {
        return this.wajib;
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
