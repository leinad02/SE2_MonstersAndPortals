package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import t_industries.monstersandportals.myclient.MyClient;

/**
 * Created by Michael on 17.06.2017.
 */

public class MessageClient extends AsyncTask<Void, Void, Void> {
    int rolledNr;
    MyClient client;

    public MessageClient(int roll, MyClient client) {
        this.rolledNr = roll;
        this.client = client;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.client.sendPosition(this.rolledNr);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
