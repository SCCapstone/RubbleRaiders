package LoadAndSave;

import ScreenOverlayRework.Inventory.itemDocument;
import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.Gdx;
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
        Player ply = new Player();
        itemDocument slot = ply.inventoryItems.get(0);
        slot.setDefauls = false;
        slot.setImageLocation("InventoryItems/Weapons/Sword/1.png");
        slot.setLevel(1);
        slot.setCategory("Weapons");
        slot.setName("Sword");
        slot.setDamage(10);
        slot.setTargetItem("Any");
        slot.setItemDescription(
                String.valueOf(slot.getName())+'\n'+
                        String.valueOf(slot.getRange())+'\n'+
                        String.valueOf(slot.getLevel())+'\n');


        slot = ply.inventoryItems.get(1);
        slot.setDefauls = false;
        slot.setImageLocation(("InventoryItems/Armor/armor.png"));
        slot.setLevel(2);
        slot.setCategory("Armor");
        slot.setName("ChestPlate");
        slot.setDamage(10);
        slot.setTargetItem("Any");
        slot.setItemDescription(
                String.valueOf(slot.getName())+'\n'+
                        String.valueOf(slot.getRange())+'\n'+
                        String.valueOf(slot.getLevel())+'\n');

//        slot = ply.inventoryItems.get(2);
//        slot.setDefauls = false;
//        slot.setImageLocation(("InventoryItems/Spells/HealSpell.png"));
//        slot.setLevel(2);
//        slot.setCategory("Spell");
//        slot.setName("Healing");
//        slot.setDamage(10);
//        slot.setTargetItem("Any");
//        slot.setItemDescription(
//                String.valueOf(slot.getName())+'\n'+
//                        String.valueOf(slot.getRange())+'\n'+
//                        String.valueOf(slot.getLevel())+'\n');
        String deafult = saveParser.toJson(ply);

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
}
