package de.tomgrill.gdxtesting.UnitTests;

import com.badlogic.game.screens.Room;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class RoomNullTest {
    @Test
    public void RoomNullTest() {
        Room room = new Room();
        Assert.assertNotNull(room);
    }
}
