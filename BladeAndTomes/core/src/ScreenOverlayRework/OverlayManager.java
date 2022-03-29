package ScreenOverlayRework;


import ScreenOverlayRework.Health.Health;
import ScreenOverlayRework.Inventory.InventoryUI;
import ScreenOverlayRework.Inventory.NPCTrades.NPCBuyer;
import ScreenOverlayRework.Inventory.NPCTrades.NPCSeller;
import ScreenOverlayRework.Map.MapUI;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

import static com.badlogic.gdx.utils.Align.*;

public class OverlayManager {
    private final BladeAndTomes game;
    private Table table;
    private InventoryUI inventory;
    private Health healthBar;
    private NPCSeller seller;
    private NPCBuyer buyer;
    private DragAndDrop dnd;
    private MapUI mapUI;
    public OverlayManager(BladeAndTomes Game)  {

            game = Game;
            dnd = new DragAndDrop();
            table = new Table();
            table.setDebug(true);
            table.defaults();
            table.setBounds(0,0,Game.stageInstance.getWidth(),Game.stageInstance.getHeight());
            game.stageInstance.addActor(table);
            makeOverlays();


    }
    public void makeOverlays(){

        // Inventory
        inventory = new InventoryUI(game,dnd);
        table.align(top|left).padLeft(10);
        table.addActor(inventory.getUI());
        setHiddenTableVisibility(game.showHiddenInventory); // Initially Hidden Table is not shown.

        // MAP UI
        mapUI = new MapUI(game);
        table.align(top|right).padLeft(10);
        table.addActor(mapUI.getMapUI());


        // Health Bar
        healthBar = new Health(game);
        table.align(top|right).padLeft(10);
        table.addActor(healthBar.getHealthBar());

        // NPC Seller
        seller = new NPCSeller(game,
                dnd);
        table.align(center).padLeft(10);
        table.addActor(seller.getTable());
        seller.getTable().setVisible(game.showtrade);

//         NPC Buyer
        buyer = new NPCBuyer(game,
                dnd);
        table.align(center).padLeft(10);
        table.addActor(buyer.getTable());
        buyer.getTable().setVisible(game.showtradeBuyer);

    }
    public void setshowBuyer(boolean val){
        game.showtradeBuyer = val;
        buyer.getTable().setVisible(val);

    }
    public boolean isBuyerVis(){
        return buyer.getTable().isVisible();
    }
    public void showtradeseller(boolean val){
        game.showtrade = val;
        seller.getTable().setVisible(val);
    }
    public boolean getTradeSellervisiabel(){
        return  seller.getTable().isVisible();
    }
    public void setOverLayesVisibility(boolean value) {
        game.stageInstance.addActor(table);
        table.setVisible(value);
    }
    public void updateAll() throws CloneNotSupportedException {
        game.stageInstance.addActor(table);
    }
    public boolean reset(){
        return seller.updateSlots||buyer.updateSlots;
    }
    public void setHiddenTableVisibility(boolean val){
        game.showHiddenInventory =val;
        game.stageInstance.addActor(table);
        inventory.setHiddenInventoryVisibility(val);
    }
    public boolean getHiddenTableVisibility(){
        return inventory.getHiddenInventoryVisibility();
    }
    public void updateHealth(){
        healthBar.update();
    }
    public void updateOverlays(){
        removeALL();
        makeOverlays();
    }
    public void  removeALL(){
        inventory.removeListens();
        table.clearChildren();
    }

}