package t_industries.monstersandportals.NetworkClasses;

import java.io.Serializable;

/**
 * Created by micha on 16.04.2017.
 */

public class ServerName implements Serializable {
    private String nameFromServer;

    public String getNameFromServer() {
        return nameFromServer;
    }

    public void setNameFromServer(String nameFromServer) {
        this.nameFromServer = nameFromServer;
    }
}
