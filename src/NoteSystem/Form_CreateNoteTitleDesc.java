package NoteSystem;

import static NoteSystem.NoteSystem.defaultPath;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class Form_CreateNoteTitleDesc extends JDialog {

    NoteSystem mainForm;
    String title;
    String description;
    String tags;
    String[] textList;
    boolean added = false;

    public Form_CreateNoteTitleDesc(NoteSystem mainForm, String[] textList) {
        super();
        this.mainForm = mainForm;
        this.textList = textList;
        genGUI();
    }

    public void genGUI() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(300, 250);
        setTitle("Create New Note");

        JPanel contentPanel = (JPanel) getContentPane();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        //Error labels
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

        //Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

        JLabel titleLabel = new JLabel("One More Step...");
        titleLabel.setFocusable(true);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createHorizontalGlue());

        //Tile textbox panel
        JPanel titleFieldPanel = new JPanel();
        titleFieldPanel.setLayout(new BoxLayout(titleFieldPanel, BoxLayout.X_AXIS));

        final JTextField titleTextField = new JTextField("Title...") {
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                max.width = 250;
                return max;
            }
        };

        titleTextField.setPreferredSize(new Dimension(180, 25));
        titleTextField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
                JTextField field = (JTextField) fe.getSource();
                if (field.getText().equals("Title...")) {
                    field.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                JTextField field = (JTextField) fe.getSource();
                if (field.getText().equals("")) {
                    field.setText("Title...");
                }
            }

        });
        titleFieldPanel.add(titleTextField);
        titleFieldPanel.add(Box.createHorizontalGlue());

        //Description Panel
        JPanel descFieldPanel = new JPanel();
        descFieldPanel.setLayout(new BoxLayout(descFieldPanel, BoxLayout.X_AXIS));

        final JTextField descTextField = new JTextField("Description...") {
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                max.width = 250;
                return max;
            }
        };
        descTextField.setPreferredSize(new Dimension(180, 25));
        descTextField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
                JTextField field = (JTextField) fe.getSource();
                if (field.getText().equals("Description...")) {
                    field.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                JTextField field = (JTextField) fe.getSource();
                if (field.getText().equals("")) {
                    field.setText("Description...");
                }
            }

        });
        descFieldPanel.add(descTextField);
        descFieldPanel.add(Box.createHorizontalGlue());

        //Tag Field Panel
        JPanel tagsFieldPanel = new JPanel();
        tagsFieldPanel.setLayout(new BoxLayout(tagsFieldPanel, BoxLayout.X_AXIS));

        final JTextField tagsTextField = new JTextField("Tags (Seperate with Commas)...") {
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                max.width = 250;
                return max;
            }
        };
        tagsTextField.setPreferredSize(new Dimension(180, 25));
        tagsTextField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent fe) {
                JTextField field = (JTextField) fe.getSource();
                if (field.getText().equals("Tags (Seperate with Commas)...")) {
                    field.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {
                JTextField field = (JTextField) fe.getSource();
                if (field.getText().equals("")) {
                    field.setText("Tags (Seperate with Commas)...");
                }
            }

        });

        tagsFieldPanel.add(tagsTextField);
        tagsFieldPanel.add(Box.createHorizontalGlue());

        //Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                boolean error = false;
                //Checking title error
                if (titleTextField.getText() == null || titleTextField.getText().equals("title...")) {
                    error = true;
                    titleErrorLabel.setText("Please enter a title.");
                } else if (new File(defaultPath + "/" + titleTextField.getText()).exists()) {
                    error = true;
                    titleErrorLabel.setText("A note with this title exists! Please choose a new title!");
                } else {
                    titleErrorLabel.setText(" ");
                }

                //Checking description error
                if (descTextField.getText() == null || descTextField.getText().equals("description...")) {
                    error = true;
                    descriptionErrorLabel.setText("Please enter a description.");
                } else {
                    descriptionErrorLabel.setText(" ");
                }

                //Checking tags error
                String[] tags = new String[0];
                if (!tagsTextField.getText().equals("tags (seperate with commas)...")) {
                    tags = mainForm.noteList.formatTags(tagsTextField.getText());
                }
                if (tags == null || tags.length == 0) {
                    error = true;
                    tagErrorLabel.setText("Please enter atleast 1 tag.");
                } else {
                    tagErrorLabel.setText(" ");
                }

                if (!error) {
                    boolean success = false;
                    File newFolder = new File(defaultPath + "/" + titleTextField.getText());
                    newFolder.mkdir();
                    File noteInfo = new File(newFolder + "/data.info");
                    File textFile = new File(newFolder + "/" + "TextNote.txt");
                    try {
                        noteInfo.createNewFile();
                        textFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {

                        PrintStream fileWriter = new PrintStream(textFile);
                        for (int i = 0; i < textList.length; i++) {
                            fileWriter.println(textList[i]);
                        }
                        fileWriter.close();

                        Calendar currentDate = Calendar.getInstance();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd h:mm a");
                        String todaysDate = formatter.format(currentDate.getTime());

                        PrintStream infoPrinter = new PrintStream(noteInfo);
                        infoPrinter.println("**Please do not modify or delete this file.");
                        infoPrinter.println("**Settings may be modified in the settings menu of the application.");
                        infoPrinter.println("Title=" + titleTextField.getText());
                        infoPrinter.println("Description=" + descTextField.getText());
                        infoPrinter.println("Tags=" + tagsTextField.getText());
                        infoPrinter.println("Date=" + todaysDate);
                        infoPrinter.println("Size=" + mainForm.noteList.noteSize(newFolder));
                        infoPrinter.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Form_NoteAdded noteAdded = new Form_NoteAdded(mainForm);
                    added = true;
                    dispose();

                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }

        });

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalGlue());

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

        contentPanel.add(titlePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(20, 15)));
        contentPanel.add(titleFieldPanel);
        contentPanel.add(titleError);
        contentPanel.add(descFieldPanel);
        contentPanel.add(descriptionError);
        contentPanel.add(tagsFieldPanel);
        contentPanel.add(tagError);
        contentPanel.add(buttonPanel);

        titleTextField.setFocusable(false);
        titleTextField.setFocusable(true);

        setModal(true);
        setVisible(true);
    }
}
