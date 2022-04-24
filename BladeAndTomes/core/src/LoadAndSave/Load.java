package LoadAndSave;

import Keyboard_Mouse_Controls.MainMenuControls;
import ScreenOverlayRework.Inventory.itemDocument;
import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import jdk.tools.jmod.Main;

public class Load {
    // Path to the file where the players are being loaded
    String savePath;
    // Parser provided by libgdx
    Json saveParser;
    // List of all pre-loaded classes
    Array<String> fourPreSets;
    // File Handler to manage the file
    FileHandle file;

    /**
     *  A constructor which defines all necessary objects
     * @param savePath File path to players are loaded from
     */
    public Load(String savePath){
        // Path to file
        this.savePath=savePath;
        // Defining Json parser object by api
        saveParser = new Json();
        // FileHandler to read files
        file = new FileHandle(savePath);
    }

    /**
     * This method loads all Player from files in Array -> String format of the player class
     */
    public void loadFourPreSets(){
        // try catch if file does not exits
            try {
                // String Array containing Player classes
                fourPreSets = saveParser.fromJson(Array.class,file.read());
            } catch (Exception e) {
                // if Fails it creates default player classes
                createDeafultsPlayer();
            }
    }

    /**
     *
     * @return
     */
    public Player generateDefaultPlayer(){
        Player ply = new Player();
        itemDocument slot = ply.inventoryItems.get(0);
        slot.setDefauls = false;
        slot.setImageLocation("InventoryItems/Weapons/Sword/1.png");
        slot.setLevel(1);
        slot.setCategory("Weapons");
        slot.setName("Sword");
        slot.setDamage(1);
        slot.setRange(0);
        slot.setTargetItem("Any");

        String ItemInfo = "Item Type: "+"Sword"+"\n"+
                "Item Level: "+String.valueOf("1")+"\n"+
                "Item Damage: "+"1"+"\n"+
                ((false)? "*** Magic Item ***":"");
        slot.setItemDescription(ItemInfo);

        slot = ply.inventoryItems.get(1);
        slot.setDefauls = false;
        slot.setImageLocation(("InventoryItems/Armor/armor.png"));
        slot.setLevel(1);
        slot.setCategory("Armor");
        slot.setName("ChestPlate");
        slot.setDamage(1);
        slot.setRange(0);
        String info =  "***  Armor ***"+"\n"+
                "Armor Level: "+String.valueOf(1)+"\n"+
                "Armor Defence: "+String.valueOf(1)+"\n"+
                ((false)? "*** Magic Item ***":"");
        slot.setTargetItem("Any");
        slot.setItemDescription(info);
        slot.setDefauls = false;
        slot.isDefaultColor = true;
        return ply;

    }
    public void createDeafultsPlayer(){
        Player ply = generateDefaultPlayer();
        String deafult = saveParser.toJson(ply);
        fourPreSets  = new Array<>();
        fourPreSets.add(deafult);
        fourPreSets.add(deafult);
        fourPreSets.add(deafult);
        fourPreSets.add(deafult);
        fourPreSets.add(saveParser.toJson(new MainMenuControls()));
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
         for(int i = 0;i<26;++i){
             tempPlayer.inventoryItems.get(i).setIndex(String.valueOf(i));
         }
        } catch (Exception e){
            Gdx.files.local(savePath);
            createDeafultsPlayer();
            tempPlayer = saveParser.fromJson(Player.class,fourPreSets.get(index));
        }
        return  tempPlayer;
    }

    public MainMenuControls getSettings(){
        MainMenuControls tmpSettings;
        try{
            tmpSettings = saveParser.fromJson(MainMenuControls.class,fourPreSets.get(4));

        } catch (Exception e){
            Gdx.files.local(savePath);
            createDeafultsPlayer();
            tmpSettings = new MainMenuControls();
        }
        return  tmpSettings;

    }
}
