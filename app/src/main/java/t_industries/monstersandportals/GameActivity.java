package t_industries.monstersandportals;

import android.app.Activity;
import android.content.Intent;
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
    static String[] monster = new String[48];
    static String[] portal = new String[48];
    static String[] risk = new String[48];

    static Map<Integer, Integer> portalPositionValue;
    static Map<Integer, Integer> monsterPositionValue;
    static Map<Integer, Integer> riskPositionValue;
    static int userPosition = 0;
    static int rivalPosition = 0;

    Button f0, f1, f2, f3, f4, f5, f6, f7, f8, f9,
            f10, f11, f12, f13, f14, f15, f16, f17, f18, f19,
            f20, f21, f22, f23, f24, f25, f26, f27, f28, f29,
            f30, f31, f32, f33, f34, f35, f36, f37, f38, f39,
            f40, f41, f42, f43, f44, f45, f46, f47;

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
                gameBoard();
                setBoard();
                rollServer.setVisibility(View.VISIBLE);
                startRunnableServer();

            } else if (type.equalsIgnoreCase("client")) {
                String ip = bundle.getString("ip");
                client = new MyClient(55557, 55558, 5000);
                new MyTaskClient(ip).execute();
                disconnect.setVisibility(View.VISIBLE);
                disconnect.setOnClickListener(this);
                gameBoard();
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

    public static void gameBoard() {
        portalPositionValue = new HashMap<>();
        monsterPositionValue = new HashMap<>();
        riskPositionValue = new HashMap<>();

        /* Achtung: Spielbrettgröße und die Anzahl der Monster & Portale & Risiken können sich jederzeit ändern.
         Ich habe einfach den Array für das Spielbrett auf 48 und die Anzahl der Begegnungen auf 3 x 4 gesetzt gesetzt */

        for (int i = 0; i < 4; i++) {
            int portalPositionKey = (int) (Math.random() * 35) + 5;
            if (!portalPositionValue.keySet().contains(portalPositionKey)) {
                portalPositionValue.put((int) (Math.random() * 40) + 1, (int) (Math.random() * 10) + 5);
            } else {
                i--;
            }
            int monsterPositionKey = (int) (Math.random() * 40) + 10;
            if (!portalPositionValue.keySet().contains(portalPositionKey) &&
                    !monsterPositionValue.keySet().contains(portalPositionKey)) {
                monsterPositionValue.put((int) (Math.random() * 40) + 1, (int) (Math.random() * 10) + 5);
            } else {
                i--;
            }
            int riskPositionKey = (int) (Math.random() * 35) + 5;
            if (!portalPositionValue.keySet().contains(portalPositionKey) &&
                    !riskPositionValue.keySet().contains(portalPositionKey)) {
                riskPositionValue.put((int) (Math.random() * 40) + 1, (int) (Math.random() * 10) + 5);
            } else {
                i--;
            }
        }
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
        // userPosition = checkMonsterOrPortalOrRisk(userPosition);
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
        // rivalPosition = checkMonsterOrPortalOrRisk(rivalPosition);
    }

    public void checkBoard() {       //checkt wo Host & Gast sich gerade befinden

        f0.setText(gameBoard[0]);
        f10.setText(gameBoard[10]);
        f20.setText(gameBoard[20]);
        f30.setText(gameBoard[30]);
        f40.setText(gameBoard[40]);
        f1.setText(gameBoard[1]);
        f11.setText(gameBoard[11]);
        f21.setText(gameBoard[21]);
        f31.setText(gameBoard[31]);
        f41.setText(gameBoard[41]);
        f2.setText(gameBoard[2]);
        f12.setText(gameBoard[12]);
        f22.setText(gameBoard[22]);
        f32.setText(gameBoard[32]);
        f42.setText(gameBoard[42]);
        f3.setText(gameBoard[3]);
        f13.setText(gameBoard[13]);
        f23.setText(gameBoard[23]);
        f33.setText(gameBoard[33]);
        f43.setText(gameBoard[43]);
        f4.setText(gameBoard[4]);
        f14.setText(gameBoard[14]);
        f24.setText(gameBoard[24]);
        f34.setText(gameBoard[34]);
        f44.setText(gameBoard[44]);
        f5.setText(gameBoard[5]);
        f15.setText(gameBoard[15]);
        f25.setText(gameBoard[25]);
        f35.setText(gameBoard[35]);
        f45.setText(gameBoard[45]);
        f6.setText(gameBoard[6]);
        f16.setText(gameBoard[16]);
        f26.setText(gameBoard[26]);
        f36.setText(gameBoard[36]);
        f46.setText(gameBoard[46]);
        f7.setText(gameBoard[7]);
        f17.setText(gameBoard[17]);
        f27.setText(gameBoard[27]);
        f37.setText(gameBoard[37]);
        f47.setText(gameBoard[47]);
        f8.setText(gameBoard[8]);
        f18.setText(gameBoard[18]);
        f28.setText(gameBoard[28]);
        f38.setText(gameBoard[38]);
        f9.setText(gameBoard[9]);
        f19.setText(gameBoard[19]);
        f29.setText(gameBoard[29]);
        f39.setText(gameBoard[39]);

        // f10.setBackgroundColor(Color.BLUE);      f20.setBackgroundColor(Color.BLUE);     f30.setBackgroundColor(Color.BLUE);    f40.setBackgroundColor(Color.BLUE);

    }

    public void setBoard() {     //Die buttons aus dem Layout werden ins code übernommen

        f0 = (Button) findViewById(R.id.f0);
        f1 = (Button) findViewById(R.id.f1);
        f2 = (Button) findViewById(R.id.f2);
        f3 = (Button) findViewById(R.id.f3);
        f4 = (Button) findViewById(R.id.f4);
        f5 = (Button) findViewById(R.id.f5);
        f6 = (Button) findViewById(R.id.f6);
        f7 = (Button) findViewById(R.id.f7);
        f8 = (Button) findViewById(R.id.f8);
        f9 = (Button) findViewById(R.id.f9);

        f10 = (Button) findViewById(R.id.f10);
        f11 = (Button) findViewById(R.id.f11);
        f12 = (Button) findViewById(R.id.f12);
        f13 = (Button) findViewById(R.id.f13);
        f14 = (Button) findViewById(R.id.f14);
        f15 = (Button) findViewById(R.id.f15);
        f16 = (Button) findViewById(R.id.f16);
        f17 = (Button) findViewById(R.id.f17);
        f18 = (Button) findViewById(R.id.f18);
        f19 = (Button) findViewById(R.id.f19);

        f20 = (Button) findViewById(R.id.f20);
        f21 = (Button) findViewById(R.id.f21);
        f22 = (Button) findViewById(R.id.f22);
        f23 = (Button) findViewById(R.id.f23);
        f24 = (Button) findViewById(R.id.f24);
        f25 = (Button) findViewById(R.id.f25);
        f26 = (Button) findViewById(R.id.f26);
        f27 = (Button) findViewById(R.id.f27);
        f28 = (Button) findViewById(R.id.f28);
        f29 = (Button) findViewById(R.id.f29);

        f30 = (Button) findViewById(R.id.f30);
        f31 = (Button) findViewById(R.id.f31);
        f32 = (Button) findViewById(R.id.f32);
        f33 = (Button) findViewById(R.id.f33);
        f34 = (Button) findViewById(R.id.f34);
        f35 = (Button) findViewById(R.id.f35);
        f36 = (Button) findViewById(R.id.f36);
        f37 = (Button) findViewById(R.id.f37);
        f38 = (Button) findViewById(R.id.f38);
        f39 = (Button) findViewById(R.id.f39);

        f40 = (Button) findViewById(R.id.f40);
        f41 = (Button) findViewById(R.id.f41);
        f42 = (Button) findViewById(R.id.f42);
        f43 = (Button) findViewById(R.id.f43);
        f44 = (Button) findViewById(R.id.f44);
        f45 = (Button) findViewById(R.id.f45);
        f46 = (Button) findViewById(R.id.f46);
        f47 = (Button) findViewById(R.id.f47);

        rollClient = (ImageView) findViewById(R.id.rollClient);
        //rollClient.setText("Würfel");

        rollServer = (ImageView) findViewById(R.id.rollServer);
        //rollServer.setText("Würfel");

        for (int i = 0; i < gameBoard.length; i++) {
            gameBoard[i] = "";
            monster[i] = "";
            portal[i] = "";
            risk[i] = "";
        }

        gameBoard[0] = "H G";
        f0.setText(gameBoard[0]);


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
