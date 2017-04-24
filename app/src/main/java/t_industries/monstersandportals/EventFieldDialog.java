package t_industries.monstersandportals;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Random;

import static java.lang.String.valueOf;

/**
 * Created by Jasmin on 24.04.2017.
 */

public class EventFieldDialog extends AppCompatActivity {
    Random random = new Random();
    private int number = (random.nextInt(10) + 1);
    //Randomzahl zwischen 1 und 10
    private String num = valueOf(number);
    private int move = 4; //wenn richtig, den Player 4 Positionen vor schicken

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog.Builder builderfalse = new AlertDialog.Builder(this);

        // Hilfsklasse für Dialogfenster erstellen
        builder.setMessage("Ist die Zahl "+num+ " gerade?");

        //für das Weiterbewegen des Spielers
        final DialogInterface.OnClickListener goListener = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Spieler bewegen
            }
        };

        DialogInterface.OnClickListener positivListener = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                do {
                    number = number % 2;
                }while(number>1);
                if(number==0){ //dann Zahl gerade
                    builderfalse.setMessage("Juhu, du hast richtig geantwortet!");
                    builderfalse.setPositiveButton("4 Felder vor", goListener);
                    builderfalse.show();
                }else{
                    dialog.dismiss();//Dialogfenster wird beendet, weil Zahl ungerade ist
                }
            }
        };

        DialogInterface.OnClickListener negativListener = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                do {
                    number = number % 2;
                }while(number>1);
                if(number==1){ //dann Zahl ungerade
                    builderfalse.setMessage("Juhu, du hast richtig geantwortet");
                    builderfalse.setPositiveButton("4 Felder vor", goListener);
                    builderfalse.show();

                }else{
                    dialog.dismiss();//Dialogfenster wird beendet, weil Zahl gerade ist
                }
            }
        };
        builder.setPositiveButton("richtig",positivListener);
        builder.setNegativeButton("falsch", negativListener);
        builder.show();

    }

}
