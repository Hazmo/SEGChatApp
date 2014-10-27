/**
 *  @author Codrin Gidei - 1326651
 *  @email codrin.gidei@kcl.ac.uk
 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * The class / frame used to create a new chat room.
 */
public class CreateChatDialog extends JFrame {

    /** The chat room label. */
    JLabel chatRoomLabel = new JLabel("Chat Room Name:");

    /** The field used to give the chat room a name. */
    JTextField chatRoomField = new JTextField(20);

    /** The description label. */
    JLabel descriptionLabel = new JLabel("Description:");

    /** The JTextArea used to give the chat room a description. */
    JTextArea descriptionArea = new JTextArea();

    /** The ok button. */
    JButton okButton = new JButton("OK");

    /** The cancel button. */
    JButton cancelButton = new JButton("Cancel");

    /** The frame containing the student GUI. */
    StudentGUI studentFrame;

    /** The ComboBox containing the topics. */
    JComboBox topicsComboBox = new JComboBox();

    /** The categories label. */
    JLabel categoriesLabel = new JLabel("Select Category: ");

    /** The model used by the topics ComboBox */
    DefaultComboBoxModel topicsModel;

    /**
     * Instantiates the CreateChatDialog class
     * @param studentFrame
     *        the frame containing the student GUI
     */
    public CreateChatDialog(StudentGUI studentFrame) {
        this.studentFrame = studentFrame;
        setLocationRelativeTo(null);
        setLayout();
        setVisible(true);
        pack();
    }

    /**
     * Sets the layout along with all the widgets / components in the dialog.
     */
    public void setLayout() {
        JPanel center = new JPanel(new GridLayout(4, 4));

        String[] topics = { "Informatics", "Mathematics" };
        topicsModel = new DefaultComboBoxModel(topics);
        topicsComboBox.setModel(topicsModel);

        center.add(categoriesLabel);
        center.add(topicsComboBox);
        center.add(chatRoomLabel);
        center.add(chatRoomField);
        center.add(descriptionLabel);
        center.add(descriptionArea);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        okButton.addActionListener(new OkButtonListener(studentFrame, this));
        south.add(okButton);
        cancelButton.addActionListener(new CancelButtonListener(this));
        south.add(cancelButton);

        this.add(center);
        this.add(south, BorderLayout.SOUTH);
    }

    /**
     * The listener used to detect events in the OK button. When the button is pressed, the data
     * entered in the fields is used to create a new chat room class, which, in turn, is added to
     * its respective topic.
     * @see TopicClass
     * @see ChatRoomClass
     */
    public class OkButtonListener implements ActionListener {

        /** The parent frame containing the button. */
        JFrame parent;

        /** The frame containing the student GUI. */
        StudentGUI studentFrame;

        /**
         * Instantiates a new listener for the OK button.
         * @param studentFrame
         *        the frame containing the student GUI
         * @param parent
         *        the frame containing the button
         */
        public OkButtonListener(StudentGUI studentFrame, JFrame parent) {
            this.parent = parent;
            this.studentFrame = studentFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String topicName = (String) topicsComboBox.getSelectedItem();
            String chatRoomName = chatRoomField.getText();
            String chatRoomDescription = descriptionArea.getText();
            String[] rowData = { chatRoomName, "0", chatRoomDescription };

            ChatRoomClass chatRoom = new ChatRoomClass(chatRoomName, chatRoomDescription);

            for (TopicClass topic : studentFrame.topicsClasses) {
                if (topic.toString().equals(topicName)) {
                    topic.addChatRoom(chatRoom);
                    topic.addRow(chatRoom);
                }
            }

            this.parent.dispose();

        }
    }

    /**
     * The listener used for the cancel button. When pressed, the dialog frame is closed without
     * creating a new chat room.
     */
    public class CancelButtonListener implements ActionListener {

        /** The parent frame, containing the cancel button. */
        JFrame parent;

        /**
         * Instantiates the listener.
         * @param parent
         *        the parent frame, containing the cancel button.
         */
        public CancelButtonListener(JFrame parent) {
            this.parent = parent;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            this.parent.dispose();
        }
    }
}
