package com.badlogic.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;

import static com.badlogic.gdx.graphics.Color.FOREST;

public class Overworld extends ScreenAdapter {

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;
    Image playerIcon;

    public Overworld (final BladeAndTomes game) {

        this.GAME = game;

        MOVE_DISTANCE = 64;

        //TODO: Simplify all of this into Player class?
        //TODO: Move Player Icon Definitions to Backbone?
        playerIcon = new Image(new Texture(Gdx.files.internal("PlayerIcon.jpg")));
        playerIcon.setPosition(960, 1110);

        // Thank you to libGDX.info editors for creating a helpful tutorial
        // on MoveActions as well as the libGDX creators for teaching pool-able actions
        // and InputListeners on their wiki.
        // https://libgdx.info/basic_action/
        // https://github.com/libgdx/libgdx/wiki/Scene2d
        playerIcon.addListener(new InputListener() {

            @Override
            public boolean keyDown(InputEvent event, int keycode)
            {
                if(keycode == Input.Keys.LEFT) {
                    playerIcon.addAction(Actions.moveTo(playerIcon.getX() - MOVE_DISTANCE, playerIcon.getY(),0));
                    checkOffMap();
                    return true;
                }

                if(keycode == Input.Keys.RIGHT) {
                    playerIcon.addAction(Actions.moveTo(playerIcon.getX() + MOVE_DISTANCE, playerIcon.getY(),0));
                    checkOffMap();
                    return true;
                }

                if(keycode == Input.Keys.UP) {
                    playerIcon.addAction(Actions.moveTo(playerIcon.getX(), playerIcon.getY() + MOVE_DISTANCE,0));
                    checkOffMap();
                    return true;
                }

                if(keycode == Input.Keys.DOWN) {
                    playerIcon.addAction(Actions.moveTo(playerIcon.getX(), playerIcon.getY() - MOVE_DISTANCE,0));
                    checkOffMap();
                    return true;
                }

                else {
                    return false;
                }
            }

            /**
             * Function that makes sure to moves the screen to the dungeon screen so that the player can leave the overworld
             * and adventure within the depths.
             */
            public void checkOffMap()
            {
                //TODO: Reduce compound statement into something easily runnable.
                if((playerIcon.getX() <= 128 || playerIcon.getY() <= 128) || (playerIcon.getX() > 1920 - 128 || playerIcon.getY() > 1280 - 128))
                {
                    GAME.stageInstance.clear();
                    dispose();
                    GAME.setScreen(new Dungeon(GAME));
                }
            }
        });

        //Reference page that referred to how to set up Keyboard Focus by the libGDX developers
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/Stage.html#setKeyboardFocus-com.badlogic.gdx.scenes.scene2d.Actor-
        GAME.stageInstance.setKeyboardFocus(playerIcon);

        //Adds the player's icon to the stage.
        GAME.stageInstance.addActor(playerIcon);


    }

    @Override
    public void render(float delta){

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
