package ScreenOverlayRework.Inventory.NPCInventoryUI;

import ScreenOverlayRework.Inventory.itemDocument;
import ScreenOverlayRework.Inventory.itemSlot;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

public class SellerItemSlot extends itemSlot {
    final boolean[] isOverSlot = {false};
    final boolean[] isOverItem = {false};
    final Label cancelLabel;
    Array<itemSlot> slots;

    itemDocument itemDocument;
    public SellerItemSlot(BladeAndTomes game,
                    itemDocument doc,
                    DragAndDrop DND,
                    AssetManager manager,
                        Array<itemSlot> slots){
        super();
        this.slots=slots;
        this.manager=manager;
        BaseSlotTexturePath = "SkinAssets/Inventory/Slot/SlotUI";
        table = new Group();
                cancelLabel = new Label("   CANCEL \n   TRADE!", game.BaseLabelStyle2);

        if(manager.contains(BaseSlotTexturePath+".json",Skin.class)){
            slotSkin = manager.get(BaseSlotTexturePath+".json",Skin.class);
        } else{
            manager.load(BaseSlotTexturePath+".json",Skin.class,new SkinLoader.SkinParameter(BaseSlotTexturePath+".atlas"));
            manager.finishLoading();
            slotSkin = manager.get(BaseSlotTexturePath+".json",Skin.class);
        }
        this.doc = doc;
        slot = new ImageButton(slotSkin);
         final Label.LabelStyle style = game.BaseLabelStyle2;
        slot.setSize(100,100);
        table.addActor(slot);
        item = new Image();
        itemDocument = new itemDocument();
        item.setName(itemDocument.getName());
        item.setSize(65,65);
        item.setPosition(slot.getWidth()*0.15f,slot.getWidth()*0.15f);
        table.addActor(item);
        this.game = game;
        dnd = DND;
        final boolean[] isOverCancelLabel = {false};
        cancelLabel.setSize(75,75);
        cancelLabel.setPosition(10,10);

        item.setDebug(true);
        slot.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {

                if(sellingObj) {
                    slot.addActor(cancelLabel);
                    item.setVisible(false);
                }
                super.enter(event, x, y, pointer, fromActor);
            }


            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                slot.removeActor(cancelLabel);
                item.setVisible(true);
                super.exit(event, x, y, pointer, toActor);
            }
        });
        cancelLabel.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                isOverItem[0] = true;
                if(sellingObj){
                    table.addActor(cancelLabel);
                }
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean addBack = addBackToInventory(itemDocument);
                if(addBack){

                    slot.removeActor(cancelLabel);

                    tradeComplete();
                } else{
                    cancelLabel.setText("No Slots Empty!");
                }
                super.clicked(event, x, y);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                {
                    table.removeActor(cancelLabel);
                }
                super.exit(event, x, y, pointer, toActor);
            }
        });
        sellingObj = false;
        currentPayload = null;
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
        item.addListener(info);
    }

    public boolean addBackToInventory(itemDocument newItemDoc){
        boolean availableSlot = true;
        for(int i =0;i<game.player.inventoryItems.size;++i){
            if(game.player.inventoryItems.get(i).setDefauls &&availableSlot&&i<14)
            {
                newItemDoc.setIndex(String.valueOf(i));
                newItemDoc.setDefauls = false;
                game.player.inventoryItems.set(i,newItemDoc);
                itemSlot slot = slots.get(i);
                slot.getItem().setDrawable(newItemDoc.getImage(manager).getDrawable());
                slot.displayInfo();

                return true;
            }
        }
        return false;
    }

    @Override
    public void applyTarget() {
        dnd.addTarget(new DragAndDrop.Target(table) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {

                try {

                    int sellingItemIndex = Integer.valueOf(((Image) payload.getObject()).getName());
                    int sellingItemlvl = game.player.inventoryItems.get(sellingItemIndex).getLevel();
                    String sellingItemCategory = game.player.inventoryItems.get(sellingItemIndex).getCategory();
                    String sellingItemName = game.player.inventoryItems.get(sellingItemIndex).getName();

                    sellingObj = doc.getName().equalsIgnoreCase(sellingItemName) &&
                            doc.getCategory().equalsIgnoreCase(sellingItemCategory) &&
                            doc.getLevel()==sellingItemlvl;
                    System.out.println(sellingObj);

                    return sellingObj;

                } catch (Exception e){
                    System.out.println(e);

                    sellingObj = false;

                }

                return sellingObj;
            }
            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                int sellingItemIndex = Integer.valueOf(((Image) payload.getObject()).getName());
                    Drawable temp = ((Image) payload.getObject()).getDrawable();
                    ((Image) payload.getObject()).setDrawable(item.getDrawable());
                    item.setDrawable(temp);
                    itemDocument tempDoc = game.player.inventoryItems.get(sellingItemIndex);
                    itemDocument.setIndex(String.valueOf(sellingItemIndex));
                    game.player.inventoryItems.set(sellingItemIndex,itemDocument);
                    itemDocument = tempDoc;
                    ((Image) payload.getObject()).setName(String.valueOf(sellingItemIndex));
//                    item.setName("BUY");
            }});
    }


    public void tradeComplete(){
        sellingObj = false;
        itemDocument = new itemDocument();
        item.setName(itemDocument.getName());
        item.setDrawable(null);
    }

}
