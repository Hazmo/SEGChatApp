package src;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ChatClientThread extends Thread {

    Socket client;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    
    String roomName = null;

    public ChatClientThread(Socket client) {
        this.client = client;

    }

    public void sendMessage(MessageClass message) throws IOException {
        out.writeObject(message);
    }

    @Override
    public void run() {
        MessageClass message;
        try {
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
            
            MessageClass initialMessage = (MessageClass) in.readObject();
            roomName = initialMessage.getRoomName();            
        }
        catch (IOException e) {
            System.out.println("I/O failed.");
            System.exit(-1);
            
        } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        

        while (true) {
        	
        	
            try {
                message = (MessageClass) in.readObject();
             
                for (ChatClientThread thread : ChatServer.getClientThreads()) {
                	if(message.getRoomName().equals(thread.getRoomName())) {
                		thread.sendMessage(message);
                	}
                }              
            }
            catch (IOException e) {
                System.out.println("Read failed");
                System.exit(-1);
            } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    private String getRoomName() {
    	return roomName;
    }
}
