package src;


import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Harry on 05/11/2014.
 */

public class RegisterLoginServerThread extends Thread {

    ServerSocket server;

    public RegisterLoginServerThread(ServerSocket server) {
        this.server = server;
    }

    public void run() {
        while (true) {
            try {
                RegisterLoginThread rlg = new RegisterLoginThread(server.accept());
                rlg.start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
