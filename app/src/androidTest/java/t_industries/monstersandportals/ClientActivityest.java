package t_industries.monstersandportals;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by SW on 08.05.2017.
 */

@RunWith(AndroidJUnit4.class)
public class ClientActivityest {

    //Bedeutet, dass beim Test automatisch die ServerActivity aufgerufen wird
    @Rule
    public ActivityTestRule<ClientActivity> clientActivityTestRule =
            new ActivityTestRule<ClientActivity>(ClientActivity.class);

    @Test
    public void editTextNameAndIP(){
        /*findet Button mit der richtigen ID und simuliert einen User der "Michael" als Name eingibt, schließt Keyboard
       olt das Keyboard* gibt anschließend eine Ip-Adresse ein und schließt wiederhi*/
        onView(withId(R.id.textfieldNameClient))
                .perform(typeText("Michael"), closeSoftKeyboard());
        onView(withId(R.id.textfieldIP))
                .perform(typeText("193.168.43.x"), closeSoftKeyboard());
            }



}
