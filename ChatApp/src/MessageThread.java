import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JTextArea;


public class MessageThread extends Thread{
	
	ObjectInputStream in;
	JTextArea ta;
	
	public MessageThread(ObjectInputStream in, JTextArea ta) {
		this.in = in;
		this.ta = ta;
		
	}
	
	public void run() {
		String line;
		
		while(true) {
		
			try {			
				
				MessageClass message = (MessageClass) in.readObject();
				ta.append(message.getMessage() + "\n");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
