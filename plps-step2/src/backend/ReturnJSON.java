package backend;

public class ReturnJSON {
    
    private boolean isSuccess;
    private String message;
    
    public ReturnJSON (boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }
    
    public boolean isSuccess() {
        return this.isSuccess;
    }
    
    public String getMessage() {
        return this.message;
    }
    
}
