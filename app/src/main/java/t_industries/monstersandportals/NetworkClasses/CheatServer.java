package t_industries.monstersandportals.NetworkClasses;

/**
 * Created by micha on 07.06.2017.
 */

public class CheatServer {
    private int successCheatClient = 0;
    private int readyCheatServer = 1;
    private int clientCheat = 0;
    private int detectCheat = 0;
    private int allowFurtherClient = 1;
    private String textCheat;

    public int getSuccessCheatClient() {
        return successCheatClient;
    }

    public void setSuccessCheatClient(int successCheatClient) {
        this.successCheatClient = successCheatClient;
    }

    public int getReadyCheatServer() {
        return readyCheatServer;
    }

    public void setReadyCheatServer(int readyCheatServer) {
        this.readyCheatServer = readyCheatServer;
    }

    public int getClientCheat() {
        return clientCheat;
    }

    public void setClientCheat(int clientCheat) {
        this.clientCheat = clientCheat;
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

    public int getAllowFurtherClient() {
        return allowFurtherClient;
    }

    public void setAllowFurtherClient(int allowFurtherClient) {
        this.allowFurtherClient = allowFurtherClient;
    }
}
