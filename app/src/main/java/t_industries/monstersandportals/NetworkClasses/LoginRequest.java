package t_industries.monstersandportals.NetworkClasses;

import java.io.Serializable;

/**
 * Created by Michi on 06.04.17.
 */

public class LoginRequest implements Serializable {
    private String messageText;

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
