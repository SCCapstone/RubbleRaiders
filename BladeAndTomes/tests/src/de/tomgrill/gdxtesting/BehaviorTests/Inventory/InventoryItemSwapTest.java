package de.tomgrill.gdxtesting.BehaviorTests.Inventory;

import ScreenOverlayRework.Inventory.itemDocument;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)

public class InventoryItemSwapTest {


    AssetManager assets = new AssetManager();
    Array<itemDocument> doc = new Array<>();
    @Test
    public void SwapTest(){
        // Armor
        itemDocument testDocument_1 = new itemDocument();
        // Weapon
        itemDocument testDocument_2 = new itemDocument();
        // Spell
        itemDocument testDocument_3 = new itemDocument();
        // Null
        itemDocument testDocument_4 = new itemDocument();


        // Testing item doc 1: Armor
        testDocument_1.setDefauls = false;
        testDocument_1.setIndex("0");
        testDocument_1.setTargetItem("Any");
        testDocument_1.setDamage(7);
        testDocument_1.setLevel(2);
        testDocument_1.isDefaultColor = false;
        testDocument_1.setImageLocation("InventoryItems/Armor/armor.png");
        testDocument_1.setCategory("Armor");
        testDocument_1.setName("armor");

        // Testing item doc 2: Weapon
        testDocument_2.setDefauls = false;
        testDocument_2.setIndex("1");
        testDocument_2.setTargetItem("Any");
        testDocument_2.setDamage(2);
        testDocument_2.setLevel(4);
        testDocument_2.isDefaultColor = false;
        testDocument_2.setImageLocation("InventoryItems/Weapons/axe.png");
        testDocument_2.setName("axe");


        // Testing item doc 3: Spell
        testDocument_3.setDefauls = false;
        testDocument_3.setIndex("2");
        testDocument_3.setTargetItem("Any");
        testDocument_3.setDamage(7);
        testDocument_3.setLevel(1);
        testDocument_3.isDefaultColor = false;
        testDocument_3.setImageLocation("InventoryItems/Spells/HealSpell.png");
        testDocument_3.setCategory("Spell");
        testDocument_3.setName("HealSpell");

        // Item Slot with Nothing
        testDocument_4.setIndex("3");
        testDocument_4.setTargetItem("Null");
        testDocument_4.setCategory("Null");

        // Setting Image details for swapping
        Image armor = testDocument_1.getImage(assets);
        armor.setName(testDocument_1.getIndex());
        Image axe = testDocument_2.getImage(assets);
        axe.setName(testDocument_2.getIndex());
        Image spell = testDocument_3.getImage(assets);
        spell.setName(testDocument_3.getIndex());
        Image nothing = testDocument_4.getImage(assets);
        nothing.setName(testDocument_4.getIndex());

        doc.add(testDocument_1);
        doc.add(testDocument_2);
        doc.add(testDocument_3);
        doc.add(testDocument_4);

        // Checking Nothing cannot be moved into armor only slot
        Assert.assertTrue(!checkIfItemDroppable(armor,nothing,"Armor",doc));
        Assert.assertTrue(!checkIfItemDroppable(armor,spell,"Armor",doc));
        Assert.assertTrue(checkIfItemDroppable(armor,axe,"Armor",doc));
        Assert.assertTrue(checkIfItemDroppable(armor,armor,"Armor",doc));

        // Checking Nothing cannot be moved into spell only slot
        Assert.assertTrue(!checkIfItemDroppable(spell,nothing,"Armor",doc));
        Assert.assertTrue(!checkIfItemDroppable(armor,spell,"Armor",doc));
        Assert.assertTrue(checkIfItemDroppable(spell,axe,"Armor",doc));
        Assert.assertTrue(checkIfItemDroppable(spell,spell,"Spell",doc));

        // Checking anything can be moved into any slot
        Assert.assertTrue(checkIfItemDroppable(axe,nothing,"Any",doc));
        Assert.assertTrue(checkIfItemDroppable(axe,axe,"Any",doc));


        // Checking Actual Item Swaps
        SwapItems(armor,axe,doc);
        Assert.assertEquals(doc.get(0),testDocument_2);
        Assert.assertEquals(doc.get(1),testDocument_1);
        Assert.assertEquals(testDocument_2.getIndex(),"0");
        Assert.assertEquals(testDocument_1.getIndex(),"1");

    }

    /**
     * This method is from itemSlot.java class, this is defined under drop method in addTarget method
     * This method is used to test if any other item could be dropped in target specific like "armor only" or "spell only"
     * Due to issue with behavior test, simple logic is being tested.
     * @param source Source image
     * @param payload Current image
     * @param targetSlot armor only or spell only
     * @param items list of items
     * @return true if it can be dropped else false
     */
    public boolean checkIfItemDroppable(Image source, Image payload, String targetSlot, Array<itemDocument> items){
        try {
            // the new item index
            int newItem = Integer.valueOf(((Image) payload).getName());
            // the current item index
            int currentItem = Integer.valueOf(source.getName());

            // THE NEXT FEW 'IF' STATEMENTS WILL GET INVOKED IF THE SLOT HAS A SPECIFIC TARGET
            // SPELL TARGET
            if (newItem == 2)
                return items.get(currentItem).getCategory().equalsIgnoreCase("Spell") || (currentItem != 16 && items.get(currentItem).getCategory().equalsIgnoreCase("NUll"));
            // ARMOR TARGET
            if (newItem == 0)
                return items.get(currentItem).getCategory().equalsIgnoreCase("Armor") || ((currentItem != 14 && currentItem != 15) &&items.get(currentItem).getCategory().equalsIgnoreCase("NUll"));
            // ANOTHER NAMING CONVENTION FOR SPELL
            if (targetSlot.equalsIgnoreCase("Spell")) {
                targetSlot = "Spells";
            }
            // THIS BOOLEAN STATEMENT MAKES CERTAIN THAT SOURCE ITEM IS NOT NULL/NOTHING
            boolean sourceItemSlot = (targetSlot.equalsIgnoreCase(items.get(newItem).getCategory()) || targetSlot.equalsIgnoreCase("Any"));
            // THIS BOOLEAN STATEMENT MAKES CERTAIN THAT TARGET IS AVAILABLE
            boolean targetItemSlot = (targetSlot.equalsIgnoreCase(items.get(currentItem).getCategory()) || targetSlot.equalsIgnoreCase("Any") || "NULL".equalsIgnoreCase(items.get(currentItem).getCategory()));
            // RETURNS BOOLEAN COMB OF BOTH SOURCE AND TARGET TO SEE IF A TRADE CAN BE MADE
            return (!items.get(newItem).getTargetItem().equalsIgnoreCase("NUll")) || targetItemSlot && sourceItemSlot;
        } catch (Exception e) {
            // RETURNS FALSE IF ANYTHING FAILS
            return false;
        }
    }
    /**
     * This method is from itemSlot.java class, method called "add" target which has some basic logic for item swapping
     * @param Source Source image
     * @param payload new item/ payload image
     * @param items list of items/ itemDocuments
     */
    public void SwapItems(Image Source, Image payload, Array<itemDocument> items){
        int newItem = Integer.valueOf(((Image) payload).getName());
        // INDEX OF THE CURRENT ITEM
        int currentItem = Integer.valueOf(Source.getName());
        // EXCHANGING INDEX NEW ITEM <- CURRENT ITEM
        items.get(newItem).setIndex(String.valueOf(currentItem));
        // EXCHANGING INDEX NEW ITEM -> CURRENT ITEM
        items.get(currentItem).setIndex(String.valueOf(newItem));
        // SWAPPING THE ITEM DOCUMENTS
        items.swap(newItem, currentItem);
        // SWAPPING TEMP VARIABLE FOR ITEM IMAGES BETWEEN BOTH
        Drawable temp = ((Image) payload).getDrawable();
        // SWAPPING TEMP COLOR VARIABLE FOR ITEM LEVEL OF CURRENT ITEM
        Color currentItemColor = Source.getColor().cpy();
        // SWAPPING TEMP COLOR VARIABLE FOR ITEM LEVEL OF NEW ITEM
        Color newtItemColor = ((Image) payload).getColor();
        // SETTING THE NEW IMAGE TO NEW ITEM
        ((Image) payload).setDrawable(Source.getDrawable());
        // SETTING THE NEW ITEM TO CURRENT ITEM
        Source.setDrawable(temp);
        // SETTING THE COLOR TO NEW ITEM
        Source.setColor(newtItemColor);
        // SETTING THE COLOR TO OLD ITEM
        ((Image) payload).setColor(currentItemColor);
    }

    @Test
    public void testInventory_ItemDocument(){
        itemDocument testDocument_1 = new itemDocument();
        itemDocument testDocument_2 = new itemDocument();
        // Checking if return valid objects even if invalid fields are set
        testDocument_1.setDefauls = false;
        testDocument_1.setIndex("1");
        testDocument_1.setTargetItem("Any");
        testDocument_1.setDamage(7);
        testDocument_1.setLevel(2);
        testDocument_1.isDefaultColor = false;
        testDocument_1.setImageLocation("nullul");
        boolean equalsInvalidImagePath  = testDocument_1.getImage(assets).getDrawable()==new Image().getDrawable();
        boolean setImageColorLevel = testDocument_1.getImage(assets).getColor()!=new Image().getColor();
        // This checks if image with invalid path gets clear drawables set
        Assert.assertTrue(equalsInvalidImagePath);
        // This will check color of image is set even if image has no drawables
        Assert.assertTrue(setImageColorLevel);
        // Checking if it returns valid object if valid fields are set

        // Checking if return valid objects even if invalid fields are set
        testDocument_2.setDefauls = false;
        testDocument_2.setIndex("3");
        testDocument_2.setTargetItem("Any");
        testDocument_2.setDamage(2);
        testDocument_2.setLevel(4);
        testDocument_2.isDefaultColor = false;
        testDocument_2.setImageLocation("Player/Player.png");
        equalsInvalidImagePath  = testDocument_2.getImage(assets).getDrawable()!=new Image().getDrawable();
        // Temp img for checking if colors match
        Image img = new Image();
        img.setColor( Color.RED);
        img.getColor().a = 1;
        setImageColorLevel = testDocument_2.getImage(assets).getColor().g==img.getColor().g&&
                testDocument_2.getImage(assets).getColor().r==img.getColor().r&&
                testDocument_2.getImage(assets).getColor().b==img.getColor().b&&
                testDocument_2.getImage(assets).getColor().a==img.getColor().a;
        // This checks if image with valid path gets clear drawables set
        Assert.assertTrue(equalsInvalidImagePath);
        // This will check color of image is defined
        Assert.assertTrue(setImageColorLevel);
    }
}
