package com.badlogic.game.screens;

import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCBuyer;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCSeller;
import ScreenOverlayRework.Inventory.NPCInventoryUI.TownHallQuestBoard;
import ScreenOverlayRework.Inventory.TreasureChest.TreasureChestUI;
import ScreenOverlayRework.OverlayManager;
import Sounds.playerMoveSound;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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
import com.badlogic.gdx.utils.Scaling;

import java.awt.*;

public class Overworld extends ScreenAdapter {

    final BladeAndTomes GAME;
    final float MOVE_DISTANCE;
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
        tileMeasurement = ((TiledMapTileLayer) overWorldMap.getLayers().get(1)).getTileWidth();

        //columns = collisionLayer.getTileWidth();
        //rows = collisionLayer.getHeight();

        //System.out.println(columns + rows);

        //System.out.println(overWorldMap.getTileSets().getTileSet(1).getTile(2).getOffsetX());

        renderer = new OrthogonalTiledMapRenderer(overWorldMap);

        world = new World(new Vector2(0, 0),true);
        worldRender = new Box2DDebugRenderer();

        parseCollision();

        MOVE_DISTANCE = 32f;
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


    }

    @Override
    public void render(float delta) {


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(camera);
        renderer.render();
        renderer.getBatch().begin();
        //renderer.renderTileLayer(backgroundLayer);
        renderer.renderTileLayer(collisionLayer);
        renderer.getBatch().end();
        worldRender.render(world, camera.combined);

        //getTileCells(collisionLayer);

        GAME.playerMovement();

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
        if(GAME.player.playerIcon.equals(overWorldMap.getTileSets().getTileSet("130"))) {
            GAME.stageInstance.removeListener(escapePauseOver);
            GAME.stageInstance.clear();
            dispose();
            GAME.setScreen(new Dungeon(GAME));
        }
        if((int)(GAME.player.moveSquare.getX()-Portal_Cords.getLocation().x)/100 == 0 &&(int)(GAME.player.moveSquare.getY()-Portal_Cords.getLocation().y)/100 == 0){
            GAME.stageInstance.removeListener(escapePauseOver);
            GAME.stageInstance.clear();
            dispose();
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
        //isTileCollisionHandled(GAME.player, GAME.player.moveSquare);
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

    }

    @Override
    public void resize(int width, int height) {
        Vector2 size = Scaling.fill.apply(1920, 1080, width, height);
        int viewportWidth = (int) size.x;
        int viewportHeight = (int) size.y;
        Gdx.gl.glViewport(0, 0, viewportWidth, viewportHeight);
        GAME.stageInstance.getViewport().update(viewportWidth, viewportHeight, true);
   }

    @Override
    public void show() {
        //overWorldMap = new TmxMapLoader().load("Maps/Overworld_Revamped_Two.tmx");

        //renderer = new OrthogonalTiledMapRenderer(overWorldMap);

        camera = (OrthographicCamera) GAME.stageInstance.getCamera();
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
        if (player.playerIcon.getX() <= 2 * MOVE_DISTANCE) {

            player.playerIcon.setPosition(player.playerIcon.getX() + MOVE_DISTANCE, player.playerIcon.getY());
            player.moveSquare.setPosition(player.moveSquare.getX() + MOVE_DISTANCE, player.moveSquare.getY());
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);

        } else if (player.playerIcon.getY() <= 2 * MOVE_DISTANCE) {

            player.playerIcon.setPosition(player.playerIcon.getX(), player.playerIcon.getY() + MOVE_DISTANCE);
            player.moveSquare.setPosition(player.moveSquare.getX(), player.moveSquare.getY() + MOVE_DISTANCE);
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);

        } else if (player.playerIcon.getX() >= stage.getWidth() - 2 * MOVE_DISTANCE) {

            player.playerIcon.setPosition(player.playerIcon.getX() - MOVE_DISTANCE, player.playerIcon.getY());
            player.moveSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY());
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);

        } else if (player.playerIcon.getY() >= stage.getHeight() - 2 * MOVE_DISTANCE) {

            player.playerIcon.setPosition(player.playerIcon.getX(), player.playerIcon.getY() - MOVE_DISTANCE);
            player.moveSquare.setPosition(player.moveSquare.getX(), player.moveSquare.getY() - MOVE_DISTANCE);
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);
        }
        return true;
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
                        body.createFixture(polygonShape, 1.0f);
                        polygonShape.dispose();

                        //System.out.println(body.getPosition());
                        //System.out.println(GAME.player.playerIcon.getX());

                        //isTileCollisionHandled(GAME.player, rectangle);
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

    private BodyDef playerBod(float i, float j) {
        BodyDef playerBodyDef = new BodyDef();
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(i, j);
        return playerBodyDef;
    }
}
