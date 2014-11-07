

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatClientThread extends Thread {

    Socket socket;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;

    String roomName = null;

    public ChatClientThread(Socket socket) {
        this.socket = socket;

    }

    public void sendMessage(MessageClass message) throws IOException {
        out.writeObject(message);
    }

    @Override
    public void run() {
        while (true) {
            MessageClass message;
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());

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

                    for (ChatClientThread thread : MainChatThread.getClientThreads()) {
                        if (message.getRoomName().equals(thread.getRoomName())) {
                            thread.sendMessage(message);
                        }
                    }
                }

                catch (IOException e) {
                    System.out.println("Read failed.");
                    System.exit(-1);
                }
                catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    private String getRoomName() {
        return roomName;
    }
}
