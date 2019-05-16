package frontend;

import backend.Database;

public class PerangkatLunakPerencanaanStudi {

    public static void main(String[] args) {
        WelcomePage welcomePage = new WelcomePage();
        
        Database db = new Database();
        
        db.runQuery("SELECT * FROM rekening");
        db.printAllResult();
    }
    
}
