package com.badlogic.game.screens;

import ScreenOverlay.Events;
import ScreenOverlay.MainInventory;
import com.badlogic.game.creatures.Goblin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.Random;

public class Dungeon extends ScreenAdapter {

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;
    Texture eventTex;
    private Texture eventDodgeTex;
    private Texture eventGoblinTex;
    private Texture eventTraderTex;
    private Texture eventSuccessTex;
    private Texture eventFailTex;

    public int optionOneChoice;
    public int optionTwoChoice;
    private TextButton optionDone;
    private TextButton optionOne;
    private TextButton optionTwo;
    private boolean loadEventOnce = false;
    private boolean resetDungeonMusic = false;
    private boolean resetBattleMusic = true;

    Image eventImage;
    Image backgroundImage;
    private Image eventDodgeImage;
    private Image eventGoblinImage;
    private Image eventTraderImage;
    private Image eventSuccessImage;
    private Image eventFailImage;

    float eventX, eventY, eventSizeX, eventSizeY;
    RoomHandler roomHandler;

    private Goblin[] goblins;

    private Random generator = new Random();

    private final AssetManager manager = new AssetManager();

    public Dungeon(final BladeAndTomes game) {

        //Initial backbone values carried over
        this.GAME = game;
        MOVE_DISTANCE = 64;
        GAME.resetElapsedTime();
        //Clears the stage instance
        GAME.stageInstance.clear();

        //Instances the player's inventory

        roomHandler = new RoomHandler(GAME.stageInstance, GAME.player, GAME.overlays);

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
        manager.load("EventImages/EventSuccess.png", Texture.class);
        manager.load("EventImages/EventFail.png", Texture.class);
        //manager.load("Music/Battle.wav", Music.class);
        manager.finishLoading();
        Texture background = manager.get("DungeonRooms/EWRoom.png");
        backgroundImage = new Image(background);
        eventTex = manager.get("GoldChest.jpg");
        eventImage = new Image(eventTex);
        eventSizeX = 120f;
        eventSizeY = 120f;
        eventX = MathUtils.random(360, 1600);
        eventY = MathUtils.random(240, 960);

        eventDodgeTex = manager.get("EventImages/EventDodge.png");
        eventDodgeImage = new Image(eventDodgeTex);
        eventGoblinTex = manager.get("EventImages/EventGoblin.png");
        eventGoblinImage = new Image(eventGoblinTex);
        eventTraderTex = manager.get("EventImages/EventTrader.png");
        eventTraderImage = new Image(eventTraderTex);
        eventSuccessTex = manager.get("EventImages/EventSuccess.png");
        eventSuccessImage = new Image(eventSuccessTex);
        eventFailTex = manager.get("EventImages/EventFail.png");
        eventFailImage = new Image(eventFailTex);

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

        // Thanks to Alex Farcer for providing the dimensions of the original background. I (Aidan) rescaled the
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
//        inventory = new MainInventory(GAME);

        game.overlays.setOverLayesVisibility(true);


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
        GAME.batch.begin();
        GAME.runPlayerAnimation();

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
                    /*if(goblin.isAttacking) {
                        goblin.resetElapsedTime();
                        goblin.runAttackAnimation();
                    }*/
                    /*if(goblin.moving) {
                        goblin.resetElapsedTime();
                        goblin.runMovingAnimation();
                    }*/
                    goblin.runAnimation(GAME);
                }
            }
        }
        GAME.batch.end();
        //inventory.update();

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
            roomHandler.movement();
            if(resetDungeonMusic) {
                GAME._bgmusic.playDungeonMusic();
                resetDungeonMusic = false;
            }
            resetBattleMusic = true;
        }
        optionOneChoice = 0;
        optionTwoChoice = 0;
        if(roomHandler.eventFlag) {
            if(!loadEventOnce) {
                int eventType = generator.nextInt(3);
                switch (eventType) {
                    case (0):
                        eventGoblinImage.setX((GAME.WINDOWWIDTH / 2) - 250);
                        eventGoblinImage.setY((GAME.WINDOWHIGHT / 2) - 250);
                        GAME.stageInstance.addActor(eventGoblinImage);
                        optionOne.setText("Speech");
                        optionTwo.setText("Brute Force");
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
                                GAME.player.skillToken++;
                                eventGoblinImage.remove();
                                eventTraderImage.remove();
                                eventDodgeImage.remove();
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
                                GAME.player.skillToken++;
                                eventDodgeImage.remove();
                                eventGoblinImage.remove();
                                eventTraderImage.remove();
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
                                GAME.player.skillToken++;
                                eventTraderImage.remove();
                                eventGoblinImage.remove();
                                eventDodgeImage.remove();
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
                                optionOne.remove();
                                optionTwo.remove();
                                eventFailImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventFailImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventFailImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            break;
                    }
                }
            });
            optionTwo.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    switch(optionTwoChoice) {
                        case(0):
                            if(GAME.player.getBruteforce() > 10) {
                                GAME.player.skillToken++;
                                eventGoblinImage.remove();
                                eventDodgeImage.remove();
                                eventTraderImage.remove();
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
                                GAME.player.skillToken++;
                                eventDodgeImage.remove();
                                eventGoblinImage.remove();
                                eventTraderImage.remove();
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
                                GAME.player.skillToken++;
                                eventTraderImage.remove();
                                eventGoblinImage.remove();
                                eventDodgeImage.remove();
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
                                optionOne.remove();
                                optionTwo.remove();
                                eventFailImage.setX((GAME.WINDOWWIDTH/2)-250);
                                eventFailImage.setY((GAME.WINDOWHIGHT/2)-250);
                                GAME.stageInstance.addActor(eventFailImage);
                                GAME.stageInstance.addActor(optionDone);
                            }
                            break;
                    }
                }
            });
            optionDone.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    roomHandler.eventFlag = false;
                    loadEventOnce = false;
                    optionDone.remove();
                    eventSuccessImage.remove();
                    eventFailImage.remove();
                    eventDodgeImage.remove();
                    eventGoblinImage.remove();
                    eventTraderImage.remove();
                }
            });
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
