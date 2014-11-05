package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class SettingsThread extends Thread {
    ServerSocket settingsServer;
    UserClass user;
    JTextField[] jFields;

    public SettingsThread(ServerSocket settingsServer) {
        this.settingsServer = settingsServer;
    }

    @Override
    public void run() {
        while (true) {
            try (Socket s = settingsServer.accept();
                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(s.getInputStream());) {

                jFields = (JTextField[]) in.readObject();
                user = (UserClass) in.readObject();
                out.writeObject(updateInfo());
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

    public int updateInfo() {

        int confirmation = 0;
        final File csvFile = new File("users.csv");
        final File tempFile = new File("temp.csv");

        String line = "";
        final String splitBy = ",";
        BufferedReader br = null;
        PrintWriter pw = null;

        try {
            br = new BufferedReader(new FileReader(csvFile));
            pw = new PrintWriter(new FileWriter(tempFile));

            while ((line = br.readLine()) != null) {
                final String[] temp = line.split(splitBy);
                if (temp[1].equals(user.getID())) {
                    continue;
                }
                pw.println(line);
            }

            pw.print("0");
            pw.print(",");
            pw.print(user.getID());
            pw.print(",");

            if (!jFields[0].getText().equals("")) {
                pw.print(jFields[0].getText());
                user.setName(jFields[0].getText());
                confirmation = 1;
            }
            else {
                pw.print(user.getName());
            }

            pw.print(",");
            if (!(jFields[1].getText().equals(""))) {
                if (jFields[1].getText().equals(jFields[2].getText())) {
                    pw.print(jFields[1].getText());
                    user.setPassword(jFields[1].getText());
                    confirmation = 1;
                }
                else {
                    JOptionPane.showMessageDialog(new JFrame(), "Password fields don't match!");
                    jFields[1].setText("");
                    jFields[2].setText("");
                }
            }
            else {
                pw.print(user.getPassword());
            }

            pw.print(",");
            if (!jFields[3].getText().equals("")) {
                pw.print(jFields[3].getText());
                user.setQuestion(jFields[3].getText());
                confirmation = 1;
            }
            else {
                pw.print(user.getQuestion());
            }

            pw.print(",");
            if (!jFields[4].getText().equals("")) {
                pw.print(jFields[4].getText());
                user.setAnswer(jFields[4].getText());
                confirmation = 1;
            }
            else {
                pw.print(user.getAnswer());
            }
            pw.println();

        }
        catch (final Exception ex) {
            ex.printStackTrace();
            confirmation = 2;
        }
        finally {
            try {
                br.close();
            }
            catch (final IOException e1) {
                e1.printStackTrace();
            }
            pw.close();
        }
        csvFile.delete();
        tempFile.renameTo(new File("users.csv"));
        return confirmation;
    }
}
