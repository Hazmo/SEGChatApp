package src;

/**
 * @author Ainur Makhmet - 1320744
 * @email ainur.makhmet@kcl.ac.uk
 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Class extends JFrame and implements ActionListener
 */
class RegisterGUI extends JFrame implements ActionListener {

    private JPasswordField passwordField = new JPasswordField(15);
    private JPasswordField confirmPasswordField = new JPasswordField(15);
    private JTextField[] jFields = new JTextField[6];
    private JTextField studentField = new JTextField(15);
    private String[][] userData;
    Socket socket;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;

    /**
     * The constructor is called whenever the student wants to register.
     */
    RegisterGUI() {

        try {
            socket = new Socket("localhost", 4459);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("REGISTRATION FORM");

        JPanel center = new JPanel(new GridLayout(6, 1, 10, 10));
        JPanel south = new JPanel(new FlowLayout());
        JPanel west = new JPanel(new GridLayout(6, 1, 10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));

        getContentPane().setLayout(new BorderLayout(20, 10));
        getContentPane().add(center);
        getContentPane().add(south, BorderLayout.SOUTH);
        getContentPane().add(west, BorderLayout.WEST);

        JButton submitButton = new JButton("SUBMIT");
        JButton cancelButton = new JButton("CANCEL");
        south.add(submitButton);
        south.add(cancelButton);

        SwingUtilities.getRootPane(submitButton).setDefaultButton(submitButton);

        JLabel nameLabel = new JLabel("Name:");
        JLabel studentIdLabel = new JLabel("Student ID:");
        studentIdLabel.setToolTipText("");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmPasswordLabel = new JLabel("Confirm password:");
        JLabel securityQuestionLabel = new JLabel("Security question:");
        securityQuestionLabel
                .setToolTipText("This question will be asked when you forget you password");
        JLabel answerLabel = new JLabel("Answer:");
        west.add(nameLabel);
        west.add(studentIdLabel);
        west.add(passwordLabel);
        west.add(confirmPasswordLabel);
        west.add(securityQuestionLabel);
        west.add(answerLabel);

        JTextField nameField = new JTextField(15);
        JTextField questionField = new JTextField(15);
        JTextField answerField = new JTextField(15);
        jFields = new JTextField[] { nameField, studentField, passwordField, confirmPasswordField,
                questionField, answerField };

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

    /**
     * handles events which are caused by the the buttons "SUBMIT" and "CANCEL" being clicked.
     * @param e
     *        identifies which button is clicked
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("submitDetails")) {
            if (check()) {

                int confirmation = 0;
                try {
                    out.writeObject(jFields);
                    confirmation = (int) in.readObject();
                }

                catch (Exception e1) {
                    e1.printStackTrace();
                    confirmation = 3;
                }

                switch (confirmation) {
                case (1): {
                    JOptionPane
                            .showMessageDialog(this,
                                    "Congratulations with successful registration! To log in use your password and student ID.");
                    this.dispose();
                    break;
                }

                case (2): {
                    JOptionPane.showMessageDialog(this,
                            "Registration failed! Problem with the user database!");
                    this.dispose();
                    break;
                }

                case (3): {
                    JOptionPane.showMessageDialog(this,
                            "Registration failed, problem with connection to server!");
                    this.dispose();
                    break;
                }
                case (4): {
                    JOptionPane.showMessageDialog(this,
                            "Registration failed, this ID has already been registered!");

                    this.dispose();

                    break;
                }
                }

                try {
                    socket.close();
                    in.close();
                    out.close();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        else if (e.getActionCommand().equals("cancelRegistration")) {
            try {
                socket.close();
                in.close();
                out.close();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * this method checks whether the form is filled correctly.
     * @return true if the form is filled correctly, otherwise notify the user about the problem and
     *         return false.
     */
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
            JOptionPane.showMessageDialog(this, "Your passwords do not match!");
            return false;
        }
        return true;
    }

}
