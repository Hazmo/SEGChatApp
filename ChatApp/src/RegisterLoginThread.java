
package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JTextField;

/**
 * Created by Harry on 05/11/2014.
 */
public class RegisterLoginThread extends Thread {
    Socket s;

    String[][] userDataLogIn;
    String studentID;
    String password;
    File users;
    UserClass user;

    JTextField[] jFields = new JTextField[6];
    String[][] userDataRegister;
    static boolean ok = false;

    public RegisterLoginThread(Socket s) {
        this.s = s;
    }

    public void run() {

        try (ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());) {

            while (true) {

                MessageClass message = (MessageClass) in.readObject();

                if (message.getMessageType().equals("log_in")) {

                    studentID = message.getMessage();
                    password = (String) message.getExtraData()[0];
                    // System.out.println("studentID = " + studentID);
                    // System.out.println("password = " + password);

                    boolean loggedIn = loginUser(studentID, password);
                    // System.out
                    // .println("Boolean.toString(loggedIn) = " + Boolean.toString(loggedIn));
                    out.writeObject(new MessageClass("logged_in", Boolean.toString(loggedIn)));
                    if (loggedIn) {
                        out.writeObject(user);
                    }



                } else if (message.getMessageType().equals("register")) {
                    jFields = (JTextField[]) message.getExtraData();
                    try {
                        getUserDataRegister();
                        ok = true;
                    }
                    catch (Exception e) {
                        ok = false;
                    }
                    if (ok == true) {
                        boolean duplicate = false;
                        for (int i = 0; i < userDataRegister.length; i++) {
                            if (userDataRegister[i][0].equals(jFields[1].getText())) {
                                duplicate = true;
                                break;
                            }
                        }
                        if (duplicate == true) {
                            out.writeObject(4);
                        }
                        else {
                            int registered = registerUser();
                            out.writeObject(registered);
                        }
                    }
                    else {
                        int registered = registerUser();
                        out.writeObject(registered);
                        ok = true;
                    }
                }
            }
        } catch (IOException e) {
            e.getMessage();
        } catch (ClassNotFoundException e) {

            e.getMessage();
        }
    }

    public boolean loginUser(String studentID, String password) {
        getUserDataLogIn();

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

    public void getUserDataLogIn() {
        users = new File("users.csv");

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
    }

    public int registerUser() {
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

    /**
     * Method to get the data from the users csv file
     */
    public void getUserDataRegister() {
        users = new File("users.csv");

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
    }
}
