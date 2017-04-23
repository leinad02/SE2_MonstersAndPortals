package t_industries.monstersandportals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    private Button serverBtn, clientBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //Damit der Screen immer aktiv bleibt, diese kleine Änderung
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        serverBtn = (Button) findViewById(R.id.server);
        serverBtn.setOnClickListener(this);
        clientBtn = (Button) findViewById(R.id.client);
        clientBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.server:
                startActivity(new Intent(this, ServerActivity.class));
                break;

            case R.id.client:
                startActivity(new Intent(this, ClientActivity.class));
                break;

            default:
                break;
        }
    }
}
