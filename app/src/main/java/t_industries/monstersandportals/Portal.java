package t_industries.monstersandportals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Created by SW on 23.04.2017.
 */

public class Portal extends AppCompatActivity implements View.OnClickListener {

    int position;
    Button portal;
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portal);
        portal = (Button)findViewById(R.id.portalBtn);
        portal.setOnClickListener(this);

        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("file:///C:/Users/SW/AndroidStudioProjects/SE2_MonstersAndPortals_NEUohneLibGDX/app/src/main/assets/portal2.html");


    }


    @Override
    public void onClick(View v) {
        //übergebene Position verändern Code

        //Übergang zurück ins Spiel
        setContentView(R.layout.activity_menu);
    }
}
