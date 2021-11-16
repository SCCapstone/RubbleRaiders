package com.badlogic.game.screens;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class SideDungeon extends ScreenAdapter {

    //Accessible only from the main Dungeon screen
    //Can only exit to a main Dungeon room

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;
    Image playerIcon;
    Texture background;
    Image backgroundImage;

    public SideDungeon(final BladeAndTomes game) {
        this.GAME = game; 
        MOVE_DISTANCE = 64;

    }
}
