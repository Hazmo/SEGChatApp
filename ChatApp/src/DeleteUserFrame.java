package src;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

public class DeleteUserFrame extends JFrame implements ActionListener {

    JTextField userDeleteField;
    UserClass user;
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

    /**
     * Instantiates a new settings class and initiates all the GUI widgets / components.
     */
    public DeleteUserFrame(Socket socket, ObjectInputStream in, ObjectOutputStream out,
            UserClass user) {

        this.user = user;
        this.socket = socket;
        this.in = in;
        this.out = out;

        setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 0));
        final JLabel userDeleteLabel = new JLabel("Delete user with ID : ");

        userDeleteField = new JTextField(20);

        final JPanel center;

        center = new JPanel(new GridLayout(1, 2));

        center.add(userDeleteLabel);
        center.add(userDeleteField);

        final JButton okButton = new JButton("Ok");
        okButton.setActionCommand("Ok");
        okButton.addActionListener(this);
        final JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);

        final JPanel south = new JPanel(new FlowLayout());
        south.add(okButton);
        south.add(cancelButton);
        SwingUtilities.getRootPane(okButton).setDefaultButton(okButton);

        add(center);
        add(south, BorderLayout.SOUTH);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                /**
                 * try { socket.close(); in.close(); out.close(); } catch (Exception ex) {
                 * ex.printStackTrace(); }
                 **/
            }

        });

        setVisible(true);
        setLocationRelativeTo(null);
        pack();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if ((e.getActionCommand()).equals("Cancel")) {
            /*
             * try { socket.close(); in.close(); out.close(); } catch (IOException e1) {
             * e1.printStackTrace(); }
             */
            dispose();
        }
        else {
            if (e.getActionCommand().equals("Ok")) {
                int confirmation = 0;

                try {
                    if (userDeleteField.getText().equals(user.getID())) {
                        confirmation = 5;
                    }
                    else {

                        out.writeObject(new MessageClass("delete_user", userDeleteField.getText()));
                        confirmation = (int) in.readObject();
                    }
                }
                catch (Exception e1) {
                    e1.printStackTrace();
                    confirmation = 3;
                }

                switch (confirmation) {
                case (1): {
                    JOptionPane.showMessageDialog(this, "Information updated!");
                    this.dispose();
                    break;
                }

                case (2): {
                    JOptionPane.showMessageDialog(this,
                            "Update failed! Problem with the user database!");
                    this.dispose();
                    break;
                }

                case (3): {
                    JOptionPane.showMessageDialog(this,
                            "Update failed, problem with connection to server!");
                    this.dispose();
                    break;
                }
                case (4): {
                    JOptionPane.showMessageDialog(this, "User with " + userDeleteField.getText()
                            + " ID has not been found!");
                    break;
                }
                case (5): {
                    JOptionPane.showMessageDialog(this, "You cannot delete your own account!");
                    break;
                }
                }

                this.dispose();

            }
        }
    }
}
