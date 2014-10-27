import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.Scanner;

public class LoginGUI extends JFrame {

	 private JLabel studentLabel = new JLabel("Student ID: ");
     private JLabel passLabel = new JLabel("Password: ");
     public JTextField studentIDField = new JTextField(20);
     public JPasswordField passField = new JPasswordField(20);
     private JButton loginButton = new JButton("Log In");
     private JButton registerButton = new JButton("Register");
     private JLabel forgottenPassLabel = new JLabel("<html><b><u>Forgotten your password?" +
     		"</u></b></html>");
    private String password;
    private String question;
    private String answer;
	
    public LoginGUI() {
    	setTitle("Log in 3");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout();
        setVisible(true);
        setResizable(false);
        //setSize(400, 400);
        pack();
        setLocationRelativeTo(null);
        addListeners();
    }

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
    
    private void addListeners(){
    	LoginListener login = new LoginListener(studentIDField, passField);
    	loginButton.addActionListener(login);
    	
    	registerButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				new RegisterGUI();
			}
		});
    	
    	forgottenPassLabel.addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
			}
			public void mousePressed(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseClicked(MouseEvent e) {
                askForID();

			}
		});
    }
    private void askForID() {
        String inputValue = JOptionPane.showInputDialog("Enter your student ID");
        if (inputValue==null) {
            return;
        }
        if (validateID(inputValue)) {
            new ForgottenPasswordFrame(password, question, answer);
        } else {
            JOptionPane.showMessageDialog(null, "No student with such ID is registered");
            askForID();
        }
    }
    private boolean validateID(String id) {

        File users = new File("users.csv");
        int count = 0;
        Scanner sc;
        try {
            sc = new Scanner(users);
            while(sc.hasNextLine()) {
                count++;
                sc.nextLine();
            }

            sc.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        String[][] userData = new String[count][4];

        String csvFile = "users.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(csvFile));
            int count2 = 0;
            while((line = br.readLine()) != null) {
                String[] temp = line.split(csvSplitBy);
                userData[count2][0] = temp[1];
                userData[count2][1] = temp[3];
                userData[count2][2] = temp[4];
                userData[count2][3] = temp[5];
                count2++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for(int i = 0; i < count; i++) {
            if((id.equals(userData[i][0]))) {
                password = userData[i][1];
                question = userData[i][2];
                answer = userData[i][3];
                return true;
            }
        }
        return false;
    }
}
