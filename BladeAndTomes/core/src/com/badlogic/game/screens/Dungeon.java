package com.badlogic.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Dungeon extends ScreenAdapter {

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;
    Texture eventTex;

    Image eventImage;
    Image backgroundImage;
    float eventX, eventY, eventSizeX, eventSizeY;
    RoomHandler roomHandler;

    Window returnMenu;
    TextButton returnChoices[];
    Label returnWarning;
    boolean safeGuard;

    public Dungeon(final BladeAndTomes game) {

        //Initial backbone values carried over
        this.GAME = game;
        MOVE_DISTANCE = 64;

        //Clears the stage instance
        GAME.stageInstance.clear();

        //Instances the player's inventory

        roomHandler = new RoomHandler(GAME.stageInstance, GAME.player, GAME.overlays);

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

        // Thanks to Alex Facer for providing the dimensions of the original background. I (Aidan) rescaled the
        // image so that it would properly fit within the confines of the background.
        roomHandler.level.getBackgroundImage().setSize(2000,1150);
        roomHandler.level.getBackgroundImage().setPosition(-25,-20);
        GAME.stageInstance.addActor(roomHandler.level.getBackgroundImage());

        // Currently having size as a set variable here want to move it to events class
        // This should keep it permanently in place through the dungeon right now
        eventImage.setSize(eventSizeX, eventSizeY);
        eventImage.setPosition(eventX, eventY);
        GAME.stageInstance.addActor(eventImage);

        //Adds the player's icon to the stage.
        GAME.player.playerIcon.setPosition(GAME.stageInstance.getWidth()/2,GAME.stageInstance.getHeight()/2);
        GAME.player.moveSquare.setPosition(GAME.stageInstance.getWidth()/2,GAME.stageInstance.getHeight()/2);
        GAME.player.interactSquare.setPosition(GAME.stageInstance.getWidth()/2 - MOVE_DISTANCE,GAME.stageInstance.getHeight()/2 - MOVE_DISTANCE);
        GAME.stageInstance.addActor(GAME.player.playerIcon);
        GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);

        //Instances the player's inventory
        //inventory = new MainInventory(GAME);

        //Adds the overlays through Anirudh's code for overlays
        game.overlays.setOverLayesVisibility(true);

        //Based off of Window code in Dungeon for Save & Quit menu by Brent Able & Alex Facer
        returnMenu = new Window("", GAME.generalWindowStyle);
        returnMenu.setWidth(600);
        returnMenu.setHeight(400);
        returnMenu.setMovable(true);
        returnMenu.setKeepWithinStage(true);
        returnMenu.setPosition(GAME.stageInstance.getWidth()/3, GAME.stageInstance.getHeight()/3);

        //Based off of definition of label
        returnWarning = new Label("What do you choose adventurer?", GAME.generalLabelStyle);

        //Based on code in Dungeon for adding and setting buttons more dynamically suggested
        //and implemented by Anirudh Oruganti
        int num = 0;
        returnChoices = new TextButton[2];
        for(TextButton ignored : returnChoices) {
            returnChoices[num] = new TextButton("", GAME.generalTextButtonStyle);
            num++;
        }

        //Creates Text button labeled "Delve Further" that moves player to the next level
        //Based off of code by Alex Facer and Brent Able in Dungeon for Save & Quit game menu
        returnChoices[0].setText("Delve further");
        returnChoices[0].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                returnMenu.clear();
                returnMenu.remove();
                GAME.stageInstance.clear();
                roomHandler.deconstruct(false);

                GAME.overlays.setOverLayesVisibility(true);
                roomHandler.level.getBackgroundImage().setSize(2000, 1150);
                roomHandler.level.getBackgroundImage().setPosition(-25, -20);
                GAME.stageInstance.addActor(roomHandler.level.getBackgroundImage());

                GAME.player.playerIcon.setPosition(GAME.stageInstance.getWidth()/2, GAME.stageInstance.getHeight()/2);
                GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);
                GAME.stageInstance.addActor(GAME.player.playerIcon);

                safeGuard = false;
                roomHandler.setExitAvailability(false);
            }
        });

        //Creates a button labeled "Return to Town" that returns Player to town
        //Based off of code by Alex Facer for TextButton for Window
        returnChoices[1].setText("Return to Town");
        returnChoices[1].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                dispose();
                GAME.stageInstance.clear();
                GAME.player.setGold((int) (10f * (float) roomHandler.getGoblinsKilled() * roomHandler.getLevelMultiplier()));
                GAME.setScreen(new Overworld(GAME));
                safeGuard = false;
            }
        });
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

        if(!roomHandler.combatFlag &&
            roomHandler.level.getMapID() == 1) {
            roomHandler.combatFlag = true;
            roomHandler.spawnGoblin();
        }

        //The code for adding columns and how to put things into the Window
        //is based off of Alex Facer's code for the Windows and Menus
        if(roomHandler.level.getMapID() == 10 &&
                roomHandler.GRID_X[10] == GAME.player.playerIcon.getX() &&
                roomHandler.GRID_Y[5] == GAME.player.playerIcon.getY() && !safeGuard) {
            GAME.stageInstance.setKeyboardFocus(null);
            GAME.stageInstance.addActor(returnMenu);
            returnMenu.add(returnWarning).center().colspan(3);
            returnMenu.row();
            returnMenu.add(returnChoices[0], returnChoices[1]).center();
            safeGuard = true;
        }

        //Decides if combat movement or normal movement will be used
        if(roomHandler.combatFlag) {
            roomHandler.handleCombat();
        }
        else {
            roomHandler.movement();
        }

        //If player is dead, return to the OverWorld
        if(GAME.player.getHealthPoints() <= 0) {
            dispose();
            GAME.stageInstance.clear();
            GAME.player.setHealthPoints(GAME.player.getFullHealth());
            GAME.setScreen(new Overworld(GAME));
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.E))
            GAME.overlays.setHiddenTableVisibility(!GAME.showHiddenInventory);
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
