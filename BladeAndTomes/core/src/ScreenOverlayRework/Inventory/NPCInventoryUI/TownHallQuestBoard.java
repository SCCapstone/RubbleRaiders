package ScreenOverlayRework.Inventory.NPCInventoryUI;

import ScreenOverlayRework.Inventory.ItemUI.Quest.QuestDocument;
import ScreenOverlayRework.Inventory.ItemUI.Quest.QuestTradeElements;
import ScreenOverlayRework.Inventory.ItemUI.QuestUI;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class TownHallQuestBoard extends TradeUI{
    private BladeAndTomes game;
    private AssetManager manager;
    private Array<QuestTradeElements> questTrades;
    public boolean isVisible = false;
    public TownHallQuestBoard(BladeAndTomes game, AssetManager itemsManager, QuestUI questUI) {
        super(game, null, itemsManager, null, "Town Hall \n       Quest Board", true);
        this.manager = itemsManager;
        Table table = new Table();
        questTrades = new Array<>();
        for (int i = 0; i < 4;++i){
            QuestDocument questDoc = new QuestDocument(randomDiff());
            QuestTradeElements doc = new QuestTradeElements(
                    game,
                    manager,
                    questDoc,
                    250,
                    25*(17-5*i),
                    i,
                    questUI,
                    gold);
            questTrades.add(doc);
            table.addActor(doc.getTable());
        }
        table.setPosition(400,-10);
        rightTable.addActor(table);
    }
    public String randomDiff(){

        int prob = (new Random()).nextInt(1000)+1;
        if (prob>800)
            return "HARD";
        else if (prob>500)
            return "MEDIUM";
        else
            return "EASY";
    }
    public void render(){
        updateGold();
    }

}
