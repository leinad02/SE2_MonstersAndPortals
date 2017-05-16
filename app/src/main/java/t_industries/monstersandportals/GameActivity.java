package t_industries.monstersandportals;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
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

public class GameActivity extends Activity implements Serializable, View.OnClickListener {
    private TextView tvServerName, tvClientName;
    private Button closeServer, disconnect;
    int rolledNumber;
    Handler handler;
    MyServer server;
    MyClient client;
    UpdateClient updateClient;
    UpdateServer updateServer;
    RiskServer riskServer;
    RiskClient riskClient;


    static String[] gameBoard = new String[48]; // 8 x 6 Spielfeld
    static int[] monster = {12, 31, 46, 5, 25, 19};
    static int[] portal = {7, 22, 33, 16, 28, 41};
    static int[] risk = {10, 26, 40};

    static int userPosition = 0;
    static int rivalPosition = 0;

    Button [] buttons;
    Button roll;

    ImageView rollClient;
    ImageView rollServer;

    Random random = new Random();
    private int number = (random.nextInt(10) + 1);
    //Randomzahl zwischen 1 und 10
    private String num = valueOf(number);
    private int isRisk = 0; //wenn richtig, den Player 4 Positionen vor schicken
    private String type;

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
                if(isRisk == 0){
                    newrivalPosition(rolledNrRival);
                    checkBoard();
                    Toast.makeText(GameActivity.this, "Client würfelte: " + rolledNrRival + ". Du bist am Zug!", Toast.LENGTH_SHORT).show();
                }
                if(rivalPosition == 10 || rivalPosition == 26 || rivalPosition == 40){
                    if(riskServer.isCheckField() == 0){
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
                    }
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
                    }
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
                if (updateClient.getReadyForTurnClient() == 0) {
                    Toast.makeText(GameActivity.this, "Bitte warten, der Server ist noch am Zug.", Toast.LENGTH_LONG).show();
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

                    if(rivalPosition == 10 || rivalPosition == 26 || rivalPosition == 40){
                        drawRiskcardClient();
                    }
                    new ACKClient(updateClient).execute();
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
                    new MessageServer(rolledNo).execute();
                    System.out.println("Client ist dran:");
                    newUserPosition(rolledNo);
                    checkBoard();

                    if(userPosition == 10 || userPosition == 26 || userPosition == 40){
                        drawRiskcardServer();
                    }
                    new ACKServer(updateServer).execute();
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


        /*roll = (Button) findViewById(R.id.roll);
        roll.setText("Würfel");*/

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
