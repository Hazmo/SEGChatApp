import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Created by Ainura on 03.11.2014.
 */
class UserReport extends JFrame implements ActionListener{

	String title;
	UserClass user;
	JTextField answer = new JTextField(30);
	JTextArea detailsTextArea = new JTextArea(10, 30);
	
    UserReport(UserClass user, String title) {
    	this.user = user;
    	this.title = title;
        setTitle("Report: " + title);
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));
        JPanel main = new JPanel(new BorderLayout(0, 10));
        JPanel north = new JPanel(new BorderLayout());
        JPanel center = new JPanel(new BorderLayout());
        JPanel south = new JPanel(new FlowLayout());
        
        JLabel question = new JLabel("What would you like to report about?");
        JLabel detailsLabel = new JLabel("Give us more details");
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
            submitReport();
            JOptionPane
                    .showMessageDialog(this,
                            "Thank you for reporting about the problem! Your report has been sent to the moderator.");
            this.dispose();
        } else {
            this.dispose();
        }
    }
    private void submitReport() {
    	String userName = user.getName();
    	String reportTitle = answer.getText().toString();
    	String reportMessage = detailsTextArea.getText().toString();
    	
    	ReportClass report = new ReportClass(userName, reportTitle, reportMessage, title);
    	
    	try(Socket reportSocket = new Socket("localhost", 4459);
            ObjectOutputStream out = new ObjectOutputStream(reportSocket.getOutputStream());
    		BufferedReader in = new BufferedReader(new InputStreamReader(reportSocket.getInputStream()));
    	){
    		out.writeObject(new String("ADD"));
    		out.writeObject(report);
    		JOptionPane.showMessageDialog(null, in.readLine());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}