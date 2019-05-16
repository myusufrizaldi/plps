package stringProcessing;

public class InputMatkulMahasiswaJSON extends JSON {
    
    private int nomor;
    private String idMatkul;
    private String nilai;
    
    public InputMatkulMahasiswaJSON(boolean accepted, int lastValidState, String nomor, String idMatkul, String nilai) {
        super(accepted, lastValidState);
        try{
            this.nomor = Integer.parseInt(nomor);
        } catch(Exception ex) {
            this.nomor = 0;
        }
        this.idMatkul = idMatkul;
        this.nilai = nilai;
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
    
}
