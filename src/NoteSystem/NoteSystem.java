package NoteSystem;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Nathaniel
 */
public class NoteSystem extends JFrame {

    public String[] columnNames = {"Date",
        "Title",
        "Description",
        "Format"};

    Object[][] data = {
        {"", "",
            "", ""}
    };

    protected void initUI() {
        JFrame frame = new JFrame();
        JPanel contentPanel = (JPanel) frame.getContentPane();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //topButtonBar
        JPanel topButtonBar = new JPanel();
        topButtonBar.setLayout(new BoxLayout(topButtonBar, BoxLayout.X_AXIS));

        //Menubar 
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitButton = new JMenuItem("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
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
                System.out.println(x.getSelectedItem().toString());
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
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                buttonPressed();
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
        DefaultTableModel dtm = new DefaultTableModel(data, columnNames);
        JTable selectionTable = new JTable(dtm) {
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
        contentPanel.add(topButtonBar);
        contentPanel.add(spacerPanel);
        contentPanel.add(tablePanel);

        frame.setJMenuBar(menuBar);
        frame.setSize(600, 600);
        frame.setVisible(true);
    }

    public void buttonPressed() {
        //Create the "Add Note" Dialog
        JDialog dialog = new JDialog();
        dialog.setSize(400, 500);

        JPanel contentPanel = (JPanel) dialog.getContentPane();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        //Panel for the title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

        JLabel titleLabel = new JLabel("Add a New Note");
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

        final JLabel selectedFile = new JLabel("");
        
        JButton selectButton = new JButton("Select a File:");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String filename = File.separator + "tmp";
                JFileChooser fileChooser = new JFileChooser(new File(filename));
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.showOpenDialog(null);
                if(fileChooser.getSelectedFiles().length > 1){
                    selectedFile.setText("<html>" + fileChooser.getSelectedFile().getName() + "<br>" + 
                     "and " + (fileChooser.getSelectedFiles().length - 1) + " more.</html>");
                }else{
                    selectedFile.setText(fileChooser.getSelectedFile().getName());
                }
            }
        });
        selectFilePanel.add(selectButton);
        selectFilePanel.add(Box.createRigidArea(new Dimension(20, 2)));
        selectFilePanel.add(selectedFile);
        selectFilePanel.add(Box.createHorizontalGlue());
        
        //Panel for the title of the note
        JPanel noteTitlePanel = new JPanel(){
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }
        };
        noteTitlePanel.setLayout(new BoxLayout(noteTitlePanel, BoxLayout.X_AXIS));
        
        JLabel noteTitleLabel = new JLabel("Title:  ");
        noteTitlePanel.add(noteTitleLabel);
        
        noteTitlePanel.add(Box.createRigidArea(new Dimension(90, 2)));
        
        JTextField noteTitleField = new JTextField("");
        noteTitlePanel.add(noteTitleField);
        
        //Panel for the description of the note
        JPanel noteDescriptionPanel = new JPanel(){
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }
        };
        noteDescriptionPanel.setLayout(new BoxLayout(noteDescriptionPanel, BoxLayout.X_AXIS));
        
        JLabel noteDescriptionLabel = new JLabel("Description:  ");
        noteDescriptionPanel.add(noteDescriptionLabel);
        
        noteDescriptionPanel.add(Box.createRigidArea(new Dimension(50, 2)));
        
        JTextField noteDescriptionField = new JTextField("");
        noteDescriptionPanel.add(noteDescriptionField);
        
        //Panel for the tags of the note
        JPanel noteTagsPanel = new JPanel(){
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }
        };
        noteTagsPanel.setLayout(new BoxLayout(noteTagsPanel, BoxLayout.X_AXIS));
        
        JLabel noteTagsLabel = new JLabel("Tags:  ");
        noteTagsPanel.add(noteTagsLabel);
        
        noteTagsPanel.add(Box.createRigidArea(new Dimension(87, 2)));
        
        JTextField noteTagsField = new JTextField("");
        noteTagsPanel.add(noteTagsField);
        
        //Panel for the text below the tags label
        JPanel tagsPanel2 = new JPanel();
        tagsPanel2.setLayout(new BoxLayout(tagsPanel2, BoxLayout.X_AXIS));

        JLabel tagsLabel2 = new JLabel("(Seperate with Spaces)");
        tagsPanel2.add(tagsLabel2);
        tagsPanel2.add(Box.createHorizontalGlue());
        
        //Add everything to our contentPanel (Master Panel)
        contentPanel.add(titlePanel);
        contentPanel.add(spacerPanel);
        contentPanel.add(selectFilePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        contentPanel.add(noteTitlePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        contentPanel.add(noteDescriptionPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(15, 15)));
        contentPanel.add(noteTagsPanel);
        contentPanel.add(tagsPanel2);
        dialog.setModal(true);
        dialog.setVisible(true);
        System.out.println("dialog closed. tf=");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                NoteSystem MainWindow = new NoteSystem();
                MainWindow.initUI();

            }
        });
    }
}
