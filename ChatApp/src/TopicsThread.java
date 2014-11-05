package src;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Harry on 04/11/2014.
 */
public class TopicsThread extends Thread {

    Socket s;

    public TopicsThread(Socket s) {
        this.s = s;
    }

    public void run() {
        try (
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(s.getInputStream());) {

            while(true) {

                MessageClass message = (MessageClass) in.readObject();

                if (message.getMessageType().equals("get_topics")) {
                    out.writeObject(TopicsServerThread.getTopics());
                    out.writeObject(TopicsServerThread.getTopicsModel());

                } else if (message.getMessageType().equals("send_topics")) {

                    TopicsServerThread.setTopics((ArrayList<TopicClass>) in.readObject());
                    TopicsServerThread.setTopicsModel((DefaultListModel) in.readObject());
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
