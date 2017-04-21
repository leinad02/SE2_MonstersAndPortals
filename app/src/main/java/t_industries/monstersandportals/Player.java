package t_industries.monstersandportals;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by danie on 21.04.2017.
 */

public class Player {

    private String name;
    private int position;


    public Player(String name){
        this.name = name;
        position = 0; //Position wird am Anfang auf 0 gesetzt.
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void changePosition(int wuerfelzahl){
        position += wuerfelzahl;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int wuerfelzahl) {
        this.position = wuerfelzahl;
    }

    //TODO: Würfelfunktion implementieren
}
