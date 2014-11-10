package src;

/**
 *  @author Codrin Gidei - 1326651
 *  @email codrin.gidei@kcl.ac.uk
 */
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

/**
 * The class used as a frame for the settings that the user can access.
 */
public class SettingsClass extends JFrame implements ActionListener {

    JTextField nameChangeField;
    JTextField passChangeField;
    JTextField confirmPassField;
    JTextField questionField;
    JTextField answerField;
    JTextField modAppointField;
    JTextField modDismissField;

    UserClass user;

    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

    /**
     * Instantiates a new settings class and initiates all the GUI widgets / components.
     */
    public SettingsClass(Socket socket, ObjectInputStream in, ObjectOutputStream out,
            final UserClass user) {

        this.socket = socket;
        this.in = in;
        this.out = out;

        setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
        this.user = user;
        setLayout(new BorderLayout(15, 0));

        final JLabel nameChangeLabel = new JLabel("Change name: ");
        final JLabel passChangeLabel = new JLabel("Change password: ");
        final JLabel confirmPassLabel = new JLabel("Confirm new password: ");
        final JLabel questionLabel = new JLabel("Change secret question: ");
        final JLabel answerLabel = new JLabel("Change secret answer: ");
        final JLabel modAppointLabel = new JLabel("Appoint new moderator: ");
        final JLabel modDismissLabel = new JLabel("Dismiss moderator: ");

        nameChangeField = new JTextField(20);
        passChangeField = new JTextField(20);
        confirmPassField = new JTextField(20);
        questionField = new JTextField(20);
        answerField = new JTextField(20);
        modAppointField = new JTextField(20);
        modDismissField = new JTextField(20);

        final JPanel center;
        if (!user.isAdmin())
            center = new JPanel(new GridLayout(5, 2));
        else
            center = new JPanel(new GridLayout(7, 2));

        center.add(nameChangeLabel);
        center.add(nameChangeField);
        center.add(passChangeLabel);
        center.add(passChangeField);
        center.add(confirmPassLabel);
        center.add(confirmPassField);
        center.add(questionLabel);
        center.add(questionField);
        center.add(answerLabel);
        center.add(answerField);

        if (user.isAdmin()) {
            center.add(modAppointLabel);
            center.add(modAppointField);
            center.add(modDismissLabel);
            center.add(modDismissField);

        }

        final JButton okButton = new JButton("Ok");
        okButton.setActionCommand("Ok");
        okButton.addActionListener(this);
        final JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);

        final JPanel south = new JPanel(new FlowLayout());
        south.add(okButton);
        south.add(cancelButton);

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

        SwingUtilities.getRootPane(okButton).setDefaultButton(okButton);

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
                JTextField[] jFields = { nameChangeField, passChangeField, confirmPassField,
                        questionField, answerField, modAppointField, modDismissField };

                try {
                    System.out.println(jFields[0].getText());
                    out.writeObject(new MessageClass("update_settings", ""));
                    out.writeObject(jFields);
                    out.writeObject(user);
                    confirmation = (int) in.readObject();
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
                    JOptionPane.showMessageDialog(this, "The password fields don't match!");
                    this.dispose();
                    break;
                }
                case (5): {
                    JOptionPane.showMessageDialog(this,
                            "You cannot modify your own moderator status!");
                    this.dispose();
                    break;
                }
                case (6): {
                    JOptionPane.showMessageDialog(this, "User with " + modAppointField.getText()
                            + " ID has not been found!");
                    break;
                }
                case (7): {
                    JOptionPane.showMessageDialog(this, "User with " + modDismissField.getText()
                            + " ID has not been found!");
                    break;
                }
                }

                this.dispose();

                /**
                 * try { socket.close(); in.close(); out.close(); } catch (IOException e1) {
                 * e1.printStackTrace(); }
                 **/
            }
        }
    }

    public void setConnections(Socket socketIn, ObjectOutputStream outIn, ObjectInputStream inIn) {
        socket = socketIn;
        in = inIn;
        out = outIn;

    }
}
