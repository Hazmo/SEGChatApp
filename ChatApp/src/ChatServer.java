import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {

    ServerSocket server;
    Socket client;
    static ArrayList<ChatClientThread> clientWorkers = new ArrayList<ChatClientThread>();

    public void listenSocket() {
        try {
            server = new ServerSocket(444);
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

    public static ArrayList<ChatClientThread> getClientThreads() {
        return clientWorkers;
    }

    public static void main(String args[]) {
        ChatServer cs = new ChatServer();
        cs.listenSocket();
    }

}
