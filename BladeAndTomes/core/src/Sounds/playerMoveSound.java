package Sounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;

// class for player move sound
public class playerMoveSound {

    private float movVolume;
    private float movPitch;
    private String source;
    private Sound moven;
    private long identifier;

    public playerMoveSound(float volume, float pitch, String path) {
        movVolume = volume;
        movPitch = pitch;
        source = path;
    }

    public playerMoveSound() {
        movVolume = 0.8f;
        movPitch = .2f;
        source = "Player/PlayerStep.mp3";
    }

    public void playMoveSound() {
        moven = Gdx.audio.newSound(Gdx.files.internal(source));
        identifier = moven.play(movVolume);
        moven.setPitch(identifier, movPitch);
        moven.setLooping(identifier, false);
        /* trying to put a schedule to make sound play immediately. But apparently libgdx has an issue with
        immediate sound playing (events such as on triggers and stuff)
        Timer.schedule(new Timer.Task() {
            public void run() {
                moven.pause();
            }
        }, 0.01f);
         */
    }

}
