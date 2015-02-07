package NoteSystem;

import static NoteSystem.NoteSystem.defaultPath;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;


public class Form_AddNote extends JDialog{
    NoteSystem mainForm;
    
    public Form_AddNote(NoteSystem mainForm){
        super();
        this.mainForm = mainForm;
        genGUI();
    }
    
    public void genGUI(){
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400, 300);

        //Error labels
        final JLabel selectErrorLabel = new JLabel(" ") {
            {
                setFont(new Font("Arial", Font.PLAIN, 14));
                setForeground(Color.RED);
            }
        };
        final JLabel titleErrorLabel = new JLabel(" ") {
            {
                setFont(new Font("Arial", Font.PLAIN, 14));
                setForeground(Color.RED);
            }
        };
        final JLabel descriptionErrorLabel = new JLabel(" ") {
            {
                setFont(new Font("Arial", Font.PLAIN, 14));
                setForeground(Color.RED);
            }
        };
        final JLabel tagErrorLabel = new JLabel(" ") {
            {
                setFont(new Font("Arial", Font.PLAIN, 14));
                setForeground(Color.RED);
            }
        };

        JPanel contentPanel = (JPanel) getContentPane();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        //Panel for the title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

        final JLabel titleLabel = new JLabel("Add a New Note");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);

        //Empty Panel for spacing
        JPanel spacerPanel = new JPanel();
        spacerPanel.setLayout(new BoxLayout(spacerPanel, BoxLayout.X_AXIS));
        JLabel blankLabel = new JLabel(" ");
        spacerPanel.add(blankLabel);

        //Panel with file selector
        JPanel selectFilePanel = new JPanel();
        selectFilePanel.setLayout(new BoxLayout(selectFilePanel, BoxLayout.X_AXIS));

        String filename = File.separator + "tmp";
        final JFileChooser fileChooser = new JFileChooser(new File(filename));

        final JLabel selectedFile = new JLabel("No File Selected");

        JButton selectButton = new JButton("Select File(s)...");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.showOpenDialog(null);
                if (fileChooser.getSelectedFiles().length > 1) {
                    selectedFile.setText("<html>" + fileChooser.getSelectedFile().getName() + "<br>"
                            + "and " + (fileChooser.getSelectedFiles().length - 1) + " more.</html>");
                } else if (!(fileChooser.getSelectedFiles().length == 0)) {
                    selectedFile.setText(fileChooser.getSelectedFile().getName());
                }
            }
        });
        selectFilePanel.add(selectButton);
        selectFilePanel.add(Box.createRigidArea(new Dimension(20, 2)));
        selectFilePanel.add(selectedFile);
        selectFilePanel.add(Box.createHorizontalGlue());

        //Panel for the title of the note
        JPanel noteTitlePanel = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }
        };
        noteTitlePanel.setLayout(new BoxLayout(noteTitlePanel, BoxLayout.X_AXIS));

        final JLabel noteTitleLabel = new JLabel("Title:  ");
        noteTitlePanel.add(noteTitleLabel);

        noteTitlePanel.add(Box.createRigidArea(new Dimension(90, 2)));

        final JTextField noteTitleField = new JTextField("");
        noteTitlePanel.add(noteTitleField);

        //Panel for the description of the note
        JPanel noteDescriptionPanel = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }
        };
        noteDescriptionPanel.setLayout(new BoxLayout(noteDescriptionPanel, BoxLayout.X_AXIS));

        final JLabel noteDescriptionLabel = new JLabel("Description:  ");
        noteDescriptionPanel.add(noteDescriptionLabel);

        noteDescriptionPanel.add(Box.createRigidArea(new Dimension(50, 2)));

        final JTextField noteDescriptionField = new JTextField("");
        noteDescriptionPanel.add(noteDescriptionField);

        //Panel for the tags of the note
        JPanel noteTagsPanel = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }
        };
        noteTagsPanel.setLayout(new BoxLayout(noteTagsPanel, BoxLayout.X_AXIS));

        final JLabel noteTagsLabel = new JLabel("Tags:  ");
        noteTagsPanel.add(noteTagsLabel);

        noteTagsPanel.add(Box.createRigidArea(new Dimension(87, 2)));

        final JTextField noteTagsField = new JTextField("");
        noteTagsPanel.add(noteTagsField);

        //Panel for the text below the tags label
        JPanel tagsPanel2 = new JPanel();
        tagsPanel2.setLayout(new BoxLayout(tagsPanel2, BoxLayout.X_AXIS));

        JLabel tagsLabel2 = new JLabel("(Seperate with Spaces)");
        tagsPanel2.add(tagsLabel2);
        tagsPanel2.add(Box.createHorizontalGlue());

        //Panel for the 2 buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                boolean error = false;

                //Checking file selector errors
                if (fileChooser.getSelectedFiles().length == 0) {
                    error = true;
                    selectErrorLabel.setText("Please select a file.");
                } else {
                    for (int i = 0; i < fileChooser.getSelectedFiles().length; i++) {
                        if (!mainForm.extensionAllowed(fileChooser.getSelectedFiles()[i])) {
                            error = true;
                        }
                    }
                    if (error) {
                        error = true;
                        selectErrorLabel.setText("File Extension Error");
                    }
                }

                if (!error) {
                    selectErrorLabel.setText(" ");
                }

                //Checking title error
                if (noteTitleField.getText() == null || noteTitleField.getText().length() == 0) {
                    error = true;
                    titleErrorLabel.setText("Please enter a title.");
                } else if (noteTitleField.getText().length() > 20) {
                    error = true;
                    titleErrorLabel.setText("Please enter a title less than 20 characters.");
                } else if (new File(defaultPath + "/" + noteTitleField.getText()).exists()) {
                    error = true;
                    titleErrorLabel.setText("A note with this title exists! Please choose a new title!");
                } else {
                    titleErrorLabel.setText(" ");
                }

                //Checking description error
                if (noteDescriptionField.getText() == null || noteDescriptionField.getText().length() == 0) {
                    error = true;
                    descriptionErrorLabel.setText("Please enter a description.");
                } else {
                    descriptionErrorLabel.setText(" ");
                }

                //Checking tags error
                String[] tags = mainForm.noteList.formatTags(noteTagsField.getText());
                if (tags == null || tags.length == 0) {
                    error = true;
                    tagErrorLabel.setText("Please enter atleast 1 tag.");
                } else {
                    tagErrorLabel.setText(" ");
                }

                if (!error) {
                    boolean success = false;
                    File newFolder = new File(defaultPath + "/" + noteTitleField.getText());
                    newFolder.mkdir();
                    File noteInfo = new File(newFolder + "/data.info");
                    try {
                        noteInfo.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        for (int i = 0; i < fileChooser.getSelectedFiles().length; i++) {
                            File originalFile = fileChooser.getSelectedFiles()[i];

                            String extension = "";
                            int j = originalFile.getPath().lastIndexOf('.');
                            if (j > 0) {
                                extension = originalFile.getPath().substring(j + 1).toLowerCase();
                            }

                            File destinationFile = new File(newFolder + "/" + i + "." + extension);
                            mainForm.copyFile(originalFile, destinationFile);
                            success = true;
                        }
                        Calendar currentDate = Calendar.getInstance();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd h:mm a");
                        String todaysDate = formatter.format(currentDate.getTime());

                        PrintStream infoPrinter = new PrintStream(noteInfo);
                        infoPrinter.println("**Please do not modify or delete this file.");
                        infoPrinter.println("**Settings may be modified in the settings menu of the application.");
                        infoPrinter.println("Title=" + noteTitleField.getText());
                        infoPrinter.println("Description=" + noteDescriptionField.getText());
                        infoPrinter.println("Tags=" + noteTagsField.getText());
                        infoPrinter.println("Date=" + todaysDate);
                        infoPrinter.println("Size=" + mainForm.noteList.noteSize(newFolder));
                        infoPrinter.close();

                    } catch (Exception e) {
                        System.out.println("File not found for some reason...");
                    }

                    if (success) {
                        Form_NoteAdded noteAdded = new Form_NoteAdded(mainForm);
                        dispose();
                    }
                }
            }
        });
        buttonPanel.add(addButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(30, 30)));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        });
        buttonPanel.add(cancelButton);

        JPanel selectError = new JPanel();
        selectError.setLayout(new BoxLayout(selectError, BoxLayout.X_AXIS));
        selectError.add(selectErrorLabel);
        selectError.add(Box.createHorizontalGlue());

        JPanel titleError = new JPanel();
        titleError.setLayout(new BoxLayout(titleError, BoxLayout.X_AXIS));
        titleError.add(titleErrorLabel);
        titleError.add(Box.createHorizontalGlue());

        JPanel descriptionError = new JPanel();
        descriptionError.setLayout(new BoxLayout(descriptionError, BoxLayout.X_AXIS));
        descriptionError.add(descriptionErrorLabel);
        descriptionError.add(Box.createHorizontalGlue());

        JPanel tagError = new JPanel();
        tagError.setLayout(new BoxLayout(tagError, BoxLayout.X_AXIS));
        tagError.add(tagErrorLabel);
        tagError.add(Box.createHorizontalGlue());

        //Add everything to our contentPanel (Master Panel)
        contentPanel.add(titlePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        contentPanel.add(selectFilePanel);
        contentPanel.add(selectError);
        contentPanel.add(noteTitlePanel);
        contentPanel.add(titleError);
        contentPanel.add(noteDescriptionPanel);
        contentPanel.add(descriptionError);
        contentPanel.add(noteTagsPanel);
        contentPanel.add(tagsPanel2);
        contentPanel.add(tagError);
        contentPanel.add(buttonPanel);
        setModal(true);
        setVisible(true);
    }
}
