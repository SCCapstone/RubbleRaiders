package UnitTests;

import com.badlogic.game.creatures.Entity;

import org.junit.Assert;
import org.junit.Test;

public class EntityTest {

    //Information on jUnit is all thanks to www.tutorialspoint.org for providing brilliant tutorials
    //in order to learn about Unit testing specifically with jUnit
    //https://www.tutorialspoint.com/junit/junit_test_framework.htm

    //Creates an instance of the test Entity for testing within this class
    private Entity testEntity = new Entity();

    /**
     * Creates a super class object in order to make sure the superclass object works definitively.
     */
    @Test
    public void setTestEntity() {

        //Sets the armor, health, and movement to arbitrary values
        testEntity.setArmorPoints(7);
        testEntity.setHealthPoints(10);
        testEntity.setMovement(10);

        // https://junit.org/junit4/javadoc/latest/org/junit/Assert.html
        // https://www.tutorialspoint.com/junit/junit_test_framework.htm
        // Above link shows documentation how jUnit Assert works thanks to
        // junit-team for making repository available as well as www.tutorialspoint.org
        // for providing brilliant tutorial in order to learn about Unit testing specifically with jUnit
        Assert.assertEquals(testEntity.getArmorPoints(), 7);
        Assert.assertEquals(testEntity.getHealthPoints(), 10);
        Assert.assertEquals(testEntity.getMovement(), 10);
    }

}
