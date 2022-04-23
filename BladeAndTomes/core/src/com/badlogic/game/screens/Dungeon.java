package com.badlogic.game.screens;


import ScreenOverlayRework.OverlayManager;

import com.badlogic.game.creatures.Goblin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.Random;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class Dungeon extends ScreenAdapter {

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;
    Texture eventTex;
    private Texture eventDodgeTex;
    private Texture eventBladeTex;
    private Texture eventHaggleTex;
    private Texture eventMerchantTex;
    private Texture eventStealTex;
    private Texture eventTimerTex;
    private Texture eventThanksTex;
    private Texture eventGoblinTex;
    private Texture eventTraderTex;
    private Texture eventJimmyTex;
    private Texture eventGhostTex;
    private Texture eventTrapTex;
    private Texture eventSuccessTex;
    private Texture eventFailTex;

    public int optionOneChoice = 0;
    public int optionTwoChoice = 0;
    private TextButton optionDone;
    private TextButton optionOne;
    private TextButton optionTwo;
    private boolean loadEventOnce = false;
    private boolean resetDungeonMusic = false;
    private boolean resetBattleMusic = true;

    Image backgroundImage;
    private Image eventDodgeImage;
    private Image eventGoblinImage;
    private Image eventTraderImage;
    private Image eventJimmyImage;
    private Image eventGhostImage;
    private Image eventTrapImage;
    private Image eventSuccessImage;
    private Image eventFailImage;
    private Image eventBladeImage;
    private Image eventHaggleImage;
    private Image eventMerchantImage;
    private Image eventStealImage;
    private Image eventTimerImage;
    private Image eventThanksImage;

    float eventX, eventY, eventSizeX, eventSizeY;
    RoomHandler roomHandler;

    Window returnMenu;
    TextButton returnChoices[];
    Label returnWarning;
    boolean safeGuard;
    boolean oneToken = true;

    Window deathMenu;
    TextButton deathChoices[];
    Label deathNotice;

    //Goblin[] goblins;
    Label tutorialMessage;
    TextButton next;
    int tutorialStep;
    boolean combatExplained;
    boolean chestExplained;

   /* Window exitMenu;
    Label warning;
    TextButton pauseOptions[];
    Table quitTable;
    InputListener escapeListen;*/
    static TextButton exit;

    private Goblin[] goblins;

    private Random generator = new Random();

    private final AssetManager manager = new AssetManager();

    public Dungeon(final BladeAndTomes game) {

        //Initial backbone values carried over
        this.GAME = game;
        //game.player.playerIcon.removeListener(Overworld.escapePauseOver);
        MOVE_DISTANCE = 64;
        GAME.resetElapsedTime();
        //Clears the stage instance
        GAME.stageInstance.clear();
        //Instances the player's inventory

        roomHandler = new RoomHandler(GAME.stageInstance, GAME.player, GAME.overlays, GAME);

        //make changes to the pause menu for dungeon
        Overworld.warning.setText("Do you want to exit the Dungeon?\nYour progress will be lost.");
        Overworld.chooseQuit.setText("Exit Dungeon");
        //creates new exit button in quit menu to exit back to overworld
        exit = new TextButton("Confirm", GAME.generalTextButtonStyle);
        exit.setPosition(GAME.stageInstance.getWidth()/3,GAME.stageInstance.getHeight()/3);
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GAME.stageInstance.clear();
                dispose();
                BladeAndTomes.exitDungeon = true;
                BladeAndTomes.enterDungeon = false;
                GAME.player.setHealthPoints(GAME.player.getFullHealth());
                GAME.setScreen(new Overworld(GAME));
            }
        });


        //set background info
        //Dungeon background images taken from https://opengameart.org/content/set-of-background-for-dungeon-room
        //Author of images Kamigeek
        // Textures rendered in for our event
        // currently giving it a reasonable range to spawn into, and keeping it in dungeon 1
        manager.load("DungeonRooms/EWRoom.png", Texture.class);
        manager.load("GoldChest.jpg", Texture.class);
        manager.load("EventImages/EventDodge.png", Texture.class);
        manager.load("EventImages/EventGoblin.png", Texture.class);
        manager.load("EventImages/EventTrader.png", Texture.class);
        manager.load("EventImages/EventJimmy.png", Texture.class);
        manager.load("EventImages/EventGhost.png", Texture.class);
        manager.load("EventImages/EventTrap.png", Texture.class);
        manager.load("EventImages/EventSuccess.png", Texture.class);
        manager.load("EventImages/EventBlade.png", Texture.class);
        manager.load("EventImages/EventHagle.png", Texture.class);
        manager.load("EventImages/EventMerchantxcf.png", Texture.class);
        manager.load("EventImages/EventStealxcf.png", Texture.class);
        manager.load("EventImages/EventThanks.png", Texture.class);
        manager.load("EventImages/EventTimer.png", Texture.class);
        manager.load("EventImages/EventFail.png", Texture.class);
        //manager.load("Music/Battle.wav", Music.class);
        manager.finishLoading();
        //Texture background = manager.get("DungeonRooms/EWRoom.png");
        //backgroundImage = new Image(background);
        //eventTex = manager.get("GoldChest.jpg");
        //eventImage = new Image(eventTex);
        //eventSizeX = 120f;
        //eventSizeY = 120f;
        //eventX = MathUtils.random(360, 1600);
        //eventY = MathUtils.random(240, 960);

        eventDodgeTex = manager.get("EventImages/EventDodge.png");
        eventDodgeImage = new Image(eventDodgeTex);
        eventGoblinTex = manager.get("EventImages/EventGoblin.png");
        eventGoblinImage = new Image(eventGoblinTex);
        eventTraderTex = manager.get("EventImages/EventTrader.png");
        eventTraderImage = new Image(eventTraderTex);
        eventJimmyTex = manager.get("EventImages/EventJimmy.png");
        eventJimmyImage = new Image(eventJimmyTex);
        eventGhostTex = manager.get("EventImages/EventGhost.png");
        eventGhostImage = new Image(eventGhostTex);
        eventTrapTex = manager.get("EventImages/EventTrap.png");
        eventTrapImage = new Image(eventTrapTex);
        eventSuccessTex = manager.get("EventImages/EventSuccess.png");
        eventSuccessImage = new Image(eventSuccessTex);
        eventFailTex = manager.get("EventImages/EventFail.png");
        eventFailImage = new Image(eventFailTex);
        eventBladeTex = manager.get("EventImages/EventBlade.png");
        eventBladeImage = new Image(eventBladeTex);
        eventHaggleTex = manager.get("EventImages/EventHagle.png");
        eventHaggleImage = new Image(eventBladeTex);
        eventMerchantTex = manager.get("EventImages/EventMerchantxcf.png");
        eventMerchantImage = new Image(eventBladeTex);
        eventStealTex = manager.get("EventImages/EventStealxcf.png");
        eventStealImage = new Image(eventBladeTex);
        eventTimerTex = manager.get("EventImages/EventTimer.png");
        eventTimerImage = new Image(eventBladeTex);
        eventThanksTex = manager.get("EventImages/EventThanks.png");
        eventThanksImage = new Image(eventBladeTex);

        optionOne = new TextButton("", GAME.generalTextButtonStyle);
        optionOne.hit((GAME.WINDOWWIDTH / 2) - 150, (GAME.WINDOWHIGHT / 2) - 150, true);
        optionOne.setX((GAME.WINDOWWIDTH / 2) - 150);
        optionOne.setY((GAME.WINDOWHIGHT / 2) - 150);
        optionOne.setWidth(100);
        optionOne.setHeight(100);
        optionTwo = new TextButton("", GAME.generalTextButtonStyle);
        optionOne.hit((GAME.WINDOWWIDTH / 2) + 50, (GAME.WINDOWHIGHT / 2) - 150, true);
        optionTwo.setX((GAME.WINDOWWIDTH / 2) + 50);
        optionTwo.setY((GAME.WINDOWHIGHT / 2) - 150);
        optionTwo.setWidth(100);
        optionTwo.setHeight(100);
        optionDone = new TextButton("Done", GAME.generalTextButtonStyle);
        optionDone.hit((GAME.WINDOWWIDTH / 2) - 50, (GAME.WINDOWHIGHT / 2) - 150, true);
        optionDone.setX((GAME.WINDOWWIDTH / 2) - 50);
        optionDone.setY((GAME.WINDOWHIGHT / 2) - 150);
        optionDone.setWidth(100);
        optionDone.setHeight(100);

        GAME._bgmusic.playDungeonMusic();

        goblins = new Goblin[0];

        // Thanks to Alex Facer for providing the dimensions of the original background. I (Aidan) rescaled the
        // image so that it would properly fit within the confines of the background.
        //roomHandler.level.getBackgroundImage().setSize(2000,1150);
        //roomHandler.level.getBackgroundImage().setPosition(-25,-20);
        //GAME.stageInstance.addActor(roomHandler.level.getBackgroundImage());

        //Adds the player's icon to the stage.
        GAME.player.playerIcon.setPosition(GAME.stageInstance.getWidth()/2,GAME.stageInstance.getHeight()/2);
        GAME.player.moveSquare.setPosition(GAME.stageInstance.getWidth()/2,GAME.stageInstance.getHeight()/2);
        GAME.player.interactSquare.setPosition(GAME.stageInstance.getWidth()/2 - MOVE_DISTANCE,GAME.stageInstance.getHeight()/2 - MOVE_DISTANCE);
        GAME.stageInstance.addActor(GAME.player.playerIcon);
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
            GAME.player.setBruteforce(30);
            GAME.player.setHealthPoints(100);
            //if goblin in room, set step to 2 to explain combat
        }

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

                //Clears the menus and removes all elements
                returnMenu.clear();
                returnMenu.remove();

                //Clears the stage of all elements and makes sure to remove alot of unneeded elements
                GAME.stageInstance.clear();
                roomHandler.deconstruct(false);

                //Makes sure Anirudh's Overlays are now visible
                GAME.overlays.setOverLayesVisibility(true);
                //roomHandler.level.getBackgroundImage().setSize(2000, 1150);
                //roomHandler.level.getBackgroundImage().setPosition(-25, -20);
                //GAME.stageInstance.addActor(roomHandler.level.getBackgroundImage());

                //Sets the x and y position of the player as well as making the player controllable
                GAME.player.playerIcon.setPosition(GAME.stageInstance.getWidth()/2, GAME.stageInstance.getHeight()/2);
                GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);
                GAME.stageInstance.addActor(GAME.player.playerIcon);

                //Renders the Overlay properly for the player
                GAME.overlays.render();

                //Save player

                GAME.loadSaveManager.savePlayer(GAME.player, GAME.currentSaveIndex);

                //Makes sure repeated instances are free
                safeGuard = false;

                //Sets the exit to not be available for players
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
                GAME.player.setGold(GAME.player.getGold() + (int) (10f * (float) roomHandler.getGoblinsKilled() * roomHandler.getLevelMultiplier())+5);
                GAME.loadSaveManager.savePlayer(GAME.player, GAME.currentSaveIndex);
                //GAME.player.kEarnedGoldThroughLevels++;
                BladeAndTomes.exitDungeon = true;
                BladeAndTomes.enterDungeon = false;
                GAME.setScreen(new Overworld(GAME));
                safeGuard = false;
            }
        });

        //Lists the death choices for him
        deathMenu = new Window("", GAME.generalWindowStyle);
        deathMenu.setWidth(600);
        deathMenu.setHeight(400);
        deathMenu.setMovable(true);
        deathMenu.setKeepWithinStage(true);
        deathMenu.setPosition(GAME.stageInstance.getWidth()/3, GAME.stageInstance.getHeight()/3);
        //GAME.player.playerIcon.addListener(escapeListen);

        //Gives the player a warning before exit the dungeon

        //Let's the player know that he/she has died. Based on Label code in MainMenu
        deathNotice = new Label("You have died!", GAME.generalLabelStyle);
        deathNotice.setAlignment(1,1);

        //Defines the Choices the player can make as objects
        deathChoices = new TextButton[2];

        for(int i=0; i<2; i++) {
            deathChoices[i] = new TextButton("", GAME.generalTextButtonStyle);
        }

        //Creates the TextButton needed for properly labelling the buttons
        deathChoices[0].setText("Return to Town");
        deathChoices[1].setText("Return to Menu");

        //Settles the player's choices
        deathChoices[0].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                GAME.stageInstance.clear();
                BladeAndTomes.exitDungeon = true;
                BladeAndTomes.enterDungeon = false;
                GAME.player.setHealthPoints(GAME.player.getFullHealth());
                GAME.setScreen(new Overworld(GAME));
            }
        });

        //Allows the player to choose between exiting to the overworld, or
        //going to the Main Menu
        deathChoices[1].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dispose();
                GAME.stageInstance.clear();
                GAME.setScreen(new MainMenu(GAME));
            }
        });


    }

    public void nextTutorial(){
        switch(tutorialStep){
            case 1:
                GAME.stageInstance.addActor(tutorialMessage);
                break;
            case 2: //if goblin appears in room, explain combat
                GAME.stageInstance.addActor(tutorialMessage);
                tutorialMessage.setText("Goblins are in this room!\nThere is close combat and ranged\ncombat items" +
                        "\nClick 'Q' and use movement keys to\nselect an enemy to attack. Then\nconfirm the attack with 'Enter'.");
                tutorialMessage.setPosition(GAME.stageInstance.getWidth()-300, GAME.stageInstance.getHeight()-200);
                combatExplained = true;
                break;
            case 5: //if chest appears, explain chests
                GAME.stageInstance.addActor(tutorialMessage);
                tutorialMessage.setText("There is a chest in this room.\n Go up to the chest and \nuse 'T' to open it. You\n can drag and drop any items\n" +
                        "into your inventory.");
                chestExplained = true;
                tutorialMessage.setPosition(GAME.stageInstance.getWidth()-300, GAME.stageInstance.getHeight()-200);
                break;
            case 7: //done
                tutorialMessage.remove();
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

        if (!roomHandler.combatFlag &&
                roomHandler.level.getMapID() == 1) {
            roomHandler.combatFlag = true;
            roomHandler.spawnGoblin();
        }

        //The code for adding columns and how to put things into the Window
        //is based off of Alex Facer's code for the Windows and Menus
        if (roomHandler.level.getMapID() == 10 &&
                GAME.GRID_X[10] == GAME.player.playerIcon.getX() &&
                GAME.GRID_Y[5] == GAME.player.playerIcon.getY() && !safeGuard) {
            GAME.stageInstance.setKeyboardFocus(null);
            GAME.stageInstance.addActor(returnMenu);
            returnMenu.add(returnWarning).center().colspan(4);
            returnMenu.row();
            returnMenu.add(returnChoices[0], returnChoices[1]).center();
            GAME.player.kDungeonsExplored++;
            GAME.player.setHealthPoints(GAME.player.getFullHealth());
            safeGuard = true;
        }

        if (roomHandler.level.getMapID() == 2) {
            //TODO: Set up chest properly
            roomHandler.checkTouchChest();
            //GAME.chest.setTreasureChestVisible(!chest.isTreasureChestVisible());
            //GAME.overlays.displayChest(chest);
        }

        GAME.batch.begin();
        GAME.batch.draw(roomHandler.level.getBackgroundText(), -25, -20, 2000, 1150);
        GAME.runPlayerAnimation();
        //Tutorial checks
        if (MainMenu.isTutorial && roomHandler.level.getChest() != null) { //chest
            setTutorial(5);
            nextTutorial();
        }
        if (MainMenu.isTutorial && roomHandler.combatFlag && combatExplained == false) { //combat
            setTutorial(2);
            nextTutorial();
        }

        if (Gdx.input.isKeyJustPressed(GAME.controls.getMoveUp())) {
            GAME.resetElapsedTime();
            GAME.runMoveUpAnimation();
        } else if (Gdx.input.isKeyJustPressed(GAME.controls.getMoveDown())) {
            GAME.resetElapsedTime();
            GAME.runMoveDownAnimation();
        } else if (Gdx.input.isKeyJustPressed(GAME.controls.getMoveLeft())) {
            GAME.resetElapsedTime();
            GAME.runMoveLeftAnimation();
        } else if (Gdx.input.isKeyJustPressed(GAME.controls.getMoveRight())) {
            GAME.resetElapsedTime();
            GAME.runMoveRightAnimation();
        }
        if (roomHandler.combatFlag) {
            if (Gdx.input.isKeyJustPressed(GAME.controls.getFightAction())) {
                GAME.resetElapsedTime();
                GAME.runAttackDownAnimation();
            }
        }
        goblins = roomHandler.getGoblins();
        if (goblins.length > 0) {
            for (Goblin goblin : goblins) {
                if (goblin != null) {
                    goblin.runAnimation(GAME);
                }
            }
        }
        GAME.batch.end();
        GAME.stageInstance.draw();
        //inventory.update();
        GAME.overlays.setOverLayesVisibility(true);


        //Decides if combat movement or normal movement will be used
        if(roomHandler.combatFlag) {
            roomHandler.handleCombat();
            if(resetBattleMusic) {
                GAME._bgmusic.playBattleMusic();
                resetBattleMusic = false;
            }
            resetDungeonMusic = true;
        }
        else {
            GAME.player.isTurn = true;
            roomHandler.movement();
            if(resetDungeonMusic) {
                GAME._bgmusic.playDungeonMusic();
                resetDungeonMusic = false;
            }
            resetBattleMusic = true;
        }
        if(roomHandler.eventFlag) {
            GAME.stageInstance.setKeyboardFocus(null);
            if(!loadEventOnce) {
                int eventType = generator.nextInt(12);
                switch (eventType) {
                    case (0):
                        eventGoblinImage.setX((GAME.WINDOWWIDTH / 2) - 250);
                        eventGoblinImage.setY((GAME.WINDOWHIGHT / 2) - 250);
                        GAME.stageInstance.addActor(eventGoblinImage);
                        optionOne.setText("Speech");
                        optionOneChoice = 0;
                        optionTwo.setText("Brute Force");
                        optionTwoChoice = 0;
                        break;
                    case (1):
                        eventDodgeImage.setX((GAME.WINDOWWIDTH / 2) - 250);
                        eventDodgeImage.setY((GAME.WINDOWHIGHT / 2) - 250);
                        GAME.stageInstance.addActor(eventDodgeImage);
                        optionOne.setText("Awareness");
                        optionOneChoice = 1;
                        optionTwo.setText("Acrobatics");
                        optionTwoChoice = 1;
                        break;
                    case (2):
                        eventTraderImage.setX((GAME.WINDOWWIDTH / 2) - 250);
                        eventTraderImage.setY((GAME.WINDOWHIGHT / 2) - 250);
                        GAME.stageInstance.addActor(eventTraderImage);
                        optionOne.setText("Intuition");
                        optionOneChoice = 2;
                        optionTwo.setText("Barter");
                        optionTwoChoice = 2;
                        break;
                    case (3):
                        eventJimmyImage.setX((GAME.WINDOWWIDTH / 2) - 250);
                        eventJimmyImage.setY((GAME.WINDOWHIGHT / 2) - 250);
                        GAME.stageInstance.addActor(eventJimmyImage);
                        optionOne.setText("Brute Force");
                        optionOneChoice = 3;
                        optionTwo.setText("Barter");
                        optionTwoChoice = 2;
                        break;
                    case (4):
                        eventGhostImage.setX((GAME.WINDOWWIDTH / 2) - 250);
                        eventGhostImage.setY((GAME.WINDOWHIGHT / 2) - 250);
                        GAME.stageInstance.addActor(eventGhostImage);
                        optionOne.setText("Awareness");
                        optionOneChoice = 1;
                        optionTwo.setText("Speech");
                        optionTwoChoice = 3;
                        break;
                    case (5):
                        eventTrapImage.setX((GAME.WINDOWWIDTH / 2) - 250);
                        eventTrapImage.setY((GAME.WINDOWHIGHT / 2) - 250);
                        GAME.stageInstance.addActor(eventTrapImage);
                        optionOne.setText("Intuition");
                        optionOneChoice = 2;
                        optionTwo.setText("Acrobatics");
                        optionTwoChoice = 1;
                        break;
                    case (6):
                        eventBladeImage.setX((GAME.WINDOWWIDTH / 2) - 250);
                        eventBladeImage.setY((GAME.WINDOWHIGHT / 2) - 250);
                        GAME.stageInstance.addActor(eventBladeImage);
                        optionOne.setText("Brute Force");
                        optionOneChoice = 3;
                        optionTwo.setText("Awareness");
                        optionTwoChoice = 1;
                        break;
                    case (7):
                        eventHaggleImage.setX((GAME.WINDOWWIDTH / 2) - 250);
                        eventHaggleImage.setY((GAME.WINDOWHIGHT / 2) - 250);
                        GAME.stageInstance.addActor(eventHaggleImage);
                        optionOne.setText("Barter");
                        optionOneChoice = 2;
                        optionTwo.setText("Awareness");
                        optionTwoChoice = 1;
                        break;
                    case (8):
                        eventMerchantImage.setX((GAME.WINDOWWIDTH / 2) - 250);
                        eventMerchantImage.setY((GAME.WINDOWHIGHT / 2) - 250);
                        GAME.stageInstance.addActor(eventMerchantImage);
                        optionOne.setText("Acrobatics");
                        optionOneChoice = 2;
                        optionTwo.setText("Barter");
                        optionTwoChoice = 2;
                        break;
                    case (9):
                        eventStealImage.setX((GAME.WINDOWWIDTH / 2) - 250);
                        eventStealImage.setY((GAME.WINDOWHIGHT / 2) - 250);
                        GAME.stageInstance.addActor(eventStealImage);
                        optionOne.setText("Acrobatics");
                        optionOneChoice = 2;
                        optionTwo.setText("Speech");
                        optionTwoChoice = 1;
                        break;
                    case (10):
                        eventThanksImage.setX((GAME.WINDOWWIDTH / 2) - 250);
                        eventThanksImage.setY((GAME.WINDOWHIGHT / 2) - 250);
                        GAME.stageInstance.addActor(eventThanksImage);
                        optionOne.setText("Intuition");
                        optionOneChoice = 2;
                        optionTwo.setText("Speech");
                        optionTwoChoice = 1;
                        break;
                    case (11):
                        eventTimerImage.setX((GAME.WINDOWWIDTH / 2) - 250);
                        eventTimerImage.setY((GAME.WINDOWHIGHT / 2) - 250);
                        GAME.stageInstance.addActor(eventTimerImage);
                        optionOne.setText("Intuition");
                        optionOneChoice = 2;
                        optionTwo.setText("Brute Force");
                        optionTwoChoice = 1;
                        break;
                }
                GAME.stageInstance.addActor(optionOne);
                GAME.stageInstance.addActor(optionTwo);
                loadEventOnce = true;
            }

            optionOne.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {

                    switch(optionOneChoice) {
                        case(0):
                            if(GAME.player.getSpeech() > 10) {
                                if(oneToken) {
                                    GAME.player.tokens.set(GAME.player.tokens.get() + 1);
                                    oneToken = false;
                                }
                                eventGoblinImage.remove();
                                eventTraderImage.remove();
                                eventDodgeImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventSuccessImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventSuccessImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventSuccessImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            else {
                                eventGoblinImage.remove();
                                eventTraderImage.remove();
                                eventDodgeImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventFailImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventFailImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventFailImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            break;
                        case(1):
                            if(GAME.player.getAwareness() > 10) {
                                if(oneToken) {
                                    GAME.player.tokens.set(GAME.player.tokens.get() + 1);
                                    oneToken = false;
                                }
                                eventDodgeImage.remove();
                                eventGoblinImage.remove();
                                eventTraderImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventSuccessImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventSuccessImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventSuccessImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            else {
                                eventDodgeImage.remove();
                                eventGoblinImage.remove();
                                eventTraderImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventFailImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventFailImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventFailImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            break;
                        case(2):
                            if(GAME.player.getIntuition() > 10) {
                                if(oneToken) {
                                    GAME.player.tokens.set(GAME.player.tokens.get() + 1);
                                    oneToken = false;
                                }
                                eventTraderImage.remove();
                                eventGoblinImage.remove();
                                eventDodgeImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventSuccessImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventSuccessImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventSuccessImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            else {
                                eventTraderImage.remove();
                                eventGoblinImage.remove();
                                eventDodgeImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventFailImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventFailImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventFailImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            break;
                        case(3):
                            if(GAME.player.getBruteforce() > 10) {
                                if(oneToken) {
                                    GAME.player.tokens.set(GAME.player.tokens.get() + 1);
                                    oneToken = false;
                                }
                                eventTraderImage.remove();
                                eventGoblinImage.remove();
                                eventDodgeImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventSuccessImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventSuccessImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventSuccessImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            else {
                                eventTraderImage.remove();
                                eventGoblinImage.remove();
                                eventDodgeImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventFailImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventFailImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventFailImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            break;
                    }
                    GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);
                }
            });
            optionTwo.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    switch(optionTwoChoice) {
                        case(0):
                            if(GAME.player.getBruteforce() > 10) {
                                if(oneToken) {
                                    GAME.player.tokens.set(GAME.player.tokens.get() + 1);
                                    oneToken = false;
                                }
                                eventGoblinImage.remove();
                                eventDodgeImage.remove();
                                eventTraderImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventSuccessImage.setX((GAME.WINDOWWIDTH / 2) - 250);
                                eventSuccessImage.setY((GAME.WINDOWHIGHT / 2) - 250);
                                GAME.stageInstance.addActor(eventSuccessImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            else {
                                eventGoblinImage.remove();
                                eventDodgeImage.remove();
                                eventTraderImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventFailImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventFailImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventFailImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            break;
                        case(1):
                            if(GAME.player.getAcrobatics() > 10) {
                                if(oneToken) {
                                    GAME.player.tokens.set(GAME.player.tokens.get() + 1);
                                    oneToken = false;
                                }
                                eventDodgeImage.remove();
                                eventGoblinImage.remove();
                                eventTraderImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventSuccessImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventSuccessImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventSuccessImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            else {
                                eventDodgeImage.remove();
                                eventGoblinImage.remove();
                                eventTraderImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventFailImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventFailImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventFailImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            break;
                        case(2):
                            if(GAME.player.getBarter() > 10) {
                                if(oneToken) {
                                    GAME.player.tokens.set(GAME.player.tokens.get() + 1);
                                    oneToken = false;
                                }
                                eventTraderImage.remove();
                                eventGoblinImage.remove();
                                eventDodgeImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventSuccessImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventSuccessImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventSuccessImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            else {
                                eventTraderImage.remove();
                                eventGoblinImage.remove();
                                eventDodgeImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventFailImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventFailImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventFailImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            break;
                        case(3):
                            if(GAME.player.getSpeech() > 10) {
                                if(oneToken) {
                                    GAME.player.tokens.set(GAME.player.tokens.get() + 1);
                                    oneToken = false;
                                }
                                eventTraderImage.remove();
                                eventGoblinImage.remove();
                                eventDodgeImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventSuccessImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventSuccessImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventSuccessImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            else {
                                eventTraderImage.remove();
                                eventGoblinImage.remove();
                                eventDodgeImage.remove();
                                eventGhostImage.remove();
                                eventJimmyImage.remove();
                                eventTrapImage.remove();
                                eventBladeImage.remove();
                                eventHaggleImage.remove();
                                eventMerchantImage.remove();
                                eventStealImage.remove();
                                eventTimerImage.remove();
                                eventThanksImage.remove();
                                optionOne.remove();
                                optionTwo.remove();
                                eventFailImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventFailImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventFailImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            break;
                    }
                    GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);
                }
            });
            optionDone.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    roomHandler.eventFlag = false;
                    loadEventOnce = false;
                    oneToken = true;
                    optionDone.remove();
                    eventSuccessImage.remove();
                    eventFailImage.remove();
                    eventDodgeImage.remove();
                    eventGoblinImage.remove();
                    eventTraderImage.remove();
                    GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);
                    eventGhostImage.remove();
                    eventJimmyImage.remove();
                    eventTrapImage.remove();
                }
            });
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
            deathMenu.add(deathNotice).colspan(4).width(deathMenu.getWidth()/3+30);
            deathMenu.row();
            deathMenu.add(deathChoices[0], deathChoices[1]);
            BladeAndTomes.exitDungeon = true;
            BladeAndTomes.enterDungeon = false;
            GAME.setScreen(new Overworld(GAME));
        }
        if(Gdx.input.isKeyJustPressed(GAME.controls.getOpenInventory())) {
            GAME.overlays.setHiddenTableVisibility(!GAME.showHiddenInventory);
            if(GAME.showHiddenInventory) {
                GAME.stageInstance.setKeyboardFocus(null);
            }
            else {
                GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);
            }
        }

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
