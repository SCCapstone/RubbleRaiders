package de.tomgrill.gdxtesting.UnitTests;

import com.badlogic.game.creatures.Inventory;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

//GdxTestRunner written by Thomas Pronold (TomGrill) for purpose of using as a framework for Gdx testing
//using jUnit and Mockito
//Source: https://github.com/TomGrill/gdx-testing
@RunWith(GdxTestRunner.class)
public class InventoryTest {

    private Inventory testInventory = new Inventory();

    /**
     * Tests the Inventory class and makes sure that the assignable characteristics work as intended.
     * More specifically testing usable methods within the code.
     */
    @Test
    public void testInventory() {

        // Saving different data at various points for swapping
        // before testing to see what those swaps are.
        int dataPointOne = testInventory.addSwap(13)[0];
        int dataPointTwo = testInventory.addSwap(11)[1];
        int dataPointThree = testInventory.addSwap(2)[1];
        int dataPointFour = testInventory.addSwap(3)[0];

        // Using the referred to Assert function by the jUnit team to be able to
        // Check to see and test if the function works as it should. Tutorialspoint.com
        // also helped in this endeavor as it provided the training needed to understand
        // jUnit properly. Relevant links are listed below:
        // https://junit.org/junit4/javadoc/latest/org/junit/Assert.html
        // https://www.tutorialspoint.com/junit/junit_test_framework.htm
        Assert.assertNotNull(testInventory.addSwap(15));
        Assert.assertNotEquals(dataPointOne, dataPointTwo);
        Assert.assertEquals(dataPointThree, dataPointFour);
    }

}
