package Sounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
        movVolume = .8f;
        movPitch = .2f;
        source = "Player/PlayerStep.mp3";
    }

    public void playMoveSound() {
        moven = Gdx.audio.newSound(Gdx.files.internal(source));
        identifier = moven.play(movVolume);
        moven.setPitch(identifier, movPitch);
        moven.setLooping(identifier, false);
    }

}
