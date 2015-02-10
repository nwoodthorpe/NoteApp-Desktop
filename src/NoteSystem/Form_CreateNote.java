package NoteSystem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;


public class Form_CreateNote extends JDialog{
    NoteSystem mainForm;
    public Form_CreateNote(NoteSystem mainForm){
        super();
        this.mainForm = mainForm;
        genGUI();
    }
    
    public void genGUI(){
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setTitle("Create New Note");

        JPanel contentPanel = (JPanel) getContentPane();
        contentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        //Panel for the textbox
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
        
        final JTextArea textArea = new JTextArea();
        textArea.setBorder(BorderFactory.createLineBorder(Color.black));
        textPanel.add(textArea);
        
        //Panel for the butttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        
        final JButton saveButton = new JButton("Save...");
        saveButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                String[] textList = textArea.getText().split("\\r?\\n");
                if(textList.length != 0){
                    Form_CreateNoteTitleDesc form = new Form_CreateNoteTitleDesc(mainForm, textList);
                    if(form.added)
                        dispose();
                }
            }
            
        });
        buttonPanel.add(saveButton);
        
        final JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
            
        });
        buttonPanel.add(Box.createRigidArea(new Dimension(30, 30)));
        buttonPanel.add(cancelButton);
        
        contentPanel.add(textPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(10, 10)));
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        
        setModal(true);
        setVisible(true);
    }
}
