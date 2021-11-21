package com.badlogic.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Dungeon extends ScreenAdapter {

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;
    Image playerIcon;
    Texture background;
    Image backgroundImage;

    public Dungeon(final BladeAndTomes game) {

        this.GAME = game;
        MOVE_DISTANCE = 64;

        //set background info
        //Dungeon background images taken from https://opengameart.org/content/set-of-background-for-dungeon-room
        //Author of images Kamigeek
        background = new Texture(Gdx.files.internal("MainDungeon.png"));
        backgroundImage = new Image(background);
        backgroundImage.setSize(2000,1350);
        backgroundImage.setPosition(-25,-20);
        GAME.stageInstance.addActor(backgroundImage);

        //set player image and location
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
                //Two side rooms - one to the left and one to the right
                if((playerIcon.getX() <= 150 && playerIcon.getY() < 750 && playerIcon.getY() > 500) ||
                        (playerIcon.getX() > 1920 - 150 && playerIcon.getY() < 750 && playerIcon.getY() > 500))
                {
                    GAME.stageInstance.clear();
                    dispose();
                    GAME.setScreen(new SideDungeon(GAME));
                }
            }

        });

        GAME.stageInstance.setKeyboardFocus(playerIcon);

        //Adds the player's icon to the stage.
        GAME.stageInstance.addActor(playerIcon);
    }

    @Override
    public void render(float delta) {

        //Simplifying render thanks to libGDX for their "Extending the Simple Game" Tutorial,
        //Specifically the advanced section on super.render() as well as the following section on the main
        //game screen
        //https://libgdx.com/dev/simple-game-extended/
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
        GAME.stageInstance.draw();
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
