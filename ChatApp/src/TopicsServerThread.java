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

    String initialTopics[] = {"Informatics", "Mathematics"};

    ArrayList<TopicClass> topics = new ArrayList<TopicClass>();
    DefaultListModel topicsModel = new DefaultListModel();
    ServerSocket server;

    TopicsServerThread(ServerSocket server) {
        this.server = server;


        setUpInitialTopics();
    }

    private void setUpInitialTopics() {
        for(String topicName : initialTopics) {
            topics.add(new TopicClass(topicName));
            topicsModel.addElement(topicName);
        }
    }

    public void run() {
        while(true) {
            try {
                TopicsThread tt = new TopicsThread(this, server.accept());
                tt.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized ArrayList<TopicClass> getTopics() {
        return topics;
    }

    public synchronized DefaultListModel getTopicsModel() {
        return topicsModel;
    }

    public synchronized void setTopics(ArrayList<TopicClass> topicsSent) {
        topics = topicsSent;
    }

    public synchronized void setTopicsModel(DefaultListModel topicsModelSent) {
        topicsModel = topicsModelSent;
    }
}