package src;
/**
 * @author Ainur Makhmet - 1320744
 * @email ainur.makhmet@kcl.ac.uk
 */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * Extends JFrame and contains only one constructor.
 */
class ForgottenPasswordFrame extends JFrame implements ActionListener {
    JLabel IDLabel;
    JTextField IDField;
    JButton getQuestionButton;
    JLabel questionLabel;
    JLabel answerLabel;
    JTextField answerField;
    JButton getPassButton;
    Socket socket;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    boolean confirm = false;

    /**
     * This constructor is called whenever the user correctly inserted his student ID in order to
     * remember his password. If the user answers to the secret question correctly, then the
     * password appears underneath the answer, else the user is notified the the answer is wrong.
     * @param password
     * is the user's forgotten password.
     * @param question
     * is the secret question that the student introduced when was filling the registration
     * form.
     * @param answer
     * is the answer to the secret question that has been introduced by the student himself.
     */
    ForgottenPasswordFrame(Socket socket, ObjectOutputStream out, ObjectInputStream in) {

        this.socket = socket;
        this.out = out;
        this.in = in;

        /**
        try {
            socket = new Socket("localhost", 4457);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

         */
        setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
        setTitle("Password Retrieval");
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));
        setLayout(new GridLayout(3, 1, 10, 10));
        IDLabel = new JLabel("Enter your ID: ");
        IDField = new JTextField(20);
        IDField.setActionCommand("IDField");
        IDField.addActionListener(this);
        getQuestionButton = new JButton("Get question");
        getQuestionButton.setActionCommand("questionButton");
        getQuestionButton.addActionListener(this);
        questionLabel = new JLabel("Question: ");
        answerLabel = new JLabel("Enter answer: ");
        answerField = new JTextField(20);
        answerField.setActionCommand("answerField");
        answerField.addActionListener(this);
        getPassButton = new JButton("Get password");
        getPassButton.setActionCommand("getPassButton");
        getPassButton.addActionListener(this);
        JPanel IDInputPanel = new JPanel(new BorderLayout(5, 0));
        IDInputPanel.add(IDLabel, BorderLayout.WEST);
        IDInputPanel.add(IDField);
        IDInputPanel.add(getQuestionButton, BorderLayout.EAST);
        JPanel answerInputPanel = new JPanel(new BorderLayout(5, 0));
        answerInputPanel.add(answerLabel, BorderLayout.WEST);
        answerInputPanel.add(answerField);
        answerInputPanel.add(getPassButton, BorderLayout.EAST);
        add(IDInputPanel);
        add(questionLabel);
        add(answerInputPanel);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    //socket.close();
                   //in.close();
                    //out.close();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().matches("questionButton")) {
            try {
                out.writeObject(new MessageClass("forgot_password", ""));

                JLabel ID = new JLabel(IDField.getText());
                out.writeObject(ID);
                confirm = (boolean) in.readObject();
                if (confirm) {
                    getQuestionButton.setEnabled(false);
                    questionLabel.setText("Question: " + in.readObject());
                }
                else {
                    questionLabel.setText("Question: ");
                    JOptionPane.showMessageDialog(this, "This ID has not been registered!");
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (e.getActionCommand().matches("getPassButton")) {
            try {
                out.writeObject(new MessageClass("get_password", ""));

                out.writeObject(answerField.getText());

                confirm = (boolean) in.readObject();

                if (confirm) {
                    String password = (String) in.readObject();

                    JOptionPane.showMessageDialog(this, "Your password is: " + password);
                    this.dispose();
                }
                else {
                    JOptionPane.showMessageDialog(this, "Answer is incorrect!");
                    this.dispose();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}