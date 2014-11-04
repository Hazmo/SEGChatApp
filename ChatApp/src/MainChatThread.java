package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Created by Harry on 03/11/2014.
 */
public class MainChatThread extends Thread {

    ServerSocket server;
    static ArrayList<ChatClientThread> clientWorkers = new ArrayList<ChatClientThread>();

    public MainChatThread(ServerSocket server) {
        this.server = server;

    }


    @Override
    public void run() {
        ChatClientThread ccl;
        try {
            while(true) {
                ccl = new ChatClientThread(server.accept());
                clientWorkers.add(ccl);
                ccl.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<ChatClientThread> getClientThreads() {
        return clientWorkers;
    }

}
