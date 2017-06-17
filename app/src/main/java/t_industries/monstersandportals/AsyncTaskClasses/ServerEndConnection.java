package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import t_industries.monstersandportals.myserver.MyServer;

/**
 * Created by micha on 17.06.2017.
 */

public class ServerEndConnection extends AsyncTask<Void, Void, Void> {
    MyServer server;

    public ServerEndConnection(MyServer server) {
        this.server = server;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.server.sendEndConnection();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
