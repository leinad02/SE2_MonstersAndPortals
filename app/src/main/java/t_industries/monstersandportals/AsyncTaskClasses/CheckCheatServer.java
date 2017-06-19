package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import t_industries.monstersandportals.myserver.MyServer;

/**
 * Created by Michael on 18.06.2017.
 */

public class CheckCheatServer extends AsyncTask<Void, Void, Void> {
    String decision;
    MyServer server;
    public CheckCheatServer(String decision, MyServer server) {
        this.decision = decision;
        this.server = server;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if(decision.equalsIgnoreCase("successcheat")){
            this.server.sendCheatMessage();
        } else {
            this.server.sendCheatMessageFail();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
