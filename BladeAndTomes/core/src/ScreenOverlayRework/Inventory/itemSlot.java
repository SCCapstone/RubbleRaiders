package ScreenOverlayRework.Inventory;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;


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
    // Image of the item which being held
    protected Image item;
    // Texture/Skin of the slot fir base
    protected Skin slotSkin;
    // Adding a overlay image button
    protected ImageButton slot;
    // A group which contains image, base slot image, a slot button
    protected Group table;
    // Drag and drop class which allows to check for target and source
    protected DragAndDrop dnd;
    // Universal Game class for communication
    protected BladeAndTomes game;
    // Index/id of current slot
    protected int documentIndex;
    // Target which for accepting only certain items
    protected String targetSlot;
    // A source which is being added to that slot
    protected DragAndDrop.Source sourceLister;
    // A target which is being added to that slot
    protected DragAndDrop.Target targetLister;
    // A item document which contains all necessary details of the current item
    protected itemDocument doc;
    // boolean flag which checks if the item is sellable
    protected boolean sellingObj;
    // boolean flag which checks if the item is swappable
    protected boolean swappingObj;
    // Universal asset manager which has all the assets
    protected AssetManager manager;
    // A popup message displaying the information of current item
    protected TextTooltip info;
    // A Image button for npc
    protected ImageButton imageButtonAddOn;
    // A flag that is set if the class is being used for an npc
    protected boolean isBuyer = false;
    // This is used for buyer to display if they would like to cancel the trade
    protected Label cancelLabel;
    // Path to the base slot skin
    protected String BaseSlotTexturePath;

    /**
     * An empty constructor that does nothing
     * This is needed for child class
     */
    public itemSlot() {
        /*Nothing*/
    }

    /**
     * This constructor adds base item slot image and current item to group is exists.
     * This constructor initializes all required assets
     * @param GAME A uversial game class
     * @param DND Universal drag and drop class
     * @param index The item index
     * @param targetSlot Target to allow only certain items
     * @param manager a universal manager that manages everything.
     */
    public itemSlot(BladeAndTomes GAME, DragAndDrop DND, int index, String targetSlot, AssetManager manager) {
        // Setting all parameters to current objects
        // AssetManager
        this.manager = manager;
        // Blade and tomes game class
        game = GAME;
        // drag and drop class
        dnd = DND;
        // current item slot target
        this.targetSlot = targetSlot;
        // current item index
        documentIndex = index;
        // cancelLabel null since it is not needed when this constructor is called
        cancelLabel = null;

        // Path of base slot
        BaseSlotTexturePath = "SkinAssets/Inventory/Slot/SlotUI";
        // Loads base slot texture to memory
        if (!manager.contains(BaseSlotTexturePath + ".json", Skin.class)) {
            manager.load(BaseSlotTexturePath + ".json", Skin.class, new SkinLoader.SkinParameter(BaseSlotTexturePath + ".atlas"));
            manager.finishLoading();
        }
        // Assigning a skin from memory to slotskin
        slotSkin = manager.get(BaseSlotTexturePath + ".json", Skin.class);
        // Creation of base slot
        slot = new ImageButton(slotSkin);
        // Creation of current table
        table = new Group();
        // Current slot size
        slot.setSize(100, 100);
        // Adding base slot to group
        table.addActor(slot);
        // Gives current an overlay texture if target slot is specified
        // Loading that Spell Target slot
        if (targetSlot.equalsIgnoreCase("Spell")) {
            if (!manager.contains("InventoryItems/Other/SpellArt/SpellBase" + ".json", Skin.class)) {
                manager.load("InventoryItems/Other/SpellArt/SpellBase" + ".json", Skin.class, new SkinLoader.SkinParameter("InventoryItems/Other/SpellArt/SpellBase" + ".atlas"));
                manager.finishLoading();
            }
            // Assigning the target slot image
            imageButtonAddOn = new ImageButton(manager.get("InventoryItems/Other/SpellArt/SpellBase" + ".json", Skin.class));
            // Setting the target slot size and position
            imageButtonAddOn.setSize(60, 60);
            imageButtonAddOn.setPosition(20, 20);
            // adding the target slot if exists to group
            table.addActor(imageButtonAddOn);
        }  // Loading that Armor Target slot
        else if (targetSlot.equalsIgnoreCase("armor")) {
            if (!manager.contains("InventoryItems/Other/ArmorBase/ArmorBase" + ".json", Skin.class)) {
                manager.load("InventoryItems/Other/ArmorBase/ArmorBase" + ".json", Skin.class, new SkinLoader.SkinParameter("InventoryItems/Other/ArmorBase/ArmorBase" + ".atlas"));
                manager.finishLoading();
            }
            // Assigning the target slot image
            imageButtonAddOn = new ImageButton(manager.get("InventoryItems/Other/ArmorBase/ArmorBase" + ".json", Skin.class));
            // Setting the target slot size and position
            imageButtonAddOn.setSize(60, 60);
            imageButtonAddOn.setPosition(20, 20);
            // adding the target slot if exists to group
            table.addActor(imageButtonAddOn);
        }

        // This method is called to load current item image and display it on group
        setItem();
        // Setting this a swappable object
        swappingObj = true;
        // Base Skin for pop up message of item info
        Skin toolTip;
        // File path for  pop up message
        String ToolTpPath = "InventoryItems/Other/SlotTextToolTip/SlotTextToolTip";
        if (manager.contains(ToolTpPath + ".json", Skin.class)) {
            toolTip = manager.get(ToolTpPath + ".json", Skin.class);
        } else {
            manager.load(ToolTpPath + ".json", Skin.class, new SkinLoader.SkinParameter(ToolTpPath + ".atlas"));
            manager.finishLoading();
            toolTip = manager.get(ToolTpPath + ".json", Skin.class);
        }
        // Setting the pop-up msg with info and skin
        info = new TextTooltip("", toolTip);
        // Making it visible instantly
        info.setAlways(true);
        info.setInstant(true);
        // Displaying Info of that item
        displayInfo();
        // Adding pop up to current slot as a listener
        table.addListener(info);
        // Updating current Slot with target
        updateSlotElements();
    }

    /**
     * This constructor adds base item slot image and current item to group is exists.
     * This constructor initializes all required assets
     * This constructor is used to get just the slot ui elements
     *
     * @param index The item index
     * @param manager a universal manager that manages everything.
     */
    public itemSlot(itemDocument doc, AssetManager manager) {
        // Path of base slot
        BaseSlotTexturePath = "SkinAssets/Inventory/Slot/SlotUI";
        // Setting asset manager to load textures
        this.manager = manager;
        // Loads base slot texture to memory
        if (manager.contains(BaseSlotTexturePath + ".json", Skin.class)) {
            slotSkin = manager.get(BaseSlotTexturePath + ".json", Skin.class);
        } else {
            manager.load(BaseSlotTexturePath + ".json", Skin.class, new SkinLoader.SkinParameter(BaseSlotTexturePath + ".atlas"));
            manager.finishLoading();
            slotSkin = manager.get(BaseSlotTexturePath + ".json", Skin.class);
        }
        Skin toolTip;
        // File path for  pop up message
        String ToolTpPath = "InventoryItems/Other/SlotTextToolTip/SlotTextToolTip";
        if (manager.contains(ToolTpPath + ".json", Skin.class)) {
            toolTip = manager.get(ToolTpPath + ".json", Skin.class);
        } else {
            manager.load(ToolTpPath + ".json", Skin.class, new SkinLoader.SkinParameter(ToolTpPath + ".atlas"));
            manager.finishLoading();
            toolTip = manager.get(ToolTpPath + ".json", Skin.class);
        }

        // Creating slot image
        slot = new ImageButton(slotSkin);
        // Creating group to hold slots
        table = new Group();
        // setting slot size
        slot.setSize(100, 100);
        // adding the slot to current group
        table.addActor(slot);
        // getting item info from given doc
        item = doc.getImage(manager);
        // setting the item name from given in doc
        item.setName(doc.getName());
        // setting item size
        item.setSize(65, 65);
        // setting item position
        item.setPosition(slot.getWidth() * 0.15f, slot.getWidth() * 0.15f);
        // adding the item to group
        table.addActor(item);
        // defining pop up message as info
        info = new TextTooltip("", toolTip);
        // setting it instant when hovered over
        info.setInstant(true);
        // adding it to current group
        table.addListener(info);
    }

    /**
     * This method is called to update the slots' info for pop-up
     */

    public void displayInfo() {
        // getting th description from item document ad upper case
        info.getActor().setText(String.valueOf(game.player.inventoryItems.get(documentIndex).getItemDescription()).toUpperCase());
        // This disables the pop-up if it is null
        if (String.valueOf(game.player.inventoryItems.get(documentIndex).getItemDescription()).equalsIgnoreCase("")) {
            table.removeListener(info);
            info.setInstant(false);
            info.hide();
        } else {
            // This adds the pop-up if description is not null
            info.setInstant(true);
            // adds to current slot group
            table.addListener(info);
        }
    }

    /**
     *
     * This method is called to update the slots' info for pop-up
     * @param doc applies this provided doc to current slot
     */
    public void displayInfo(itemDocument doc) {
        // This gets the description from given doc in terms of upper case
        info.getActor().setText(String.valueOf(doc.getItemDescription()).toUpperCase());
        // Removes it from group if the description provided is null
        if (doc.getItemDescription().equalsIgnoreCase("")) {
            table.removeListener(info);
        } else {
            // adds to table
            table.addListener(info);
        }
    }


    /**
     * @return if this current itemSlot is a buyable item aka. not null and stuff
     */
    public boolean canBuy() {
        return sellingObj;
    }

    /**
     * This sets the color of base slot to Dark Gray or Light gray based on given value
     * @param val boolean value which represent if true DARK_GRAY else LIGHT_GRAY
     */
    public void applySelection(boolean val) {
        if (val) slot.setColor(Color.DARK_GRAY);
        else slot.setColor(Color.LIGHT_GRAY);

    }

    /**
     * This method is called to update the current slot with new image
     */
    public void setItem() {
        // Gets the image from item document
        item = game.player.inventoryItems.get(documentIndex).getImage(manager);
        // Gets the name of item from document
        item.setName(game.player.inventoryItems.get(documentIndex).getIndex());
        // setting size and location
        item.setSize(65, 65);
        item.setPosition(slot.getWidth() * 0.15f, slot.getWidth() * 0.15f);
        // Adding this to current group
        table.addActor(item);
        // This attempts to add the pop-up msg
        try {
            displayInfo();
        } catch (Exception e) {
        }
    }

    /**
     * This method updates the current item image without resetting everything
     */
    public void updateDrawable() {
        // Gets the texture from item document
        item.setDrawable(game.player.inventoryItems.get(documentIndex).getImage(manager).getDrawable());
        // Gets the color of current item which indicates the level
        item.setColor(game.player.inventoryItems.get(documentIndex).getImage(manager).getColor());
        // This attempts to add the pop-up msg
        try {
            displayInfo();
        } catch (Exception e) {
        }

    }

    // THIS CLASS LETS ITEM TO BE DRAGGED

    /**
     * This is method applies default source for dragging
     */
    public void applySource() {
        // Adding it to universal Drag and drop  object
        dnd.addSource(sourceLister = new DragAndDrop.Source(item) {
            // creating a temporary payload for holding the item
            final DragAndDrop.Payload payload = new DragAndDrop.Payload();

            // This method gets called when item is being dragged
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                // Setting the payload as item
                payload.setObject(item);
                // creating a actor for temp dragging image
                Image actor = (Image) payload.getObject();
                // setting the visibility of current static image to false until it stops dragging
                item.setVisible(false);
                // creating a temp dragging image
                Image temp = new Image(actor.getDrawable());
                temp.setColor(actor.getColor());
                temp.setSize(50, 50);
                // Setting valid drag and invalid drop as the same temp image
                payload.setDragActor(temp);
                payload.setInvalidDragActor(temp);
                payload.setValidDragActor(temp);
                // centering the image to mouse when drag starts
                dnd.setDragActorPosition(actor.getImageWidth() / 2, -actor.getImageHeight() / 2);
                // updating pop message
                updateSlotElements();
                // returning that current payload
                return payload;
            }

            // This gets called the payload finally stops dragging
            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                // The actual image in item slot is set to true
                ((Image) payload.getObject()).setVisible(true);
                // Updating current pop msg
                displayInfo();
                // updating target image slot if exists
                updateSlotElements();
                // setting the object a not sellable for sec
                sellingObj = false;
            }
        });

    }

    /**
     * When this method is called, it removes everything relating with the item and sets it no null
     */
    public void removeItem() {
        try {
            int itemIndex = Integer.valueOf(documentIndex);
            itemDocument itemTemp = new itemDocument();
            itemTemp.setIndex(String.valueOf(itemIndex));
            itemTemp.setTargetItem("Null");
            itemTemp.setCategory("Null");
            item.setDrawable((new Image()).getDrawable());
            game.player.inventoryItems.set(itemIndex, itemTemp);
            displayInfo();
        } catch (Exception e) {
        }
    }

    /**
     *  THIS METHOD CHECKS IF A DRAGGING ITEM COULD BE MOVED INTO A SLOT
      */
    public void applyTarget() {
        // adding the target to universal drag and drop class
        dnd.addTarget(targetLister = new DragAndDrop.Target(table) {
            // when ever drag stops this method gets called, and this will return if that image can be dropped at this target
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {

                try {
                    // the new item index
                    int newItem = Integer.valueOf(((Image) payload.getObject()).getName());
                    // the current item index
                    int currentItem = Integer.valueOf(item.getName());

                    // THE NEXT FEW 'IF' STATEMENTS WILL GET INVOKED IF THE SLOT HAS A SPECIFIC TARGET
                    // SPELL TARGET
                    if (newItem == 15 || newItem == 14)
                        return game.player.inventoryItems.get(currentItem).getCategory().equalsIgnoreCase("Spell") || (currentItem != 16 && game.player.inventoryItems.get(currentItem).getCategory().equalsIgnoreCase("NUll"));
                    // ARMOR TARGET
                    if (newItem == 16)
                        return game.player.inventoryItems.get(currentItem).getCategory().equalsIgnoreCase("Armor") || ((currentItem != 14 && currentItem != 15) && game.player.inventoryItems.get(currentItem).getCategory().equalsIgnoreCase("NUll"));
                    // ANOTHER NAMING CONVENTION FOR SPELL
                    if (targetSlot.equalsIgnoreCase("Spell")) {
                        targetSlot = "Spells";
                    }
                    // THIS BOOLEAN STATEMENT MAKES CERTAIN THAT SOURCE ITEM IS NOT NULL/NOTHING
                    boolean sourceItemSlot = (targetSlot.equalsIgnoreCase(game.player.inventoryItems.get(newItem).getCategory()) || targetSlot.equalsIgnoreCase("Any"));
                    // THIS BOOLEAN STATEMENT MAKES CERTAIN THAT TARGET IS AVAILABLE
                    boolean targetItemSlot = (targetSlot.equalsIgnoreCase(game.player.inventoryItems.get(currentItem).getCategory()) || targetSlot.equalsIgnoreCase("Any") || "NULL".equalsIgnoreCase(game.player.inventoryItems.get(currentItem).getCategory()));
                    // RETURNS BOOLEAN COMB OF BOTH SOURCE AND TARGET TO SEE IF A TRADE CAN BE MADE
                    return (!game.player.inventoryItems.get(newItem).getTargetItem().equalsIgnoreCase("NUll")) && targetItemSlot && sourceItemSlot && !game.player.inventoryItems.get(newItem).getCategory().equalsIgnoreCase("null");
                } catch (Exception e) {
                    // RETURNS FALSE IF ANYTHING FAILS
                    return false;
                }
            }

            // THIS METHOD GETS INVOKED IF THE ITEM IS DROPPABLE
            // THIS METHOD WILL EXCHANGE THE DOCUMENTS, IMAGES AND POPUPS FOR BOTH ITEMS
            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                // INDEX OF THE NEW ITEM
                int newItem = Integer.valueOf(((Image) payload.getObject()).getName());
                // INDEX OF THE CURRENT ITEM
                int currentItem = Integer.valueOf(item.getName());
                // EXCHANGING INDEX NEW ITEM <- CURRENT ITEM
                game.player.inventoryItems.get(newItem).setIndex(String.valueOf(currentItem));
                // EXCHANGING INDEX NEW ITEM -> CURRENT ITEM
                game.player.inventoryItems.get(currentItem).setIndex(String.valueOf(newItem));
                // SWAPPING THE ITEM DOCUMENTS
                game.player.inventoryItems.swap(newItem, currentItem);
                // SWAPPING TEMP VARIABLE FOR ITEM IMAGES BETWEEN BOTH
                Drawable temp = ((Image) payload.getObject()).getDrawable();
                // SWAPPING TEMP COLOR VARIABLE FOR ITEM LEVEL OF CURRENT ITEM
                Color currentItemColor = item.getColor().cpy();
                // SWAPPING TEMP COLOR VARIABLE FOR ITEM LEVEL OF NEW ITEM
                Color newtItemColor = ((Image) payload.getObject()).getColor();
                // SETTING THE NEW IMAGE TO NEW ITEM
                ((Image) payload.getObject()).setDrawable(item.getDrawable());
                // SETTING THE NEW ITEM TO CURRENT ITEM
                item.setDrawable(temp);
                // SETTING THE COLOR TO NEW ITEM
                item.setColor(newtItemColor);
                // SETTING THE COLOR TO OLD ITEM
                ((Image) payload.getObject()).setColor(currentItemColor);
                // UPDATING TARGET SLOT IMAGE
                updateSlotElements();
                // UPDATES THE POPUP MESSAGES
                displayInfo();
                // CHECKS IF THIS CURRENT ITEM IS ARMOR APPLY IT PLAYER DEFENCE
                if (newItem == 16 || currentItem == 16)
                    game.player.playerDefence = game.player.inventoryItems.get(16).getDamage();
                // UPDATING TARGET SLOT IMAGE
                updateSlotElements();

            }
        });
    }

    /**
     * THIS METHOD IS RESPONSIBLE FOR UPDATING TARGET BASE IMAGE IF THERE IS NO ITEM
     */
    public void updateSlotElements() {
        // SETTING THE BASE IMAGE VISIBLE IF THE SLOT HAS A TARGET
        if (targetSlot.equalsIgnoreCase("Spell") || targetSlot.equalsIgnoreCase("Spells") || targetSlot.equalsIgnoreCase("armor")) {
            imageButtonAddOn.setVisible(game.player.inventoryItems.get(documentIndex).getCategory().equalsIgnoreCase("NUll"));
        }
    }

    /**
     * THIS IS A GETTER WHICH RETURNS DRAG N DROP OBJECT
     * @return DND OBJECT
     */
    public DragAndDrop getDND() {
        return dnd;
    }

    /**
     * THIS METHOD IS A SETTER WHICH ASSIGNS A CUSTOM DND OBJECT WHEN CALLED
     * @param dnd
     */
    public void setDND(DragAndDrop dnd) {
        this.dnd = dnd;
    }

    /**
     * THIS METHOD IS A GETTER WHICH RETURNS THE GROUP OF ITEMS
     * @return
     */
    public Group getSlot() {
        return table;
    }

    /**
     * THIS METHOD RETURN A ITEM IMAGE IT SELF
     * @return
     */
    public Image getItem() {
        return item;
    }
}
