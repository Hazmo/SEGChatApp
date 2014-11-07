package src;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class LoginThread extends Thread {
    String[][] userData;
    String studentID;
    String password;
    File users;
    ServerSocket loginServer;
    UserClass user;

    public LoginThread(ServerSocket loginServer) {
        this.loginServer = loginServer;
    }

    @Override
    public void run() {
        while (true) {
            try (Socket s = loginServer.accept();
                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(s.getInputStream()));) {

                studentID = in.readLine();
                password = in.readLine();
                boolean loggedIn = loginUser(studentID, password);
                out.writeObject(loggedIn);
                if (loggedIn) {
                    out.writeObject(user);
                }
            }
            catch (Exception e) {
                if (e.getClass().equals(IOException.class)) {
                    System.out.println("Accept failed: 4457");
                    System.exit(-1);
                }
                else if (e.getClass().equals(ClassNotFoundException.class))
                    e.printStackTrace();
            }
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
