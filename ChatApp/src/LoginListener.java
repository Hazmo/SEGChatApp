package src;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class LoginListener implements ActionListener {
	
	private JTextField studentIDField;
	private JPasswordField passField;
	
        /** Parametric constructor providing the text field 
	 *     and the password field from the login
	 */
	public LoginListener(JTextField studentIDField, JPasswordField passField) {
		this.studentIDField = studentIDField;
		this.passField = passField;
	}

	public void actionPerformed(ActionEvent event) {
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
		
		String[][] userData = new String[count][3];
		
		String csvFile = "users.csv";
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		
		try {
			br = new BufferedReader(new FileReader(csvFile));
			int count2 = 0;
			while((line = br.readLine()) != null) {
				String[] temp = line.split(csvSplitBy);
				userData[count2][0] = temp[0];
				userData[count2][1] = temp[1];
				userData[count2][2] = temp[3];
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
		
		String user = studentIDField.getText();
		String pass = String.valueOf(passField.getPassword());
		
		MessageDigest md;
		String encrypted_pass;

            String passString = new String(passField.getPassword());
			boolean login_true = false;
			for(int i = 0; i < count; i++) {
				if((user.equals(userData[i][1])) && (passString.equals(userData[i][2]))) {
					login_true = true;
				}
			} 
			if(login_true == true) {
                System.out.println("Correct login");
                new StudentGUI();
			} else {
				JOptionPane.showMessageDialog(null, "No such login");
			}
	}

}