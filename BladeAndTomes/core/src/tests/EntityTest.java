package tests;

import com.badlogic.game.creatures.Entity;
import com.badlogic.game.creatures.Npc;

import org.junit.Assert;
import org.junit.Test;

public class EntityTest {
    //Information is all thanks to www.tutorialspoint.org for providing brilliant tutorials
    //in order to learn about Unit testing specifically with jUnit
    //https://www.tutorialspoint.com/junit/junit_test_framework.htm
    Entity testEntity = new Entity();
    //Player testPlayer = new Player();
    Npc testNPC = new Npc();

    //Tests each aspect in working with an NPC
    @Test
    public void setTestEntity() {

        testEntity.setArmorPoints(7);
        testEntity.setHealthPoints(10);
        testEntity.setMovement(10);

        //https://junit.org/junit4/javadoc/latest/org/junit/Assert.html
        //Above link shows documentation how jUnit Assert works thanks to
        //junit-team for making repository available
        Assert.assertEquals(testEntity.getArmorPoints(), 7);
        Assert.assertEquals(testEntity.getHealthPoints(), 10);
        Assert.assertEquals(testEntity.getMovement(), 10);
    }

    //Creates a blank instance of player and tests assignable aspects to make sure that they function
    /*@Test
    public void setTestPlayer()
    {
        testPlayer.setArmorPoints(7);
        testPlayer.setHealthPoints(10);
        testPlayer.setMovement(10);

        //https://junit.org/junit4/javadoc/latest/org/junit/Assert.html
        //Above link shows documentation how jUnit Assert works thanks to
        //junit-team for making repository available
        Assert.assertEquals(testPlayer.getArmorPoints(), 7);
        Assert.assertEquals(testPlayer.getHealthPoints(), 10);
        Assert.assertEquals(testPlayer.getMovement(), 10);
    }*/

    //Tests the NPC class and makes sure that the assignable characteristics work as intended
    @Test
    public void setTestNPC() {
        testNPC.setNpcClass(2);
        testNPC.setMovement(10);
        testNPC.setHealthPoints(10);
        testNPC.setArmorPoints(7);

        //https://junit.org/junit4/javadoc/latest/org/junit/Assert.html
        //Above link shows documentation how jUnit Assert works thanks to
        //junit-team for making repository available
        Assert.assertEquals(testNPC.getArmorPoints(), 7);
        Assert.assertEquals(testNPC.getHealthPoints(), 10);
        Assert.assertEquals(testNPC.getMovement(), 10);
        Assert.assertEquals(testNPC.getNpcClass(), 2);
    }

}
