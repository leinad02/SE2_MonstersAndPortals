package t_industries.monstersandportals;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;

import t_industries.monstersandportals.myclient.MyClient;
import t_industries.monstersandportals.myserver.MyServer;

/**
 * Created by micha on 22.04.2017.
 */

public class GameActivity extends Activity implements Serializable, View.OnClickListener {
    private TextView tvServerName, tvClientName;
    private Button closeServer, disconnect;
    MyServer server;
    MyClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //Damit der Screen immer aktiv bleibt, diese kleine Änderung
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        tvServerName = (TextView) findViewById(R.id.nameServer);
        tvClientName = (TextView) findViewById(R.id.nameClient);
        closeServer = (Button) findViewById(R.id.serverClose);
        disconnect = (Button) findViewById(R.id.disconnect);

        Intent i = this.getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            String serverName = bundle.getString("serverName");
            tvServerName.setText(serverName);
            String clientName = bundle.getString("clientName");
            tvClientName.setText(clientName);
            String type = bundle.getString("type");
            if(type.equalsIgnoreCase("server")){
                server = new MyServer(55557, 55558);
                new MyTaskServer().execute();
                closeServer.setVisibility(View.VISIBLE);
                closeServer.setOnClickListener(this);
            } else if (type.equalsIgnoreCase("client")){
                String ip = bundle.getString("ip");
                client = new MyClient(55557, 55558, 5000);
                new MyTaskClient(ip).execute();
                disconnect.setVisibility(View.VISIBLE);
                disconnect.setOnClickListener(this);
            }
        } else {
            System.out.println("Fehler, kein Name vorhanden!");
        }

        /*Object object = getIntent().getSerializableExtra("object");
        if(object instanceof MyServer){
            final MyServer server = (MyServer) object;
            closeServer.setVisibility(View.VISIBLE);
            if(closeServer.isPressed()){
                server.stopServer();
            }
        } else if(object instanceof MyClient){
            final MyClient client = (MyClient) object;
            client.sendWelcomeMessage();
            disconnect.setVisibility(View.VISIBLE);
            if(disconnect.isPressed()){
                client.disconnect();
            }
        }*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.serverClose:
                server.stopServer();
                break;

            case R.id.disconnect:
                client.disconnect();
                break;

            default:
                break;
        }
    }

    private class MyTaskServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            server.startServerNew();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }

    private class MyTaskClient extends AsyncTask<Void, Void, Void> {
        String ip;
        public MyTaskClient(String ip) {
            this.ip = ip;
        }

        @Override
        protected Void doInBackground(Void... params) {
            client.connectNew(this.ip);
            client.sendWelcomeMessage();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            super.onPostExecute(result);
        }
    }
}
