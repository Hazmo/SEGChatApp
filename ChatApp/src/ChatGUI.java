import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatGUI extends JFrame {

    Socket socket;
    PrintWriter out;
    BufferedReader in;

    JTextArea chatWindow;
    JScrollPane jsp;
    JTextField userName;
    JTextField enterText;
    JButton send;
    JButton quit;

    JPanel jp;

    ChatRoomClass chatRoom;

    public ChatGUI(ChatRoomClass chatRoom) {
        this.chatRoom = chatRoom;

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
                out.println(text);
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
            socket = new Socket("localhost", 444);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

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
