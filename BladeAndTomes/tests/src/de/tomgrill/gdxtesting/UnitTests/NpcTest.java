package de.tomgrill.gdxtesting.UnitTests;

import com.badlogic.game.creatures.Npc;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

//GdxTestRunner written by Thomas Pronold (TomGrill) for purpose of using as a framework for Gdx testing
//Using jUnit and Mockito
//Source: https://github.com/TomGrill/gdx-testing
@RunWith(GdxTestRunner.class)
public class NpcTest {

    // Creation of basic NPC to allow for easy testing
    private Npc testNPC = new Npc();

    /**
     * jUnit Test function which runs a small test by creating a test NPC using the libraries given and making sure
     * the given values are correct and assigned correctly.
     */
    @Test
    public void setTestNPC() {

        // Sets basic NPC stats for use within testing to assert each of the NPC functions.
        testNPC.setNpcClass(2);
        testNPC.setMovement(10);
        testNPC.setHealthPoints(10);
        testNPC.setArmorPoints(7);

        // https://junit.org/junit4/javadoc/latest/org/junit/Assert.html
        // https://www.tutorialspoint.com/junit/junit_test_framework.htm
        // Above link shows documentation how jUnit Assert works thanks to
        // junit-team for making repository available as well as
        // tutorialspoint.com for providing tutorials on how to use jUnit
        // properly.
        Assert.assertEquals(testNPC.getArmorPoints(), 7);
        Assert.assertEquals(testNPC.getHealthPoints(), 10);
        Assert.assertEquals(testNPC.getNpcClass(), 2);
    }
}
