package stringProcessing;

import backend.Database;
import backend.Security;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

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
    
    private Database db;
    
    public InputDatabaseMatkulJSON (boolean accepted, int lastValidState, String idMatkul, String namaMatkul, String sks, String semester, boolean wajib, String prioritas, String logicPointRate, String mathPointRate, String memoryPointRate) {
        super(accepted, lastValidState);
        
        this.db = new Database();
        try {
            this.idMatkul = Security.mysql_real_escape_string(db.getConnection(), idMatkul);
        } catch (Exception ex) {
            this.idMatkul = "";
            JOptionPane.showMessageDialog(null, ex.toString());
        }
        try {
            this.namaMatkul = Security.mysql_real_escape_string(db.getConnection(), namaMatkul);
        } catch (Exception ex) {
            this.namaMatkul = "";
            JOptionPane.showMessageDialog(null, ex.toString());
        }
        
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
