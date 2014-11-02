package src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatClientThread extends Thread {

    ServerSocket server;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;

    String roomName = null;

    public ChatClientThread(ServerSocket server) {
        System.out.println("Inside the ChatClientThread constructor");
        this.server = server;

    }

    public void sendMessage(MessageClass message) throws IOException {
        out.writeObject(message);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket s = server.accept();
                MessageClass message;
                try {
                    in = new ObjectInputStream(s.getInputStream());
                    out = new ObjectOutputStream(s.getOutputStream());

                    MessageClass initialMessage = (MessageClass) in.readObject();
                    roomName = initialMessage.getRoomName();
                }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.out.println("I/O failed.");
                    System.exit(-1);

                }
                catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                while (true) {

                    try {
                        message = (MessageClass) in.readObject();

                        for (ChatClientThread thread : ChatServer.getClientThreads()) {
                            if (message.getRoomName().equals(thread.getRoomName())) {
                                thread.sendMessage(message);
                            }
                        }
                    }
                    catch (IOException e) {
                        System.out.println("Read failed");
                        System.exit(-1);
                    }
                    catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            catch (IOException e) {
                System.out.println("Accept failed: 4455");
                System.exit(-1);
            }
        }

    }

    private String getRoomName() {
        return roomName;
    }
}
