package LoadAndSave;

import Keyboard_Mouse_Controls.MainMenuControls;
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
    public Player generatePlayer(){
        return load.generateDefaultPlayer();
    }
    public void savePlayer(Player player, int index){
        save.saveNewProgress(player,index);
    }
    public Player loadPlayer(int index){
        return load.getPlayer(index);
    }
    public MainMenuControls getSettings(){return load.getSettings();}
    public void saveSettings(MainMenuControls controls){
        save.setSettings(controls);
    }
}
