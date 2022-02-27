package ScreenOverlayRework.Inventory.NPCTrades;

import ScreenOverlayRework.Inventory.itemSlot;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.utils.Align.*;

public class npcUI {
    protected Table table;
    protected BladeAndTomes game;
    protected Table HiddenInventoryTable;
    protected DragAndDrop dnd;
    protected String Title;
    protected Group tableOverlays = new Stack();
    protected Label gold;
    public boolean updateSlots;


    public npcUI(BladeAndTomes GAME, DragAndDrop dnd, String title) {
        Title = title;
        game = GAME;

        // Trading Table
        table = new Table();
        HiddenInventoryTable = new Table();
        this.dnd = dnd;


        // Making the Base Table
        table.setDebug(true);
        table.background(game.BaseLabelStyle2.background);
        table.defaults();
        table.setBounds(game.stageInstance.getWidth() * 0.2f, game.stageInstance.getHeight() * 0.2f,
                game.stageInstance.getWidth() * 0.5f, game.stageInstance.getHeight() * 0.57f);
        table.add(tableOverlays).row();

        // Drawing the Top Layer Stuff in Table
        drawTopLayer();

        // Users Items
        HiddenInventoryTable.setDebug(true);
        HiddenInventoryTable.left().padTop(100).padLeft(50);
        tableOverlays.addActor(HiddenInventoryTable);
        drawHiddenInventorySlots();

    }

    public void drawTopLayer() {
        // Top Layer Table
        Table topLayer = new Table();
        topLayer.defaults();
        tableOverlays.addActor(topLayer);


        // Adding Title
        Label title = new Label("       " + Title, game.BaseLabelStyle1);
        topLayer.add(title).size(150, 100).padTop(10).colspan(3);

        // Adding Gold Available

        gold = new Label("       Gold : " +String.valueOf(game.player.getGold()), game.BaseLabelStyle1);
        topLayer.row();
        topLayer.add(gold).size(150, 50).colspan(3);
        topLayer.row();

        // Adding Dividers Available

        Label line3 = new Label("", game.BaseLabelStyle1);
        topLayer.add(line3).size(400, 10).top();
        Label line = new Label("", game.BaseLabelStyle1);
        topLayer.add(line).size(10, 450).top();
        Label line2 = new Label("", game.BaseLabelStyle1);
        topLayer.add(line2).size(400, 10).top();

    }

    public void drawHiddenInventorySlots() {

        // Drawing Hidden General Inventory Slots

        Table generalItems = new Table();
        generalItems.defaults();
        generalItems.add(new Label("        Your Items", game.BaseLabelStyle1)).size(150, 50).colspan(3);
        for (int i = 5; i < 14; ++i) {

            itemSlot temp = new itemSlot(game, dnd, i, "Any");
            temp.applySource();
            temp.applyTarget();
            dnd = temp.getDND();
            if ((i+1) % 3 == 0) {
                generalItems.row();
            }
            generalItems.add(temp.getSlot()).size(100, 100);
        }
//        HiddenInventoryTable.align(center);
        HiddenInventoryTable.add(generalItems).row();

    }
    public void updateLabels(){
        gold.setText("       Gold : " +String.valueOf(game.player.getGold()));
    }

    public Table getTable() {
        return table;
    }
}
