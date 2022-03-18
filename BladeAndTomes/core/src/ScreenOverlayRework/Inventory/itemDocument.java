package ScreenOverlayRework.Inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
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

    public itemDocument(){
        category = "Null";
        targetItem = "Null";
        setDefauls = true;
        image = new Image();

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

    private int level;
    private int range;
    private int damage;
    private int price;

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
            }else{
                manager.load(ImageLocation,Texture.class);
                manager.finishLoading();
                image = new Image(manager.get(ImageLocation,Texture.class));
            }
        }catch (Exception e){
            image = new Image();
        }
        return image;
    }

//    public void setImage(Image image) {
//        this.image = image;
//    }

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
