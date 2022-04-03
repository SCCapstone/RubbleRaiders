package ScreenOverlayRework.Inventory.NPCInventoryUI;

import ScreenOverlayRework.Inventory.itemSlot;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;

public class TradeUI {
    final protected BladeAndTomes game;
    protected transient DragAndDrop dnd;
    protected Table table,tableOverlays;
    protected Table leftTable;
    protected Table rightTable;
    protected AssetManager itemsManager;
    protected Array<itemSlot> slots;
    protected String Title;
    protected Label gold;
    protected Stack leftStack,rightStack;
    protected boolean haveGold;
    public TradeUI(BladeAndTomes game,
                     DragAndDrop dnd,
                     AssetManager itemsManager,
                     Array<itemSlot> slots,
                        String Title,
                   boolean showGold) {
        // Setting Pre-Loaded Pointers
        this.game = game;
        this.dnd = dnd;
        this.itemsManager = itemsManager;
        this.slots = slots;
        this.Title = Title;
        this.haveGold = showGold;

        // Creating a new Trade Table
        table = new Table();
        tableOverlays = new Table();
        leftTable = new Table();
        rightTable = new Table();
        table.background(game.BaseLabelStyle2.background);
        table.defaults();
        table.setBounds(game.stageInstance.getWidth() * 0.2f, game.stageInstance.getHeight() * 0.1f,
                game.stageInstance.getWidth() * 0.5f, game.stageInstance.getHeight() * 0.67f);


        leftStack = new Stack();
        rightStack = new Stack();

        table.add(tableOverlays).row();
        drawTopLayer();

        tableOverlays.addActor(leftTable);
        leftTable.add(leftStack);

        tableOverlays.addActor(rightTable);
        rightTable.add(rightStack);
    }

    public void addActorStack(Actor actor, float posX, float posY){
        updateGold();
        leftTable.setPosition(posX,posY);
        leftStack.addActor(actor);
    }
    public void removeActorStack(Actor actor){
        updateGold();
        leftStack.removeActor(actor);
    }


    public void drawTopLayer() {
        // Top Layer Table
        Table topLayer = new Table();
        topLayer.defaults();
        tableOverlays.add(topLayer);


        // Adding Title
        Label title = new Label("       " + Title, game.BaseLabelStyle1);
        topLayer.add(title).size(150, 100).padTop(10).colspan(3);

        // Adding Gold Available

        if(haveGold){
        gold = new Label("       Gold : " +String.valueOf(game.player.getGold()), game.BaseLabelStyle1);
        topLayer.row();
        topLayer.add(gold).size(150, 50).colspan(3);}
        topLayer.row();

        // Adding Dividers Available

        Label line3 = new Label("", game.BaseLabelStyle1);
        topLayer.add(line3).size(400, 10).top();
        Label line = new Label("", game.BaseLabelStyle1);
        topLayer.add(line).size(10, 500).top();
        Label line2 = new Label("", game.BaseLabelStyle1);
        topLayer.add(line2).size(400, 10).top();

    }
    public Table getTable(){
        return table;
    }
    public void updateGold(){
        gold.setText("       Gold : " +String.valueOf(game.player.getGold()));

    }
}
