package ScreenOverlayRework.Inventory.NPCTrades;

import ScreenOverlayRework.Inventory.itemDocument;
import ScreenOverlayRework.Inventory.itemSlot;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.utils.Align.*;

public class NPCBuyer extends  npcUI{
    private Array<RandomItemGenerator> trade;
    private Table buyer;
    private int numberOfTrades;
    public boolean updateSlots;

    public NPCBuyer(BladeAndTomes GAME, DragAndDrop dnd) {
        super(GAME, dnd, "NPC Trader");
        buyer = new Table();
        trade = new Array<>();
        numberOfTrades = 3;
        generateTrades(numberOfTrades);
        drawBuyer();
        updateSlots = false;
    }
    private void generateTrades(int numberTrades){
        for(int i = 0;i <numberTrades;++i)
            trade.add(new RandomItemGenerator());
    }


    public void drawBuyer() {

        Table trades = new Table();
        trades.defaults();
        trades.debug();
        trades.align(right|center|top);
        trades.padBottom(100).padRight(50).padTop(200);
        trades.add(new Label("        Offers", game.BaseLabelStyle1)).size(150, 50);
        trades.row();

        for (int i = 0; i <numberOfTrades;++i){
            trades.add(makeitemRow(trade.get(i)));
            trades.row();
        }
        tableOverlays.addActor(trades);
    }
    public Table makeitemRow(final RandomItemGenerator currentTrade){
        Table table = new Table();
        final boolean tradeOK = false;
        final int price = (int) (currentTrade.getItemDocument().getPrice()*0.2);
        itemDocument doc = currentTrade.getItemDocument();
        final itemSlot slot = new itemSlot(game,doc, dnd);
        table.add(slot.getSlot()).size(100,100);
        slot.applySource();
        DragAndDrop.Target target =  slot.applyBuyerTarget();
        dnd.addTarget(target);

        table.add(currentTrade.getItemDocument().getImage()).size(50,50);
        TextButton button = new TextButton("Get "+String.valueOf(price)+" Gold",game.generalTextButtonStyle);
        table.add(button).size(150,70).colspan(3);

        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int availableGold = game.player.getGold();
                int itemPrice = price;
                final boolean tradeTrans = tradeOK;
                if(slot.canBuy()) {
                    game.player.setGold(availableGold + itemPrice);
                    gold.setText("       Gold : " +String.valueOf(game.player.getGold()));
                    slot.removeItem();
                    updateSlots = true;

                }

            };
        });
        return table;
    }
}
