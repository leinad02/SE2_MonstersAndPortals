package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import t_industries.monstersandportals.NetworkClasses.UpdateClient;
import t_industries.monstersandportals.myclient.MyClient;

/**
 * Created by Michael on 17.06.2017.
 */

public class RandomStartClient extends AsyncTask<Void, Void, Void> {
    UpdateClient updateClient;
    int number;
    MyClient client;
    public RandomStartClient(UpdateClient updateClient, int numberForOrder, MyClient client) {
        this.updateClient = updateClient;
        this.number = numberForOrder;
        this.client = client;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.client.sendRandomNumber(this.updateClient, this.number);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
