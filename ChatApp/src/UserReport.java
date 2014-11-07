package src;

import javax.swing.*; import java.awt.*; import java.awt.event.ActionEvent; import java.awt.event.ActionListener;

/** * Created by Ainura on 03.11.2014. */ class UserReport extends JFrame implements ActionListener{
JLabel question;
    String questionString= "What would you like to report about?";
    JComponent answer = new JTextField(30);
    JTextArea detailsTextArea;

    UserReport() {
        addLayout();
    }
    UserReport(String reportObject, String reportObjectValue) {
        questionString = "You are reporting about the following "+ reportObject;
        answer = new JLabel(reportObjectValue);
        answer.setForeground(Color.blue);
        answer.setBackground(Color.lightGray);
        addLayout();
    }
    public void addLayout() {
        setTitle("Report");
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));
        JPanel main = new JPanel(new BorderLayout(0, 10));
        JPanel north = new JPanel(new BorderLayout());
        JPanel center = new JPanel(new BorderLayout());
        JPanel south = new JPanel(new FlowLayout());



        JLabel question = new JLabel(questionString);
        JLabel detailsLabel = new JLabel("Why do you want to report about this?");
        detailsTextArea = new JTextArea(10, 30);
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
        south.add(submitButton); south.add(cancelButton);

        main.add(north, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);
        main.add(south, BorderLayout.SOUTH);

        add(main);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("submit")) {
            if (detailsTextArea.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "You did not tell us the reason for your report! Please, fill the relevant section for this.");
            } else {
                submitReport();
                JOptionPane.showMessageDialog(this, "Thank you for your report! It will be reviewed by a moderator.");
                this.dispose();
            }
        } else {
            this.dispose();
        }
    } private void submitReport() {

    }
}