package t_industries.monstersandportals;

import android.app.Dialog;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by SW on 08.05.2017.
 */
@RunWith(AndroidJUnit4.class)
public class PortalTest {

/*
    @Rule
    public ActivityTestRule<Portal> portalActivityTestRule =
            new ActivityTestRule<Portal>(Portal.class);


    @Test
    public void testPortalButton(){
        //findet Button im Dialog und drückt ihn
        onView(withText("Gehe durch das Portal!")).perform(click());
    }
    */
    @Test
    public void testPortalDialog() {
        //DIalog Testen etwa so?
       // getInstrumentation().waitForIdleSync();
       // Dialog dialog = getActivity().getDialogManager().findDialogByTag
    }
}
