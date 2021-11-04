package com.badlogic.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class MainMenu extends ScreenAdapter {
    BladeAndTomes game;

    // I KNOW WE PLAN TO HAVE SOME GLOBABL VARIABLES BACKGROUND SHOULD BE ONE, JUST FOR NOW DOING IT
    // BY CLASS SINCE THIS IS THE FIRST STARTING ONE

    Texture background;

    public MainMenu(BladeAndTomes game) {
        this.game = game;
        background = new Texture(Gdx.files.internal("Main_Menu_Screen"));
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
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.batch.end();
        // SHOULD RENDER IN A MOUSE OR SOME TYPE OF CURSOR FOR THE PERSON
    }

    // this needs to be on every page, tells our screens not to take input when not on them
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}