package ScreenOverlayRework.Inventory.ItemUI;

import ScreenOverlayRework.Inventory.itemSlot;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.top;

public class GeneralItemUI {
    final private BladeAndTomes game;
    private transient DragAndDrop dnd;
    private Table table;
    private Table tableMain;

    private AssetManager itemsManager;
    private Array<itemSlot> slots;
    public GeneralItemUI(BladeAndTomes GAME,
                         DragAndDrop dnd,
                         AssetManager itemsManager,
                         Array<itemSlot> slots){

        // Setting Pointer Object
        game = GAME;
        this.dnd = dnd;
        this.itemsManager = itemsManager;
        this.slots = slots;
        tableMain = new Table();
        table = new Table();
        tableMain.add(table);

        create();
    }

    public void create(){


        // Drawing Hidden General Inventory Slots
        Table generalItems = new Table();
        Label generalItemsInfo = new Label("\tGeneral Items", game.BaseLabelStyle1);
        generalItemsInfo.setFontScale(0.7f);
        generalItems.add(generalItemsInfo).size(175, 50).colspan(3);
        for (int i = 5; i < 14; ++i) {

            itemSlot temp = new itemSlot(game, dnd, i, "Any",itemsManager);
            temp.applySource();
            temp.applyTarget();
            dnd = temp.getDND();
            slots.add(temp);
            if ((i+1) % 3 == 0) {
                generalItems.row();
            }
            generalItems.add(slots.get(i).getSlot()).size(100, 100);
        }
        table.add(generalItems).colspan(6);
        table.row();

        // Drawing Hidden Sell Item Inventory Slots
        Table SpellItems = new Table();
        SpellItems.align(top | center);
        for (int i = 14; i < 16; ++i) {

            itemSlot temp = new itemSlot(game, dnd, i, "Spell",itemsManager);
            temp.applySource();
            temp.applyTarget();
            dnd = temp.getDND();
            slots.add(temp);
            if ((i+1) % 3 == 0) {
                generalItems.row();
            }
            generalItems.add(slots.get(i).getSlot()).size(100, 100);
        }
        table.add(SpellItems).top();

        // Drawing Hidden Armor Inventory Slots
        Table ArmorItems = new Table();

        for (int i = 16; i < 17; ++i) {

            itemSlot temp = new itemSlot(game, dnd, i, "Armor",itemsManager);
            temp.applySource();
            temp.applyTarget();
            dnd = temp.getDND();
            slots.add(temp);
            if ((i+1) % 3 == 0)
                SpellItems.row();
            generalItems.add(slots.get(i).getSlot()).size(100, 100);
        }
        table.add(ArmorItems).colspan(10);
    }

    public Table getTable(){
        return tableMain;
    }
}
