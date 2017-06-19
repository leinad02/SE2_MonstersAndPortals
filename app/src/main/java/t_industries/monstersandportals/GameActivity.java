package t_industries.monstersandportals;

import android.app.Activity;

import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Random;

import t_industries.monstersandportals.AsyncTaskClasses.ACKClient;
import t_industries.monstersandportals.AsyncTaskClasses.ACKServer;
import t_industries.monstersandportals.AsyncTaskClasses.CheckCheatClient;
import t_industries.monstersandportals.AsyncTaskClasses.CheckCheatServer;
import t_industries.monstersandportals.AsyncTaskClasses.CheckRiskClient;
import t_industries.monstersandportals.AsyncTaskClasses.CheckRiskServer;
import t_industries.monstersandportals.AsyncTaskClasses.ClientDetectCheat;
import t_industries.monstersandportals.AsyncTaskClasses.ClientEndConnection;
import t_industries.monstersandportals.AsyncTaskClasses.MessageClient;
import t_industries.monstersandportals.AsyncTaskClasses.MessageServer;
import t_industries.monstersandportals.AsyncTaskClasses.MyTaskClient;
import t_industries.monstersandportals.AsyncTaskClasses.MyTaskServer;
import t_industries.monstersandportals.AsyncTaskClasses.RandomStartClient;
import t_industries.monstersandportals.AsyncTaskClasses.RandomStartServer;
import t_industries.monstersandportals.AsyncTaskClasses.ServerDetectCheat;
import t_industries.monstersandportals.AsyncTaskClasses.ServerEndConnection;
import t_industries.monstersandportals.NetworkClasses.CheatClient;
import t_industries.monstersandportals.NetworkClasses.CheatServer;
import t_industries.monstersandportals.NetworkClasses.RiskClient;
import t_industries.monstersandportals.NetworkClasses.RiskServer;
import t_industries.monstersandportals.NetworkClasses.UpdateClient;
import t_industries.monstersandportals.NetworkClasses.UpdateServer;
import t_industries.monstersandportals.myclient.MyClient;
import t_industries.monstersandportals.myserver.MyServer;

import static java.lang.String.valueOf;

/**
 * Created by micha on 22.04.2017.
 */

public class GameActivity extends Activity implements Serializable, View.OnClickListener, SensorEventListener {
    private TextView tvServerName, tvClientName;
    private Button closeServer, disconnect;
    int rolledNumber;
    float speed;
    Handler handler;
    MyServer server;
    MyClient client;
    UpdateClient updateClient;
    UpdateServer updateServer;
    RiskServer riskServer; //für die Kommunikation
    RiskClient riskClient;
    CheatClient cheatClient;
    CheatServer cheatServer;
    private SensorManager sensorManager;
    private Sensor sensor;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 800;


    static String[] gameBoard = new String[48];  // 8 x 6 Spielfeld;;
    static int[] monster = {12, 31, 46, 5, 25, 19};
    static int[] portal = {7, 22, 33, 16, 28, 41};
    static int[] risk = {10, 26, 40};

    static int userPosition = 0;
    static int rivalPosition = 0;

    ImageView[] buttons;
    int[] gras = new int[48];                                       // für zufällige Grasbilder
    int[] grasImg = new int[]{                                      // für die Schleife in checkBoard();
            R.drawable.gras_1, R.drawable.gras_2,
            R.drawable.gras_3, R.drawable.gras_4,};

    ImageView rollClient;
    ImageView rollServer;
    Button btnCheatClient, btnCheatServer;


    Random random = new Random();
    protected int number = (random.nextInt(10) + 1);                  //Randomzahl zwischen 1 und 10
    protected String num = valueOf(number);
    protected int isRisk = 0;                                         //wenn richtig, den Player 4 Positionen vor schicken
    protected int isShown = 0;
    protected int isActiveOrderServer = 0;
    protected int isActiveOrderClient = 0;
    private int numberForOrder;
    private String type;
    //Dialoge für Monster,Portale,Gewonnen,Verloren
    Dialog dialog;
    ProgressDialog progressDialog;
    //neue Alternative für die Sounds,Initialisiere MediaPlayer zur Verwaltung von Audiodateien
    protected MediaPlayer mpLaugh, mpMonster, mpPortal, mpPunch, mpUoh, mpWoo, mpYeah, mpMainGameSound, mpWinSound, mpLoseSound;


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

        initializeButtons();
        initializeNetwork();
        initializeSensor();
        new MPSounds().execute();

        Intent i = this.getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            String serverName = bundle.getString("serverName");
            tvServerName.setText(serverName);
            String clientName = bundle.getString("clientName");
            tvClientName.setText(clientName);
            type = bundle.getString("type");
            if (type.equalsIgnoreCase("server")) {
                server = new MyServer(55557, 55558);
                new MyTaskServer(server, updateServer, riskServer, cheatServer).execute();
                closeServer.setVisibility(View.VISIBLE);
                closeServer.setOnClickListener(this);
                btnCheatServer.setOnClickListener(this);
                setBoard();
                rollServer.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startRunnableGameOrderServer();
                    }
                }, 1000);
            } else if (type.equalsIgnoreCase("client")) {
                final String ip = bundle.getString("ip");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        client = new MyClient(55557, 55558, 5000);
                        new MyTaskClient(client, updateClient, riskClient, cheatClient, ip).execute();
                    }
                }, 1000);
                disconnect.setVisibility(View.VISIBLE);
                disconnect.setOnClickListener(this);
                btnCheatClient.setOnClickListener(this);
                setBoard();
                rollClient.setVisibility(View.VISIBLE);
                startRunnableGameOrderClient();
            }

        } else {
            Toast.makeText(GameActivity.this, "Keine Daten vorhanden!", Toast.LENGTH_LONG).show();
        }

    }


    //alle Buttons initialisieren
    protected void initializeButtons() {
        tvServerName = (TextView) findViewById(R.id.nameServer);
        tvClientName = (TextView) findViewById(R.id.nameClient);
        closeServer = (Button) findViewById(R.id.serverClose);
        disconnect = (Button) findViewById(R.id.disconnect);
        rollClient = (ImageView) findViewById(R.id.rollClient);
        rollServer = (ImageView) findViewById(R.id.rollServer);
        btnCheatClient = (Button) findViewById(R.id.btnCheatClient);
        btnCheatServer = (Button) findViewById(R.id.btnCheatServer);
    }

    //Netzwerkteile initialisieren
    protected void initializeNetwork() {
        updateServer = new UpdateServer();
        updateClient = new UpdateClient();
        riskServer = new RiskServer();
        riskClient = new RiskClient();
        cheatClient = new CheatClient();
        cheatServer = new CheatServer();
        handler = new Handler();
    }

    //Sensor initialisieren
    protected void initializeSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, 1000000);
    }

    //Sounds werden in den Variablen gespeichert
    protected void createMPSounds() {
        mpLaugh = MediaPlayer.create(this, R.raw.laugh);
        mpMonster = MediaPlayer.create(this, R.raw.monster2);
        mpPortal = MediaPlayer.create(this, R.raw.portal);
        mpPunch = MediaPlayer.create(this, R.raw.punch);
        mpUoh = MediaPlayer.create(this, R.raw.ouh);
        mpWoo = MediaPlayer.create(this, R.raw.woo);
        mpYeah = MediaPlayer.create(this, R.raw.yeah);
        createWinSound();
        createLoseSound();
        createMainGameSound();
    }

    protected void createMainGameSound() {
        mpMainGameSound = MediaPlayer.create(this, R.raw.main_game_sound);
        mpMainGameSound.setLooping(true);
        mpMainGameSound.start();
    }

    protected void createWinSound() {
        mpWinSound = MediaPlayer.create(this, R.raw.win_sound);
        mpWinSound.setLooping(true);
    }

    protected void createLoseSound() {
        mpLoseSound = MediaPlayer.create(this, R.raw.lose_sound);
        mpLoseSound.setLooping(true);
    }

    private void setToastMessageStart() {
        if (type.equalsIgnoreCase("client")) {
            if (updateClient.getReadyForTurnClient() == 0) {
                Toast.makeText(GameActivity.this, "Schade, der Gegner beginnt!", Toast.LENGTH_SHORT).show();
                startRunnableClient();
            } else if (updateClient.getReadyForTurnClient() == 1) {
                Toast.makeText(GameActivity.this, "Glückwunsch, du darfst beginnen!", Toast.LENGTH_SHORT).show();
                gameHandlerClient();
            }
        } else {
            if (updateServer.getReadyForTurnServer() == 0) {
                Toast.makeText(GameActivity.this, "Schade, der Gegner beginnt!", Toast.LENGTH_SHORT).show();
                startRunnableServer();
            } else if (updateServer.getReadyForTurnServer() == 1) {
                Toast.makeText(GameActivity.this, "Glückwunsch, du darfst beginnen!", Toast.LENGTH_SHORT).show();
                gameHandlerServer();
            }
        }
    }

    private void startRunnableGameOrderServer() {
        handler.postDelayed(runnableOrderServer, 1000);
    }

    private Runnable runnableOrderServer = new Runnable() {
        @Override
        public void run() {
            if (updateServer.getCheckRandomNrServer() == 0) {
                if (isActiveOrderServer == 0) {
                    new RandomStartServer(server).execute();
                    isActiveOrderServer = 1;
                }
                startRunnableGameOrderServer();
            } else {
                setToastMessageStart();
                new RandomStartServer(server).execute();
            }
        }
    };

    private void startRunnableGameOrderClient() {
        handler.postDelayed(runnableOrderClient, 1000);
    }

    private Runnable runnableOrderClient = new Runnable() {
        @Override
        public void run() {
            if (updateClient.getCheckRandomNrClient() == 0) {
                if (isActiveOrderClient == 0) {
                    numberForOrder = random.nextInt(2);
                    new RandomStartClient(updateClient, numberForOrder, client).execute();
                    isActiveOrderClient = 1;
                }
                startRunnableGameOrderClient();
            } else {
                setToastMessageStart();
                new RandomStartClient(updateClient, numberForOrder, client).execute();
            }
        }
    };

    private void startRunnableServer() {
        handler.postDelayed(runnableServer, 500);
    }

    private Runnable runnableServer = new Runnable() {
        public void run() {
            if (updateServer.getIsConnectedServer() == 1) {
                if (cheatServer.getDetectCheat() == 0) {
                    int getReady = updateServer.getReadyForTurnServer();
                    int rolledNrRival = updateServer.getPosition();
                    if (getReady == 1) {
                        if (isRisk == 0) {
                            newrivalPosition(rolledNrRival);
                            checkBoard();
                            Toast.makeText(GameActivity.this, "Client würfelte: " + rolledNrRival + ". Du bist am Zug!", Toast.LENGTH_SHORT).show();
                        }
                        if (rivalPosition == 10 || rivalPosition == 26 || rivalPosition == 40) {
                            turn(4);
                        } else if (rivalPosition == 2 || rivalPosition == 18 || rivalPosition == 34) {
                            if (cheatServer.getSuccessCheatClient() == 0 && cheatServer.getAllowFurtherClient() == 1) {
                                setTurnSettings();
                                isRisk = 1;
                                checkProgressDialog();
                                startRunnableServer();
                            } else {
                                progressDialog.dismiss();
                                if (cheatServer.getClientCheat() == 1) {
                                    turn(6);
                                }
                                isShown = 0;
                                cheatServer.setSuccessCheatClient(0);
                                resetRiskValues();
                                resetTurnSettings();
                            }
                        }

                        if (rivalPosition == 8 || rivalPosition == 24 || rivalPosition == 40) {
                            if (cheatServer.getClientCheat() == 1) {
                                btnCheatServer.setVisibility(View.VISIBLE);
                                startTimerButton();
                                cheatServer.setClientCheat(0);
                            }
                        }

                        if (rivalPosition == 47) {
                            showDialogLose();
                        }

                        gameHandlerServer();
                    } else {
                        startRunnableServer();
                    }
                } else {
                    Toast.makeText(GameActivity.this, "Der Gegner hat dich entlarvt, du darfst jetzt nicht mehr schummeln!", Toast.LENGTH_SHORT).show();
                    cheatServer.setDetectCheat(0);
                    startRunnableServer();
                }
            } else {
                endConnectionDialogPassive();
            }

        }
    };

    private void startRunnableClient() {
        handler.postDelayed(runnableClient, 500);
    }

    private Runnable runnableClient = new Runnable() {
        public void run() {
            if (updateClient.getIsConnectedClient() == 1) {
                if (cheatClient.getDetectCheat() == 0) {
                    int getReady = updateClient.getReadyForTurnClient();
                    int rolledNrRival = updateClient.getPosition();
                    if (getReady == 1) {
                        if (isRisk == 0) {
                            newUserPosition(rolledNrRival);
                            checkBoard();
                            Toast.makeText(GameActivity.this, "Server würfelte: " + rolledNrRival + ". Du bist am Zug!", Toast.LENGTH_SHORT).show();
                        }
                        if (userPosition == 10 || userPosition == 26 || userPosition == 40) {
                            turn(4);
                        } else if (userPosition == 2 || userPosition == 18 || userPosition == 34) {
                            if (cheatClient.getSuccessCheatServer() == 0 && cheatClient.getAllowFurtherServer() == 1) {
                                setTurnSettings();
                                isRisk = 1;
                                checkProgressDialog();
                                startRunnableClient();
                            } else {
                                progressDialog.dismiss();
                                if (cheatClient.getServerCheat() == 1) {
                                    turn(6);
                                }
                                isShown = 0;
                                cheatClient.setSuccessCheatServer(0);
                                resetRiskValues();
                                resetTurnSettings();
                            }
                        }

                        if (userPosition == 8 || userPosition == 24 || userPosition == 40) {
                            if (cheatClient.getServerCheat() == 1) {
                                btnCheatClient.setVisibility(View.VISIBLE);
                                startTimerButton();
                                cheatClient.setServerCheat(0);
                            }
                        }

                        if (userPosition == 47) {
                            showDialogLose();
                        }

                        gameHandlerClient();
                    } else {
                        startRunnableClient();
                    }
                } else {
                    Toast.makeText(GameActivity.this, "Der Gegner hat dich entlarvt, du darfst jetzt nicht mehr schummeln!", Toast.LENGTH_SHORT).show();
                    cheatClient.setDetectCheat(0);
                    startRunnableClient();
                }
            } else {
                endConnectionDialogPassive();
            }

        }
    };

    private void setTurnSettings() {
        if (type.equalsIgnoreCase("client")) {
            rollClient.setVisibility(View.INVISIBLE);
            updateClient.setActiveSensorClient(0);
        } else {
            rollServer.setVisibility(View.INVISIBLE);
            updateServer.setActiveSensorServer(0);
        }
    }

    private void resetTurnSettings() {
        if (type.equalsIgnoreCase("client")) {
            rollClient.setVisibility(View.VISIBLE);
            updateClient.setActiveSensorClient(1);
        } else {
            rollServer.setVisibility(View.VISIBLE);
            updateServer.setActiveSensorServer(1);
        }
    }

    private void checkProgressDialog() {
        if (isShown == 0) {
            progressDialog = ProgressDialog.show(GameActivity.this, "Bitte warten", "Der Gegner ist gerade beim Beantworten.", true);
            isShown = 1;
        }
    }

    private void startTimerButton() {
        if (type.equalsIgnoreCase("client")) {
            handler.postDelayed(endTimerClient, 1000);
        } else {
            handler.postDelayed(endTimerServer, 1000);
        }
    }

    private Runnable endTimerServer = new Runnable() {
        @Override
        public void run() {
            btnCheatServer.setVisibility(View.INVISIBLE);
        }
    };

    private Runnable endTimerClient = new Runnable() {
        @Override
        public void run() {
            btnCheatClient.setVisibility(View.INVISIBLE);
        }
    };

    private void resetRiskValues() {
        isRisk = 0;
        if (type.equalsIgnoreCase("client")) {
            riskClient.setCheckFieldClient(0);
            riskClient.setFailCounterClient(0);
        } else {
            riskServer.setCheckFieldServer(0);
            riskServer.setFailCounterServer(0);
        }
    }

    private void gameHandlerClient() {
        rollClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameTurnClient();
            }
        });
    }


    private void gameHandlerServer() {
        rollServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameTurnServer();
            }
        });
    }

    public void gameTurnServer() {
        if (updateServer.getReadyForTurnServer() == 0) {
            Toast.makeText(GameActivity.this, "Bitte warten, der Client ist noch am Zug.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPosition <= 47 && rivalPosition <= 47) {
            int rolledNo = rollDice();
            setDiceServer();
            System.out.println("Host zieht weiter:");
            new MessageServer(rolledNo, server).execute();
            System.out.println("Client ist dran:");
            newUserPosition(rolledNo);
            checkBoard();

            if (userPosition == 10 || userPosition == 26 || userPosition == 40) {
                drawRiskcardServer();
            }

            if (userPosition == 2 || userPosition == 18 || userPosition == 34) {
                if (cheatServer.getReadyCheatServer() == 1) {
                    cheat();
                }
            }

            new ACKServer(updateServer, server).execute();
            startRunnableServer();
        }

        if (userPosition == 47) {
            showDialogWin();
        }
    }

    public void gameTurnClient() {
        if (updateClient.getReadyForTurnClient() == 0) {
            Toast.makeText(GameActivity.this, "Bitte warten, der Server ist noch am Zug.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPosition <= 47 && rivalPosition <= 47) {
            int rolledNo = rollDice();
            setDiceClient();
            System.out.println("Client zieht weiter:");
            new MessageClient(rolledNo, client).execute();
            System.out.println("Host ist dran:");
            newrivalPosition(rolledNo);
            checkBoard();

            if (rivalPosition == 10 || rivalPosition == 26 || rivalPosition == 40) {
                drawRiskcardClient();
            }

            if (rivalPosition == 2 || rivalPosition == 18 || rivalPosition == 34) {
                if (cheatClient.getReadyCheatClient() == 1) {
                    cheat();
                }
            }

            new ACKClient(updateClient, client).execute();
            startRunnableClient();
        }

        if (rivalPosition == 47) {
            showDialogWin();
        }
    }

    public void newUserPosition(int rolledNo) {                // Bewegt den Host

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
        userPosition = checkFieldtypeServer(userPosition);
    }

    public void newrivalPosition(int rolledNo) {             // Bewegt den Gast

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
        rivalPosition = checkFieldtypeClient(rivalPosition);
    }

    private int checkFieldtypeServer(int position) {       // überprüft ob das Feld, welches man Betreten hat, eines der Eventfelder ist
        for (int i = 0; i < 3; i++) {
            if (position == monster[i]) {
                System.out.println("Ohhh Nein! Ein MONSTER greift dich an!");

                if (gameBoard[monster[i]] == "H G") {
                    gameBoard[monster[i]] = "G";
                } else {
                    gameBoard[monster[i]] = "";
                }

                if (type.equalsIgnoreCase("server")) {
                    showDialogMonster();
                } else {
                    showDialogRivalMonster();
                }

                position = monster[i + 3];             // der derzeitige Spieler betritt einen Monsterfeld

                if (gameBoard[monster[i + 3]] == "G") {
                    gameBoard[monster[i + 3]] = "H G";
                } else {
                    gameBoard[monster[i + 3]] = "H";
                }


            } else if (position == portal[i]) {     // Hier wird das Portal ausgeführt

                if (gameBoard[portal[i]] == "H G") {
                    gameBoard[portal[i]] = "G";
                } else {
                    gameBoard[portal[i]] = "";
                }

                if (type.equalsIgnoreCase("server")) {
                    showDialogPortal();
                } else {
                    showDialogRivalPortal();
                }

                position = portal[i + 3];             // der derzeitige Spieler geht durch den Portal

                if (gameBoard[portal[i + 3]] == "G") {
                    gameBoard[portal[i + 3]] = "H G";
                } else {
                    gameBoard[portal[i + 3]] = "H";
                }


            }
        }
        return position;
    }

    private int checkFieldtypeClient(int position) {       // überprüft ob das Feld, welches man Betreten hat, eines der Eventfelder ist
        for (int i = 0; i < 3; i++) {
            if (position == monster[i]) {
                System.out.println("Ohhh Nein! Ein MONSTER greift dich an!");

                if (gameBoard[monster[i]] == "H G") {
                    gameBoard[monster[i]] = "H";
                } else {
                    gameBoard[monster[i]] = "";
                }

                if (type.equalsIgnoreCase("client")) {
                    showDialogMonster();
                } else {
                    showDialogRivalMonster();
                }

                position = monster[i + 3];             // der derzeitige Spieler betritt einen Monsterfeld

                if (gameBoard[monster[i + 3]] == "H") {
                    gameBoard[monster[i + 3]] = "H G";
                } else {
                    gameBoard[monster[i + 3]] = "G";
                }

            } else if (position == portal[i]) {     // Hier wird in der Zukunft die Klasse Portal ausgeführt
                System.out.println("Yeah, du hast einen Portal betreten!");

                if (gameBoard[portal[i]] == "H G") {
                    gameBoard[portal[i]] = "H";
                } else {
                    gameBoard[portal[i]] = "";
                }
                if (type.equalsIgnoreCase("client")) {
                    showDialogPortal();
                } else {
                    showDialogRivalPortal();
                }

                position = portal[i + 3];             // der derzeitige Spieler geht durch den Portal

                if (gameBoard[portal[i + 3]] == "H") {
                    gameBoard[portal[i + 3]] = "H G";
                } else {
                    gameBoard[portal[i + 3]] = "G";
                }

            }
        }
        return position;
    }

    //Um Speicherplatz wieder frei zu geben
    @Override
    protected void onDestroy() {
        mpLaugh.release();
        mpMonster.release();
        mpPortal.release();
        mpPunch.release();
        mpUoh.release();
        mpWoo.release();
        mpYeah.release();
        mpMainGameSound.release();
        mpWinSound.release();
        mpLoseSound.release();
        super.onDestroy();
    }

    private void showDialogMonster() {
        dialog = new Dialog(GameActivity.this);
        dialog.setContentView(R.layout.monster);
        dialog.show();

        //Beim Öffnen des Dialogs Sound zurücksetzen und anschließend abspielen
        mpMonster.seekTo(0);
        mpMonster.start();

        Button portal = (Button) dialog.findViewById(R.id.monsterBtn);
        portal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.cancel();
                mpMonster.pause(); //Sound anhalten bis zum nächsten Aufruf
            }
        });
    }

    private void showDialogRivalMonster() {
        dialog = new Dialog(GameActivity.this);
        dialog.setContentView(R.layout.monster_rival);
        dialog.show();

        //Beim Öffnen des Dialogs Sound abspielen
        mpLaugh.seekTo(0);
        mpLaugh.start();

        Button portal = (Button) dialog.findViewById(R.id.monsterBtn);
        portal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.cancel();
                mpLaugh.pause();
            }
        });
    }

    private void showDialogPortal() {
        dialog = new Dialog(GameActivity.this);
        dialog.setContentView(R.layout.portal);
        dialog.show();

        //Beim Öffnen des Dialogs Sound abspielen
        mpPortal.seekTo(0);
        mpPortal.start();

        Button portal = (Button) dialog.findViewById(R.id.portalBtn);
        portal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.cancel();
                mpPortal.pause();
            }
        });
    }

    private void showDialogRivalPortal() {
        dialog = new Dialog(GameActivity.this);
        dialog.setContentView(R.layout.portal_rival);
        dialog.show();

        //Beim Öffnen des Dialogs Sound abspielen
        mpUoh.seekTo(0);
        mpUoh.start();

        Button portal = (Button) dialog.findViewById(R.id.portalBtn);
        portal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.cancel();
                mpUoh.pause();
            }
        });
    }

    private void showDialogWin() {
        dialog = new Dialog(GameActivity.this);
        dialog.setContentView(R.layout.win);
        dialog.show();

        //Beim Öffnen des Dialogs Sound abspielen
        setTurnSettings();
        mpMainGameSound.stop();
        mpYeah.seekTo(0);
        mpYeah.start();
        mpWoo.seekTo(0);
        mpWoo.start();
        mpWinSound.start();

        Button back = (Button) dialog.findViewById(R.id.backMenuBtnn);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                mpYeah.pause();
                mpWoo.pause();
                endGameConnection();
                startActivity(new Intent(GameActivity.this, EndActivity.class));
            }
        });
    }

    private void showDialogLose() {
        dialog = new Dialog(GameActivity.this);
        dialog.setContentView(R.layout.lose);
        dialog.show();

        //Beim Öffnen des Dialogs Sound abspielen
        setTurnSettings();
        mpMainGameSound.stop();
        mpPunch.seekTo(0);
        mpPunch.start();
        mpLoseSound.start();

        Button back = (Button) dialog.findViewById(R.id.backMenuBtnn);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                mpPunch.pause();
                endGameConnection();
                startActivity(new Intent(GameActivity.this, EndActivity.class));
            }
        });
    }

    private void endConnectionDialogActive() {
        //Für Sarah: Hier Dialog einbinden "Du hast das Spiel beendet..."
        startActivity(new Intent(GameActivity.this, PlayerEndConnection.class));
        endGameConnection();
    }

    private void endConnectionDialogPassive() {
        //Für Sarah: Hier Dialog einbinden "Der Gegner hat das Spiel beendet..."
        startActivity(new Intent(GameActivity.this, OpponentEndConnection.class));
        endGameConnection();
    }

    private void endGameConnection() {
        if (type.equalsIgnoreCase("client")) {
            client.disconnect();
        } else {
            server.stopServer();
        }
    }

    public void drawRiskcardClient() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog.Builder builderfalse = new AlertDialog.Builder(this);

        // Hilfsklasse für Dialogfenster erstellen
        builder.setMessage("Ist die Zahl " + num + " gerade?");

        //für das Weiterbewegen des Spielers
        final DialogInterface.OnClickListener goListenerClient = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        DialogInterface.OnClickListener positivListenerClient = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                do {
                    number = number % 2;
                } while (number > 1);
                if (number == 0) { //dann Zahl gerade
                    builderfalse.setMessage("Juhu, du hast richtig geantwortet!");
                    builderfalse.setPositiveButton("4 Felder vor", goListenerClient);
                    builderfalse.show();
                    sendRiskMessageSuccess();
                } else {
                    sendRiskMessageFail();
                    dialog.dismiss();
                    Toast.makeText(GameActivity.this, "Sorry, leider falsch!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        DialogInterface.OnClickListener negativListenerClient = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                do {
                    number = number % 2;
                } while (number > 1);
                if (number == 1) { //dann Zahl ungerade
                    builderfalse.setMessage("Juhu, du hast richtig geantwortet");
                    builderfalse.setPositiveButton("4 Felder vor", goListenerClient);
                    builderfalse.show();
                    sendRiskMessageSuccess();
                } else {
                    sendRiskMessageFail();
                    dialog.dismiss();
                    Toast.makeText(GameActivity.this, "Sorry, leider falsch!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        builder.setPositiveButton("richtig", positivListenerClient);
        builder.setNegativeButton("falsch", negativListenerClient);
        builder.show();

    }

    public void drawRiskcardServer() {
        AlertDialog.Builder builderServer = new AlertDialog.Builder(this);
        final AlertDialog.Builder builderfalseServer = new AlertDialog.Builder(this);

        // Hilfsklasse für Dialogfenster erstellen
        builderServer.setMessage("Ist die Zahl " + num + " gerade?");

        //für das Weiterbewegen des Spielers
        final DialogInterface.OnClickListener goListenerServer = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        DialogInterface.OnClickListener positivListenerServer = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                do {
                    number = number % 2;
                } while (number > 1);
                if (number == 0) { //dann Zahl gerade
                    builderfalseServer.setMessage("Juhu, du hast richtig geantwortet!");
                    builderfalseServer.setPositiveButton("4 Felder vor", goListenerServer);
                    builderfalseServer.show();
                    sendRiskMessageSuccess();
                } else {
                    sendRiskMessageFail();
                    dialog.dismiss();
                    Toast.makeText(GameActivity.this, "Sorry, leider falsch!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        DialogInterface.OnClickListener negativListenerServer = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                do {
                    number = number % 2;
                } while (number > 1);
                if (number == 1) { //dann Zahl ungerade
                    builderfalseServer.setMessage("Juhu, du hast richtig geantwortet");
                    builderfalseServer.setPositiveButton("4 Felder vor", goListenerServer);
                    builderfalseServer.show();
                    sendRiskMessageSuccess();

                } else {
                    sendRiskMessageFail();
                    dialog.dismiss();
                    Toast.makeText(GameActivity.this, "Sorry, leider falsch!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        builderServer.setPositiveButton("richtig", positivListenerServer);
        builderServer.setNegativeButton("falsch", negativListenerServer);
        builderServer.show();

    }

    public void cheat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog.Builder builderfalse = new AlertDialog.Builder(this);

        // Hilfsklasse für Dialogfenster erstellen

        //cheatclient/server hier als ober if machen, um sicher zu sein, dass das nur bei 0 angezeigt wird?
        builder.setMessage("Willst du deinem Gegner eines auswischen?");

        final DialogInterface.OnClickListener goListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendRiskMessageFailCheat();
                dialog.dismiss();
            }
        };

        DialogInterface.OnClickListener positivListenerClient = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendRiskMessageSuccessCheat();
                dialog.dismiss();
            }
        };
        builder.setPositiveButton("schummeln", positivListenerClient);
        builder.setNegativeButton("jetzt nicht", goListener);
        builder.show();

    }

    public void turn(int num) {
        if (type.equalsIgnoreCase("client")) {
            if (riskClient.isCheckFieldClient() == 0) {
                setTurnSettings();
                checkProgressDialog();
                isRisk = 1;
                startRunnableClient();
            } else {
                progressDialog.dismiss();
                if (riskClient.getFailCounterClient() == 0) {
                    newUserPosition(num);
                    checkBoard();
                    Toast.makeText(GameActivity.this, "Der Gegner hat die Frage richtig beantwortet!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GameActivity.this, "Der Gegner konnte die Frage nicht beantworten!", Toast.LENGTH_SHORT).show();
                }
                resetRiskValues();
                isShown = 0;
                resetTurnSettings();
            }
        } else {
            if (riskServer.isCheckField() == 0) {
                setTurnSettings();
                checkProgressDialog();
                isRisk = 1;
                startRunnableServer();
            } else {
                progressDialog.dismiss();
                if (riskServer.getFailCounterServer() == 0) {
                    newrivalPosition(num);
                    checkBoard();
                    Toast.makeText(GameActivity.this, "Der Gegner hat die Frage richtig beantwortet!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GameActivity.this, "Der Gegner konnte die Frage nicht beantworten!", Toast.LENGTH_SHORT).show();
                }
                resetRiskValues();
                isShown = 0;
                resetTurnSettings();
            }

        }
    }

    public void sendRiskMessageSuccessCheat() {
        String decision = "successcheat";
        if (type.equalsIgnoreCase("client")) {
            newrivalPosition(6);
            checkBoard();
            new CheckCheatClient(decision, client).execute();
            new MessageClient(6, client).execute();
        } else {
            newUserPosition(6);
            checkBoard();
            new CheckCheatServer(decision, server).execute();
            new MessageServer(6, server).execute();
        }
    }

    public void sendRiskMessageFailCheat() {
        String decision = "failcheat";
        if (type.equalsIgnoreCase("client")) {
            new CheckCheatClient(decision, client).execute();
        } else {
            new CheckCheatServer(decision, server).execute();
        }
    }

    public void sendRiskMessageSuccess() {
        String decision = "success";
        if (type.equalsIgnoreCase("client")) {
            newrivalPosition(4);
            checkBoard();
            new CheckRiskClient(decision, client).execute();
            new MessageClient(4, client).execute();
        } else {
            newUserPosition(4);
            checkBoard();
            new CheckRiskServer(decision, server).execute();
            new MessageServer(4, server).execute();
        }
    }

    public void sendRiskMessageFail() {
        String decision = "fail";
        if (type.equalsIgnoreCase("client")) {
            new CheckRiskClient(decision, client).execute();
        } else {
            new CheckRiskServer(decision, server).execute();
        }
    }

    public void checkBoard() {                                           //checkt wo Host & Gast sich gerade befinden

        for (int i = 0; i < 47; i++) {

            for (int j = 0; j < 3; j++) {                                // Schleife soll verhindern, dass die Eventfelder neue Zeichnungen bekommen
                if (i == monster[j] || i == portal[j]) {
                    i++;
                }
            }

            if (gameBoard[i] == "H") {
                if (i == monster[3] || i == monster[4] || i == monster[5]) {
                    buttons[i].setImageResource(R.drawable.gras_m_h);
                } else if (i == portal[3] || i == portal[4] || i == portal[5]) {
                    buttons[i].setImageResource(R.drawable.gras_p_h);
                } else buttons[i].setImageResource(R.drawable.player_host);
            } else if (gameBoard[i] == "G") {
                if (i == monster[3] || i == monster[4] || i == monster[5]) {
                    buttons[i].setImageResource(R.drawable.gras_m_g);
                } else if (i == portal[3] || i == portal[4] || i == portal[5]) {
                    buttons[i].setImageResource(R.drawable.gras_p_g);
                } else buttons[i].setImageResource(R.drawable.player_guest);
            } else if (gameBoard[i] == "H G") {
                if (i == monster[3] || i == monster[4] || i == monster[5]) {
                    buttons[i].setImageResource(R.drawable.gras_m_b);
                } else if (i == portal[3] || i == portal[4] || i == portal[5]) {
                    buttons[i].setImageResource(R.drawable.gras_p_b);
                } else buttons[i].setImageResource(R.drawable.player_both);
            } else if (gameBoard[i] == "") {
                if (i == monster[3] || i == monster[4] || i == monster[5]) {
                    buttons[i].setImageResource(R.drawable.gras_m);
                } else if (i == portal[3] || i == portal[4] || i == portal[5]) {
                    buttons[i].setImageResource(R.drawable.gras_p);
                } else
                    for (int j = 0; j < grasImg.length; j++) {                    // nimmt wieder die Ursprüngliche Grasform an
                        if (gras[i] == (j + 1)) {
                            buttons[i].setImageResource(grasImg[j]);
                            j = grasImg.length;
                        }
                    }
                if (i == 47) {
                    buttons[i].setImageResource(R.drawable.end_field);             // das letzte Feld behält die Zeichnung
                }

            }
        }
    }

    public void setBoard() {                                             //Die buttons aus dem Layout werden ins code übernommen

        buttons = new ImageView[48];


        Resources res = getResources();                                 //if you are in an activity
        for (int i = 0; i < 48; i++) {
            String idName = "f" + i;
            buttons[i] = (ImageView) findViewById(res.getIdentifier(idName, "id", getPackageName()));
        }


        for (int i = 0; i < 3; i++) {
            buttons[monster[i]].setImageResource(R.drawable.m_field);              // Monsterfelder
            buttons[portal[i]].setImageResource(R.drawable.p_field);               // Portalfelder

            buttons[monster[i + 3]].setImageResource(R.drawable.gras_m);             // Monsterfeld Ausgänge
            buttons[portal[i + 3]].setImageResource(R.drawable.gras_p);              // Portalfeld Ausgänge
        }


        for (int i = 0; i < gameBoard.length; i++) {                    // Board Array wird gecleart
            gameBoard[i] = "";
        }

        gameBoard[0] = "H G";                                           // Spieler werden auf den ersten Feld gesetzt
        buttons[0].setImageResource(R.drawable.player_both);

        buttons[47].setImageResource(R.drawable.end_field);            // letzter Feld bekommt eine Zeichnung

        for (int i = 0; i < buttons.length; i++) {                       // Das Array, welches für die jeweiligen Gras-Felder zuständig ist,
            for (int j = 0; j < 3; j++) {                                 // bekommt die Werte 1-4 zugeteilt für die Jeweiligen Felder
                if (i != monster[j] || i != portal[j] || i != monster[j + 3] || i != portal[j + 3]) {
                    gras[i] = (int) (Math.random() * 4) + 1;
                }
            }
        }

        checkBoard();                                                   // Images für die Felder laden
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.serverClose:
                new ServerEndConnection(server).execute();
                endConnectionDialogActive();
                break;

            case R.id.disconnect:
                new ClientEndConnection(client).execute();
                endConnectionDialogActive();
                break;

            case R.id.btnCheatServer:
                Toast.makeText(GameActivity.this, "Glückwunsch, du hast den Gegner beim Schummeln erwischt!", Toast.LENGTH_SHORT).show();
                new ServerDetectCheat(cheatServer, server).execute();
                break;

            case R.id.btnCheatClient:
                Toast.makeText(GameActivity.this, "Glückwunsch, du hast den Gegner beim Schummeln erwischt!", Toast.LENGTH_SHORT).show();
                new ClientDetectCheat(cheatClient, client).execute();
                break;

            default:
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        long curTime = System.currentTimeMillis();

        if ((curTime - lastUpdate) > 100) {

            if (calculateSensor(x, y, z, last_x, last_y, last_z, curTime) > SHAKE_THRESHOLD) {

                if (type.equalsIgnoreCase("Client")) {
                    if (updateClient.getActiveSensorClient() == 1) {
                        gameTurnClient();
                        updateClient.setActiveSensorClient(0);
                    } else {
                        if (calculateSensor(x, y, z, last_x, last_y, last_z, curTime) > 2000) {
                            Toast.makeText(GameActivity.this, "Server noch am Zug, warten mit Schuetteln!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                if (type.equalsIgnoreCase("Server")) {
                    if (updateServer.getActiveSensorServer() == 1) {
                        gameTurnServer();
                        updateServer.setActiveSensorServer(0);
                    } else {
                        if (calculateSensor(x, y, z, last_x, last_y, last_z, curTime) > 2000) {
                            Toast.makeText(GameActivity.this, "Client noch am Zug, warten mit Schuetteln!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    protected float calculateSensor(float x, float y, float z, float last_x, float last_y, float last_z, long curTime) {

        long diffTime = (curTime - lastUpdate);
        lastUpdate = curTime;

        return Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
    }

    protected int rollDice() {
        Random random_d = new Random();
        rolledNumber = random_d.nextInt(6) + 1;
        return rolledNumber;
    }

    private void setDiceServer() {
        int diceImageServer = getResources().getIdentifier("d" + rolledNumber, "drawable", "t_industries.monstersandportals");
        rollServer.setImageResource(diceImageServer);
    }

    private void setDiceClient() {
        int diceImageClient = getResources().getIdentifier("d" + rolledNumber, "drawable", "t_industries.monstersandportals");
        rollClient.setImageResource(diceImageClient);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //wird nicht gebraucht
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
            Sensor sensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            sensorManager.registerListener(this, sensor, 1000000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private class MPSounds extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            createMPSounds();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}
