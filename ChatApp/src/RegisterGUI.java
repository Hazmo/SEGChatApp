/**
 * @author Ainur Makhmet - 1320744
 * @email ainur.makhmet@kcl.ac.uk
 */
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Class extends JFrame and implements ActionListener
 */
class RegisterGUI extends JFrame implements ActionListener{

    private JPasswordField passwordField = new JPasswordField(15);
    private JPasswordField confirmPasswordField = new JPasswordField(15);
    private JTextField[] jFields = new JTextField[6];
    private String[][] userData;

    /**
     *The constructor is called whenever the student wants to register.
     */
    RegisterGUI() {
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

    /**
     * handles events which are caused by the the buttons "SUBMIT" and "CANCEL" being clicked.
     * @param e identifies which button is clicked
     */
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

                toFileWriter.flush();

                toFileWriter.close();
                
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

    /**
     * this method checks whether the form is filled correctly.
     * @return true if the form is filled correctly, otherwise notify the user about the problem and return false.
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
            JOptionPane.showMessageDialog(this, "You passwords do not match!");
            return false;
        }
        
        // Go through each row in userData 2d array, check if student ID already exists
        // within the array, if so, user cannot be created
        for(String[] row : userData) {
        	if(row[0].equals(studentField.getText())) {
        		JOptionPane.showMessageDialog(this, "Student ID already exists. Please try forgotten password option.");
        		return false;
        	}
        }
        
        return true;
    }
    
    /**
     * Method to get the data from the users csv file
     */
    public void getUserData() {
    	File users = new File("users.csv");
        int count = 0;
        Scanner sc;
        try {
            sc = new Scanner(users);
            while(sc.hasNextLine()) {
                count++;
                sc.nextLine();
            }

            sc.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        userData = new String[count][4];
        
        String csvFile = "users.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            int count2 = 0;
            while((line = br.readLine()) != null) {
                String[] temp = line.split(csvSplitBy);
                userData[count2][0] = temp[1];
                userData[count2][1] = temp[3];
                userData[count2][2] = temp[4];
                userData[count2][3] = temp[5];
                count2++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
