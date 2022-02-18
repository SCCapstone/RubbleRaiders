package de.tomgrill.gdxtesting.UnitTests;

import com.badlogic.game.creatures.Player;
import com.badlogic.game.screens.Room;
import com.badlogic.game.screens.RoomHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(GdxTestRunner.class)
public class RoomTest {

    @Test
    public void RoomTest() {
        Stage test = new Stage(new ScreenViewport(), mock(SpriteBatch.class));
        Gdx.graphics = mock(Gdx.graphics.getClass());
        Player testPlayer = new Player();
        test.addActor(testPlayer.playerIcon);

        RoomHandler testHandle = new RoomHandler(test, testPlayer);
        Room temp = testHandle.level;
        testPlayer.playerInput.keyDown(new InputEvent(), Input.Keys.LEFT);
        testHandle.movement();

        Assert.assertEquals(temp, testHandle.level);
    }
}
