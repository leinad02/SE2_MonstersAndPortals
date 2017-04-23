package t_industries.monstersandportals.NetworkClasses;

import java.io.Serializable;

/**
 * Created by micha on 16.04.2017.
 */

public class ClientName implements Serializable {
    private String nameFromClient;

    public String getNameFromClient() {
        return nameFromClient;
    }

    public void setNameFromClient(String nameFromClient) {
        this.nameFromClient = nameFromClient;
    }
}
