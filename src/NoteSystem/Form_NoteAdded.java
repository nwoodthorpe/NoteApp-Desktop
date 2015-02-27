package NoteSystem;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;


public class Form_NoteAdded extends JDialog{
    NoteSystem mainForm;
    String type;

    public Form_NoteAdded(NoteSystem mainForm, String type) {
        super();
        this.mainForm = mainForm;
        this.type = type;
        genGUI();
    }
    
    public void genGUI(){
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setTitle("Note " + type);

        JPanel contentPanel = (JPanel) getContentPane();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));

        final JLabel titleLabel = new JLabel("Successfully " + type + "!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        textPanel.add(titleLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                mainForm.noteList.refreshNoteList();
                mainForm.noteList.sortList(mainForm.sortType);
                mainForm.populateTable();
                dispose();
            }
        });
        buttonPanel.add(okButton);

        contentPanel.add(textPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(40, 40)));
        contentPanel.add(buttonPanel);

        getRootPane().setDefaultButton(okButton);
        
        setModal(true);
        setVisible(true);
    }
}
