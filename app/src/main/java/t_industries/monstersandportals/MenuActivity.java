package t_industries.monstersandportals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    Button portalTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        portalTest= (Button) findViewById(R.id.portaltestBtn);
        portalTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        setContentView(R.layout.portal);
    }
}
