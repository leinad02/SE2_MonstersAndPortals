package t_industries.monstersandportals.NetworkClasses;

/**
 * Created by micha on 28.04.2017.
 */

public class UpdateServer {
    int position;
    int readyForTurnServer;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getReadyForTurnServer() {
        return readyForTurnServer;
    }

    public void setReadyForTurnServer(int readyForTurnClient) {
        this.readyForTurnServer = readyForTurnClient;
    }
}
