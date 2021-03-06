package src;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class MessageThread extends Thread {

    ObjectInputStream in;
    JList ta;
    DefaultListModel<String> lm;

    public MessageThread(ObjectInputStream in, JList ta, DefaultListModel<String> lm) {
        this.in = in;
        this.ta = ta;
        this.lm = lm;

    }

    public void run() {
        String line;
        boolean ok = true;
        while (ok) {

            try {
                ok = (boolean) in.readObject();
                if (!ok) {
                    break;
                }
                MessageClass message = (MessageClass) in.readObject();
                lm.addElement(message.getMessage());
                ta.setModel(lm);
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                ok = false;
            }
            catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
