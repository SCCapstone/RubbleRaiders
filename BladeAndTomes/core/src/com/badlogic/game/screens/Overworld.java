package com.badlogic.game.screens;

import Keyboard_Mouse_Controls.SaveLoadGame;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCBuyer;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCSeller;
import ScreenOverlayRework.Inventory.NPCInventoryUI.TownHallQuestBoard;
import ScreenOverlayRework.Inventory.TreasureChest.TreasureChestUI;
import ScreenOverlayRework.OverlayManager;
import Sounds.playerMoveSound;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.creatures.Goblin;
import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.maps.*;
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
import jdk.tools.jmod.Main;
import com.badlogic.gdx.utils.Scaling;

import java.awt.*;

public class Overworld extends ScreenAdapter {

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;
    SpriteBatch batch;

    private World world;
    private TiledMap overWorldMap;
    private final Box2DDebugRenderer worldRender;

    private AssetManager manager;
    private OrthogonalTiledMapRenderer renderer;

    private String blocked;
    private String tilePortal;

    private TiledMapTileLayer collisionLayer;
    private TiledMapTileLayer backgroundLayer;
    int objectLayerId;
    int tileMeasurement;
    boolean collidedX, collidedY;

    int columns;
    int rows;

    private OrthographicCamera camera;

    Window pauseMenu;
    Label warning;
    TextButton options[];
    Table quitTable;
    InputListener escapePauseOver;

    playerMoveSound playerMovenSound;

    Point NPC_Cords;
    Point Portal_Cords;
    boolean doTrade;
    Label npcTraderMsg;

    Window saveQuit;
    TextButton saveBack;
    TextButton game1;
    TextButton game2;
    TextButton game3;
    TextButton game4;
    Table savedGames;

    Label tutorialMessage;
    TextButton next;
    int tutorialStep;

    NPCSeller npcSeller;
    boolean isNpcSellerVisible;

    NPCBuyer npcBuyer;
    boolean isNpcBuyerVisible;

    TownHallQuestBoard questBoardTrade;
    boolean isQuestBoardTradeVisible;

    TreasureChestUI chest;
    int counter;
    // Helpful Collision Detection Tutorials (NOT IMPLEMENTED IN CODE YET)
    // TODO: IMPLEMENT THESE IN CODE
    //https://stackoverflow.com/questions/61491889/how-to-detect-collisions-between-objects-in-libgdx
    //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/math/Intersector.html

    public Overworld (final BladeAndTomes game) {
        game.player = game.loadSaveManager.loadPlayer(game.currentSaveIndex);

        counter = 0;

        this.GAME = game;
        GAME.resetElapsedTime();
        objectLayerId = 2;

        manager = new AssetManager();
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load("Maps/Overworld_Revamped_Two.tmx", TiledMap.class);
        manager.finishLoading();
        overWorldMap = manager.get("Maps/Overworld_Revamped_Two.tmx");

        //overWorldMap = new TmxMapLoader().load("Maps/Overworld_Revamped_Two.tmx");

        MapLayers mapLayers = overWorldMap.getLayers();
        collisionLayer = (TiledMapTileLayer) mapLayers.get("Buildings");
        backgroundLayer = (TiledMapTileLayer) mapLayers.get("Background");
        //tileMeasurement = collisionLayer.getTileWidth();
        //tileMeasurement = ((TiledMapTileLayer) overWorldMap.getLayers().get(1)).getTileWidth();

        MapProperties mapProperties = overWorldMap.getProperties();
        tileMeasurement = mapProperties.get("tilewidth", Integer.class);
        //System.out.println(tileMeasurement);

        //columns = collisionLayer.getTileWidth();
        //rows = collisionLayer.getHeight();

        //System.out.println(columns + rows);

        //System.out.println(overWorldMap.getTileSets().getTileSet(1).getTile(2).getOffsetX());

        renderer = new OrthogonalTiledMapRenderer(overWorldMap);
        world = new World(new Vector2(-1f, -1f),true);
        //world.setContactFilter();

        // Following below sets up our player box. Its density, and friction when hitting other boxes.
        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(GAME.vec.x, GAME.vec.y);

        Body playerBody = world.createBody(playerBodyDef);

        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            if(GAME.vec.y+GAME.speed>=0&&(GAME.vec.y+GAME.speed)< GAME.stageInstance.getHeight())
                GAME.vec.add(0,GAME.speed);
                //playerBody.applyLinearImpulse(GAME.speed,GAME.vec.x, GAME.vec.y, false);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            if(GAME.vec.y-GAME.speed>=0&&(GAME.vec.y-GAME.speed)< GAME.stageInstance.getHeight())
                GAME.vec.add(0,-GAME.speed);
                playerBodyDef.linearVelocity.set(0, -GAME.speed);
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            if(GAME.vec.x-GAME.speed>=0&&(GAME.vec.x-GAME.speed)< GAME.stageInstance.getWidth())
                GAME.vec.add(-GAME.speed,0);
                playerBodyDef.linearVelocity.set(-GAME.speed,0);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            if(GAME.vec.x+GAME.speed>=0&&(GAME.vec.x+GAME.speed)< GAME.stageInstance.getWidth())
                GAME.vec.add(GAME.speed,0);
                playerBodyDef.linearVelocity.set(GAME.speed, 0);
        }
        PolygonShape playerShape = new PolygonShape();
        playerShape.setAsBox(GAME.player.playerIcon.getX() / 10, GAME.player.playerIcon.getY() / 10);
        System.out.println(GAME.player.playerIcon.getX());
        System.out.println(GAME.vec.x);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = playerShape;
        fixtureDef.restitution = 0.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.density = 0.0f;

        playerBody.createFixture(fixtureDef);
        playerShape.dispose();
        //playerBod(GAME.vec.x, GAME.vec.y);
        worldRender = new Box2DDebugRenderer();

        parseCollision();
//        worldRender.VELOCITY_COLOR.b=

        MOVE_DISTANCE = 64;
        doTrade = false;
        batch = new SpriteBatch();

        collidedX = false;
        collidedY = false;

        pauseMenu = new Window("", GAME.generalWindowStyle);
        pauseMenu.setHeight(500);
        pauseMenu.setWidth(700);
        pauseMenu.setPosition(GAME.stageInstance.getWidth()/3, GAME.stageInstance.getHeight()/3);
        pauseMenu.setMovable(true);
        pauseMenu.setKeepWithinStage(true);

        saveQuit = new Window("SaveQuit", GAME.generalWindowStyle);
        saveQuit.setSize(GAME.stageInstance.getWidth()/3,GAME.stageInstance.getHeight());
        saveQuit.setPosition(GAME.stageInstance.getWidth()*0.35f, GAME.stageInstance.getHeight()*0.35f);

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

        GAME.player.playerIcon.addListener(escapePauseOver);

        warning = new Label("Are you sure you want to Quit?", GAME.generalLabelStyle);
        warning.setSize(300f, 200f);
        warning.setAlignment(1,1);

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
        npcSeller = game.overlays.generateNewNPSeller();
        isNpcSellerVisible =false;
        npcBuyer = game.overlays.generateNewNPCBuyer();
        isNpcBuyerVisible = false;
        questBoardTrade= game.overlays.generateQuestBoard();
        isQuestBoardTradeVisible = false;
        chest = game.overlays.generateChest();

        //Tutorial
        tutorialStep = 1;
        tutorialMessage = new Label("Welcome to Blade and Tomes!\n\nThis tutorial will help you understand" +
                "\nhow to play the game.\n\nClick Next to continue.", GAME.generalLabelStyle);
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
            GAME.player.setHealthPoints(10);
            if(BladeAndTomes.exitDungeon){
                tutorialStep = 8;
                nextTutorial();
            }
            else {
                nextTutorial();
                GAME.stageInstance.setKeyboardFocus(next);
            }
        }


    }

    public void nextTutorial() {
        switch (tutorialStep) {
            case 1:
                GAME.stageInstance.addActor(tutorialMessage);
                GAME.stageInstance.addActor(next);
                break;
            case 2: //Inventory screen
                tutorialMessage.setText("Click 'E' to access your Inventory.\n\n Here you can view your items\n and equip armour or potions by\n" +
                        "dragging them into the bottom slots.\n\nDrag an item from the 5 slots\nin the upper left hand corner into\na slot in this Inventory.");
                tutorialMessage.setSize(300f, 300f);
                tutorialMessage.setPosition(GAME.stageInstance.getWidth() / 4 - 50, GAME.stageInstance.getHeight() / 2);
                next.setPosition(tutorialMessage.getX() + 100, tutorialMessage.getY() - 50);
                break;
            case 3: //Equipped Quests
                tutorialMessage.setText("Click on the 'Quests' tab in\nthe Inventory.\n\nHere you can view quests that you\nequipped." +
                        " Buying quests will \nbe explained soon.");
                tutorialMessage.setSize(300f, 200f);
                break;
            case 4: //Upgrading skills
                tutorialMessage.setText("Click on the 'Skills' tab.\n\nHere you can spend tokens to \nupgrade your primary and\nsecondary skills.\n\nGive it a try then click\n 'E' to exit the Inventory.");
                break;
            case 5: //Quests Buying and Selling
                tutorialMessage.setText("Click 'Q' to buy and\nsell quests.\n\nHere you can spend gold on quests.\nEach quest has a difficulty \nand reward shown" +
                        "\n\nClick 'Q' to exit quests.");
                tutorialMessage.setPosition(80, GAME.stageInstance.getHeight()/2);
                next.setPosition(tutorialMessage.getX() + 100, tutorialMessage.getY() - 50);
                break;
            case 6: //Buy items
                tutorialMessage.setText("Click 'T' to buy items\n\nClick the pay button to buy the item.\n\nClick 'T' to exit menu.");
                break;
            case 7: //enter dungeon
                tutorialMessage.setText("Now it's time to fight!\n\nUsing the arrow keys, walk into\nthe portal at the bottom of town\n to enter the dungeon.");
                GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);
                tutorialMessage.setPosition(GAME.stageInstance.getWidth() / 2 - 100, (GAME.stageInstance.getHeight() / 3) * 2);
                tutorialMessage.setSize(300f, 200f);
                next.remove();
                break;
            case 8: //final explanation
                GAME.stageInstance.addActor(tutorialMessage);
                GAME.stageInstance.addActor(next);
                next.setText("Exit");
                tutorialMessage.setText("Now you know the basics\nof Blade and Tomes!\n\nAll controls can be changed\n in the settings on the main menu.");
                break;
            case 9:
                GAME.stageInstance.clear();
                dispose();
                GAME.setScreen(new MainMenu(GAME));
                break;
        }
    }

    @Override
    public void render(float delta) {


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //renderer.setView((OrthographicCamera) GAME.stageInstance.getCamera());
        world.step(1/60f, 6, 2);
        renderer.setView(camera);
        renderer.render();
        renderer.getBatch().begin();
        //renderer.renderTileLayer(backgroundLayer);
        renderer.renderTileLayer(collisionLayer);
        renderer.getBatch().end();
        worldRender.render(world, camera.combined);

        //getTileCells(collisionLayer);

        GAME.playerMovement();
        GAME.vectorPlayerMovement();

        // Set the pixel lengths & heights for each texture. This allows for proper scaling of our project

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
        GAME.batch.end();
        //how player enters dungeon through the portal
        //I followed Anirudh Oruganti's method for the NPC interation in the overworld
        if((int)(GAME.player.moveSquare.getX()-Portal_Cords.getLocation().x)/100 == 0 &&(int)(GAME.player.moveSquare.getY()-Portal_Cords.getLocation().y)/100 == 0){
            GAME.stageInstance.removeListener(escapePauseOver);
            GAME.stageInstance.clear();
            dispose();
            BladeAndTomes.enterDungeon = true;
            BladeAndTomes.exitDungeon = false;
            GAME.setScreen(new Dungeon(GAME));
        }
        if((int)(GAME.vec.x-Portal_Cords.getLocation().x)/100 == 0 &&(int)(GAME.vec.y-Portal_Cords.getLocation().y)/100 == 0){
            GAME.stageInstance.removeListener(escapePauseOver);
            GAME.stageInstance.clear();
            dispose();
            BladeAndTomes.enterDungeon = true;
            BladeAndTomes.exitDungeon = false;
            GAME.setScreen(new Dungeon(GAME));
        }

        //Simplifying render thanks to libGDX for their "Extending the Simple Game" Tutorial,
        //Specifically the advanced section on super.render() as well as the following section on the main
        //game screen
        //https://libgdx.com/dev/simple-game-extended/
        npcTraderMsg = new Label("Wanna Trade\n Press \"T\" ", GAME.BaseLabelStyle2);

        GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
        GAME.stageInstance.draw();
        isCollisionHandled(GAME.player, GAME.stageInstance);
//        isTileCollisionHandled(GAME.player, collisionLayer);
        GAME.overlays.updateHealth();

        // Displays Hidden Inventory Table

        // COMMENT THIS CODE TO GET TRADING WORKING
        if(Gdx.input.isKeyJustPressed(Input.Keys.E) && !isNpcSellerVisible && !isNpcBuyerVisible&&!isQuestBoardTradeVisible)
            GAME.overlays.setHiddenTableVisibility(!GAME.showHiddenInventory);
        if(Gdx.input.isKeyJustPressed(GAME.controls.getTradeMenu())&& !isNpcBuyerVisible){
            isNpcSellerVisible =!isNpcSellerVisible;
            isQuestBoardTradeVisible =false;
            GAME.overlays.setQuestBoardTradeVisibility(false,questBoardTrade);
            GAME.overlays.NPCBuyerInventory(false,npcBuyer);
            GAME.overlays.setHiddenTableVisibility(false);
            GAME.overlays.setQuestBoardTradeVisibility(false,questBoardTrade);
            GAME.overlays.NPCSellerInventory(isNpcSellerVisible,npcSeller);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.B)&& !isNpcSellerVisible){
            isNpcBuyerVisible =!isNpcBuyerVisible;
            isQuestBoardTradeVisible =false;
            GAME.overlays.NPCSellerInventory(false,npcSeller);
            GAME.overlays.setQuestBoardTradeVisibility(false,questBoardTrade);
            GAME.overlays.setHiddenTableVisibility(false);
            GAME.overlays.NPCBuyerInventory(isNpcBuyerVisible,npcBuyer);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.Q)&& !isNpcSellerVisible){
            isQuestBoardTradeVisible = !isQuestBoardTradeVisible;
            GAME.overlays.NPCSellerInventory(false,npcSeller);
            GAME.overlays.setQuestBoardTradeVisibility(isQuestBoardTradeVisible,questBoardTrade);
            GAME.overlays.setHiddenTableVisibility(false);
            GAME.overlays.NPCBuyerInventory(false,npcBuyer);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.C)&& !isNpcSellerVisible){
            isQuestBoardTradeVisible =     //Save all player data including name, stats, inventory
            !isQuestBoardTradeVisible;
            GAME.overlays.NPCSellerInventory(false,npcSeller);
            GAME.overlays.setQuestBoardTradeVisibility(false,questBoardTrade);
            GAME.overlays.setHiddenTableVisibility(false);
            GAME.overlays.NPCBuyerInventory(false,npcBuyer);
            chest.setTreasureChestVisible(!chest.isTreasureChestVisible());
            GAME.overlays.displayChest(chest);
        }

        // Updates Elements for QuestBord
        if(isQuestBoardTradeVisible){
            questBoardTrade.render();
        }

        GAME.overlays.render();
//        System.out.println(GAME.player.inventoryItems.get(GAME.currentInventorySelection).getDamage());

        GAME.loadSaveManager.savePlayer(GAME.player,GAME.currentSaveIndex);
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            if(GAME.vec.y+GAME.speed>=0&&(GAME.vec.y+GAME.speed)< GAME.stageInstance.getHeight())
                GAME.vec.add(0,GAME.speed);
            //playerBodyDef.linearVelocity.set(0, GAME.speed);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            if(GAME.vec.y-GAME.speed>=0&&(GAME.vec.y-GAME.speed)< GAME.stageInstance.getHeight())
                GAME.vec.add(0,-GAME.speed);
            //playerBodyDef.linearVelocity.set(0, -GAME.speed);
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            if(GAME.vec.x-GAME.speed>=0&&(GAME.vec.x-GAME.speed)< GAME.stageInstance.getWidth())
                GAME.vec.add(-GAME.speed,0);
            //playerBodyDef.linearVelocity.set(-GAME.speed,0);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            if(GAME.vec.x+GAME.speed>=0&&(GAME.vec.x+GAME.speed)< GAME.stageInstance.getWidth())
                GAME.vec.add(GAME.speed,0);
            //playerBodyDef.linearVelocity.set(GAME.speed, 0);
        }
        /*
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            if(GAME.vec.y+GAME.speed>=0&&(GAME.vec.y+GAME.speed)<GAME.stageInstance.getHeight())
            GAME.vec.add(0,GAME.speed);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
            if(GAME.vec.y-GAME.speed>=0&&(GAME.vec.y-GAME.speed)<GAME.stageInstance.getHeight())
                GAME.vec.add(0,-GAME.speed);
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)){
            if(GAME.vec.x-GAME.speed>=0&&(GAME.vec.x-GAME.speed)<GAME.stageInstance.getWidth())
                GAME.vec.add(-GAME.speed,0);
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
            if(GAME.vec.x+GAME.speed>=0&&(GAME.vec.x+GAME.speed)<GAME.stageInstance.getWidth())
                GAME.vec.add(GAME.speed,0);
        }
        System.out.println(GAME.vec.x+"\t\t"+GAME.vec.y+"\t\t\t"+ GAME.stageInstance.getViewport().getScreenWidth());

         */
    }

        //GAME.loadSaveManager.savePlayer(GAME.player,GAME.currentSaveIndex);



int width,height;

    @Override
    public void resize(int width, int height) {
        //this.width = width;
        //this.height = height;
        //GAME.stageInstance.getViewport().update(width, height);
        camera.viewportHeight = height;
        camera.viewportWidth = width;
        camera.translate(GAME.stageInstance.getWidth() / 2, GAME.stageInstance.getHeight() / 2);
        camera.update();
        GAME.stageInstance.getViewport().update(width, height, true);
   }

    @Override
    public void show() {
        //overWorldMap = new TmxMapLoader().load("Maps/Overworld_Revamped_Two.tmx");
        //parseCollision();

        renderer = new OrthogonalTiledMapRenderer(overWorldMap);
        camera = new OrthographicCamera();
        Gdx.input.setInputProcessor(GAME.stageInstance);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        world.dispose();
        overWorldMap.dispose();
        renderer.dispose();
        manager.dispose();
    }

    //sets boundaries in the overworld
    //based off of Aidan Emmons boundary method for dungeon
    public boolean isCollisionHandled(Player player, Stage stage) {
//        if (player.playerIcon.getX() <= 2 * MOVE_DISTANCE) {
//
//            player.playerIcon.setPosition(player.playerIcon.getX() + MOVE_DISTANCE, player.playerIcon.getY());
//            player.moveSquare.setPosition(player.moveSquare.getX() + MOVE_DISTANCE, player.moveSquare.getY());
//            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);
//
//        } else if (player.playerIcon.getY() <= 2 * MOVE_DISTANCE) {
//
//            player.playerIcon.setPosition(player.playerIcon.getX(), player.playerIcon.getY() + MOVE_DISTANCE);
//            player.moveSquare.setPosition(player.moveSquare.getX(), player.moveSquare.getY() + MOVE_DISTANCE);
//            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);
//
//        } else if (player.playerIcon.getX() >= stage.getWidth() - 2 * MOVE_DISTANCE) {
//
//            player.playerIcon.setPosition(player.playerIcon.getX() - MOVE_DISTANCE, player.playerIcon.getY());
//            player.moveSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY());
//            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);
//
//        } else if (player.playerIcon.getY() >= stage.getHeight() - 2 * MOVE_DISTANCE) {
//
//            player.playerIcon.setPosition(player.playerIcon.getX(), player.playerIcon.getY() - MOVE_DISTANCE);
//            player.moveSquare.setPosition(player.moveSquare.getX(), player.moveSquare.getY() - MOVE_DISTANCE);
//            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);
//        }
        return false;
    }

    public boolean isTileCollisionHandled(Player player, Rectangle rectangle) {
        //getTileCells(collisionLayer);
        Rectangle playerRec = new Rectangle();
        playerRec.set(player.playerIcon.getX(), player.playerIcon.getY(),
                player.playerIcon.getImageWidth(), player.playerIcon.getImageHeight());
        if(playerRec.overlaps(rectangle)) {
            System.out.println("yup");
            return true;
        }
        return false;
    }

    public void getTileCells(TiledMapTileLayer tileLayer) {
        MapLayers mapLayer = overWorldMap.getLayers();
        tileLayer = (TiledMapTileLayer) mapLayer.get("Buildings");
    }

    // Very helpful guide on setting up tile collisions from following source
    // https://lyze.dev/2021/03/25/libGDX-Tiled-Box2D-example-tiles/
    public void parseCollision() {
        for(int i = 0; i < collisionLayer.getWidth(); i++) {
            for (int j = 0; j < collisionLayer.getHeight(); j++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(i, j);
                if (cell == null) {
                    //System.out.println(cell);
                    continue;
                } if (cell.getTile() == null) {
                    continue;
                }

                MapObjects cellObjects = cell.getTile().getObjects();
                if (cellObjects.getCount() != 1) {
                    //System.out.println(cellObjects);
                    continue;
                }

                MapObject mapObject = cellObjects.get(0);
                //System.out.println(mapObject);

                if (mapObject instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) mapObject;
                    Rectangle rectangle = rectangleObject.getRectangle();
                        BodyDef bodyDef = getBodyDef(i * tileMeasurement + tileMeasurement / 2f + rectangle.getX()
                                        - (tileMeasurement - rectangle.getWidth()) / 2f,
                                j * tileMeasurement + tileMeasurement / 2f + rectangle.getY()
                                        - (tileMeasurement - rectangle.getHeight()) / 2f);

                        Body body = world.createBody(bodyDef);
                        PolygonShape polygonShape = new PolygonShape();
                        polygonShape.setAsBox(rectangle.getWidth() / 2f, rectangle.getHeight() / 2f);
                        body.createFixture(polygonShape, 0f).setRestitution(0.0f);
                        polygonShape.dispose();
                }
            }
        }
    }

    private BodyDef getBodyDef(float i, float j) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(i, j);
        return bodyDef;
    }

    /*
    private BodyDef playerBod(float i, float j) {
        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(i, j);

        Body playerBody = world.createBody(playerBodyDef);
        PolygonShape playerShape = new PolygonShape();
        playerShape.setAsBox(i, j);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = playerShape;
        fixtureDef.density = 1.0f;

        Fixture fixture = playerBody.createFixture(fixtureDef);
        playerShape.dispose();

        return playerBodyDef;
    }

     */
}
