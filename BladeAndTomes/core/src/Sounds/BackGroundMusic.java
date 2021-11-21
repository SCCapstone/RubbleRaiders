package Sounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import java.util.Random;

public class BackGroundMusic {
    private float volume;
    private boolean isMute;
    private String[] playList;
    private Music music;
    public BackGroundMusic(String[] customPlayList, float customVolume,boolean muted){
        playList = customPlayList;
        volume  =   customVolume;
        isMute = muted;
    }
   public BackGroundMusic(){
        playList = new String[]{"new_main_menu_theme.mp3"};
        volume  =   0.5f;
        isMute = false;
    }

    public float getVolume() { return volume; }

    public void setVolume(float newVolume){
        volume = newVolume;
    }

    public void setMute(boolean mute){
        isMute = mute;
    }

    public void playMusic(){
        int musicType = new Random().nextInt(playList.length);
        Music test = Gdx.audio.newMusic(Gdx.files.internal(playList[musicType]));
        test.play();
        test.setLooping(true);
        test.setVolume(volume);
        if(!isMute)
        test.play();
    }

}
