package ScreenOverlayRework.Inventory.ItemUI;

import ScreenOverlayRework.Inventory.itemDocument;
import ScreenOverlayRework.Inventory.itemSlot;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.utils.Align.topLeft;

public class MainItemsUI {
    final private BladeAndTomes game;
    private transient DragAndDrop dnd;
    private Table table;
    private AssetManager itemsManager;
    private Array<itemSlot> slots;

    public MainItemsUI(BladeAndTomes GAME,
                       DragAndDrop dnd,
                       AssetManager itemsManager,
                       Array<itemSlot> slots){
        // Setting Pointer Object
        game = GAME;
        this.dnd = dnd;
        this.itemsManager = itemsManager;
        this.slots = slots;

        // Initializing table
        table = new Table();
        table.setDebug(true);
        table.defaults();
        table.setBounds(0, 0, game.stageInstance.getWidth(), game.stageInstance.getHeight());
        table.align(topLeft).padLeft(10);

        // Crates Main Inventory Layout and adds items
        create();
    }

    public void create(){
        for (int i = 0; i < 5; ++i) {
            itemSlot temp = new itemSlot(game, dnd, i, "Any",itemsManager);
            temp.applySource();
            temp.applyTarget();
            slots.add(temp);
            table.add(slots.get(i).getSlot()).size(100, 100);
        }
    }

    public Table getTable(){
        return table;
    }

}
