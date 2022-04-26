package de.tomgrill.gdxtesting.BehaviorTests.Inventory;

import ScreenOverlayRework.Inventory.itemDocument;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class itemDocumentTest {

    AssetManager assets = new AssetManager();

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
