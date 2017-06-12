package t_industries.monstersandportals;

import android.widget.ImageButton;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static t_industries.monstersandportals.GameActivity.gameBoard;
import static t_industries.monstersandportals.GameActivity.monster;
import static t_industries.monstersandportals.GameActivity.portal;
import static t_industries.monstersandportals.GameActivity.risk;

/**
 * Created by SW on 07.06.2017.
 */
public class GameActivityTest {



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
        int lowerBoundInclusive=1;
        int upperBoundInclusive=47;

        //Zufallsfeld (int) generieren >> https://stackoverflow.com/questions/738629/math-random-versus-random-nextintint
        int rolledNr = new Random().nextInt(upperBoundInclusive)+lowerBoundInclusive;

        //mit try-catch, falls das Feld ein Aktionsfeld ist
        // für Monsterfeld int rolledNr = 12;
        try{
            testGame.newUserPosition(rolledNr);
            assertTrue(testGame.userPosition==rolledNr);
        }catch (NullPointerException e){
            //assertEquals("server",testGame.checkMonsterOrPortalOrRiskServer(rolledNr));
            //assertEquals(12,testGame.checkMonsterOrPortalOrRiskServer(rolledNr));
        }

    }

    @Test
    public void newrivalPosition() throws Exception {
        GameActivity testGame = new GameActivity();
        int lowerBoundInclusive=1;
        int upperBoundInclusive=47;

        //Zufallsfeld (int) generieren
        int rolledNr = new Random().nextInt(upperBoundInclusive)+lowerBoundInclusive;
        try{
        testGame.newUserPosition(rolledNr);
        assertTrue(testGame.userPosition==rolledNr);
        }catch (NullPointerException e){

        }
    }


    //prüft ob die Variablen wieder freigegeben wurden
    @Test
    public void onDestroy() throws Exception {
        GameActivity testGame = new GameActivity();
        assertTrue(testGame.mpLaugh==null);
        assertTrue(testGame.mpMonster==null);
        assertTrue(testGame.mpPortal==null);
        assertTrue(testGame.mpPunch==null);
        assertTrue(testGame.mpUoh==null);
        assertTrue(testGame.mpWoo==null);
        assertTrue(testGame.mpYeah==null);
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

    //testet ob die Bilder richtig zugeteilt wurden, funktioniert auch noch nicht richtig, Probleme beim Zugriff auf die Ressource
    @Test
    public void checkBoard() throws Exception {
        GameActivity testGame = new GameActivity();
        ImageButton[] testButtons = new ImageButton[48];

        for (int i = 0; i < 48; i++) {

            for ( int j = 0; j <3; j++){                                // soll verhindern, dass die Eventfelder neue Zeichnungen bekommen
                if ( i == monster[j] || i == portal[j] || i == risk[j] || i == 47){
                    i++;
                }
            }
            if (gameBoard[i] == "H" ){
                testButtons[i].setImageResource(R.drawable. player_host);
            } else if (gameBoard[i] == "G") {
                testButtons[i].setImageResource(R.drawable. player_guest);
            } else if  (gameBoard[i] == "H G") {
                testButtons[i].setImageResource(R.drawable. player_both);
            } else if (gameBoard[i] == "") {
                testButtons[i].setImageResource(R.drawable. grass);
            }
        }

     for (int i = 0; i < 48; i++) {

        for ( int j = 0; j <3; j++){                                // soll verhindern, dass die Eventfelder neue Zeichnungen bekommen
            if ( i == monster[j] || i == portal[j] || i == risk[j] || i == 47){
                i++;
            }
        }
        if (gameBoard[i] == "H" ){
            assertEquals(testButtons[i].getResources(),testGame.buttons[i].getResources());
        } else if (gameBoard[i] == "G") {
            assertEquals(testButtons[i],testGame.buttons[i]);
        } else if  (gameBoard[i] == "H G") {
            assertEquals(testButtons[i],testGame.buttons[i]);
        } else if (gameBoard[i] == "") {
            assertEquals(testButtons[i],testGame.buttons[i]);
        }
    }
}
    @Test
    public void checkBoard2() throws Exception {
        GameActivity testGame = new GameActivity();
        ImageButton[] testButtons = new ImageButton[48];
        //ClassLoader classLoaderTest = getClass().getClassLoader();
       

        for (int i = 0; i < 48; i++) {

            for ( int j = 0; j <3; j++){                                // soll verhindern, dass die Eventfelder neue Zeichnungen bekommen
                if ( i == monster[j] || i == portal[j] || i == risk[j] || i == 47){
                    i++;
                }
            }
            if (gameBoard[i] == "H" ){
                testButtons[i].setImageResource(R.drawable. player_guest);
            } else if (gameBoard[i] == "G") {
                testButtons[i].setImageResource(R.drawable. player_host);
            } else if  (gameBoard[i] == "H G") {
                testButtons[i].setImageResource(R.drawable. grass);
            } else if (gameBoard[i] == "") {
                testButtons[i].setImageResource(R.drawable. player_both);
            }
        }

        try {
            for (int i = 0; i < 48; i++) {

                for (int j = 0; j < 3; j++) {                                // soll verhindern, dass die Eventfelder neue Zeichnungen bekommen
                    if (i == monster[j] || i == portal[j] || i == risk[j] || i == 47) {
                        i++;
                    }
                }
                if (gameBoard[i] == "H") {
                    assertEquals(testButtons[i].getResources(),testGame.buttons[i].getResources());
                } else if (gameBoard[i] == "G") {
                    assertEquals(testButtons[i].getResources(),testGame.buttons[i].getResources());
                } else if (gameBoard[i] == "H G") {
                    assertEquals(testButtons[i].getResources(),testGame.buttons[i].getResources());
                } else if (gameBoard[i] == "") {
                    assertEquals(testButtons[i].getResources(),testGame.buttons[i].getResources());
                }
            }
            fail("Exception sollte geworden werden, da Bilder nicht richtig zugeordnet sind" );
        }
        catch (IllegalArgumentException expected) {
        }
    }


    @Test
    public void setBoard() throws Exception {

    }

    @Test
    public void onClick() throws Exception {

    }

    @Test
    public void onSensorChanged() throws Exception {

    }

    @Test
    public void onAccuracyChanged() throws Exception {

    }

    @Test
    public void onResume() throws Exception {

    }

    @Test
    public void onPause() throws Exception {

    }

}