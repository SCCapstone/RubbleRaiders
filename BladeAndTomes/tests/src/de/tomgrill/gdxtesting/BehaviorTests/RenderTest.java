package de.tomgrill.gdxtesting.BehaviorTests;

import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.headless.mock.input.MockInput;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
public class RenderTest {

    // Thanks to Crigges for pointing out this stage workaround:
    //https://stackoverflow.com/questions/51728471/unit-testing-libgdx-classes-that-call-a-shaderprogram-such-as-a-stage-via-a-he
    Stage test = new Stage(new ScreenViewport(), mock(SpriteBatch.class));

    Player player = new Player();

    /**
     * Makes sure that the render is being mocked and is not null.
     */
    @Test
    public void checkMockRender() {
        Assert.assertNotNull(Gdx.gl);
    }

    /**
     * Mocks to make sure that the stage is existent and allows for simple troubleshooting in case of bad render.
     */
    @Test
    public void checkSuccessfulMock() {
        Assert.assertNotNull(test);
    }

    /**
     * The literal point of the function is to make sure that the keyboard is still being read and that it isn't
     * null when I assign the player as an actor.
     */
    @Test
    public void checkKeyboard() {
        test.addActor(player.playerIcon);
        test.setKeyboardFocus(player.playerIcon);
        Assert.assertNotNull(test.getKeyboardFocus());
    }

    /**
     * Moves the player manually to the designated space despite their being no render of the player since
     * the test is being ran via a headless application.
     */
    @Test
    public void movePlayerManual() {

        //https://stackoverflow.com/questions/69507957/libgdx-unit-test-for-key-input-using-robot-to-simulate-key-press
        //Thanks to user Boranader for providing the inspiration and the answer to how to mock input and provide a hint
        //as to what could be done to automate input
        Gdx.input = mock(MockInput.class);
        Gdx.graphics = mock(Gdx.graphics.getClass());

        //Mock statements used to make sure that returned values are what I want them to be
        //for the test
        when(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)).thenReturn(true);
        when(Gdx.graphics.getDeltaTime()).thenReturn(5.0f);

        //Allows player movement via one method
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
        {
            player.playerIcon.addAction(Actions.moveTo(-64, 0));
        }

        //Thanks to libGDX documentation for reminding me that the .act() method tells the stage to enact all actions
        player.playerIcon.act(Gdx.graphics.getDeltaTime());

        //Asserts a given statement to make sure the icon has genuinely moved in this version of the code
        Assert.assertNotEquals((int) player.playerIcon.getX(), 0);
    }
}
