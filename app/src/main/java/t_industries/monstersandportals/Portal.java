package t_industries.monstersandportals;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by SW on 23.04.2017.
 */


/*
* Klasse wird auch als Methode in das Spielfeld eingebunden*/

public class Portal extends AppCompatActivity {

    int position;
    Button portal;

    //zum handlen der Threads
    //Handler handler;

    Button portalTest;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

       // portalTest= (Button) findViewById(R.id.portaltestBtn);
        portalTest.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                dialog = new Dialog(Portal.this);
                dialog.setContentView(R.layout.portal);
                dialog.show();

                Button portal = (Button) dialog.findViewById(R.id.portalBtn);
                portal.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

        /*übergebene Position verändern Code

        position = Spielbrett.getUserPosition()+ (int)(Math.random()*10)+1
        if (position > 47){
        positon = 47
        }else{
        Spielbrett.setUserPosition(position);
        }
        * */
                        dialog.cancel();
                    }
                });
            }



        /*handler.postDelayed(new Runnable() {

            @Override
            public void run() {

               dialog.dismiss();
            }
        }, 3000);*/
        });
    }
}


