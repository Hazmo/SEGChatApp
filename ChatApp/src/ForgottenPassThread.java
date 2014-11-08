package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class ForgottenPassThread extends Thread {
    ServerSocket forgottenServer;
    boolean confirm = false;
    String password;
    String userID;
    String question;
    String answer;

    public ForgottenPassThread(ServerSocket forgottenServer) {
        this.forgottenServer = forgottenServer;
    }

    @Override
    public void run() {

        while (true) {
            try (Socket s = forgottenServer.accept();
                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(s.getInputStream());) {
                Object input = in.readObject();

                while ((input.getClass()).equals(JLabel.class) || confirm != true) {
                    confirm = validateID((String) ((JLabel) input).getText());
                    out.writeObject(confirm);
                    if (confirm) {
                        out.writeObject(question);
                        input = in.readObject();
                        if (input.equals(answer)) {
                            confirm = true;
                            out.writeObject(confirm);
                            out.writeObject(password);
                        }
                        else {
                            while (!input.equals(answer)) {
                                confirm = false;
                                out.writeObject(confirm);
                                input = in.readObject();
                            }
                            confirm = true;
                            out.writeObject(confirm);
                            out.writeObject(password);
                        }
                    }
                    else
                        input = in.readObject();
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

    private boolean validateID(String userID) {

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

        String[][] userData = new String[count][4];

        String csvFile = "users.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            int count2 = 0;
            while ((line = br.readLine()) != null) {
                String[] temp = line.split(csvSplitBy);
                userData[count2][0] = temp[1];
                userData[count2][1] = temp[3];
                userData[count2][2] = temp[4];
                userData[count2][3] = temp[5];
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
        for (int i = 0; i < count; i++) {
            if ((userID.equals(userData[i][0]))) {
                password = userData[i][1];
                question = userData[i][2];
                answer = userData[i][3];
                return true;
            }
        }
        return false;
    }
}
