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
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by SW on 08.05.2017.
 */

@RunWith(AndroidJUnit4.class)
public class ServerActivityest {

    //Bedeutet, dass beim Test automatisch die ServerActivity aufgerufen wird
    @Rule
    public ActivityTestRule<ServerActivity> serverActivityTestRule =
            new ActivityTestRule<ServerActivity>(ServerActivity.class);

    @Test
    public void editTextName(){
        //findet Button mit der richtigen ID und simuliert einen User der "Sarah" als Name eingibt, schließt Keyboard
        onView(withId(R.id.textfieldNameServer))
                .perform(typeText("Sarah"), closeSoftKeyboard());
        //onView(withId(R.id.createServer)).perform(click());
        onView(withId(R.id.home)).perform(click());

    }



}
