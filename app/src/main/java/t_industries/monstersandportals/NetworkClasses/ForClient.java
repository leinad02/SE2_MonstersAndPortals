package t_industries.monstersandportals.NetworkClasses;

import java.io.Serializable;

/**
 * Created by micha on 16.04.2017.
 */

public class ForClient implements Serializable {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
