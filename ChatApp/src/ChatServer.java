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
    }

    public void listenSocket() {
        try {
            server = new ServerSocket(4455);
            registerServer = new ServerSocket(4459);
        }
        catch (IOException e) {
            System.out.println("Could not listen to port");
            System.exit(-1);
        }

        RegisterThread rt = new RegisterThread(registerServer);
        rt.start();

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
