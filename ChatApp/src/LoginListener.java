package src;

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

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginListener implements ActionListener {

    private final JTextField studentIDField;
    private final JPasswordField passField;
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

    /**
     * Parametric constructor providing the text field and the password field from the login
     */
    public LoginListener(final JTextField studentIDField, final JPasswordField passField) {
        this.studentIDField = studentIDField;
        this.passField = passField;
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final File users = new File("users.csv");
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
        catch (final FileNotFoundException e1) {
            e1.printStackTrace();
        }

        final String[][] userData = new String[count][6];

        final String csvFile = "users.csv";
        BufferedReader br = null;
        String line = "";
        final String csvSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            int count2 = 0;
            while ((line = br.readLine()) != null) {
                final String[] temp = line.split(csvSplitBy);
                userData[count2][0] = temp[0];
                userData[count2][1] = temp[1];
                userData[count2][2] = temp[2];
                userData[count2][3] = temp[3];
                userData[count2][4] = temp[4];
                userData[count2][5] = temp[5];
                count2++;
            }
        }
        catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        final String user = this.studentIDField.getText();
        final String passString = new String(this.passField.getPassword());

        final String[] userLoginData = new String[6];

        boolean login_true = false;
        for (int i = 0; i < count; i++) {
            if ((user.equals(userData[i][1])) && (passString.equals(userData[i][3]))) {
                login_true = true;

                for (int j = 0; j < 6; j++) {
                    userLoginData[j] = userData[i][j];
                }
                break;
            }
        }
        if (login_true == true) {
            System.out.println("Correct login");
//            try {
//                socket = new Socket("localhost", 446);
//                out = new ObjectOutputStream(socket.getOutputStream());
//                in = new ObjectInputStream(socket.getInputStream());
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
            //new StudentGUI(new UserClass(userLoginData));
        }
        else {
            JOptionPane.showMessageDialog(null, "No such login");
        }
    }
}
