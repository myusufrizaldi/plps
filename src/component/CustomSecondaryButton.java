package component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CustomSecondaryButton extends JPanel {
    
    public CustomSecondaryButton() {
        super();
        this.addMouseListener(new SecondaryButtonMouseListener(this));
    }

}

class SecondaryButtonMouseListener implements MouseListener {
    private final JComponent component;

    public SecondaryButtonMouseListener(JComponent component) {
        this.component = component;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.component.setBackground(Color.decode("#bdc3c7"));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.component.setBackground(Color.decode("#ecf0f1"));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.component.setBackground(Color.decode("#adb3b7"));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.component.setBackground(Color.decode("#bdc3c7"));
    }
}
