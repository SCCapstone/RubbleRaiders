package ScreenOverlayRework.Inventory;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Random;


public class itemSlot extends Actor {
    /*
    - Anirudh Oruganti
        - While making this class I often referred to following source below
            -- https://www.youtube.com/watch?v=iHQ2UsoWhCE
            -- https://github.com/Wire2D/libGDX---wire2D/tree/master/core/src/com/game/Inventory
            -- "Chapter 4. Where Do I Put My Stuff?" in Mastering LibGDX Game Development Sented by Shon
            -- Libgdx discord issues : https://discord.gg/pAHEfgya
            -- Libgdx docs: https://libgdx.com/wiki/
     */
    private Image item;
    private Skin slotSkin;
    private ImageButton slot;
    private Group table;
    private DragAndDrop dnd;
    private BladeAndTomes game;
    private int documentIndex;
    private String targetSlot;


    /*
    TODO
        DND - DRAG AND DROP CLASS
        index - THE SLOT'S INDEX
        targetSlot - THE SLOT INFO
     */
    public itemSlot(BladeAndTomes GAME,
                    DragAndDrop DND,
                    int index,
                    String targetSlot) {
        game = GAME;
        dnd=DND;
        this.targetSlot = targetSlot;
        documentIndex = index;

        slotSkin = new Skin(Gdx.files.internal("SkinAssets/Inventory/Slot/SlotUI.json"),
                new TextureAtlas(Gdx.files.internal("SkinAssets/Inventory/Slot/SlotUI.atlas")));
        int ran = ((new Random()).nextInt(3));
        Texture[] arr = {new Texture(Gdx.files.internal("Goblin.png")),
                new Texture(Gdx.files.internal("GoldChest.jpg")),null};

        slot = new ImageButton(slotSkin);
        table = new Group();
        slot.setSize(100,100);
        table.addActor(slot);
        setItem();
    }
    // THIS METHOD CREATES AN ITEM AND SLOT
    public void setItem(){

        if(game.inventoryItems.get(documentIndex).setDefauls){
            item = new Image();
        }
        else{
            item = game.inventoryItems.get(documentIndex).getImage();
        }
        item.setName(game.inventoryItems.get(documentIndex).getName());
        item.setSize(70,70);
        item.setPosition(slot.getWidth()*0.15f,slot.getWidth()*0.15f);
        table.addActor(item);
    }
    // THIS CLASS LETS ITEM TO BE DRAGGED
    public void applySource(){
        dnd.addSource(new DragAndDrop.Source(item) {
            final DragAndDrop.Payload payload = new DragAndDrop.Payload();
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                payload.setObject(item);
                Image actor =(Image) payload.getObject();
                item.setVisible(false);
                Image temp = new Image(actor.getDrawable());
                temp.setSize(75,75);
                payload.setDragActor(temp);
                payload.setInvalidDragActor(temp);
                payload.setValidDragActor(temp);
                dnd.setDragActorPosition(actor.getImageWidth()/2,
                        -actor.getImageHeight()/2);
                return payload;
            }
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer,
                                 DragAndDrop.Payload payload,
                                 DragAndDrop.Target target) {
                if(target == null)
                    ((Image) payload.getObject()).setVisible(true);
                else
                    ((Image) payload.getObject()).setVisible(true);
                ;            }
        });

    }

    // THIS METHOD CHECKS IF A DRAGGING ITEM COULD BE MOVED INTO A SLOT
    public void applyTarget(){
        dnd.addTarget(new DragAndDrop.Target(table){
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                int newItem =Integer.valueOf(((Image) payload.getObject()).getName());
                int currentItem = Integer.valueOf(item.getName());

                boolean sourceItemSlot = (targetSlot.equalsIgnoreCase(game.inventoryItems.get(newItem).getCategory())
                        || targetSlot.equalsIgnoreCase("Any"));
                boolean targetItemSlot = (targetSlot.equalsIgnoreCase(game.inventoryItems.get(currentItem).getCategory())
                        || targetSlot.equalsIgnoreCase("Any") || "NULL".equalsIgnoreCase(game.inventoryItems.get(currentItem).getCategory()));
                return (!game.inventoryItems.get(newItem).getTargetItem().equalsIgnoreCase("NUll")) && targetItemSlot && sourceItemSlot;
            }
            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {

                int newItem = Integer.valueOf(((Image) payload.getObject()).getName());
                int currentItem = Integer.valueOf(item.getName());

                Drawable temp = ((Image) payload.getObject()).getDrawable();
                ((Image) payload.getObject()).setDrawable(item.getDrawable());
                item.setDrawable(temp);
//                System.out.println(" Source: "+((Image) payload.getObject()).getName()+" Target: "+item.getName());
                game.inventoryItems.swap(newItem,currentItem);

            }
        });

    }

    public DragAndDrop getDND(){
        return dnd;
    }
    public Group getSlot(){
        return table;
    }
}
