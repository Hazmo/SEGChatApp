package src;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Harry on 05/11/2014.
 */
public class RegisterLoginThread extends Thread{

    Socket s;

    String[][] userData;
    String studentID;
    String password;
    File users;
    UserClass user;

    public RegisterLoginThread(Socket s) {
        this.s = s;
    }

    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());) {

            while (true) {

                MessageClass message = (MessageClass) in.readObject();

                if (message.getMessageType().equals("log_in")) {

                    studentID = message.getMessage();
                    password = (String) message.getExtraData()[0];
                    System.out.println("studentID = " + studentID);
                    System.out.println("password = " + password);

                    boolean loggedIn = loginUser(studentID, password);
                    System.out.println("Boolean.toString(loggedIn) = " + Boolean.toString(loggedIn));
                    out.writeObject(new MessageClass("logged_in", Boolean.toString(loggedIn)));
                    if (loggedIn) {
                        out.writeObject(user);
                    }


                } else if (message.getMessageType().equals("register")) {

                }
            }
        } catch (IOException e) {
            e.getMessage();
        } catch (ClassNotFoundException e) {
            e.getMessage();
        }
    }


    public boolean loginUser(String studentID, String password) {
        getUserData();

        for (String[] row : userData) {
            if (row[1].equals(studentID) && row[3].equals(password)) {
                String[] userTmp = getUser();
                user = new UserClass(userTmp);
                return true;
            }
        }

        return false;
    }

    public String[] getUser() {
        for (String[] row : userData) {
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


    public void getUserData() {
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

        userData = new String[count][6];

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
                userData[count2][2] = temp[2];
                userData[count2][3] = temp[3];
                userData[count2][4] = temp[4];
                userData[count2][5] = temp[5];
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
