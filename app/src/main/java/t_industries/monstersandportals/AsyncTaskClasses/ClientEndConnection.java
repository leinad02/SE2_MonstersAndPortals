package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import t_industries.monstersandportals.myclient.MyClient;

/**
 * Created by Michael on 17.06.2017.
 */

public class ClientEndConnection extends AsyncTask<Void, Void, Void> {
    MyClient client;

    public ClientEndConnection(MyClient client) {
        this.client = client;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.client.sendEndConnection();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
