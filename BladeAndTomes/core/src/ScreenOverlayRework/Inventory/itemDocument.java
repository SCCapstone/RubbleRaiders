package ScreenOverlayRework.Inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import jdk.internal.jimage.ImageLocation;

public class itemDocument {
    private String category;
    private String Name;
    private String targetItem;
    private transient Image image;
    private String index;
    private String ImageLocation;

    private int level;
    private int range;
    private int damage;
    private int price;
    public Color color;
    public boolean isDefaultColor=true;

    private String itemDescription;

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public itemDocument(){
        category = "Null";
        targetItem = "Any";
        setDefauls = true;
        image = new Image();

        range = 0;
        damage = 0;
        level = 0;

        itemDescription = "";
    }

    public void setImageLocation(String loc){
        ImageLocation = loc;
    }
    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }



    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isSetDefauls() {
        return setDefauls;
    }

    public boolean setDefauls;

    public String getCategory() {
        return category;
    }

    public void setTargetItem(String target) {
        targetItem = target;
    }

    public String getTargetItem() {
        return targetItem;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Image getImage(AssetManager manager) {
        try {
            if(manager.contains(ImageLocation,Texture.class)){
                image = new Image(manager.get(ImageLocation,Texture.class));

//                if(isDefaultColor)
//                    image.setColor(color);
            }else{
                manager.load(ImageLocation,Texture.class);
                manager.finishLoading();
                image = new Image(manager.get(ImageLocation,Texture.class));
//                if(isDefaultColor)
//                    image.setColor(color);
            }
        }catch (Exception e){
            image = new Image();

        }
        return image;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
