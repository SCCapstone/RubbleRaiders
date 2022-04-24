package ScreenOverlayRework.Inventory.NPCInventoryUI;

import ScreenOverlayRework.Inventory.itemDocument;
import ScreenOverlayRework.Inventory.itemSlot;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;

public class SellerItemSlot extends itemSlot {

    Array<itemSlot> slots;
    itemDocument itemDocument;
    Label info;
    Label displayItemMatch;
    String initialItemLabelText;
    boolean isOverSlot;

    public SellerItemSlot(BladeAndTomes game, final itemDocument doc, DragAndDrop DND, final AssetManager manager, Array<itemSlot> slots, Label info) {
        super();
        initialItemLabelText = info.getText().toString();
        this.info = info;
        this.slots = slots;
        this.manager = manager;
        BaseSlotTexturePath = "SkinAssets/Inventory/Slot/SlotUI";
        table = new Group();
        cancelLabel = new Label("   CANCEL \n   TRADE!", game.BaseLabelStyle2);

        if (manager.contains(BaseSlotTexturePath + ".json", Skin.class)) {
            slotSkin = manager.get(BaseSlotTexturePath + ".json", Skin.class);
        } else {
            manager.load(BaseSlotTexturePath + ".json", Skin.class, new SkinLoader.SkinParameter(BaseSlotTexturePath + ".atlas"));
            manager.finishLoading();
            slotSkin = manager.get(BaseSlotTexturePath + ".json", Skin.class);
        }
        isBuyer = true;
        this.doc = doc;
        slot = new ImageButton(slotSkin);
        final Label.LabelStyle style = game.BaseLabelStyle2;
        slot.setSize(100, 100);
        table.addActor(slot);
        slot.getListeners().clear();
        item = doc.getImage(manager);
        itemDocument = new itemDocument();
        item.setName(itemDocument.getName());
        item.setSize(65, 65);
        item.setColor(item.getColor().r, item.getColor().g, item.getColor().b, 0.25f);
        item.setPosition(slot.getWidth() * 0.15f, slot.getWidth() * 0.15f);
        table.addActor(item);
        this.game = game;
        dnd = DND;
        cancelLabel.setSize(75, 75);
        cancelLabel.setPosition(10, 10);
        cancelLabel.setFontScale(.63f);
        displayItemMatch = new Label("", game.BaseLabelStyle2);
        displayItemMatch.setSize(75, 75);
        displayItemMatch.setPosition(10, 10);
        cancelLabel.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (sellingObj) {
                    table.addActor(cancelLabel);
                    cancelLabel.getColor().a = 1.0f;
                }
                super.enter(event, x, y, pointer, fromActor);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean addBack = addBackToInventory(itemDocument);
                if (addBack) {
                    table.removeActor(cancelLabel);
                    tradeComplete();
                    item.setDrawable(doc.getImage(manager).getDrawable());
                } else {
                    cancelLabel.setText("No Slots Empty!");

                }
                super.clicked(event, x, y);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                {
                    cancelLabel.getColor().a = 0f;
                }
                super.exit(event, x, y, pointer, toActor);
            }
        });
        cancelLabel.getColor().a = 1.0f;
        sellingObj = false;
        isOverSlot = false;
    }

    public boolean addBackToInventory(itemDocument newItemDoc) {
        boolean availableSlot = true;
        for (int i = 0; i < game.player.inventoryItems.size; ++i) {
            if (game.player.inventoryItems.get(i).setDefauls && availableSlot && i < 14) {
                newItemDoc.setIndex(String.valueOf(i));
                newItemDoc.setDefauls = false;
                game.player.inventoryItems.set(i, newItemDoc);
                itemSlot slot = slots.get(i);
                slot.getItem().setDrawable(newItemDoc.getImage(manager).getDrawable());
                slot.getItem().setColor(newItemDoc.getImage(manager).getColor());
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
            public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                if (isOverSlot) {
                    table.removeActor(displayItemMatch);
                    info.setText(initialItemLabelText);
                    info.setFontScale(0.63f);
                    isOverSlot = false;
                }
                super.reset(source, payload);
            }

            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                try {
                    int sellingItemIndex = Integer.valueOf(((Image) payload.getObject()).getName());
                    int sellingItemlvl = game.player.inventoryItems.get(sellingItemIndex).getLevel();
                    String sellingItemCategory = game.player.inventoryItems.get(sellingItemIndex).getCategory();
                    String sellingItemName = game.player.inventoryItems.get(sellingItemIndex).getName();
                    boolean itemTypeEquals = doc.getName().equalsIgnoreCase(sellingItemName);
                    boolean itemCategoryEquals = doc.getCategory().equalsIgnoreCase(sellingItemCategory);
                    boolean itemLevelEquals = doc.getLevel() == sellingItemlvl;


                    sellingObj = itemTypeEquals && itemCategoryEquals && itemLevelEquals;
                    if (!sellingObj) {
                        info.setText("          Provided: \n          " + "  Level " + sellingItemlvl + " " + sellingItemName.toUpperCase() + "\n" + "          Wants:\n          " + " Level: " + doc.getLevel() + "" + doc.getName().toUpperCase());
                        info.setFontScale(.63f);
                        displayItemMatch.setText(" CAN'T\n TRADE\n ITEM");
                        table.addActor(displayItemMatch);
                    } else {
                        displayItemMatch.setText("    DROP");
                        table.addActor(displayItemMatch);
                    }
                    isOverSlot = true;
                    return sellingObj;

                } catch (Exception e) {

                    sellingObj = false;

                }

                return sellingObj;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                int sellingItemIndex = Integer.valueOf(((Image) payload.getObject()).getName());
                Color color = ((Image) payload.getObject()).getColor();
                ((Image) payload.getObject()).setDrawable((new Image()).getDrawable());
                item.getColor().a = 1;
                itemDocument tempDoc = game.player.inventoryItems.get(sellingItemIndex);
                itemDocument.setIndex(String.valueOf(sellingItemIndex));
                game.player.inventoryItems.set(sellingItemIndex, itemDocument);
                itemDocument = tempDoc;
                ((Image) payload.getObject()).setName(String.valueOf(sellingItemIndex));
                cancelLabel.setName("   CANCEL \n   TRADE!");
                table.addActor(cancelLabel);
                cancelLabel.getColor().a = 1f;


//                    item.setName("BUY");
            }
        });
    }

    public void setBackDrawable() {
    }


    public void tradeComplete() {
        sellingObj = false;
        itemDocument = new itemDocument();
        item.setName(itemDocument.getName());
        item.setColor(item.getColor().r, item.getColor().g, item.getColor().b, 0.25f);
    }

}
