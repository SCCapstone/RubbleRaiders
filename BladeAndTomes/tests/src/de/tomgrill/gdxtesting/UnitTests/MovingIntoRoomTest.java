package de.tomgrill.gdxtesting.UnitTests;

import ScreenOverlayRework.OverlayManager;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.creatures.Player;
import com.badlogic.game.screens.RoomHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class MovingIntoRoomTest {
    @Test
    public void MovingIntoRoomTest() {
        Stage test = new Stage(new ScreenViewport(), mock(SpriteBatch.class));
        Gdx.graphics = mock(Gdx.graphics.getClass());

        Player testPlayer = new Player();

        //RoomHandler testHandle = new RoomHandler(test, testPlayer, mock(OverlayManager.class), mock(BladeAndTomes.class));

        //testHandle.generateLevelLayout();
        //testPlayer.playerInput.keyDown(new InputEvent(), Input.Keys.DOWN);
        //testHandle.movement();
    }
}
