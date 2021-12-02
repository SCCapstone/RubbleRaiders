package ScreenOverlay;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Events {
    private final BladeAndTomes Game;
    // private final Button[] eventChoice;
    float eventArea, eventWidth, eventHeight;
    Label Title, Description;

    public Events(BladeAndTomes game) {
        Game = game;
        eventWidth = 120f;
        eventArea = 120f;
        eventHeight = 120f;

    }

    public void update() {}

    public void show() {}
}
