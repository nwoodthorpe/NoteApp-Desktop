package NoteSystem;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class Form_ListNoteContents extends JDialog {

    int selected;
    File[] noteFiles;
    NoteSystem mainForm;

    public Form_ListNoteContents(NoteSystem mainForm, int selected) {
        super();
        this.mainForm = mainForm;
        this.selected = selected;
        noteFiles = loadFiles();
        genGUI();
    }

    public File[] loadFiles() {
        Note note = mainForm.noteList.list.get(selected);
        File[] files = new File[note.numOfFiles];
        String title = note.title;

        File directory = new File(mainForm.noteList.defaultPath + "/" + title);
        File[] rawFiles = directory.listFiles();

        int fileNum = 0;
        for (int i = 0; i < rawFiles.length; i++) {
            if (!rawFiles[i].getName().equals("data.info")) {
                files[fileNum] = rawFiles[i];
                fileNum++;
            }
        }
        return files;
    }

    private void openFileViewer(File file) {
        try {
            
            Desktop.getDesktop().open(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void openFileExplorer(File file){
        try {
            Runtime.getRuntime().exec("explorer.exe /select,"+file.getPath());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void genGUI() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 400);

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
        for (int i = 0; i < strippedNames.length; i++) {
            strippedNames[i] = noteFiles[i].getName();
        }

        final JButton explorerButton = new JButton("Explorer");
        final JButton viewerButton = new JButton("Default Viewer");

        final JList list = new JList(strippedNames);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 2) {
                    openFileViewer(noteFiles[list.getSelectedIndex()]);
                }
                if (list.getSelectedIndex() != -1) {
                    explorerButton.setEnabled(true);
                    viewerButton.setEnabled(true);
                } else {
                    explorerButton.setEnabled(false);
                    viewerButton.setEnabled(false);
                }

            }
        });
        scrollPanel.add(list, BorderLayout.CENTER);

        //Panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JLabel openLabel = new JLabel("Open using:");

        explorerButton.setEnabled(false);
        explorerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                openFileExplorer(noteFiles[list.getSelectedIndex()]);
            }

        });

        viewerButton.setEnabled(false);
        viewerButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                openFileViewer(noteFiles[list.getSelectedIndex()]);
            }

        });

        buttonPanel.add(openLabel);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(explorerButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        buttonPanel.add(viewerButton);

        contentPanel.add(titlePanel);
        contentPanel.add(scrollPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        contentPanel.add(buttonPanel);

        setModal(true);
        setVisible(true);
    }
}
