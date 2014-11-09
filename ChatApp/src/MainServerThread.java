package src;


import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import javax.swing.*;
import java.util.ArrayList;


/**
 * Created by Harry on 05/11/2014.
 */
public class MainServerThread extends Thread{

    ServerSocket server;


    String initialTopics[] = {"Informatics", "Mathematics"};
    ArrayList<TopicClass> topics = new ArrayList<>();
    DefaultListModel topicsModel = new DefaultListModel();
    ArrayList<ChatRoomClass> chatRooms = new ArrayList<>();

    ArrayList<ReportClass> reports = new ArrayList<>();

    public MainServerThread(ServerSocket server) {
        this.server = server;
        setUpInitialTopics();
    }

    public void run() {
        while(true) {
            try {
                MainServerSocketThread rlg = new MainServerSocketThread(this,server.accept());
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

    //topic stuff
    public synchronized ArrayList<TopicClass> getTopics() {
        return topics;
    }
    public synchronized DefaultListModel getTopicsModel() {
        return topicsModel;
    }
    public synchronized ArrayList<ChatRoomClass> getChatRoomsList(){
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

    //report stuff
    public synchronized ArrayList<ReportClass> getReports() {
        return reports;
    }
    public synchronized void setReports(ArrayList<ReportClass> reportsSent) {
        reports = reportsSent;
    }
    public synchronized void addReport(ReportClass report) {
        reports.add(report);
    }


    public synchronized ChatRoomClass findChatRoomClass(ChatRoomClass chatRoomClass) {
        for (ChatRoomClass chatRoom : chatRooms) {
            System.out.println(chatRoom.equals(chatRoomClass));
            if(chatRoom.equals(chatRoomClass)) {
                System.out.println("we find it");
                return chatRoom;
            }
        }
        return null;
    }
 }
