package t_industries.monstersandportals.NetworkClasses;

/**
 * Created by micha on 28.04.2017.
 */

public class UpdateClient {
    private int position;
    private int readyForTurnClient = 1;
    private int activeSensorClient = 1;

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

    public int getActiveSensorClient() {
        return activeSensorClient;
    }

    public void setActiveSensorClient(int activeSensorClient) {
        this.activeSensorClient = activeSensorClient;
    }
}
