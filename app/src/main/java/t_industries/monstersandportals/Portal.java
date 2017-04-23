package t_industries.monstersandportals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by SW on 23.04.2017.
 */

public class Portal extends AppCompatActivity implements View.OnClickListener {

    int position;
    Button portal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portal);

    portal = (Button) findViewById(R.id.portalBtn);
        portal.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        //übergebene Position verändern Code

        //Übergang zurück ins Spiel
        setContentView(R.layout.activity_menu);
    }
}
