package t_industries.monstersandportals;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * Created by SW on 30.04.2017.
 */

public class SoundPlayer {

    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 7;

    private static SoundPool soundPool;
    private static int monsterSound,portalSound,winningSound1,winningSound2;
    private static int losingSound,portalRivalSound,monsterRivalSound;





    public SoundPlayer(Context context) {

        // SoundPool wurde in API level 21. (Lollipop) überholt
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();

        } else {
            //SoundPool (int maxStreams, int streamType, int srcQuality)
            soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
        }

        //Sounds in initialisierte Soundvariablen speichern
        monsterSound = soundPool.load(context, R.raw.monster2, 1);
        portalSound = soundPool.load(context, R.raw.portal, 1);
        winningSound1 = soundPool.load(context, R.raw.yeah, 1);
        winningSound2 = soundPool.load(context, R.raw.woo, 1);
        losingSound = soundPool.load(context, R.raw.punch, 1);
        portalRivalSound = soundPool.load(context, R.raw.uoh, 1);
        monsterRivalSound= soundPool.load(context, R.raw.laugh, 1);


    }

    public void playMonsterSound() {

        // play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(monsterSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playPortalSound() {
        soundPool.play(portalSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }


    public void playWinningSound() {
        soundPool.play(winningSound1, 1.0f, 1.0f, 1, 0, 1.0f);
        soundPool.play(winningSound2, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playLosingSound() {
        soundPool.play(losingSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playRivalPortalSound() {
        soundPool.play(portalRivalSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playRivalMonsterSound() {
        soundPool.play(monsterRivalSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void stopSound(){
        soundPool.stop(monsterSound);
        soundPool.stop(monsterRivalSound);
        soundPool.stop(portalSound);
        soundPool.stop(portalRivalSound);
        soundPool.stop(winningSound1);
        soundPool.stop(winningSound2);
        soundPool.stop(losingSound);
    }

}
