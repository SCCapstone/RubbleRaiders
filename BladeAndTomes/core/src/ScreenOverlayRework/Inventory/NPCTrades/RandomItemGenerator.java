package ScreenOverlayRework.Inventory.NPCTrades;

import ScreenOverlayRework.Inventory.itemDocument;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.Hashtable;
import java.util.Random;

public class RandomItemGenerator {
    private Random random;
    private Hashtable<String, Integer> items;
    private itemDocument itemDocument;
    private String inventoryItemsPath;
    private String itemType;
    private int level;

    public RandomItemGenerator() {

        random = new Random();
        items = new Hashtable<String, Integer>();
        itemDocument = new itemDocument();
        inventoryItemsPath = "InventoryItems/";
        addAvailableItems();

        itemType = (String) items.keySet().toArray()[random.nextInt(items.size())];
        level = random.nextInt(items.get(itemType)) + 1;
        makeItemDoc();
    }

    public RandomItemGenerator(String itemType) {
        random = new Random();

    }

    public int calculatePrice() {
        int min = level * 3;
        int max = level * 9;
        int price = (random.nextInt(min) + random.nextInt(max));
        return price;
    }

    public void makeItemDoc() {
        itemDocument.setImageLocation("InventoryItems/" + itemType
                + "/" + String.valueOf(level) + ".png");
        itemDocument.setCategory(itemType);
        itemDocument.setTargetItem("Any");
        itemDocument.setRange(3);
        itemDocument.setLevel(level);
        itemDocument.setPrice(calculatePrice());
        itemDocument.setName("Sword");
        itemDocument.setDefauls = false;
    }

    public void addAvailableItems() {
        items.put("Weapons", 3);
    }

    public Hashtable<String, Integer> getItems() {
        return items;
    }

    public ScreenOverlayRework.Inventory.itemDocument getItemDocument() {
        return itemDocument;
    }

    public String getInventoryItemsPath() {
        return inventoryItemsPath;
    }

    public String getItemType() {
        return itemType;
    }

    public int getLevel() {
        return level;
    }

    public Random getRandom() {
        return random;
    }
}
