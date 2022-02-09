package com.badlogic.game.screens;

import Sounds.ButtonClickSound;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.creatures.Item;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fasterxml.jackson.xml.XmlMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainMenu extends ScreenAdapter {

    //Definition of constant for game instance
    final BladeAndTomes GAME;

    //All used imagery for the given examples below.
    Texture background;
    Image backgroundImage;
    Button MainMenuOptions[];
    TextButton settingsQuitOption;
    Window settingsWindow;
    Slider settingsMusicSlider;
    Label settingsMusicLabel;
    TextField interactKey;
    Label interactLabel;
    TextField attackKey;
    Label attackLabel;
    TextField inventoryKey;
    Label inventoryLabel;

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        torchAtlas.dispose();
    }

    Window loadWindow;

    // Options
    int newGame,characters,settings,exitGame;

    // Option Dimensions and Location
    float optionSpace, optionWidth,optionHeight,optionLocX,optionLocY;

    //BackGroundMusic
    //BackGroundMusic _bgmusic;
    ButtonClickSound buttonSound;
    private SpriteBatch batch;
    private TextureAtlas torchAtlas;
    private Animation<TextureRegion> animation;
    private float timePassed;
    /**
     * Constructor for the game, giving the various and
     * @param game - Running instance of the game, holding all top level variables.
     */
    public MainMenu(final BladeAndTomes game) {
        this.GAME = game;
        batch = new SpriteBatch();
        torchAtlas = new TextureAtlas(Gdx.files.internal("AnimationFiles/Torch.atlas"));
        animation = new Animation<TextureRegion>(1/6f,torchAtlas.getRegions());
////        background = new Texture(Gdx.files.internal("Main_Menu_Screen.jpg"));
//        backgroundImage = new Image(background);
//        GAME.stageInstance.addActor(backgroundImage);
        newGame = 0; characters = 1; settings = 2; exitGame = 3;

        //TODO: Move menu sounds to backbone layer
        //_bgmusic = new BackGroundMusic();
        //_bgmusic.playMusic();

        buttonSound = new ButtonClickSound();

        settingsMusicLabel = new Label("Music Volume", GAME.generalLabelStyle);

        //keybinding for Interact on settings page
        interactLabel = new Label("Interact Key", GAME.generalLabelStyle);
        interactKey = new TextField("", GAME.generalTextFieldStyle);
        interactKey.setMaxLength(1);
        interactKey.setAlignment(1);
        interactKey.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
            }
        });
        //keybinding for Attack on settings page
        attackLabel = new Label("Attack Key", GAME.generalLabelStyle);
        attackKey = new TextField("", GAME.generalTextFieldStyle);
        attackKey.setMaxLength(1);
        attackKey.setAlignment(1);
        attackKey.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
            }
        });
        //keybinding for Inventory on settings page
        inventoryLabel = new Label("Inventory Label", GAME.generalLabelStyle);
        inventoryKey = new TextField("", GAME.generalTextFieldStyle);
        inventoryKey.setMaxLength(1);
        inventoryKey.setAlignment(1);
        inventoryKey.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
            }
        });

        optionSpace = 150; optionWidth = 256f; optionHeight = 128f; optionLocX = 800f; optionLocY = 760f;
        MainMenuOptions = new TextButton[]{
                new TextButton("New Game", game.generalTextButtonStyle),
                new TextButton("Characters", game.generalTextButtonStyle),
                new TextButton("Settings", game.generalTextButtonStyle),
                new TextButton("Exit Game", game.generalTextButtonStyle)};

        //Helpful references for how Windows work in libGDX by libGDX team
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Window.html
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Window.WindowStyle.html
        settingsWindow = new Window("Settings", GAME.generalWindowStyle);
        settingsWindow.getTitleLabel().setAlignment(1);
        settingsWindow.getTitleLabel().setFontScale(2f);
        settingsWindow.setPosition(600, 250);
        settingsWindow.setKeepWithinStage(true);
        settingsWindow.setHeight(700);
        settingsWindow.setWidth(700);
        settingsWindow.setMovable(true);

        //libGDX documentation on how Slider works as well as UseOf.org example by libGDX on application of sliders by libGDX team
        //and curators of UseOf.org
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Slider.html#isOver--
        //http://useof.org/java-open-source/com.badlogic.gdx.scenes.scene2d.ui.Slider
        settingsMusicSlider = new Slider(0f, 100f, 1f, false ,GAME.generalSliderStyle);
        settingsMusicSlider.setButton(0);
        settingsMusicSlider.setVisualPercent(GAME._bgmusic.getVolume());
        settingsMusicSlider.setWidth(500);
        //sets the width of the slider
        settingsMusicSlider.getStyle().knob.setMinWidth(50);
        settingsMusicSlider.getStyle().knobDown.setMinWidth(50);

        //sets the music volume based on slider value - NOT WORKING YET
        settingsMusicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GAME._bgmusic.setVolume(settingsMusicSlider.getVisualValue());
            }
        });

        settingsQuitOption = new TextButton("Exit Settings", GAME.generalTextButtonStyle);
        settingsQuitOption.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                settingsWindow.clear();
                settingsWindow.remove();
            }
        });

        //Helpful references for how Windows work in libGDX by libGDX team. The formatting for the style and window
        //follow the Jave documentation.
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Window.html
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Window.WindowStyle.html
        loadWindow = new Window("Load Game", GAME.generalWindowStyle);

        MainMenuOptions[newGame].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                for(int i = 0; i< MainMenuOptions.length; i++) {
                    MainMenuOptions[i].remove();
                }
                dispose();
                GAME.setScreen(new CharacterCreation(GAME));
            }
        });

        final TextButton loadQuitOption = new TextButton("Cancel", GAME.generalTextButtonStyle);

        loadQuitOption.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                loadWindow.remove();
            }
        });

        //Helpful references for how Windows work in libGDX by libGDX team. The formatting for the style and window
        //follow the Jave documentation.
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Window.html
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Window.WindowStyle.html
        MainMenuOptions[characters].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                //GAME.stageInstance.addActor(loadWindow);
                //settingsWindow.add(loadQuitOption).top();
            }
        });

        //The reference sheets for the documentation is made by the libGDX team and used extensively
        //as a reference for different functions
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Slider.html#isOver--
        MainMenuOptions[settings].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GAME.stageInstance.addActor(settingsWindow);
                settingsWindow.setDebug(true);

                settingsWindow.row();
                settingsWindow.add(settingsMusicLabel);//.center();

                //Example 5 on the website UseOf details how to properly size the Slider and where this code comes from.
                //All thanks go to libGDX for providing the example as well the curators of UseOf.org
                //http://useof.org/java-open-source/com.badlogic.gdx.scenes.scene2d.ui.Slider
                settingsWindow.add(settingsMusicSlider).top().minWidth(300).colspan(3);
                settingsWindow.row();

                //add keybind changes
                settingsWindow.add(interactLabel).left();
                settingsWindow.add(interactKey).left();
                settingsWindow.row();
                settingsWindow.add(attackLabel).left();
                settingsWindow.add(attackKey).left();
                settingsWindow.row();
                settingsWindow.add(inventoryLabel).left();
                settingsWindow.add(inventoryKey).left();
                settingsWindow.row();

                settingsWindow.add(settingsQuitOption).center();
            }
        });

        MainMenuOptions[exitGame].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                dispose();
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
        GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
        GAME.stageInstance.draw();
        // Torch Animation, Source: https://www.youtube.com/watch?v=vjgdX95HVrM
        batch.begin();
        timePassed +=Gdx.graphics.getDeltaTime();
        batch.draw(animation.getKeyFrame(timePassed,true),1240,600);
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
