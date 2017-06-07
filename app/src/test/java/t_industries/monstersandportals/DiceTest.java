package t_industries.monstersandportals;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by danie on 03.06.2017.
 */
public class DiceTest {

    @Test
    public void rollDice() throws Exception {

        GameActivity rollDice = new GameActivity();

        for (int i = 0; i < 100000; i++) {
            int diceCount = rollDice.rollDice();
            assertTrue("zahl kleiner 1 || größer 6 " + diceCount, 1 <= diceCount && diceCount <= 6);
            System.out.println(diceCount);

        }
    }

    @Test
    public void rollDiceCountLowerThanOne() throws Exception{

        GameActivity rollDice = new GameActivity();

        for (int i = 0; i < 100000; i++) {
            int diceCount = rollDice.rollDice();
            assertFalse("Die Zahl ist kleiner 1 " + diceCount, 1 > diceCount);
            System.out.println(diceCount);

        }

    }

    @Test
    public void rollDiceCountHigherThanSix() throws Exception{

        GameActivity rollDice = new GameActivity();

        for (int i = 0; i < 100000; i++) {
            int diceCount = rollDice.rollDice();
            assertFalse("Die Zahl ist größer 6 " + diceCount, 6 < diceCount);
            System.out.println(diceCount);

        }

    }



}