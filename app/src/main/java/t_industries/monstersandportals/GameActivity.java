package t_industries.monstersandportals;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import t_industries.monstersandportals.NetworkClasses.Message;
import t_industries.monstersandportals.NetworkClasses.UpdateClient;
import t_industries.monstersandportals.NetworkClasses.UpdateServer;
import t_industries.monstersandportals.myclient.MyClient;
import t_industries.monstersandportals.myserver.MyServer;

/**
 * Created by micha on 22.04.2017.
 */

public class GameActivity extends Activity implements Serializable, View.OnClickListener {
    private TextView tvServerName, tvClientName;
    private Button closeServer, disconnect;
    int rolledNumber;
    Handler handler;
    MyServer server;
    MyClient client;
    UpdateClient updateClient;
    UpdateServer updateServer;


    static String[] gameBoard = new String[48]; // 8 x 6 Spielfeld
    static int[] monster = {19, 30, 46, 12, 25, 39};
    static int[] portal = {8, 22, 33, 13, 27, 41};
    static int[] risk = {10, 25, 39};

    static int userPosition = 0;
    static int rivalPosition = 0;

    Button [] buttons;
    Button roll;

    ImageView rollClient;
    ImageView rollServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Die Titelleiste (Akku etc.) wird ausgeblendet wichtig, dies muss vor setContentView geschehen, sonst schmeißt
        das Programm eine Exception*/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //Damit der Screen immer aktiv bleibt, diese kleine Änderung
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Der Screen wird noch auf Fullscreen gesetzt
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tvServerName = (TextView) findViewById(R.id.nameServer);
        tvClientName = (TextView) findViewById(R.id.nameClient);
        closeServer = (Button) findViewById(R.id.serverClose);
        disconnect = (Button) findViewById(R.id.disconnect);
        updateServer = new UpdateServer();
        updateClient = new UpdateClient();
        handler = new Handler();

        Intent i = this.getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            String serverName = bundle.getString("serverName");
            tvServerName.setText(serverName);
            String clientName = bundle.getString("clientName");
            tvClientName.setText(clientName);
            final String type = bundle.getString("type");
            if (type.equalsIgnoreCase("server")) {
                server = new MyServer(55557, 55558);
                new MyTaskServer().execute();
                closeServer.setVisibility(View.VISIBLE);
                closeServer.setOnClickListener(this);
                setBoard();
                rollServer.setVisibility(View.VISIBLE);
                startRunnableServer();

            } else if (type.equalsIgnoreCase("client")) {
                String ip = bundle.getString("ip");
                client = new MyClient(55557, 55558, 5000);
                new MyTaskClient(ip).execute();
                disconnect.setVisibility(View.VISIBLE);
                disconnect.setOnClickListener(this);
                setBoard();
                rollClient.setVisibility(View.VISIBLE);
                gameHandlerClient();
            }

        } else {
            Toast.makeText(GameActivity.this, "Keine Daten vorhanden!", Toast.LENGTH_LONG).show();
        }
    }

    private void startRunnableServer() {
        handler.postDelayed(runnableServer, 1000);
    }

    private Runnable runnableServer = new Runnable() {
        public void run() {
            int getReady = updateServer.getReadyForTurnServer();
            int rolledNrRival = updateServer.getPosition();
            if (getReady == 1) {
                newrivalPosition(rolledNrRival);
                checkBoard();
                Toast.makeText(GameActivity.this, "Client würfelte: " + rolledNrRival + ". Du bist am Zug!", Toast.LENGTH_SHORT).show();
                gameHandlerServer();
            } else {
                startRunnableServer();
            }
        }
    };

    private void startRunnableClient() {
        handler.postDelayed(runnableClient, 1000);
    }

    private Runnable runnableClient = new Runnable() {
        public void run() {
            int getReady = updateClient.getReadyForTurnClient();
            int rolledNrRival = updateClient.getPosition();
            if (getReady == 1) {
                newUserPosition(rolledNrRival);
                checkBoard();
                Toast.makeText(GameActivity.this, "Server würfelte: " + rolledNrRival + ". Du bist am Zug!", Toast.LENGTH_SHORT).show();
                gameHandlerClient();
            } else {
                startRunnableClient();
            }
        }
    };

    private void gameHandlerClient() {
        rollClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateClient.getReadyForTurnClient() == 0) {
                    Toast.makeText(GameActivity.this, "Bitte warten, der Server ist noch am Zug.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (userPosition <= 47 && rivalPosition <= 47) {
                    int rolledNo = rollDice();
                    setDiceClient(rolledNo);
                    System.out.println("Client zieht weiter:");
                    new MessageClient(rolledNo, updateClient).execute();
                    System.out.println("Host ist dran:");
                    newrivalPosition(rolledNo);
                    checkBoard();
                    startRunnableClient();
                }

                if (userPosition == 47) {
                    Toast.makeText(GameActivity.this, "Winner is the HOST!", Toast.LENGTH_LONG).show();
                } else if (rivalPosition == 47) {
                    Toast.makeText(GameActivity.this, "Winner is the GUEST!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void gameHandlerServer() {
        rollServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateServer.getReadyForTurnServer() == 0) {
                    Toast.makeText(GameActivity.this, "Bitte warten, der Client ist noch am Zug.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (userPosition <= 47 && rivalPosition <= 47) {
                    int rolledNo = rollDice();
                    setDiceServer(rolledNo);
                    System.out.println("Host zieht weiter:");
                    //if(newUserPosition(rolledNo) == 20)
                    new MessageServer(rolledNo, updateServer).execute();
                    System.out.println("Client ist dran:");
                    newUserPosition(rolledNo);
                    checkBoard();
                    startRunnableServer();
                }

                if (userPosition == 47) {
                    Toast.makeText(GameActivity.this, "Winner is the HOST!", Toast.LENGTH_LONG).show();
                } else if (rivalPosition == 47) {
                    Toast.makeText(GameActivity.this, "Winner is the GUEST!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }



    public static void newUserPosition(int rolledNo) {                // Bewegt den Host

        if (gameBoard[userPosition] == "H G") {
            gameBoard[userPosition] = "G";
        } else {
            gameBoard[userPosition] = "";
        }

        userPosition = userPosition + rolledNo;
        if (userPosition > 47) {
            userPosition = 47;
        }

        if (gameBoard[userPosition] == "G") {
            gameBoard[userPosition] = "H G";
        } else {
            gameBoard[userPosition] = "H";
        }
        System.out.println("Hostposition ist: " + userPosition);
        userPosition = checkMonsterOrPortalOrRisk(userPosition);
    }

    public static void newrivalPosition(int rolledNo) {             // Bewegt den Gast

        if (gameBoard[rivalPosition] == "H G") {
            gameBoard[rivalPosition] = "H";
        } else {
            gameBoard[rivalPosition] = "";
        }

        rivalPosition = rivalPosition + rolledNo;
        if (rivalPosition > 47) {
            rivalPosition = 47;
        }

        if (gameBoard[rivalPosition] == "H") {
            gameBoard[rivalPosition] = "H G";
        } else {
            gameBoard[rivalPosition] = "G";
        }
        System.out.println("Die Position des Gastspielers ist: " + rivalPosition);
        rivalPosition = checkMonsterOrPortalOrRisk(rivalPosition);
    }

    private static int checkMonsterOrPortalOrRisk(int position) {       // überprüft ob das Feld, welches man Betreten hat, eines der Eventfelder ist
        for ( int i = 0; i < 3; i++) {
            if (position == monster[i]) {
                System.out.println("Ohhh Nein! Ein MONSTER greift dich an!");

                if (getReady == 1){
                    if (gameBoard[monster[i]] == "H G"){
                        gameBoard[monster[i]] = "G";
                    } else {
                        gameBoard[monster[i]] = ""; }
                } else {
                    if ( gameBoard[monster[i]] == "H G"){
                        gameBoard[monster[i]] = "H";
                    } else {
                        gameBoard[monster[i]] = "";
                    }
                }

                position = monster[i+3];             // der derzeitige Spieler betritt einen Monsterfeld

                if (getReady == 1){
                    if (gameBoard[monster[i+3]] == "G"){
                        gameBoard[monster[i+3]] = "H G";
                    } else {
                        gameBoard[monster[i+3]] = "H"; }
                } else {
                    if ( gameBoard[monster[i+3]] == "H"){
                        gameBoard[monster[i+3]] = "H G";
                    } else {
                        gameBoard[monster[i+3]] = "G";
                    }
                }

            } else if (position == portal[i]) {     // Hier wird in der Zukunft die Klasse Portal ausgeführt

                if (getReady == 1){
                    if (gameBoard[portal[i]] == "H G"){
                        gameBoard[portal[i]] = "G";
                    } else {
                        gameBoard[portal[i]] = ""; }
                } else {
                    if ( gameBoard[portal[i]] == "H G"){
                        gameBoard[portal[i]] = "H";
                    } else {
                        gameBoard[portal[i]] = "";
                    }
                }

                position = portal[i+3];             // der derzeitige Spieler geht durch den Portal

                if (getReady == 1){
                    if (gameBoard[portal[i+3]] == "G"){
                        gameBoard[portal[i+3]] = "H G";
                    } else {
                        gameBoard[portal[i+3]] = "H"; }
                } else {
                    if ( gameBoard[portal[i+3]] == "H"){
                        gameBoard[portal[i+3]] = "H G";
                    } else {
                        gameBoard[portal[i+3]] = "G";
                    }
                }

            } else if (position == risk[i]) {
                System.out.println("WoW, ein Risikofeld. Du ziehst jetzt eine RISIKOKARTE!");
                // drawRiskcard();      Hier dann einfügen was passieren soll, wenn man auf einem Risiko Feld landet
            }
        }
        return position;
    }

    public void checkBoard(){                                           //checkt wo Host & Gast sich gerade befinden

        for (int i = 0; i < 48; i++) {
            String idName = "f" + i;
            buttons[i].setText(gameBoard[i]);
        }

    }

    public void setBoard(){                                             //Die buttons aus dem Layout werden ins code übernommen

        buttons = new Button[48];

        Resources res = getResources();                                 //if you are in an activity
        for (int i = 0; i < 48; i++) {
            String idName = "f" + i;
            buttons[i] = (Button) findViewById(res.getIdentifier(idName, "id", getPackageName()));
        }


        roll = (Button) findViewById(R.id.roll);
        roll.setText("Würfel");

        for (int i = 0; i < 3; i++) {
            buttons[monster[i]].setBackgroundColor(Color.RED);          // Monsterfelder
            buttons[risk[i]].setBackgroundColor(Color.GREEN);           // Eventfelder
            buttons[portal[i]].setBackgroundColor(Color.BLUE);          // Portalfelder

            buttons[monster[i+3]].setBackgroundColor(Color.DKGRAY);     // Monsterfelder Ausgang
            buttons[portal[i+3]].setBackgroundColor(Color.CYAN);        // Portalfelder Ausgang

        }

        for (int i = 0; i < gameBoard.length; i++ ){                    // Board Array wird gecleart
            gameBoard[i] = "";
        }

        gameBoard[0] = "H G";                                           // Spieler werden auf den ersten Feld gesetzt
        buttons[0].setText(gameBoard[0]);

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
            server.startServerNew(updateServer);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
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
            client.connectNew(this.ip, updateClient);
            client.sendWelcomeMessage();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private class MessageClient extends AsyncTask<Void, Void, Void> {
        int rolledNr;
        UpdateClient updateClient;

        public MessageClient(int roll, UpdateClient updateClient) {
            this.rolledNr = roll;
            this.updateClient = updateClient;
        }

        @Override
        protected Void doInBackground(Void... params) {
            client.sendPosition(this.rolledNr, this.updateClient);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private class MessageServer extends AsyncTask<Void, Void, Void> {
        int rolledNr;
        UpdateServer updateServer;

        public MessageServer(int roll, UpdateServer updateServer) {
            this.rolledNr = roll;
            this.updateServer = updateServer;
        }

        @Override
        protected Void doInBackground(Void... params) {
            server.sendPosition(this.rolledNr, this.updateServer);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private int rollDice() {
        Random random_d = new Random();
        rolledNumber = random_d.nextInt(6) + 1;
        return rolledNumber;
    }


    private void setDiceServer(int rolledNo) {

        if (rolledNo == 1) {
            rollServer.setImageResource(R.drawable.d1);
        } else if (rolledNo == 2) {
            rollServer.setImageResource(R.drawable.d2);
        } else if (rolledNo == 3) {
            rollServer.setImageResource(R.drawable.d3);
        } else if (rolledNo == 4) {
            rollServer.setImageResource(R.drawable.d4);
        } else if (rolledNo == 5) {
            rollServer.setImageResource(R.drawable.d5);
        } else if (rolledNo == 6) {
            rollServer.setImageResource(R.drawable.d6);
        }
    }

    private void setDiceClient(int rolledNo) {
        if (rolledNo == 1) {
            rollClient.setImageResource(R.drawable.d1);
        } else if (rolledNo == 2) {
            rollClient.setImageResource(R.drawable.d2);
        } else if (rolledNo == 3) {
            rollClient.setImageResource(R.drawable.d3);
        } else if (rolledNo == 4) {
            rollClient.setImageResource(R.drawable.d4);
        } else if (rolledNo == 5) {
            rollClient.setImageResource(R.drawable.d5);
        } else if (rolledNo == 6) {
            rollClient.setImageResource(R.drawable.d6);
        }
    }

}
