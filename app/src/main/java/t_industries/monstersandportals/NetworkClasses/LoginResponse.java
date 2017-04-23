package t_industries.monstersandportals.NetworkClasses;

import java.io.Serializable;

/**
 * Created by Michi on 06.04.17.
 */

public class LoginResponse implements Serializable {
    private String responseText;

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
}
