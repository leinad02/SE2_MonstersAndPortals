package t_industries.monstersandportals;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by micha on 18.06.2017.
 */

public class MusicManager {
    public static MediaPlayer player;

    public static void SoundPlayer(Context ctx, int raw_id) {
        player = MediaPlayer.create(ctx, raw_id);
        player.setLooping(true); // Set looping
        player.setVolume(100, 100);
        player.start();
    }
}
