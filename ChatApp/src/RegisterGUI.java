
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class RegisterGUI extends JFrame implements ActionListener{

    private JPasswordField passwordField = new JPasswordField(15);
    private JPasswordField confirmPasswordField = new JPasswordField(15);
    private JTextField[] jFields = new JTextField[6];

    public RegisterGUI() {
        setTitle("REGISTRATION FORM");

        JPanel center = new JPanel(new GridLayout(6, 1, 10, 10));
        JPanel south  = new JPanel(new FlowLayout());
        JPanel west = new JPanel(new GridLayout(6, 1, 10, 10));
        ((JComponent)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));

        getContentPane().setLayout(new BorderLayout(20, 10));
        getContentPane().add(center);
        getContentPane().add(south, BorderLayout.SOUTH);
        getContentPane().add(west, BorderLayout.WEST);

        JButton submitButton = new JButton("SUBMIT");
        JButton cancelButton = new JButton("CANCEL");
        south.add(submitButton);
        south.add(cancelButton);

        JLabel nameLabel = new JLabel("Name:");
        JLabel studentIdLabel  = new JLabel("Student ID:");
        studentIdLabel.setToolTipText("");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmPasswordLabel = new JLabel("Confirm password:");
        JLabel securityQuestionLabel = new JLabel("Security question:");
        securityQuestionLabel.setToolTipText("This question will be asked when you forget you password");
        JLabel answerLabel = new JLabel("Answer:");
        west.add(nameLabel);
        west.add(studentIdLabel);
        west.add(passwordLabel);
        west.add(confirmPasswordLabel);
        west.add(securityQuestionLabel);
        west.add(answerLabel);

        JTextField nameField = new JTextField(15);
        JTextField studentField = new JTextField(15);
        JTextField questionField = new JTextField(15);
        JTextField answerField = new JTextField(15);
        jFields = new JTextField[]{nameField, studentField, passwordField, confirmPasswordField, questionField, answerField};

        center.add(nameField);
        center.add(studentField);
        center.add(passwordField);
        center.add(confirmPasswordField);
        center.add(questionField);
        center.add(answerField);

        submitButton.setActionCommand("submitDetails");
        cancelButton.setActionCommand("cancelRegistration");
        submitButton.addActionListener(this);
        cancelButton.addActionListener(this);


        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("submitDetails")) {
            if (check()) {


                FileWriter registrationFileWriter = null;
                try {
                    registrationFileWriter = new FileWriter("users.csv", true);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                PrintWriter toFileWriter = new PrintWriter(registrationFileWriter);
                // ',' divides the word into columns
                toFileWriter.print("0");
                toFileWriter.print(",");
                toFileWriter.print(jFields[1].getText().toLowerCase());
                toFileWriter.print(",");
                toFileWriter.print(jFields[0].getText());
                toFileWriter.print(",");
                toFileWriter.print(jFields[2].getText());
                toFileWriter.print(",");
                toFileWriter.print(jFields[4].getText());
                toFileWriter.print(",");
                toFileWriter.print(jFields[5].getText().toLowerCase());
                toFileWriter.println();
                //Flush the output to the file
                toFileWriter.flush();

                //Close the Print Writer
                toFileWriter.close();
                
                //Close the File Writer
                try {
                    registrationFileWriter.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                JOptionPane.showMessageDialog(this, "Congratulations with successful registration! To log in use your password and student ID.");
                this.dispose();
            }
        } else if (e.getActionCommand().equals("cancelRegistration")) {
            this.dispose();
        }
    }
    private boolean check() {
        for (int i = 0; i < 6; i++) {
            if (jFields[i].getText().length() == 0) {
                JOptionPane.showMessageDialog(this, "Please, fill all the required fields!");

                return false;
            }
        }
        char[] passwordCharArray = passwordField.getPassword();
        char[] confirmPasswordCharArray = confirmPasswordField.getPassword();
        String passwordString = new String(passwordCharArray);
        String confirmPasswordString = new String(confirmPasswordCharArray);
        if (!passwordString.equals(confirmPasswordString)) {
            JOptionPane.showMessageDialog(this, "You passwords do not match!");
            return false;
        }
        return true;
    }
}
