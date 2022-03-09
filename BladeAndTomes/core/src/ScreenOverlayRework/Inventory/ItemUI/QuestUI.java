package ScreenOverlayRework.Inventory.ItemUI;

import ScreenOverlayRework.Inventory.ItemUI.Quest.QuestDocument;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class QuestUI {
    private BladeAndTomes game;
    private AssetManager manager;

    private Table table;
    public QuestUI(BladeAndTomes game, AssetManager questManager) {

        this.game = game;
        this.manager = questManager;
        table = new Table();
        QuestDocument doc = new QuestDocument(game,questManager);
        table.add(doc.getTable());

    }

    public Table getTable(){
        return table;
    }
}
