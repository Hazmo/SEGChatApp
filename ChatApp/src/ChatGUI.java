package src;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.management.ObjectInstance;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatGUI extends JFrame {

    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

    JTextArea chatWindow;
    JScrollPane jsp;
    JTextField userName;
    JTextField enterText;
    JButton send;
    JButton quit;

    JPanel jp;

    ChatRoomClass chatRoom;

    public ChatGUI(final ChatRoomClass chatRoom) {
        this.chatRoom = chatRoom;

        setTitle(chatRoom.toString());

        chatWindow = new JTextArea();
        jsp = new JScrollPane(chatWindow);

        userName = new JTextField();
        userName.setColumns(5);
        enterText = new JTextField();
        enterText.setColumns(10);

        send = new JButton("Send");
        send.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String text = userName.getText() + ": " + enterText.getText();

                MessageClass message = new MessageClass(text, chatRoom);

                try {
                    out.writeObject(message);
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
                enterText.setText("");

            }
        });

        quit = new JButton("quit");
        quit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

            }
        });

        jp = new JPanel(new FlowLayout());

        jp.add(userName);
        jp.add(enterText);
        jp.add(send);
        jp.add(quit);

        add(jsp, BorderLayout.CENTER);
        add(jp, BorderLayout.SOUTH);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);
        // pack();

        listenSocket();
        MessageThread t = new MessageThread(in, chatWindow);
        t.start();

    }

    public void listenSocket() {
        try {
            socket = new Socket("localhost", 4455);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Send the initial connection message to the server giving the information about this
            // particular chatroom
            // specifically it's name
            MessageClass initialMessage = new MessageClass(null, chatRoom);
            out.writeObject(initialMessage);

        }
        catch (UnknownHostException e) {
            System.out.println("UnknownHostException");
            System.exit(1);
        }
        catch (IOException e) {
            System.out.println("No I/O in gui");
            System.exit(1);
        }
    }
}
