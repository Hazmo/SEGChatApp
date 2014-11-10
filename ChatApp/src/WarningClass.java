package src;

import java.io.Serializable;

public class WarningClass implements Serializable {

    String mod;
    String warningMessage;
    String user;

    WarningClass(String mod, String userID, String warningMessage) {
        this.mod = mod;
        this.user = userID;
        this.warningMessage = warningMessage;
    }

    String getUser() {
        return user;
    }

    String getWarningMessage() {
        return warningMessage;
    }

    String getMod() {
        return mod;
    }

    String getListDesc() {
        return new String(mod + " - " + warningMessage);
    }

}
