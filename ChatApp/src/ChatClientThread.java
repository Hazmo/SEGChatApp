
package src;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ChatClientThread extends Thread {

    Socket socket;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    int index;
    String roomName = null;

    public ChatClientThread(Socket socket, int index) {
        this.socket = socket;
        this.index = index;
    }

    public void sendMessage(MessageClass message) {
        try {
            out.writeObject(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendConfirmation(boolean ok) {
        try {
            out.writeObject(ok);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        boolean ok = true;
        while (ok) {
            MessageClass message;
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());

                MessageClass initialMessage = (MessageClass) in.readObject();
                roomName = initialMessage.getRoomName();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("I/O failed.");
                ok = false;
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            while (ok) {
                try {
                    ok = (boolean) in.readObject();
                    if (!ok) {
                        out.writeObject(ok);
                        break;
                    }
                    message = (MessageClass) in.readObject();
                    for (ChatClientThread thread : MainChatThread.getClientThreads()) {
                        if (message.getRoomName().equals(thread.getRoomName())) {
                            thread.sendConfirmation(true);
                            thread.sendMessage(message);
                        }
                    }
                } catch (IOException e) {
                    ok = false;
                    // System.out.println("Read failed.");
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            ArrayList<ChatClientThread> clientWorkers = MainChatThread.getClientThreads();
            clientWorkers.remove(index);
            for (int i = index; i < clientWorkers.size(); i++) {
                clientWorkers.get(i).index--;
            }
            try {
                out.close();
                in.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            MainChatThread.count--;
        }
    }

    private String getRoomName() {
        return roomName;
    }
}
