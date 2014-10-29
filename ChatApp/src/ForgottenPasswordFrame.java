package src;
/**
 * @author Ainur Makhmet - 1320744
 * @email ainur.makhmet@kcl.ac.uk
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Extends JFrame and contains only one constructor.
 */
class ForgottenPasswordFrame extends JFrame {
    /**
     * This constructor is called whenever the user correctly inserted his student ID in order to remember his password.
     * If the user answers to the secret question correctly, then the password appears underneath the answer, else the
     * user is notified the the answer is wrong.
     * @param password is the user's forgotten password.
     * @param question is the secret question that the student introduced when was filling the registration form.
     * @param answer is the answer to the secret question that has been introduced by the student himself.
     */
    ForgottenPasswordFrame(final String password, String question, final String answer) {

        setTitle("FORGOTTEN YOUR PASSWORD");
        ((JComponent)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));
        setLayout(new GridLayout(3,1,10,10));
        JLabel questionLabel = new JLabel("Your security question: " + question);
        final JTextField answerField = new JTextField(20);

        final JLabel passwordLabel = new JLabel("Correct! Your password is "+ password);
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
                    passwordLabel.setText("Correct! Your password is "+ password);
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
