package src;

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

    /** The topics list. */
    JList topicsList = new JList();

    /** The chat rooms table. */
    JTable roomsTable = new JTable();

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
    String[] columnHeaders = { "Room Name", "Users", "Description" };

    /** The button used to refresh the list of chat rooms. */
    JButton refreshButton = new JButton("Refresh List");

    /** The array list of topic classes. */
    ArrayList<TopicClass> topicsClasses = new ArrayList<TopicClass>();

    UserClass user;

    /**
     * Instantiates a new student class.
     */
    public StudentGUI(final UserClass user) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setLayout();
        setVisible(true);
        this.user = user;
    }

    /**
     * Sets the layout for the student GUI.
     */
    public void setLayout() {
        setLayout(new BorderLayout(0, 20));

        final JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        searchButton.addActionListener(new SearchButtonListener());

        final JPanel northEastPanel = new JPanel(new FlowLayout());
        settingsButton.addActionListener(new SettingsButtonListener());
        northEastPanel.add(settingsButton);
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
        roomsTable.setModel(new DefaultTableModel(this.columnHeaders, 0) {
            @Override
            public boolean isCellEditable(final int row, final int column) {
                return false;
            }
        });
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
        southPanel.add(createButton);
        southPanel.add(refreshButton);

        add(northPanel, BorderLayout.NORTH);
        add(topicRoomsPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    /**
     * Adds the new chatroom to the chatroom ArrayList
     * @param ChatRoomClass
     * @see CreateChatDialog
     */
    public void addChatToList(final ChatRoomClass chat) {
        this.chatRooms.add(chat);
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

            for (final ChatRoomClass chatRoom : StudentGUI.this.chatRooms) {
                if (this.naiveSearch(chatRoom.toString().toLowerCase(), toSearch)
                        || this.naiveSearch(chatRoom.getDesc().toLowerCase(), toSearch)) {
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
                public boolean isCellEditable(final int row, final int column) {
                    return false;
                }
            });

            for (final ChatRoomClass cr : results) {
                tblResults.addRow(cr.toArray());
            }

            roomsTable.setModel(tblResults);
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (!StudentGUI.this.searchField.getText().equals("")) {
                final ArrayList<ChatRoomClass> results = this
                        .getResults(StudentGUI.this.searchField.getText().toLowerCase());
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
     * @see SettingsClass
     */
    public class SettingsButtonListener implements ActionListener {

        /** The settings frame. */
        SettingsClass settingsFrame;

        @Override
        public void actionPerformed(final ActionEvent e) {
            this.settingsFrame = new SettingsClass(StudentGUI.this.user);
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
        public void mouseClicked(final MouseEvent e) {
            if (e.getClickCount() == 2) {
                final int tableIndex = roomsTable.getSelectedRow();
                final String topicString = (String) topicsList.getSelectedValue();
                for (final TopicClass topic : StudentGUI.this.topicsClasses) {
                    if (topic.toString().equals(topicString)) {
                        final ChatRoomClass chatRoom = topic.getChatRooms().get(tableIndex);
                        new ChatGUI(chatRoom);
                    }
                }

            }
        }

        /**
         * @param e
         */
        @Override
        public void mousePressed(final MouseEvent e) {
            if (e.isPopupTrigger()) {
                this.doPop(e);
            }
        }

        /**
         * @param e
         */
        @Override
        public void mouseReleased(final MouseEvent e) {
            if (e.isPopupTrigger()) {
                this.doPop(e);
            }
        }

        /**
         * creates a menu and shows it at the place where the current mouse location is
         * @param e
         *        mouse event
         */
        private void doPop(final MouseEvent e) {
            final RightClickMenu menu = new RightClickMenu();
            menu.show(e.getComponent(), e.getX(), e.getY());
        }

        /**
         * class with the constructor that creates menu on right mouse click.
         */
        class RightClickMenu extends JPopupMenu {
            JMenuItem reportItem;

            public RightClickMenu() {
                this.reportItem = new JMenuItem("Report");
                this.add(this.reportItem);
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
     * @see ListSelectionEvent
     */
    public class TopicsMouseSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(final ListSelectionEvent e) {
            final String topicString = (String) StudentGUI.this.topicsList.getSelectedValue();
            for (final TopicClass topic : StudentGUI.this.topicsClasses) {
                if (topic.topicName.equals(topicString)) {
                    roomsTable.setModel(topic.getTableModel());
                }
            }
        }
    }
}
