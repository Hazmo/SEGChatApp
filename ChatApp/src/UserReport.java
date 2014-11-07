package src;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/** * Created by Ainura on 03.11.2014. */
class UserReport extends JFrame implements ActionListener {

    UserReport() {
        setTitle("Report");
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));
        JPanel main = new JPanel(new BorderLayout(0, 10));
        JPanel north = new JPanel(new BorderLayout());
        JPanel center = new JPanel(new BorderLayout());
        JPanel south = new JPanel(new FlowLayout());

        JLabel question = new JLabel("What would you like to report about?");
        JTextField answer = new JTextField(30);
        JLabel detailsLabel = new JLabel("Give us more details");
        JTextArea detailsTextArea = new JTextArea(10, 30);
        JButton submitButton = new JButton("SUBMIT");
        JButton cancelButton = new JButton("CANCEL");
        submitButton.addActionListener(this);
        submitButton.setActionCommand("submit");
        cancelButton.addActionListener(this);
        cancelButton.setActionCommand("cancel");

        north.add(question, BorderLayout.NORTH);
        north.add(answer, BorderLayout.SOUTH);
        center.add(detailsLabel, BorderLayout.NORTH);
        center.add(detailsTextArea, BorderLayout.CENTER);
        south.add(submitButton);
        south.add(cancelButton);

        main.add(north, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);

        add(main);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("submit")) {
            submitReport();
            JOptionPane
                    .showMessageDialog(this,
                            "Thank you for reporting about the problem! Your report has been sent to the moderator.");
            this.dispose();
        }
        else {
            this.dispose();
        }
    }

    private void submitReport() {

    }
}
