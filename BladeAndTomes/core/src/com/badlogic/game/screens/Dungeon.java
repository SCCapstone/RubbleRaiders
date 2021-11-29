package com.badlogic.game.screens;

import ScreenOverlay.MainInventory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Dungeon extends ScreenAdapter {

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;
    Image playerIcon;
    Texture background;
    Image backgroundImage;
    MainInventory inventory;

    Rectangle walkableArea;
    Rectangle doorHitBox;
    float xIcon, yIcon, xMove, yMove, xInter, yInter;

    int roomId;

    public Dungeon(final BladeAndTomes game) {

        this.GAME = game;
        MOVE_DISTANCE = 64;

        GAME.player.playerIcon.setPosition(960,690);
        GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);

        //set background info
        //Dungeon background images taken from https://opengameart.org/content/set-of-background-for-dungeon-room
        //Author of images Kamigeek
        background = new Texture(Gdx.files.internal("MainDungeon.png"));
        backgroundImage = new Image(background);
        backgroundImage.setSize(2000,1350);
        backgroundImage.setPosition(-25,-20);
        GAME.stageInstance.addActor(backgroundImage);


        // Used dimensions of the room as a reference point thanks to Alex Farcer for providing them later in code
        // (See render function)
        walkableArea = new Rectangle();
        walkableArea.setSize((int) backgroundImage.getWidth() - 3*MOVE_DISTANCE, (int) backgroundImage.getHeight() - 3*MOVE_DISTANCE);
        walkableArea.setCenter(walkableArea.getWidth()/2, walkableArea.getHeight()/2);
        walkableArea.setPosition(MOVE_DISTANCE*3, MOVE_DISTANCE*3);

        //Adds the player's icon to the stage.
        GAME.stageInstance.addActor(playerIcon);
        inventory = new MainInventory(GAME);
    }

    @Override
    public void render(float delta) {

        // Thanks to user "centenond" on StackOverflow for pointing out a useful function on using rectangle to detect.
        // Co-Opted for use in our Project for creating walkable areas and loading zones.
        //https://stackoverflow.com/questions/61491889/how-to-detect-collisions-between-objects-in-libgdx
        if (!GAME.player.moveSquare.overlaps(walkableArea)) {
            if (GAME.player.playerIcon.getY() > (walkableArea.getY() + walkableArea.getHeight())) {
                GAME.player.playerIcon.setPosition(xIcon, yIcon - 3*MOVE_DISTANCE);
                GAME.player.moveSquare.setPosition(xMove, yMove - 3*MOVE_DISTANCE);
                GAME.player.interactSquare.setPosition(xInter, yInter - 3*MOVE_DISTANCE);
            }
            else if (GAME.player.playerIcon.getY() < walkableArea.getY()) {
                GAME.player.playerIcon.setPosition(xIcon, yIcon + 3*MOVE_DISTANCE);
                GAME.player.moveSquare.setPosition(xMove, yMove + 3*MOVE_DISTANCE);
                GAME.player.interactSquare.setPosition(xInter, yInter + 3*MOVE_DISTANCE);
            }

            if (GAME.player.playerIcon.getX() > (walkableArea.getX() + walkableArea.getWidth())) {
                GAME.player.playerIcon.setPosition(xIcon - 3*MOVE_DISTANCE, yIcon);
                GAME.player.moveSquare.setPosition(xMove - 3*MOVE_DISTANCE, yMove);
                GAME.player.interactSquare.setPosition(xInter - 3*MOVE_DISTANCE, yInter);
            }
            else if (GAME.player.playerIcon.getX() < walkableArea.getX()) {
                GAME.player.playerIcon.setPosition(xIcon + 3*MOVE_DISTANCE, yIcon);
                GAME.player.moveSquare.setPosition(xMove + 3*MOVE_DISTANCE, yMove);
                GAME.player.interactSquare.setPosition(xInter + 3*MOVE_DISTANCE, yInter);
            }
        }
        else {
            xIcon = GAME.player.playerIcon.getX();
            yIcon = GAME.player.playerIcon.getY();
            xMove = GAME.player.moveSquare.getX();
            yMove = GAME.player.moveSquare.getY();
            xInter = GAME.player.interactSquare.getX();
            yInter = GAME.player.interactSquare.getY();
        }


        //Simplifying render thanks to libGDX for their "Extending the Simple Game" Tutorial,
        //Specifically the advanced section on super.render() as well as the following section on the main
        //game screen
        //https://libgdx.com/dev/simple-game-extended/
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
        GAME.stageInstance.draw();
        inventory.update();

        //Two side rooms - one to the left and one to the right
        //Alex Farcer programmed in hitbox for Dungeon Doors. Improved on it to distinguish different rooms in the dungeon.
        if(roomId == 0 &&
                (GAME.player.playerIcon.getX() <= 150 && GAME.player.playerIcon.getY() < 750 && GAME.player.playerIcon.getY() > 500) ||
                (GAME.player.playerIcon.getX() > 1920 - 150 && GAME.player.playerIcon.getY() < 750 && GAME.player.playerIcon.getY() > 500))
        {
            if(GAME.player.playerIcon.getX() > 1920 - 150 && GAME.player.playerIcon.getY() < 750 && GAME.player.playerIcon.getY() > 500) {
                roomId = 1;
            }
            else {
                roomId = 2;
            }

            GAME.stageInstance.clear();
            backgroundImage = new Image(new Texture(Gdx.files.internal("SideDungeon.png")));
            GAME.stageInstance.addActor(backgroundImage);
            GAME.stageInstance.addActor(GAME.player.playerIcon);
            GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);
        }
        else if (roomId != 0)
        {

        }
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
