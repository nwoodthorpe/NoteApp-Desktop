package NoteSystem;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Nathaniel
 */
public class NoteSystem extends JFrame {

    public NoteList noteList;

    public String[] imagePaths = {"buttons/AddNote.png"};

    public Image[] imageArray = new Image[imagePaths.length];
    public Image[] scaledImageArray = new Image[imagePaths.length];

    public String[] columnNames = {"Title",
        "Description",
        "Date",
        "Num. of Files"
    };
    Object[][] data = {
        {"", "", "", ""}
    };

    public String[] allowedExtensions
            = {"png", "bmp", "jpeg", "jpg",
                "docx", "doc",
                "txt",
                "pdf",
                "zip", "rar"};

    DefaultTableModel dtm;
    JTable selectionTable;
    static String defaultPath = Paths.get("").toAbsolutePath().toString();
    ;
    int sortType = 0;

    public static NoteSystem MainWindow;

    protected void genGUI() {
        noteList = new NoteList(defaultPath);
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

        JMenuItem createNoteButton = new JMenuItem("Create New Note...");
        
        JMenuItem newNoteButton = new JMenuItem("Add Note....");
        
        JMenuItem settingsButton = new JMenuItem("Settings");
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Form_Settings settingsForm = new Form_Settings(MainWindow);
            }
        });
        JMenuItem exitButton = new JMenuItem("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
        
        fileMenu.add(createNoteButton);
        fileMenu.add(newNoteButton);
        fileMenu.add(new JSeparator());
        fileMenu.add(settingsButton);
        fileMenu.add(new JSeparator());
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

        //Add items to sortbox
        sortBox.addItem("Date - Newest First");
        sortBox.addItem("Date - Oldest First");
        sortBox.addItem("File Size - Largest First");
        sortBox.addItem("File Size - Smallest First");
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

        searchField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                noteList.searchList(((JTextField) ae.getSource()).getText());
                populateTable();
            }
        });

        //Add button
        final JButton addButton = new JButton("Add...");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Form_AddNote x = new Form_AddNote(MainWindow);
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (selectionTable.getSelectedRow() != -1) {
                    Form_DeleteNote form = new Form_DeleteNote(MainWindow);
                    if (form.returnValue) {
                        String noteTitle = noteList.list.get(selectionTable.getSelectedRow()).title;
                        File noteFolder = new File(defaultPath + "/" + noteTitle);
                        System.out.println(deleteDir(noteFolder));
                        noteList.refreshNoteList();
                        populateTable();

                    }
                }
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
        topButtonBar.add(Box.createRigidArea(new Dimension(20, 30)));
        topButtonBar.add(deleteButton);
        
        createNoteButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                Form_CreateNote form = new Form_CreateNote(MainWindow);
            }
            
        });
        
        newNoteButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                addButton.doClick();
            }
            
        });

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
        selectionTable.setDefaultRenderer(Object.class, new CustomTableRenderer());
        selectionTable.setRowHeight(40);
        selectionTable.setRowSelectionAllowed(true);
        selectionTable.setShowVerticalLines(false);
        selectionTable.setShowGrid(false);
        selectionTable.setIntercellSpacing(new Dimension(0, 0));
        selectionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionTable.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent ce) {
                //Reszie table widths to maintain proportions
                JTable table = (JTable) ce.getSource();
                int totalWidth = table.getWidth();
                table.getColumnModel().getColumn(0).setPreferredWidth(5 * (totalWidth / 16));
                table.getColumnModel().getColumn(1).setPreferredWidth(5 * (totalWidth / 16));
                table.getColumnModel().getColumn(2).setPreferredWidth(3 * (totalWidth / 16));
                table.getColumnModel().getColumn(3).setPreferredWidth(3 * (totalWidth / 16));
            }

            @Override
            public void componentMoved(ComponentEvent ce) {
            }

            @Override
            public void componentShown(ComponentEvent ce) {
            }

            @Override
            public void componentHidden(ComponentEvent ce) {
            }

        });
        selectionTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    System.out.println(selectionTable.getSelectedRow());
                    Form_ListNoteContents noteContents = new Form_ListNoteContents(MainWindow, selectionTable.getSelectedRow());
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {

            }

            @Override
            public void mouseReleased(MouseEvent me) {

            }

            @Override
            public void mouseEntered(MouseEvent me) {

            }

            @Override
            public void mouseExited(MouseEvent me) {

            }
        });

        JScrollPane tableHolder = new JScrollPane(selectionTable);

        tablePanel.add(tableHolder);

        //Add everything to the contentPanel
        contentPanel.add(titlePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        contentPanel.add(topButtonBar);
        contentPanel.add(spacerPanel);
        contentPanel.add(tablePanel);

        frame.setJMenuBar(menuBar);
        frame.setSize(700, 600);
        frame.setVisible(true);
        try {
            noteList.refreshNoteList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //SortBox Action Listener
        sortBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JComboBox x = (JComboBox) ae.getSource();
                sortType = x.getSelectedIndex();
                noteList.sortList(x.getSelectedIndex());
                populateTable();
            }
        });

        noteList.sortList(noteList.SORT_DATE_NEWESTFIRST);

        populateTable();
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        
        return dir.delete();
    }

    public void loadImages() {
        for (int i = 0; i < imagePaths.length; i++) {
            try {
                imageArray[i] = ImageIO.read(new File("images/" + imagePaths[i]));
            } catch (IOException e) {
                System.out.println("IMAGE ERROR");
            }
        }
    }

    //Reads noteList and populates table with data
    public void populateTable() {
        ArrayList<Note> list = noteList.list;
        ((DefaultTableModel) selectionTable.getModel()).setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            String[] row = new String[4];
            Note currentNote = list.get(i);
            //Date, Title,Description, Format
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, yyyy");
            row[0] = currentNote.title;
            row[1] = currentNote.description;
            row[2] = sdf.format(currentNote.date.getTime());
            row[3] = Integer.toString(currentNote.numOfFiles);
            ((DefaultTableModel) selectionTable.getModel()).addRow(new java.util.Vector<String>(java.util.Arrays.asList(row)));

        }
    }

    //Checks for settings file
    public void startUpSettingsCheck() throws Exception {
        File settingsFile = new File("settings.info");

        loadImages();

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

    //Copies a file, used when to copy note files into the program save folder
    public static void copyFile(File from, File to) throws IOException {
        if (!to.exists()) {
            to.createNewFile();
        }
        System.out.println(to.getPath());

        try (
                FileChannel in = new FileInputStream(from).getChannel();
                FileChannel out = new FileOutputStream(to).getChannel()) {
            out.transferFrom(in, 0, in.size());
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow = new NoteSystem();
                try {
                    MainWindow.startUpSettingsCheck();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MainWindow.genGUI();
            }
        });
    }
}
