package src;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ChatGUI extends JFrame {

    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

    JList chatWindow = new JList();
    DefaultListModel<String> chatModel = new DefaultListModel<String>();
    JScrollPane jsp = new JScrollPane(chatWindow);
    
    JTextField enterText = new JTextField();
    JButton send = new JButton("Send");

    ChatRoomClass chatRoom;
    UserClass user;
    String color;

    public ChatGUI(final ChatRoomClass chatRoom, final UserClass user) {
        setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
        this.chatRoom = chatRoom;
        this.user = user;

        setTitle(chatRoom.toString());
        chatWindow.setModel(chatModel);

        enterText.setColumns(10);
        SwingUtilities.getRootPane(this).setDefaultButton(send);

        chatWindow.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent e) {

                if (SwingUtilities.isRightMouseButton(e)) {

                    Point p = e.getPoint();
                    chatWindow.setSelectedIndex(chatWindow.locationToIndex(p));
                    if (chatWindow.isSelectionEmpty()==false) {
                        RightClickMenu menu = new RightClickMenu();
                        menu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }

            /**
             * class with the constructor that creates menu on right mouse click.
             */
            class RightClickMenu extends JPopupMenu {
                JMenuItem reportItem;

                public RightClickMenu() {
                    this.reportItem = new JMenuItem("Report");
                    this.add(this.reportItem);
                    this.reportItem.addActionListener(new ActionListener() {
                    	@Override
                        public void actionPerformed(ActionEvent e) {
                            String messageTitle =chatWindow.getSelectedValue().toString();
                            try {
								Socket repSock = new Socket("localhost", 4455);
								ObjectOutputStream repOut = new ObjectOutputStream(repSock.getOutputStream());
					            ObjectInputStream repIn = new ObjectInputStream(repSock.getInputStream());
								new UserReport("message", messageTitle, user.getName(), socket, repOut, repIn);
							} catch (UnknownHostException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
                        }
                    });
                }
            }

        });

        selectColor();
        sendMessageListener();
        setLayout();

        setVisible(true);
        setSize(500, 500);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    out.writeObject(false);
                    in.close();
                    out.close();
                    socket.close();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        listenSocket();
        MessageThread t = new MessageThread(in, chatWindow, chatModel);
        t.start();
    }
    private void selectColor() {
        int rnd = (int)(Math.random() * ((12) + 1));
        switch(rnd) {
            case 0: color = "Blue"; break;
            case 1: color = "Aqua"; break;
            case 2: color = "Fuchsia"; break;
            case 3: color = "Gray"; break;
            case 4: color = "Green"; break;
            case 5: color = "Lime"; break;
            case 6: color = "Maroon"; break;
            case 7: color = "Navy"; break;
            case 8: color = "Olive"; break;
            case 9: color = "Purple"; break;
            case 10: color = "Red"; break;
            case 11: color = "Teal"; break;
            case 12: color = "Yellow"; break;
        }
    }
    public void setLayout() {
        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        center.add(jsp, BorderLayout.CENTER);
        
        JPanel jp = new JPanel(new FlowLayout());

        jp.add(enterText);
        jp.add(send);

        add(center, BorderLayout.CENTER);
        add(jp, BorderLayout.SOUTH);

    }

    public void listenSocket() {
        try {
            socket = new Socket("localhost", 4454);
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


    public void sendMessageListener() {
        send.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                Calendar calendar = Calendar.getInstance();
                Timestamp currentTimestamp = new Timestamp(calendar.getTime().getTime());
                String time = new SimpleDateFormat("yy-MM-dd hh:mm").format(currentTimestamp);

                String text = "<html><font color=\""+ color+"\">"+user.getName() + " (" + time + ") : </font>" + enterText.getText() + "</html>";
                if (enterText.getText().equals("")) {
                    return;
                }
                MessageClass message = new MessageClass(text, chatRoom);

                try {

                    out.writeObject(true);
                    out.writeObject(message);
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
                enterText.setText("");

            }
        });
    }
}
