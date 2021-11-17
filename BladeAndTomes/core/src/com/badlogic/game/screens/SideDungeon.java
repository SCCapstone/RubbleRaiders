package com.badlogic.game.screens;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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

        background = new Texture(Gdx.files.internal("SideDungeon.png"));
        backgroundImage = new Image(background);
        backgroundImage.setSize(2000,1350);
        backgroundImage.setPosition(-25,-20);
        GAME.stageInstance.addActor(backgroundImage);

        playerIcon = new Image(new Texture(Gdx.files.internal("PlayerIcon.jpg")));
        playerIcon.setPosition(960, 1110);

        playerIcon.addListener(new InputListener() {

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.LEFT) {
                    playerIcon.addAction(Actions.moveTo(playerIcon.getX() - MOVE_DISTANCE, playerIcon.getY(), 0));
                    checkEnterRoom();
                    return true;
                }

                if (keycode == Input.Keys.RIGHT) {
                    playerIcon.addAction(Actions.moveTo(playerIcon.getX() + MOVE_DISTANCE, playerIcon.getY(), 0));
                    checkEnterRoom();
                    return true;
                }

                if (keycode == Input.Keys.UP) {
                    playerIcon.addAction(Actions.moveTo(playerIcon.getX(), playerIcon.getY() + MOVE_DISTANCE, 0));
                    checkEnterRoom();
                    return true;
                }

                if (keycode == Input.Keys.DOWN) {
                    playerIcon.addAction(Actions.moveTo(playerIcon.getX(), playerIcon.getY() - MOVE_DISTANCE, 0));
                    checkEnterRoom();
                    return true;
                } else {
                    return false;
                }
            }
            public void checkEnterRoom()
            {
                //Go back to main dungeon
                if((playerIcon.getX() > 800 && playerIcon.getX() < 1050 && playerIcon.getY() < 150))
                {
                    GAME.stageInstance.clear();
                    dispose();
                    GAME.setScreen(new Dungeon(GAME));
                }
            }

        });

        GAME.stageInstance.setKeyboardFocus(playerIcon);
        //Adds the player's icon to the stage.
        GAME.stageInstance.addActor(playerIcon);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(GAME.stageInstance);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

}
