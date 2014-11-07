package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

public class TopicsServerThread extends Thread {

    String initialTopics[] = { "Informatics", "Mathematics" };

    static ArrayList<TopicClass> topics = new ArrayList<TopicClass>();
    static DefaultListModel topicsModel = new DefaultListModel();
    ServerSocket server;

    TopicsServerThread(ServerSocket server) {
        this.server = server;
        setUpInitialTopics();
    }

    private void setUpInitialTopics() {
        for (String topicName : initialTopics) {
            topics.add(new TopicClass(topicName));
            topicsModel.addElement(topicName);
        }
    }

    public void run() {
        while (true) {
            try {
                TopicsThread tt = new TopicsThread(server.accept());
                tt.start();
            }
            catch (IOException e) {
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
