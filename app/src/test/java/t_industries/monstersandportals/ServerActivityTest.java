package t_industries.monstersandportals;

import android.text.TextUtils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by danie on 21.06.2017.
 */
public class ServerActivityTest {

    @Test
    public void onCreate() {
        ServerActivity serverActivity = new ServerActivity();
        assertTrue(serverActivity.name == null);
        assertTrue(serverActivity.createS ==null);
        assertTrue(serverActivity.home == null);
    }

    @Test
    public void onClick()  {
    }

}