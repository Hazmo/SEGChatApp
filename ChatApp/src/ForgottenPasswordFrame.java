import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ForgottenPasswordFrame extends JFrame {

    public ForgottenPasswordFrame(String password, String question, final String answer) {
        final String PASSWORD = password;
        final String QUESTION = question;
        final String ANSWER = answer;

        setTitle("FORGOTTEN YOUR PASSWORD");
        ((JComponent)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));
        setLayout(new GridLayout(3,1,10,10));
        JLabel questionLabel = new JLabel("Your security question: "+question);
        final JTextField answerField = new JTextField(20);

        final JLabel passwordLabel = new JLabel("Correct! Your password is "+ PASSWORD);
        passwordLabel.setVisible(false);
        getContentPane().add(questionLabel);
        getContentPane().add(answerField);
        getContentPane().add(passwordLabel);

        answerField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!answerField.getText().toLowerCase().equals(answer)) {
                    passwordLabel.setText("Incorrect answer");
                } else {
                    passwordLabel.setText("Correct! Your password is "+ PASSWORD);
                }
                passwordLabel.setVisible(true);
            }
        });

        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
