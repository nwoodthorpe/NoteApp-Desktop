package NoteSystem;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;


public class Form_ListNoteContents extends JDialog{
    int selected;
    File[] noteFiles;
    NoteSystem mainForm;
    
    public Form_ListNoteContents(NoteSystem mainForm, int selected){
        super();
        this.mainForm = mainForm;
        this.selected = selected;
        noteFiles = loadFiles();
        genGUI();
    }
    
    public File[] loadFiles(){
        Note note = mainForm.noteList.list.get(selected);
        File[] files = new File[note.numOfFiles];
        String title = note.title;
        
        File directory = new File(mainForm.noteList.defaultPath + "/" + title);
        File[] rawFiles = directory.listFiles();
        
        int fileNum = 0;
        for(int i = 0; i<rawFiles.length; i++){
            if(!rawFiles[i].getName().equals("data.info")){
                files[fileNum] = rawFiles[i];
                fileNum++;
            }
        }
        return files;
    }
    
    public void genGUI(){
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(300, 400);

        JPanel contentPanel = (JPanel) getContentPane();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        //Panel for the title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

        final JLabel titleLabel = new JLabel("Note Contents");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        
        //Panel for the scroll list
        JPanel scrollPanel = new JPanel();
        scrollPanel.setLayout(new BorderLayout());
        
        String[] strippedNames = new String[noteFiles.length];
        for(int i = 0; i<strippedNames.length; i++){
            strippedNames[i] = noteFiles[i].getName();
        }
        
        JList list = new JList(strippedNames);
        scrollPanel.add(list, BorderLayout.CENTER);
        
        contentPanel.add(titlePanel);
        contentPanel.add(scrollPanel);
        
        
        setModal(true);
        setVisible(true);
    }
}
