package src;


import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.*;
import java.util.ArrayList;


/**
 * Created by Harry on 05/11/2014.
 */
public class RegisterLoginServerThread extends Thread{

    ServerSocket server;


    String initialTopics[] = {"Informatics", "Mathematics"};
    ArrayList<TopicClass> topics = new ArrayList<TopicClass>();
    DefaultListModel topicsModel = new DefaultListModel();

    public RegisterLoginServerThread (ServerSocket server) {
        this.server = server;
        setUpInitialTopics();
    }

    public void run() {
        while(true) {
            try {
                RegisterLoginThread rlg = new RegisterLoginThread(this,server.accept());
                rlg.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpInitialTopics() {
        for(String topicName : initialTopics) {
            topics.add(new TopicClass(topicName));
            topicsModel.addElement(topicName);
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
