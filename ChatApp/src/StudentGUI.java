/**
 *  @author Codrin Gidei - 1326651
 *  @email codrin.gidei@kcl.ac.uk
 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

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

    /** The topics list. */
    JList topicsList = new JList();

    /** The chat rooms table. */
    JTable roomsTable = new JTable();

    /** The list model for the list of topics. */
    DefaultListModel listModel = new DefaultListModel();

    /** The button used for creating a chat room. */
    JButton createButton = new JButton("Create chat room");

    /** The list scroll pane. */
    JScrollPane listScrollPane = new JScrollPane(topicsList);

    /** The table scroll pane. */
    JScrollPane tableScrollPane = new JScrollPane(roomsTable);

    /** The predefined topics. */
    String[] topics = { "Informatics", "Mathematics" };

    /** The table model used by the table of chat rooms. */
    DefaultTableModel tableModel;

    /** The array list of chat rooms. */
    ArrayList<ChatRoomClass> chatRooms = new ArrayList<ChatRoomClass>();

    /** The button used to refresh the list of chat rooms. */
    JButton refreshButton = new JButton("Refresh List");

    /** The array list of topic classes. */
    ArrayList<TopicClass> topicsClasses = new ArrayList<TopicClass>();

    /**
     * Instantiates a new student class.
     */
    public StudentGUI() {

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
        this.setLayout(new BorderLayout(0, 20));

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        JPanel northEastPanel = new JPanel(new FlowLayout());
        settingsButton.addActionListener(new SettingsButtonListener());
        northEastPanel.add(settingsButton);
        northEastPanel.add(signoutButton);

        JPanel northPanel = new JPanel(new BorderLayout(0, 20));
        northPanel.add(searchPanel, BorderLayout.CENTER);
        northPanel.add(northEastPanel, BorderLayout.EAST);

        // The following (~20) lines set up the table containing the chat rooms

        JPanel topicRoomsPanel = new JPanel(new BorderLayout(15, 0));
        topicRoomsPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        String[] columnHeaders = { "Room Name", "Users", "Description" };
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        roomsTable.setDefaultRenderer(Object.class, centerRenderer);
        roomsTable.setShowVerticalLines(false);
        roomsTable.setModel(new DefaultTableModel(columnHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        roomsTable.addMouseListener(new TableMouseListener());
        tableModel = (DefaultTableModel) roomsTable.getModel();
        roomsTable.setRowHeight(20);
        TitledBorder tableTitle = new TitledBorder("Chat Rooms");
        tableTitle.setTitleJustification(tableTitle.CENTER);
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
        TitledBorder listTitle = new TitledBorder("Topics List");
        listTitle.setTitleJustification(listTitle.CENTER);
        topicsList.addListSelectionListener(new TopicsMouseSelectionListener());
        topicsList.setLayoutOrientation(JList.VERTICAL);
        topicsList.setFixedCellWidth(100);
        topicsList.setBorder(listTitle);
        topicRoomsPanel.add(listScrollPane, BorderLayout.WEST);

        JPanel southPanel = new JPanel(new FlowLayout());
        createButton.addActionListener(new CreateButtonListener(this));
        southPanel.add(createButton);
        southPanel.add(refreshButton);

        this.add(northPanel, BorderLayout.NORTH);
        this.add(topicRoomsPanel, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    /**
     * The listener interface for receiving events in the settings button. Opens up a new frame
     * containing the user settings when the button is pressed.
     * @see SettingsClass
     */
    public class SettingsButtonListener implements ActionListener {

        /** The settings frame. */
        SettingsClass settingsFrame;

        @Override
        public void actionPerformed(ActionEvent e) {
            settingsFrame = new SettingsClass();
        }
    }

    /**
     * The listener interface for receiving events in the chat rooms table. It gets the table row
     * that is selected, using this to get the chat room object from the topics class and using this
     * to open the chat GUI.
     * @see TopicClass
     * @see ChatRoomClass
     */
    public class TableMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int tableIndex = roomsTable.getSelectedRow();
                String topicString = (String) topicsList.getSelectedValue();
                for (TopicClass topic : topicsClasses) {
                    if (topic.toString().equals(topicString)) {
                        ChatRoomClass chatRoom = topic.getChatRooms().get(tableIndex);
                        new ChatGUI(chatRoom);
                    }
                }

            }
        }

        /**
         *
         * @param e
         */
        public void mousePressed(MouseEvent e){
            if (e.isPopupTrigger())
                doPop(e);
        }

        /**
         *
         * @param e
         */
        public void mouseReleased(MouseEvent e){
            if (e.isPopupTrigger())
                doPop(e);
        }

        /**
         * creates a menu and shows it at the place where the current mouse location is
         * @param e mouse event
         */
        private void doPop(MouseEvent e){
            RightClickMenu menu = new RightClickMenu();
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     * class with the constructor that creates menu on right mouse click.
     */
    class RightClickMenu extends JPopupMenu {
        JMenuItem reportItem;
        public RightClickMenu(){
            reportItem = new JMenuItem("Report");
            add(reportItem);
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
        public CreateButtonListener(JFrame studentFrame) {
            this.studentFrame = (StudentGUI) studentFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            createChatRoom = new CreateChatDialog(studentFrame);
        }
    }

    /**
     * The listener used for receiving events from the topics list. Listens for a selection in the
     * topics list: if it detects an event, it will update the rooms table with all the chat rooms
     * from the selected topic.
     * @see ListSelectionEvent
     */
    public class TopicsMouseSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            String topicString = (String) topicsList.getSelectedValue();
            for (TopicClass topic : topicsClasses) {
                if (topic.topicName.equals(topicString)) {
                    roomsTable.setModel(topic.getTableModel());
                }
            }
        }
    }
}
