package src;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class UserWarnings extends JFrame {
    private JList warningsList = new JList();
    // private JTextArea warningDesc = new JTextArea();
    private JButton backButton = new JButton("Back");
    private WarningClass warning;
    String userID;
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

    UserWarnings(String userID, Socket socket, ObjectOutputStream out, ObjectInputStream in) {
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.userID = userID;
        initGUI();
    }

    void initGUI() {
        JPanel centrePanel = new JPanel(new BorderLayout());
        // warningsList.addListSelectionListener(new ListSelectionListener() {
        //
        // @Override
        // public void valueChanged(ListSelectionEvent arg0) {
        // if (!warningsList.isSelectionEmpty()) {
        // System.out.println("DISPLAYING WARNING");
        // warningDesc.setText(warning.getWarningMessage());
        // }
        // }
        //
        // });
        JScrollPane wrnPane = new JScrollPane(warningsList);
        // JScrollPane textPane = new JScrollPane(warningDesc);
        // warningDesc.setEditable(false);
        centrePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        centrePanel.add(wrnPane, BorderLayout.CENTER);
        // centrePanel.add(textPane, BorderLayout.SOUTH);
        add(centrePanel, BorderLayout.CENTER);

        try {
            out.writeObject(new MessageClass("get_warning", userID));
            warning = (WarningClass) in.readObject();
        }
        catch (Exception e) {
            if (e.getClass().equals(IOException.class)) {
                System.out.println("Accept failed: 4457");
                System.exit(-1);
            }
            else if (e.getClass().equals(ClassNotFoundException.class))
                e.printStackTrace();
        }

        setWarning();

        JPanel bottomPane = new JPanel(new FlowLayout());
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        bottomPane.add(backButton);

        add(bottomPane, BorderLayout.SOUTH);
        setTitle("Moderator Warnings");
        setResizable(false);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void setWarning() {
        DefaultListModel<String> warnsModel = new DefaultListModel<String>();
        if (!(warning == null))
            warnsModel.addElement(warning.getListDesc());
        warningsList.setModel(warnsModel);
    }
}
