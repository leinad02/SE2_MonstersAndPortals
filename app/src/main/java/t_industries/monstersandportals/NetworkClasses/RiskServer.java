package t_industries.monstersandportals.NetworkClasses;

/**
 * Created by micha on 15.05.2017.
 */

public class RiskServer {
    private int checkFieldServer, failCounterServer;
    private String text;

    public int isCheckField() {
        return checkFieldServer;
    }

    public void setCheckFieldServer(int checkFieldServer) {
        this.checkFieldServer = checkFieldServer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getFailCounterServer() {
        return failCounterServer;
    }

    public void setFailCounterServer(int failCounterServer) {
        this.failCounterServer = failCounterServer;
    }
}
