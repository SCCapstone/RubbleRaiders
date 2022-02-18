package com.badlogic.game.screens;

import ScreenOverlay.MainInventory;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.awt.Point;

public class Overworld extends ScreenAdapter {

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;
    SpriteBatch batch;

    Texture background;
    Texture chapel;
    Texture barracks;
    Texture questBoard;
    Texture portal;
    Texture NPCTrader;
    Texture marketStall;
    Texture tavern;

    Window pauseMenu;
    Label warning;
    TextButton options[];
    Table quitTable;
    InputListener escapePauseOver;

    MainInventory inventory;
    Point NPC_Cords;
    Point Portal_Cords;
    boolean doTrade;
    Label npcTraderMsg;

    // Helpful Collision Detection Tutorials (NOT IMPLEMENTED IN CODE YET)
    // TODO: IMPLEMENT THESE IN CODE
    //https://stackoverflow.com/questions/61491889/how-to-detect-collisions-between-objects-in-libgdx
    //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/math/Intersector.html

    public Overworld (final BladeAndTomes game) {

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
          new TextButton("Confirm", GAME.generalTextButtonStyle),
          new TextButton("Cancel", GAME.generalTextButtonStyle)
        };

        options[0].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                saveGame();
                GAME.stageInstance.removeListener(escapePauseOver);
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
        inventory = new MainInventory(GAME);
        NPC_Cords = new Point();
        NPC_Cords.setLocation(GAME.stageInstance.getWidth() /8,GAME.stageInstance.getHeight() / 2);
        Portal_Cords = new Point();
        Portal_Cords.setLocation(GAME.stageInstance.getWidth() /2,GAME.stageInstance.getHeight() / 8);
    }

    @Override
    public void render(float delta){

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        GAME.batch.begin();
        GAME.batch.draw(background, GAME.stageInstance.getWidth() * 0 ,GAME.stageInstance.getHeight() * 0);
        GAME.batch.draw(tavern, (float) (GAME.stageInstance.getWidth() * 0.75), (float) (GAME.stageInstance.getHeight() * 0.75));
        GAME.batch.draw(marketStall, GAME.stageInstance.getWidth() / 10, GAME.stageInstance.getWidth() / 10);
        GAME.batch.draw(barracks, (float) (GAME.stageInstance.getWidth()* 0.75), GAME.stageInstance.getHeight() / 4);
        GAME.batch.draw(chapel, GAME.stageInstance.getWidth() / 4, (float) (GAME.stageInstance.getHeight() * 0.75));
        GAME.batch.draw(questBoard, GAME.stageInstance.getWidth() / 2, GAME.stageInstance.getHeight() / 2);
        GAME.batch.draw(portal, GAME.stageInstance.getWidth() / 2, GAME.stageInstance.getHeight() / 8);
        GAME.batch.draw(NPCTrader, NPC_Cords.getLocation().x, NPC_Cords.getLocation().y);
        GAME.batch.end();

        //Simplifying the boundaries and attaching them to constants allows for ease of player access.
        /*
        if((GAME.player.moveSquare.getX() <= 2*MOVE_DISTANCE ||
                GAME.player.moveSquare.getY() <= 2*MOVE_DISTANCE) ||
                (GAME.player.moveSquare.getX() > GAME.stageInstance.getWidth() - 2*MOVE_DISTANCE ||
                        GAME.player.moveSquare.getY() > GAME.stageInstance.getHeight() - 2*MOVE_DISTANCE))
        {
            GAME.stageInstance.removeListener(escapePauseOver);
            GAME.stageInstance.clear();
            dispose();
            GAME.setScreen(new Dungeon(GAME));
        }*/

        //how player enters dungeon through the portal
        //I followed Anirudh Oruganti's method for the NPC interation in the overworld
        if((int)(GAME.player.moveSquare.getX()-Portal_Cords.getLocation().x)/100 == 0 &&(int)(GAME.player.moveSquare.getY()-Portal_Cords.getLocation().y)/100 == 0){
            GAME.stageInstance.removeListener(escapePauseOver);
            GAME.stageInstance.clear();
            dispose();
            GAME.setScreen(new Dungeon(GAME));
        }

        //if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
        //{
        //    GAME.stageInstance.setKeyboardFocus(null);
          //  GAME.stageInstance.addActor(pauseMenu);
            //pauseMenu.add(warning).center();
            //pauseMenu.row();
          //  pauseMenu.add(options[0], options[1]).center();
        //}

        // Thanks to user "centenond" on StackOverflow for pointing out a useful function on using rectangle to detect.
        // Co-Opted for use in our Project for creating walkable areas and loading zones.
        //https://stackoverflow.com/questions/61491889/how-to-detect-collisions-between-objects-in-libgdx
        /*for(int i=0; i<loadZone.length; i++) {
            if(GAME.player.moveSquare.overlaps(loadZone[i])) {
                GAME.stageInstance.clear();
                dispose();
                GAME.setScreen(new Dungeon(GAME));
            }
        }*/

        //Simplifying render thanks to libGDX for their "Extending the Simple Game" Tutorial,
        //Specifically the advanced section on super.render() as well as the following section on the main
        //game screen
        //https://libgdx.com/dev/simple-game-extended/
        npcTraderMsg = new Label("Wanna Trade\n Press \"T\" ", GAME.BaseLabelStyle2);

        if((int)(GAME.player.moveSquare.getX()-NPC_Cords.getLocation().x)/200 == 0 &&(int)(GAME.player.moveSquare.getY()-NPC_Cords.getLocation().y)/100 ==0){
            inventory.update();
            npcTraderMsg.setHeight(100);
            npcTraderMsg.setWidth(100);
            npcTraderMsg.setX(NPC_Cords.getLocation().x+50);
            npcTraderMsg.setY(NPC_Cords.getLocation().y+100);
            GAME.stageInstance.addActor(npcTraderMsg);

            if(Gdx.input.isKeyJustPressed(Input.Keys.T)){
            doTrade = !doTrade;
                inventory.showNPCTradeScreen(doTrade);
            }
        } else {
            if(doTrade){
                doTrade =false;
                inventory.showNPCTradeScreen(doTrade);
            }
            npcTraderMsg.addAction(Actions.removeActor());
        }
        inventory.update();

        GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
        GAME.stageInstance.draw();
        isCollisionHandled(GAME.player, GAME.stageInstance);
    }

    //Save all player data including name, stats, inventory
    public void saveGame(){
        //Need to determine which game to save to
        /*
        Preferences prefs = Gdx.app.getPreferences("Game1");
        prefs.putString("name", GAME.player.getName());
        prefs.putInteger("physical", GAME.player.getPhysical());
        prefs.putInteger("social", GAME.player.getSocial());
        prefs.putInteger("mental", GAME.player.getMental());
        prefs.putInteger("acrobatics", GAME.player.getAcrobatics());
        prefs.putInteger("brute force", GAME.player.getBruteforce());
        prefs.putInteger("bartering", GAME.player.getBarter());
        prefs.putInteger("speech", GAME.player.getSpeech());
        prefs.putInteger("awareness", GAME.player.getAwareness());
        prefs.putInteger("intuition", GAME.player.getIntuition());
        //Movement
        prefs.put*/
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
    public boolean isCollisionHandled(Player player, Stage stage)
    {
        if(player.playerIcon.getX() <= 2*MOVE_DISTANCE)
        {
            player.playerIcon.setPosition(player.playerIcon.getX() + MOVE_DISTANCE, player.playerIcon.getY());
            player.moveSquare.setPosition(player.moveSquare.getX() + MOVE_DISTANCE, player.moveSquare.getY());
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);
        }
        else if(player.playerIcon.getY() <= 2*MOVE_DISTANCE)
        {
            player.playerIcon.setPosition(player.playerIcon.getX(), player.playerIcon.getY() + MOVE_DISTANCE);
            player.moveSquare.setPosition(player.moveSquare.getX(), player.moveSquare.getY() + MOVE_DISTANCE);
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);
        }
        else if(player.playerIcon.getX() >= stage.getWidth() - 2*MOVE_DISTANCE)
        {
            player.playerIcon.setPosition(player.playerIcon.getX() - MOVE_DISTANCE, player.playerIcon.getY());
            player.moveSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY());
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);
        }
        else if(player.playerIcon.getY() >= stage.getHeight() - 2*MOVE_DISTANCE)
        {
            player.playerIcon.setPosition(player.playerIcon.getX(), player.playerIcon.getY() - MOVE_DISTANCE);
            player.moveSquare.setPosition(player.moveSquare.getX(), player.moveSquare.getY() - MOVE_DISTANCE);
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);
        }

        return true;
    }

}
