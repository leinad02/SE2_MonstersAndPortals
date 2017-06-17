package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import com.esotericsoftware.kryonet.Server;

import t_industries.monstersandportals.NetworkClasses.CheatServer;
import t_industries.monstersandportals.NetworkClasses.RiskServer;
import t_industries.monstersandportals.NetworkClasses.UpdateServer;
import t_industries.monstersandportals.myserver.MyServer;

/**
 * Created by micha on 17.06.2017.
 */

public class MyTaskServer extends AsyncTask<Void, Void, Void> {
    MyServer server;
    UpdateServer updateServer;
    RiskServer riskServer;
    CheatServer cheatServer;
    public MyTaskServer(MyServer server, UpdateServer updateServer, RiskServer riskServer, CheatServer cheatServer) {
        this.server = server;
        this.updateServer = updateServer;
        this.riskServer = riskServer;
        this.cheatServer = cheatServer;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.server.startServerNew(this.updateServer, this.riskServer, this.cheatServer);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
