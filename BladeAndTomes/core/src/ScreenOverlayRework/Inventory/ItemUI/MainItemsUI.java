package ScreenOverlayRework.Inventory.ItemUI;

import ScreenOverlayRework.Inventory.itemDocument;
import ScreenOverlayRework.Inventory.itemSlot;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
        table.defaults();
        table.setBounds(0, 0, game.stageInstance.getWidth(), game.stageInstance.getHeight());
        table.align(topLeft).padLeft(10);

        // Crates Main Inventory Layout and adds items
        create();
    }

    public void create(){
        for (int i = 0; i < 5; ++i) {
            final itemSlot temp = new itemSlot(game, dnd, i, "Any",itemsManager);
            temp.applySource();
            temp.applyTarget();
            final int finalI = i;
            game.stageInstance.addListener(new InputListener()
            {
                @Override
                public boolean keyDown(InputEvent event, int keycode)
                {
                    if(game.controls.getSelection(finalI)==keycode){
                        slots.get(game.currentInventorySelection).applySelection(false);
                        temp.applySelection(true);
                        game.currentInventorySelection = finalI;
                    }
                    if(game.controls.getFightAction() == keycode){
                        if(game.currentInventorySelection == finalI)
                            switch (game.player.inventoryItems.get(game.currentInventorySelection).getCategory()){
                                case "Spells":
                                    if(game.player.inventoryItems.get(game.currentInventorySelection).getName().equalsIgnoreCase("HealSpell")){
                                        int classHeal = game.player.getPlayerClass() == 1 || game.player.getPlayerClass() == 2 ? game.player.getMental()/2 : 0;
                                        int playerhealth = Math.min(game.player.inventoryItems.get(game.currentInventorySelection).getDamage()+game.player.getHealthPoints()+classHeal,10);
                                        game.player.setHealthPoints(playerhealth);
                                        temp.removeItem();
                                    } else if(game.player.inventoryItems.get(game.currentInventorySelection).getName().equalsIgnoreCase("StrengthSpell")){
                                    game.spellDamageIncrease = Math.min(game.player.inventoryItems.get(game.currentInventorySelection).getDamage(),10);
                                    temp.removeItem();
                                }
                                    break;
                                case "Armor":
                                    game.player.inventoryItems.swap(finalI, 16);
                                    slots.get(finalI).updateDrawable();
                                    slots.get(16).updateDrawable();
                                    game.player.playerDefence = game.player.inventoryItems.get(16).getDamage();
                                    break;
                            }
                        }
                    return true;
                }
            });
            if(game.currentInventorySelection == i)
                temp.applySelection(true);
            slots.add(temp);
            table.add(slots.get(i).getSlot()).size(100, 100);
        }
    }

    public Table getTable(){
        return table;
    }

}
