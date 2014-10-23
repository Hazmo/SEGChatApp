import javax.swing.*;
import java.awt.*;

public class LoginClass extends JFrame {

    private JLabel studentLabel = new JLabel("Student ID: ");
    private JLabel passLabel = new JLabel("Password: ");
    private JTextField studentIDField = new JTextField(20);
    private JPasswordField passField = new JPasswordField(20);
    private JButton loginButton = new JButton("Log In");
    private JButton registerButton = new JButton("Register");
    private JLabel forgottenPassLabel = new JLabel("Forgotten your password?");

    public LoginClass() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout();
        setVisible(true);
        setResizable(false);
        pack();
    }

    public void setLayout() {
        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2));
        fieldsPanel.add(studentLabel);
        fieldsPanel.add(studentIDField);
        fieldsPanel.add(passLabel);
        fieldsPanel.add(passField);

        centerPanel.add(fieldsPanel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        buttonsPanel.add(loginButton);
        buttonsPanel.add(registerButton);

        centerPanel.add(buttonsPanel, BorderLayout.SOUTH);

        add(centerPanel);
        add(forgottenPassLabel, BorderLayout.SOUTH);
    }
}
