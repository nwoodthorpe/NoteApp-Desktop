package NoteSystem;

import static NoteSystem.NoteSystem.defaultPath;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
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

public class Form_Settings extends JDialog{

    NoteSystem mainForm;

    public Form_Settings(NoteSystem mainForm) {
        super();
        this.mainForm = mainForm;
        genGUI();
    }

    public void genGUI() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(600, 300);

        JPanel contentPanel = (JPanel) getContentPane();
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
                            mainForm.rewriteDefaultPath(defaultFile);
                            dispose();
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
                dispose();
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

        setModal(true);
        setVisible(true);
    }
}
