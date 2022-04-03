package ScreenOverlayRework.Inventory.NPCInventoryUI;

import ScreenOverlayRework.Inventory.itemDocument;
import ScreenOverlayRework.Inventory.itemSlot;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.utils.Align.*;

public class NPCSeller extends TradeUI {
    private Array<RandomItemGenerator> trade;
    private Table seller;
    private int numberOfTrades;

    public NPCSeller(BladeAndTomes game, DragAndDrop dnd, AssetManager itemsManager, Array<itemSlot> slots) {
        super(game, dnd, itemsManager, slots, "NPC Seller",true);
        seller = new Table();
        trade = new Array<>();
        numberOfTrades = 3;
        generateTrades(numberOfTrades);
        drawSeller();
        seller.setPosition(500,500);
        rightStack.addActor(seller);
    }
    private void generateTrades(int numberTrades){
        for(int i = 0;i <numberTrades;++i)
            trade.add(new RandomItemGenerator(itemsManager));
    }


    public void drawSeller() {

        Table trades = new Table();
        trades.defaults();
        trades.align(right|center|top);
        trades.add(new Label("        Offers", game.BaseLabelStyle1)).size(150, 50);
        trades.row();
        for (int i = 0; i <numberOfTrades;++i){
            trades.add(makeitemRow(trade.get(i)));
            trades.row();
        }
//        game.stageInstance.addActor(trades);
        trades.setPosition(700,450);
        seller.addActor(trades);
    }
    public Table makeitemRow(final RandomItemGenerator currentTrade){
        Table table = new Table();
        itemSlot slot = new itemSlot(currentTrade.getItemDocument(),itemsManager);
        slot.displayInfo(currentTrade.getItemDocument());
        table.add(slot.getSlot()).size(100,100);
        TextButton button = new TextButton("Pay "+String.valueOf(currentTrade.getItemDocument().getPrice())+" Gold",game.generalTextButtonStyle);
        table.add(button).size(150,70).colspan(3);
        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int availableGold = game.player.getGold();
                int itemPrice = currentTrade.getItemDocument().getPrice();
                itemDocument doc = currentTrade.getItemDocument();
                if(itemPrice<=availableGold) {

                    boolean availableSlot = true;
                    for(int i =0;i<game.player.inventoryItems.size;++i){
                        if(game.player.inventoryItems.get(i).getCategory().equalsIgnoreCase("Null") &&availableSlot&&i<14)
                        {
                            doc.setIndex(String.valueOf(i));
                            doc.setDefauls = false;
                            game.player.inventoryItems.set(i,doc);
                            itemSlot slot = slots.get(i);
                            slot.getItem().setDrawable(doc.getImage(itemsManager).getDrawable());
                            slot.displayInfo();
                            availableSlot = false;
                            game.player.setGold(availableGold - itemPrice);
                            gold.setText("       Gold : " +String.valueOf(game.player.getGold()));
                            game.player.kTradesNPCSeller+=1;
                        }
                    }
                }

            };
        });
        return table;
    }
}
