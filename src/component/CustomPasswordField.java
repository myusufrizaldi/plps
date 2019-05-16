package component;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.border.MatteBorder;

public class CustomPasswordField extends JPasswordField {
    
    public CustomPasswordField () {
        super();
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.decode("#cccccc")));
    }
    
}
