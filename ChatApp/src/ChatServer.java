package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JTextField;

public class ChatServer {

    ServerSocket server;
    Socket client;
    static ArrayList<ChatClientThread> clientWorkers = new ArrayList<ChatClientThread>();
    JTextField[] jFields = new JTextField[6];
    String[][] userData;
    File users;

    public ChatServer() {
        // getUserData();
    }

    public void listenSocket() {
        try {
            server = new ServerSocket(4455);
        }
        catch (IOException e) {
            System.out.println("Could not listen to port");
            System.exit(-1);
        }

        while (true) {
            ChatClientThread w;
            try {

                ChatClientThread ccl = new ChatClientThread(server.accept());
                clientWorkers.add(ccl);
                ccl.start();
            }
            catch (IOException e) {
                System.out.println("Accept failed: 4444");
                System.exit(-1);
            }
        }
    }

    public boolean registerUser() {
        FileWriter registrationFileWriter = null;
        try {
            registrationFileWriter = new FileWriter("users.csv", true);
        }
        catch (IOException e1) {
            e1.printStackTrace();
            return false;
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
        return true;
    }

    /**
     * Method to get the data from the users csv file
     */
    // public void getUserData() {
    // users = new File("users.csv");
    //
    // try {
    // PrintWriter pw = new PrintWriter(new FileWriter(users));
    // pw.print("testtt");
    // }
    // catch (Exception e) {
    // }
    //
    // int count = 0;
    // Scanner sc;
    // try {
    // sc = new Scanner(users);
    // while (sc.hasNextLine()) {
    // count++;
    // sc.nextLine();
    // }
    //
    // sc.close();
    // }
    // catch (FileNotFoundException e1) {
    // e1.printStackTrace();
    // }
    //
    // userData = new String[count][4];
    //
    // String csvFile = "users.csv";
    // BufferedReader br = null;
    // String line = "";
    // String csvSplitBy = ",";
    //
    // try {
    // br = new BufferedReader(new FileReader(csvFile));
    // int count2 = 0;
    // while ((line = br.readLine()) != null) {
    // String[] temp = line.split(csvSplitBy);
    // userData[count2][0] = temp[1];
    // userData[count2][1] = temp[3];
    // userData[count2][2] = temp[4];
    // userData[count2][3] = temp[5];
    // count2++;
    // }
    // }
    // catch (FileNotFoundException e) {
    // e.printStackTrace();
    // }
    // catch (IOException e) {
    // e.printStackTrace();
    // }
    // finally {
    // if (br != null) {
    // try {
    // br.close();
    // }
    // catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // }

    public static ArrayList<ChatClientThread> getClientThreads() {
        return clientWorkers;
    }

    public static void main(String args[]) {
        ChatServer cs = new ChatServer();
        cs.listenSocket();
    }

}
