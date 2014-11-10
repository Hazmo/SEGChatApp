package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;

/**
 * Created by Harry on 05/11/2014.
 */
public class MainServerThread extends Thread {

    ServerSocket server;

    String initialTopics[] = { "Informatics", "Mathematics" };
    ArrayList<TopicClass> topics = new ArrayList<>();
    DefaultListModel topicsModel = new DefaultListModel();
    ArrayList<ChatRoomClass> chatRooms = new ArrayList<>();
    ArrayList<UserClass> users = new ArrayList<UserClass>();
    HashMap<String, WarningClass> warnings = new HashMap<String, WarningClass>();
    ArrayList<ReportClass> reports = new ArrayList<>();

    public MainServerThread(ServerSocket server) {
        this.server = server;
        setUpInitialTopics();
    }

    public void run() {
        while (true) {
            try {
                MainServerSocketThread rlg = new MainServerSocketThread(this, server.accept());
                rlg.start();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpInitialTopics() {
        for (String topicName : initialTopics) {
            topics.add(new TopicClass(topicName));
            topicsModel.addElement(topicName);
        }
    }

    public synchronized WarningClass getWarning(String userID) {
        return warnings.get(userID);
    }

    public synchronized void addUser(UserClass user) {
        users.add(user);
    }

    // topic stuff
    public synchronized ArrayList<TopicClass> getTopics() {
        return topics;
    }

    public synchronized DefaultListModel getTopicsModel() {
        return topicsModel;
    }

    public synchronized ArrayList<ChatRoomClass> getChatRoomsList() {
        return chatRooms;
    };

    public synchronized void setTopics(ArrayList<TopicClass> topicsSent) {
        topics = topicsSent;
    }

    public synchronized void setTopicsModel(DefaultListModel topicsModelSent) {
        topicsModel = topicsModelSent;
    }

    public synchronized void setChatRoomsList(ArrayList<ChatRoomClass> chatRoomsListSent) {
        chatRooms = chatRoomsListSent;
    }

    // report stuff
    public synchronized ArrayList<ReportClass> getReports() {
        return reports;
    }

    public synchronized void setReports(ArrayList<ReportClass> reportsSent) {
        reports = reportsSent;
    }

    public synchronized void addReport(ReportClass report) {
        reports.add(report);
    }

    public synchronized void setWarnings(HashMap<String, WarningClass> warningsSent) {
        warnings = warningsSent;
    }

    public synchronized void addWarning(WarningClass warning) {
        warnings.put(warning.getUser(), warning);
    }
}
