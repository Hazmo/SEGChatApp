package src;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class WarnFrame extends JFrame implements ActionListener {

    JTextField warnField;
    JTextField idField;
    private JButton checkWarnsButton = new JButton("Check User Warnings");

    Socket socket;
    public ObjectOutputStream out;
    ObjectInputStream in;
    UserClass user;

    public WarnFrame(Socket socket, ObjectInputStream in, ObjectOutputStream out, UserClass user) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.user = user;

        setTitle("Warn User");
        setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 0));

        final JLabel IDLabel = new JLabel("ID of user to warn: ");
        final JLabel warnLabel = new JLabel("Reason for warn: ");

        warnField = new JTextField(20);
        idField = new JTextField(20);

        final JPanel center = new JPanel(new GridLayout(2, 2));

        center.add(IDLabel);
        center.add(idField);
        center.add(warnLabel);
        center.add(warnField);

        final JPanel south = new JPanel(new FlowLayout());
        final JButton sendButton = new JButton("Send Warning");
        sendButton.addActionListener(this);
        sendButton.setActionCommand("send");
        checkWarnsButton.addActionListener(this);
        checkWarnsButton.setActionCommand("check");
        south.add(sendButton);
        south.add(checkWarnsButton);

        add(center);
        add(south, BorderLayout.SOUTH);

        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("send")) {
            if (idField.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "You did not specify the user's ID!");
            }
            else if (warnField.getText().equals("")) {
                JOptionPane
                        .showMessageDialog(this, "You did not specify a reason for the warning!");
            }
            else {
                int confirmation = 0;
                JTextField[] jFields = { idField, warnField };
                try {
                    out.writeObject(new MessageClass("add_warning", idField.getText()));
                    switch ((int) in.readObject()) {
                    case (1): {
                        JOptionPane.showMessageDialog(this, "User with ID " + idField.getText()
                                + " has not beed found!");
                        break;
                    }
                    case (2): {
                        out.writeObject(new WarningClass(user.getName(), idField.getText(),
                                warnField.getText()));
                        String messageDialogString = (String) in.readObject();
                        JOptionPane.showMessageDialog(this, messageDialogString);
                        dispose();
                        break;
                    }
                    case (3): {
                        JOptionPane.showMessageDialog(this, "Sending warning failed!");
                        break;
                    }
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        else if (e.getActionCommand().equals("check")) {
            if (idField.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "You did not specify the user's ID!");
            }
            else {
                try {
                    out.writeObject(new MessageClass("get_nr_warnings", idField.getText()));
                    boolean confirmation = (boolean) in.readObject();
                    if (confirmation) {
                        int nr = Integer.parseInt((String) in.readObject());
                        JOptionPane.showMessageDialog(this, "User with ID " + idField.getText()
                                + " has " + nr + " warnings.");
                    }
                    else {
                        JOptionPane.showMessageDialog(this, "User with ID " + idField.getText()
                                + " has not beed found!");
                    }

                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
                catch (ClassNotFoundException e2) {
                    e2.printStackTrace();
                }
            }

        }
    }

}
