package ScreenOverlayRework.Inventory.NPCInventoryUI;

import ScreenOverlayRework.Inventory.itemDocument;
import ScreenOverlayRework.Inventory.itemSlot;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.utils.Align.*;

public class NPCBuyer extends TradeUI {
    private Array<RandomItemGenerator> trade;
    private Table buyer;
    private int numberOfTrades;

    public NPCBuyer(BladeAndTomes game, DragAndDrop dnd, AssetManager itemsManager, Array<itemSlot> slots) {
        super(game, dnd, itemsManager, slots, "NPC Buyer",true);

        buyer = new Table();
        trade = new Array<>();
        numberOfTrades = 3;
        generateTrades(numberOfTrades);
        drawBuyer();
        buyer.setPosition(500,500);
        rightStack.addActor(buyer);
    }
    private void generateTrades(int numberTrades){
        for(int i = 0;i <numberTrades;++i)
            trade.add(new RandomItemGenerator(itemsManager));
    }

    public void drawBuyer() {
        Table trades = new Table();
        trades.defaults();
        trades.align(right|center|top);
        trades.add(new Label("        Offers", game.BaseLabelStyle1)).size(150, 50);
        trades.row();
        for (int i = 0; i <numberOfTrades;++i){
            trades.add(makeitemRow(trade.get(i)));
            trades.row();
        }
        trades.setPosition(875,450);
        buyer.addActor(trades);
    }
    public Table makeitemRow(final RandomItemGenerator currentTrade){
        Table table = new Table();
        final boolean tradeOK = false;
        final int price = (int) (currentTrade.getItemDocument().getPrice()*0.2);
        itemDocument doc = currentTrade.getItemDocument();
        String str = new String("           Trader is Buying:\n"+"           Item: "+doc.getName().toUpperCase()+"\n"+"           Level: "+String.valueOf(doc.getLevel()));
        Label itemLabel = new Label(str,game.BaseLabelStyle1);
        final SellerItemSlot slot = new SellerItemSlot(game,doc, dnd,itemsManager,slots,itemLabel);
        slot.applyTarget();
        Image itemImage = currentTrade.getItemDocument().getImage(itemsManager);


        itemImage.setColor(Color.BLACK);
        itemImage.setSize(50,50);
        itemImage.setPosition(15,15);
        //slot.getSlot().addActor(itemImage);//.size(50,50);
        table.add(slot.getSlot()).size(100,100);
        itemLabel.setFontScale(0.63f);
        table.add(itemLabel).size(260,80);
        TextButton button = new TextButton("Get "+String.valueOf(price)+" Gold",game.generalTextButtonStyle);
        table.add(button).size(100,80).colspan(3);

        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int availableGold = game.player.getGold();
                int itemPrice = price;
                final boolean tradeTrans = tradeOK;
                if(slot.canBuy()) {
                    game.player.setGold(availableGold + itemPrice);
                    gold.setText("       Gold : " +String.valueOf(game.player.getGold()));
                    slot.tradeComplete();
                    game.player.kTradesNPCBuyer+=1;
//                    slot.removeItem();
                }

            };
        });
        return table;
    }
}
