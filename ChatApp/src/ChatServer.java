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
    ServerSocket forgottenServer;



    public ChatServer() {
    }

    public void listenSocket() {
        int ok = 0;
        try {
            ok = 4454;
            server = new ServerSocket(4454);
            ok = 4455;
            loginServer = new ServerSocket(4455);
            ok = 4459;
            registerServer = new ServerSocket(4459);
            ok = 4456;
            settingsServer = new ServerSocket(4456);
            ok = 4457;
            forgottenServer = new ServerSocket(4457);
        }
        catch (IOException e) {
            System.out.println("Could not listen to port " + ok);
            System.exit(-1);
        }

        LoginThread lt = new LoginThread(loginServer);
        lt.start();

        RegisterThread rt = new RegisterThread(registerServer);
        rt.start();

        SettingsThread st = new SettingsThread(settingsServer);
        st.start();

        ForgottenPassThread fpt = new ForgottenPassThread(forgottenServer);
        fpt.start();


        MainChatThread mct = new MainChatThread(server);
        mct.start();


    }

    public static void main(String args[]) {
        ChatServer cs = new ChatServer();
        cs.listenSocket();
    }

}
