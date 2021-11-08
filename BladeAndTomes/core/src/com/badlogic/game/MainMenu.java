package com.badlogic.game;

import Sounds.BackGroundMusic;
import Sounds.ButtonClickSound;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu extends ScreenAdapter {

    // I KNOW WE PLAN TO HAVE SOME GLOBAL VARIABLES BACKGROUND SHOULD BE ONE, JUST FOR NOW DOING IT
    // BY CLASS SINCE THIS IS THE FIRST STARTING ONE

    //Definition of constant for game instance
    final BladeAndTomes GAME;

    //All used imagery for the given examples below.
    Texture background;
    Image backgroundImage;
    Button MainMenuOptions[];

    // Options
    int newGame,characters,settings,exitGame;

    // Option Dimensions and Location
    float optionSpace, optionWidth,optionHeight,optionLocX,optionLocY;
<<<<<<< HEAD

=======
    // BackGroundMusic
    BackGroundMusic _bgmusic;
    ButtonClickSound buttonSound;
>>>>>>> 757ecb8eb29c45dd018e044e6caf1913a01f17ec
//    TextButton createCharacterButton,
//                exitButton,
//                settingsButton,
//                characterSelection;

    /**
     * Constructor for the game, giving the various and
     * @param game - Running instance of the game, holding all top level variables.
     */
    public MainMenu(BladeAndTomes game) {
        this.GAME = game;
        background = new Texture(Gdx.files.internal("Main_Menu_Screen.jpg"));
        backgroundImage = new Image(background);
        GAME.stageInstance.addActor(backgroundImage);
        newGame = 0; characters = 1; settings = 2; exitGame = 3;

//        createCharacterButton = new TextButton("New Game", game.generalTextButtonStyle);
//        characterSelection = new TextButton("Characters", game.generalTextButtonStyle);
//        settingsButton = new TextButton("Settings", game.generalTextButtonStyle);
//        exitButton = new TextButton("Exit Game", game.generalTextButtonStyle);

        optionSpace = 150; optionWidth = 256f; optionHeight = 128f; optionLocX = 900f; optionLocY = 960f;
        MainMenuOptions = new TextButton[]{
                new TextButton("New Game", game.generalTextButtonStyle),
                new TextButton("Characters", game.generalTextButtonStyle),
                new TextButton("Settings", game.generalTextButtonStyle),
                new TextButton("Exit Game", game.generalTextButtonStyle)};

        MainMenuOptions[newGame].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GAME.stageInstance.clear();
                dispose();
                GAME.setScreen(new CharacterCreation(GAME));
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
<<<<<<< HEAD

        Music test = Gdx.audio.newMusic(Gdx.files.internal("test.mp3"));
        test.play();
        test.setLooping(true);
        test.setVolume(1f);
        test.play();
=======
        _bgmusic = new BackGroundMusic();
        _bgmusic.playMusic();

        buttonSound = new ButtonClickSound();
>>>>>>> 757ecb8eb29c45dd018e044e6caf1913a01f17ec
    }

    /**
     * Makes sure that the InputProcessor is reinitialized when the program us unhidden in task bar or not used.
     */
    @Override
    public void show() {

        //Stage Input Processor Model as given by Reiska of StackOverflow
        //https://stackoverflow.com/questions/36819541/androidstudio-libgdx-changelistener-not-working
        Gdx.input.setInputProcessor(GAME.stageInstance);

        //Will be moved somewhere else
        /*new InputAdapter() {
            @Override
            // NOTE WE SHOULD BE USING BUTTONS FOR OUR KEY PRESSES AS BUTTONS REFER TO MOUSE
            public boolean keyDown(int keyRef) {
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    // Will need to add in the dimensions here for game
                }
                return true;
            }
        }*/
    }

    /**
     * This is a continuously called method to render a scene in libGDX through OpenGL
     * @param delta - Change in time between render
     */
    @Override
    public void render(float delta) {
        // I DONT KNOW IF WE PLAN ON MORE TEXTURES FOR THIS SCREEN BUT I DONT THINK SO
        //game.batch.begin();
        //game.batch.draw(background, 0, 0);
        //game.batch.end();

<<<<<<< HEAD
=======
        MainMenuOptions[exitGame].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                dispose();
                Gdx.app.exit();
            }
        });
        if(Gdx.input.justTouched()){
            buttonSound.playClick();
        }
>>>>>>> 757ecb8eb29c45dd018e044e6caf1913a01f17ec
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
        GAME.stageInstance.draw();

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