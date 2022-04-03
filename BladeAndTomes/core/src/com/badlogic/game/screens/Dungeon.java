package com.badlogic.game.screens;


import ScreenOverlayRework.Inventory.ItemUI.Quest.QuestDocument;
import ScreenOverlayRework.OverlayManager;
import com.badlogic.game.creatures.Player;

import com.badlogic.game.creatures.Goblin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import jdk.tools.jmod.Main;

import java.util.HashMap;

public class Dungeon extends ScreenAdapter {

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;
    Texture eventTex;

    Image eventImage;
    Image backgroundImage;
    float eventX, eventY, eventSizeX, eventSizeY;
    RoomHandler roomHandler;

    Label tutorialMessage;
    TextButton next;
    int tutorialStep;
    boolean combatExplained;
    boolean chestExplained;

    private Goblin[] goblins;

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


    }

    public void nextTutorial(){
        switch(tutorialStep){
            case 1:
                GAME.stageInstance.addActor(tutorialMessage);
                break;
            case 2: //if goblin appears in room, explain combat
                GAME.stageInstance.addActor(tutorialMessage);
                tutorialMessage.setText("Goblins are in this room!\nMove to a goblin and use 'Q'\n to attack!\n\nThere is also ranged combat items\nSelect the enemy to attack\n using XXXX and use 'Q' to attack.");
                tutorialMessage.setPosition(GAME.stageInstance.getWidth()-300, GAME.stageInstance.getHeight()-200);
                next.setPosition(tutorialMessage.getX()+100, tutorialMessage.getY()-50);
                break;
            case 5: //if chest appears, explain events
                tutorialMessage.setText("There is a chest in this room.\n...");
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
        GAME.stageInstance.draw();
        GAME.batch.begin();
        GAME.runPlayerAnimation();
        //Tutorial checks
        if(MainMenu.isTutorial && eventImage.isVisible() && tutorialStep != 1){
            setTutorial(5);
            nextTutorial();
        }
        if(MainMenu.isTutorial && roomHandler.combatFlag && combatExplained==false) {
            setTutorial(2);
            nextTutorial();
        }

        //GAME.vectorPlayerMovement();
        //GAME.playerMovement();

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
            roomHandler.movement();
        }
        GAME.stageInstance.act(Gdx.graphics.getDeltaTime());

        //If player is dead, return to the OverWorld
        if(GAME.player.getHealthPoints() <= 0) {
            dispose();
            GAME.stageInstance.clear();
            GAME.player.setHealthPoints(GAME.player.getFullHealth());
            BladeAndTomes.exitDungeon = true;
            BladeAndTomes.enterDungeon = false;
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
