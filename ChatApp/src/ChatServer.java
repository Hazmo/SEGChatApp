package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

    ServerSocket server;
    ServerSocket registerServer;
    ServerSocket loginServer;
    ServerSocket settingsServer;
    Socket client;
    static ArrayList<ChatClientThread> clientWorkers = new ArrayList<ChatClientThread>();

    public ChatServer() {
    }

    public void listenSocket() {
        try {
            server = new ServerSocket(4454);
            loginServer = new ServerSocket(4455);
            registerServer = new ServerSocket(4459);
            settingsServer = new ServerSocket(4456);
        }
        catch (IOException e) {
            System.out.println("Could not listen to port");
            System.exit(-1);
        }

        LoginThread lt = new LoginThread(loginServer);
        lt.start();

        RegisterThread rt = new RegisterThread(registerServer);
        rt.start();

        SettingsThread st = new SettingsThread(settingsServer);
        st.start();

        ChatClientThread ccl = new ChatClientThread(server);
        clientWorkers.add(ccl);
        ccl.start();

    }

    public static ArrayList<ChatClientThread> getClientThreads() {
        return clientWorkers;
    }

    public static void main(String args[]) {
        ChatServer cs = new ChatServer();
        cs.listenSocket();
    }

}
