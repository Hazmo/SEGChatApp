package src;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Harry on 04/11/2014.
 */
public class TopicsServerThread extends Thread {

    ArrayList<TopicClass> topics = new ArrayList<TopicClass>();
    DefaultListModel topicsModel = new DefaultListModel();
    ServerSocket server;

    TopicsServerThread(ServerSocket server) {
        this.server = server;

        TopicClass t = new TopicClass("Hello");
        ChatRoomClass c = new ChatRoomClass("hello", "hello");
        t.addChatRoom(c);
        t.addRow(c);

        topics.add(t);
        topicsModel.addElement("Hello");
    }

    public void run() {
        while (true) {
            try (Socket s = server.accept();
                 ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(s.getInputStream());) {
                System.out.println("Reached");
                MessageClass message = (MessageClass) in.readObject();
                System.out.println(message.getMessage());

                if(message.getMessageType().equals("get_topics")) {
                    out.writeObject(topics);
                    out.writeObject(topicsModel);
                }

                else if(message.getMessageType().equals("send_topics")) {
                    ArrayList<TopicClass> topicsSent = (ArrayList<TopicClass>) in.readObject();
                    topics = topicsSent;
                }

            } catch (IOException e) {

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
