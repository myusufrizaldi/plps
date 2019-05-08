package backend;

public class LoginJSON {
    
    private boolean isSuccess;
    private String message;
    
    public LoginJSON (boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
    
}
