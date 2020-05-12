package backend;

import java.util.ArrayList;

public class MataKuliah {
    private String idMatkul;
    private String nama;
    private int semester;
    private boolean wajib;
    private int prioritas;
    private ArrayList<String> idMatkulSyarat;
    private double logicPointRate;
    private double mathPointRate;
    private double memoryPointRate;
    
    public MataKuliah (String idMatkul, String nama, int semester, boolean wajib, int prioritas, ArrayList<String> idMatkulSyarat) {
        this.idMatkul = idMatkul;
        this.nama = nama;
        this.semester = semester;
        this.wajib = wajib;
        this.prioritas = prioritas;
        this.idMatkulSyarat = idMatkulSyarat;
    }

    public String getIdMatkul() {
        return this.idMatkul;
    }

    public String getNama() {
        return this.nama;
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

    
    
    public ArrayList<String> getIdMatkulSyarat() {
        return this.idMatkulSyarat;
    }
    
    
}
