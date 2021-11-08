package com.badlogic.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class CharacterCreation extends ScreenAdapter {

    final BladeAndTomes GAME;

    TextField nameField;
    TextButton[] classSelection;

    /**
     * The constructor for the Character Creation Screen, defining the values for the Text Field and the Text Button.
     * @param game - Variable for the running instance of the game and all variables from the backbone.
     */
    public CharacterCreation(BladeAndTomes game)
    {
        //Instance for the game
        this.GAME = game;

        nameField = new TextField("", GAME.generalTextFieldStyle);
        nameField.setAlignment(1);
        nameField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
            }
        });

        classSelection = new TextButton[] {
          new TextButton("Warrior", GAME.generalTextButtonStyle),
                new TextButton("Cleric", GAME.generalTextButtonStyle),
                new TextButton("Wizard", GAME.generalTextButtonStyle)
        };

    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

}
