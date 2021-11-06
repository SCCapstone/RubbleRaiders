package com.badlogic.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenu extends ScreenAdapter {
    final BladeAndTomes GAME;

    // I KNOW WE PLAN TO HAVE SOME GLOBAL VARIABLES BACKGROUND SHOULD BE ONE, JUST FOR NOW DOING IT
    // BY CLASS SINCE THIS IS THE FIRST STARTING ONE

    Texture background;
    TextButton createCharacterButton;
    TextButton exitButton;

    public MainMenu(BladeAndTomes game) {
        this.GAME = game;
        background = new Texture(Gdx.files.internal("Main_Menu_Screen.jpg"));
        createCharacterButton = new TextButton("New Game", game.generalTextButtonStyle);
        exitButton = new TextButton("Exit Game", game.generalTextButtonStyle);
        GAME.stageInstance.addActor(createCharacterButton);
        GAME.stageInstance.addActor(exitButton);

        createCharacterButton.hit(960, 960, true);
        createCharacterButton.setX(896f);
        createCharacterButton.setWidth(128f);
        createCharacterButton.setHeight(64f);
        createCharacterButton.setY(960f);

        exitButton.setX(896f);
        exitButton.setY(420f);
        exitButton.setWidth(128f);
        exitButton.setHeight(64f);

        createCharacterButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GAME.stageInstance.clear();
                GAME.setScreen(new CharacterCreation(GAME));
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                dispose();
                Gdx.app.exit();
            }
        });


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