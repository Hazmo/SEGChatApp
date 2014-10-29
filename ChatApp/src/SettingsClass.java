package src;

/**
 *  @author Codrin Gidei - 1326651
 *  @email codrin.gidei@kcl.ac.uk
 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The class used as a frame for the settings that the user can access.
 */
public class SettingsClass extends JFrame implements ActionListener {

    /**
     * Instantiates a new settings class and initiates all the GUI widgets / components.
     */
    JTextField nameChangeField;
    JTextField passChangeField;
    JTextField confirmPassField;
    JTextField questionField;
    JTextField answerField;
    UserClass user;

    public SettingsClass(final UserClass user) {
        this.user = user;
        this.setLayout(new BorderLayout(15, 0));

        final JLabel nameChangeLabel = new JLabel("Change name: ");
        final JLabel passChangeLabel = new JLabel("Change password: ");
        final JLabel confirmPassLabel = new JLabel("Confirm new password: ");
        final JLabel questionLabel = new JLabel("Change secret question: ");
        final JLabel answerLabel = new JLabel("Secret answer: ");

        this.nameChangeField = new JTextField(20);
        this.passChangeField = new JTextField(20);
        this.confirmPassField = new JTextField(20);
        this.questionField = new JTextField(20);
        this.answerField = new JTextField(20);

        final JPanel center = new JPanel(new GridLayout(5, 2));
        center.add(nameChangeLabel);
        center.add(this.nameChangeField);
        center.add(passChangeLabel);
        center.add(this.passChangeField);
        center.add(confirmPassLabel);
        center.add(this.confirmPassField);
        center.add(questionLabel);
        center.add(this.questionField);
        center.add(answerLabel);
        center.add(this.answerField);

        final JPanel east = new JPanel(new GridLayout(5, 1));

        final JButton okButton = new JButton("Ok");
        okButton.setActionCommand("Ok");
        okButton.addActionListener(this);
        final JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);

        final JPanel south = new JPanel(new FlowLayout());
        south.add(okButton);
        south.add(cancelButton);

        this.add(center);
        this.add(south, BorderLayout.SOUTH);

        this.setVisible(true);
        this.pack();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if ((e.getActionCommand()).equals("Cancel")) {
            this.dispose();
        }
        else {
            if (e.getActionCommand().equals("Ok")) {
                final File csvFile = new File("users.csv");
                final File tempFile = new File("temp.csv");

                String line = "";
                final String splitBy = ",";
                BufferedReader br = null;
                PrintWriter pw = null;

                try {
                    br = new BufferedReader(new FileReader(csvFile));
                    pw = new PrintWriter(new FileWriter(tempFile));

                    while ((line = br.readLine()) != null) {
                        final String[] temp = line.split(splitBy);
                        if (temp[1].equals(this.user.getID())) {
                            continue;
                        }
                        pw.println(line);
                    }

                    pw.print("0");
                    pw.print(",");
                    pw.print(this.user.getID());
                    pw.print(",");

                    if (!this.nameChangeField.getText().equals("")) {
                        pw.print(this.nameChangeField.getText());
                        this.user.setName(this.nameChangeField.getText());
                    }
                    else {
                        pw.print(this.user.getName());
                    }

                    pw.print(",");
                    if (!(this.passChangeField.getText().equals(""))) {
                        if (this.passChangeField.getText().equals(this.confirmPassField.getText())) {
                            pw.print(this.passChangeField.getText());
                            this.user.setPassword(this.passChangeField.getText());
                        }
                        else {
                            JOptionPane.showMessageDialog(this, "Password fields don't match!");
                            this.passChangeField.setText("");
                            this.confirmPassField.setText("");
                        }
                    }
                    else {
                        pw.print(this.user.getPassword());
                    }

                    pw.print(",");
                    if (!this.questionField.getText().equals("")) {
                        pw.print(this.questionField.getText());
                        this.user.setQuestion(this.questionField.getText());
                    }
                    else {
                        pw.print(this.user.getQuestion());
                    }

                    pw.print(",");
                    if (!this.answerField.getText().equals("")) {
                        pw.print(this.answerField.getText());
                        this.user.setAnswer(this.answerField.getText());
                    }
                    else {
                        pw.print(this.user.getAnswer());
                    }
                    pw.println();

                }
                catch (final Exception ex) {
                    ex.printStackTrace();
                }
                finally {
                    try {
                        br.close();
                    }
                    catch (final IOException e1) {
                        e1.printStackTrace();
                    }
                    pw.close();
                }
                csvFile.delete();
                tempFile.renameTo(new File("users.csv"));

            }
            this.dispose();
        }
    }
}
