package Sounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

import java.util.Random;

public class BackGroundMusic {
    private float volume;
    private boolean isMute;
    private String[] playList;
    private Music music;
    public int musicType;
    public Music test;
    private final AssetManager manager = new AssetManager();
    public BackGroundMusic(String[] customPlayList, float customVolume,boolean muted){
        playList = customPlayList;
        musicType = new Random().nextInt(playList.length);
        test = Gdx.audio.newMusic(Gdx.files.internal(playList[musicType]));
        volume  =   customVolume;
        isMute = muted;
    }
   public BackGroundMusic(){
        playList = new String[]{"new_main_menu_theme.mp3", "Music/Dungeon.mp3", "Music/Battle.mp3", "Music/Town.mp3"};
        for(String music: playList) {
            manager.load(music, Music.class);
        }
        manager.finishLoading();
        //playList = new String[]{"new_main_menu_theme.mp3"};
       musicType = new Random().nextInt(playList.length);
        volume  =   0.5f;
        isMute = false;
        music = manager.get(playList[0]);
    }

    public float getVolume() { return volume; }

    public void setVolume(float newVolume){
        volume = newVolume;
        music.setVolume(volume);
        music.play();
    }

    public void setMute(boolean mute){
        isMute = mute;
    }

    public void playOverWorldMusic(){
        music.dispose();
        music = manager.get(playList[0]);
        music.play();
        music.setLooping(true);
        music.setVolume(volume);
        if(!isMute)
        music.play();
    }



    public void playDungeonMusic(){
        // Music from: https://opengameart.org/content/the-dunjiin
        music.dispose();
        music.stop();
        music = manager.get(playList[1]);
        music.play();
        music.setLooping(true);
        music.setVolume(volume);
        if(!isMute)
            music.play();
    }

    public void playBattleMusic(){
        // Music from: https://opengameart.org/content/battle-loops-2 superTuesday
        music.dispose();
        music = manager.get(playList[2]);
        music.play();
        music.setLooping(true);
        music.setVolume(volume);
        if(!isMute)
            music.play();
    }

    public void playTownMusic(){
        // Music from: https://opengameart.org/content/town
        music.dispose();
        music = manager.get(playList[3]);
        music.play();
        music.setLooping(true);
        music.setVolume(volume);
        if(!isMute)
            music.play();
    }

}
