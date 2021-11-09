package com.badlogic.game.screens;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.Input;
import java.lang.String;

public class CharacterCreation extends ScreenAdapter {

    final BladeAndTomes GAME;

    //TODO: Make this public class in backbone or Player?
    enum classes {WARRIOR, CLERIC ,WIZARD}

    TextField nameField;
    TextButton[] classSelection;
    TextButton exitButton;

    String name;

    int selection;

    /**
     * The constructor for the Character Creation Screen, defining the values for the Text Field and the Text Button.
     * @param game - Variable for the running instance of the game and all variables from the backbone.
     */
    public CharacterCreation(final BladeAndTomes game) {

        //Instance for the game
        this.GAME = game;

        //Text Field for the Name
        nameField = new TextField("", GAME.generalTextFieldStyle);
        nameField.setAlignment(1);
        nameField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
            }
        });
        nameField.setX(960,1);
        nameField.setY(960, 1);

        //Anirudh Oruganti made a good suggestion as to how objects with
        //similar functions should be initialized, especially buttons.
        //This was modeled off of the code he pushed to MainMenu.java .
        classSelection = new TextButton[] {
          new TextButton("Warrior", GAME.generalTextButtonStyle),
                new TextButton("Cleric", GAME.generalTextButtonStyle),
                new TextButton("Wizard", GAME.generalTextButtonStyle)
        };

        //See above comment
        for(int i=0; i<classes.values().length; i++) {
            classSelection[i].setWidth(360);
            classSelection[i].setHeight(90);
            classSelection[i].setX(360+i*180,1);
            classSelection[i].setY(640,1);
            GAME.stageInstance.addActor(classSelection[i]);
        }

        classSelection[classes.WARRIOR.ordinal()].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                selection = classes.WARRIOR.ordinal();
            }
        });

        classSelection[classes.CLERIC.ordinal()].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                selection = classes.CLERIC.ordinal();
            }
        });

        classSelection[classes.WIZARD.ordinal()].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                selection = classes.WIZARD.ordinal();
            }
        });

        exitButton = new TextButton("Begin your Adventure!!", GAME.generalTextButtonStyle);
        exitButton.setWidth(360);
        exitButton.setHeight(90);
        exitButton.setX(960,1);
        exitButton.setY(320,1);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                dispose();
                GAME.stageInstance.clear();
                GAME.player.setHealthPoints(10);
                GAME.player.setArmorPoints(0);
                GAME.player.setPlayerClass(selection);
                GAME.player.setName(name);
            }
        });
        GAME.stageInstance.addActor(exitButton);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
        {
            name = nameField.getText();
        }
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

    @Override
    public void dispose() {
            super.dispose();
    }

}
