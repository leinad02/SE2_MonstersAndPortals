package t_industries.monstersandportals;

        import android.app.Dialog;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;

/**
 * Created by SW on 30.04.2017.
 */


public class Monster extends AppCompatActivity{
    int position;
    //zum handlen der Threads
    //Handler handler;

    Button monsterTest;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // portalTest= (Button) findViewById(R.id.portaltestBtn);
        monsterTest.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                dialog = new Dialog(Monster.this);
                dialog.setContentView(R.layout.monster);
                dialog.show();

                Button portal = (Button) dialog.findViewById(R.id.monsterBtn);
                portal.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

       /*übergebene Position verändern Code

        position = Spielbrett.getUserPosition()- (int)(Math.random()*10)+1
        if (position < 0){
        positon = 0
        }else{
        Spielbrett.setUserPosition(position);
        }
        * */

                        dialog.cancel();
                    }
                });
            }

        });
    }
}
