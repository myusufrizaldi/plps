package driver;

import frontend.*;
import backend.*;

public class Driver {
    private MainForm form;
    
    public Database db;
    
    public Driver () {
        this.form = new MainForm();
        
        this.db = new Database();
    }
    
    public static void main(String[] args) {
        Driver main = new Driver();
        
        main.form.setVisible(true);
    }
}
