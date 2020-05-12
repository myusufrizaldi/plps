package stringProcessing;

public class AutomataJSON {
    
    boolean accepted;
    int nomor;
    String idMatkul;
    String nilai;
    int lastValidState;
    
    public AutomataJSON(boolean accepted, String nomor, String idMatkul, String nilai, int lastValidState) {
        this.nomor = Integer.parseInt(nomor);
        this.accepted = accepted;
        this.idMatkul = idMatkul;
        this.nilai = nilai;
        this.lastValidState = lastValidState;
    }

    public boolean isAccepted() {
        return this.accepted;
    }
    
    public int getNomor() {
        return this.nomor;
    }

    public String getIdMatkul() {
        return this.idMatkul;
    }

    public String getNilai() {
        return this.nilai;
    }
    
    public int getLastValidState() {
        return this.lastValidState;
    }
    
}
