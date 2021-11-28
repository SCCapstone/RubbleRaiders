package com.badlogic.game;

import com.badlogic.game.creatures.Inventory;
import com.badlogic.game.creatures.Player;
import com.badlogic.game.screens.MainMenu;
import com.badlogic.game.screens.Overworld;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class BladeAndTomes extends Game {
    ShapeRenderer shapeRenderer;
    public SpriteBatch batch;
    BitmapFont font;
    Texture image;
    public Stage stageInstance;
    public final float WINDOWWIDTH = 1920, WINDOWHIGHT = 1280;


    public enum classes {WARRIOR, CLERIC, WIZARD}

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

    public Player player;
    public Image playerIcon;
    public Inventory inventory;

    public final int MOVE_DISTANCE = 64;

    /**
     * Creates and initializes all objects and variables for the main project before moving the program to
     * the first screen.
     */
    @Override
    public void create() {

        player = new Player();
        inventory = new Inventory();


        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();

        //Sets Scene2D instance
        stageInstance = new Stage(new ScreenViewport());

//Sets upstate and downstate textures for texture Buttons
        generalTextButtonUpState = new Texture(Gdx.files.internal("Text_Button_Up_State.jpg"));
        generalTextButtonDownState = new Texture(Gdx.files.internal("Text_Button_Down_State.jpg"));
        inventoryTextButtonState = new Texture(Gdx.files.internal("inventorySlot.png"));
        inventoryBase1 = new Texture(Gdx.files.internal("inventoryBaseImage.png"));
        inventoryBase2 = new Texture(Gdx.files.internal("inventoryBaseImage2.png"));



        //Sets up the region to be used
        generalTextButtonUpRegion = new TextureRegion(generalTextButtonUpState);
        generalTextButtonDownRegion = new TextureRegion(generalTextButtonDownState);
        inventoryTextButtonRegion = new TextureRegion(inventoryTextButtonState);

        //Defines the style to be used in the text buttons
        generalTextButtonStyle = new TextButtonStyle ();
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
        generalTextFieldStyle.fontColor = new Color(0f,0f,0f,1f);
        generalTextFieldStyle.background = new TextureRegionDrawable(generalTextButtonUpRegion);

        //Defines the style to be used for the Label
        generalLabelStyle = new LabelStyle();
        generalLabelStyle.font = font;
        generalLabelStyle.background = new TextureRegionDrawable(generalTextButtonUpRegion);
        generalLabelStyle.fontColor = new Color(0f,0f,0f,1f);

        HealthLabelStyle = new LabelStyle();
        HealthLabelStyle.font = font;
        HealthLabelStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("healthBar.jpg"))));
        HealthLabelStyle.fontColor = new Color(100f,0f,0f,1f);

        BaseLabelStyle1 = new LabelStyle();
        BaseLabelStyle1.font = font;
        BaseLabelStyle1.background = new TextureRegionDrawable(new TextureRegion(inventoryTextButtonState));
        BaseLabelStyle1.fontColor = new Color(0f,0f,0f,10f);

        BaseLabelStyle2 = new LabelStyle();
        BaseLabelStyle2.font = font;
        BaseLabelStyle2.background = new TextureRegionDrawable(new TextureRegion(inventoryBase2));
        BaseLabelStyle2.fontColor = new Color(0f,0f,0f,10f);

        //libGDX documentation on WindowStyle and how it is to be implemented by libGDX devs
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Window.WindowStyle.html
        generalWindowStyle = new WindowStyle();
        generalWindowStyle.titleFont = font;
        generalWindowStyle.titleFontColor = new Color(0f,0f,0f,1f);
        generalWindowStyle.stageBackground = new TextureRegionDrawable(new Texture(Gdx.files.internal("Main_Menu_Screen.jpg")));

        //libGDX documentation on SliderStyle and how it is to be implemented by libGDX devs
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Slider.SliderStyle.html
        generalSliderStyle = new SliderStyle();
        generalSliderStyle.backgroundDown = new TextureRegionDrawable(generalTextButtonDownRegion);
        generalSliderStyle.background = new TextureRegionDrawable(generalTextButtonUpRegion);
        generalSliderStyle.knob = new TextureRegionDrawable(generalTextButtonDownRegion);
        generalSliderStyle.knobDown = new TextureRegionDrawable(generalTextButtonUpRegion);


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
    @Override
    public void resize(int width, int height) {
        stageInstance.getViewport().update(width, height, true);
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
    }
}
