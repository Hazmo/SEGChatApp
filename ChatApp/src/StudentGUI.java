package src;

/**
 *  @author Codrin Gidei - 1326651
 *  @email codrin.gidei@kcl.ac.uk
 */
//import sun.plugin2.message.Message;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import src.StudentGUI.TableMouseListener.RightClickMenu;

/**
 * This class is used to create GUI displaying all available chat rooms in their respective topics,
 * allow the user to access a chat room by double clicking on it, create a new chatroom and also
 * change their settings such as their password and/or username
 */
public class StudentGUI extends JFrame {

    /** The search field. */
    JTextField searchField = new JTextField();

    /** The search button. */
    JButton searchButton = new JButton("Search");

    /** The sign out button. */
    JButton signoutButton = new JButton("Sign out");

    /** The user settings button. */
    JButton settingsButton = new JButton("Settings");

    /** The moderator reports button. */
    JButton reportsButton = new JButton("User Reports");

    JButton warningsButton = new JButton("Moderator Warnings");

    /** The topics list. */
    JList topicsList = new JList();

    /** The chat rooms table. */
    final JTable roomsTable = new JTable();

    /** The list model for the list of topics. */
    DefaultListModel listModel = new DefaultListModel();

    /** The button used for creating a chat room. */
    JButton createButton = new JButton("Create chat room");

    /** The list scroll pane. */
    JScrollPane listScrollPane = new JScrollPane(this.topicsList);

    /** The table scroll pane. */
    JScrollPane tableScrollPane = new JScrollPane(this.roomsTable);

    /** The predefined topics. */
    String[] topics = { "Informatics", "Mathematics" };

    /** The table model used by the table of chat rooms. */
    DefaultTableModel tableModel;

    /** The array list of chat rooms. */
    ArrayList<ChatRoomClass> chatRooms = new ArrayList<ChatRoomClass>();

    /** Column headers for the table */
    String[] columnHeaders = { "Room Name", "Users", "Description", "Votes", "Topic" };

    /** The button used to refresh the list of chat rooms. */
    JButton refreshButton = new JButton("Refresh List");

    /** The array list of topic classes. */
    ArrayList<TopicClass> topicsClasses = new ArrayList<TopicClass>();

    UserClass user;

    Socket socket;

    ObjectOutputStream out;

    ObjectInputStream in;

    /**
     * Instantiates a new student class.
     */
    public StudentGUI(Socket socketIn, ObjectInputStream inIn, ObjectOutputStream outIn,
            final UserClass user) {
        this.socket = socketIn;
        this.out = outIn;
        this.in = inIn;
        this.user = user;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setLayout();
        setVisible(true);
    }

    /**
     * Sets the layout for the student GUI.
     */
    public void setLayout() {
        setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 20));

        final JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        searchButton.addActionListener(new SearchButtonListener());

        final JPanel northEastPanel = new JPanel(new FlowLayout());
        settingsButton.addActionListener(new SettingsButtonListener());
        northEastPanel.add(settingsButton);
        signoutButton.addActionListener(new SignoutButtonListener());
        northEastPanel.add(signoutButton);

        final JPanel northPanel = new JPanel(new BorderLayout(0, 20));
        northPanel.add(searchPanel, BorderLayout.CENTER);
        northPanel.add(northEastPanel, BorderLayout.EAST);

        // The following (~20) lines set up the table containing the chat rooms

        final JPanel topicRoomsPanel = new JPanel(new BorderLayout(15, 0));
        topicRoomsPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        roomsTable.setDefaultRenderer(Object.class, centerRenderer);
        roomsTable.setShowVerticalLines(false);
        roomsTable.setModel(new DefaultTableModel(columnHeaders, 0) {
            @Override
            public boolean isCellEditable(final int row, final int column) {
                return false;
            }
        });

        roomsTable.getColumnModel().getColumn(4).setMinWidth(0);
        roomsTable.getColumnModel().getColumn(4).setMaxWidth(0);

        roomsTable.addMouseListener(new TableMouseListener());
        tableModel = (DefaultTableModel) roomsTable.getModel();

        roomsTable.setRowHeight(20);
        final TitledBorder tableTitle = new TitledBorder("Chat Rooms");
        tableTitle.setTitleJustification(TitledBorder.CENTER);
        tableScrollPane.setBorder(tableTitle);
        topicRoomsPanel.add(tableScrollPane);

        /*
         * Adds all the topics to the model of the JList holding them and adds the topic to the
         * topics ArrayList. The lines that follow set up the list of topics.
         */

        for (int i = 0; i < topics.length; i++) {
            listModel.addElement(topics[i]);
            topicsClasses.add(new TopicClass(topics[i]));
        }
        topicsList.setModel(listModel);
        final TitledBorder listTitle = new TitledBorder("Topics List");
        listTitle.setTitleJustification(TitledBorder.CENTER);
        topicsList.addListSelectionListener(new TopicsMouseSelectionListener());
        topicsList.setLayoutOrientation(JList.VERTICAL);
        topicsList.setFixedCellWidth(100);
        topicsList.setBorder(listTitle);
        topicRoomsPanel.add(listScrollPane, BorderLayout.WEST);

        final JPanel southPanel = new JPanel(new FlowLayout());
        createButton.addActionListener(new CreateButtonListener(this));

        if (user.isAdmin()) {
            reportsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new ModeratorReports(socket, out, in, user);
                }
            });
            southPanel.add(reportsButton);
        }
        else {
            warningsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new UserWarnings(user.getID(), socket, out, in);
                }
            });
            southPanel.add(warningsButton);
        }
        southPanel.add(createButton);
        refreshButton.addActionListener(new RefreshButtonListener(this));
        southPanel.add(refreshButton);

        add(northPanel, BorderLayout.NORTH);
        add(topicRoomsPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        getTopicsFromServer();
    }

    /**
     * Adds the new chatroom to the chatroom ArrayList
     * @param ChatRoomClass
     * @see src.CreateChatDialog
     */
    public void addChatToList(final ChatRoomClass chat) {
        this.chatRooms.add(chat);
    }

    public void setTopicsArrayList(ArrayList<TopicClass> topics) {
        this.topicsClasses = topics;
    }

    public void setTopicsJListModel(DefaultListModel topicsModel) {
        topicsList.setModel(topicsModel);
    }

    public void setChatRoomsList(ArrayList<ChatRoomClass> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public void sendTopicsToServer(ArrayList<TopicClass> topics, DefaultListModel topicsListModel) {
        try {
            out.writeObject(new MessageClass("send_topics", "whatever"));
            out.writeObject(topics);
            out.writeObject(topicsListModel);
            out.writeObject(chatRooms);
            getTopicsFromServer();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getTopicsFromServer() {
        try {
            int index = topicsList.getSelectedIndex();
            out.writeObject(new MessageClass("get_topics", "whatever"));
            setTopicsArrayList((ArrayList<TopicClass>) in.readObject());
            setTopicsJListModel((DefaultListModel) in.readObject());
            setChatRoomsList((ArrayList<ChatRoomClass>) in.readObject());
            topicsList.setSelectedIndex(index);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public class SignoutButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.gc();
            for (Window window : Window.getWindows()) {
                window.dispose();
            }
            new LoginGUI();
        }
    }

    /**
     * Search button listener class Takes user input from searchField JTextField and finds
     * corresponding chatrooms. Results are stored within an ArrayList and stored in the JTable
     * roomsTable.
     * @author Roger
     */
    public class SearchButtonListener implements ActionListener {

        /**
         * getResults methods returns chatrooms that correspond to search query
         * @returns results ArrayList object
         */
        ArrayList<ChatRoomClass> getResults(final String toSearch) {
            final ArrayList<ChatRoomClass> results = new ArrayList<ChatRoomClass>();

            for (final ChatRoomClass chatRoom : chatRooms) {
                if (naiveSearch(chatRoom.toString().toLowerCase(), toSearch)
                        || naiveSearch(chatRoom.getDesc().toLowerCase(), toSearch)) {
                    results.add(chatRoom);
                }
            }
            return results;
        }

        /**
         * NaiveSearch algorithm, used to search for appropriate chat rooms
         * @returns true or false value, dependent on whether match is found
         */
        boolean naiveSearch(final String text, final String pattern) {
            final int n = text.length();
            final int m = pattern.length();

            for (int s = 0; s <= (n - m); s++) {
                final String textPart = text.substring(s, (s + m));
                if (pattern.equals(textPart)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Method to add search results to the table
         */
        void appendTable(final ArrayList<ChatRoomClass> results) {
            final DefaultTableModel tblResults = (new DefaultTableModel(columnHeaders, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            });

            for (final ChatRoomClass cr : results) {
                tblResults.addRow(cr.toArray());
            }
            topicsList.clearSelection();
            roomsTable.setModel(tblResults);

            roomsTable.getColumnModel().getColumn(4).setMinWidth(0);
            roomsTable.getColumnModel().getColumn(4).setMaxWidth(0);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (!searchField.getText().equals("")) {
                final ArrayList<ChatRoomClass> results = getResults(searchField.getText()
                        .toLowerCase());
                if (results.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No chatrooms found :-(");
                }
                else {
                    this.appendTable(results);
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "Please enter a search query");
            }

        }

    }

    /**
     * The listener interface for receiving events in the settings button. Opens up a new frame
     * containing the user settings when the button is pressed.
     * @see src.SettingsClass
     */
    public class SettingsButtonListener implements ActionListener {

        /** The settings frame. */
        SettingsClass settingsFrame;

        @Override
        public void actionPerformed(final ActionEvent e) {
            this.settingsFrame = new SettingsClass(socket, in, out, user);
        }
    }

    /**
     * The listener interface for receiving events in the chat rooms table. It gets the table row
     * that is selected, using this to get the chat room object from the topics class and using this
     * to open the chat GUI.
     * @see src.TopicClass
     * @see src.ChatRoomClass
     */
    public class TableMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(final MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                int rowIndex = roomsTable.getSelectedRow();
                String topicString = (String) roomsTable.getModel().getValueAt(rowIndex, 4);
                for (TopicClass topic : topicsClasses) {
                    if (topic.toString().equals(topicString)) {
                        for (ChatRoomClass chatRoom : topic.getChatRooms()) {
                            if (chatRoom.name.equals(roomsTable.getModel().getValueAt(rowIndex, 0))) {
                                new ChatGUI(chatRoom, user);
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            else if (SwingUtilities.isRightMouseButton(e)) {
                int rowNumber = roomsTable.rowAtPoint(e.getPoint());
                ListSelectionModel chatLSModel = roomsTable.getSelectionModel();
                chatLSModel.setSelectionInterval(rowNumber, rowNumber);
                if (chatLSModel.isSelectionEmpty() == false) {
                    RightClickMenu menu = new RightClickMenu(chatLSModel);
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }

        /**
         * class with the constructor that creates menu on right mouse click.
         */
        class RightClickMenu extends JPopupMenu implements ActionListener {
            JMenuItem reportItem;
            JMenuItem deleteRoom;
            JMenuItem upvoteItem;
            JMenuItem downVoteItem;

            int row = roomsTable.getSelectedRow();
            DefaultTableModel tblMod = (DefaultTableModel) roomsTable.getModel();

            public RightClickMenu(final ListSelectionModel chatLSModel) {
                if (!user.isAdmin()) {
                    reportItem = new JMenuItem("Report");
                    add(reportItem);
                    reportItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new UserReport("chat room", chatRooms.get(
                                    chatLSModel.getMinSelectionIndex()).toString(), user.getName(),
                                    socket, out, in);
                        }
                    });
                }

                if (user.isAdmin()) {
                    deleteRoom = new JMenuItem("Delete");
                    add(deleteRoom);
                    deleteRoom.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            tblMod.removeRow(row);
                            roomsTable.setModel(tblMod);
                            sendTopicsToServer(topicsClasses, listModel);
                            getTopicsFromServer();
                        }
                    });
                }

                upvoteItem = new JMenuItem("Upvote");
                add(upvoteItem);
                upvoteItem.addActionListener(this);
                downVoteItem = new JMenuItem("Downvote");
                add(downVoteItem);
                downVoteItem.addActionListener(this);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                int rowIndex = roomsTable.getSelectedRow();
                String topicString = (String) roomsTable.getModel().getValueAt(rowIndex, 4);
                for (TopicClass topic : topicsClasses) {
                    if (topic.toString().equals(topicString)) {
                        for (ChatRoomClass chatRoom : topic.getChatRooms()) {
                            if (chatRoom.name.equals(roomsTable.getModel().getValueAt(rowIndex, 0))) {
                                if (e.getSource().equals(downVoteItem))
                                    chatRoom.votes--;
                                if (e.getSource().equals(upvoteItem))
                                    chatRoom.votes++;
                                topic.tableModel.setValueAt(chatRoom.votes, rowIndex, 3);
                                break;
                            }
                        }
                        break;
                    }
                }
                sendTopicsToServer(topicsClasses, listModel);
            }
        }
    }

    /**
     * The listener used for receiving events in the button used to create chat rooms. Opens up a
     * new window asking for details of the new chat room.
     */
    public class CreateButtonListener implements ActionListener {

        /** The dialog used to create a chat room. */
        CreateChatDialog createChatRoom;

        /** The student GUI frame containing the button. */
        StudentGUI studentFrame;

        /**
         * Instantiates the button listener.
         * @param studentFrame
         *        the student frame
         */
        public CreateButtonListener(final JFrame studentFrame) {
            this.studentFrame = (StudentGUI) studentFrame;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            this.createChatRoom = new CreateChatDialog(this.studentFrame);
        }
    }

    /**
     * The listener used for receiving events from the topics list. Listens for a selection in the
     * topics list: if it detects an event, it will update the rooms table with all the chat rooms
     * from the selected topic.
     * @see javax.swing.event.ListSelectionEvent
     */
    public class TopicsMouseSelectionListener implements ListSelectionListener {

        public TopicsMouseSelectionListener() {
        }

        @Override
        public void valueChanged(final ListSelectionEvent e) {
            final String topicString = (String) topicsList.getSelectedValue();
            for (final TopicClass topic : topicsClasses) {
                if (topic.topicName.equals(topicString)) {
                    roomsTable.setModel(topic.getTableModel());
                    roomsTable.getColumnModel().getColumn(4).setMinWidth(0);
                    roomsTable.getColumnModel().getColumn(4).setMaxWidth(0);

                }
            }
        }
    }

    public class RefreshButtonListener implements ActionListener {

        StudentGUI ui;

        public RefreshButtonListener(StudentGUI ui) {
            this.ui = ui;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ui.getTopicsFromServer();
        }
    }
}
