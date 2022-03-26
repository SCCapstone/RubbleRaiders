package de.tomgrill.gdxtesting.UnitTests;

import com.badlogic.game.creatures.Goblin;
import com.badlogic.game.creatures.Player;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class GoblinTest {
    @Test
    public void spawnGoblinTest() {
        Goblin goblin = new Goblin(new Player());
        Assert.assertNotNull(goblin);
    }
}
