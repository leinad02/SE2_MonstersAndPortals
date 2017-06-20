package t_industries.monstersandportals;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Michael on 21.04.2017.
 */

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    protected Button serverBtn, clientBtn, anleitungBtn;
    Dialog dialog;
    protected int isPlayed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Die Titelleiste (Akku etc.) wird ausgeblendet wichtig, dies muss vor setContentView geschehen, sonst schmeißt
        das Programm eine Exception*/
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //Damit der Screen immer aktiv bleibt, diese kleine Änderung
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Der Screen wird noch auf Fullscreen gesetzt
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        serverBtn = (Button) findViewById(R.id.server);
        serverBtn.setOnClickListener(this);
        clientBtn = (Button) findViewById(R.id.client);
        clientBtn.setOnClickListener(this);
        anleitungBtn = (Button) findViewById(R.id.anleitungBtn);
        anleitungBtn.setOnClickListener(this);
        Intent i = this.getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            isPlayed = bundle.getInt("isPlayed");
        }
        if (isPlayed == 0) {
            MusicManager.SoundPlayer(this, R.raw.opener);
            isPlayed = 1;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.server:
                Intent serverAct = new Intent(this, ServerActivity.class);
                serverAct.putExtra("isPlayed", isPlayed);
                startActivity(serverAct);
                break;

            case R.id.client:
                Intent clientAct = new Intent(this, ClientActivity.class);
                clientAct.putExtra("isPlayed", isPlayed);
                startActivity(clientAct);
                break;

            case R.id.anleitungBtn:
                dialog = new Dialog(MenuActivity.this);
                dialog.setContentView(R.layout.anleitung);
                dialog.show();
                Button backMenuBtn = (Button) dialog.findViewById(R.id.backMenuBtn);
                backMenuBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dialog.cancel();
                    }
                });
                break;

            default:
                break;
        }
    }
}
