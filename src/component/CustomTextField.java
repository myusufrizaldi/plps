package component;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import sun.awt.AppContext;

public class CustomTextField extends JTextField {
    
    public CustomTextField () {
        super();
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.decode("#cccccc")));
    }
    
}
