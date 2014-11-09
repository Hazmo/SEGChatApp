package src;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Harry on 05/11/2014.
 */
public class MainServerSocketThread extends Thread{

    Socket s;
    MainServerThread server;


    //RegisterLogin Variables
    String studentID;
    String password;
    UserClass user = null;
    JTextField[] jFields = new JTextField[6];
    String[][] userDataRegister;
    static boolean ok = false;
    UserData userData = new UserData("users.csv");

    public MainServerSocketThread(MainServerThread server, Socket s) {
        this.server = server;
        this.s = s;
    }

    public void run() {

        try (ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());) {

            while (true) {


                MessageClass message = (MessageClass) in.readObject();

                if (message.getMessageType().equals("get_topics")) {
                    out.writeObject(server.getTopics());
                    out.writeObject(server.getTopicsModel());
                    out.writeObject(server.getChatRoomsList());

                } else if (message.getMessageType().equals("send_topics")) {
                    server.setTopics((ArrayList<TopicClass>) in.readObject());
                    server.setTopicsModel((DefaultListModel) in.readObject());
                    server.setChatRoomsList((ArrayList<ChatRoomClass>) in.readObject());

                } else if (message.getMessageType().equals("log_in")) {
                    studentID = message.getMessage();
                    password = (String) message.getExtraData()[0];
                    System.out.println("studentID = " + studentID);
                    System.out.println("password = " + password);

                    boolean loggedIn = userData.loginUser(studentID, password);

                    System.out.println("Boolean.toString(loggedIn) = " + Boolean.toString(loggedIn));
                    out.writeObject(new MessageClass("logged_in", Boolean.toString(loggedIn)));
                    if (loggedIn) {
                        user = userData.getUserClass();
                        out.writeObject(user);
                    }

                } else if (message.getMessageType().equals("register")) {
                    jFields = (JTextField[]) message.getExtraData();
                    try {
                        userDataRegister = userData.getUserDataRegister();
                        ok = true;
                    } catch (Exception e) {
                        ok = false;
                    }
                    if (ok == true) {
                        boolean duplicate = false;
                        for (int i = 0; i < userDataRegister.length; i++) {
                            if (userDataRegister[i][0].equals(jFields[1].getText())) {
                                duplicate = true;
                                break;
                            }
                        }
                        if (duplicate == true) {
                            out.writeObject(4);
                        } else {
                            int registered = userData.registerUser(jFields);
                            out.writeObject(registered);
                        }
                    } else {
                        int registered = userData.registerUser(jFields);
                        out.writeObject(registered);
                        ok = true;
                    }
                } else if (message.getMessageType().equals("update_settings")) {
                    JTextField settingsFields[] = (JTextField[]) in.readObject();
                    for (JTextField settings : settingsFields) {
                        System.out.println("settings. = " + settings.getText());
                    }
                    UserClass settingsUser = (UserClass) in.readObject();
                    out.writeObject(userData.updateInfo(settingsUser, settingsFields));


                } else if (message.getMessageType().equals("forgot_password")) {


                    Object input = in.readObject();
                    boolean confirm = userData.validateID(((JLabel) input).getText());
                    out.writeObject(confirm);
                    if (confirm) {
                        out.writeObject(userData.getForgottenQuestion());
                    }

                } else if (message.getMessageType().equals("get_password")) {
                    Object input = in.readObject();
                    if (input.equals(userData.getForgottenAnswer())) {
                        boolean confirm = true;
                        out.writeObject(confirm);
                        out.writeObject(userData.getForgottenPassword());
                    } else {
                        boolean confirm = false;
                        out.writeObject(confirm);
                    }

                } else if (message.getMessageType().equals("add_reports")) {
                    server.addReport((ReportClass) in.readObject());
                    out.writeObject(new String("Report has been logged."));

                } else if (message.getMessageType().equals("get_reports")) {
                    out.writeObject(server.getReports());

                } else if (message.getMessageType().equals("resolve_report")) {
                    server.setReports((ArrayList<ReportClass>) in.readObject());

                } else if (message.getMessageType().equals("get_chat_model")) {
                    ChatRoomClass chatRoom = (ChatRoomClass) message.getExtraData()[0];
                    chatRoom = server.findChatRoomClass(chatRoom);
                    out.writeObject(chatRoom.getChatModel());
                }
            }

        } catch (IOException e) {
            e.getMessage();
        } catch (ClassNotFoundException e) {
            e.getMessage();
        }
    }
}
