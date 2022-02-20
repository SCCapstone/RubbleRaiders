package ScreenOverlayRework.Inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class itemDocument {
    private String category;
    private String Name;
    private Image  image;
    private String targetItem;
    private int level;
    private int range;
    private int damage;
    public boolean setDefauls;


    public itemDocument(){
        setDefauls = true;
    }

    public String getCategory() {
        return category;
    }
    public void setTargetItem(String target) {
        targetItem = target;
    }
    public String  getTargetItem() {
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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
