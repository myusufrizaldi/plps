/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

/**
 *
 * @author Acer
 */
public class ReturnLoginJSON extends ReturnJSON {
    
    private Session session;
    
    public ReturnLoginJSON(boolean isSuccess, String message, Mahasiswa mahasiswa) {
        super(isSuccess, message);
        this.session = new Session(mahasiswa);
    }
    
    public ReturnLoginJSON(boolean isSuccess, String message, Dosen dosen) {
        super(isSuccess, message);
        this.session = new Session(dosen);
    }
    
    public ReturnLoginJSON(boolean isSuccess, String message, Admin admin){
        super(isSuccess, message);
        this.session = new Session(admin);
    }

    public Session getSession() {
        return this.session;
    }
    
}
