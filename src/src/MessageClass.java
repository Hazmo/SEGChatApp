package src;

/**
 *  @author Codrin Gidei - 1326651
 *  @email codrin.gidei@kcl.ac.uk
 */
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * The class used to store data related to a message in a chat room.
 */
public class MessageClass implements Serializable {

    /** The message itself. */
    String message;

    /** The chat room in which the message was sent. */
    ChatRoomClass chatRoom;

    /** The date and time at which the message was sent. */
    Date date;

    Timestamp timestamp;

    /**
     * Instantiates a new message class.
     * @param message
     *        the message itself
     * @param chatRoom
     *        the chat room in which the message was sent
     */
    public MessageClass(String message, ChatRoomClass chatRoom) {
        this.message = message;
        this.chatRoom = chatRoom;
        date = new Date();
        timestamp = new Timestamp(date.getTime());

    }

    /**
     * Gets the message.
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the room name.
     * @return the room name
     */
    public String getRoomName() {
        return chatRoom.toString();
    }

    /**
     * Gets the time stamp.
     * @return the time stamp
     */
    public String getTimestamp() {
        return date.toString();
    }
}
