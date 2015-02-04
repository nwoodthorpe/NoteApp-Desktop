package NoteSystem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Nathaniel
 */

public class NoteSystem extends JFrame {

    public NoteList noteList;
    public String[] columnNames = {"Date",
        "Title",
        "Description",
        "Num. of Files"
    };

    Object[][] data = {
        {"", "", "", ""}
    };

    public String[] allowedExtensions
            = {"png", "bmp", "jpeg", "doc", "docx", "jpg", "txt"};

    DefaultTableModel dtm;
    JTable selectionTable;
    static String defaultPath = "C:\\Users\\Nathaniel\\Documents\\NetBeansProjects\\NoteSystem";
    int sortType;
    protected void initUI() {
        JFrame frame = new JFrame();
        JPanel contentPanel = (JPanel) frame.getContentPane();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //TitlePanel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

        JLabel title = new JLabel("Title will be here");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        titlePanel.add(title);

        //topButtonBar
        JPanel topButtonBar = new JPanel();
        topButtonBar.setLayout(new BoxLayout(topButtonBar, BoxLayout.X_AXIS));

        //Menubar 
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem settingsButton = new JMenuItem("Settings");
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                settingsButtonPressed();
            }
        });
        JMenuItem exitButton = new JMenuItem("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
        fileMenu.add(settingsButton);
        fileMenu.add(exitButton);

        menuBar.add(fileMenu);

        //Sort Label
        JLabel sortLabel = new JLabel("Sort by:    ");

        //Sort Combobox
        JComboBox sortBox = new JComboBox() {
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                max.width = getPreferredSize().width;
                return max;
            }
        };
        //SortBox Action Listener
        sortBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JComboBox x = (JComboBox) ae.getSource();
            }
        });
        //Add items to sortbox
        sortBox.addItem("Date - Most Recent");
        sortBox.addItem("Date - Oldest");
        sortBox.addItem("File Size - Largest");
        sortBox.addItem("File Size - Smallest");
        sortBox.addItem("Most Viewed");

        //Separator lines
        JSeparator line = new JSeparator(SwingConstants.VERTICAL) {
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }
        };
        line.setPreferredSize(new Dimension(2, 30));

        JSeparator secondLine = new JSeparator(SwingConstants.VERTICAL) {
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                max.width = 20;
                return max;
            }
        };
        secondLine.setPreferredSize(new Dimension(2, 30));

        //Search textbox
        JTextField searchField = new JTextField("Search...") {
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                max.width = 100;
                return max;
            }
        };
        searchField.setPreferredSize(new Dimension(180, 25));
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent fe) {
                JTextField search = (JTextField) fe.getSource();
                if (search.getText().equals("Search...")) {
                    search.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent fe) {

            }
        });

        //Add button
        JButton addButton = new JButton("Add...");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addNoteButtonPressed();
            }
        });

        //Add all the components to our panel
        topButtonBar.add(sortLabel);
        topButtonBar.add(sortBox);
        topButtonBar.add(Box.createRigidArea(new Dimension(20, 30)));
        topButtonBar.add(line);
        topButtonBar.add(Box.createRigidArea(new Dimension(20, 30)));
        topButtonBar.add(new JLabel("Search:    "));
        topButtonBar.add(searchField);
        topButtonBar.add(Box.createRigidArea(new Dimension(20, 30)));
        topButtonBar.add(secondLine);
        topButtonBar.add(Box.createRigidArea(new Dimension(20, 30)));
        topButtonBar.add(addButton);

        //Spacer panel
        JPanel spacerPanel = new JPanel();
        spacerPanel.setLayout(new BoxLayout(spacerPanel, BoxLayout.X_AXIS));
        JLabel blankLabel = new JLabel(" ");
        spacerPanel.add(blankLabel);

        //tablePanel, the panel that will hold our table
        JPanel tablePanel = new JPanel();
        //Plan to add some sort of comtrol to the left or right of the table
        //So we're using an X_AXIS Boxlayout to make this easy.
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.X_AXIS));

        //Set up table and tablemodel
        dtm = new DefaultTableModel(data, columnNames);
        selectionTable = new JTable(dtm) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        selectionTable.setRowHeight(40);
        selectionTable.setShowVerticalLines(false);
        JScrollPane tableHolder = new JScrollPane(selectionTable);

        tablePanel.add(tableHolder);

        //Add everything to the contentPanel
        contentPanel.add(titlePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        contentPanel.add(topButtonBar);
        contentPanel.add(spacerPanel);
        contentPanel.add(tablePanel);

        frame.setJMenuBar(menuBar);
        frame.setSize(600, 600);
        frame.setVisible(true);

        noteList = new NoteList(defaultPath);
        try {
            noteList.refreshNoteList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        populateTable();
    }

    //Reads noteList and populates table with data
    public void populateTable() {
        noteList.sortList(sortType);
        ArrayList<Note> list = noteList.list;
        ((DefaultTableModel) selectionTable.getModel()).setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            String[] row = new String[4];
            Note currentNote = list.get(i);
            //Date, Title,Description, Format
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
            row[0] = sdf.format(currentNote.date.getTime());
            row[1] = currentNote.title;
            row[2] = currentNote.description;
            row[3] = Integer.toString(currentNote.numOfFiles);
            ((DefaultTableModel) selectionTable.getModel()).addRow(new java.util.Vector<String>(java.util.Arrays.asList(row)));

        }
    }

    //Checks for settings file
    public void startUpSettingsCheck() throws Exception {
        File settingsFile = new File("settings.info");

        //Make settings file if it does not exist yet.
        if (!settingsFile.exists()) {
            settingsFile.createNewFile();
            Path currentRelativePath = Paths.get("");
            String currentPath = currentRelativePath.toAbsolutePath().toString();
            PrintStream settingsPrinter = new PrintStream(settingsFile);
            settingsPrinter.println("**Please do not modify or delete this file.");
            settingsPrinter.println("**Settings may be modified in the settings menu of the application.");
            settingsPrinter.println("DefaultPath=" + currentPath + "\\save");
            settingsPrinter.close();
        }

        Scanner settingsReader = new Scanner(settingsFile);
        ArrayList<String> settingsList = new ArrayList<>();
        while (settingsReader.hasNext()) {
            settingsList.add(settingsReader.nextLine());
        }

        if (settingsList.size() != 3) {
            System.out.println("FILE CORRUPT - ADD STUFF HERE LATER");
        } else {
            int lineSize = settingsList.get(2).length();
            defaultPath = settingsList.get(2).substring(12, lineSize);
            File defaultFile = new File(defaultPath);
            if (!defaultFile.exists()) {
                boolean success = defaultFile.mkdir();
                if (!success) {
                    System.err.println("ERROR MAKING FOLDER!");
                }
            }

        }

    }

    //Change default path in settings file
    public void rewriteDefaultPath(File newDefaultPath) throws IOException {
        defaultPath = newDefaultPath.getPath();
        Scanner settingsReader = new Scanner(new File("settings.info"));
        ArrayList<String> settingsList = new ArrayList<>();
        while (settingsReader.hasNext()) {
            settingsList.add(settingsReader.nextLine());
        }
        settingsReader.close();

        settingsList.set(2, "DefaultPath=" + defaultPath);

        PrintStream settingsWriter = new PrintStream(new File("settings.info"));
        for (int i = 0; i < settingsList.size(); i++) {
            settingsWriter.println(settingsList.get(i));
        }
        settingsWriter.close();
    }

    //Return true if extension is allowed
    public boolean extensionAllowed(File file) {
        String extension = "";
        int i = file.getPath().lastIndexOf('.');
        if (i > 0) {
            extension = file.getPath().substring(i + 1);
        }

        extension = extension.toLowerCase();
        for (int j = 0; j < allowedExtensions.length; j++) {
            if (extension.equals(allowedExtensions[j])) {
                return true;
            }
        }
        return false;
    }

    public static void copyFile(File from, File to) throws IOException {
        if (!to.exists()) {
            to.createNewFile();
        }

        try (
                FileChannel in = new FileInputStream(from).getChannel();
                FileChannel out = new FileOutputStream(to).getChannel()) {
            out.transferFrom(in, 0, in.size());
        }
    }

    public void settingsButtonPressed() {
        final JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(600, 300);

        JPanel contentPanel = (JPanel) dialog.getContentPane();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        //Panel for the title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

        final JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);

        JPanel defaultPathPanel = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }
        };
        defaultPathPanel.setLayout(new BoxLayout(defaultPathPanel, BoxLayout.X_AXIS));

        final JLabel savePathLabel = new JLabel("Save Path:");
        final JFileChooser fileChooser = new JFileChooser(new File(defaultPath)) {
            {
                setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                this.setMultiSelectionEnabled(false);
            }
        };
        final JTextField selectedFile = new JTextField(defaultPath);

        JButton editSaveButton = new JButton("Browse...");
        editSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                fileChooser.setMultiSelectionEnabled(false);
                fileChooser.showOpenDialog(null);
                if (fileChooser.getSelectedFile() != null) {
                    selectedFile.setText(fileChooser.getSelectedFile().getPath());
                }
                selectedFile.setCaretPosition(0);
            }
        });
        defaultPathPanel.add(savePathLabel);
        defaultPathPanel.add(Box.createRigidArea(new Dimension(20, 2)));
        defaultPathPanel.add(selectedFile);
        defaultPathPanel.add(Box.createRigidArea(new Dimension(10, 2)));
        defaultPathPanel.add(editSaveButton);
        defaultPathPanel.add(Box.createHorizontalGlue());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                File defaultFile = new File(selectedFile.getText());
                if (defaultFile.exists() && defaultFile.isDirectory()) {
                    if (defaultFile.canWrite() && defaultFile.canRead()) {
                        try {
                            rewriteDefaultPath(defaultFile);
                            System.out.println("We rewrote the file...");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Permissions Error");
                    }
                } else {
                    System.out.println("Directory not valid error");
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("Cancel pressed.");
                dialog.dispose();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(30, 30)));
        buttonPanel.add(cancelButton);

        contentPanel.add(titlePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        contentPanel.add(defaultPathPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        contentPanel.add(buttonPanel);

        dialog.setModal(true);
        dialog.setVisible(true);
    }

    public void noteAdded() {
        final JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(300, 200);

        JPanel contentPanel = (JPanel) dialog.getContentPane();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));

        final JLabel titleLabel = new JLabel("Successfully Added!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        textPanel.add(titleLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                noteList.refreshNoteList();
                noteList.sortList(noteList.SORT_DATE_NEWESTFIRST);
                populateTable();
                dialog.dispose();
            }
        });
        buttonPanel.add(okButton);

        contentPanel.add(textPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(40, 40)));
        contentPanel.add(buttonPanel);

        dialog.setModal(true);
        dialog.setVisible(true);
    }

    public void addNoteButtonPressed() {
        //Create the "Add Note" Dialog
        final JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(400, 300);

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

        JPanel contentPanel = (JPanel) dialog.getContentPane();
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
                        if (!extensionAllowed(fileChooser.getSelectedFiles()[i])) {
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
                String[] tags = noteList.formatTags(noteTagsField.getText());
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
                        infoPrinter.close();
                        for (int i = 0; i < fileChooser.getSelectedFiles().length; i++) {
                            File originalFile = fileChooser.getSelectedFiles()[i];

                            String extension = "";
                            int j = originalFile.getPath().lastIndexOf('.');
                            if (j > 0) {
                                extension = originalFile.getPath().substring(j + 1).toLowerCase();
                            }

                            File destinationFile = new File(newFolder + "/" + i + "." + extension);
                            copyFile(originalFile, destinationFile);
                            success = true;
                        }
                    } catch (Exception e) {
                        System.out.println("File not found for some reason...");
                    }

                    System.out.println("Saved!");
                    if (success) {
                        noteAdded();
                        dialog.dispose();
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
                System.out.println("Cancel Pressed.");
                dialog.dispose();
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
        dialog.setModal(true);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                NoteSystem MainWindow = new NoteSystem();
                try {
                    MainWindow.startUpSettingsCheck();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MainWindow.initUI();
            }
        });
    }
}
