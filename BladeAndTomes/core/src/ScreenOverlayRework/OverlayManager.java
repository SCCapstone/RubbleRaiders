package ScreenOverlayRework;


import ScreenOverlayRework.Health.Health;
import ScreenOverlayRework.Inventory.InventoryUI;
//import ScreenOverlayRework.Inventory.NPCTrades.NPCBuyer;
//import ScreenOverlayRework.Inventory.NPCTrades.NPCSeller;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCBuyer;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCSeller;
import ScreenOverlayRework.Inventory.NPCInventoryUI.TownHallQuestBoard;
import ScreenOverlayRework.Inventory.TreasureChest.TreasureChestUI;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Disposable;

import static com.badlogic.gdx.utils.Align.*;

public class OverlayManager implements Disposable {
    private final BladeAndTomes game;
    private Table table;
    private InventoryUI inventory;
    private Health healthBar;
    private DragAndDrop dnd;
    private AssetManager manager;
    int counter = 0;

    public OverlayManager(BladeAndTomes Game)  {
        manager = Game.assets;

            game = Game;
            dnd = game.dnd;
            table = new Table();
            table.setDebug(true);
            table.defaults();
            table.setBounds(0,0,Game.stageInstance.getWidth(),Game.stageInstance.getHeight());
            game.stageInstance.addActor(table);
            makeOverlays();



    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void makeOverlays(){
        // Inventory
        inventory = new InventoryUI(game,dnd);
        table.align(top|left).padLeft(10);
        table.addActor(inventory.getUI());
        setHiddenTableVisibility(game.showHiddenInventory); // Initially Hidden Table is not shown.

        // MAP UI
//        mapUI = new MapUI(game);
//        table.align(top|right).padLeft(10);
//        table.addActor(mapUI.getMapUI());


        // Health Bar
        healthBar = new Health(game,manager,counter);
        table.align(top|right).padLeft(10);
        table.addActor(healthBar.getHealthBar());


    }

    public void NPCBuyerInventory(boolean val, NPCBuyer npc){
        inventory.Trade_Inventory_NPCBuyer(val,npc);
    }
    public void NPCSellerInventory(boolean val, NPCSeller npc){

        inventory.Trade_Inventory_NPCSeller(val,npc);
    }
    public NPCBuyer generateNewNPCBuyer(){
        return inventory.makeNPCBuyer();
    }
    public NPCSeller generateNewNPSeller(){

        return inventory.makeNPCSeller();
    }

    public void removeALlActors(){
        table.clear();
    }
    public void setHiddenTableVisibility(boolean val){
        game.showHiddenInventory =val;
        inventory.setHiddenInventoryVisibility(val);
    }

    public void render(){
        inventory.render();
        healthBar.update();
    }

    public void setOverLayesVisibility(boolean value) {
        game.stageInstance.addActor(table);
        table.setVisible(value);
    }
    public void setQuestBoardTradeVisibility(boolean val, TownHallQuestBoard board){
        inventory.displayQuestBoardTradeUI(val,board);
    }

    public TownHallQuestBoard generateQuestBoard(){
        return inventory.makeQuestBoard();
    }
    public void updateHealth(){
        healthBar.update();
    }

public TreasureChestUI generateChest(){
        return inventory.makeTreasureChest();
}
public void displayChest(TreasureChestUI chest){
        inventory.displayChest(chest);

}
    @Override
    public void dispose() {
        table.clear();
//
//        for(String s: manager.getAssetNames())
//            manager.unload(s);
////        healthBar.dispose();
//        inventory.dispose();
//        inventory =null;
    }
}
