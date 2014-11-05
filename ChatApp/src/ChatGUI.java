package src;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
    
    JList userWindow = new JList();
    DefaultListModel<String> userModel = new DefaultListModel<String>();
    JScrollPane jsp2 = new JScrollPane(userWindow);
    
    JTextField enterText = new JTextField();
    JButton send = new JButton("Send");

    ChatRoomClass chatRoom;
    UserClass user;

    public ChatGUI(final ChatRoomClass chatRoom, UserClass user) {
        setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
        this.chatRoom = chatRoom;
        this.user = user;

        setTitle(chatRoom.toString());

        chatWindow.setModel(chatModel);

        userWindow.setModel(userModel);
        userModel.addElement(user.getName());
        
        enterText.setColumns(10);
        
        SwingUtilities.getRootPane(this).setDefaultButton(send);
        
        rightClickOption();
        sendMessageListener();
        setLayout();
        
        setVisible(true);
        setSize(500, 500);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    socket.close();
                    in.close();
                    out.close();
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
    
    public void setLayout(){
        JPanel center = new JPanel(new BorderLayout());
        
        JPanel centerWindow = new JPanel(new BorderLayout());
        centerWindow.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        centerWindow.add(jsp, BorderLayout.CENTER);
        center.add(centerWindow, BorderLayout.CENTER);
        
        JPanel eastWindow = new JPanel(new BorderLayout());
        eastWindow.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));
        eastWindow.add(jsp2, BorderLayout.CENTER);
        center.add(eastWindow, BorderLayout.EAST);
        
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
    
    public void rightClickOption(){
        class RightClickMenu extends JPopupMenu {
            JMenuItem reportItem;

            public RightClickMenu() {
                this.reportItem = new JMenuItem("Report");
                this.add(this.reportItem);
                this.reportItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new UserReport();
                    }
                });
            }
        }
        chatWindow.addMouseListener(new MouseAdapter(){
        	
        	@Override
			public void mousePressed(MouseEvent e) {
			
				    if (SwingUtilities.isRightMouseButton(e)) {
				    	RightClickMenu menu = new RightClickMenu();
				    	menu.show(e.getComponent(), e.getX(), e.getY());
				    }
				}
        });
    }
    
    public void sendMessageListener(){
        send.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                Calendar calendar = Calendar.getInstance();
            	Timestamp currentTimestamp = new Timestamp(calendar.getTime().getTime());
            	String time = new SimpleDateFormat("yy-MM-dd hh:mm").format(currentTimestamp);
            	
                String text = user.getName() + " (" + time + ") : " + enterText.getText();

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
    }
}
