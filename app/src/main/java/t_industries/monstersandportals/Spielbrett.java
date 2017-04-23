package t_industries.monstersandportals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Spielbrett extends AppCompatActivity{

    static int turn = 0;
    static String[] gameBoard = new String[48]; // 8 x 6 Spielfeld
    static Map<Integer, Integer> portalPositionValue;
    static Map<Integer, Integer> monsterPositionValue;
    static Map<Integer, Integer> riskPositionValue;
    static int userPosition = 0;
    static int rivalPosition = 0;




    Button  f0, f1, f2, f3, f4, f5, f6, f7, f8, f9,
            f10, f11, f12, f13, f14, f15, f16, f17, f18, f19,
            f20,  f21, f22, f23, f24, f25, f26, f27, f28, f29,
            f30,  f31, f32, f33, f34, f35, f36, f37, f38, f39,
            f40,  f41, f42, f43, f44, f45, f46, f47, roll;


    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spielbrett);

        setBoard();



        // dieser OnClickListener ist nur zum Testen da!

        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( userPosition != 47 && rivalPosition != 47) {

                    if (userTurn()) {
                        System.out.println("Du bist dran:");
                        int rolledNo = 3;
                        System.out.println("Ziehe weiter:");
                        newUserPosition(rolledNo);
                        checkBoard();
                    } else {
                        System.out.println("Dein Gegner ist dran:");
                        int rolledNo = 2;
                        System.out.println("Dein Gegner zieht weiter:");
                        newrivalPosition(rolledNo);
                        checkBoard();
                    }
                }

                if (userPosition == 47) {
                    Toast.makeText(Spielbrett.this, "Winner is the HOST!", Toast.LENGTH_LONG).show();
                } else if (rivalPosition == 47) {
                    Toast.makeText(Spielbrett.this, "Winner is the GUEST!", Toast.LENGTH_LONG).show();
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
                portalPositionValue.put((int) (Math.random() * 100) + 1, (int) (Math.random() * 10) + 5);
            } else {
                i--;
            }
            int monsterPositionKey = (int) (Math.random() * 40) + 10;
            if (!portalPositionValue.keySet().contains(portalPositionKey) &&
                    !monsterPositionValue.keySet().contains(portalPositionKey)) {
                monsterPositionValue.put((int) (Math.random() * 100) + 1, (int) (Math.random() * 10) + 5);
            } else {
                i--;
            }
            int riskPositionKey = (int) (Math.random() * 35) + 5;
            if (!portalPositionValue.keySet().contains(portalPositionKey) &&
                    !riskPositionValue.keySet().contains(portalPositionKey)) {
                riskPositionValue.put((int) (Math.random() * 100) + 1, (int) (Math.random() * 10) + 5);
            } else {
                i--;
            }
        }
    }

    public static int rollDice(int repeat) {
        System.out.println("ROLLING DICE...PRESS on the Dice button or shake:");

        //Todo: Würfeln mit Schütteln oder klicken
        return 1;
    }

    public static boolean userTurn() {
        if (turn % 2 == 0) {
            turn++;
            return true;
        }
        turn++;
        return false;
    }


    public static void newUserPosition(int rolledNo) {

        if ( gameBoard[userPosition] == "H G"){
            gameBoard[userPosition] = "G";
        } else {
            gameBoard[userPosition] = "";
        }

        userPosition = userPosition + rolledNo;
        if ( userPosition > 47 ){
            userPosition = 47;
        }
        gameBoard[userPosition] = "H";
        System.out.println("Hostposition ist:" + userPosition);
        // userPosition = checkMonsterOrPortalOrRisk(userPosition);
    }

    public static void newrivalPosition(int rolledNo) {

        if ( gameBoard[rivalPosition] == "H G"){
            gameBoard[rivalPosition] = "H";
        } else {
            gameBoard[rivalPosition] = "";
        }

        rivalPosition = rivalPosition + rolledNo;
        if ( rivalPosition > 47 ){
            rivalPosition = 47;
        }
        gameBoard[rivalPosition] = "G";
        System.out.println("Die Position des Gastspielers ist:" + rivalPosition);
        // rivalPosition = checkMonsterOrPortalOrRisk(rivalPosition);
    }

    private static int checkMonsterOrPortalOrRisk(int position) {
        if (monsterPositionValue.keySet().contains(position)) {
            System.out.println("Ohhh Nein! Ein MONSTER greift dich an!");
            position = position - monsterPositionValue.get(position);
        } else if (portalPositionValue.keySet().contains(position)) {
            System.out.println("Es ist ein magisches PORTAL!");
            position = position + portalPositionValue.get(position);
        } else if (riskPositionValue.keySet().contains(position)) {
            System.out.println("WoW, ein Risikofeld. Du ziehst jetzt eine RISIKOKARTE!");
            drawRiskcard();
        }
        return position;
    }

    public static void drawRiskcard(){
        // Todo: Risikokarten einfügen
        // Risikokarten müssen randomisiert werden
    }



    public void main(String args[]) {
        System.out.println("WILLKOMMEN BEI MONSTERS & PORTALS!");

        gameBoard();
    }


    public void setBoard(){

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

        roll = (Button) findViewById(R.id.roll);
        roll.setText("Würfel");

        for (int i = 0; i < gameBoard.length; i++ ){
            gameBoard[i] = "";
        }

        gameBoard[0] = "H G";
        f0.setText(gameBoard[0]);

    }

    public void checkBoard(){

        f0.setText(gameBoard[0]);       f10.setText(gameBoard[10]);      f20.setText(gameBoard[20]);        f30.setText(gameBoard[30]);        f40.setText(gameBoard[40]);
        f1.setText(gameBoard[1]);       f11.setText(gameBoard[11]);      f21.setText(gameBoard[21]);        f31.setText(gameBoard[31]);        f41.setText(gameBoard[41]);
        f2.setText(gameBoard[2]);       f12.setText(gameBoard[12]);      f22.setText(gameBoard[22]);        f32.setText(gameBoard[32]);        f42.setText(gameBoard[42]);
        f3.setText(gameBoard[3]);       f13.setText(gameBoard[13]);      f23.setText(gameBoard[23]);        f33.setText(gameBoard[33]);        f43.setText(gameBoard[43]);
        f4.setText(gameBoard[4]);       f14.setText(gameBoard[14]);      f24.setText(gameBoard[24]);        f34.setText(gameBoard[34]);        f44.setText(gameBoard[44]);
        f5.setText(gameBoard[5]);       f15.setText(gameBoard[15]);      f25.setText(gameBoard[25]);        f35.setText(gameBoard[35]);        f45.setText(gameBoard[45]);
        f6.setText(gameBoard[6]);       f16.setText(gameBoard[16]);      f26.setText(gameBoard[26]);        f36.setText(gameBoard[36]);        f46.setText(gameBoard[46]);
        f7.setText(gameBoard[7]);       f17.setText(gameBoard[17]);      f27.setText(gameBoard[27]);        f37.setText(gameBoard[37]);        f47.setText(gameBoard[47]);
        f8.setText(gameBoard[8]);       f18.setText(gameBoard[18]);      f28.setText(gameBoard[28]);        f38.setText(gameBoard[38]);
        f9.setText(gameBoard[9]);       f19.setText(gameBoard[19]);      f29.setText(gameBoard[29]);        f39.setText(gameBoard[39]);

    }

}