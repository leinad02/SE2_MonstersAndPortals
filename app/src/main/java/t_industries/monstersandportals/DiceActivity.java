package t_industries.monstersandportals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

/**
 * Created by danie on 25.04.2017.
 */

public class DiceActivity extends AppCompatActivity {

    Button b_roll;
    ImageView iv_dicepreview;
    Random random; //Zufallszahl
    int rolledNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        b_roll = (Button) findViewById(R.id.b_roll);
        iv_dicepreview = (ImageView) findViewById(R.id.iv_dicepreview);

        random = new Random();

        b_roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rolledNumber = random.nextInt(6) + 1; // +1 da bei int immer Nachkommazahl weggeschnitten

                if (rolledNumber == 1) {
                    iv_dicepreview.setImageResource(R.drawable.d1);
                } else if (rolledNumber == 2) {
                    iv_dicepreview.setImageResource(R.drawable.d2);
                } else if (rolledNumber == 3) {
                    iv_dicepreview.setImageResource(R.drawable.d3);
                } else if (rolledNumber == 4) {
                    iv_dicepreview.setImageResource(R.drawable.d4);
                } else if (rolledNumber == 5) {
                    iv_dicepreview.setImageResource(R.drawable.d5);
                } else if (rolledNumber == 6) {
                    iv_dicepreview.setImageResource(R.drawable.d6);
                }
            }
        });
    }
}
