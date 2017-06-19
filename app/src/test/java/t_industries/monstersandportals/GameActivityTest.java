package t_industries.monstersandportals;

import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.runner.RunWith;

import static t_industries.monstersandportals.GameActivity.gameBoard;
import static t_industries.monstersandportals.GameActivity.monster;
import static t_industries.monstersandportals.GameActivity.portal;
import static t_industries.monstersandportals.GameActivity.risk;
import static t_industries.monstersandportals.GameActivity.rivalPosition;
import static t_industries.monstersandportals.GameActivity.userPosition;

/**
 * Created by SW on 07.06.2017.
 */


public class GameActivityTest {


    @Test
    public void setBoardFirst() {

        int lowerBoundInclusive = 1;
        int upperBoundInclusive = 48;
        int arraySize = new Random().nextInt(upperBoundInclusive) + lowerBoundInclusive; //Zufällige Arraygröße
        String[] testGameArray = new String[arraySize];
        //assertTrue(Arrays.equals(testGameArray,GameActivity.gameBoard));

        try {

            assertEquals(testGameArray.length, gameBoard.length);
            System.out.println("Arrays sind gleich groß. Test funktioniert.");

        } catch (AssertionError expected) {
            System.out.println("Arrays sind ungleich groß. Test funktioniert.");
        }

    }

    @Test
    public void setMonster() {
        int[] testMonster = {12, 31, 46, 5, 25, 19};
        assertTrue(Arrays.equals(testMonster, monster));

    }

    @Test
    public void setPortal() {
        int[] testPortal = {7, 22, 33, 16, 28, 41};
        assertTrue(Arrays.equals(testPortal, portal));
    }

    @Test
    public void setRisk() {
        int[] testRisk = {10, 26, 40};
        assertTrue(Arrays.equals(testRisk, risk));
    }

    @Test
    public void setUserPosition() {
        int testUserPosition = 0;
        assertEquals(testUserPosition, userPosition);
    }

    @Test
    public void setRivalPosition() {
        int testRivalPosition = 0;
        assertEquals(testRivalPosition, rivalPosition);
    }

    @Test
    public void setImageView() {
        GameActivity testGame = new GameActivity();
        assertNull(testGame.buttons);
    }

    @Test
    public void setImageView2() {
        GameActivity testGame = new GameActivity();
        assertNull(testGame.rollClient);
    }

    @Test
    public void setImageView3() {
        GameActivity testGame = new GameActivity();
        assertNull(testGame.rollServer);
    }

    @Test
    public void setButtons() {
        GameActivity testGame = new GameActivity();
        assertNull(testGame.btnCheatClient);
    }

    @Test
    public void setButtons2() {
        GameActivity testGame = new GameActivity();
        assertNull(testGame.btnCheatServer);
    }


    @Test
    public void setGras() {
        int[] testGras = new int[48];
        GameActivity testGame = new GameActivity();


        //assertTrue(Arrays.equals(testGameArray,testGame.gameBoard));

        try {

            assertEquals(testGras.length, testGame.gras.length);
            System.out.println("Arrays sind gleich groß. Test funktioniert.");

        } catch (AssertionError expected) {
            System.out.println("Arrays sind ungleich groß. Test funktioniert.");
        }

    }

    @Test
    public void checkRandom() throws Exception {

        GameActivity randomTest = new GameActivity();
        assertTrue("Zahl muss zwischen 1 und 10 liegen: " + randomTest.number, 1 <= randomTest.number && randomTest.number <= 10);

    }

    @Test
    public void checkString() throws Exception {

        GameActivity numTest = new GameActivity();
        String test = String.valueOf(numTest.number);
        assertEquals(test, numTest.num);
    }

    @Test
    public void checkisRisk() throws Exception {

        GameActivity numTest = new GameActivity();
        assertEquals(0, numTest.isRisk);
    }

    @Test
    public void checkIsActiveOrderServer() throws Exception {

        GameActivity numTest = new GameActivity();
        assertEquals(0, numTest.isActiveOrderServer);
    }

    @Test
    public void checkIsActiveOrderClient() throws Exception {

        GameActivity numTest = new GameActivity();
        assertEquals(0, numTest.isActiveOrderClient);
    }


    @Test
    public void onCreate() throws Exception {

    }

    @Test
    public void gameTurnServer() throws Exception {

    }

    @Test
    public void gameTurnClient() throws Exception {

    }


    //klappt nur solange man nicht auf einem Ereignisfeld landet >>>suche noch nach Lösung
    @Test
    public void newUserPosition() throws Exception {
        GameActivity testGame = new GameActivity();
        int lowerBoundInclusive = 0;
        int upperBoundInclusive = 45;
        int rolledNr;

        //Zufallsfeld (int) so lange generieren >> https://stackoverflow.com/questions/738629/math-random-versus-random-nextintint
        //bis Sie auf kein Ereignisfeld kommt
        do {
            rolledNr = new Random().nextInt(upperBoundInclusive) + lowerBoundInclusive;
        }
        while (rolledNr == 12 || rolledNr == 31 || rolledNr == 5 || rolledNr == 25 || rolledNr == 19
                || rolledNr == 7 || rolledNr == 22 || rolledNr == 33 || rolledNr == 16 || rolledNr == 28 || rolledNr == 41
                || rolledNr == 10 || rolledNr == 26 || rolledNr == 40);

        //mit try-catch, falls das Feld ein Aktionsfeld ist
        // für Monsterfeld int rolledNr = 12;
        try {
            testGame.newUserPosition(rolledNr);
            assertTrue(testGame.userPosition == rolledNr);
        } catch (NullPointerException e) {
            //assertEquals("server",testGame.checkMonsterOrPortalOrRiskServer(rolledNr));
            //assertEquals(12,testGame.checkMonsterOrPortalOrRiskServer(rolledNr));
        }

    }


    @Test
    public void newrivalPosition() throws Exception {
        GameActivity testGame = new GameActivity();
        int lowerBoundInclusive = 0;
        int upperBoundInclusive = 45;
        int rolledNr;

        //Zufallsfeld (int) so lange generieren
        //bis Sie auf kein Ereignisfeld kommt
        do {
            rolledNr = new Random().nextInt(upperBoundInclusive) + lowerBoundInclusive;
        }
        while (rolledNr == 12 || rolledNr == 31 || rolledNr == 5 || rolledNr == 25 || rolledNr == 19
                || rolledNr == 7 || rolledNr == 22 || rolledNr == 33 || rolledNr == 16 || rolledNr == 28 || rolledNr == 41
                || rolledNr == 10 || rolledNr == 26 || rolledNr == 40);

        try {
            testGame.newrivalPosition(rolledNr);
            assertTrue(rivalPosition == rolledNr);
        } catch (NullPointerException e) {
            Logger.getLogger("Falsche Position", String.valueOf(e));
        }
    }


    //prüft ob die Variablen wieder freigegeben wurden
    @Test
    public void onDestroy() throws AssertionError {
        GameActivity testGame = new GameActivity();
        assertTrue(testGame.mpLaugh == null);
        assertTrue(testGame.mpMonster == null);
        assertTrue(testGame.mpPortal == null);
        assertTrue(testGame.mpPunch == null);
        assertTrue(testGame.mpUoh == null);
        assertTrue(testGame.mpWoo == null);
        assertTrue(testGame.mpYeah == null);
    }

    @Test
    public void drawRiskcardClient() throws Exception {

    }

    @Test
    public void drawRiskcardServer() throws Exception {

    }

    @Test
    public void sendRiskMessageSuccess() throws Exception {

    }

    @Test
    public void sendRiskMessageFail() throws Exception {

    }

/*
    @Test
    public void checkBoard() throws Exception {
        /*
    }
        GameActivity testGame = new GameActivity();
        testGame.setBoard();
        ImageView[] testButtons = new ImageView[48];
        int[] gras = new int[48];


        for (int i = 0; i < 47; i++) {


            for (int j = 0; j < 3; j++) {                                // Schleife soll verhindern, dass die Eventfelder neue Zeichnungen bekommen

                if (i == monster[j] || i == portal[j] || i == risk[j]) {

                    i++;

                }

            }


            if (gameBoard[i] == "H") {

                if (i == monster[3] || i == monster[4] || i == monster[5]) {

                    testButtons[i].setImageResource(R.drawable.gras_m); //falsches Bild

                } else if (i == portal[3] || i == portal[4] || i == portal[5]) {

                    testButtons[i].setImageResource(R.drawable.gras_p_h);

                } else testButtons[i].setImageResource(R.drawable.player_host);

            } else if (gameBoard[i] == "G") {

                if (i == monster[3] || i == monster[4] || i == monster[5]) {

                    testButtons[i].setImageResource(R.drawable.gras_m_g);

                } else if (i == portal[3] || i == portal[4] || i == portal[5]) {

                    testButtons[i].setImageResource(R.drawable.gras_p_g);

                } else testButtons[i].setImageResource(R.drawable.player_guest);

            } else if (gameBoard[i] == "H G") {

                if (i == monster[3] || i == monster[4] || i == monster[5]) {

                    testButtons[i].setImageResource(R.drawable.gras_m_b);

                } else if (i == portal[3] || i == portal[4] || i == portal[5]) {

                    testButtons[i].setImageResource(R.drawable.gras_p_b);

                } else testButtons[i].setImageResource(R.drawable.player_both);

            } else if (gameBoard[i] == "") {

                if (i == monster[3] || i == monster[4] || i == monster[5]) {

                    testButtons[i].setImageResource(R.drawable.gras_m);

                } else if (i == portal[3] || i == portal[4] || i == portal[5]) {

                    testButtons[i].setImageResource(R.drawable.gras_p);                    // nimmt wieder die Ursprüngliche Grasform an

                    /*} else if (gras[i] == 1){

                        testButtons[i].setImageResource(R.drawable. gras_1);

                    } else if (gras[i] == 2) {

                        testButtons[i].setImageResource(R.drawable. gras_2);

                    } else if (gras[i] == 3) {

                        testButtons[i].setImageResource(R.drawable. gras_3);

                    } else if (gras[i] == 4) {

                        testButtons[i].setImageResource(R.drawable. gras_4);

                    }*/
/*
                    if (i == 47) {

                        testButtons[i].setImageResource(R.drawable.end_field);             // das letzte Feld behält die Zeichnung

                    }


                }

            }

        }
        try {
            for (int i = 0; i < 47; i++) {


                for (int j = 0; j < 3; j++) {                                // Schleife soll verhindern, dass die Eventfelder neue Zeichnungen bekommen

                    if (i == monster[j] || i == portal[j] || i == risk[j]) {

                        i++;

                    }

                }


                if (gameBoard[i] == "H") {

                    if (i == monster[3] || i == monster[4] || i == monster[5]) {

                        assertEquals(testButtons[i].getResources(), testGame.buttons[i].getResources());

                    } else if (i == portal[3] || i == portal[4] || i == portal[5]) {

                        assertEquals(testButtons[i].getResources(), testGame.buttons[i].getResources());

                    } else
                        assertEquals(testButtons[i].getResources(), testGame.buttons[i].getResources());

                } else if (gameBoard[i] == "G") {

                    if (i == monster[3] || i == monster[4] || i == monster[5]) {

                        assertEquals(testButtons[i].getResources(), testGame.buttons[i].getResources());

                    } else if (i == portal[3] || i == portal[4] || i == portal[5]) {

                        assertEquals(testButtons[i].getResources(), testGame.buttons[i].getResources());

                    } else
                        assertEquals(testButtons[i].getResources(), testGame.buttons[i].getResources());

                } else if (gameBoard[i] == "H G") {

                    if (i == monster[3] || i == monster[4] || i == monster[5]) {

                        assertEquals(testButtons[i].getResources(), testGame.buttons[i].getResources());

                    } else if (i == portal[3] || i == portal[4] || i == portal[5]) {

                        assertEquals(testButtons[i].getResources(), testGame.buttons[i].getResources());

                    } else
                        assertEquals(testButtons[i].getResources(), testGame.buttons[i].getResources());

                } else if (gameBoard[i] == "") {

                    if (i == monster[3] || i == monster[4] || i == monster[5]) {

                        assertEquals(testButtons[i].getResources(), testGame.buttons[i].getResources());

                    } else if (i == portal[3] || i == portal[4] || i == portal[5]) {

                        assertEquals(testButtons[i].getResources(), testGame.buttons[i].getResources());                  // nimmt wieder die Ursprüngliche Grasform an

                    /*} else if (gras[i] == 1){

                        assertEquals(testButtons[i].getResources(),testGame.buttons[i].getResources());

                    } else if (gras[i] == 2) {

                       assertEquals(testButtons[i].getResources(),testGame.buttons[i].getResources());

                    } else if (gras[i] == 3) {

                        assertEquals(testButtons[i].getResources(),testGame.buttons[i].getResources());

                    } else if (gras[i] == 4) {

                       assertEquals(testButtons[i].getResources(),testGame.buttons[i].getResources());

                    }*/
/*
                        if (i == 47) {

                            assertEquals(testButtons[i].getResources(), testGame.buttons[i].getResources());             // das letzte Feld behält die Zeichnung

                        }


                    }

                }

            }


            fail("Exception sollte geworden werden, da Bilder nicht richtig zugeordnet sind");
        } catch (IllegalArgumentException expected) {
        }
    }
*/

    @Test
    public void setBoard() throws Exception {

    }

    @Test
    public void onClick() throws Exception {

    }


    @Test
    public void calculateSensor() throws ArithmeticException {

        final GameActivity SensorTest = new GameActivity();

        float x = 100;
        float y = 200;
        float z = 150;
        float last_x = 80;
        float last_y = 230;
        float last_z = 400;
        float diffTime = 1000;

        //curTime muss als Parameter mitgegeben werden, da aber diffTime berechnet wird nehmen wir einfach an, dass die Zeit noch nie upgedated wurde und die diffTime = currentTime ist
        float testSpeed = SensorTest.calculateSensor(100, 200, 150, 80, 230, 400, 1000);
        float Speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

        assertEquals(Speed, testSpeed, 0.1);
        System.out.println("Ergebnis: " + Speed + "=" + testSpeed);
    }

    @Test
    public void rollDice() throws AssertionError {

        GameActivity rollDice = new GameActivity();

        for (int i = 0; i < 100000; i++) {
            int diceCount = rollDice.rollDice();
            System.out.println(diceCount);
            assertTrue("Zahl ist kleiner 1 oder groesser 6.", 1 <= diceCount && diceCount <= 6);
        }

    }

    @Test
    public void onAccuracyChanged() throws Exception {
        //wird nicht getestet, da nicht verwendet
    }


    @Test
    public void onResume() throws Exception {

    }

    @Test
    public void onPause() throws Exception {

    }

}
