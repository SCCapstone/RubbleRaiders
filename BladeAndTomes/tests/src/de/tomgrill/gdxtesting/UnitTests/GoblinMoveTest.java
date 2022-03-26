package de.tomgrill.gdxtesting.UnitTests;

import com.badlogic.game.creatures.Goblin;
import com.badlogic.game.creatures.Player;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class GoblinMoveTest {
    @Test
    public void GoblinMoveTest() {
        Player playerTest = new Player();
        Goblin test  = new Goblin(playerTest);

        Assert.assertFalse(test.movement());
    }
}
