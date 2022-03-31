package com.badlogic.game.screens;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.lang.String;

public class CharacterCreation extends ScreenAdapter {

    final BladeAndTomes GAME;

    //TODO: Have indication of selection being made.

    Label primaryPoints;
    int points;
    Label pointsMessage;
    Label classMessage;
    TextField nameField;
    TextButton[] statFields;
    Label[] secondaryStatFields;
    TextButton[] classSelection;
    TextButton exitButton;
    Boolean wizard;
    Boolean cleric;
    Boolean warrior;
    TextButton backButton;

    String name;

    int selection;
    /*
    private FitViewport viewport;
    private OrthographicCamera camera;
     */
    SpriteBatch batch;

    /**
     * The constructor for the Character Creation Screen, defining the values for the Text Field and the Text Button.
     * @param game - Variable for the running instance of the game and all variables from the backbone.
     */
    public CharacterCreation(final BladeAndTomes game) {

        //Instance for the game
        this.GAME = game;

        batch = new SpriteBatch();

        points = 9;
        primaryPoints = new Label("Points: "+points, game.generalLabelStyle);
        warrior = false;
        wizard = false;
        cleric = false;


        backButton = new TextButton("Back to Menu", GAME.generalTextButtonStyle);
        backButton.setSize(120f, 65f);
        backButton.setPosition(50, GAME.stageInstance.getHeight()-100);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GAME.stageInstance.clear();
                GAME.setScreen(new MainMenu(GAME));
            }
        });
        GAME.stageInstance.addActor(backButton);
        //Text Field for the Name
        nameField = new TextField("", GAME.generalTextFieldStyle);
        nameField.setAlignment(1);
        nameField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                GAME.player.setName(textField.getText());
                System.out.println(GAME.player.getName());
            }
        });
        nameField.setX(960,1);
        nameField.setY(950, 1);
        GAME.stageInstance.addActor(nameField);

        primaryPoints.setAlignment(1);
        primaryPoints.setX(950, 1);
        primaryPoints.setY(700, 1);
        primaryPoints.setHeight(60);
        primaryPoints.setWidth(150);
        primaryPoints.setFontScale(1.25f);
        primaryPoints.setColor(Color.GOLD);
        GAME.stageInstance.addActor(primaryPoints);

        pointsMessage = new Label("Allocate points to physical, mental, and social", GAME.generalLabelStyle);
        pointsMessage.setX(860, 1);
        pointsMessage.setY(630, 1);
        pointsMessage.setAlignment(1, 1);
        pointsMessage.setHeight(60);
        pointsMessage.setWidth(500);
        pointsMessage.setColor(Color.GOLD);
        pointsMessage.setFontScale(1.25f);
        GAME.stageInstance.addActor(pointsMessage);

        classMessage = new Label("Please select a class", GAME.generalLabelStyle);
        classMessage.setX(880, 1);
        classMessage.setY(875, 1);
        classMessage.setHeight(50);
        classMessage.setWidth(300);
        classMessage.setAlignment(1, 1);
        classMessage.setFontScale(1.25f);
        classMessage.setColor(Color.GOLD);
        GAME.stageInstance.addActor(classMessage);

        //Displays for the Physical, Mental, and Social stats done in the same way as Anirudh Oruganti suggested
        statFields = new TextButton[]{
                new TextButton("Physical: " + GAME.player.getPhysical(), GAME.generalTextButtonStyle),
                new TextButton("Mental: " + GAME.player.getMental(), GAME.generalTextButtonStyle),
                new TextButton("Social: " + GAME.player.getSocial(), GAME.generalTextButtonStyle)
        };

        //PHYSICAL
        statFields[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(points>0) {
                    /*if(warrior == false){
                        if((cleric == true && GAME.player.getSocial()>GAME.player.getPhysical()) ||
                                wizard == true && GAME.player.getMental()>GAME.player.getPhysical()){*/
                            GAME.player.setPhysical(GAME.player.getPhysical() + 1);
                            statFields[0].setText("Physical: " + GAME.player.getPhysical());
                            points--;
                            primaryPoints.setText("Points: " + points);
                            setSecondaryStats();
                      /*  }
                    }*/
                }
            }
        });
        //MENTAL
        statFields[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(points>0) {
                    /*if(wizard == false) {
                        if ((cleric == true && GAME.player.getSocial() > GAME.player.getMental()) ||
                                warrior == true && GAME.player.getPhysical() > GAME.player.getMental()) {*/
                            GAME.player.setMental(GAME.player.getMental() + 1);
                            statFields[1].setText("Mental: " + GAME.player.getMental());
                            points--;
                            primaryPoints.setText("Points: " + points);
                            setSecondaryStats();
                       /* }
                    }*/
                }
            }
        });
        //SOCIAL
        statFields[2].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(points>0) {
                    /*if(cleric == false) {
                        if ((wizard == true && GAME.player.getMental() > GAME.player.getSocial()) ||
                                warrior == true && GAME.player.getPhysical() > GAME.player.getSocial()) {*/
                            GAME.player.setSocial(GAME.player.getSocial() + 1);
                            statFields[2].setText("Social: " + GAME.player.getSocial());
                            points--;
                            primaryPoints.setText("Points: " + points);
                            setSecondaryStats();
/*                        }
                    }*/
                }
            }
        });

        for(int i=0; i<statFields.length; i++) {
            //statFields[i].setAlignment(1);
            statFields[i].setHeight(90);
            statFields[i].setX(780+i*180,1);
            statFields[i].setY(500,1);
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
            secondaryStatFields[i].setY(350,1);
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
            classSelection[i].setWidth(120);
            classSelection[i].setHeight(75);
            classSelection[i].setX(780+i*180,1);
            classSelection[i].setY(750,1);
            GAME.stageInstance.addActor(classSelection[i]);
        }

        classSelection[BladeAndTomes.classes.WARRIOR.ordinal()].addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                selection = BladeAndTomes.classes.WARRIOR.ordinal();
                warrior = true;
                wizard = false;
                cleric = false;
                GAME.player.setMental(0);
                GAME.player.setSocial(0);
                GAME.player.setPhysical(9);
                points = 9;
                classSelection[0].setColor(Color.GRAY);
                classSelection[1].setColor(1f,1f,1f,1f);
                classSelection[2].setColor(1f,1f,1f,1f);
                pointsMessage.setText("Physical must be the strongest stat");
                primaryPoints.setText("Points: "+points);
                //GAME.player.setPhysical(10);
                statFields[0].setText("Physical: " + GAME.player.getPhysical());
                //GAME.player.setMental(7);
                statFields[1].setText("Mental: " + GAME.player.getMental());
                //GAME.player.setSocial(5);
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
                wizard = false;
                cleric = true;
                warrior = false;
                GAME.player.setSocial(9);
                GAME.player.setMental(0);
                GAME.player.setPhysical(0);
                points=9;
                primaryPoints.setText("Points: "+points);
                classSelection[0].setColor(1f,1f,1f,1f);
                classSelection[1].setColor(Color.GRAY);
                classSelection[2].setColor(1f,1f,1f,1f);
                pointsMessage.setText("Social must be the strongest stat");
                //GAME.player.setPhysical(7);
                statFields[0].setText("Physical: " + GAME.player.getPhysical());
                //GAME.player.setMental(5);
                statFields[1].setText("Mental: " + GAME.player.getMental());
                //GAME.player.setSocial(10);
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
                wizard = true;
                cleric = false;
                warrior = false;
                GAME.player.setPhysical(0);
                GAME.player.setMental(9);
                GAME.player.setSocial(0);
                points=9;
                primaryPoints.setText("Points: "+points);
                classSelection[0].setColor(1f,1f,1f,1f);
                classSelection[1].setColor(1f,1f,1f,1f);
                classSelection[2].setColor(Color.GRAY);
                pointsMessage.setText("Mental must be the strongest stat");
                //GAME.player.setPhysical(5);
                statFields[0].setText("Physical: " + GAME.player.getPhysical());
                //GAME.player.setMental(10);
                statFields[1].setText("Mental: " + GAME.player.getMental());
                //GAME.player.setSocial(7);
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
        exitButton.setY(225,1);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                dispose();
                GAME.stageInstance.clear();
                GAME.player.setHealthPoints(10);
                GAME.player.setFullHealth(10);
                GAME.player.setArmorPoints(10);
                GAME.player.setPlayerClass(selection);
                GAME.player.setMovement(5);
                GAME.player.setDefault(false);
                GAME.loadSaveManager.savePlayer(GAME.player, GAME.currentSaveIndex);
                GAME.setScreen(new Overworld(GAME));
            }
        });
        GAME.stageInstance.addActor(exitButton);

    }

    public void setSecondaryStats(){
        /*GAME.player.getAcrobatics();
        GAME.player.getBruteforce();
        GAME.player.getBarter();
        GAME.player.getSpeech();
        GAME.player.getIntuition();
        GAME.player.getAwareness();*/
        secondaryStatFields[0].setText("Acrobatics: " + GAME.player.getAcrobatics());
        secondaryStatFields[1].setText("Bruteforce: " + GAME.player.getBruteforce());
        secondaryStatFields[2].setText("Speech: " + GAME.player.getSpeech());
        secondaryStatFields[3].setText("Barter: " + GAME.player.getBarter());
        secondaryStatFields[4].setText("Awareness: " + GAME.player.getAwareness());
        secondaryStatFields[5].setText("Intuition: " + GAME.player.getIntuition());
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


        /*
        // Need a getter for the camera class
        batch.setTransformMatrix(GAME.getCamera().view);
        batch.setProjectionMatrix(GAME.getCamera().projection);
         */
    }

    public void resize(int width, int height) {
        GAME.stageInstance.getViewport().update(width, height, true);
        /*
        viewport.update(width, height);
        camera.update();
         */
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
