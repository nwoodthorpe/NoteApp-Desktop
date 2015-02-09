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


public class Form_DeleteNote extends JDialog{
    public NoteSystem mainForm;
    public boolean returnValue = false;
    
    public Form_DeleteNote(NoteSystem mainForm){
        super();
        this.mainForm = mainForm;
        genGUI();
    }
    
    public void genGUI(){
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(300, 200);

        JPanel contentPanel = (JPanel) getContentPane();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        //Panel for the title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

        final JLabel titleLabel = new JLabel("Are you sure?");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        //Panel for the 2 buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        
        JButton yesButton = new JButton("Yes");
        yesButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                returnValue = true;
                dispose();
            }
            
        });
        
        JButton noButton = new JButton("No");
        
        noButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
            
        });
        
        buttonPanel.add(yesButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(50,50)));
        buttonPanel.add(noButton);
        
        contentPanel.add(titlePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(40, 40)));
        contentPanel.add(buttonPanel);
        
        setModal(true);
        setVisible(true);
    }
}
