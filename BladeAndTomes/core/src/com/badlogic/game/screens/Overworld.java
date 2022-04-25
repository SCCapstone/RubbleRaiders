package com.badlogic.game.screens;

import Keyboard_Mouse_Controls.SaveLoadGame;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCBuyer;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCSeller;
import ScreenOverlayRework.Inventory.NPCInventoryUI.TownHallQuestBoard;
import ScreenOverlayRework.Inventory.TreasureChest.TreasureChestUI;
import ScreenOverlayRework.OverlayManager;
import Sounds.playerMoveSound;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.EntityUI.EntitiesHandler;
import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;
import jdk.tools.jmod.Main;

import java.awt.*;

public class Overworld extends ScreenAdapter {

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;
    SpriteBatch batch;

    private World world;
    private TiledMap overWorldMap;

    private AssetManager manager;
    private OrthogonalTiledMapRenderer renderer;

    int objectLayerId;
    boolean collidedX, collidedY;


    Window pauseMenu;
    static Label warning;
    static TextButton options[];
    ChangeListener quitOption;
    static InputListener escapePauseOver;
    Label controls;
    TextButton exitControls;
    TextButton viewControls;
    static TextButton chooseQuit;
    TextButton exitPause;

    Point NPC_Cords;
    Point Portal_Cords;
    boolean doTrade;

    TextButton saveBack;
    Label tutorialMessage;
    TextButton next;
    static int tutorialStep;

    TreasureChestUI chest1;
    TreasureChestUI chest2;

    TownHallQuestBoard questBoardTrade;
    boolean isQuestBoardTradeVisible;

    // Helpful Collision Detection Tutorials (NOT IMPLEMENTED IN CODE YET)
    // TODO: IMPLEMENT THESE IN CODE
    //https://stackoverflow.com/questions/61491889/how-to-detect-collisions-between-objects-in-libgdx
    //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/math/Intersector.html
    EntitiesHandler entitiesHandler;
    public Overworld (final BladeAndTomes game) {
        // Displays TownHall Interact msg
        // The labels part of anris work for trading and selling
        // Is also piggybacking off of collision
        game.player = game.loadSaveManager.loadPlayer(game.currentSaveIndex);
        game.player.playerIcon.setPosition(1920/2,1080/2);

        //Passes current animations and game controls to player to make sure it has those stored
        //Keyboard Controls are done by Anirudh Oruganti and Alex Facer.
        //Animation functions were researched and performed by Miller Banford
        game.player.setKeyControl(game.controls);
        game.player.setCurrentAnimation(game.getCurrentAnimation());


        this.GAME = game;
        GAME.resetElapsedTime();
        objectLayerId = 2;
        GAME._bgmusic.playTownMusic();

        manager = new AssetManager();
        MOVE_DISTANCE = 64;
        doTrade = false;
        batch = new SpriteBatch();

        collidedX = false;
        collidedY = false;

        //set up pause menu window
        pauseMenu = new Window("", GAME.generalWindowStyle);
        pauseMenu.setHeight(500);
        pauseMenu.setWidth(700);
        pauseMenu.setPosition(GAME.stageInstance.getWidth()/3, GAME.stageInstance.getHeight()/3);
        pauseMenu.setMovable(true);
        pauseMenu.setKeepWithinStage(true);

        //set up three options in pause menu (controls, exit game, exit menu)
        viewControls = new TextButton("View Controls", GAME.generalTextButtonStyle);
        viewControls.setSize(150,100);
        viewControls.setPosition(GAME.stageInstance.getWidth()/2-150,GAME.stageInstance.getHeight()/2);
        chooseQuit = new TextButton("Quit", GAME.generalTextButtonStyle);
        chooseQuit.setSize(150,100);
        chooseQuit.setPosition(GAME.stageInstance.getWidth()/2,GAME.stageInstance.getHeight()/2);
        chooseQuit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
               viewControls.remove();
               chooseQuit.remove();
               exitPause.remove();
                GAME.stageInstance.addActor(pauseMenu);
                pauseMenu.add(warning).colspan(4).width(pauseMenu.getWidth()/3+25);
                pauseMenu.row();
                pauseMenu.row();
                if(BladeAndTomes.enterDungeon == false){
                    pauseMenu.add(options[0], options[1]);
                } else {
                    pauseMenu.add(Dungeon.exit);
                    pauseMenu.add(options[1]);
                }
            }
        });

        exitPause = new TextButton("Close", GAME.generalTextButtonStyle);
        exitPause.setSize(100, 50);
        exitPause.setPosition(viewControls.getX()+100, viewControls.getY()-50);
        exitPause.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);
                viewControls.remove();
                chooseQuit.remove();
                exitPause.remove();
            }
        });

        //label showing player their current controls in game
        controls = new Label("", GAME.generalLabelStyle);
        controls.setText("        Controls:\n        Move Up: "+ Input.Keys.toString(GAME.controls.getMoveUp())
                +"\n        Move Left: "+Input.Keys.toString(GAME.controls.getMoveLeft())
                +"\n        Move Down: "+Input.Keys.toString(GAME.controls.getMoveDown())
                +"\n        Move Right: "+Input.Keys.toString(GAME.controls.getMoveRight())
                +"\n        Trading/Chests: "+Input.Keys.toString(GAME.controls.getTradeMenu())
                +"\n        Pause: "+Input.Keys.toString(GAME.controls.getOpenPauseMenu())
                +"\n        Inventory: "+Input.Keys.toString(GAME.controls.getOpenInventory())
                +"\n        Combat: "+Input.Keys.toString(GAME.controls.getFightAction())
                +"\n        Slot 1: "+Input.Keys.toString(GAME.controls.getItem1())
                +"\n        Slot 2: "+Input.Keys.toString(GAME.controls.getItem2())
                +"\n        Slot 3: "+Input.Keys.toString(GAME.controls.getItem3())
                +"\n        Slot 4: "+Input.Keys.toString(GAME.controls.getItem4())
                +"\n        Slot 5: "+Input.Keys.toString(GAME.controls.getItem5()));
        controls.setFontScale(0.8f);
        controls.setSize(250, 350);
        controls.setPosition(GAME.stageInstance.getWidth()/2-100, GAME.stageInstance.getHeight()/2-175);
        //option to exit controls view and go back to pause menu options
        exitControls = new TextButton("Back", GAME.generalTextButtonStyle);
        exitControls.setSize(150, 50);
        exitControls.setPosition(controls.getX()+50, controls.getY()-150);
        exitControls.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                controls.remove();
                exitControls.remove();
                GAME.stageInstance.addActor(viewControls);
                GAME.stageInstance.addActor(chooseQuit);
                GAME.stageInstance.addActor(exitPause);
            }
        });

        viewControls.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                viewControls.remove();
                chooseQuit.remove();
                GAME.stageInstance.addActor(controls);
                GAME.stageInstance.addActor(exitControls);
            }
        });
        escapePauseOver = new InputListener() {
            public boolean keyDown(InputEvent event, int keycode)
            {
                if(keycode == game.controls.getOpenPauseMenu())
                {
                    if(MainMenu.isTutorial){
                        tutorialMessage.remove();
                        next.remove();
                    }
                    GAME.stageInstance.setKeyboardFocus(null);
                    GAME.stageInstance.addActor(viewControls);
                    GAME.stageInstance.addActor(chooseQuit);
                    GAME.stageInstance.addActor(exitPause);
                }
                return true;
            }
        };
        GAME.player.playerIcon.addListener(escapePauseOver);

        warning = new Label("Are you sure \nyou want to Quit?", GAME.generalLabelStyle);
        warning.setSize(300f, 200f);
        warning.setAlignment(1,1);
        warning.setAlignment(1);

        options = new TextButton[] {
          new TextButton("Quit", GAME.generalTextButtonStyle),
          new TextButton("Cancel", GAME.generalTextButtonStyle)
        };


        options[0].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GAME.stageInstance.clear();
                pauseMenu.clear();
                pauseMenu.remove();
                dispose();
                GAME.setScreen(new MainMenu(GAME));

            }
        });

        options[1].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);
                pauseMenu.clear();
                pauseMenu.remove();
                if(MainMenu.isTutorial && BladeAndTomes.enterDungeon==false){
                    GAME.stageInstance.addActor(tutorialMessage);
                    if(tutorialStep<=8)
                        GAME.stageInstance.addActor(next);
                }
            }
        });

        pauseMenu.setZIndex(1);
        options[0].setZIndex(1);
        options[1].setZIndex(1);


        //Reference page that referred to how to set up Keyboard Focus by the libGDX developers
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/Stage.html#setKeyboardFocus-com.badlogic.gdx.scenes.scene2d.Actor-
        GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);

        //Adds the player's icon to the stage.
        GAME.stageInstance.addActor(GAME.player.playerIcon);

        NPC_Cords = new Point();
        NPC_Cords.setLocation(GAME.stageInstance.getWidth() / 8, GAME.stageInstance.getHeight() / 2);
        Portal_Cords = new Point();
        Portal_Cords.setLocation(GAME.stageInstance.getWidth() / 2, GAME.stageInstance.getHeight() / 8);
        // For overlays
        game.overlays =new OverlayManager(game);
        game.overlays.setOverLayesVisibility(true);

        //Tutorial
        tutorialStep = 1;
        tutorialMessage = new Label("Welcome to Blade and Tomes!\n\nThis tutorial will help you\nunderstand" +
                "how to play\nthe game.\n\nClick Next to continue.", GAME.generalLabelStyle);
        tutorialMessage.setPosition(GAME.stageInstance.getWidth()/2-200, GAME.stageInstance.getHeight()/2);
        tutorialMessage.setSize(300f, 200f);
        tutorialMessage.setAlignment(1,1);
        next = new TextButton("Next", GAME.generalTextButtonStyle);
        next.setSize(100f, 50f);
        next.setPosition(tutorialMessage.getX()+100, tutorialMessage.getY()-50);
        next.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tutorialStep++;
                nextTutorial();
            }
        });
        if(MainMenu.isTutorial){
            GAME.player.setHealthPoints(10);
            if(BladeAndTomes.exitDungeon){
                tutorialStep = 9;
                nextTutorial();
            }
            else
                nextTutorial();

        }

        entitiesHandler = new EntitiesHandler(game);
    }

    @Override
    public void render(float delta) {
        GAME.player.isTurn = true;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView((OrthographicCamera) GAME.stageInstance.getCamera());
        renderer.render();
        // Renders All UI elements
        entitiesHandler.render(delta);

        // Set the pixel lengths & heights for each texture. This allows for proper scaling of our project

        //Simplifying render thanks to libGDX for their "Extending the Simple Game" Tutorial,
        //Specifically the advanced section on super.render() as well as the following section on the main
        //game screen
        //https://libgdx.com/dev/simple-game-extended/
//        npcTraderMsg = new Label("Wanna Trade\n Press \"T\" ", GAME.BaseLabelStyle2);

        GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
        GAME.stageInstance.draw();
        //isCollisionHandled(GAME.player, GAME.stageInstance);
        //isTileCollisionHandled(GAME.player, collisionLayer);
        GAME.overlays.updateHealth();


        //Tutorial check
        if(MainMenu.isTutorial && BladeAndTomes.exitDungeon){
            tutorialStep = 10;
            nextTutorial();
        }

        // Updates Elements for QuestBord
        if(isQuestBoardTradeVisible){
            questBoardTrade.render();
        }
        GAME.overlays.render();
        GAME.loadSaveManager.savePlayer(GAME.player,GAME.currentSaveIndex);
    }
    @Override
    public void resize(int width, int height) {
   }
    @Override
    public void show() {
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load("Maps/Overworld_Revamped_Two.tmx", TiledMap.class);
        manager.finishLoading();
        overWorldMap = manager.get("Maps/Overworld_Revamped_Two.tmx");
        renderer = new OrthogonalTiledMapRenderer(overWorldMap);
        Gdx.input.setInputProcessor(GAME.stageInstance);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        GAME.player.playerIcon.removeListener(escapePauseOver);
        overWorldMap.dispose();
        renderer.dispose();
        manager.dispose();
    }

    // Very helpful guide on setting up tile collisions from following source
    // https://lyze.dev/2021/03/25/libGDX-Tiled-Box2D-example-tiles/

    public void nextTutorial() {
        switch (tutorialStep) {
            case 1:
                GAME.stageInstance.addActor(tutorialMessage);
                tutorialMessage.setZIndex(0);
                GAME.stageInstance.addActor(next);
                next.setZIndex(0);
                break;
            case 2: //Inventory screen
                tutorialMessage.setText("Click 'E' to access your Inventory.\n\n Here you can view your items\n and equip armour or potions by\n" +
                        "dragging them into the\nbottom slots.\n\nDrag an item from the 5 slots\nin the upper left hand corner\ninto a slot in this Inventory.");
                tutorialMessage.setSize(300f, 300f);
                tutorialMessage.setPosition(GAME.stageInstance.getWidth() / 4 - 50, GAME.stageInstance.getHeight() / 2);
                next.setPosition(tutorialMessage.getX() + 100, tutorialMessage.getY() - 50);
                break;
            case 3: //Equipped Quests
                tutorialMessage.setText("Click on the 'Quest' tab in\nthe Inventory.\n\nHere you can view quests\nthat you equipped." +
                        "\nBuying quests will be\nexplained soon.");
                tutorialMessage.setSize(300f, 200f);
                break;
            case 4: //Upgrading skills
                tutorialMessage.setText("Click on the 'Skill' tab.\n\nHere you can spend tokens to \nupgrade your primary and\nsecondary skills. You can earn\ntokens from being successful\nin events in the dungeon.\n\nClick 'E' to exit the Inventory.");
                break;
            case 5: //Quests Buying and Selling
                tutorialMessage.setText("Walk up to the board in the\nmiddle of town and click 'T'.\n\nHere you can spend gold\non quests. Each quest has a\ndifficulty and reward shown" +
                        "\n\nClick 'T' to exit quests.");
                GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);
                //tutorialMessage.setPosition(GAME.stageInstance.getWidth()/2, GAME.stageInstance.getHeight()/2+300);
                tutorialMessage.setPosition(GAME.stageInstance.getWidth() / 2 - 300, GAME.stageInstance.getHeight()-250);
                next.setPosition(tutorialMessage.getX() + 100, tutorialMessage.getY() - 50);
                break;
            case 6: //Sell items
                tutorialMessage.setSize(300f,250f);
                tutorialMessage.setText("Walk to the top left building\n and click 'T' to sell items.\n\nIf you own one of the items\nthey want to buy, drag " +
                        "it into\nthe slot and click sell.\nMake sure to check that the\nlevel of the items are\nthe same" +
                        "\n\nClick 'T' to exit menu.");
                break;
            case 7: //Buy items
                tutorialMessage.setSize(300f,200f);
                tutorialMessage.setText("Walk to the building near\nthe bottom of town and\nclick 'T' to open the menu.\nClick the pay button to\nbuy the item." +
                        "\n\nClick 'T' to exit menu.");
                break;
            case 8: //item slot selection
                next.setPosition(tutorialMessage.getX() + 100, tutorialMessage.getY() - 50);
                tutorialMessage.setText("The item slots in the top left can\n be selected using 1-5 for the \nrespective slot position");
                break;
            case 9: //enter dungeon
                tutorialMessage.setText("Now it's time to fight!\n\nWalk into the portal at the \nbottom of town to enter\nthe dungeon.");
                tutorialMessage.setSize(300f, 200f);
                next.remove();
                break;
            case 10: //final explanation
                GAME.stageInstance.addActor(tutorialMessage);
                GAME.stageInstance.addActor(next);
                next.setText("Exit");
                tutorialMessage.setText("Now you know the basics\nof Blade and Tomes!\n\nAll controls can be changed\n in the settings on the main menu.");
                break;
            case 11:
                GAME.stageInstance.clear();
                dispose();
                GAME.setScreen(new MainMenu(GAME));
                break;
        }
    }
}
