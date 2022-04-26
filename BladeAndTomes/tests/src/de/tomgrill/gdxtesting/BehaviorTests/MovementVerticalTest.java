package de.tomgrill.gdxtesting.BehaviorTests;

import com.badlogic.game.EntityUI.EntityUIBase;
import com.badlogic.game.EntityUI.PlayerEnitityUI;
import com.badlogic.game.creatures.Player;
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

import static org.mockito.Mockito.*;

//GdxTestRunner written by Thomas Pronold (TomGrill) for purpose of using as a framework for Gdx testing
//Source: https://github.com/TomGrill/gdx-testing
@RunWith(GdxTestRunner.class)
public class MovementVerticalTest {

    EntityUIBase entity;
    PlayerEnitityUI player;

    /**
     * Tests to make sure the input listeners are listening by instigating InputEvents and then making the computer
     * Move up and down
     */
    @Test
    public void movementVerticalTest() {

        //https://stackoverflow.com/questions/69507957/libgdx-unit-test-for-key-input-using-robot-to-simulate-key-press
        //Thanks to user Boranader for providing the inspiration and the answer to how to mock input and provide a hint
        //as to what could be done to automate input
        Stage test = new Stage(new ScreenViewport(), mock(SpriteBatch.class));

        //Mocks the graphics class so that functions can be called with intended results
        Gdx.graphics = mock(Gdx.graphics.getClass());

       entity = mock(EntityUIBase.class);
       player = mock(PlayerEnitityUI.class);

        //Adds actor to the stage and focuses the keyboard onto him
        //test.addActor(player.playerIcon);
        //test.setKeyboardFocus(player.playerIcon);

        //Forces the call of .getDeltaTime() to send a signal of 5 seconds
        when(Gdx.graphics.getDeltaTime()).thenReturn(5.0f);

        entity.moveDown();
        entity.moveUP();
        entity.moveUP();

        //Sets in a few inputs into the input listener
        //player.playerInput.keyDown(new InputEvent(), Input.Keys.DOWN);
        //player.playerInput.keyDown(new InputEvent(), Input.Keys.UP);
        //player.playerInput.keyDown(new InputEvent(), Input.Keys.DOWN);

        //Makes sure each action in the input listener is carried out
        test.act(Gdx.graphics.getDeltaTime());
        test.act(Gdx.graphics.getDeltaTime());
        test.act(Gdx.graphics.getDeltaTime());

        //Asserts that the
        //Assert.assertEquals(-64, (int) player.playerIcon.getY());
        //Assert.assertEquals(160, );
    }
}
