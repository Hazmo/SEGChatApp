package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer {

    ServerSocket server;
    ServerSocket registerServer;
    ServerSocket loginServer;
    ServerSocket settingsServer;
    ServerSocket forgottenServer;
    ServerSocket topicsServer;
    ServerSocket mainServerSocket;

    ServerSocket reportServer;
    static ArrayList<ReportClass> reports = new ArrayList<ReportClass>();

    public ChatServer() {
    }

    public void listenSocket() {
        int ok = 0;
        try {
            ok = 4454;
            server = new ServerSocket(4454);
            ok = 4455;
            mainServerSocket = new ServerSocket(4455);

            // registerServer = new ServerSocket(4459);

        }
        catch (IOException e) {
            System.out.println("Could not listen to port " + ok);
            System.exit(-1);
        }

        // LoginThread lt = new LoginThread(loginServer);
        // lt.start();

        MainServerThread tlst = new MainServerThread(mainServerSocket);
        tlst.start();
        MainChatThread mct = new MainChatThread(server);
        mct.start();
    }

    public static void main(String args[]) {
        ChatServer cs = new ChatServer();
        cs.listenSocket();
    }

}
