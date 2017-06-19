package t_industries.monstersandportals;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import t_industries.monstersandportals.NetworkClasses.ForClient;
import t_industries.monstersandportals.myclient.MyClient;

/**
 * Created by micha on 21.04.2017.
 */

public class ClientActivity extends Activity implements View.OnClickListener {
    private EditText name, ip;
    private Button connectS, home;
    protected int isPlayed;
    MyClient client;
    ForClient forClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Die Titelleiste (Akku etc.) wird ausgeblendet wichtig, dies muss vor setContentView geschehen, sonst schmeißt
        das Programm eine Exception*/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        //Damit der Screen immer aktiv bleibt, diese kleine Änderung
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Der Screen wird noch auf Fullscreen gesetzt
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        name = (EditText) findViewById(R.id.textfieldNameClient);
        ip = (EditText) findViewById(R.id.textfieldIP);
        connectS = (Button) findViewById(R.id.connectServer);
        connectS.setOnClickListener(this);
        home = (Button) findViewById(R.id.home);
        home.setOnClickListener(this);
        forClient = new ForClient();
        Intent i = this.getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            isPlayed = bundle.getInt("isPlayed");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.connectServer:
                final String textName = name.getText().toString();
                final String textIP = ip.getText().toString();
                if (TextUtils.isEmpty(textName)) {
                    name.setError("Bitte Namen eingeben!");
                    return;
                } else if (TextUtils.isEmpty(textIP)) {
                    ip.setError("Bitte IP-Adresse eingeben!");
                    return;
                }
                new MyTask().execute();
                break;

            case R.id.home:
                Intent home = new Intent(this, MenuActivity.class);
                home.putExtra("isPlayed", isPlayed);
                startActivity(home);
                break;

            default:
                break;
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        final String textName = name.getText().toString();
        final String textIP = ip.getText().toString();

        @Override
        protected Void doInBackground(Void... params) {
            client = new MyClient(55555, 55556, 5000);
            client.connect(textIP, textName, forClient);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Intent i = new Intent(getApplicationContext(), GameActivity.class);
            i.putExtra("serverName", forClient.getName());
            i.putExtra("clientName", textName);
            i.putExtra("type", "client");
            i.putExtra("ip", textIP);
            //i.putExtra("objectClient", client);
            startActivity(i);
            MusicManager.player.stop();
            client.disconnect();
        }
    }
}
