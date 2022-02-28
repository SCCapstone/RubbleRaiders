package ScreenOverlayRework.Inventory.NPCTrades;

import ScreenOverlayRework.Inventory.itemDocument;
import ScreenOverlayRework.Inventory.itemSlot;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.utils.Align.*;

public class NPCSeller extends npcUI {
    private Array<RandomItemGenerator> trade;
    private Table seller;
    private int numberOfTrades;
    public NPCSeller(BladeAndTomes GAME, DragAndDrop dnd) {
        super(GAME, dnd, "NPC TRADER");
        seller = new Table();
        trade = new Array<>();
        numberOfTrades = 3;
        generateTrades(numberOfTrades);
        drawSeller();
        updateSlots = false;

    }
    private void generateTrades(int numberTrades){
        for(int i = 0;i <numberTrades;++i)
            trade.add(new RandomItemGenerator());
    }


    public void drawSeller() {

        Table trades = new Table();
        trades.defaults();
        trades.debug();
        trades.align(right|center|top);
        trades.padBottom(100).padRight(50).padTop(200);
        trades.add(new Label("        Offers", game.BaseLabelStyle1)).size(150, 50);
        trades.row();


//        Image temp = trade.getItemDocument().getImage();
//        temp.setSize(20 ,20);
//        trades.add(temp).size(100,100);
//        trades.add(makeitemRow(trade.get(0)));
//        trades.row();
        for (int i = 0; i <numberOfTrades;++i){
        trades.add(makeitemRow(trade.get(i)));
        trades.row();
        }

//        HiddenInventoryTable.align(center);
        tableOverlays.addActor(trades);
    }
    public Table makeitemRow(final RandomItemGenerator currentTrade){
        Table table = new Table();
//        Image temp = currentTrade.getItemDocument().getImage();
//        temp.setSize(20 ,20);
//        table.add(temp).size(70,70);
        itemSlot slot = new itemSlot(currentTrade.getItemDocument());
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
                    game.player.setGold(availableGold - itemPrice);
                            gold.setText("       Gold : " +String.valueOf(game.player.getGold()));
                            boolean availableSlot = true;
                    for(int i =0;i<game.inventoryItems.size;++i){
                        if(game.inventoryItems.get(i).setDefauls &&availableSlot)
                                {
                                    doc.setIndex(String.valueOf(i));
                                    doc.setDefauls = false;
                                    game.inventoryItems.set(i,doc);
                                    availableSlot = false;
                                    updateSlots = true;
//                                    break;
                                }
                                else{

                                }

                    }
//                    game.inventoryItems = test;

                }

            };
        });
        return table;
    }
}
