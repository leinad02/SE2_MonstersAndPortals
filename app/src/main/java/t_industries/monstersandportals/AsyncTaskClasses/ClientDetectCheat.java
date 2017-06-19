package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import t_industries.monstersandportals.NetworkClasses.CheatClient;
import t_industries.monstersandportals.myclient.MyClient;

/**
 * Created by Michael on 17.06.2017.
 */

public class ClientDetectCheat extends AsyncTask<Void, Void, Void> {
    CheatClient cheatClient;
    MyClient client;
    public ClientDetectCheat(CheatClient cheatClient, MyClient client) {
        this.cheatClient = cheatClient;
        this.client = client;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.client.sendDetectMessage(this.cheatClient);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
