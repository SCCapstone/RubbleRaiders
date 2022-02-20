package ScreenOverlayRework;


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

import static com.badlogic.gdx.utils.Align.left;
import static com.badlogic.gdx.utils.Align.top;

public class OverlayManager {
    final BladeAndTomes game;
    TextButton[] inventorySloat =new TextButton[5];
    Table table;
    Array<TextButton> slots =new Array<TextButton>();

    InventoryUI inventory;
    public OverlayManager(BladeAndTomes Game){
        game = Game;
        table = new Table();
        table.setDebug(true);
        table.defaults();
        table.setBounds(0,0,Game.stageInstance.getWidth(),Game.stageInstance.getHeight());
        // Inventory
        inventory = new InventoryUI(game);
        table.align(top|left).padLeft(10);
        game.stageInstance.addActor(table);
    }


}
