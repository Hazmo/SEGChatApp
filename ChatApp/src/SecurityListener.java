package src;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.Scanner;
import javax.swing.JOptionPane;


public class SecurityListener implements MouseListener{
    
    private String password;
    private String question;
    private String answer;
    
    
    public void mouseClicked(MouseEvent e) {
        askForID();
    }
    public void mousePressed(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    
    
    private void askForID() {
        String inputValue = JOptionPane.showInputDialog("Enter your student ID");
        if (inputValue==null) {
            return;
        }
        
        if (validateID(inputValue)) {
            //new ForgottenPasswordFrame(password, question, answer);
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
