package src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JTextField;

public class RegisterThread extends Thread {
    JTextField[] jFields = new JTextField[6];
    String[][] userData;
    File users;
    ServerSocket registerServer;
    //ObjectInputStream in = null;
    //ObjectOutputStream out = null;

    public RegisterThread(ServerSocket registerServer) {
        this.registerServer = registerServer;
    }

    @Override
    public void run() {
        while (true) {
            try(Socket s = registerServer.accept();
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());) {

                jFields = (JTextField[]) in.readObject();
                boolean registered = registerUser();
                out.writeObject(registered);
            }
            catch (Exception e) {
                if (e.getClass().equals(IOException.class)) {
                    System.out.println("Accept failed: 4456");
                    System.exit(-1);
                }
                else if (e.getClass().equals(ClassNotFoundException.class))
                    e.printStackTrace();
            }
        }
    }

    public boolean registerUser() {
        FileWriter registrationFileWriter = null;
        try {
            registrationFileWriter = new FileWriter("users.csv", true);
        }
        catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
        PrintWriter toFileWriter = new PrintWriter(registrationFileWriter);

        toFileWriter.print("0");
        toFileWriter.print(",");
        toFileWriter.print(jFields[1].getText().toLowerCase());
        toFileWriter.print(",");
        toFileWriter.print(jFields[0].getText());
        toFileWriter.print(",");
        toFileWriter.print(jFields[2].getText());
        toFileWriter.print(",");
        toFileWriter.print(jFields[4].getText());
        toFileWriter.print(",");
        toFileWriter.print(jFields[5].getText().toLowerCase());
        toFileWriter.println();

        toFileWriter.flush();

        toFileWriter.close();

        try {
            registrationFileWriter.close();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        return true;
    }
}
