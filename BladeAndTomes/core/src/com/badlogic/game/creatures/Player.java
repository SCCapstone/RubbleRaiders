package com.badlogic.game.creatures;

import Keyboard_Mouse_Controls.MainMenuControls;
import Sounds.playerMoveSound;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.math.Rectangle;

import java.awt.geom.Rectangle2D;
import java.lang.String;

public class Player extends Entity {

    //https://www.youtube.com/watch?v=5VyDsO0mFDU
    //Very helpful tutorial on enums by Margret Posch
    //TODO: Maybe make this a public enum in backbone?
    private enum classes {WARRIOR, CLERIC ,WIZARD};

    private int playerClass;
    private int physical;
    private int mental;
    private int social;
    private int acrobatics;
    private int bruteforce;
    private int speech;
    private int barter;
    private int awareness;
    private int intuition;
    private int gold;
    private String id;
    private String name;
    public Image playerIcon;
    private Sprite sprite;
    //public Texture playerIcon;
    public MainMenuControls controls;

    private World world;

    private Body playerBody;

    // Movement Sound
    playerMoveSound playerMovenSound;

    public Rectangle interactSquare;
    public Rectangle moveSquare;
    public Rectangle rangeSquare;
    public boolean isTurn;

    private ShapeRenderer renderer = new ShapeRenderer();

    public InputListener playerInput;

    final int MOVE_DISTANCE = 64;
    /*final int up = controls.getMoveUp();
    final int down = controls.getMoveDown();
    final int left = controls.getMoveLeft();
    final int right = controls .getMoveRight();*/

    private TextureAtlas idleTextureAtlas;
    private transient Animation<TextureRegion> idleAnimation;
    private TextureAtlas moveDownTextureAtlas;
    private transient Animation<TextureRegion> moveDownAnimation;
    private TextureAtlas moveUpTextureAtlas;
    private transient Animation<TextureRegion> moveUpAnimation;
    private TextureAtlas moveLeftTextureAtlas;
    private transient Animation<TextureRegion> moveLeftAnimation;
    private TextureAtlas moveRightTextureAtlas;
    private transient Animation<TextureRegion> moveRightAnimation;
    private TextureAtlas attackDownTextureAtlas;
    private transient Animation<TextureRegion> attackDownAnimation;
    private transient Animation<TextureRegion> currentAnimation;
    public float elapsedTime;


    /**
     * Default constructor for player entity
     */
    public Player() {
        super();
        this.id = "player";
        this.playerClass = 1;
        this.name = "";
        this.isTurn = true;


        playerMovenSound = new playerMoveSound();
        moveSquare = new Rectangle();
        interactSquare = new Rectangle();

        //TODO: Simplify all of this into Player class?
        //TODO: Move Player Icon Definitions to Backbone?
        playerIcon = new Image(new Texture(Gdx.files.internal("PlayerIcon.jpg")));
        playerIcon.setOrigin(playerIcon.getImageWidth()/2, playerIcon.getImageHeight()/2);
        playerIcon.setPosition( Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        playerIcon.setVisible(true);
        sprite = new Sprite(new Texture("PlayerIcon.jpg"));
        playerDef(sprite);
        //draw(playerIcon, 64f);
        moveSquare.setSize(64, 64);
        moveSquare.setPosition(0, 0);

        interactSquare.setSize(playerIcon.getImageWidth()*3, playerIcon.getImageHeight()*3);
        interactSquare.setPosition(playerIcon.getX() - MOVE_DISTANCE, playerIcon.getY() - MOVE_DISTANCE);

        idleTextureAtlas = new TextureAtlas(Gdx.files.internal("AnimationFiles/playerIdle.atlas"));
        idleAnimation = new Animation<TextureRegion>(1/2f, idleTextureAtlas.getRegions());
        currentAnimation = idleAnimation;
        moveDownTextureAtlas = new TextureAtlas(Gdx.files.internal("AnimationFiles/playerMoveDown.atlas"));
        moveDownAnimation = new Animation<TextureRegion>(1/5f, moveDownTextureAtlas.getRegions());
        moveUpTextureAtlas = new TextureAtlas(Gdx.files.internal("AnimationFiles/playerMoveUp.atlas"));
        moveUpAnimation = new Animation<TextureRegion>(1/5f, moveUpTextureAtlas.getRegions());
        moveLeftTextureAtlas = new TextureAtlas(Gdx.files.internal("AnimationFiles/playerMoveLeft.atlas"));
        moveLeftAnimation = new Animation<TextureRegion>(1/5f, moveLeftTextureAtlas.getRegions());
        moveRightTextureAtlas = new TextureAtlas(Gdx.files.internal("AnimationFiles/playerMoveRight.atlas"));
        moveRightAnimation = new Animation<TextureRegion>(1/5f, moveRightTextureAtlas.getRegions());
        attackDownTextureAtlas = new TextureAtlas(Gdx.files.internal("AnimationFiles/playerAttackDown.atlas"));
        attackDownAnimation = new Animation<TextureRegion>(1/5f, attackDownTextureAtlas.getRegions());

        // Thank you to libGDX.info editors for creating a helpful tutorial
        // on MoveActions as well as the libGDX creators for teaching pool-able actions
        // and InputListeners on their wiki.
        // https://libgdx.info/basic_action/
        // https://github.com/libgdx/libgdx/wiki/Scene2d
        gold = 100;

        playerIcon.addListener(playerInput = new InputListener() {

            @Override
            public boolean keyDown(InputEvent event, int keycode)
            {
                switch(keycode) {
                    case Input.Keys.UP:
                        //playerIcon.addListener((EventListener) Actions.moveTo(playerIcon.getX(), playerIcon.getY() + MOVE_DISTANCE, 1));
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
    }

    /**
     * Alternate constructor for player entity
     */
    public Player(int healthPoints, int fullHealth ,int armorPoints, int movement, int height, int width, int playerClass, String id, String name, Image image)
    {
        super(healthPoints, fullHealth, armorPoints, movement, height, width);
        this.id = id;
        this.playerClass = playerClass;
        this.name = name;
        this.playerIcon = image;
        this.isTurn = true;

        playerMovenSound = new playerMoveSound();
        gold = 100;

        // Thank you to libGDX.info editors for creating a helpful tutorial
        // on MoveActions as well as the libGDX creators for teaching pool-able actions
        // and InputListeners on their wiki.
        // https://libgdx.info/basic_action/
        // https://github.com/libgdx/libgdx/wiki/Scene2d
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

        playerIcon = new Image(new Texture(Gdx.files.internal("PlayerIcon.jpg")));
        playerIcon.setOrigin(playerIcon.getImageWidth()/2, playerIcon.getImageHeight()/2);
        playerIcon.setPosition( Gdx.graphics.getWidth()/ 2, Gdx.graphics.getHeight()/2);

        //playerDef(playerIcon);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(playerIcon.getImageWidth() / 2, playerIcon.getImageHeight() / 2);

        idleTextureAtlas = new TextureAtlas(Gdx.files.internal("AnimationFiles/playerIdle.atlas"));
        idleAnimation = new Animation(1/2f, idleTextureAtlas.getRegions());
        moveDownTextureAtlas = new TextureAtlas(Gdx.files.internal("AnimationFiles/playerMoveDown.atlas"));
        moveDownAnimation = new Animation<TextureRegion>(1/5f, moveDownTextureAtlas.getRegions());



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
        acrobatics = (int) ((physical * 1.25) + (mental / 2));
        return acrobatics;
    }

    public int getBruteforce() {
        bruteforce = (int) (physical * 1.75);
        return bruteforce;
    }

    public int getSpeech() {
        speech = (int) ((social * 1.25) + (physical / 2));
        return speech;
    }

    public int getBarter() {
        barter = social;
        return barter;
    }

    public int getAwareness() {
        awareness = (int) ((mental * 1.5) + (social / 2));
        return awareness;
    }

    public int getIntuition() {
        intuition = (int) (mental * .75);
        return intuition;
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
        acrobatics = (int) ((physical * 1.25) + (mental / 2));
        bruteforce = (int) (physical * 1.75);
        speech = (int) ((social * 1.25) + (physical / 2));
        barter = social;
        awareness = (int) ((mental * 1.5) + (social / 2));
        intuition = (int) (mental * .75);
    }

    public void setMental(int mental) {
        this.mental = mental;
        acrobatics = (int) ((physical * 1.25) + (mental / 2));
        bruteforce = (int) (physical * 1.75);
        speech = (int) ((social * 1.25) + (physical / 2));
        barter = social;
        awareness = (int) ((mental * 1.5) + (social / 2));
        intuition = (int) (mental * .75);
    }

    public void setSocial(int social) {
        this.social = social;
        acrobatics = (int) ((physical * 1.25) + (mental / 2));
        bruteforce = (int) (physical * 1.75);
        speech = (int) ((social * 1.25) + (physical / 2));
        barter = social;
        awareness = (int) ((mental * 1.5) + (social / 2));
        intuition = (int) (mental * .75);
    }

    public void setAcrobatics(int acro) { this.acrobatics = acro; }

    public void setBruteforce(int brute) { this.bruteforce = brute; }

    public void setSpeech(int s) { this.speech = s; }

    public void setBarter(int bart) { this.barter = bart; }

    public void setAwareness(int aware) { this.awareness = aware; }

    public void setIntuition(int i) { this.intuition = i; }

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

    public void resetElapsedTime() { elapsedTime = 0; }

    public void runAnimation(BladeAndTomes GAME) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        if(currentAnimation.isAnimationFinished(elapsedTime)) currentAnimation = idleAnimation;
        GAME.batch.draw(currentAnimation.getKeyFrame(elapsedTime, true), playerIcon.getX(), playerIcon.getY());
    }
    public void runMoveDownAnimation() { currentAnimation = moveDownAnimation; }
    public void runMoveUpAnimation() { currentAnimation = moveUpAnimation; }
    public void runMoveLeftAnimation() { currentAnimation = moveLeftAnimation; }
    public void runMoveRightAnimation() { currentAnimation = moveRightAnimation; }
    public void runAttackDownAnimation() { currentAnimation = attackDownAnimation; }


    private BodyDef playerDef(Sprite player) {
        BodyDef playerBod = new BodyDef();
        playerBod.type = BodyDef.BodyType.DynamicBody;
        playerBod.position.set(player.getX() + player.getWidth(),player.getY() + player.getHeight());

        //PolygonShape rectangle = new PolygonShape();
        //EdgeShape edge = new EdgeShape();

        FixtureDef fd = new FixtureDef();
        //fd.shape = rectangle;
        fd.density = 1;
        fd.friction = 1.0f;
        fd.restitution = 0.1f;

        return playerBod;
    }

}
