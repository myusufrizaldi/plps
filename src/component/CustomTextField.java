package component;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import java.awt.Component;

public class CustomTextField extends JTextField {
    
    public CustomTextField () {
        super();
        this.addFocusListener(new CustomTextFieldFocusListener(this));
        this.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.decode("#cccccc")));
    }
    
}

class CustomTextFieldFocusListener implements FocusListener {

    private CustomTextField component;
    
    public CustomTextFieldFocusListener (CustomTextField component){
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