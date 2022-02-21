package ScreenOverlayRework;


import ScreenOverlayRework.Health.Health;
import ScreenOverlayRework.Inventory.InventoryUI;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.creatures.Inventory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Queue;
import org.w3c.dom.Text;

import static com.badlogic.gdx.utils.Align.*;

public class OverlayManager {
    private final BladeAndTomes game;
    private Table table;
    private InventoryUI inventory;
    private Health healthBar;
    public OverlayManager(BladeAndTomes Game){
        game = Game;
        table = new Table();
        table.setDebug(true);
        table.defaults();
        table.setBounds(0,0,Game.stageInstance.getWidth(),Game.stageInstance.getHeight());
        game.stageInstance.addActor(table);


        // Inventory
        inventory = new InventoryUI(game);
        table.align(top|left).padLeft(10);
        table.addActor(inventory.getUI());

        // Health Bar
        healthBar = new Health(game);
        table.align(top|right).padLeft(10);
        table.addActor(healthBar.getHealthBar());

    }


    public void setOverLayesVisibility(boolean value) {
        game.stageInstance.addActor(table);
        table.setVisible(value);
    }
    public void updateHealth(){
        healthBar.update();
    }
}
