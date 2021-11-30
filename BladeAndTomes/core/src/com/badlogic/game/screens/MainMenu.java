package com.badlogic.game.screens;

import Sounds.BackGroundMusic;
import Sounds.ButtonClickSound;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Input;
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

    // BackGroundMusic
    BackGroundMusic _bgmusic;
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
        _bgmusic = new BackGroundMusic();
        _bgmusic.playMusic();

        buttonSound = new ButtonClickSound();

        settingsMusicLabel = new Label("Music Volume", GAME.generalLabelStyle);

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
        settingsWindow.setPosition(760, 590);
        settingsWindow.setKeepWithinStage(true);
        settingsWindow.setHeight(400);
        settingsWindow.setWidth(600);
        settingsWindow.setMovable(true);

        //libGDX documentation on how Slider works as well as UseOf.org example by libGDX on application of sliders by libGDX team
        //and curators of UseOf.org
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Slider.html#isOver--
        //http://useof.org/java-open-source/com.badlogic.gdx.scenes.scene2d.ui.Slider
        settingsMusicSlider = new Slider(0f, 100f, 1f, false ,GAME.generalSliderStyle);
        settingsMusicSlider.setButton(1);
        settingsMusicSlider.setVisualPercent(_bgmusic.getVolume());
        settingsMusicSlider.setWidth(500);

        settingsMusicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                _bgmusic.setVolume(settingsMusicSlider.getVisualValue());
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
                GAME.stageInstance.addActor(loadWindow);
                settingsWindow.add(loadQuitOption).top();
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

                settingsWindow.add();

                settingsWindow.row();
                settingsWindow.add(settingsMusicLabel).center();

                //Example 5 on the website UseOf details how to properly size the Slider and where this code comes from.
                //All thanks go to libGDX for providing the example as well the curators of UseOf.org
                //http://useof.org/java-open-source/com.badlogic.gdx.scenes.scene2d.ui.Slider
                settingsWindow.add(settingsMusicSlider).top().minWidth(100).colspan(3);
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
        batch.draw(animation.getKeyFrame(timePassed,true),1100,600);
        batch.draw(animation.getKeyFrame(timePassed,true),350,600);

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
}
