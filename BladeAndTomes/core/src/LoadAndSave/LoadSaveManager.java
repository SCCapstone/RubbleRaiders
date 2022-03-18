package LoadAndSave;

import com.badlogic.game.creatures.Player;

public class LoadSaveManager {
    private Load load;
    private Save save;
    private String saveFilePath;

    public LoadSaveManager(){
        saveFilePath = ".1";
        load = new Load(saveFilePath);
        load.loadFourPreSets();
        save=new Save(saveFilePath,load.getFourPlayers());
    }
    public void savePlayer(Player player, int index){
        save.saveNewProgress(player,index);
    }
    public Player loadPlayer(int index){
        return load.getPlayer(index);
    }
}
