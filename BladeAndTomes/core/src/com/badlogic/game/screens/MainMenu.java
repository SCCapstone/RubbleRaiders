package com.badlogic.game.screens;

import Keyboard_Mouse_Controls.SaveLoadGame;
import LoadAndSave.LoadSaveManager;
import Sounds.ButtonClickSound;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.creatures.Item;
import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.fasterxml.jackson.xml.XmlMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainMenu extends ScreenAdapter {

    //Definition of constant for game instance
    final BladeAndTomes GAME;

    private String[] names;
    private String[] saveTime;

    private TiledMap overWorldMap;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;

    //All used imagery for the given examples below.
    Texture background;
    Image backgroundImage;
    Button MainMenuOptions[];
    boolean isTutorial;

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        torchAtlas.dispose();
    }


    // Options
    int newGame,tutorial,settings,exitGame;

    // Option Dimensions and Location
    float optionSpace, optionWidth,optionHeight,optionLocX,optionLocY;

    //BackGroundMusic
    //BackGroundMusic _bgmusic;
    ButtonClickSound buttonSound;
    private SpriteBatch batch;
    private TextureAtlas torchAtlas;
    private Animation<TextureRegion> animation;
    private float timePassed;
    private final AssetManager manager = new AssetManager();
    /**
     * Constructor for the game, giving the various and
     * @param game - Running instance of the game, holding all top level variables.
     */
    public MainMenu(final BladeAndTomes game) {

        this.GAME = game;
        game.player = new Player();
        game.player = game.loadSaveManager.loadPlayer(game.currentSaveIndex);


        batch = new SpriteBatch();
        overWorldMap = new TmxMapLoader().load("Maps/Overworld_Revamped_Two.tmx");
        manager.load("AnimationFiles/Torch.atlas", TextureAtlas.class);
        manager.finishLoading();
        torchAtlas = manager.get("AnimationFiles/Torch.atlas");
        animation = new Animation<TextureRegion>(1/6f,torchAtlas.getRegions());
////        background = new Texture(Gdx.files.internal("Main_Menu_Screen.jpg"));
//        backgroundImage = new Image(background);
//        GAME.stageInstance.addActor(backgroundImage);
        newGame = 0; tutorial = 1; settings = 2; exitGame = 3;
        isTutorial = false;

        //TODO: Move menu sounds to backbone layer
        //_bgmusic = new BackGroundMusic();
        //_bgmusic.playMusic();

        buttonSound = new ButtonClickSound();

        optionSpace = 150; optionWidth = 256f; optionHeight = 128f; optionLocX = 800f; optionLocY = 760f;
        MainMenuOptions = new TextButton[]{
                new TextButton("Play", game.generalTextButtonStyle),
                new TextButton("Tutorial", game.generalTextButtonStyle),
                new TextButton("Settings", game.generalTextButtonStyle),
                new TextButton("Exit Game", game.generalTextButtonStyle)};


        //Helpful references for how Windows work in libGDX by libGDX team. The formatting for the style and window
        //follow the Jave documentation.
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Window.html
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Window.WindowStyle.html


        MainMenuOptions[newGame].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                for(int i = 0; i< MainMenuOptions.length; i++) {
                    MainMenuOptions[i].remove();
                }
                GAME.stageInstance.clear();
                dispose();
                GAME.setScreen(new GameSelection(GAME));
            }
        });

        final TextButton loadQuitOption = new TextButton("Cancel", GAME.generalTextButtonStyle);

        loadQuitOption.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
            }
        });

        //Helpful references for how Windows work in libGDX by libGDX team. The formatting for the style and window
        //follow the Jave documentation.
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Window.html
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Window.WindowStyle.html
        MainMenuOptions[tutorial].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GAME.stageInstance.clear();
                isTutorial = true;
                GAME.setScreen(new Overworld(GAME));

            }
        });

        //The reference sheets for the documentation is made by the libGDX team and used extensively
        //as a reference for different functions
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Slider.html#isOver--
        MainMenuOptions[settings].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GAME.stageInstance.clear();
                GAME.setScreen(new Settings(GAME));
            }
        });

        MainMenuOptions[exitGame].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GAME.stageInstance.dispose();
                Gdx.app.exit();
            }
        });

        for(int i = 0; i <MainMenuOptions.length;++i){
            MainMenuOptions[i].hit(optionLocX, optionLocY-i*optionSpace, true);
            MainMenuOptions[i].setX(optionLocX);
            MainMenuOptions[i].setY(optionLocY-i*optionSpace);
            MainMenuOptions[i].setWidth(optionWidth);
            MainMenuOptions[i].setHeight(optionHeight);
            GAME.stageInstance.addActor(MainMenuOptions[i]);
        }

    }

    /**
     * Makes sure that the InputProcessor is reinitialized when the program us unhidden in task bar or not used.
     */
    @Override
    public void show() {
        overWorldMap = new TmxMapLoader().load("Maps/Overworld_Revamped_Two.tmx");
        renderer = new OrthogonalTiledMapRenderer(overWorldMap);
        camera = new OrthographicCamera();
        //Stage Input Processor Model as given by Reiska of StackOverflow
        //https://stackoverflow.com/questions/36819541/androidstudio-libgdx-changelistener-not-working
        Gdx.input.setInputProcessor(GAME.stageInstance);
    }

    /**
     * This is a continuously called method to render a scene in libGDX through OpenGL
     * @param delta - Change in time between render
     */
    @Override
    public void render(float delta) {

        //Simplifying render thanks to libGDX for their "Extending the Simple Game" Tutorial,
        //Specifically the advanced section on super.render() as well as the following section on the main
        //game screen
        //https://libgdx.com/dev/simple-game-extended/
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(camera);
        renderer.render();
        GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
        GAME.stageInstance.draw();
        // Torch Animation, Source: https://www.youtube.com/watch?v=vjgdX95HVrM
        batch.begin();
        timePassed +=Gdx.graphics.getDeltaTime();
        batch.draw(animation.getKeyFrame(timePassed,true),width,600);
        batch.draw(animation.getKeyFrame(timePassed,true),490,600);

        batch.end();

        if(Gdx.input.justTouched()){
            buttonSound.playClick();
        }

        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
        //GAME.stageInstance.draw();

        // SHOULD RENDER IN A MOUSE OR SOME TYPE OF CURSOR FOR THE PERSON
    }

    float width = 0, height = 0;
    @Override
    public void resize(int width, int height) {
        this.width =width;
        this.height=height;
        camera.viewportHeight = height;
        camera.viewportWidth = width;
        camera.translate(GAME.stageInstance.getWidth() / 2, GAME.stageInstance.getHeight() / 2);
        camera.update();

        GAME.stageInstance.getViewport().update(width, height, true);
    }

    /**
     * This needs to be on every page, as it makes sure our screens does not take input
     * when not selected on them.
     */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    public static void settingsSerialize() {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            try {
                String xmlString = xmlMapper.writeValueAsString(new Item());

                File xmlOutput = new File("Settings.xml");
                FileWriter fileWriter = new FileWriter(xmlOutput);
                fileWriter.write(xmlString);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void settingsDeserialize() {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            String readSettingsFile = new String(Files.readAllBytes(Paths.get("Settings.xml")));
            MainMenu deserializeMenu = xmlMapper.readValue(readSettingsFile, MainMenu.class);

            System.out.println("Rebind Settings");
            System.out.println("Move up: " + deserializeMenu);
            System.out.println("Move down: " + deserializeMenu);
            System.out.println("Move left: " + deserializeMenu);
            System.out.println("Move right: " + deserializeMenu);
            System.out.println("Interact: " + deserializeMenu);
            System.out.println("Menu: " + deserializeMenu);
            System.out.println("Cursor: " + deserializeMenu);
        } catch (IOException e) {
            // for now use auto generated
            e.printStackTrace();
        }
    }

}
