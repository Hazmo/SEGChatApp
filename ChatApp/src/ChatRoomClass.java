/**
 * The class used to store data related to a chat room class.
 */
public class ChatRoomClass {

    /** The number of current connections made by clients to the chat room. */
    int connections;

    /** The name of the chat room. */
    String name;

    /** The description of the chat room. */
    String description;

    /**
     * Instantiates a new chat room class.
     * @param name
     *        the name of the chat room
     * @param description
     *        the description of the chat room
     */
    public ChatRoomClass(String name, String description) {
        this.name = name;
        this.description = description;
        connections = 0;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Converts and returns the chat room information as an array of strings.
     * @return the chat room information as an array of strings
     */
    public String[] toArray() {
        String[] chatRoomInfo = { name, "" + connections, description };
        return chatRoomInfo;
    }
}
