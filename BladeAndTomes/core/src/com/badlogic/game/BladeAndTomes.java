package com.badlogic.game;
// check check
import Keyboard_Mouse_Controls.MainMenuControls;
import LoadAndSave.LoadSaveManager;
import ScreenOverlayRework.Inventory.itemDocument;
import ScreenOverlayRework.OverlayManager;
import Sounds.BackGroundMusic;
import com.badlogic.game.creatures.Inventory;
import com.badlogic.game.creatures.Player;
import com.badlogic.game.screens.MainMenu;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.*;

import java.util.Random;

// PUT IN CLAYMORE STORM EASTER EGG
// Put in rick roll easter egg also
public class BladeAndTomes extends Game {
    ShapeRenderer shapeRenderer;
    public SpriteBatch batch;
    public BitmapFont font;
    Texture image;
    public Stage stageInstance;
    public final float WINDOWWIDTH = 1920, WINDOWHIGHT = 1080;

    public enum classes {WARRIOR, CLERIC, WIZARD}

    public ScalingViewport scaleViewport;
    public OrthographicCamera camera;

    //Defining general text button look
    public TextButtonStyle generalTextButtonStyle;
    TextureRegion generalTextButtonUpRegion;
    TextureRegion generalTextButtonDownRegion;
    Texture generalTextButtonUpState;
    Texture generalTextButtonDownState;

    public TextFieldStyle generalTextFieldStyle;
    public LabelStyle generalLabelStyle;
    public WindowStyle generalWindowStyle;
    public SliderStyle generalSliderStyle;

    public TextButtonStyle inventoryTextButtonStyle;
    public TextButtonStyle inventoryBaseStyle;

    Texture inventoryTextButtonState;
    Texture inventoryBase1;
    Texture inventoryBase2;
    TextureRegion inventoryTextButtonRegion;
    public LabelStyle BaseLabelStyle1;
    public LabelStyle BaseLabelStyle2;

    public LabelStyle HealthLabelStyle;
    public MainMenuControls controls;
    public Player player;
    public Image playerIcon;
    public BackGroundMusic _bgmusic;
    public Array<itemDocument> inventoryItems;

    public OverlayManager overlays;
    public boolean showtrade = false;
    public boolean showtradeBuyer = false;
    public boolean showHiddenInventory = false;
    public boolean refreshInventory = false;
    public final int MOVE_DISTANCE = 64;
    public AssetManager assets;
    public DragAndDrop dnd;

    public int GRID_X_SQUARE = 21;
    public int GRID_Y_SQUARE = 12;
    public final int[] GRID_X = new int[GRID_X_SQUARE];
    public final int[] GRID_Y = new int[GRID_Y_SQUARE];

    public int currentInventorySelection;
    public int tokens;
    public int currentSaveIndex;
    // Resets at the end of each dung
    public int spellDamageIncrease;

    public LoadSaveManager loadSaveManager;
    public itemDocument NullItemDoc;
    public TextureAtlas idleTextureAtlas;
    public transient Animation<TextureRegion> idleAnimation;
    public TextureAtlas moveDownTextureAtlas;
    public transient Animation<TextureRegion> moveDownAnimation;
    public TextureAtlas moveUpTextureAtlas;
    public transient Animation<TextureRegion> moveUpAnimation;
    public TextureAtlas moveLeftTextureAtlas;
    public transient Animation<TextureRegion> moveLeftAnimation;
    public TextureAtlas moveRightTextureAtlas;
    public transient Animation<TextureRegion> moveRightAnimation;
    public TextureAtlas attackDownTextureAtlas;
    public transient Animation<TextureRegion> attackDownAnimation;
    public TextureAtlas attackUpTextureAtlas;
    public transient Animation<TextureRegion> attackUpAnimation;
    public TextureAtlas attackLeftTextureAtlas;
    public transient Animation<TextureRegion> attackLeftAnimation;
    public TextureAtlas attackRightTextureAtlas;
    public transient Animation<TextureRegion> attackRightAnimation;
    private transient Animation<TextureRegion> currentAnimation;
    public float elapsedTime;

    public static boolean enterDungeon;
    public static boolean exitDungeon;
    public static boolean isChest;

    public boolean isPaused;

//    Rectangle townHall;

    private final AssetManager manager = new AssetManager();


    /**
     * Creates and initializes all objects and variables for the main project before moving the program to
     * the first screen.
     */
    @Override
    public void create() {
        spellDamageIncrease = 0;
        NullItemDoc = new itemDocument();
        currentSaveIndex = 2;
        dnd = new DragAndDrop();
        enterDungeon = false;
        exitDungeon = false;
        isChest = false;
        isPaused = false;
        loadSaveManager = new LoadSaveManager();
        controls = loadSaveManager.getSettings();

        assets = new AssetManager();
        currentInventorySelection = 1;
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        //Section sets up the grid so that the player can move around correctly
        int x_start = 264;
        int y_start = 152;
        for (int i = 0; i < GRID_X_SQUARE; i++) {
            GRID_X[i] = x_start + 64 * (i + 1);
        }
        for (int i = 0; i < GRID_Y_SQUARE; i++) {
            GRID_Y[i] = y_start + 64 * (i + 1);
        }

        // Work for resizing of screen

        //Used BackgroundMusic created and designed by Anirudh Oruganti and moved it to the backbone
        //to fix pause menu glitch.
        _bgmusic = new
                BackGroundMusic();
        _bgmusic.playOverWorldMusic();

        // Inventory Things

        //Sets Scene2D instance

        // Adjusting this to a scale viewport for now on
        // Anri suggested checking out tabnine for libgdx. Found a solution with following line
        // specifically changing viewport to ScalingViewport
        stageInstance = new Stage(new ScalingViewport(Scaling.fit, 1920, 1080));

        //Sets upstate and downstate textures for texture Buttons
        assets.load("Text_Button_Up_State.jpg",Texture.class);
        assets.load("Text_Button_Down_State.jpg",Texture.class);
        assets.load("inventorySlot.png",Texture.class);
        assets.load("inventoryBaseImage.png",Texture.class);
        assets.load("inventoryBaseImage2.png",Texture.class);
        assets.finishLoading();


        generalTextButtonUpState = assets.get("Text_Button_Up_State.jpg",Texture.class);
        generalTextButtonDownState = assets.get("Text_Button_Down_State.jpg",Texture.class);
        inventoryTextButtonState = assets.get("inventorySlot.png",Texture.class);
        inventoryBase1 = assets.get("inventoryBaseImage.png",Texture.class);
        inventoryBase2 = assets.get("inventoryBaseImage2.png",Texture.class);

        manager.load("Text_Button_Up_State.jpg", Texture.class);
        manager.load("Text_Button_Down_State.jpg", Texture.class);
        manager.load("inventorySlot.png", Texture.class);
        manager.load("inventoryBaseImage.png", Texture.class);
        manager.load("inventoryBaseImage2.png", Texture.class);
        manager.load("AnimationFiles/playerIdle.atlas", TextureAtlas.class);
        manager.load("AnimationFiles/playerMoveUp.atlas", TextureAtlas.class);
        manager.load("AnimationFiles/playerMoveDown.atlas", TextureAtlas.class);
        manager.load("AnimationFiles/playerMoveLeft.atlas", TextureAtlas.class);
        manager.load("AnimationFiles/playerMoveRight.atlas", TextureAtlas.class);
        manager.load("AnimationFiles/playerAttackDown.atlas", TextureAtlas.class);
        manager.load("AnimationFiles/playerAttackUp.atlas", TextureAtlas.class);
        manager.load("AnimationFiles/playerAttackLeft.atlas", TextureAtlas.class);
        manager.load("AnimationFiles/playerAttackRight.atlas", TextureAtlas.class);
        manager.finishLoading();

        generalTextButtonUpState = manager.get("Text_Button_Up_State.jpg");
        generalTextButtonDownState = manager.get("Text_Button_Down_State.jpg");
        inventoryTextButtonState = manager.get("inventorySlot.png");
        inventoryBase1 = manager.get("inventoryBaseImage.png");
        inventoryBase2 = manager.get("inventoryBaseImage2.png");
        idleTextureAtlas = manager.get("AnimationFiles/playerIdle.atlas");
        idleAnimation = new Animation<TextureRegion>(1/8f, idleTextureAtlas.getRegions());
        currentAnimation = idleAnimation;
        moveDownTextureAtlas = manager.get("AnimationFiles/playerMoveDown.atlas");
        moveDownAnimation = new Animation<TextureRegion>(1/8f, moveDownTextureAtlas.getRegions());
        moveUpTextureAtlas = manager.get("AnimationFiles/playerMoveUp.atlas");
        moveUpAnimation = new Animation<TextureRegion>(1/8f, moveUpTextureAtlas.getRegions());
        moveLeftTextureAtlas = manager.get("AnimationFiles/playerMoveLeft.atlas");
        moveLeftAnimation = new Animation<TextureRegion>(1/8f, moveLeftTextureAtlas.getRegions());
        moveRightTextureAtlas = manager.get("AnimationFiles/playerMoveRight.atlas");
        moveRightAnimation = new Animation<TextureRegion>(1/8f, moveRightTextureAtlas.getRegions());
        attackDownTextureAtlas = manager.get("AnimationFiles/playerAttackDown.atlas");
        attackDownAnimation = new Animation<TextureRegion>(1/5f, attackDownTextureAtlas.getRegions());
        attackUpTextureAtlas = manager.get("AnimationFiles/playerAttackUp.atlas");
        attackUpAnimation = new Animation<TextureRegion>(1/5f, attackUpTextureAtlas.getRegions());
        attackLeftTextureAtlas = manager.get("AnimationFiles/playerAttackLeft.atlas");
        attackLeftAnimation = new Animation<TextureRegion>(1/5f, attackLeftTextureAtlas.getRegions());
        attackRightTextureAtlas = manager.get("AnimationFiles/playerAttackRight.atlas");
        attackRightAnimation = new Animation<TextureRegion>(1/5f, attackRightTextureAtlas.getRegions());

        //Sets up the region to be used
        generalTextButtonUpRegion = new TextureRegion(generalTextButtonUpState);
        generalTextButtonDownRegion = new TextureRegion(generalTextButtonDownState);
        inventoryTextButtonRegion = new TextureRegion(inventoryTextButtonState);

        //Defines the style to be used in the text buttons
        generalTextButtonStyle = new TextButtonStyle();
        generalTextButtonStyle.up = new TextureRegionDrawable(generalTextButtonUpRegion);
        generalTextButtonStyle.down = new TextureRegionDrawable(generalTextButtonDownRegion);
        generalTextButtonStyle.font = font;
        generalTextButtonStyle.fontColor = new Color(0f, 0f, 0f, 1f);

        inventoryTextButtonStyle = new TextButtonStyle();
        inventoryTextButtonStyle.up = new TextureRegionDrawable(inventoryTextButtonRegion);
        inventoryTextButtonStyle.down = new TextureRegionDrawable(inventoryTextButtonRegion);
        inventoryTextButtonStyle.font = font;
        inventoryTextButtonStyle.fontColor = new Color(0f, 0f, 0f, 1f);

        inventoryBaseStyle = new TextButtonStyle();
        inventoryBaseStyle.up = new TextureRegionDrawable(new TextureRegion(inventoryBase1));
        inventoryBaseStyle.down = new TextureRegionDrawable(new TextureRegion(inventoryBase2));
        inventoryBaseStyle.font = font;
        inventoryBaseStyle.fontColor = new Color(0f, 0f, 0f, 1f);

        //Defines the style to be used for the text field
        generalTextFieldStyle = new TextFieldStyle();
        generalTextFieldStyle.font = font;
        generalTextFieldStyle.fontColor = new Color(0f, 0f, 0f, 1f);
        generalTextFieldStyle.background = new TextureRegionDrawable(generalTextButtonUpRegion);

        //Defines the style to be used for the Label
        generalLabelStyle = new LabelStyle();
        generalLabelStyle.font = font;
        generalLabelStyle.background = new TextureRegionDrawable(generalTextButtonUpRegion);
        generalLabelStyle.fontColor = new Color(0f, 0f, 0f, 1f);

        HealthLabelStyle = new LabelStyle();
        HealthLabelStyle.font = font;
        //HealthLabelStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("healthBar.jpg"))));
        HealthLabelStyle.fontColor = new Color(100f, 0f, 0f, 1f);

        BaseLabelStyle1 = new LabelStyle();
        BaseLabelStyle1.font = font;
        BaseLabelStyle1.background = new TextureRegionDrawable(new TextureRegion(inventoryTextButtonState));
        BaseLabelStyle1.fontColor = new Color(0f, 0f, 0f, 10f);

        BaseLabelStyle2 = new LabelStyle();
        BaseLabelStyle2.font = font;
        BaseLabelStyle2.background = new TextureRegionDrawable(new TextureRegion(inventoryBase2));
        BaseLabelStyle2.fontColor = new Color(0f, 0f, 0f, 10f);

        //libGDX documentation on WindowStyle and how it is to be implemented by libGDX devs
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Window.WindowStyle.html
        generalWindowStyle = new WindowStyle();
        generalWindowStyle.titleFont = font;
        generalWindowStyle.titleFontColor = new Color(0f, 0f, 0f, 1f);
        //generalWindowStyle.stageBackground = new TextureRegionDrawable(new Texture(Gdx.files.internal("Main_Menu_Screen.jpg")));

        //libGDX documentation on SliderStyle and how it is to be implemented by libGDX devs
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Slider.SliderStyle.html
        generalSliderStyle = new SliderStyle();
        generalSliderStyle.backgroundDown = new TextureRegionDrawable(generalTextButtonDownRegion);
        generalSliderStyle.background = new TextureRegionDrawable(generalTextButtonUpRegion);
        generalSliderStyle.knob = new TextureRegionDrawable(generalTextButtonDownRegion);
        generalSliderStyle.knobDown = new TextureRegionDrawable(generalTextButtonUpRegion);

//        player.setHealthPoints(10);
        this.setScreen(new MainMenu(this));
    }


    /**
     * This function is called by OpenGL to render objects presented in the order defined below and
     * then displaying it to the user as well as acting when the program will take in input and accept
     * that input.
     */
    @Override
    public void render() {

        //Simplifying render thanks to libGDX for their "Extending the Simple Game" Tutorial,
        //Specifically the advanced section on super.render() as well as the following section on the main
        //game screen
        //https://libgdx.com/dev/simple-game-extended/
        super.render();

    }


    /**
     * Sets the stage viewport to the height and width of the window.
     *
     * @param width
     * @param height
     */
    public float width = 1920, height = 1080;

    @Override
    public void resize(int width, int height) {
        stageInstance.getViewport().update(width,height,true);
    }

    /**
     * Disposes of all instances that take space up in memory and require rendering
     * including Scene2D, batch, and textures
     */
    @Override
    public void dispose() {
        stageInstance.dispose();
        batch.dispose();
        shapeRenderer.dispose();
        idleTextureAtlas.dispose();
        moveDownTextureAtlas.dispose();
        moveLeftTextureAtlas.dispose();
        moveRightTextureAtlas.dispose();
        moveUpTextureAtlas.dispose();
        attackDownTextureAtlas.dispose();
    }

    public void resetElapsedTime() { elapsedTime = 0; }

    public void runPlayerAnimation() {
        elapsedTime += Gdx.graphics.getDeltaTime();

        //Passes the elapsed time to the player for using with internal variables
        //Animation code was created by Miller Banford
        player.setElapsedTime(elapsedTime);
        if(currentAnimation.isAnimationFinished(elapsedTime)) currentAnimation = idleAnimation;
        batch.draw(currentAnimation.getKeyFrame(elapsedTime, true), player.playerIcon.getX(), player.playerIcon.getY(),64 ,64);
    }

    public void runMoveDownAnimation() { currentAnimation = moveDownAnimation; }
    public void runMoveUpAnimation() { currentAnimation = moveUpAnimation; }
    public void runMoveLeftAnimation() { currentAnimation = moveLeftAnimation; }
    public void runMoveRightAnimation() { currentAnimation = moveRightAnimation; }
    public void runAttackDownAnimation() { currentAnimation = attackDownAnimation; }

    public void playerMovement() {
        player.playerIcon.addListener(player.playerInput = new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode)
            {
                switch(keycode) {
                    case Input.Keys.UP:
                        player.playerIcon.addAction(Actions.moveTo(player.playerIcon.getX(), player.playerIcon.getY() + MOVE_DISTANCE,1));
                        break;
                    case Input.Keys.DOWN:
                        player.playerIcon.addAction(Actions.moveTo(player.playerIcon.getX(), player.playerIcon.getY() - MOVE_DISTANCE,1));
                        break;
                    case Input.Keys.LEFT:
                        player.playerIcon.addAction(Actions.moveTo(player.playerIcon.getX() - MOVE_DISTANCE, player.playerIcon.getY(),1));
                        break;
                    case Input.Keys.RIGHT:
                        player.playerIcon.addAction(Actions.moveTo(player.playerIcon.getX() + MOVE_DISTANCE, player.playerIcon.getY(),1));
                        break;
                    default:
                        return false;
                }
                player.isTurn = false;
                player.moveSquare.setPosition(player.playerIcon.getX(), player.playerIcon.getY());
                player.interactSquare.setPosition(player.playerIcon.getX() - MOVE_DISTANCE, player.playerIcon.getY() - MOVE_DISTANCE);
                return true;
            }
        });
    }

    /**
     * Allows for objects to get the currentAnimation and utilize it in their code as noted for the Player class
     * Code is based on Miller Banford's code to work on animations.
     * @return - The currentAnimation pointer for loading and working with animations
     */
    public Animation<TextureRegion> getCurrentAnimation() {
        return currentAnimation;
    }
}
