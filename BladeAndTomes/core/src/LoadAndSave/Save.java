package LoadAndSave;

import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import java.io.*;

import java.util.HashMap;

public class Save {
    String savePath;
    Json saveParser;
    Array<String> fourPreSets;
    FileHandle file;

    public Save(String savePath,Array<String> fourPreSets){
        this.savePath=savePath;
        saveParser = new Json();
        this.fourPreSets = fourPreSets;
}
public void addToGeneralSave(){}
    public void saveNewProgress(Player player,int index){
        fourPreSets.set(index,saveParser.toJson(player,Player.class));
        file = Gdx.files.local(savePath);
        file.writeString(saveParser.toJson(fourPreSets), false);
    }
}
