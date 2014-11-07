

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ReportThread extends Thread {

	ServerSocket reportServer;
	ArrayList<ReportClass> reports;
	
	ReportThread(ServerSocket reportServer, ArrayList<ReportClass> reports) {
		this.reportServer = reportServer;
		this.reports = reports;
	}
	
	@Override
	public void run() {
		while(true) {
			try (Socket s = reportServer.accept();
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			) {
				String socketHeader = (String) in.readObject();
				switch(socketHeader) {
					case "ADD":
						try {					
							reports.add((ReportClass) in.readObject());
							out.writeObject(new String("Report has been logged."));
						} catch (Exception e) {
							out.writeObject(new String("Error when trying to log report."));
							e.printStackTrace();
						}
						break;
					case "READ":
						out.writeObject(reports);
						break;
					case "RESOLVE":
						reports = (ArrayList<ReportClass>) in.readObject();
						break;
				}
            }
            catch (Exception e) {
                if (e.getClass().equals(IOException.class)) {
                    System.out.println("Accept failed: 4457");
                    System.exit(-1);
                }
                else if (e.getClass().equals(ClassNotFoundException.class))
                    e.printStackTrace();
            }
		}
	}
	
}
