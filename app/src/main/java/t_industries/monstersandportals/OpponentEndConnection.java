package t_industries.monstersandportals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class OpponentEndConnection extends AppCompatActivity {

    //private Button againBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Die Titelleiste (Akku etc.) wird ausgeblendet wichtig, dies muss vor setContentView geschehen, sonst schmeißt
        das Programm eine Exception*/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_c_opponent);
        //Damit der Screen immer aktiv bleibt, diese kleine Änderung
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Der Screen wird noch auf Fullscreen gesetzt
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}
