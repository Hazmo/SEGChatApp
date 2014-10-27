import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JTextArea;


public class MessageThread extends Thread{
	
	BufferedReader in;
	JTextArea ta;
	
	public MessageThread(BufferedReader in, JTextArea ta) {
		this.in = in;
		this.ta = ta;
		
	}
	
	public void run() {
		String line;
		
		try {
			while(true) {
				line = in.readLine();
				ta.append(line + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
