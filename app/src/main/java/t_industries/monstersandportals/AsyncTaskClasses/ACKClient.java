package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import t_industries.monstersandportals.NetworkClasses.UpdateClient;
import t_industries.monstersandportals.myclient.MyClient;

/**
 * Created by micha on 17.06.2017.
 */

public class ACKClient extends AsyncTask<Void, Void, Void> {
    UpdateClient updateClient;
    MyClient client;

    public ACKClient(UpdateClient updateClient, MyClient client) {
        this.updateClient = updateClient;
        this.client = client;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.client.sendACK(this.updateClient);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
