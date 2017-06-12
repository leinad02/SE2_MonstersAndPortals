package t_industries.monstersandportals;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by SW on 07.06.2017.
 */
public class MenuActivityUnitTest {

    //müssen null sein, bevor Ihnen ein Wert zugewiesen wird
    @Test
    public void onCreate() throws Exception {
        MenuActivity testMenu = new MenuActivity();
        assertTrue(testMenu.serverBtn == null);
        assertTrue(testMenu.clientBtn == null);
        assertTrue(testMenu.anleitungBtn == null);
    }

    @Test
    public void onClick() throws Exception {

    }

}