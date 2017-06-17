package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import t_industries.monstersandportals.NetworkClasses.CheatServer;
import t_industries.monstersandportals.myserver.MyServer;

/**
 * Created by micha on 17.06.2017.
 */

public class ServerDetectCheat extends AsyncTask<Void, Void, Void> {
    CheatServer cheatServer;
    MyServer server;

    public ServerDetectCheat(CheatServer cheatServer, MyServer server) {
        this.cheatServer = cheatServer;
        this.server = server;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.server.sendDetectMessage(this.cheatServer);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
