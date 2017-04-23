package t_industries.monstersandportals;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;

import t_industries.monstersandportals.NetworkClasses.ForServer;
import t_industries.monstersandportals.myclient.MyClient;
import t_industries.monstersandportals.myserver.MyServer;

/**
 * Created by micha on 21.04.2017.
 */

public class ServerActivity extends Activity implements View.OnClickListener {
    private EditText name;
    private Button createS, home;
    MyServer server;
    Handler handler;
    ForServer forServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        //Damit der Screen immer aktiv bleibt, diese kleine Änderung
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        name = (EditText) findViewById(R.id.textfieldNameServer);
        createS = (Button) findViewById(R.id.createServer);
        createS.setOnClickListener(this);
        home = (Button) findViewById(R.id.home);
        home.setOnClickListener(this);
        handler = new Handler();
        forServer = new ForServer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.createServer:
                final String textName = name.getText().toString();
                if(TextUtils.isEmpty(textName)){
                    name.setError("Bitte Namen eingeben!");
                    return;
                }

                new MyTask().execute();

                /*final ProgressDialog dialog = ProgressDialog.show(this, "Laden", "Bitte warten bis der Gegner beigetreten ist...", true);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        server.startServer(textName);
                        dialog.dismiss();
                    }
                }, 30000);*/
                break;

            case R.id.home:
                startActivity(new Intent(this, MenuActivity.class));
                break;

            default:
                break;
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        final String textName = name.getText().toString();

        @Override
        protected Void doInBackground(Void... params) {
            server = new MyServer(55555, 55556);
            server.startServer(textName, forServer);
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            Intent i = new Intent(getApplicationContext(), GameActivity.class);
            i.putExtra("serverName",textName);
            i.putExtra("clientName",forServer.getName());
            i.putExtra("type", "server");
            //i.putExtra("objectServer", server);
            startActivity(i);
            server.stopServer();
        }
    }
}
