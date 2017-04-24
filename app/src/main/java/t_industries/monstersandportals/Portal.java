package t_industries.monstersandportals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Created by SW on 23.04.2017.
 */


public class Portal extends AppCompatActivity {

    int position;
    Button portal;
    //WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portal);

        portal = (Button)findViewById(R.id.portalBtn);
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
        System.out.println("Button funktioniert");
                    //Übergang zurück ins Spiel
                    setContentView(R.layout.activity_menu);

                }

        });
    /*
         Thread timer = new Thread(){

            public void run(){
                try{
                    sleep(3000); //Dauer der Gif Animation
                    System.out.println("Timer");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    Intent intent = new Intent(Portal.this, MenuActivity.class);
                    startActivity(intent);
                }
            }

        };
        timer.start();;
        */


    }

    /*@Override
    protected void onPause(){
        super.onPause();
        finish();
    }*/





}


