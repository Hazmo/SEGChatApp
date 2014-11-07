package src;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Harry on 05/11/2014.
 */
public class RegisterLoginThread extends Thread{

    Socket s;
    RegisterLoginServerThread server;


    //RegisterLogin Variables
    String studentID;
    String password;
    UserClass user;
    JTextField[] jFields = new JTextField[6];
    String[][] userDataRegister;
    static boolean ok = false;
    UserData userData = new UserData("users.csv");



    public RegisterLoginThread(RegisterLoginServerThread server, Socket s) {
        this.server = server;
        this.s = s;
    }

    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());) {

            while (true) {

                System.out.println("s.isClosed() = " + s.isClosed());

                MessageClass message = (MessageClass) in.readObject();

                if (message.getMessageType().equals("get_topics")) {
                    System.out.println("inside");
                    out.writeObject(server.getTopics());
                    out.writeObject(server.getTopicsModel());

                } else if (message.getMessageType().equals("send_topics")) {
                    System.out.println("inside send");
                    server.setTopics((ArrayList<TopicClass>) in.readObject());
                    server.setTopicsModel((DefaultListModel) in.readObject());

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
                    }
                    catch (Exception e) {
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
                        }
                        else {
                            int registered = userData.registerUser(jFields);
                            out.writeObject(registered);
                        }
                    }
                    else {
                        int registered = userData.registerUser(jFields);
                        out.writeObject(registered);
                        ok = true;
                    }
                } else if(message.getMessageType().equals("update_settings")) {
                    JTextField settingsFields[] = (JTextField[]) in.readObject();
                    for(JTextField settings : settingsFields) {
                        System.out.println("settings. = " + settings.getText());
                    }
                    UserClass settingsUser = (UserClass) in.readObject();
                    out.writeObject(userData.updateInfo(settingsUser, settingsFields));
                }

            }
        } catch (IOException e) {
            e.getMessage();
        } catch (ClassNotFoundException e) {
            e.getMessage();
        }
    }


}
