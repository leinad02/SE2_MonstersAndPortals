package t_industries.monstersandportals;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import junit.framework.Assert;

import org.junit.Test;

import java.sql.ResultSet;
import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

/**
 * Created by danie on 07.05.2017.
 */

public class DiceTest extends GameActivity {

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



    @Test
    public void setDiceIsCorrect(){

        ImageView iv_test1 = new ImageView(getApplicationContext());
        iv_test1.setImageResource(R.drawable.d1);

        rollClient.setImageResource(R.drawable.d1);


        if (rolledNumber == 1){
           // assertEquals(R.drawable.d1, rollClient.setImageResource(rolledNumber));
        }

    }



}



