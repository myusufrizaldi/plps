package component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CustomPrimaryButton extends JPanel {
    
    public CustomPrimaryButton() {
        super();
        this.addMouseListener(new CustomPrimaryButtonMouseListener(this));
        this.setBackground(Color.decode("#0099ff"));
    }

}

class CustomPrimaryButtonMouseListener implements MouseListener {
    private final JComponent component;

    public CustomPrimaryButtonMouseListener(JComponent component) {
        this.component = component;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.component.setBackground(Color.decode("#2389ef"));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.component.setBackground(Color.decode("#3399ff"));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.component.setBackground(Color.decode("#1379df"));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.component.setBackground(Color.decode("#1389eF"));
    }
}