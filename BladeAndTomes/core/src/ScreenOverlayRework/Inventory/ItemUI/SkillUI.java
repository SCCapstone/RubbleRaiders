package ScreenOverlayRework.Inventory.ItemUI;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class SkillUI {
    private Table table;
    private final BladeAndTomes game;
    private  AssetManager assets;
    private Label tile;



    public SkillUI(BladeAndTomes game, AssetManager mainItemsUIManager) {
        this.game = game;
        this.assets = mainItemsUIManager;
        table = new Table();
        tile = new Label("",game.BaseLabelStyle1);
        drawSkillTable();
    }
    public void drawSkillTable()
    {
         tile.setText("        Tokens: "+game.player.tokens);

        table.add(tile).size(150,50);

    }
    public void updateTokens(){
        tile.setText("        Tokens: "+game.player.tokens);
    }
    public void render(){
        updateTokens();
    }

    public Table getTable(){
        return table;
    }
}
