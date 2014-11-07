package src;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginGUI extends JFrame {

    private JLabel studentLabel = new JLabel("Student ID: ");
    private JLabel passLabel = new JLabel("Password: ");
    public JTextField studentIDField = new JTextField(20);
    public JPasswordField passField = new JPasswordField(20);
    private JButton loginButton = new JButton("Log In");
    private JButton registerButton = new JButton("Register");
    private JLabel forgottenPassLabel = new JLabel("<html><b><u>Forgotten your password?"
            + "</u></b></html>");
    File csvFile = new File("users.csv");

    Socket loginSocket;
    ObjectOutputStream out;
    ObjectInputStream in;

    /**
     * Default constructor
     */
    public LoginGUI() {

        try {
            this.loginSocket = new Socket("localhost", 4455);
            this.out = new ObjectOutputStream(loginSocket.getOutputStream());
            this.in = new ObjectInputStream(loginSocket.getInputStream());
        } catch (IOException e) {
            e.getMessage();
        }
        setTitle("Log in");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout();
        setVisible(true);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        addListeners();
    }

    /** setting the layout of the frame with all the widgets */
    private void setLayout() {
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JPanel fieldsPanel1 = new JPanel(new GridLayout(2, 1, 10, 10));
        JPanel fieldsPanel2 = new JPanel(new GridLayout(2, 1, 10, 10));
        fieldsPanel1.add(studentLabel);
        fieldsPanel2.add(studentIDField);
        fieldsPanel1.add(passLabel);
        fieldsPanel2.add(passField);

        centerPanel.add(fieldsPanel1, BorderLayout.WEST);
        centerPanel.add(fieldsPanel2, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        buttonsPanel.add(forgottenPassLabel);
        buttonsPanel.add(loginButton);
        buttonsPanel.add(registerButton);

        centerPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(centerPanel);
    }

    private void addListeners() {

        // setting the listener for the login button
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    String idFieldText = studentIDField.getText();
                    String passFieldText = new String(passField.getPassword());

                    MessageClass message = new MessageClass("log_in", idFieldText, new Object[]{passFieldText});

                    out.writeObject(message);

                    //out.println(studentIDField.getText());
                    //out.println(new String(passField.getPassword()))

                    //boolean loginConfirm = (boolean) in.readObject();

                    MessageClass loginConfirm = (MessageClass) in.readObject();

                    if (loginConfirm.getMessage().equals("true")) {
                        UserClass user = (UserClass) in.readObject();
                        StudentGUI studentGUI = new StudentGUI(loginSocket, in, out, user);
                        //studentGUI.setConnection(login, out, in);
                        dispose();
                        //setVisible(false);
                    }
                    else {
                        JOptionPane.showMessageDialog(null,
                                "Error: Incorrect username or password.", "Error", 0);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        SwingUtilities.getRootPane(loginButton).setDefaultButton(loginButton);

        // setting the listener for the register file
        registerButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                new RegisterGUI();
            }
        });

        // setting the listener for using the security question
        forgottenPassLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ForgottenPasswordFrame();
            }
        });
    }
}
