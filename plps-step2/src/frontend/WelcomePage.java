package frontend;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;

public class WelcomePage{
    
    JFrame page;
    JDesktopPane loginPane;
    JInternalFrame loginFrame;
    
    public WelcomePage() {
        this.page = new JFrame();
        this.page.setVisible(true);
        this.page.setSize(640, 640);
        this.page.setTitle("PLPS");
        this.setFullscreen(true);
        this.page.setBackground(Color.gray);
        
        
        this.loginFrame = new JInternalFrame("Login");
        this.loginFrame.setVisible(true);
        this.loginFrame.setSize(450, 600);
        //this.loginFrame.setMaximumSize(300, 600);
        this.loginFrame.setLocation((this.page.getWidth() / 2) - (this.loginFrame.getWidth() / 2), (this.page.getHeight() / 2) - (this.loginFrame.getHeight() / 2));
        this.loginFrame.setBackground(Color.white);
        
        
        
        this.loginPane = new JDesktopPane();
        
        this.loginPane.setVisible(true);
        this.loginPane.setSize(300, 600);
        this.loginPane.setLocation((this.page.getWidth() / 2) - 150, (this.page.getHeight() / 2) - 300);
        this.loginPane.setBackground(Color.gray);
        this.loginPane.add(loginFrame);
        this.page.add(loginPane);
        
        
    }
    
    public void setFullscreen(Boolean value) {
        if(value) this.page.setExtendedState(JFrame.MAXIMIZED_BOTH);
        else this.page.setExtendedState(JFrame.NORMAL);
        
    }
}
