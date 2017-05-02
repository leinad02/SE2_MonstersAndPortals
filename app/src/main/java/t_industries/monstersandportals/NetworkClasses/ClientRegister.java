package t_industries.monstersandportals.NetworkClasses;

import java.io.Serializable;

/**
 * Created by Michael on 13.04.2017.
 */

public class ClientRegister implements Serializable {
    private boolean login;

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
}
