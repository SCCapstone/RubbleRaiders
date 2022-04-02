package ScreenOverlayRework.Inventory;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
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
    protected Image item;
    protected Skin slotSkin;
    protected ImageButton slot;
    protected Group table;
    protected DragAndDrop dnd;
    protected BladeAndTomes game;
    protected int documentIndex;
    protected String targetSlot;
    protected DragAndDrop.Source sourceLister;
    protected DragAndDrop.Target targetLister;
    protected itemDocument doc;
    protected boolean sellingObj;
    protected String removeIndexDoc;
    protected DragAndDrop.Payload currentPayload;
    protected boolean swappingObj;
    protected AssetManager manager;
    protected TextTooltip info;
    protected ImageButton imageButtonAddOn;

    /*
    TODO
        DND - DRAG AND DROP CLASS
        index - THE SLOT'S INDEX
        targetSlot - THE SLOT INFO
     */
    protected String BaseSlotTexturePath;

    /*
    TODO
        DND - DRAG AND DROP CLASS
        index - THE SLOT'S INDEX
        targetSlot - THE SLOT INFO

     */
    protected String itemInfo ="";

    public itemSlot(BladeAndTomes GAME,
                    DragAndDrop DND,
                    int index,
                    String targetSlot,
                    AssetManager manager) {
        this.manager=manager;
        game = GAME;
        dnd=DND;
        this.targetSlot = targetSlot;
        documentIndex = index;

        BaseSlotTexturePath = "SkinAssets/Inventory/Slot/SlotUI";
        if(!manager.contains(BaseSlotTexturePath+".json",Skin.class)){
            manager.load(BaseSlotTexturePath+".json",Skin.class,new SkinLoader.SkinParameter(BaseSlotTexturePath+".atlas"));
            manager.finishLoading();
        }
        slotSkin = manager.get(BaseSlotTexturePath+".json",Skin.class);



        slot = new ImageButton(slotSkin);
        table = new Group();
        slot.setSize(100,100);
        table.addActor(slot);
        if(targetSlot.equalsIgnoreCase("Spell")){
            if(!manager.contains("InventoryItems/Other/SpellArt/SpellBase"+".json",Skin.class)){
                manager.load("InventoryItems/Other/SpellArt/SpellBase"+".json",Skin.class,new SkinLoader.SkinParameter("InventoryItems/Other/SpellArt/SpellBase"+".atlas"));
                manager.finishLoading();
            }
             imageButtonAddOn = new ImageButton(manager.get("InventoryItems/Other/SpellArt/SpellBase"+".json",Skin.class));
            imageButtonAddOn.setSize(60,60);
            imageButtonAddOn.setPosition(20,20);
            table.addActor(imageButtonAddOn);
        }else if(targetSlot.equalsIgnoreCase("armor")){
            if(!manager.contains("InventoryItems/Other/ArmorBase/ArmorBase"+".json",Skin.class)){
                manager.load("InventoryItems/Other/ArmorBase/ArmorBase"+".json",Skin.class,new SkinLoader.SkinParameter("InventoryItems/Other/ArmorBase/ArmorBase"+".atlas"));
                manager.finishLoading();
            }
            imageButtonAddOn = new ImageButton(manager.get("InventoryItems/Other/ArmorBase/ArmorBase"+".json",Skin.class));
            imageButtonAddOn.setSize(60,60);
            imageButtonAddOn.setPosition(20,20);
            table.addActor(imageButtonAddOn);
        }

        setItem();
        swappingObj = true;
        Skin toolTip;
        String ToolTpPath = "InventoryItems/Other/SlotTextToolTip/SlotTextToolTip";
        if(manager.contains(ToolTpPath+".json",Skin.class)){
            toolTip = manager.get(ToolTpPath+".json",Skin.class);
        } else{
            manager.load(ToolTpPath+".json",Skin.class,new SkinLoader.SkinParameter(ToolTpPath+".atlas"));
            manager.finishLoading();
            toolTip = manager.get(ToolTpPath+".json",Skin.class);
        }
        info = new TextTooltip("",toolTip);

        info.setInstant(true);
        displayInfo();
        table.addListener(info);

    }
    public void displayInfo() {
        info.getActor().setText(String.valueOf(game.player.inventoryItems.get(documentIndex).getItemDescription()).toUpperCase());
        if(String.valueOf(game.player.inventoryItems.get(documentIndex).getItemDescription()).equalsIgnoreCase("")){
            table.removeListener(info);
            info.setInstant(false);
        }
        else {
            info.setInstant(true);

            table.addListener(info);
        }
    }

    public void displayInfo(itemDocument doc) {

        info.getActor().setText(String.valueOf(doc.getItemDescription()).toUpperCase());
        if(doc.getItemDescription().equalsIgnoreCase("")){
            table.removeListener(info);
        }
        else {
            table.addListener(info);
        }
    }

    public itemSlot() {

    }


    public boolean canBuy(){
        return sellingObj;
    }


    public void setDND(DragAndDrop dnd){
        this.dnd = dnd;
    }



    public void applySelection(boolean val){
        if(val)
        slot.setColor(Color.DARK_GRAY);
        else
            slot.setColor(Color.LIGHT_GRAY);

    }
    public itemSlot(itemDocument doc,AssetManager manager){
        BaseSlotTexturePath = "SkinAssets/Inventory/Slot/SlotUI";
        this.manager = manager;

        if(manager.contains(BaseSlotTexturePath+".json",Skin.class)){
            slotSkin = manager.get(BaseSlotTexturePath+".json",Skin.class);
        } else{
            manager.load(BaseSlotTexturePath+".json",Skin.class,new SkinLoader.SkinParameter(BaseSlotTexturePath+".atlas"));
            manager.finishLoading();
            slotSkin = manager.get(BaseSlotTexturePath+".json",Skin.class);
        }
        Skin toolTip;
        String ToolTpPath = "InventoryItems/Other/SlotTextToolTip/SlotTextToolTip";
        if(manager.contains(ToolTpPath+".json",Skin.class)){
            toolTip = manager.get(ToolTpPath+".json",Skin.class);
        } else{
            manager.load(ToolTpPath+".json",Skin.class,new SkinLoader.SkinParameter(ToolTpPath+".atlas"));
            manager.finishLoading();
            toolTip = manager.get(ToolTpPath+".json",Skin.class);
        }

        slot = new ImageButton(slotSkin);
        table = new Group();
        slot.setSize(100,100);
        table.addActor(slot);
        item = doc.getImage(manager);
        item.setName(doc.getName());
        item.setSize(65,65);
        item.setPosition(slot.getWidth()*0.15f,slot.getWidth()*0.15f);
        table.addActor(item);

        info = new TextTooltip("",toolTip);
        info.setInstant(true);

        table.addListener(info);
    }


    // THIS METHOD CREATES AN ITEM AND SLOT
    public void setItem(){

        item = game.player.inventoryItems.get(documentIndex).getImage(manager);
        item.setName(game.player.inventoryItems.get(documentIndex).getIndex());
        item.setSize(65,65);
        item.setPosition(slot.getWidth()*0.15f,slot.getWidth()*0.15f);
        table.addActor(item);
        try{
        displayInfo();
    }catch (Exception e){}
    }

    public void updateDrawable(){
        item.setDrawable(game.player.inventoryItems.get(documentIndex).getImage(manager).getDrawable());
        displayInfo();

    }
    // THIS CLASS LETS ITEM TO BE DRAGGED
    public void applySource(){
        dnd.addSource(sourceLister = new DragAndDrop.Source(item) {
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
                    ((Image) payload.getObject()).setVisible(true);


                try{
                    int sellingItemIndex = Integer.valueOf(((Image) payload.getObject()).getName());
                    int sellingItemlvl = game.player.inventoryItems.get(sellingItemIndex).getLevel();
                String sellingItemCategory = game.player.inventoryItems.get(sellingItemIndex).getCategory();
                String sellingItemName = game.player.inventoryItems.get(sellingItemIndex).getName();

                    sellingObj = doc.getName().equalsIgnoreCase(sellingItemName) &&
                            doc.getCategory().equalsIgnoreCase(sellingItemCategory) &&
                        doc.getLevel()==sellingItemlvl;
                    displayInfo();

                }catch (Exception e){
                    displayInfo();

                    sellingObj =false;
                }
            }
        });

    }


    public void removeItem(){
        try {
            int itemIndex = Integer.valueOf(documentIndex);
            itemDocument itemTemp = new itemDocument();
            itemTemp.setIndex(String.valueOf(itemIndex));
            itemTemp.setTargetItem("Null");
            itemTemp.setCategory("Null");
            item.setDrawable((new Image()).getDrawable());
            game.player.inventoryItems.set(itemIndex,itemTemp);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    // THIS METHOD CHECKS IF A DRAGGING ITEM COULD BE MOVED INTO A SLOT
    public void applyTarget(){
        dnd.addTarget(targetLister = new DragAndDrop.Target(table){
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {

                try{
                int newItem =Integer.valueOf(((Image) payload.getObject()).getName());
                int currentItem = Integer.valueOf(item.getName());

                if(newItem==15||newItem==14)
                    return game.player.inventoryItems.get(currentItem).getCategory().equalsIgnoreCase("Spell")||(currentItem!=16&&game.player.inventoryItems.get(currentItem).getCategory().equalsIgnoreCase("NUll"));
                if(newItem==16)
                    return game.player.inventoryItems.get(currentItem).getCategory().equalsIgnoreCase("Armor")||((currentItem!=14&&currentItem!=15)&&game.player.inventoryItems.get(currentItem).getCategory().equalsIgnoreCase("NUll"));
                        if(targetSlot.equalsIgnoreCase("Spell")) {
                            targetSlot = "Spells";
                        }

                boolean sourceItemSlot = (targetSlot.equalsIgnoreCase(game.player.inventoryItems.get(newItem).getCategory()) || targetSlot.equalsIgnoreCase("Any"));
                boolean targetItemSlot = (targetSlot.equalsIgnoreCase(game.player.inventoryItems.get(currentItem).getCategory())
                        || targetSlot.equalsIgnoreCase("Any") || "NULL".equalsIgnoreCase(game.player.inventoryItems.get(currentItem).getCategory()));

                        return  (!game.player.inventoryItems.get(newItem).getTargetItem().equalsIgnoreCase("NUll")) && targetItemSlot && sourceItemSlot;}
                    catch (Exception e){
                        return false;
                    }
            }
            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {

                int newItem = Integer.valueOf(((Image) payload.getObject()).getName());
                int currentItem = Integer.valueOf(item.getName());
                System.out.println("Before Swap\t" + game.player.inventoryItems.get(newItem).getIndex()+"\t"+game.player.inventoryItems.get(currentItem).getIndex());
                game.player.inventoryItems.get(newItem).setIndex(String.valueOf(currentItem));
                game.player.inventoryItems.get(currentItem).setIndex(String.valueOf(newItem));
                game.player.inventoryItems.swap(newItem, currentItem);
                game.player.inventoryItems.get(newItem).setIndex(String.valueOf(currentItem));
                game.player.inventoryItems.get(currentItem).setIndex(String.valueOf(newItem));

                Drawable temp = ((Image) payload.getObject()).getDrawable();
                ((Image) payload.getObject()).setDrawable(item.getDrawable());
                item.setDrawable(temp);
                displayInfo();
                System.out.println("After Swap\t" + game.player.inventoryItems.get(newItem).getIndex()+"\t"+game.player.inventoryItems.get(currentItem).getIndex());
                if(newItem == 16 ||currentItem == 16)
                    game.player.playerDefence = game.player.inventoryItems.get(16).getDamage();
            }
        });
    }

    public DragAndDrop.Source getSourceLister() {
        return sourceLister;
    }

    public DragAndDrop.Target getTargetLister() {
        return targetLister;
    }

    public DragAndDrop getDND(){
        return dnd;
    }
    public Group getSlot(){
        return table;
    }
    public Image getItem(){
        return item;
    }
}
