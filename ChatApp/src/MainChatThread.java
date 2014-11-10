
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
    static int count = 0;

    public MainChatThread(ServerSocket server) {
        this.server = server;

    }
    @Override
    public void run() {
        ChatClientThread ccl;
        try {
            while (true) {
                ccl = new ChatClientThread(server.accept(), count);
                clientWorkers.add(ccl);
                ccl.start();
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<ChatClientThread> getClientThreads() {
        return clientWorkers;
    }

}
