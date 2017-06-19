package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import t_industries.monstersandportals.myclient.MyClient;

/**
 * Created by Michael on 18.06.2017.
 */

public class CheckCheatClient extends AsyncTask<Void, Void, Void> {
    String decision;
    MyClient client;
    public CheckCheatClient(String decision, MyClient client) {
        this.decision = decision;
        this.client = client;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if(decision.equalsIgnoreCase("successcheat")){
            this.client.sendCheatMessage();
        } else {
            this.client.sendCheatMessageFail();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
