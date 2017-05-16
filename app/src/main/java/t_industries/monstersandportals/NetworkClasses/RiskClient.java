package t_industries.monstersandportals.NetworkClasses;

/**
 * Created by micha on 15.05.2017.
 */

public class RiskClient {
    private int checkFieldClient, failCounterClient;
    private String text;

    public int isCheckFieldClient() {
        return checkFieldClient;
    }

    public void setCheckFieldClient(int checkFieldClient) {
        this.checkFieldClient = checkFieldClient;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getFailCounterClient() {
        return failCounterClient;
    }

    public void setFailCounterClient(int failCounterClient) {
        this.failCounterClient = failCounterClient;
    }
}
