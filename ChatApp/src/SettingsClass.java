/**
 *  @author Codrin Gidei - 1326651
 *  @email codrin.gidei@kcl.ac.uk
 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The class used as a frame for the settings that the user can access.
 */
public class SettingsClass extends JFrame {

    /**
     * Instantiates a new settings class and initiates all the GUI widgets / components.
     */
    public SettingsClass() {
        this.setLayout(new BorderLayout(15, 0));

        JLabel nameChangeLabel = new JLabel("Change name: ");
        JLabel passChangeLabel = new JLabel("Change password: ");
        JLabel confirmPassLabel = new JLabel("Confirm new password: ");
        JLabel questionLabel = new JLabel("Change secret question: ");
        JLabel answerLabel = new JLabel("Secret answer: ");

        JTextField nameChangeField = new JTextField(20);
        JTextField passChangeField = new JTextField(20);
        JTextField confirmPassField = new JTextField(20);
        JTextField questionField = new JTextField(20);
        JTextField answerField = new JTextField(20);

        JPanel center = new JPanel(new GridLayout(5, 5));
        center.add(nameChangeLabel);
        center.add(nameChangeField);
        center.add(passChangeLabel);
        center.add(passChangeField);
        center.add(confirmPassLabel);
        center.add(confirmPassField);
        center.add(questionLabel);
        center.add(questionField);
        center.add(answerLabel);
        center.add(answerField);

        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");

        JPanel south = new JPanel(new FlowLayout());
        south.add(okButton);
        south.add(cancelButton);

        this.add(center);
        this.add(south, BorderLayout.SOUTH);

        setVisible(true);
        pack();
    }
}
