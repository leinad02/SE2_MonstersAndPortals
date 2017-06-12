package t_industries.monstersandportals.NetworkClasses;

/**
 * Created by micha on 07.06.2017.
 */

public class CheatClient {
    private int successCheatServer = 0;
    private int readyCheatClient = 1;
    private int serverCheat = 0;
    private int detectCheat = 0;
    private int allowFurtherServer = 1;
    private String textCheat;

    public int getSuccessCheatServer() {
        return successCheatServer;
    }

    public void setSuccessCheatServer(int successCheatServer) {
        this.successCheatServer = successCheatServer;
    }

    public int getReadyCheatClient() {
        return readyCheatClient;
    }

    public void setReadyCheatClient(int readyCheatClient) {
        this.readyCheatClient = readyCheatClient;
    }

    public int getServerCheat() {
        return serverCheat;
    }

    public void setServerCheat(int serverCheat) {
        this.serverCheat = serverCheat;
    }

    public int getDetectCheat() {
        return detectCheat;
    }

    public void setDetectCheat(int detectCheat) {
        this.detectCheat = detectCheat;
    }

    public String getTextCheat() {
        return textCheat;
    }

    public void setTextCheat(String textCheat) {
        this.textCheat = textCheat;
    }

    public int getAllowFurtherServer() {
        return allowFurtherServer;
    }

    public void setAllowFurtherServer(int allowFurtherServer) {
        this.allowFurtherServer = allowFurtherServer;
    }
}
