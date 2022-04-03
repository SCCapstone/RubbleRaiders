package com.badlogic.game.screens;


import ScreenOverlayRework.Inventory.ItemUI.Quest.QuestDocument;
import ScreenOverlayRework.Inventory.TreasureChest.TreasureChestUI;
import ScreenOverlayRework.OverlayManager;
import com.badlogic.game.creatures.Player;

import com.badlogic.game.creatures.Goblin;
import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import jdk.tools.jmod.Main;

import java.util.HashMap;

public class Dungeon extends ScreenAdapter {

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;

    Image backgroundImage;

    RoomHandler roomHandler;

    Window returnMenu;
    TextButton returnChoices[];
    Label returnWarning;
    boolean safeGuard;

    Window deathMenu;
    TextButton deathChoices[];
    Label deathNotice;

    //Goblin[] goblins;
    Label tutorialMessage;
    TextButton next;
    int tutorialStep;
    boolean combatExplained;
    boolean chestExplained;

    Window pauseMenu;
    Label warning;
    TextButton options[];
    Table quitTable;
    InputListener escapePauseOver;

    private Goblin[] goblins;

    public Dungeon(final BladeAndTomes game) {

        //Initial backbone values carried over
        this.GAME = game;
        MOVE_DISTANCE = 64;
        GAME.resetElapsedTime();
        //Clears the stage instance
        GAME.stageInstance.clear();
        //Instances the player's inventory

        roomHandler = new RoomHandler(GAME.stageInstance, GAME.player, GAME.overlays, GAME);

        //set background info
        //Dungeon background images taken from https://opengameart.org/content/set-of-background-for-dungeon-room
        //Author of images Kamigeek
        Texture background = new Texture(Gdx.files.internal("DungeonRooms/EWRoom.png"));
        backgroundImage = new Image(background);

        // Thanks to Alex Facer for providing the dimensions of the original background. I (Aidan) rescaled the
        // image so that it would properly fit within the confines of the background.
        roomHandler.level.getBackgroundImage().setSize(2000,1150);
        roomHandler.level.getBackgroundImage().setPosition(-25,-20);
        GAME.stageInstance.addActor(roomHandler.level.getBackgroundImage());

        //Adds the player's icon to the stage.
        GAME.player.playerIcon.setPosition(GAME.stageInstance.getWidth()/2,GAME.stageInstance.getHeight()/2);
        GAME.player.moveSquare.setPosition(GAME.stageInstance.getWidth()/2,GAME.stageInstance.getHeight()/2);
        GAME.player.interactSquare.setPosition(GAME.stageInstance.getWidth()/2 - MOVE_DISTANCE,GAME.stageInstance.getHeight()/2 - MOVE_DISTANCE);
        GAME.stageInstance.addActor(GAME.player.playerIcon);
        //GAME.player.playerIcon.setVisible(true);
        GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);

        //Instances the player's inventory
        //inventory = new MainInventory(GAME);
        game.overlays = new OverlayManager(game);
        game.overlays.setOverLayesVisibility(true);

        //Tutorial
        tutorialStep = 1;
        combatExplained = false;
        chestExplained = false;
        tutorialMessage = new Label("This is the dungeon!\n\nExplore rooms of the dungeon to find\n a goblin to fight.", GAME.generalLabelStyle);
        tutorialMessage.setPosition(GAME.stageInstance.getWidth()/2-200, GAME.stageInstance.getHeight()/2);
        tutorialMessage.setSize(300f, 200f);
        tutorialMessage.setAlignment(1,1);
        tutorialMessage.setZIndex(1);
        next = new TextButton("Next", GAME.generalTextButtonStyle);
        next.setSize(100f, 50f);
        next.setZIndex(1);
        next.setPosition(tutorialMessage.getX()+100, tutorialMessage.getY()-50);
        next.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tutorialStep++;
                nextTutorial();
            }
        });
        if(MainMenu.isTutorial){
            nextTutorial();
            //if goblin in room, set step to 2 to explain combat
        }

        pauseMenu = new Window("", GAME.generalWindowStyle);
        pauseMenu.setHeight(500);
        pauseMenu.setWidth(700);
        pauseMenu.setPosition(GAME.stageInstance.getWidth()/3, GAME.stageInstance.getHeight()/3);
        pauseMenu.setMovable(true);
        pauseMenu.setKeepWithinStage(true);

        /*saveQuit = new Window("SaveQuit", GAME.generalWindowStyle);
        saveQuit.setSize(GAME.stageInstance.getWidth()/3,GAME.stageInstance.getHeight());
        saveQuit.setPosition(GAME.stageInstance.getWidth()*0.35f, GAME.stageInstance.getHeight()*0.35f);*/

        escapePauseOver = new InputListener() {
            public boolean keyDown(InputEvent event, int keycode)
            {
                if(keycode == Input.Keys.ESCAPE)
                {
                    GAME.stageInstance.setKeyboardFocus(null);
                    GAME.stageInstance.addActor(pauseMenu);
                    pauseMenu.add(warning).colspan(4).width(pauseMenu.getWidth()/3+25);
                    pauseMenu.row();
                    pauseMenu.row();
                    pauseMenu.add(options[0], options[1]);
                }
                return true;
            }
        };
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
                GAME.loadSaveManager.savePlayer(GAME.player, GAME.currentSaveIndex);
                GAME.player.setGold(GAME.player.getGold() + (int) (10f * (float) roomHandler.getGoblinsKilled() * roomHandler.getLevelMultiplier()));
                //GAME.player.kEarnedGoldThroughLevels++;
                GAME.setScreen(new Overworld(GAME));
                safeGuard = false;
            }
        });

        deathMenu = new Window("", GAME.generalWindowStyle);
        deathMenu.setWidth(600);
        deathMenu.setHeight(400);
        deathMenu.setMovable(true);
        deathMenu.setKeepWithinStage(true);
        deathMenu.setPosition(GAME.stageInstance.getWidth()/3, GAME.stageInstance.getHeight()/3);
        GAME.player.playerIcon.addListener(escapePauseOver);

        warning = new Label("Are you sure you want\nto exit the Dungeon?", GAME.generalLabelStyle);
        warning.setSize(300f, 200f);
        warning.setAlignment(1,1);

        options = new TextButton[] {
                new TextButton("Confirm", GAME.generalTextButtonStyle),
                new TextButton("Cancel", GAME.generalTextButtonStyle)
        };

        options[0].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GAME.stageInstance.clear();
                pauseMenu.clear();
                pauseMenu.remove();
                dispose();
                GAME.setScreen(new Overworld(GAME));

        deathNotice = new Label("You have died!", GAME.generalLabelStyle);

        deathChoices = new TextButton[2];

        for(int i=0; i<2; i++) {
            deathChoices[i] = new TextButton("", GAME.generalTextButtonStyle);
        }
        deathChoices[0].setText("Return to Town");
        deathChoices[1].setText("Return to Menu");

        deathChoices[0].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                GAME.stageInstance.clear();
                GAME.setScreen(new Overworld(GAME));
            }
        });

        deathChoices[1].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                GAME.stageInstance.clear();
                GAME.setScreen(new MainMenu(GAME));
            }
        });
            }
        });

        options[1].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);
                pauseMenu.clear();
                pauseMenu.remove();
            }
        });

        pauseMenu.setZIndex(1);
        options[0].setZIndex(1);
        options[1].setZIndex(1);

    }

    public void nextTutorial(){
        switch(tutorialStep){
            case 1:
                GAME.stageInstance.addActor(tutorialMessage);
                break;
            case 2: //if goblin appears in room, explain combat
                GAME.stageInstance.addActor(tutorialMessage);
                tutorialMessage.setText("Goblins are in this room!\nMove to a goblin and use 'Q'\n to attack!\n\nThere is also ranged combat items" +
                        "\nTo select an enemy to attack\n click 'Q' and confirm the \nattack with 'Enter'.");
                tutorialMessage.setPosition(GAME.stageInstance.getWidth()-300, GAME.stageInstance.getHeight()-200);
                next.setPosition(tutorialMessage.getX()+100, tutorialMessage.getY()-50);
                break;
            case 5: //if chest appears, explain chests
                tutorialMessage.setText("There is a chest in this room.\n Go up to the chest and \nuse 'T' to open it.");
                chestExplained = true;
                break;
            case 6: //find the exit portal
                tutorialMessage.setText("Continue exploring the dungeon\nand find the portal to exit.");
                BladeAndTomes.exitDungeon = true;
                BladeAndTomes.enterDungeon = false;
                break;
        }
    }

    public void setTutorial(int step){
        this.tutorialStep = step;
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
                GAME.GRID_X[10] == GAME.player.playerIcon.getX() &&
                GAME.GRID_Y[5] == GAME.player.playerIcon.getY() && !safeGuard) {
            GAME.stageInstance.setKeyboardFocus(null);
            GAME.stageInstance.addActor(returnMenu);
            returnMenu.add(returnWarning).center().colspan(3);
            returnMenu.row();
            returnMenu.add(returnChoices[0], returnChoices[1]).center();
            GAME.player.kDungeonsExplored++;
            safeGuard = true;
        }

        if(roomHandler.level.getMapID() == 2) {
            //TODO: Set up chest properly
            roomHandler.checkTouchChest();
            //GAME.chest.setTreasureChestVisible(!chest.isTreasureChestVisible());
            //GAME.overlays.displayChest(chest);
        }


        GAME.batch.begin();
        GAME.runPlayerAnimation();
        //Tutorial checks
        if(MainMenu.isTutorial && roomHandler.combatFlag && combatExplained==false) { //combat
            setTutorial(2);
            nextTutorial();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            GAME.resetElapsedTime();
            GAME.runMoveUpAnimation();
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            GAME.resetElapsedTime();
            GAME.runMoveDownAnimation();
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            GAME.resetElapsedTime();
            GAME.runMoveLeftAnimation();
        }
        else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            GAME.resetElapsedTime();
            GAME.runMoveRightAnimation();
        }
        if(roomHandler.combatFlag) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                GAME.resetElapsedTime();
                GAME.runAttackDownAnimation();
            }
        }
        goblins = roomHandler.getGoblins();
        if(goblins.length > 0){
            for(Goblin goblin: goblins) {
                if(goblin != null) {
                    goblin.runAnimation(GAME);
                }
            }
        }
        GAME.batch.end();
        //inventory.update();
        GAME.overlays.setOverLayesVisibility(true);


        //Decides if combat movement or normal movement will be used
        if(roomHandler.combatFlag) {
            roomHandler.handleCombat();
        }
        else {
            GAME.player.isTurn = true;
            roomHandler.movement();
        }
        GAME.stageInstance.act(Gdx.graphics.getDeltaTime());

        //If player is dead, return to the OverWorld
        if(GAME.player.getHealthPoints() <= 0) {
            dispose();
            GAME.player.kDeaths++;
            GAME.stageInstance.clear();
            GAME.player.setHealthPoints(GAME.player.getFullHealth());
            BladeAndTomes.exitDungeon = true;
            BladeAndTomes.enterDungeon = false;
            GAME.setScreen(new Overworld(GAME));

            GAME.stageInstance.setKeyboardFocus(null);
            GAME.stageInstance.addActor(deathMenu);
            deathMenu.add(deathNotice).center().colspan(3);
            deathMenu.row();
            deathMenu.add(deathChoices[0], deathChoices[1]).center();
            //GAME.setScreen(new Overworld(GAME));
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.E))
            GAME.overlays.setHiddenTableVisibility(!GAME.showHiddenInventory);

        GAME.overlays.render();
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
