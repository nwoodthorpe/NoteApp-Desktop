package NoteSystem;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

public class CustomTableEditor extends DefaultCellEditor {

    public CustomTableEditor(final JTextField textField) {
        super(textField);
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(final FocusEvent e) {
                textField.selectAll();
            }
        });
    }
}
