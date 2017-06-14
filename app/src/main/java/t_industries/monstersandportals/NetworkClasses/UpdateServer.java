package t_industries.monstersandportals.NetworkClasses;

/**
 * Created by micha on 28.04.2017.
 */

public class UpdateServer {
    private int position;
    private int readyForTurnServer = 0;
    private int activeSensorServer = 0;
    private int checkRandomNrServer = 0;
    private int isConnectedServer = 1;

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

    public int getActiveSensorServer() {
        return activeSensorServer;
    }

    public void setActiveSensorServer(int activeSensorServer) {
        this.activeSensorServer = activeSensorServer;
    }

    public int getCheckRandomNrServer() {
        return checkRandomNrServer;
    }

    public void setCheckRandomNrServer(int checkRandomNrServer) {
        this.checkRandomNrServer = checkRandomNrServer;
    }

    public int getIsConnectedServer() {
        return isConnectedServer;
    }

    public void setIsConnectedServer(int isConnectedServer) {
        this.isConnectedServer = isConnectedServer;
    }
}
