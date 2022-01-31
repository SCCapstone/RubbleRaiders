package de.tomgrill.gdxtesting.UnitTests;

import com.badlogic.game.creatures.Player;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

//GdxTestRunner written by Thomas Pronold (TomGrill) for purpose of using as a framework for Gdx testing
//using jUnit and Mockito
//Source: https://github.com/TomGrill/gdx-testing
@RunWith(GdxTestRunner.class)
public class PlayerTest {

    //Creates instance of player ubiquitous in all tests
    Player player = new Player();

    /**
     * Makes sure that the player is created and not null
     */
    @Test
    public void testPlayerCreation() {
        Assert.assertNotNull(player);
    }

    /**
     * Sets the player's stats to certain attributes and tests to see if any of them are incorrect
     */
    public void testPlayerSettings() {

        //Sets players stats up to be simple and elegant in nature
        player.setArmorPoints(10);
        player.setPlayerClass(0);
        player.setName("Albatross");
        player.setMovement(10);
        player.setPhysical(10);
        player.setSocial(10);
        player.setMental(10);

        // https://junit.org/junit4/javadoc/latest/org/junit/Assert.html
        // https://www.tutorialspoint.com/junit/junit_test_framework.htm
        // Above link shows documentation how jUnit Assert works thanks to
        // junit-team for making repository available as well as
        // tutorialspoint.com for providing tutorials on how to use jUnit
        // properly.
        Assert.assertEquals(10, player.getArmorPoints());
        Assert.assertEquals(0, player.getPlayerClass());
        Assert.assertEquals("Albatross", player.getPlayerClass());
        Assert.assertEquals(10, player.getMovement());
        Assert.assertEquals(10, player.getSocial());
        Assert.assertEquals(10, player.getMental());

    }

    /**
     * Checks to make sure the stats set in the testPlayersSettings test are what they say they are
     */
    public void testPlayerSecondarySkills () {

        // https://junit.org/junit4/javadoc/latest/org/junit/Assert.html
        // https://www.tutorialspoint.com/junit/junit_test_framework.htm
        // Above link shows documentation how jUnit Assert works thanks to
        // junit-team for making repository available as well as
        // tutorialspoint.com for providing tutorials on how to use jUnit
        // properly.
        Assert.assertEquals(17, player.getAcrobatics());
        Assert.assertEquals(10, player.getBarter());
        Assert.assertEquals(17, player.getBruteforce());
        Assert.assertEquals(7, player.getIntuition());
        Assert.assertEquals(17, player.getSpeech());
        Assert.assertEquals(20, player.getAwareness());
    }

}
