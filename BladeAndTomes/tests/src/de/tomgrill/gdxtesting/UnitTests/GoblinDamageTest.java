package de.tomgrill.gdxtesting.UnitTests;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.creatures.Goblin;
import com.badlogic.game.creatures.Player;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class GoblinDamageTest {
    @Test
    public void GoblinDamageTest() {
        BladeAndTomes game = new BladeAndTomes();
        Goblin test = new Goblin(new Player(), game.GRID_X, game.GRID_Y);
        test.damageTaken(5);
        Assert.assertEquals(10, test.getHealthPoints());
    }
}
