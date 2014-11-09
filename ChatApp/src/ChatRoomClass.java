package src;

import javax.swing.*;
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

    DefaultListModel chatModel;

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
        chatModel = new DefaultListModel();
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

    public void addMessageToHistory(String message) {
        chatModel.addElement(message);
    }

    public DefaultListModel getChatModel() {
        return chatModel;
    }

    /**
     * Converts and returns the chat room information as an array of strings.
     * @return the chat room information as an array of strings
     */
    public String[] toArray() {
        String[] chatRoomInfo = { name, "" + connections, description, "" + votes, topicName };

        return chatRoomInfo;
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("actually entering");
        if(obj == null) {
            System.out.println("here");
            return false;
        }
        if(getClass() != obj.getClass()) {
            System.out.println("here 2");
            return false;
        }
        final ChatRoomClass other = (ChatRoomClass) obj;
        if(!this.name.equals(other.name) && !this.topicName.equals(other.topicName)) {
            System.out.println(this.name + " " + other.name + "     " + this.topicName + " " + other.topicName);
            System.out.println("here 3");
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 53 * hash + this.topicName.hashCode();
        return hash;
    }
}
