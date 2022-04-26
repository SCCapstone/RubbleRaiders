package ScreenOverlayRework.Inventory.NPCInventoryUI;

import ScreenOverlayRework.Inventory.itemDocument;
import com.badlogic.gdx.assets.AssetManager;

import java.util.Hashtable;
import java.util.Random;

public class RandomItemGenerator {
    private Random random;
    private Hashtable<String, Hashtable<String,Integer>> items;
    private Hashtable<String, Hashtable<String,String>> itemsFinal;

    private itemDocument itemDocument;
    private String inventoryItemsPath;
    private String itemType;
    private String itemCategory;
    private AssetManager manager;
    private int level;

    private  String RandomItemType;
    private  String RandomItemName;
    private int MaxLevel;

    public RandomItemGenerator(AssetManager manager) {
        this.manager = manager;
        random = new Random();
        items = new Hashtable<String, Hashtable<String,Integer>>();
        itemsFinal = new Hashtable<String, Hashtable<String,String>>();
        itemDocument = new itemDocument();
        inventoryItemsPath = "InventoryItems/";
        MaxLevel = 4 ;
        addAvailableItems();
        itemType = (String) items.keySet().toArray()[random.nextInt(items.size())];
        itemCategory = (String) items.get(itemType).keySet().toArray()[random.nextInt(items.get(itemType).size())];
        level = random.nextInt(items.get(itemType).get(itemCategory)) + 1;
        AllItems();
        int ranAmount = random.nextInt(itemsFinal.size());
        if(ranAmount == 1)
            ranAmount = random.nextInt(itemsFinal.size());
            if(ranAmount == 1)
                ranAmount = random.nextInt(itemsFinal.size());
        RandomItemType = ((String) itemsFinal.keySet().toArray()[ranAmount]);
        if(RandomItemType.equalsIgnoreCase("Weapons")){
            generateWeaponDoc();
        }else if(RandomItemType.equalsIgnoreCase("Spells")){
            generateSpellDoc();
        } else if(RandomItemType.equalsIgnoreCase("Armor")){
            generateArmorDoc();
        }
    }


    public void generateArmorDoc(){
        RandomItemName =  (String) itemsFinal.get("Armor").keySet().toArray()[random.nextInt(itemsFinal.get("Armor").size())];
        int itemLevel = random.nextInt(MaxLevel)+1;
        boolean isMagic = (random.nextInt(1500)>1400)?true:false;
        int itemDamage =Math.min((itemLevel*itemLevel+itemLevel)/2,10);
        itemDamage = (isMagic&&itemLevel>=MaxLevel)? 10:itemDamage;

        String ItemInfo = "***  Armor ***"+"\n"+
                "Armor Level: "+String.valueOf(itemLevel)+"\n"+
                "Armor Defence: "+itemDamage+"\n"+
                ((isMagic)? "*** Magic Item ***":"");
        itemDocument doc = new itemDocument();
        doc.setImageLocation(inventoryItemsPath + RandomItemType
                + "/"+ String.valueOf(RandomItemName) + ".png");
        doc.setCategory(RandomItemType);
        doc.setTargetItem("Any");
        doc.setRange(0);
        doc.setLevel(itemLevel);
        doc.setDamage(itemDamage);

        doc.setPrice(calculatePrice(itemLevel+1));
        doc.setName(RandomItemName);
        doc.setItemDescription(ItemInfo);
        doc.setDefauls = false;
        doc.isDefaultColor = true;
        itemDocument = doc;

    }
    public void generateSpellDoc(){
        RandomItemName =  (String) itemsFinal.get("Spells").keySet().toArray()[random.nextInt(itemsFinal.get("Spells").size())];
        int itemLevel = random.nextInt(MaxLevel)+1;
        boolean isMagic = (random.nextInt(1000)>800)?true:false;
        int itemDamage =Math.min((itemLevel*itemLevel)/2+1,10);
        itemDamage = (isMagic&&itemLevel>=3)? 10:itemDamage;

        String ItemInfo = "Spell Type: "+RandomItemName+"\n"+
                "Item Level: "+String.valueOf(itemLevel)+"\n"+
                RandomItemName+"Amount: "+itemDamage+"\n"+
                ((isMagic)? "*** Magic Item ***":"");
        itemDocument doc = new itemDocument();
        doc.setImageLocation(inventoryItemsPath + RandomItemType
                + "/"+ String.valueOf(RandomItemName) + ".png");
        doc.setCategory(RandomItemType);
        doc.setTargetItem("Any");
        doc.setRange(0);
        doc.setLevel(itemLevel);
        doc.setPrice(calculatePrice(itemLevel+1));
        doc.setName(RandomItemName);
        doc.setDamage(itemDamage);
        doc.setItemDescription(ItemInfo);
        doc.setDefauls = false;
        doc.isDefaultColor = true;
        itemDocument = doc;
    }


        public void generateWeaponDoc(){
        RandomItemName =  (String) itemsFinal.get("Weapons").keySet().toArray()[random.nextInt(itemsFinal.get("Weapons").size())];
        int itemLevel = random.nextInt(MaxLevel)+1;
        boolean isMagic = (random.nextInt(100)>70)?true:false;
        int itemDamage = itemLevel*itemLevel+1;
        int range = 0;
        itemDamage += (isMagic)? 2:0;
        String ItemInfo = "Item Type: "+RandomItemName+"\n"+
                          "Item Level: "+String.valueOf(itemLevel)+"\n"+
                          "Item Damage: "+itemDamage+"\n"+
                            ((isMagic)? "*** Magic Item ***":"");
        String rangeType  = (String) itemsFinal.get("Weapons").get(RandomItemName);
        range = (rangeType.equalsIgnoreCase("Ranged"))?1:0;
        itemDocument doc = new itemDocument();
        doc.setImageLocation(inventoryItemsPath + RandomItemType
                + "/"+ String.valueOf(RandomItemName) + ".png");
        doc.setCategory(RandomItemType);
        doc.setTargetItem("Any");
        doc.setRange(range);
        doc.setLevel(itemLevel);
        doc.setPrice(calculatePrice(itemLevel+1));
        doc.setName(RandomItemName);
            doc.setDamage(itemDamage);

            doc.setItemDescription(ItemInfo);
        doc.setDefauls = false;
        doc.isDefaultColor = false;
        itemDocument = doc;
    }

    public RandomItemGenerator(String itemType) {
        random = new Random();

    }
    public int calculatePrice(int level) {
        int price = (random.nextInt(5) + level*level+1)+2;
        return price;
    }

    public void addAvailableItems() {
        Hashtable<String,Integer> itemsDic = new Hashtable<>();
        itemsDic.put("Sword",3);
        items.put("Weapons", itemsDic);
        itemsDic = new Hashtable<>();
        itemsDic.put("ChestPlate",3);
        items.put("Armor", itemsDic);
    }

    public void AllItems() {
        // ImageName, Type
        Hashtable<String,String> itemsDic = new Hashtable<>();
        itemsDic.put("axe","Not Ranged");
        itemsDic.put("dagger","Not Ranged");
        itemsDic.put("scythe","Not Ranged");
        itemsDic.put("sword","Not Ranged");
        itemsDic.put("bow","Ranged");
        itemsDic.put("crossbow","Ranged");
        itemsFinal.put("Weapons", itemsDic);
        itemsDic = new Hashtable<>();
        itemsDic.put("HealSpell","Heal");
        itemsDic.put("StrengthSpell","Strength");
        itemsFinal.put("Spells", itemsDic);
        itemsDic = new Hashtable<>();
        itemsDic.put("armor","");
        itemsFinal.put("Armor", itemsDic);

    }

    public ScreenOverlayRework.Inventory.itemDocument getItemDocument() {
        return itemDocument;
    }
    public void setItemDocument(ScreenOverlayRework.Inventory.itemDocument doc) {
         itemDocument = doc;
    }
    // Return Level of This generated item

    public int getLevel() {
        return level;
    }

}
