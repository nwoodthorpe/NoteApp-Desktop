package marksystem;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Nathaniel
 */
public class MarkSystem {

    protected void initUI() {
        JFrame frame = new JFrame();
        JPanel controlPanel = (JPanel) frame.getContentPane();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel topButtonBar = new JPanel();
        topButtonBar.setLayout(new BoxLayout(topButtonBar, BoxLayout.X_AXIS));
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        
        topButtonBar.add(Box.createHorizontalBox());
        
        JLabel sortLabel = new JLabel("Sort by:");
        topButtonBar.add(sortLabel);
        
        JComboBox sortBox = new JComboBox() {
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }

        };
        topButtonBar.add(sortBox);
        topButtonBar.add(Box.createHorizontalGlue());
        
        JButton addButton = new JButton("Add");
        topButtonBar.add(addButton);
        
        controlPanel.add(topButtonBar);
        frame.setJMenuBar(menuBar);
        frame.setSize(600, 600);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MarkSystem testMultiplePanels = new MarkSystem();
                testMultiplePanels.initUI();

            }
        });
    }

}
