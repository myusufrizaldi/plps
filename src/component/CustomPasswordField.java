package component;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.border.MatteBorder;

public class CustomPasswordField extends JPasswordField {
    
    public CustomPasswordField () {
        super();
        this.addFocusListener(new CustomPasswordFieldFocusListener(this));
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.decode("#cccccc")));
    }
    
}

class CustomPasswordFieldFocusListener implements FocusListener {

    private CustomPasswordField component;
    
    public CustomPasswordFieldFocusListener (CustomPasswordField component){
        this.component = component;
    }
    
    @Override
    public void focusGained(FocusEvent e) {
        this.component.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.decode("#3498db")));
    }

    @Override
    public void focusLost(FocusEvent e) {
        this.component.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.decode("#cccccc")));
    }
    
}
