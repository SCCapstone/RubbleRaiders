package de.tomgrill.gdxtesting.BehaviorTests;

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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//GdxTestRunner written by Thomas Pronold (TomGrill) for purpose of using as a framework for Gdx testing
//Source: https://github.com/TomGrill/gdx-testing
@RunWith(GdxTestRunner.class)
public class MovementHorizontalTest {

    /**
     * Tests to make sure the input listeners are listening by instigating InputEvents and then making the computer
     * Move left and right
     */
    @Test
    public void movementHorizontalTest() {

        //https://stackoverflow.com/questions/69507957/libgdx-unit-test-for-key-input-using-robot-to-simulate-key-press
        //Thanks to user Boranader for providing the inspiration and the answer to how to mock input and provide a hint
        //as to what could be done to automate input
        Stage test = new Stage(new ScreenViewport(), mock(SpriteBatch.class));
        Gdx.graphics = mock(Gdx.graphics.getClass());
        Player player = new Player();

        //Adds actor to the stage and focuses the keyboard on him
        test.addActor(player.playerIcon);
        test.setKeyboardFocus(player.playerIcon);

        //Mocks the graphics function to return 5 seconds
        when(Gdx.graphics.getDeltaTime()).thenReturn(5.0f);

        //Series of inputs that need to be added.
        player.playerInput.keyDown(new InputEvent(), Input.Keys.RIGHT);
        player.playerInput.keyDown(new InputEvent(), Input.Keys.RIGHT);
        player.playerInput.keyDown(new InputEvent(), Input.Keys.LEFT);

        //Making the stage act each and everyone of them out.
        test.act(Gdx.graphics.getDeltaTime());
        test.act(Gdx.graphics.getDeltaTime());
        test.act(Gdx.graphics.getDeltaTime());

        //Asserting that the location of the icon will be there where it says it is
        Assert.assertEquals(-64, (int) player.playerIcon.getX());

    }
}
