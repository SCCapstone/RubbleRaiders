package Sounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class ButtonClickSound {
    private float volume;
    private float pitch;
    private boolean isMusted;
    private String buttonClickPath;
    private Sound button;
    long id;
    public ButtonClickSound (float customVolume, float customPitch, boolean muted, String customButtonClickPath){
            volume = customVolume;
            pitch = customPitch;
            isMusted = muted;
            buttonClickPath = customButtonClickPath;
    }
    public ButtonClickSound(){
        volume = 0.5f;
        pitch = 2f;
        isMusted = false;
        buttonClickPath = "buttonClick.wav";
    }
    public void setVolume(float newVolume){
        volume = newVolume;
    }
    public void setMute(boolean mute){
        isMusted = mute;
    }
    public void playClick(){
        button = Gdx.audio.newSound(Gdx.files.internal(buttonClickPath));
        id = button.play(volume);
        button.setPitch(id,pitch);
        button.setLooping(id,false);

    }

}
