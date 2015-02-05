package NoteSystem;

import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomTableRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            setBorder(BorderFactory.createLineBorder(table.getSelectionBackground()));
        } else {
            setBorder(BorderFactory.createLineBorder(table.getBackground()));
        }
        
        if(column == 0){
            setFont(new Font(null, Font.BOLD, 14));
        }
            
        return this;
    }
}
