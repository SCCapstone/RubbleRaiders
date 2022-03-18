package com.badlogic.game.screens;

import Keyboard_Mouse_Controls.SaveLoadGame;
import ScreenOverlay.MainInventory;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCBuyer;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCSeller;
import ScreenOverlayRework.Inventory.NPCInventoryUI.TownHallQuestBoard;
import ScreenOverlayRework.OverlayManager;
import Sounds.playerMoveSound;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;

import java.awt.Point;

public class Overworld extends ScreenAdapter {

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;
    SpriteBatch batch;

    private String[] names;
    private String[] saveTime;


    Texture background;
    Texture chapel;
    Texture barracks;
    Texture questBoard;
    Texture portal;
    Texture NPCTrader;
    Texture marketStall;
    Texture tavern;

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

    Window pauseMenu;
    Label warning;
    TextButton options[];
    Table quitTable;
    InputListener escapePauseOver;

    playerMoveSound playerMovenSound;

    MainInventory inventory;
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
    int counter;
    // Helpful Collision Detection Tutorials (NOT IMPLEMENTED IN CODE YET)
    // TODO: IMPLEMENT THESE IN CODE
    //https://stackoverflow.com/questions/61491889/how-to-detect-collisions-between-objects-in-libgdx
    //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/math/Intersector.html

    public Overworld (final BladeAndTomes game) {
        counter = 0;

        this.GAME = game;

        MOVE_DISTANCE = 64;
        doTrade = false;
        batch = new SpriteBatch();

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

        pauseMenu = new Window("", GAME.generalWindowStyle);
        pauseMenu.setHeight(400);
        pauseMenu.setWidth(600);
        pauseMenu.setPosition(GAME.stageInstance.getWidth()/3, GAME.stageInstance.getHeight()/3);
        pauseMenu.setMovable(true);
        pauseMenu.setKeepWithinStage(true);

        saveQuit = new Window("SaveQuit", GAME.generalWindowStyle);
        //loadWindow.setBackground(new TextureRegionDrawable(new TextureRegion()));
        //loadWindow.setBackground();
        saveQuit.setSize(GAME.stageInstance.getWidth()/4,GAME.stageInstance.getHeight());
        saveQuit.setPosition(GAME.stageInstance.getWidth()*0.35f, GAME.stageInstance.getHeight()*0.35f);
        saveBack = new TextButton("Back", GAME.generalTextButtonStyle);
        saveBack.setSize(100f,50f);
        saveBack.setColor(Color.LIGHT_GRAY);
        savedGames = new Table();
        savedGames.setFillParent(true);
        savedGames.defaults();
        game1 = new TextButton("Saved Game 1", GAME.generalTextButtonStyle);
        game1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                saveGame(1);
                SaveLoadGame.saveGameOne();
            }
        });
        game2 = new TextButton("Saved Game 2", GAME.generalTextButtonStyle);
        game2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                saveGame(2);
                SaveLoadGame.saveGameTwo();
            }
        });
        game3 = new TextButton("Saved Game 3", GAME.generalTextButtonStyle);
        game3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                saveGame(3);
                SaveLoadGame.saveGameThree();
            }
        });
        game4 = new TextButton("Saved Game 4", GAME.generalTextButtonStyle);
        game4.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                saveGame(4);
                SaveLoadGame.saveGameFour();
            }
        });
        //Add listeners for the buttons
        savedGames.add(saveBack).padBottom(3f);
        savedGames.row();
        savedGames.add(game1).padBottom(3f);
        savedGames.row();
        savedGames.add(game2).padBottom(3f);
        savedGames.row();
        savedGames.add(game3).padBottom(3f);
        savedGames.row();
        savedGames.add(game4);

        saveQuit.addActor(savedGames);

        escapePauseOver = new InputListener() {
            public boolean keyDown(InputEvent event, int keycode)
            {
                if(keycode == Input.Keys.ESCAPE)
                {
                    GAME.stageInstance.setKeyboardFocus(null);
                    GAME.stageInstance.addActor(pauseMenu);
                    pauseMenu.add(warning).center().colspan(3);
                    pauseMenu.row();
                    pauseMenu.add(options[0], options[1]).center();
                }
                return true;
            }
        };

        GAME.player.playerIcon.addListener(escapePauseOver);

        warning = new Label("Are you sure you want to Quit?", GAME.generalLabelStyle);

        options = new TextButton[] {
          new TextButton("Save and Quit", GAME.generalTextButtonStyle),
          new TextButton("Cancel", GAME.generalTextButtonStyle)
        };

        options[0].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                //GAME.stageInstance.removeListener(escapePauseOver);
                //GAME.stageInstance.clear();
                pauseMenu.clear();
                pauseMenu.remove();
                //dispose();
                GAME.stageInstance.addActor(saveQuit);
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

        saveBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                saveQuit.remove();
                //GAME.stageInstance.clear();
                GAME.stageInstance.setKeyboardFocus(null);
                GAME.stageInstance.addActor(pauseMenu);
                pauseMenu.add(warning).center().colspan(3);
                pauseMenu.row();
                pauseMenu.add(options[0], options[1]).center();
                GAME.stageInstance.addActor(pauseMenu);
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
        game.player = game.loadSaveManager.loadPlayer(1);
        game.overlays = new OverlayManager(game);
        game.overlays.setOverLayesVisibility(true);
        game.overlays.setOverLayesVisibility(true);

        npcSeller = game.overlays.generateNewNPSeller();
        isNpcSellerVisible =false;
        npcBuyer = game.overlays.generateNewNPCBuyer();
        isNpcBuyerVisible = false;
        questBoardTrade= game.overlays.generateQuestBoard();
        isQuestBoardTradeVisible = false;


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Set the pixel lengths & heights for each texture. This allows for proper scaling of our project
        GAME.batch.begin();

        GAME.batch.draw(background, GAME.stageInstance.getWidth() * 0 ,GAME.stageInstance.getHeight() * 0);
        GAME.batch.draw(tavern, (float) (GAME.stageInstance.getWidth() * 0.75),
                (float) (GAME.stageInstance.getHeight() * 0.75), tavernHeight, tavernWidth);
        GAME.batch.draw(marketStall, GAME.stageInstance.getWidth() / 10, GAME.stageInstance.getWidth() / 10,
                marketStallHeight, marketStallWidth);
        GAME.batch.draw(barracks, (float) (GAME.stageInstance.getWidth()* 0.75), (float)
                (GAME.stageInstance.getHeight() / 4), barracksWidth, barracksHeight);
        GAME.batch.draw(chapel, (float) (GAME.stageInstance.getWidth()) / 4, (float)
                (GAME.stageInstance.getHeight() * 0.75), chapelWidth, chapelHeight);
        GAME.batch.draw(questBoard, (float) (GAME.stageInstance.getWidth()) / 2, (float)
                (GAME.stageInstance.getHeight() / 2), questBoardWidth, questBoardHeight);
        GAME.batch.draw(portal, GAME.stageInstance.getWidth() / 2,
                GAME.stageInstance.getHeight() / 8, portalWidth, portalHeight);
        GAME.batch.draw(NPCTrader, NPC_Cords.getLocation().x, NPC_Cords.getLocation().y, 64f, 64f);
        GAME.batch.end();

        //how player enters dungeon through the portal
        //I followed Anirudh Oruganti's method for the NPC interation in the overworld
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
        GAME.overlays.updateHealth();

        // Displays Hidden Inventory Table

        // COMMENT THIS CODE TO GET TRADING WORKING
        if(Gdx.input.isKeyJustPressed(Input.Keys.E) && !isNpcSellerVisible && !isNpcBuyerVisible&&!isQuestBoardTradeVisible)
            GAME.overlays.setHiddenTableVisibility(!GAME.showHiddenInventory);
        if(Gdx.input.isKeyJustPressed(Input.Keys.T)&& !isNpcBuyerVisible){
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
        GAME.loadSaveManager.savePlayer(GAME.player,1);


    }

    //Save all player data including name, stats, inventory
    public void saveGame(int id){
        GAME.loadSaveManager.savePlayer(GAME.player,0);
        GAME.stageInstance.removeListener(escapePauseOver);
        GAME.stageInstance.clear();
        dispose();
        GAME.setScreen(new MainMenu(GAME));
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
        } // following added is for buildings in overworld by Brent Able

        return true;
    }

}
