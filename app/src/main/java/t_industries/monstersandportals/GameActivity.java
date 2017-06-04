package t_industries.monstersandportals;

import android.app.Activity;

import android.app.Dialog;

import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Random;

import t_industries.monstersandportals.NetworkClasses.RiskClient;
import t_industries.monstersandportals.NetworkClasses.RiskServer;
import t_industries.monstersandportals.NetworkClasses.UpdateClient;
import t_industries.monstersandportals.NetworkClasses.UpdateServer;
import t_industries.monstersandportals.myclient.MyClient;
import t_industries.monstersandportals.myserver.MyServer;

import static android.R.id.icon;
import static java.lang.String.valueOf;

/**
 * Created by micha on 22.04.2017.
 */

public class GameActivity extends Activity implements Serializable, View.OnClickListener, SensorEventListener {
    private TextView tvServerName, tvClientName;
    private Button closeServer, disconnect;
    int rolledNumber;
    Handler handler;
    MyServer server;
    MyClient client;
    UpdateClient updateClient;
    UpdateServer updateServer;
    RiskServer riskServer; //für die Kommunikation
    RiskClient riskClient;
    private SensorManager sensorManager;
    private Sensor sensor;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 800;




    static String[] gameBoard = new String[48]; // 8 x 6 Spielfeld
    static int[] monster = {12, 31, 46, 5, 25, 19};
    static int[] portal = {7, 22, 33, 16, 28, 41};
    static int[] risk = {10, 26, 40};

    static int userPosition = 0;
    static int rivalPosition = 0;

    ImageButton [] buttons;
    // Button roll;

    ImageView rollClient;
    ImageView rollServer;

    Random random = new Random();
    private int number = (random.nextInt(10) + 1);
    //Randomzahl zwischen 1 und 10
    private String num = valueOf(number);
    private int isRisk = 0; //wenn richtig, den Player 4 Positionen vor schicken
    private int isActiveOrderServer = 0;
    private int isActiveOrderClient = 0;
    private String type;
    //Dialoge für Monster,Portale,Gewonnen,Verloren
    Dialog dialog;
    //neue Alternative für die Sounds,Initialisiere MediaPlayer zur Verwaltung von Audiodateien
    private MediaPlayer mpLaugh, mpMonster, mpPortal, mpPunch, mpUoh, mpWoo, mpYeah;


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
        rollClient = (ImageView) findViewById(R.id.rollClient);
        rollServer = (ImageView) findViewById(R.id.rollServer);
        updateServer = new UpdateServer();
        updateClient = new UpdateClient();
        riskServer = new RiskServer();
        riskClient = new RiskClient();
        handler = new Handler();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, 1000000);


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
                new MyTaskServer().execute();
                closeServer.setVisibility(View.VISIBLE);
                closeServer.setOnClickListener(this);
                setBoard();
                rollServer.setVisibility(View.VISIBLE);
                startRunnableGameOrderServer();
                new MPSounds().execute();

            } else if (type.equalsIgnoreCase("client")) {
                String ip = bundle.getString("ip");
                client = new MyClient(55557, 55558, 5000);
                new MyTaskClient(ip).execute();
                disconnect.setVisibility(View.VISIBLE);
                disconnect.setOnClickListener(this);
                setBoard();
                rollClient.setVisibility(View.VISIBLE);
                startRunnableGameOrderClient();
                new MPSounds().execute();
            }

        } else {
            Toast.makeText(GameActivity.this, "Keine Daten vorhanden!", Toast.LENGTH_LONG).show();
        }

    }
    //Spunds werden in den Variablen gespeichert
    private void createMPSounds(){
        mpLaugh = MediaPlayer.create(this, R.raw.laugh);
        mpMonster = MediaPlayer.create(this, R.raw.monster2);
        mpPortal = MediaPlayer.create(this, R.raw.portal);
        mpPunch = MediaPlayer.create(this, R.raw.punch);
        mpUoh = MediaPlayer.create(this, R.raw.uoh);
        mpWoo = MediaPlayer.create(this, R.raw.woo);
        mpYeah = MediaPlayer.create(this, R.raw.yeah);
    }

    private void setToastMessageStart(){
        if(type.equalsIgnoreCase("client")){
            if(updateClient.getReadyForTurnClient() == 0){
                Toast.makeText(GameActivity.this, "Schade, der Gegner beginnt!", Toast.LENGTH_SHORT).show();
                startRunnableClient();
            } else if(updateClient.getReadyForTurnClient() == 1){
                Toast.makeText(GameActivity.this, "Glückwunsch, du darfst beginnen!", Toast.LENGTH_SHORT).show();
                gameHandlerClient();
            }
        } else {
            if(updateServer.getReadyForTurnServer() == 0){
                Toast.makeText(GameActivity.this, "Schade, der Gegner beginnt!", Toast.LENGTH_SHORT).show();
                startRunnableServer();
            } else if(updateServer.getReadyForTurnServer() == 1){
                Toast.makeText(GameActivity.this, "Glückwunsch, du darfst beginnen!", Toast.LENGTH_SHORT).show();
                gameHandlerServer();
            }
        }
    }

    private void startRunnableGameOrderServer(){
        handler.postDelayed(runnableOrderServer, 1000);
    }

    private Runnable runnableOrderServer = new Runnable() {
        @Override
        public void run() {
            if(updateServer.getCheckRandomNrServer() == 0){
                if(isActiveOrderServer == 0){
                    new randomStartServer().execute();
                    isActiveOrderServer = 1;
                }
                startRunnableGameOrderServer();
            } else {
                setToastMessageStart();
            }
        }
    };

    private void startRunnableGameOrderClient(){
        handler.postDelayed(runnableOrderClient, 1000);
    }

    private Runnable runnableOrderClient = new Runnable() {
        @Override
        public void run() {
            if(updateClient.getCheckRandomNrClient() == 0){
                if(isActiveOrderClient == 0){
                    new randomStartClient(updateClient).execute();
                    isActiveOrderClient = 1;
                }
                startRunnableGameOrderClient();
            } else {
                setToastMessageStart();
            }
        }
    };

    private void startRunnableServer() {
        handler.postDelayed(runnableServer, 1000);
    }

    private Runnable runnableServer = new Runnable() {
        public void run() {
            int getReady = updateServer.getReadyForTurnServer();
            int rolledNrRival = updateServer.getPosition();
            if (getReady == 1) {
                if(isRisk == 0){
                    newrivalPosition(rolledNrRival);
                    checkBoard();
                    Toast.makeText(GameActivity.this, "Client würfelte: " + rolledNrRival + ". Du bist am Zug!", Toast.LENGTH_SHORT).show();
                }
                if(rivalPosition == 10 || rivalPosition == 26 || rivalPosition == 40){
                    if(riskServer.isCheckField() == 0){
                        updateServer.setActiveSensorServer(0);
                        rollServer.setVisibility(View.INVISIBLE);
                        Toast.makeText(GameActivity.this, "Bitte warten, der Gegner ist noch beim Beantworten!", Toast.LENGTH_SHORT).show();
                        isRisk = 1;
                        startRunnableServer();
                    } else {
                        if(riskServer.getFailCounterServer() == 0){
                            newrivalPosition(4);
                            checkBoard();
                            Toast.makeText(GameActivity.this, "Der Gegner hat die Frage richtig beantwortet und darf 4 Felder vorwärts!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GameActivity.this, "Der Gegner ist zu dumm, um die Frage zu beantworten!", Toast.LENGTH_SHORT).show();
                        }
                        rollServer.setVisibility(View.VISIBLE);
                        resetRiskValues();
                        updateServer.setActiveSensorServer(1);
                    }
                }

                if (rivalPosition == 47) {
                    showDialogLose();
                }

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
                if(isRisk == 0){
                    newUserPosition(rolledNrRival);
                    checkBoard();
                    Toast.makeText(GameActivity.this, "Server würfelte: " + rolledNrRival + ". Du bist am Zug!", Toast.LENGTH_SHORT).show();
                }
                if(userPosition == 10 || userPosition == 26 || userPosition == 40){
                    if(riskClient.isCheckFieldClient() == 0){
                        updateClient.setActiveSensorClient(0);
                        rollClient.setVisibility(View.INVISIBLE);
                        Toast.makeText(GameActivity.this, "Bitte warten, der Gegner ist noch beim Beantworten!", Toast.LENGTH_SHORT).show();
                        isRisk = 1;
                        startRunnableClient();
                    } else{
                        if(riskClient.getFailCounterClient() == 0){
                            newUserPosition(4);
                            checkBoard();
                            Toast.makeText(GameActivity.this, "Der Gegner hat die Frage richtig beantwortet und darf 4 Felder vorwärts!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(GameActivity.this, "Der Gegner ist zu dumm, um die Frage zu beantworten!", Toast.LENGTH_SHORT).show();
                        }
                        rollClient.setVisibility(View.VISIBLE);
                        resetRiskValues();
                        updateClient.setActiveSensorClient(1);
                    }
                }

                if (userPosition == 47) {
                    showDialogLose();
                }

                gameHandlerClient();
            } else {
                startRunnableClient();
            }
        }
    };

    private void resetRiskValues(){
        isRisk = 0;
        if(type.equalsIgnoreCase("client")){
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

    public void gameTurnServer(){
        if (updateServer.getReadyForTurnServer() == 0) {
            Toast.makeText(GameActivity.this, "Bitte warten, der Client ist noch am Zug.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPosition <= 47 && rivalPosition <= 47) {
            int rolledNo = rollDice();
            setDiceServer(rolledNo);
            System.out.println("Host zieht weiter:");
            //if(newUserPosition(rolledNo) == 20)
            new MessageServer(rolledNo).execute();
            System.out.println("Client ist dran:");
            newUserPosition(rolledNo);
            checkBoard();

            if (userPosition == 10 || userPosition == 26 || userPosition == 40) {
                drawRiskcardServer();
            }
            new ACKServer(updateServer).execute();
            startRunnableServer();
        }

        if (userPosition == 47) {
            showDialogWin();
        }
    }

    public void gameTurnClient(){
        if (updateClient.getReadyForTurnClient() == 0) {
            Toast.makeText(GameActivity.this, "Bitte warten, der Server ist noch am Zug.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPosition <= 47 && rivalPosition <= 47) {
            int rolledNo = rollDice();
            setDiceClient(rolledNo);
            System.out.println("Client zieht weiter:");
            new MessageClient(rolledNo).execute();
            System.out.println("Host ist dran:");
            newrivalPosition(rolledNo);
            checkBoard();

            if (rivalPosition == 10 || rivalPosition == 26 || rivalPosition == 40) {
                drawRiskcardClient();
            }
            new ACKClient(updateClient).execute();
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
        userPosition = checkMonsterOrPortalOrRiskServer(userPosition);
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
        rivalPosition = checkMonsterOrPortalOrRiskClient(rivalPosition);
    }

    private int checkMonsterOrPortalOrRiskServer(int position) {       // überprüft ob das Feld, welches man Betreten hat, eines der Eventfelder ist
        for ( int i = 0; i < 3; i++) {
            if (position == monster[i]) {
                System.out.println("Ohhh Nein! Ein MONSTER greift dich an!");

                if (gameBoard[monster[i]] == "H G"){
                    gameBoard[monster[i]] = "G";
                } else {
                    gameBoard[monster[i]] = ""; }

                if (type.equalsIgnoreCase("server")){
                    showDialogMonster();
                }else{
                    showDialogRivalMonster();
                }

                position = monster[i+3];             // der derzeitige Spieler betritt einen Monsterfeld

                if (gameBoard[monster[i+3]] == "G"){
                    gameBoard[monster[i+3]] = "H G";
                } else {
                    gameBoard[monster[i+3]] = "H"; }


            } else if (position == portal[i]) {     // Hier wird in der Zukunft die Klasse Portal ausgeführt

                if (gameBoard[portal[i]] == "H G"){
                    gameBoard[portal[i]] = "G";
                } else {
                    gameBoard[portal[i]] = ""; }

                if (type.equalsIgnoreCase("server")){
                    showDialogPortal();
                }else{
                    showDialogRivalPortal();
                }

                position = portal[i+3];             // der derzeitige Spieler geht durch den Portal

                if (gameBoard[portal[i+3]] == "G"){
                    gameBoard[portal[i+3]] = "H G";
                } else {
                    gameBoard[portal[i+3]] = "H"; }


            }
        }
        return position;
    }

    private int checkMonsterOrPortalOrRiskClient(int position) {       // überprüft ob das Feld, welches man Betreten hat, eines der Eventfelder ist
        for ( int i = 0; i < 3; i++) {
            if (position == monster[i]) {
                System.out.println("Ohhh Nein! Ein MONSTER greift dich an!");

                if ( gameBoard[monster[i]] == "H G"){
                    gameBoard[monster[i]] = "H";
                } else {
                    gameBoard[monster[i]] = "";
                }

                if (type.equalsIgnoreCase("client")){
                    showDialogMonster();
                }else{
                    showDialogRivalMonster();
                }

                position = monster[i+3];             // der derzeitige Spieler betritt einen Monsterfeld

                if ( gameBoard[monster[i+3]] == "H"){
                    gameBoard[monster[i+3]] = "H G";
                } else {
                    gameBoard[monster[i+3]] = "G";
                }

            } else if (position == portal[i]) {     // Hier wird in der Zukunft die Klasse Portal ausgeführt

                if ( gameBoard[portal[i]] == "H G"){
                    gameBoard[portal[i]] = "H";
                } else {
                    gameBoard[portal[i]] = "";
                }
                if (type.equalsIgnoreCase("client")){
                    showDialogPortal();
                }else{
                    showDialogRivalPortal();
                }

                position = portal[i+3];             // der derzeitige Spieler geht durch den Portal

                if ( gameBoard[portal[i+3]] == "H"){
                    gameBoard[portal[i+3]] = "H G";
                } else {
                    gameBoard[portal[i+3]] = "G";
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
        super.onDestroy();
    }

    private void showDialogMonster(){
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

    private void showDialogRivalMonster(){
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

    private void showDialogPortal(){
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

    private void showDialogRivalPortal(){
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

    private void showDialogWin(){
        dialog = new Dialog(GameActivity.this);
        dialog.setContentView(R.layout.win);
        dialog.show();

        //Beim Öffnen des Dialogs Sound abspielen
        mpYeah.seekTo(0);
        mpYeah.start();
        mpWoo.seekTo(0);
        mpWoo.start();

        Button back = (Button) dialog.findViewById(R.id.backBtn);
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

    private void showDialogLose(){
        dialog = new Dialog(GameActivity.this);
        dialog.setContentView(R.layout.lose);
        dialog.show();

        //Beim Öffnen des Dialogs Sound abspielen
        mpPunch.seekTo(0);
        mpPunch.start();

        Button back = (Button) dialog.findViewById(R.id.backBtn);
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

    private void endGameConnection(){
        if(type.equalsIgnoreCase("client")){
            client.disconnect();
        } else {
            server.stopServer();
        }
    }



    public void drawRiskcardClient(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog.Builder builderfalse = new AlertDialog.Builder(this);

        // Hilfsklasse für Dialogfenster erstellen
        builder.setMessage("Ist die Zahl "+num+ " gerade?");

        //für das Weiterbewegen des Spielers
        final DialogInterface.OnClickListener goListenerClient = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        DialogInterface.OnClickListener positivListenerClient = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                do {
                    number = number % 2;
                }while(number>1);
                if(number==0){ //dann Zahl gerade
                    builderfalse.setMessage("Juhu, du hast richtig geantwortet!");
                    builderfalse.setPositiveButton("4 Felder vor", goListenerClient);
                    builderfalse.show();
                    sendRiskMessageSuccess();
                }else{
                    sendRiskMessageFail();
                    dialog.dismiss();
                    Toast.makeText(GameActivity.this, "Sorry, leider falsch!", Toast.LENGTH_SHORT).show();                }
            }
        };

        DialogInterface.OnClickListener negativListenerClient = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                do {
                    number = number % 2;
                }while(number>1);
                if(number==1){ //dann Zahl ungerade
                    builderfalse.setMessage("Juhu, du hast richtig geantwortet");
                    builderfalse.setPositiveButton("4 Felder vor", goListenerClient);
                    builderfalse.show();
                    sendRiskMessageSuccess();
                }else{
                    sendRiskMessageFail();
                    dialog.dismiss();
                    Toast.makeText(GameActivity.this, "Sorry, leider falsch!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        builder.setPositiveButton("richtig",positivListenerClient);
        builder.setNegativeButton("falsch", negativListenerClient);
        builder.show();

    }

    public void drawRiskcardServer(){
        AlertDialog.Builder builderServer = new AlertDialog.Builder(this);
        final AlertDialog.Builder builderfalseServer = new AlertDialog.Builder(this);

        // Hilfsklasse für Dialogfenster erstellen
        builderServer.setMessage("Ist die Zahl "+num+ " gerade?");

        //für das Weiterbewegen des Spielers
        final DialogInterface.OnClickListener goListenerServer = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        DialogInterface.OnClickListener positivListenerServer = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                do {
                    number = number % 2;
                }while(number>1);
                if(number==0){ //dann Zahl gerade
                    builderfalseServer.setMessage("Juhu, du hast richtig geantwortet!");
                    builderfalseServer.setPositiveButton("4 Felder vor", goListenerServer);
                    builderfalseServer.show();
                    sendRiskMessageSuccess();
                }else{
                    sendRiskMessageFail();
                    dialog.dismiss();
                    Toast.makeText(GameActivity.this, "Sorry, leider falsch!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        DialogInterface.OnClickListener negativListenerServer = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                do {
                    number = number % 2;
                }while(number>1);
                if(number==1){ //dann Zahl ungerade
                    builderfalseServer.setMessage("Juhu, du hast richtig geantwortet");
                    builderfalseServer.setPositiveButton("4 Felder vor", goListenerServer);
                    builderfalseServer.show();
                    sendRiskMessageSuccess();

                }else{
                    sendRiskMessageFail();
                    dialog.dismiss();
                    Toast.makeText(GameActivity.this, "Sorry, leider falsch!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        builderServer.setPositiveButton("richtig",positivListenerServer);
        builderServer.setNegativeButton("falsch", negativListenerServer);
        builderServer.show();

    }

    public void sendRiskMessageSuccess(){
        String decision = "success";
        if(type.equalsIgnoreCase("client")){
            newrivalPosition(4);
            checkBoard();
            new CheckRiskClient(decision).execute();
            new MessageClient(4).execute();
        } else{
            newUserPosition(4);
            checkBoard();
            new CheckRiskServer(decision).execute();
            new MessageServer(4).execute();
        }
    }

    public void sendRiskMessageFail(){
        String decision = "fail";
        if(type.equalsIgnoreCase("client")){
            new CheckRiskClient(decision).execute();
        } else{
            new CheckRiskServer(decision).execute();
        }
    }

    public void checkBoard(){                                           //checkt wo Host & Gast sich gerade befinden

        for (int i = 0; i < 48; i++) {

            for ( int j = 0; j <3; j++){                                // soll verhindern, dass die Eventfelder neue Zeichnungen bekommen
                if ( i == monster[j] || i == portal[j] || i == risk[j] || i == 47){
                    i++;
                }
            }

            if (gameBoard[i] == "H" ){
                buttons[i].setImageResource(R.drawable. player_host);
            } else if (gameBoard[i] == "G") {
                buttons[i].setImageResource(R.drawable. player_guest);
            } else if  (gameBoard[i] == "H G") {
                buttons[i].setImageResource(R.drawable. player_both);
            } else if (gameBoard[i] == "") {
                buttons[i].setImageResource(R.drawable. grass);
            }
        }
    }

    public void setBoard(){                                             //Die buttons aus dem Layout werden ins code übernommen

        buttons = new ImageButton[48];

        Resources res = getResources();                                 //if you are in an activity
        for (int i = 0; i < 48; i++) {
            String idName = "f" + i;
            buttons[i] = (ImageButton) findViewById(res.getIdentifier(idName, "id", getPackageName()));
        }


        /*roll = (Button) findViewById(R.id.roll);
        roll.setText("Würfel");*/

        for (int i = 0; i < 3; i++) {
            // buttons[monster[i]].setBackgroundColor(Color.RED);          // Monsterfelder
            buttons[0].setImageResource(R.drawable. m_field);

            // buttons[risk[i]].setBackgroundColor(Color.GREEN);           // Eventfelder

            // buttons[portal[i]].setBackgroundColor(Color.BLUE);          // Portalfelder
            buttons[0].setImageResource(R.drawable. p_field);

            // buttons[monster[i+3]].setBackgroundColor(Color.DKGRAY);     // Monsterfelder Ausgang
            // buttons[portal[i+3]].setBackgroundColor(Color.CYAN);        // Portalfelder Ausgang

        }

        for (int i = 0; i < gameBoard.length; i++ ){                    // Board Array wird gecleart
            gameBoard[i] = "";
        }

        gameBoard[0] = "H G";                                           // Spieler werden auf den ersten Feld gesetzt
        buttons[0].setImageResource(R.drawable. player_both);

        buttons[47].setImageResource(R.drawable. end_field);            // letzter Feld bekommt eine Zeichnung

        checkBoard();                                                   // Images für die Felder laden
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

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        long curTime = System.currentTimeMillis();

        if ((curTime - lastUpdate) > 100) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {

                if (type.equalsIgnoreCase("Client")) {
                    if(updateClient.getActiveSensorClient() == 1){
                        gameTurnClient();
                        updateClient.setActiveSensorClient(0);
                    } else {
                        if (speed > 2000) {
                            Toast.makeText(GameActivity.this, "Server noch am Zug, warten mit Schuetteln!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                if (type.equalsIgnoreCase("Server")) {
                    if(updateServer.getActiveSensorServer() == 1){
                        gameTurnServer();
                        updateServer.setActiveSensorServer(0);
                    } else {
                        if (speed > 2000) {
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


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume(){
        super.onResume();

        if (sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size()!=0){
            Sensor sensor = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            sensorManager.registerListener(this, sensor, 1000000);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private class MyTaskServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            server.startServerNew(updateServer, riskServer);
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
            client.connectNew(this.ip, updateClient, riskClient);
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

        public MessageClient(int roll) {
            this.rolledNr = roll;
        }

        @Override
        protected Void doInBackground(Void... params) {
            client.sendPosition(this.rolledNr);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private class MessageServer extends AsyncTask<Void, Void, Void> {
        int rolledNr;

        public MessageServer(int roll) {
            this.rolledNr = roll;
        }

        @Override
        protected Void doInBackground(Void... params) {
            server.sendPosition(this.rolledNr);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private class ACKServer extends AsyncTask<Void, Void, Void> {
        UpdateServer updateServer;

        public ACKServer(UpdateServer updateServer) {
            this.updateServer = updateServer;
        }

        @Override
        protected Void doInBackground(Void... params) {
            server.sendACK(this.updateServer);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private class ACKClient extends AsyncTask<Void, Void, Void> {
        UpdateClient updateClient;

        public ACKClient(UpdateClient updateClient) {
            this.updateClient = updateClient;
        }

        @Override
        protected Void doInBackground(Void... params) {
            client.sendACK(this.updateClient);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private class CheckRiskClient extends AsyncTask<Void, Void, Void> {
        String decision;
        public CheckRiskClient(String decision) {
            this.decision = decision;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(decision.equalsIgnoreCase("fail")){
                client.sendRiskFail();
            } else {
                client.sendRiskField();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private class CheckRiskServer extends AsyncTask<Void, Void, Void> {
        String decision;
        public CheckRiskServer(String decision) {
            this.decision = decision;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(decision.equalsIgnoreCase("fail")){
                server.sendRiskFail();
            } else {
                server.sendRiskField();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private class randomStartClient extends AsyncTask<Void, Void, Void> {
        UpdateClient updateClient;
        public randomStartClient(UpdateClient updateClient) {
            this.updateClient = updateClient;
        }

        @Override
        protected Void doInBackground(Void... params) {
            client.sendRandomNumber(this.updateClient);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    private class randomStartServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            server.sendACKRandom();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
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
