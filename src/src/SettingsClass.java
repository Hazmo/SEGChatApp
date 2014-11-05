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

/**
 * The class used as a frame for the settings that the user can access.
 */
public class SettingsClass extends JFrame implements ActionListener {

    JTextField nameChangeField;
    JTextField passChangeField;
    JTextField confirmPassField;
    JTextField questionField;
    JTextField answerField;

    UserClass user;

    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

    /**
     * Instantiates a new settings class and initiates all the GUI widgets / components.
     */
    public SettingsClass(final UserClass user) {

        try {
            socket = new Socket("localhost", 4456);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        this.user = user;
        setLayout(new BorderLayout(15, 0));

        final JLabel nameChangeLabel = new JLabel("Change name: ");
        final JLabel passChangeLabel = new JLabel("Change password: ");
        final JLabel confirmPassLabel = new JLabel("Confirm new password: ");
        final JLabel questionLabel = new JLabel("Change secret question: ");
        final JLabel answerLabel = new JLabel("Secret answer: ");

        nameChangeField = new JTextField(20);
        passChangeField = new JTextField(20);
        confirmPassField = new JTextField(20);
        questionField = new JTextField(20);
        answerField = new JTextField(20);

        final JPanel center = new JPanel(new GridLayout(5, 2));
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

        setVisible(true);
        pack();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if ((e.getActionCommand()).equals("Cancel")) {
            try {
                socket.close();
                in.close();
                out.close();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
            dispose();
        }
        else {
            if (e.getActionCommand().equals("Ok")) {
                int confirmation = 0;
                JTextField[] jFields = { nameChangeField, passChangeField, confirmPassField,
                        questionField, answerField };

                try {
                    System.out.println(jFields[0].getText());
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
                }

                this.dispose();
                try {
                    socket.close();
                    in.close();
                    out.close();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
