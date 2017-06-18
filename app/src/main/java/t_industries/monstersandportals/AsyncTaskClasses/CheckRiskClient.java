package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import t_industries.monstersandportals.myclient.MyClient;

/**
 * Created by Michael on 17.06.2017.
 */

public class CheckRiskClient extends AsyncTask<Void, Void, Void> {
    String decision;
    MyClient client;
    public CheckRiskClient(String decision, MyClient client) {
        this.decision = decision;
        this.client = client;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if(decision.equalsIgnoreCase("fail")){
            this.client.sendRiskFail();
        } else if(decision.equalsIgnoreCase("success")) {
            this.client.sendRiskField();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
