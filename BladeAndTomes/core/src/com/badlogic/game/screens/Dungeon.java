package com.badlogic.game.screens;

import ScreenOverlay.Events;
import ScreenOverlay.MainInventory;
import com.badlogic.game.creatures.Goblin;
import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Dungeon extends ScreenAdapter {

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;
    //Image playerIcon;
    //Texture background;
    Texture eventTex;

    Image eventImage;
    Image backgroundImage;
    private boolean inRoom;
    MainInventory inventory;
    float eventX, eventY, eventSizeX, eventSizeY, enemyX, enemyY;
    //Goblin goblin;
    boolean isEnemyTurn;
    RoomHandler roomHandler;

    Events event;

    //Rectangle walkableArea;
    //Rectangle doorHitBox;
    //float xIcon, yIcon, xMove, yMove, xInter, yInter;

    int roomId;

    public Dungeon(final BladeAndTomes game) {

        this.GAME = game;
        MOVE_DISTANCE = 64;

        GAME.stageInstance.clear();

        roomHandler = new RoomHandler(GAME.stageInstance, GAME.player, inventory);

        //set background info
        //Dungeon background images taken from https://opengameart.org/content/set-of-background-for-dungeon-room
        //Author of images Kamigeek
        Texture background = new Texture(Gdx.files.internal("DungeonRooms/EWRoom.png"));
        backgroundImage = new Image(background);

        // Textures rendered in for our event
        // currently giving it a reasonable range to spawn into, and keeping it in dungeon 1
        eventTex = new Texture(Gdx.files.internal("GoldChest.jpg"));
        eventImage = new Image(eventTex);
        eventSizeX = 120f;
        eventSizeY = 120f;
        eventX = MathUtils.random(360, 1600);
        eventY = MathUtils.random(240, 960);

        // Thanks to Alex Farcer for providing the dimensions of the original background. I (Aidan) rescaled the
        // image so that it would properly fit within the confines of the background.
        roomHandler.level.getBackgroundImage().setSize(2000,1150);
        roomHandler.level.getBackgroundImage().setPosition(-25,-20);
        GAME.stageInstance.addActor(roomHandler.level.getBackgroundImage());

        //backgroundImage.setSize(2000, 1150);
        //backgroundImage.setPosition(-25, -20);
        //GAME.stageInstance.addActor(backgroundImage);

        // Currently having size as a set variable here want to move it to events class
        // This should keep it permanently in place through the dungeon right now
        eventImage.setSize(eventSizeX, eventSizeY);
        eventImage.setPosition(eventX, eventY);
        GAME.stageInstance.addActor(eventImage);


        // Used dimensions of the room as a reference point thanks to Alex Farcer for providing them later in code
        // (See render function)
        /*
        walkableArea = new Rectangle();
        walkableArea.setSize((int) backgroundImage.getWidth() - 3*MOVE_DISTANCE, (int) backgroundImage.getHeight() - 3*MOVE_DISTANCE);
        walkableArea.setCenter(walkableArea.getWidth()/2, walkableArea.getHeight()/2);
        walkableArea.setPosition(MOVE_DISTANCE*3, MOVE_DISTANCE*3);*/

        //Adds the player's icon to the stage.
        GAME.player.playerIcon.setPosition(GAME.stageInstance.getWidth()/2,GAME.stageInstance.getHeight()/2);
        GAME.player.moveSquare.setPosition(GAME.stageInstance.getWidth()/2,GAME.stageInstance.getHeight()/2);
        GAME.player.interactSquare.setPosition(GAME.stageInstance.getWidth()/2 - MOVE_DISTANCE,GAME.stageInstance.getHeight()/2 - MOVE_DISTANCE);
        GAME.stageInstance.addActor(GAME.player.playerIcon);
        GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);

        //Instances the player's inventory
        inventory = new MainInventory(GAME);


        //goblin = new Goblin(GAME.player);
        //isEnemyTurn = true;

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
        inventory.update();

        roomHandler.movement();

        if(roomHandler.combatFlag) {
            for (int i = 0; i < roomHandler.numOfGoblins; i++) {
                if (roomHandler.goblin[i] == null) {
                    continue;
                }
                roomHandler.handleCombat(roomHandler.goblin[i]);
            }
        }

        if(GAME.player.getHealthPoints() <= 0) {
            dispose();
            GAME.stageInstance.clear();
            //GAME.stageInstance.addActor();
            GAME.setScreen(new MainMenu(GAME));
        }
    }

    @Override
    public void resize(int width, int height) {
        GAME.stageInstance.getViewport().update(width, height, true);
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
