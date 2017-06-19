package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import t_industries.monstersandportals.myserver.MyServer;

/**
 * Created by Michael on 17.06.2017.
 */

public class CheckRiskServer extends AsyncTask<Void, Void, Void> {
    String decision;
    MyServer server;
    public CheckRiskServer(String decision, MyServer server) {
        this.decision = decision;
        this.server = server;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if("fail".equalsIgnoreCase(decision)){
            this.server.sendRiskFail();
        } else if("success".equalsIgnoreCase(decision)) {
            this.server.sendRiskField();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
