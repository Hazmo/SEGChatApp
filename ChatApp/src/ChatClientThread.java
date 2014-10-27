import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClientThread extends Thread {

    Socket client;
    BufferedReader in = null;
    PrintWriter out = null;

    public ChatClientThread(Socket client) {
        this.client = client;

    }

    public void sendMessage(String message) {
        out.println(message);
    }

    @Override
    public void run() {
        String line;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        }
        catch (IOException e) {
            System.out.println("I/O failed.");
            System.exit(-1);
        }

        while (true) {
            try {
                line = in.readLine();

                for (ChatClientThread thread : ChatServer.getClientThreads()) {
                    thread.sendMessage(line);
                }
            }
            catch (IOException e) {
                System.out.println("Read failed");
                System.exit(-1);
            }
        }
    }
}
