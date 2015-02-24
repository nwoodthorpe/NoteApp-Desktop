package NoteSystem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;



public class CustomTableRenderer extends DefaultTableCellRenderer {

    NoteSystem MainWindow;
    
    public CustomTableRenderer(NoteSystem MainWindow){
        this.MainWindow = MainWindow;
    }
    
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
        
        //If editableRow is not -2, it means editing is currently happening
        if(MainWindow.editableRow != -2){
            if(MainWindow.editableRow == row){
                setBackground(new Color(205, 197, 250));
            }else{
                setBackground(Color.white);
            }
        }else{
            if(table.getSelectedRow() != row)
                setBackground(Color.white);
        }
            
        return this;
    }
    
}
