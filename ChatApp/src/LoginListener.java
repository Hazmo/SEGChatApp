import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginListener implements ActionListener {

    private JTextField studentIDField;
    private JPasswordField passField;

    public LoginListener(JTextField studentIDField, JPasswordField passField) {
        this.studentIDField = studentIDField;
        this.passField = passField;
    }

    public void actionPerformed(ActionEvent event) {
        File users = new File("users.csv");
        int count = 0;
        Scanner sc;
        try {
            sc = new Scanner(users);
            while (sc.hasNextLine()) {
                count++;
                sc.nextLine();
            }

            sc.close();
        }
        catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        String[][] userData = new String[count][3];

        String csvFile = "users.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            int count2 = 0;
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(csvSplitBy);
                userData[count2][0] = temp[0];
                userData[count2][1] = temp[1];
                userData[count2][2] = temp[3];
                count2++;
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String user = studentIDField.getText();
        String pass = String.valueOf(passField.getPassword());

        MessageDigest md;
        String encrypted_pass;

        // try { md = MessageDigest.getInstance("MD5"); md.update(pass.getBytes(), 0,
        // pass.length()); encrypted_pass = new BigInteger(1, md.digest()).toString(16);

        String passString = new String(passField.getPassword());
        boolean login_true = false;
        for (int i = 0; i < count; i++) {
            if ((user.equals(userData[i][1])) && (passString.equals(userData[i][2]))) {// (encrypted_pass.equals(userData[i][2])))
                                                                                       // {
                login_true = true;
            }
        }

        if (login_true == true) {
            System.out.println("Correct login");
            new StudentGUI();

            JButton loginButton = (JButton) event.getSource();
            Window loginFrame = (SwingUtilities.getWindowAncestor(loginButton));
            loginFrame.dispose();
        }
        else {
            JOptionPane.showMessageDialog(null, "No such login");

        }

        // } catch (NoSuchAlgorithmException e) {
        // e.printStackTrace();
        // }

    }
}
