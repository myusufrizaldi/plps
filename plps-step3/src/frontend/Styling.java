package frontend;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public final class Styling {
    public static Dimension WXGA_SCREEN = new Dimension(1360, 768);
    
    
    public static void refreshSize(JFrame frame) {
        if(frame.getExtendedState() == JFrame.MAXIMIZED_BOTH){
            //this.setSize(this.getWidth(), this.getHeight() - 1);
            frame.setExtendedState(JFrame.NORMAL);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }else{
            frame.setSize(frame.getWidth(), frame.getHeight() - 1);
            frame.setSize(frame.getWidth(), frame.getHeight() + 1);
        }
    }
    
    public static void centeringPanel(JFrame frame, JPanel panel) {
        panel.setLocation((int) ((frame.getWidth() / 2) - (panel.getWidth() / 2)), (int) ((frame.getHeight() / 2) - (panel.getHeight() / 2)));
    }
    
}
