package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import t_industries.monstersandportals.NetworkClasses.CheatClient;
import t_industries.monstersandportals.NetworkClasses.RiskClient;
import t_industries.monstersandportals.NetworkClasses.UpdateClient;
import t_industries.monstersandportals.myclient.MyClient;

/**
 * Created by micha on 17.06.2017.
 */

public class MyTaskClient extends AsyncTask<Void, Void, Void> {
    MyClient client;
    UpdateClient updateClient;
    RiskClient riskClient;
    CheatClient cheatClient;
    String ip;

    public MyTaskClient(MyClient client, UpdateClient updateClient, RiskClient riskClient, CheatClient cheatClient, String ip) {
        this.client = client;
        this.updateClient = updateClient;
        this.riskClient = riskClient;
        this.cheatClient = cheatClient;
        this.ip = ip;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.client.connectNew(this.ip, this.updateClient, this.riskClient, this.cheatClient);
        this.client.sendWelcomeMessage();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
