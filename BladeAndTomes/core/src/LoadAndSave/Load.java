package LoadAndSave;

import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class Load {
    String savePath;
    Json saveParser;
    Array<String> fourPreSets;
    FileHandle file;

    public Load(String savePath){
        this.savePath=savePath;
        saveParser = new Json();
        file = new FileHandle(savePath);
    }
    public void loadFourPreSets(){
            try {
                fourPreSets = saveParser.fromJson(Array.class,file.read());
            } catch (Exception e) {
                createDeafultsPlayer();
            }
    }
    public void createDeafultsPlayer(){
        String deafult = saveParser.toJson(new Player());
        fourPreSets  = new Array<>();
        fourPreSets.add(deafult);
        fourPreSets.add(deafult);
        fourPreSets.add(deafult);
        fourPreSets.add(deafult);
    }
    public Array<String> getFourPlayers(){
        try {
            for(int i = 0;i<4;++i)
                saveParser.fromJson(Player.class,fourPreSets.get(i));
        } catch (Exception e) {
            createDeafultsPlayer();
        }
        return fourPreSets;
    }
    public Player getPlayer(int index){
        Player tempPlayer;
        try{
         tempPlayer = saveParser.fromJson(Player.class,fourPreSets.get(index));
        } catch (Exception e){
            createDeafultsPlayer();
            tempPlayer = saveParser.fromJson(Player.class,fourPreSets.get(index));
        }
        return  tempPlayer;
    }
}
