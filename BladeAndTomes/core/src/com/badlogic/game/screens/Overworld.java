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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

    TiledMapTileLayer collisionLayer;
    int objectLayerId;
    int tileMeasurement;
    boolean collidedX, collidedY;

    private OrthographicCamera camera;


    Texture background;
    Texture chapel;
    Texture barracks;
    Texture questBoard;
    Texture portal;
    Texture NPCTrader;
    Texture marketStall;
    Texture tavern;

    /*
    // use all this for collision with buildings later
    private float tavernWidth = 128f;
    private float tavernHeight = 128f;
    private float marketStallHeight = 256f;
    private float marketStallWidth = 196f;
    private float barracksHeight = 384f;
    private float barracksWidth = 256f;
    private float chapelWidth = 196f;
    private float chapelHeight = 256f;
    private float questBoardHeight = 64f;
    private float questBoardWidth = 64f;
    private float portalHeight = 64f;
    private float portalWidth = 64f;

     */

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
    Rectangle townHall;
    float prePlayerLocX;
    float prePlayerLocY;
    boolean nearTownHall ;
    boolean nearBuyerTrader ;
    boolean nearSellerTrader ;

    Label townHallMSG;
    // Helpful Collision Detection Tutorials (NOT IMPLEMENTED IN CODE YET)
    // TODO: IMPLEMENT THESE IN CODE
    //https://stackoverflow.com/questions/61491889/how-to-detect-collisions-between-objects-in-libgdx
    //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/math/Intersector.html

    public Overworld (final BladeAndTomes game) {
        // Displays TownHall Interact msg
        townHallMSG = new Label("Press "+  game.controls.getTradeMenu() +" To See Quest Board", game.BaseLabelStyle2);
        nearTownHall = false;
        nearBuyerTrader = false;
        nearSellerTrader = false;

    //    Scaling.fit.apply(1920, 1200, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
//        resize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        game.player = game.loadSaveManager.loadPlayer(game.currentSaveIndex);
        game.player.playerIcon.setPosition(1920/2,1080/2);

        counter = 0;

        this.GAME = game;
        GAME.resetElapsedTime();
        objectLayerId = 2;

        overWorldMap = new TmxMapLoader().load("Maps/Overworld_Revamped_Two.tmx");
        collisionLayer = (TiledMapTileLayer) overWorldMap.getLayers().get(1);
        tileMeasurement = ((TiledMapTileLayer) overWorldMap.getLayers().get(1)).getTileWidth();


// there are several other types, Rectangle is probably the most common one


        renderer = new OrthogonalTiledMapRenderer(overWorldMap);
        world = new World(new Vector2(0, 0),true);
        worldRender = new Box2DDebugRenderer();

        parseCollision();
//        worldRender.VELOCITY_COLOR.b=

        MOVE_DISTANCE = 64;
        doTrade = false;
        batch = new SpriteBatch();

        collidedX = false;
        collidedY = false;
        /*
        // Assets were obtained from below source
        // https://merchant-shade.itch.io/16x16-mini-world-sprites
        background = new Texture(Gdx.files.internal("OverworldBackground.jpg"));
        chapel = new Texture(Gdx.files.internal("Chapel.jpg"));
        barracks = new Texture(Gdx.files.internal("Barracks.jpg"));
        marketStall = new Texture(Gdx.files.internal("MarketBuilding.jpg"));
        portal = new Texture(Gdx.files.internal("PortalToDungeon.jpg"));
        questBoard = new Texture(Gdx.files.internal("Quests_Board.jpg"));
        tavern = new Texture(Gdx.files.internal("Tavern.jpg"));
        NPCTrader = new Texture(Gdx.files.internal("NPC_Trader.png"));
         */

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

        townHall = new Rectangle(125,725,250,260);
//        TownHallTable.setBounds(125,725,260,260);
        TownHallTable.setDebug(true);
        GAME.stageInstance.addActor(TownHallTable);
        shapeRenderer = new ShapeRenderer();
        prePlayerLocX = GAME.player.playerIcon.getX();
        prePlayerLocY = GAME.player.playerIcon.getY();
        GAME.stageInstance.addActor(townHallMSG);
        townHallMSG.setPosition(townHall.getX()+ townHall.width-200,townHall.getY()+ townHall.height-200);
        townHallMSG.setVisible(false);
    }
    ShapeRenderer shapeRenderer;


    Table TownHallTable = new Table();

    @Override
    public void render(float delta) {

//        TownHallTable.setPosition(500,500);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapeRenderer.setAutoShapeType(true);

        if(GAME.player.moveSquare.overlaps(townHall))
            System.out.println("Overlaps");


        renderer.setView((OrthographicCamera) GAME.stageInstance.getCamera());
        renderer.render();
//        GAME.stageInstance.addActor(townHall.);

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
            BladeAndTomes.enterDungeon = true;
            BladeAndTomes.exitDungeon = false;
            GAME.setScreen(new Dungeon(GAME));
        }
        if((int)(GAME.player.moveSquare.getX()-Portal_Cords.getLocation().x)/100 == 0 &&(int)(GAME.player.moveSquare.getY()-Portal_Cords.getLocation().y)/100 == 0){
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

        // Updates Elements for QuestBord
        if(isQuestBoardTradeVisible){
            questBoardTrade.render();
        }
        GAME.overlays.render();
//        System.out.println(GAME.player.inventoryItems.get(GAME.currentInventorySelection).getDamage());
        GAME.loadSaveManager.savePlayer(GAME.player,GAME.currentSaveIndex);
        prePlayerLocX = GAME.player.playerIcon.getX();
        prePlayerLocY = GAME.player.playerIcon.getY();
        interactUI();
        townHallMSG.setVisible(nearTownHall);
        System.out.println(nearTownHall);

    }

    public void interactUI(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.E) ){
            GAME.overlays.setQuestBoardTradeVisibility(false,questBoardTrade);
            GAME.overlays.NPCBuyerInventory(false,npcBuyer);
            GAME.overlays.setQuestBoardTradeVisibility(false,questBoardTrade);
            GAME.overlays.setHiddenTableVisibility(!GAME.showHiddenInventory);
        }
        if(Gdx.input.isKeyJustPressed(GAME.controls.getTradeMenu())&&nearSellerTrader){
            isNpcSellerVisible =!isNpcSellerVisible;
            isQuestBoardTradeVisible =false;
            GAME.overlays.setQuestBoardTradeVisibility(false,questBoardTrade);
            GAME.overlays.NPCBuyerInventory(false,npcBuyer);
            GAME.overlays.setHiddenTableVisibility(false);
            GAME.overlays.setQuestBoardTradeVisibility(false,questBoardTrade);
            GAME.overlays.NPCSellerInventory(isNpcSellerVisible,npcSeller);
        } else if(!nearSellerTrader){
            isNpcSellerVisible =false;
            GAME.overlays.NPCSellerInventory(isNpcSellerVisible,npcSeller);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.B)&&nearBuyerTrader){
            isNpcBuyerVisible =!isNpcBuyerVisible;
            isQuestBoardTradeVisible =false;
            GAME.overlays.NPCSellerInventory(false,npcSeller);
            GAME.overlays.setQuestBoardTradeVisibility(false,questBoardTrade);
            GAME.overlays.setHiddenTableVisibility(false);
            GAME.overlays.NPCBuyerInventory(isNpcBuyerVisible,npcBuyer);
        } else if(!nearBuyerTrader){
            isNpcBuyerVisible = false;
            GAME.overlays.NPCBuyerInventory(isNpcBuyerVisible,npcBuyer);
        }
        if(Gdx.input.isKeyJustPressed(GAME.controls.getTradeMenu())&& nearTownHall){
            isQuestBoardTradeVisible = !isQuestBoardTradeVisible;
            GAME.overlays.setHiddenTableVisibility(false);
            GAME.overlays.NPCSellerInventory(false,npcSeller);
            GAME.overlays.NPCBuyerInventory(false,npcBuyer);
            GAME.overlays.setQuestBoardTradeVisibility(isQuestBoardTradeVisible,questBoardTrade);

        } else if(!nearTownHall){
            isQuestBoardTradeVisible = false;
            GAME.overlays.setQuestBoardTradeVisibility(isQuestBoardTradeVisible,questBoardTrade);
        }
//        if(Gdx.input.isKeyJustPressed(GAME.controls.getTradeMenu())&&near ){
//            isQuestBoardTradeVisible =     //Save all player data including name, stats, inventory
//                    !isQuestBoardTradeVisible;
//            GAME.overlays.setQuestBoardTradeVisibility(false,questBoardTrade);
//            GAME.overlays.setHiddenTableVisibility(false);
//            GAME.overlays.NPCBuyerInventory(false,npcBuyer);
//            chest.setTreasureChestVisible(!chest.isTreasureChestVisible());
//            GAME.overlays.displayChest(chest);
//        }
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


        //GAME.loadSaveManager.savePlayer(GAME.player,GAME.currentSaveIndex);





    @Override
    public void resize(int width, int height) {
   }

    @Override
    public void show() {
        overWorldMap = new TmxMapLoader().load("Maps/Overworld_Revamped_Two.tmx");
        //collisionLayer = (TiledMapTileLayer) overWorldMap.getLayers().get(objectLayerId);
        //overWorldMap.getLayers().get(objectLayerId).getProperties().get("blocked");

        renderer = new OrthogonalTiledMapRenderer(overWorldMap);
        camera = new OrthographicCamera();
        Gdx.input.setInputProcessor(GAME.stageInstance);
    }

    @Override
    public void hide() {
        //overWorldMap.dispose();
        //renderer.dispose();
        //manager.dispose();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        world.dispose();
        overWorldMap.dispose();
        renderer.dispose();
        //manager.dispose();
    }

    //sets boundaries in the overworld
    //based off of Aidan Emmons boundary method for dungeon
    public boolean isCollisionHandled(Player player, Stage stage) {
        if (player.playerIcon.getX() <=  MOVE_DISTANCE) {

            player.playerIcon.setPosition(player.playerIcon.getX() + MOVE_DISTANCE, player.playerIcon.getY());
            player.moveSquare.setPosition(player.moveSquare.getX() + MOVE_DISTANCE, player.moveSquare.getY());
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);

        } else if (player.playerIcon.getY() <=  MOVE_DISTANCE) {

            player.playerIcon.setPosition(player.playerIcon.getX(), player.playerIcon.getY() + MOVE_DISTANCE);
            player.moveSquare.setPosition(player.moveSquare.getX(), player.moveSquare.getY() + MOVE_DISTANCE);
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);

        } else if (player.playerIcon.getX() >=1920-MOVE_DISTANCE) {

            player.playerIcon.setPosition(player.playerIcon.getX() - MOVE_DISTANCE, player.playerIcon.getY());
            player.moveSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY());
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);

        } else if (player.playerIcon.getY() >= 1080-MOVE_DISTANCE) {

            player.playerIcon.setPosition(player.playerIcon.getX(), player.playerIcon.getY() - MOVE_DISTANCE);
            player.moveSquare.setPosition(player.moveSquare.getX(), player.moveSquare.getY() - MOVE_DISTANCE);
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);
        }
        TownHallCollision(player,stage);
        return true;
    }

    public void movePlayer(Player player, float MOVE_DISTANCE_X, float MOVE_DISTANCE_Y){
        player.playerIcon.setPosition(player.playerIcon.getX()+MOVE_DISTANCE_X, player.playerIcon.getY() + MOVE_DISTANCE_Y);
        player.moveSquare.setPosition(player.moveSquare.getX()+MOVE_DISTANCE_X, player.moveSquare.getY() + MOVE_DISTANCE_Y);
        player.interactSquare.setPosition(player.moveSquare.getX()+MOVE_DISTANCE_X , player.moveSquare.getY()  + MOVE_DISTANCE_Y);
    }

    public boolean BlockEPhi(Player player, float locX_1, float locX_2,
                                         float locY_1, float locY_2){
        float moveBackX = 0;
        float moveBackY = 0;
        if(Gdx.input.isKeyJustPressed(GAME.controls.getMoveUp())){
            moveBackY = - MOVE_DISTANCE;
        } else if (Gdx.input.isKeyJustPressed(GAME.controls.getMoveDown())){
            moveBackY =  MOVE_DISTANCE;
        } else if(Gdx.input.isKeyJustPressed(GAME.controls.getMoveRight())){
            moveBackX = - MOVE_DISTANCE;
        } else if(Gdx.input.isKeyJustPressed(GAME.controls.getMoveLeft())){
            moveBackX =  MOVE_DISTANCE;
        }

        if(checkBlock(player.playerIcon.getX(),locX_1 ,locX_2 )&&checkBlock(player.playerIcon.getY(),locY_1 ,locY_2 ))
            movePlayer(player,moveBackX,moveBackY);
        if(checkBlock(player.playerIcon.getX(),locX_1 ,locX_2 )&&checkBlock(player.playerIcon.getY(),locY_1 ,locY_2 ))
            player.playerIcon.setPosition(prePlayerLocX , prePlayerLocY);
        System.out.println(player.playerIcon.getY()+MOVE_DISTANCE+"\t"+locY_1);
        return checkBlock(player.playerIcon.getY()+MOVE_DISTANCE,locY_1 ,locY_2 )&&checkBlock(player.playerIcon.getX(),locX_1 ,locX_2 );

    }
    public void TownHallCollision(Player player,Stage stage){
      nearTownHall =  BlockEPhi(player, townHall.getX(), townHall.getX()+townHall.width,townHall.getY(),townHall.getY()+townHall.height);
    }

    public boolean checkBlock(float plyLoc, float buildingLoc0, float buildingLoc1){
        return plyLoc>(buildingLoc0) && plyLoc<(buildingLoc1);
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

                    if (mapObject instanceof RectangleMapObject) {
                        System.out.println(rectangle.x);
//                        rectangle.x
                        BodyDef bodyDef = getBodyDef(i * tileMeasurement + tileMeasurement / 2f + rectangle.getX()
                                        - (tileMeasurement - rectangle.getWidth()) / 2f,
                                j * tileMeasurement + tileMeasurement / 2f + rectangle.getY()
                                        - (tileMeasurement - rectangle.getHeight()) / 2f);

                        Body body = world.createBody(bodyDef);
                        PolygonShape polygonShape = new PolygonShape();
                        polygonShape.setAsBox(rectangle.getWidth() / 2f, rectangle.getHeight() / 2f);
                        body.createFixture(polygonShape, 1f);
                        polygonShape.dispose();

                    }
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
}
