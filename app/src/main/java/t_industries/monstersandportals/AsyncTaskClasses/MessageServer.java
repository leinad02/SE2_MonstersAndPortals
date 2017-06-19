package t_industries.monstersandportals.AsyncTaskClasses;

import android.os.AsyncTask;

import t_industries.monstersandportals.myserver.MyServer;

/**
 * Created by Michael on 17.06.2017.
 */

public class MessageServer extends AsyncTask<Void, Void, Void> {
    int rolledNr;
    MyServer server;

    public MessageServer(int roll, MyServer server) {
        this.rolledNr = roll;
        this.server = server;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.server.sendPosition(this.rolledNr);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
