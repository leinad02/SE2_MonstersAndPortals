package t_industries.monstersandportals;

import org.junit.Test;

import java.util.Random;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by danie on 13.05.2017.
 */
public class GameActivityTest {
    @Test
    public void rollDice() throws Exception {
        Random random = new Random();
        int min = 1;
        int max = 6;


        for (int i = 0; i < 100000; i++) {
            int zahl = random.nextInt(6)+1;
            assertTrue("zahl kleiner 1 || größer 6 " + zahl, min <= zahl && zahl <= max);
            System.out.println(zahl);

        }

    }

}