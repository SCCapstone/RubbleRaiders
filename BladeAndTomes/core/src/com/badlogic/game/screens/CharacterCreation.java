package com.badlogic.game.screens;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import java.lang.String;

public class CharacterCreation extends ScreenAdapter {

    final BladeAndTomes GAME;

    //TODO: Have indication of selection being made.

    TextField nameField;
    Label[] statFields;
    Label[] secondaryStatFields;
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
        nameField.setY(910, 1);
        GAME.stageInstance.addActor(nameField);

        //Displays for the Physical, Mental, and Social stats done in the same way as Anirudh Oruganti suggested
        statFields = new Label[]{
                new Label("Physical: " + GAME.player.getPhysical(), GAME.generalLabelStyle),
                new Label("Mental: " + GAME.player.getMental(), GAME.generalLabelStyle),
                new Label("Social: " + GAME.player.getSocial(), GAME.generalLabelStyle)
        };
        for(int i=0; i<statFields.length; i++) {
            statFields[i].setAlignment(1);
            statFields[i].setHeight(90);
            statFields[i].setX(780+i*180,1);
            statFields[i].setY(760,1);
            GAME.stageInstance.addActor(statFields[i]);
        }

        //Displays for the secondary stats done in the same way as Anirudh Oruganti suggested
        secondaryStatFields = new Label[]{
                new Label("Acrobatics: " + GAME.player.getAcrobatics(), GAME.generalLabelStyle),
                new Label("Bruteforce: " + GAME.player.getBruteforce(), GAME.generalLabelStyle),
                new Label("Speech: " + GAME.player.getSpeech(), GAME.generalLabelStyle),
                new Label("Barter: " + GAME.player.getBarter(), GAME.generalLabelStyle),
                new Label("Awareness: " + GAME.player.getAwareness(), GAME.generalLabelStyle),
                new Label("Intuition: " + GAME.player.getIntuition(), GAME.generalLabelStyle)
        };
        for(int i=0; i<secondaryStatFields.length; i++) {
            secondaryStatFields[i].setAlignment(1);
            secondaryStatFields[i].setHeight(90);
            secondaryStatFields[i].setX(560+i*180,1);
            secondaryStatFields[i].setY(600,1);
            GAME.stageInstance.addActor(secondaryStatFields[i]);
        }

        //Anirudh Oruganti made a good suggestion as to how objects with
        //similar functions should be initialized, especially buttons.
        //This was modeled off of the code he pushed to MainMenu.java .
        classSelection = new TextButton[] {
          new TextButton("Warrior", GAME.generalTextButtonStyle),
                new TextButton("Cleric", GAME.generalTextButtonStyle),
                new TextButton("Wizard", GAME.generalTextButtonStyle)
        };

        //See above comment
        for(int i=0; i<BladeAndTomes.classes.values().length; i++) {
            classSelection[i].setWidth(160);
            classSelection[i].setHeight(90);
            classSelection[i].setX(780+i*180,1);
            classSelection[i].setY(440,1);
            GAME.stageInstance.addActor(classSelection[i]);
        }

        classSelection[BladeAndTomes.classes.WARRIOR.ordinal()].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                selection = BladeAndTomes.classes.WARRIOR.ordinal();
                GAME.player.setPhysical(10);
                statFields[0].setText("Physical: " + GAME.player.getPhysical());
                GAME.player.setMental(7);
                statFields[1].setText("Mental: " + GAME.player.getMental());
                GAME.player.setSocial(5);
                statFields[2].setText("Social: " + GAME.player.getSocial());
                secondaryStatFields[0].setText("Acrobatics: " + GAME.player.getAcrobatics());
                secondaryStatFields[1].setText("Bruteforce: " + GAME.player.getBruteforce());
                secondaryStatFields[2].setText("Speech: " + GAME.player.getSpeech());
                secondaryStatFields[3].setText("Barter: " + GAME.player.getBarter());
                secondaryStatFields[4].setText("Awareness: " + GAME.player.getAwareness());
                secondaryStatFields[5].setText("Intuition: " + GAME.player.getIntuition());
            }
        });

        classSelection[BladeAndTomes.classes.CLERIC.ordinal()].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                selection = BladeAndTomes.classes.CLERIC.ordinal();
                GAME.player.setPhysical(7);
                statFields[0].setText("Physical: " + GAME.player.getPhysical());
                GAME.player.setMental(5);
                statFields[1].setText("Mental: " + GAME.player.getMental());
                GAME.player.setSocial(10);
                statFields[2].setText("Social: " + GAME.player.getSocial());
                secondaryStatFields[0].setText("Acrobatics: " + GAME.player.getAcrobatics());
                secondaryStatFields[1].setText("Bruteforce: " + GAME.player.getBruteforce());
                secondaryStatFields[2].setText("Speech: " + GAME.player.getSpeech());
                secondaryStatFields[3].setText("Barter: " + GAME.player.getBarter());
                secondaryStatFields[4].setText("Awareness: " + GAME.player.getAwareness());
                secondaryStatFields[5].setText("Intuition: " + GAME.player.getIntuition());
            }
        });

        classSelection[BladeAndTomes.classes.WIZARD.ordinal()].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                selection = BladeAndTomes.classes.WIZARD.ordinal();
                GAME.player.setPhysical(5);
                statFields[0].setText("Physical: " + GAME.player.getPhysical());
                GAME.player.setMental(10);
                statFields[1].setText("Mental: " + GAME.player.getMental());
                GAME.player.setSocial(7);
                statFields[2].setText("Social: " + GAME.player.getSocial());
                secondaryStatFields[0].setText("Acrobatics: " + GAME.player.getAcrobatics());
                secondaryStatFields[1].setText("Bruteforce: " + GAME.player.getBruteforce());
                secondaryStatFields[2].setText("Speech: " + GAME.player.getSpeech());
                secondaryStatFields[3].setText("Barter: " + GAME.player.getBarter());
                secondaryStatFields[4].setText("Awareness: " + GAME.player.getAwareness());
                secondaryStatFields[5].setText("Intuition: " + GAME.player.getIntuition());
            }
        });

        exitButton = new TextButton("Begin your Adventure!!", GAME.generalTextButtonStyle);
        exitButton.setWidth(360);
        exitButton.setHeight(90);
        exitButton.setX(960,1);
        exitButton.setY(120,1);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                dispose();
                GAME.stageInstance.clear();
                GAME.player.setHealthPoints(10);
                GAME.player.setArmorPoints(0);
                GAME.player.setPlayerClass(selection);
                GAME.player.setName(name);
                GAME.player.setMovement(5);
                GAME.setScreen(new Overworld(GAME));
            }
        });
        GAME.stageInstance.addActor(exitButton);
    }

    @Override
    public void render(float delta) {

        //Simplifying render thanks to libGDX for their "Extending the Simple Game" Tutorial,
        //Specifically the advanced section on super.render() as well as the following section on the main
        //game screen
        //https://libgdx.com/dev/simple-game-extended/
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
        GAME.stageInstance.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(GAME.stageInstance);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

}
