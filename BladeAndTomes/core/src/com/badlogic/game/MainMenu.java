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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenu extends ScreenAdapter {
    final BladeAndTomes GAME;

    // I KNOW WE PLAN TO HAVE SOME GLOBAL VARIABLES BACKGROUND SHOULD BE ONE, JUST FOR NOW DOING IT
    // BY CLASS SINCE THIS IS THE FIRST STARTING ONE

    Texture background;
    Button MainMenuOptions[];
    // Options
    int newGame,characters,settings,exitGame;
    // Option Dimensions and Location
    float optionSpace, optionWidth,optionHeight,optionLocX,optionLocY;
    // BackGroundMusic
    BackGroundMusic _bgmusic;
    ButtonClickSound buttonSound;
//    TextButton createCharacterButton,
//                exitButton,
//                settingsButton,
//                characterSelection;

    public MainMenu(BladeAndTomes game) {
        this.GAME = game;
        background = new Texture(Gdx.files.internal("Main_Menu_Screen.jpg"));
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


        for(int i = 0; i <MainMenuOptions.length;++i){
            MainMenuOptions[i].hit(optionLocX, optionLocY-i*optionSpace, true);
            MainMenuOptions[i].setX(optionLocX);
            MainMenuOptions[i].setY(optionLocY-i*optionSpace);
            MainMenuOptions[i].setWidth(optionWidth);
            MainMenuOptions[i].setHeight(optionHeight);
            GAME.stageInstance.addActor(MainMenuOptions[i]);

        }
        _bgmusic = new BackGroundMusic();
        _bgmusic.playMusic();

        buttonSound = new ButtonClickSound();
    }




    @Override
    public void show() {


        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            // NOTE WE SHOULD BE USING BUTTONS FOR OUR KEY PRESSES AS BUTTONS REFER TO MOUSE

            public boolean keyDown(int keyRef) {
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    // Will need to add in the dimensions here for game
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        // I DONT KNOW IF WE PLAN ON MORE TEXTURES FOR THIS SCREEN BUT I DONT THINK SO
        //game.batch.begin();
        //game.batch.draw(background, 0, 0);
        //game.batch.end();

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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
        GAME.stageInstance.draw();

        // SHOULD RENDER IN A MOUSE OR SOME TYPE OF CURSOR FOR THE PERSON
    }

    // this needs to be on every page, tells our screens not to take input when not on them
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}