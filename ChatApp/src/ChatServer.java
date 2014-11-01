package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

    ServerSocket server;
    ServerSocket registerServer;
    Socket client;
    static ArrayList<ChatClientThread> clientWorkers = new ArrayList<ChatClientThread>();

    public ChatServer() {
        // getUserData();
    }

    public void listenSocket() {
        try {
            server = new ServerSocket(4455);
            registerServer = new ServerSocket(4456);
        }
        catch (IOException e) {
            System.out.println("Could not listen to port");
            System.exit(-1);
        }

        while (true) {

            RegisterThread rt = new RegisterThread(registerServer);
            rt.start();

            ChatClientThread ccl = new ChatClientThread(server);
            clientWorkers.add(ccl);
            ccl.start();

        }
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
