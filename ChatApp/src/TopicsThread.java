
package src;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
/**
 * Created by Harry on 04/11/2014.
 */
public class TopicsThread extends Thread {

    Socket s;

    TopicsServerThread server;

    public TopicsThread(TopicsServerThread server, Socket s) {
        this.s = s;
        this.server = server;
    }

    public void run() {
        try (
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(s.getInputStream());) {

            while(true) {

                MessageClass message = (MessageClass) in.readObject();

                if (message.getMessageType().equals("get_topics")) {
                    out.writeObject(server.getTopics());
                    out.writeObject(server.getTopicsModel());

                } else if (message.getMessageType().equals("send_topics")) {

                    server.setTopics((ArrayList<TopicClass>) in.readObject());
                    server.setTopicsModel((DefaultListModel) in.readObject());
                }
            }

        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
