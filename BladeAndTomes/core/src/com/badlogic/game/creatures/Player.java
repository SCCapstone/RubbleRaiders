package com.badlogic.game.creatures;

import Keyboard_Mouse_Controls.MainMenuControls;
import ScreenOverlayRework.Inventory.ItemUI.Quest.QuestDocument;
import ScreenOverlayRework.Inventory.itemDocument;
import Sounds.playerMoveSound;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.lang.String;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Player extends Entity {

    //https://www.youtube.com/watch?v=5VyDsO0mFDU
    //Very helpful tutorial on enums by Margret Posch
    //TODO: Maybe make this a public enum in backbone?
    private boolean isDefault;
    private enum classes {WARRIOR, CLERIC ,WIZARD};
//    public static Array<quest> quests;
//    public static Array<quest> usedQuests;
    private int playerClass;
    private int physical;
    private int mental;
    private int social;


    public AtomicInteger acrobatics;
    public AtomicInteger bruteforce;
    public AtomicInteger speech;
    public AtomicInteger barter;
    public AtomicInteger awareness;
    public AtomicInteger intuition;

    private int gold;
    private String id;
    private String name;
    public transient Image playerIcon;
    public transient MainMenuControls controls;

    public boolean updateQuest;

    // Follow variables are used for quests
    public int kAssignations;
    public int kDeaths;
    public int kDungeonsExplored;
    public int kTradesNPCSeller;
    public int kTradesNPCBuyer;
    public int kChestsOpened;
    public int kUsedPositions;
    public int kCompleteQuests;
    public int kEarnedGoldThroughQuest;
    public int kCloseRangeKills;
    public int kLongRangeKills;
    public int kLevelsCompleted;
    public int kEarnedGoldThroughLevels;
    // End of variables are used for quests

    // Player's Defence
    public int playerDefence;

    // Movement Sound
    transient playerMoveSound playerMovenSound;

    public transient Rectangle interactSquare;
    public transient Rectangle moveSquare;
    public transient Rectangle rangeSquare;
    public boolean isTurn;
    public AtomicInteger tokens;

    public transient InputListener playerInput;
    // For Inventory
    public Array<QuestDocument> activeQuests;
    public Array<itemDocument> inventoryItems;

    final int MOVE_DISTANCE = 32;
    /*final int up = controls.getMoveUp();
    final int down = controls.getMoveDown();
    final int left = controls.getMoveLeft();
    final int right = controls .getMoveRight();*/

    /**
     * Default constructor for player entity
     */
    public Player() {
        super();
        inventoryItems = new Array<>();

        this.id = "player";
        isDefault = true;
        this.playerClass = 1;
        this.name = "";
        this.isTurn = true;
        tokens = new AtomicInteger(0);
        playerDefence = 0;


        playerMovenSound = new playerMoveSound();
        moveSquare = new Rectangle();
        interactSquare = new Rectangle();

        //TODO: Simplify all of this into Player class?
        //TODO: Move Player Icon Definitions to Backbone?
        playerIcon = new Image(new Texture(Gdx.files.internal("PlayerIcon.jpg")));
        playerIcon.setOrigin(playerIcon.getImageWidth() / 2, playerIcon.getImageHeight() / 2);
        playerIcon.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        playerIcon.setVisible(false);
        playerBody(playerIcon);
        moveSquare.setSize(64, 64);
        moveSquare.setPosition(0, 0);

        interactSquare.setSize(64, 64);
        interactSquare.setPosition(playerIcon.getX() - MOVE_DISTANCE, playerIcon.getY() - MOVE_DISTANCE);

        // Thank you to libGDX.info editors for creating a helpful tutorial
        // on MoveActions as well as the libGDX creators for teaching pool-able actions
        // and InputListeners on their wiki.
        // https://libgdx.info/basic_action/
        // https://github.com/libgdx/libgdx/wiki/Scene2d
        gold = 100;

        kAssignations = 0;
        kDeaths = 0;
        kDungeonsExplored = 0;
        kTradesNPCSeller = 0;
        kTradesNPCBuyer = 0;
        kChestsOpened = 0;
        kUsedPositions = 0;
        kCompleteQuests = 0;
        kEarnedGoldThroughQuest = 0;
        kCloseRangeKills = 0;
        kLongRangeKills = 0;
        kLevelsCompleted = 0;
        kEarnedGoldThroughLevels = 0;

        if (activeQuests == null)
            activeQuests = new Array<>();
        updateQuest = false;
        for (int i = 0; i < (26); ++i) {
            itemDocument itemTemp = new itemDocument();
            itemTemp.setIndex(String.valueOf(i));
            itemTemp.setTargetItem("Null");
            itemTemp.setCategory("Null");
            inventoryItems.add(itemTemp);
        }

        activeQuests.add(null);
        activeQuests.add(null);
        activeQuests.add(null);
        activeQuests.add(null);



         acrobatics = new AtomicInteger(0);
        bruteforce = new AtomicInteger(0);
        speech = new AtomicInteger(0);
        barter = new AtomicInteger(0);
        awareness = new AtomicInteger(0);
        intuition = new AtomicInteger(0);


    }
    /**
     * Alternate constructor for player entity
     */
    public Player(int healthPoints, int fullHealth ,int armorPoints, int movement, int height, int width, int playerClass, String id, String name, Image image)
    {
        super(healthPoints, fullHealth, armorPoints, movement, height, width);
        this.id = id;
        isDefault = true;
        this.playerClass = playerClass;
        this.name = name;
        this.playerIcon = image;
        this.isTurn = true;
        tokens = new AtomicInteger(0);
        playerMovenSound = new playerMoveSound();
        gold = 100;

        playerIcon = new Image(new Texture(Gdx.files.internal("PlayerIcon.jpg")));
        playerIcon.setOrigin(playerIcon.getImageWidth()/2, playerIcon.getImageHeight()/2);
        playerIcon.setPosition( Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);

        moveSquare.setSize(playerIcon.getImageWidth(), playerIcon.getImageHeight());
        moveSquare.setPosition(playerIcon.getX(), playerIcon.getY());

        interactSquare.setSize(playerIcon.getImageWidth()*3, playerIcon.getImageHeight()*3);
        interactSquare.setPosition(playerIcon.getX() - MOVE_DISTANCE, playerIcon.getY() - MOVE_DISTANCE);


        // Thank you to libGDX.info editors for creating a helpful tutorial
        // on MoveActions as well as the libGDX creators for teaching pool-able actions
        // and InputListeners on their wiki.
        // https://libgdx.info/basic_action/
        // https://github.com/libgdx/libgdx/wiki/Scene2d
        gold = 100;

        /*
        playerIcon.addListener(playerInput = new InputListener() {

            @Override
            public boolean keyDown(InputEvent event, int keycode)
            {
                switch(keycode) {
                    case Input.Keys.UP:
                        playerIcon.addAction(Actions.moveTo(playerIcon.getX(), playerIcon.getY() + MOVE_DISTANCE,1));
                        playerMovenSound.playMoveSound();
                        break;
                    case Input.Keys.DOWN:
                        playerIcon.addAction(Actions.moveTo(playerIcon.getX(), playerIcon.getY() - MOVE_DISTANCE,1));
                        playerMovenSound.playMoveSound();
                        break;
                    case Input.Keys.LEFT:
                        playerIcon.addAction(Actions.moveTo(playerIcon.getX() - MOVE_DISTANCE, playerIcon.getY(),1));
                        playerMovenSound.playMoveSound();
                        break;
                    case Input.Keys.RIGHT:
                        playerIcon.addAction(Actions.moveTo(playerIcon.getX() + MOVE_DISTANCE, playerIcon.getY(),1));
                        playerMovenSound.playMoveSound();
                        break;
                    default:
                        return false;
                }
                isTurn = false;
                moveSquare.setPosition(playerIcon.getX(), playerIcon.getY());
                interactSquare.setPosition(playerIcon.getX() - MOVE_DISTANCE, playerIcon.getY() - MOVE_DISTANCE);
                return true;
            }
        });

         */
        kAssignations = 0;
        kDeaths = 0;
        kDungeonsExplored = 0;
        kTradesNPCSeller = 0;
        kTradesNPCBuyer = 0;
        kChestsOpened = 0;
        kUsedPositions =0;
        kCompleteQuests =0;

        kEarnedGoldThroughQuest = 0;
        kCloseRangeKills = 0;
        kLongRangeKills = 0;
        kLevelsCompleted = 0;
        kEarnedGoldThroughLevels = 0;
        playerDefence = 0;


        activeQuests.add(null);
        activeQuests.add(null);
        activeQuests.add(null);
        activeQuests.add(null);

        playerIcon = new Image(new Texture(Gdx.files.internal("PlayerIcon.jpg")));
        playerIcon.setOrigin(playerIcon.getImageWidth()/2, playerIcon.getImageHeight()/2);
        playerIcon.setPosition( Gdx.graphics.getWidth()/ 2, Gdx.graphics.getHeight()/2);

        playerBody(playerIcon);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(playerIcon.getImageWidth() / 2, playerIcon.getImageHeight() / 2);

        acrobatics = new AtomicInteger(0);
        bruteforce = new AtomicInteger(0);
        speech = new AtomicInteger(0);
        barter = new AtomicInteger(0);
        awareness = new AtomicInteger(0);
        intuition = new AtomicInteger(0);

    }

    public boolean getDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public int getPlayerClass() {
        return playerClass;
    }

    public String getId() {
        return id;
    }

    public int getPhysical() {
        return physical;
    }

    public int getMental() {
        return mental;
    }

    public int getSocial() {
        return social;
    }



    public int getAcrobatics() {
        return acrobatics.get();
    }

    public int getBruteforce() {
        return bruteforce.get();
    }

    public int getSpeech() {
        return speech.get();
    }

    public int getBarter() {
        return barter.get();
    }

    public int getAwareness() {
        return awareness.get();
    }

    public int getIntuition() {
        return intuition.get();
    }

    public void setPlayerClass(int playerClass) {
        this.playerClass = playerClass;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhysical(int physical) {
        this.physical = physical;
        acrobatics.set((int) ((physical * 1.25) + (mental / 2)));
        bruteforce.set((int) (physical * 1.75));
        speech.set( (int) ((social * 1.25) + (physical / 2)));
        awareness.set((int) ((mental * 1.5) + (social / 2)));
        barter.set(social);
        intuition.set((int) (mental * .75));

    }

    public void setMental(int mental) {
        this.mental = mental;
        acrobatics.set((int) ((physical * 1.25) + (mental / 2)));
        bruteforce.set((int) (physical * 1.75));
        speech.set( (int) ((social * 1.25) + (physical / 2)));
        awareness.set((int) ((mental * 1.5) + (social / 2)));
        barter.set(social);
        intuition.set((int) (mental * .75));
    }

    public void setSocial(int social) {
        this.social = social;
        acrobatics.set((int) ((physical * 1.25) + (mental / 2)));
        bruteforce.set((int) (physical * 1.75));
        speech.set( (int) ((social * 1.25) + (physical / 2)));
        awareness.set((int) ((mental * 1.5) + (social / 2)));
        barter.set(social);
        intuition.set((int) (mental * .75));
    }

    public void setAcrobatics(int acro) { this.acrobatics.set( acro); }

    public void setBruteforce(int brute) { this.bruteforce.set(brute); }

    public void setSpeech(int s) { this.speech.set(s); }

    public void setBarter(int bart) { this.barter.set(bart); }

    public void setAwareness(int aware) { this.awareness.set(aware); }

    public void setIntuition(int i) { this.intuition.set(i); }

    public boolean handleMovement(Rectangle playerMove, Rectangle walkableBorder) {
        return true;
    }

    public boolean handleExit(Rectangle playerMove, Rectangle exitSquare) {
        return true;
    }

    public void setGold(int val) {
        this.gold = val;
    }

    public int getGold() {
        return gold;
    }

    public BodyDef playerBody(Image player) {
        BodyDef playerBod = new BodyDef();
        playerBod.type = BodyDef.BodyType.DynamicBody;
        playerBod.position.set(player.getWidth() / 2, player.getHeight() / 2);

        return playerBod;
    }

}
