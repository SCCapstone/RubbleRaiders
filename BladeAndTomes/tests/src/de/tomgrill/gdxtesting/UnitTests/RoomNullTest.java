package de.tomgrill.gdxtesting.UnitTests;

import com.badlogic.game.screens.Room;
import com.badlogic.gdx.scenes.scene2d.Stage;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class RoomNullTest {
    @Test
    public void RoomNullTest() {
        Room room = new Room();
        Assert.assertNotNull(room);
    }
}
