package src;

import java.io.Serializable;

/**
 * The class used to store data related to a chat room class.
 */
public class ChatRoomClass implements Serializable {

    /** The number of current connections made by clients to the chat room. */
    int connections;

    /** The name of the chat room. */
    String name;

    /** The description of the chat room. */
    String description;

    String topicName;

    int votes;

    /**
     * Instantiates a new chat room class.
     * @param name
     *        the name of the chat room
     * @param description
     *        the description of the chat room
     */
    public ChatRoomClass(String topicName, String name, String description) {
        this.topicName = topicName;
        this.name = name;
        this.description = description;
        connections = 0;
        votes = 0;
    }

    /**
     * getDesc method to return description of chat room
     * @return description
     */
    public String getDesc() {
        return description;
    }

    public String getTopic() {
        return topicName;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getVotes() {
        return votes;
    }

    /**
     * Converts and returns the chat room information as an array of strings.
     * @return the chat room information as an array of strings
     */
    public String[] toArray() {
        String[] chatRoomInfo = { name, "" + connections, description, "" + votes, topicName };

        return chatRoomInfo;
    }
}
