package t_industries.monstersandportals.NetworkClasses;

/**
 * Created by micha on 28.04.2017.
 */

public class UpdateClient {
    int position;
    int readyForTurnClient = 1;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getReadyForTurnClient() {
        return readyForTurnClient;
    }

    public void setReadyForTurnClient(int readyForTurnClient) {
        this.readyForTurnClient = readyForTurnClient;
    }
}
