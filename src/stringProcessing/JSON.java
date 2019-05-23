package stringProcessing;

public class JSON {
    
    private boolean accepted;
    private int lastValidState;
    
    public JSON(boolean accepted, int lastValidState) {
        this.accepted = accepted;
        this.lastValidState = lastValidState;
    }
    
    public boolean isAccepted() {
        return this.accepted;
    }
    
    public int getLastValidState() {
        return this.lastValidState;
    }

    
}
