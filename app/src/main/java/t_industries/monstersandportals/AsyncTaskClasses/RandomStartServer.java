package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import t_industries.monstersandportals.myserver.MyServer;

/**
 * Created by micha on 17.06.2017.
 */

public class RandomStartServer extends AsyncTask<Void, Void, Void> {
    MyServer server;

    public RandomStartServer(MyServer server) {
        this.server = server;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.server.sendACKRandom();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
