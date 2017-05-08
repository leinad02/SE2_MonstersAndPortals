package t_industries.monstersandportals;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by SW on 08.05.2017.
 */

@RunWith(AndroidJUnit4.class)
public class MenuActivityTest {


   //Bedeutet, dass beim Test automatisch die MenuActivity aufgerufen wird
   @Rule
   public ActivityTestRule<MenuActivity> menuActivityTestRule =
           new ActivityTestRule<MenuActivity>(MenuActivity.class);


    @Test
    public void testServerButton(){
        //findet Button mit Beschriftung "Server" und drückt ihn
        onView(withText("Server")).perform(click());
    }

    @Test
    public void testClientButton(){
        //findet Button mit Beschriftung "Client" und drückt ihn
        onView(withText("Client")).perform(click());
    }
}
