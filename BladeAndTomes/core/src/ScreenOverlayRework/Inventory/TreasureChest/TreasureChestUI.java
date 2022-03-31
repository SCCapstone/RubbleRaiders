package ScreenOverlayRework.Inventory.TreasureChest;

import ScreenOverlayRework.Inventory.NPCInventoryUI.RandomItemGenerator;
import ScreenOverlayRework.Inventory.NPCInventoryUI.TradeUI;
import ScreenOverlayRework.Inventory.itemDocument;
import ScreenOverlayRework.Inventory.itemSlot;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.utils.Align.*;

public class TreasureChestUI extends TradeUI {
    private Array<RandomItemGenerator> trade;
    private Table chest;
    private int numberOfTrades;
    private boolean isTreasureChestVisiable;
    public TreasureChestUI(BladeAndTomes game, DragAndDrop dnd, AssetManager itemsManager, Array<itemSlot> slots) {
        super(game, dnd, itemsManager, slots, "Chest",true);
        chest = new Table();
        trade = new Array<>();
        numberOfTrades = 3;
        generateTrades(numberOfTrades);
        chest.setPosition(500,500);
        drawSeller();
        rightStack.addActor(chest);
        isTreasureChestVisiable = false;
    }
    private void generateTrades(int numberTrades){
        for(int i = 0;i <numberTrades;++i)
            trade.add(new RandomItemGenerator(itemsManager));

    }



    public void drawSeller() {
        Label label = new Label("        Chest", game.BaseLabelStyle1);
        label.setSize(150,50);
        Table trades = new Table();
        trades.defaults();
        trades.debug();
        trades.align(right|top);
        trades.row();
        tempSlots(true);
        for (int i = 17; i <(26);++i){
            if ((i+1) % 3 == 0)
                trades.row();
            if((i-17)<3)
            trades.add(makeitemRow(trade.get(i-17).getItemDocument(),i));
            else
                trades.add(makeitemRow(game.player.inventoryItems.get(i),i));
        }
        trades.setPosition(750,430);
        label.setPosition(525,440);
        tempSlots(false);

        chest.addActor(label);
        chest.addActor(trades);
    }
    public Table makeitemRow(final itemDocument currentTrade, int index){
        Table table = new Table();
        currentTrade.setIndex(String.valueOf(index));
        itemSlot slot = new itemSlot(game,dnd,index,"ANY",itemsManager);
        slot.setDND(dnd);
        slot.applySource();
        slot.applyTarget();
        table.add(slot.getSlot()).size(100,100);
        return table;
    }

    public boolean isTreasureChestVisible() {
        return isTreasureChestVisiable;
    }

    public void tempSlots(boolean have){
        if(have){
            if(game.player.inventoryItems.size > 16){
                for(int i=17;i<26;i++)
                    if (i-17<numberOfTrades)
                    game.player.inventoryItems.set(i,trade.get(i-17).getItemDocument());
                return;
            }
            for(int i = 0;i<9;++i)
                game.player.inventoryItems.add(trade.get(i).getItemDocument());
        }
    }
    public void setTreasureChestVisible(boolean treasureChestVisible) {
        isTreasureChestVisiable = treasureChestVisible;
        tempSlots(treasureChestVisible);
    }
}
