package component;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class CustomDangerButton extends JPanel {
    
    public CustomDangerButton() {
        super();
        this.addMouseListener(new CustomDangerButtonMouseListener(this));
        this.setBackground(Color.decode("#e74c3c"));
    }

}

class CustomDangerButtonMouseListener implements MouseListener {
    private final JComponent component;

    public CustomDangerButtonMouseListener(JComponent component) {
        this.component = component;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.component.setBackground(Color.decode("#d73c2c"));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.component.setBackground(Color.decode("#e74c3c"));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.component.setBackground(Color.decode("#c0392b"));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.component.setBackground(Color.decode("#d73c2c"));
    }
}