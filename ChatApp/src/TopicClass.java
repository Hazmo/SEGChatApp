/**
 *  @author Codrin Gidei - 1326651
 *  @email codrin.gidei@kcl.ac.uk
 */
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

/**
 * The class used to store data related to a topic.
 */
public class TopicClass {

    /** The topic name. */
    String topicName;

    /** The ArrayList of chat rooms. */
    ArrayList<ChatRoomClass> chatRooms;

    /** The table model. */
    DefaultTableModel tableModel;

    /**
     * Instantiates a new topic class.
     * @param topicName
     *        the topic name
     */
    public TopicClass(String topicName) {
        chatRooms = new ArrayList<ChatRoomClass>();
        this.topicName = topicName;
        String[] columnHeaders = { "Room Name", "Users", "Description" };
        tableModel = new DefaultTableModel(columnHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    /**
     * Adds the chat room to the ArrayList of chat rooms.
     * @param chatRoom
     *        the chat room
     */
    public void addChatRoom(ChatRoomClass chatRoom) {
        chatRooms.add(chatRoom);
    }

    /**
     * Adds a new chat room as a new row to the table model.
     * @param chatRoom
     *        the chat room
     */
    public void addRow(ChatRoomClass chatRoom) {
        tableModel.addRow(chatRoom.toArray());
    }

    /**
     * Gets the chat rooms array list.
     * @return the chat rooms ArrayList
     */
    public ArrayList<ChatRoomClass> getChatRooms() {
        return chatRooms;
    }

    public String toString() {
        return topicName;
    }

    /**
     * Gets the table model containing the chatRooms.
     * @return the table model
     */
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
