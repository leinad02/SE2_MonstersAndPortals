package t_industries.monstersandportals;

import org.junit.Test;

import static junit.framework.Assert.fail;

/**
 * Created by danie on 07.05.2017.
 */

public class DiceTest extends GameActivity {

    @Test
    public void rollDiceIsCorrect() throws Exception {
        int numbers;
        int minNumber = 0;//unklar warum nicht 1 ?
        int maxNumber = 6;

        for (int i = 0; i < 100000; i++) {
            numbers = rolledNumber;
            if (numbers > maxNumber) {
                fail("Maximale Zahl ist 6!");
            }

            if (numbers < minNumber) {
                fail("Minimale Zahl ist 1!");
            }

        }
    }

    @Test
    public void setDiceIsCorrect(){
        
    }
}



