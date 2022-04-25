/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package ScreenOverlayRework.Inventory;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class itemDocument {
    // THE COLOR OF THIS ITEM INDICATED BY LEVEL
    public Color color;
    // THE FLAG REPRESENTS IF THE ITEM IS NOTHING
    public boolean isDefaultColor = true;
    public boolean setDefauls;
    // THE CATEGORY OF THE ITEM: WEAPONS, ARMOR OR SPELLS
    private String category;
    // THE NAME OF THE ITEM: DAGGER, BOW, ETC.
    private String Name;
    // SPECIFIED TARGET OF THE ITEM: ARMOR ONLY OR SPELL ONLY
    private String targetItem;
    // THE IMAGE OF THE CURRENT ITEM
    private transient Image image;
    // THE INDEX OF THIS ITEM
    private String index;
    // THE IMAGE LOCATION OF THIS ITEM
    private String ImageLocation;
    // THE LEVEL OF THIS ITEM
    private int level;
    // THE RANGE OF THIS ITEM
    private int range;
    // THE DAMAGE TO THIS ITEM
    private int damage;
    // THE PRICE OF THIS ITEM FOR NPC PURPOSES
    private int price;
    // THE DESCRIPTION OF THIS ITEM, THIS IS USED FOR POP-UP MESSAGES
    private String itemDescription;

    /**
     * THE DEFAULT CONSTRUCTOR WHICH SETS ALL FILED AS NOTHING UNTIL ITEM GETS ASSIGNED
     */
    public itemDocument() {
        category = "Null";
        targetItem = "Any";
        setDefauls = true;
        image = new Image();

        range = 0;
        damage = 0;
        level = 0;

        itemDescription = "";
    }

    /**
     * THIS METHOD RETURN THE ITEM DESCRIPTION
     * @return DESCRIPTION OF THIS ITEM
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * THIS METHOD SETS THE DESCRIPTION OF THIS ITEM
     * @param itemDescription THE NEW DESCRIPTION
     */
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    /**
     * THE METHOD IS A SETTER FOR ITEM IMAGE LOCATION
     * @param loc NEW IMAGE LOCATION
     */
    public void setImageLocation(String loc) {
        ImageLocation = loc;
    }

    /**
     * THIS METHOD IS A GETTER FOR ITEM INDEX
     * @return INDEX OF ITEM IN STRING FORMAT
     */
    public String getIndex() {
        return index;
    }

    /**
     * THIS METHOD SETS NEW INDEX TO CURRENT ITEM
     * @param index THE NEW INDEX
     */
    public void setIndex(String index) {
        this.index = index;
    }

    /**
     * THIS METHOD IS A GETTER FOR PRICE
     * @return PRICE OF THIS ITEM: USED USUALLY FOR NPC TRADES
     */
    public int getPrice() {
        return price;
    }

    /**
     * THIS METHOD IS A SETTER FOR PRICE
     * @parm price OF THIS ITEM: USED USUALLY FOR NPC TRADES
     */
    public void setPrice(int price) {
        this.price = price;
    }


    /**
     * THIS IS A GETTER FOR THE ITEM CATEGORY
     * @return RETURNS ITEM CATEGORY
     */
    public String getCategory() {
        return category;
    }

    /**
     * THIS METHOD IS A SETTER FOR THE ITEM CATEGORY
     * @param category THE NEW CATEGORY OF THIS ITEM
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * THIS IS A GETTER FOR THE TARGET OF THIS ITEM
     * @return THE TARGET SLOT
     */
    public String getTargetItem() {
        return targetItem;
    }

    /**
     * THIS IS A SETTER FOT NEW TARGET OF THIS ITEM
     * @param target NEW TARGET
     */
    public void setTargetItem(String target) {
        targetItem = target;
    }

    /**
     * THIS METHOD IS A GETTER OF THE NAME OF CURRENT ITEM
     * @return THE NAME OF THE ITEM
     */
    public String getName() {
        return Name;
    }

    /**
     * THIS METHOD IS SETTER FOR NEW ITEM NAME
     * @param name THE NAME OF THE NEW ITEM
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     * THIS METHOD RETURN A NEW IMAGE WHEN CALLED
     * @param manager THE ASSETMANGER THAT IS USED TO LOAD THE IMAGE. IF THE IMAGE EXISTING IN MANGER IT GET IT ELSE LOADS IT AND THEN GETS IT
     * @return THE IMAGE WITH COLOR
     */
    public Image getImage(AssetManager manager) {
        try {
            if (!manager.contains(ImageLocation, Texture.class)) {
                manager.load(ImageLocation, Texture.class);
                manager.finishLoading();
            }
            image = new Image(manager.get(ImageLocation, Texture.class));
        } catch (Exception e) {
            image = new Image();
        }
        image.setColor(getColorLevel(level));
        return image;
    }

    /**
     * THIS METHOD IS RESPONSIBLE FOR GIVING THE COLOR TO IMAGE BASED ON LEVEL
     * @param lvl
     * @return
     */
    public Color getColorLevel(int lvl) {
        Color col = new Color();
        if (lvl == 1) col.set(Color.BROWN);
        else if (lvl == 2) col.set(Color.LIGHT_GRAY);
        else if (lvl == 3) col.set(Color.GOLDENROD);
        else if (lvl == 4) col.set(Color.RED);
        col.a = 1;
        return col;
    }

    /**
     * THIS METHOD IS A GETTER FOR LEVEL
     * @return LEVEL OF THE ITEM
     */
    public int getLevel() {
        return level;
    }

    /**
     * THIS METHOD SETS A NEW LEVEL OF THIS CURRENT ITEM
     * @param level NEW LEVEL
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * THIS METHOD IS A GETTER FOR ITEM RANGE
     * @return RANGE OF THE ITEM
     */
    public int getRange() {
        return range;
    }

    /**
     * THIS METHOD IS A SETTER OF ITEM RANGE
     * @param range NEW RANGE OF THE CURRENT ITEM
     */
    public void setRange(int range) {
        this.range = range;
    }

    /**
     * THIS METHOD IS A GETTER OF THE AMOUNT OF DAMAGE THIS ITEM DOES
     * @return THE DAMAGE TO THIS ITEM
     */
    public int getDamage() {
        return damage;
    }

    /**
     * THIS METHOD IS A SETTER FOR NEW ITEM DAMAGE
     * @param damage NEW DAMAGE
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }
}
