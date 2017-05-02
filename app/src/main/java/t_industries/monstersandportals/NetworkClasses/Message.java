package t_industries.monstersandportals.NetworkClasses;

import java.io.Serializable;

/**
 * Created by micha on 23.04.2017.
 */

public class Message implements Serializable {
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
