import java.io.Serializable;

public class UserClass implements Serializable {
    String[] userData;

    public UserClass(final String[] userData) {
        this.userData = userData;
    }

    @Override
    public String toString() {
        return this.userData[2];
    }

    public String getID() {
        return this.userData[1];
    }

    public boolean isAdmin() {
        if (this.userData[0].equals("0")) {
            return false;
        }
        else {
            return true;
        }
    }

    public String getName() {
        return this.userData[2];
    }

    public String getQuestion() {
        return this.userData[4];
    }

    public String getAnswer() {
        return this.userData[5];
    }

    public String getPassword() {
        return this.userData[3];
    }

    public void setName(final String name) {
        this.userData[2] = name;
    }

    public void setID(final String ID) {
        this.userData[1] = ID;
    }

    public void setAdmin(final boolean isAdmin) {
        if (isAdmin) {
            this.userData[0] = "1";
        }
        else {
            this.userData[0] = "0";
        }
    }

    public void setQuestion(final String question) {
        this.userData[4] = question;
    }

    public void setAnswer(final String answer) {
        this.userData[5] = answer;
    }

    public void setPassword(final String password) {
        this.userData[3] = password;
    }
}
