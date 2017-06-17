package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import t_industries.monstersandportals.NetworkClasses.UpdateServer;
import t_industries.monstersandportals.myserver.MyServer;

/**
 * Created by micha on 17.06.2017.
 */

public class ACKServer extends AsyncTask<Void, Void, Void> {
    UpdateServer updateServer;
    MyServer server;

    public ACKServer(UpdateServer updateServer, MyServer server) {
        this.updateServer = updateServer;
        this.server = server;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.server.sendACK(this.updateServer);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
