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

    static ArrayList<TopicClass> topics = new ArrayList<TopicClass>();
    static DefaultListModel topicsModel = new DefaultListModel();
    ServerSocket server;

    TopicsServerThread(ServerSocket server) {
        this.server = server;
    }

    public void run() {
        while(true) {
            try {
                TopicsThread tt = new TopicsThread(server.accept());
                tt.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<TopicClass> getTopics() {
        return topics;
    }

    public static DefaultListModel getTopicsModel() {
        return topicsModel;
    }

    public static void setTopics(ArrayList<TopicClass> topicsSent) {
        topics = topicsSent;
    }

    public static void setTopicsModel(DefaultListModel topicsModelSent) {
        topicsModel = topicsModelSent;
    }
}