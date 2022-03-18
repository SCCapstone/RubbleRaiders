package ScreenOverlayRework.Inventory.ItemUI;

import ScreenOverlayRework.Inventory.ItemUI.Quest.QuestDocument;
import ScreenOverlayRework.Inventory.ItemUI.Quest.QuestTradeElements;
import ScreenOverlayRework.Inventory.ItemUI.Quest.QuestUIElements;
import ScreenOverlayRework.Inventory.ItemUI.Quest.QuestUIElements;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

public class QuestUI {
    private BladeAndTomes game;
    private AssetManager manager;

    private Table table;
    private Array<QuestUIElements> currentQuests;
    public QuestUI(BladeAndTomes game, AssetManager questManager) {

        this.game = game;
        this.manager = questManager;
        table = new Table();
        currentQuests = new Array<>();
        for (int i = 0; i < 4;++i){
            QuestUIElements doc = new QuestUIElements(game,manager,game.player.activeQuests.get(i),250,25*(17-5*i),i);
            if(game.player.activeQuests.get(i)!=null)
           System.out.println( game.player.activeQuests.get(i).getQuestDescription());
            currentQuests.add(doc);
            table.addActor(doc.getTable());
        }

    }
    public void updateIndex(int i){
        currentQuests.get(i).updateNewQuest(game.player.activeQuests.get(i));
    }

    public Table getTable(){
        return table;
    }
}
