package src;

import javax.swing.*;
import java.io.*;
import java.util.Scanner;

/**
 * Created by Harry on 06/11/2014.
 */
public class UserData {

    String[][] userDataLogIn;
    String studentID;
    String password;
    File users;
    UserClass user;


    JTextField[] jFields = new JTextField[6];
    String[][] userDataRegister;
    static boolean ok = false;


    File userDataFile;

    public UserData(String file) {
        userDataFile = new File(file);
    }



    public boolean loginUser(String studentID, String password) {
        getUserDataLogIn();
        this.studentID = studentID;
        this.password = password;
        for (String[] row : userDataLogIn) {
            if (row[1].equals(studentID) && row[3].equals(password)) {
                String[] userTmp = getUser();
                user = new UserClass(userTmp);
                return true;
            }
        }

        return false;
    }

    public String[] getUser() {
        for (String[] row : userDataLogIn) {
            if (row[1].equals(studentID)) {
                String[] userInf = new String[6];
                userInf[0] = row[0];
                userInf[1] = row[1];
                userInf[2] = row[2];
                userInf[3] = row[3];
                userInf[4] = row[4];
                userInf[5] = row[5];

                return userInf;
            }
        }
        return new String[6];
    }


    public String[][] getUserDataLogIn() {
        users = userDataFile;

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

        userDataLogIn = new String[count][6];

        String csvFile = "users.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            int count2 = 0;
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(csvSplitBy);
                userDataLogIn[count2][0] = temp[0];
                userDataLogIn[count2][1] = temp[1];
                userDataLogIn[count2][2] = temp[2];
                userDataLogIn[count2][3] = temp[3];
                userDataLogIn[count2][4] = temp[4];
                userDataLogIn[count2][5] = temp[5];
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

        return userDataLogIn;
    }

    public int registerUser(JTextField jFields[]) {
        FileWriter registrationFileWriter = null;
        try {
            registrationFileWriter = new FileWriter("users.csv", true);
        }
        catch (IOException e1) {
            e1.printStackTrace();
            return 2;
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
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        return 1;
    }


    public String[][] getUserDataRegister() {
        users = userDataFile;

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

        userDataRegister = new String[count][4];

        String csvFile = "users.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            int count2 = 0;
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(csvSplitBy);
                userDataRegister[count2][0] = temp[1];
                userDataRegister[count2][1] = temp[3];
                userDataRegister[count2][2] = temp[4];
                userDataRegister[count2][3] = temp[5];
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
        return userDataRegister;
    }

    public UserClass getUserClass() {
        return user;
    }

    public int updateInfo(UserClass settingsUser, JTextField[] settingsFields) {

        int confirmation = 0;
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
                if (temp[1].equals(settingsUser.getID())) {
                    continue;
                }
                pw.println(line);
            }

            pw.print("0");
            pw.print(",");
            pw.print(settingsUser.getID());
            pw.print(",");

            if (!settingsFields[0].getText().equals("")) {
                pw.print(settingsFields[0].getText());
                settingsUser.setName(settingsFields[0].getText());
                confirmation = 1;
            }
            else {
                pw.print(settingsUser.getName());
            }

            pw.print(",");
            if (!(settingsFields[1].getText().equals(""))) {
                if (settingsFields[1].getText().equals(settingsFields[2].getText())) {
                    pw.print(settingsFields[1].getText());
                    settingsUser.setPassword(settingsFields[1].getText());
                    confirmation = 1;
                }
                else {
                    JOptionPane.showMessageDialog(new JFrame(), "Password fields don't match!");
                    settingsFields[1].setText("");
                    settingsFields[2].setText("");
                }
            }
            else {
                pw.print(settingsUser.getPassword());
            }

            pw.print(",");
            if (!settingsFields[3].getText().equals("")) {
                pw.print(settingsFields[3].getText());
                settingsUser.setQuestion(settingsFields[3].getText());
                confirmation = 1;
            }
            else {
                pw.print(settingsUser.getQuestion());
            }

            pw.print(",");
            if (!settingsFields[4].getText().equals("")) {
                pw.print(settingsFields[4].getText());
                settingsUser.setAnswer(settingsFields[4].getText());
                confirmation = 1;
            }
            else {
                pw.print(settingsUser.getAnswer());
            }
            pw.println();

        }
        catch (final Exception ex) {
            ex.printStackTrace();
            confirmation = 2;
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
        boolean success = tempFile.renameTo(new File("users.csv"));
        System.out.println(success);
        System.out.println("tempFile = " + tempFile.getName());

        System.out.println("userDataFile = " + userDataFile.getAbsoluteFile());
        return confirmation;
    }
}
