package com.badlogic.game.screens;

import Keyboard_Mouse_Controls.MainMenuControls;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.creatures.Item;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.fasterxml.jackson.xml.XmlMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.badlogic.gdx.utils.Align.center;
import static com.badlogic.gdx.utils.Align.right;

public class Settings extends ScreenAdapter {

    final BladeAndTomes GAME;

    String moUp;
    String moDown;
    String moLeft;
    String moRight;
    String interAction;
    String openMenu;

    MainMenuControls menuCont = new MainMenuControls(null, null,
            null, null, null, null);

    Texture background;
    Image backgroundImage;
    TextButton settingsQuitOption;
    Slider settingsMusicSlider;
    Label settingsMusicLabel;
    TextField upKey;
    Label upLabel;
    TextField downKey;
    Label downLabel;
    TextField leftKey;
    Label leftLabel;
    TextField rightKey;
    Label rightLabel;
    TextField interactKey;
    Label interactLabel;
    TextField menuKey;
    Label menuLabel;
    Table volumeTable;
    Table table;

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        GAME.stageInstance.clear();
    }

    // Option Dimensions and Location
    float optionSpace, optionWidth,optionHeight,optionLocX,optionLocY;

    private SpriteBatch batch;

    public Settings(final BladeAndTomes game){
        this.GAME = game;
        batch = new SpriteBatch();

        optionSpace = 150; optionWidth = 256f; optionHeight = 128f; optionLocX = 800f; optionLocY = 760f;

        settingsMusicLabel = new Label("Music Volume",GAME.generalLabelStyle);
        settingsMusicLabel.setAlignment(1,2);

        //keybinding for Interact on settings page
        upLabel = new Label("Move Up", GAME.generalLabelStyle);
        upLabel.setAlignment(1,2);
        upKey = new TextField(menuCont.getMoveUp(), GAME.generalTextFieldStyle);
        upKey.setMaxLength(1);
        upKey.setAlignment(1);
        upKey.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
            }
        });
        //keybinding for Attack on settings page
        downLabel = new Label("Move Down", GAME.generalLabelStyle);
        downLabel.setAlignment(1,2);
        downKey = new TextField(menuCont.getMoveDown(), GAME.generalTextFieldStyle);
        downKey.setMaxLength(1);
        downKey.setAlignment(1);
        downKey.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
            }
        });
        //keybinding for Inventory on settings page
        leftLabel = new Label("Move Left", GAME.generalLabelStyle);
        leftLabel.setAlignment(1,2);
        leftKey = new TextField("", GAME.generalTextFieldStyle);
        leftKey.setMaxLength(1);
        leftKey.setAlignment(1);
        leftKey.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
            }
        });
        //keybinding for right movement on settings page
        rightLabel = new Label("Move Right", GAME.generalLabelStyle);
        rightLabel.setAlignment(1,2);
        rightKey = new TextField("", GAME.generalTextFieldStyle);
        rightKey.setMaxLength(1);
        rightKey.setAlignment(1);
        rightKey.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
            }
        });
        //keybinding for Interact on settings page
        interactLabel = new Label("Interact", GAME.generalLabelStyle);
        interactLabel.setAlignment(1,2);
        interactKey = new TextField("", GAME.generalTextFieldStyle);
        interactKey.setMaxLength(1);
        interactKey.setAlignment(1);
        interactKey.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
            }
        });
        //keybinding for Menu on settings page
        menuLabel = new Label("Menu", GAME.generalLabelStyle);
        menuLabel.setAlignment(1,2);
        menuKey = new TextField("", GAME.generalTextFieldStyle);
        menuKey.setMaxLength(1);
        menuKey.setAlignment(1);
        menuKey.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
            }
        });
        //TODO: cursor setting?

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
                //Back to main menu screen
                dispose();
                GAME.setScreen(new MainMenu(game));
            }
        });
    }

    /**
     * Makes sure that the InputProcessor is reinitialized when the program us unhidden in task bar or not used.
     */
    @Override
    public void show() {

        //Stage Input Processor Model as given by Reiska of StackOverflow
        //https://stackoverflow.com/questions/36819541/androidstudio-libgdx-changelistener-not-working
        Gdx.input.setInputProcessor(GAME.stageInstance);

        table = new Table();
        table.defaults();
        table.setBounds(750,400,500,GAME.stageInstance.getHeight());
        table.setSize(GAME.stageInstance.getWidth()*0.2f,GAME.stageInstance.getHeight()*0.2f);
        table.add(settingsMusicLabel);
        table.add(settingsMusicSlider).colspan(4).width(table.getWidth()+43);
        table.row().padTop(10f);
        table.add(upLabel);
        table.add(upKey).left();
        table.add(downLabel);
        table.add(downKey).left();
        table.row();
        table.add(leftLabel);
        table.add(leftKey).left();
        table.add(rightLabel);
        table.add(rightKey).left();
        table.row();
        table.add(interactLabel);
        table.add(interactKey).left();
        table.add(menuLabel);
        table.add(menuKey).left();
        table.row().padTop(10f);
        table.add(settingsQuitOption).center().colspan(4).width(table.getWidth());

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
        //Gdx.gl.glClearColor(20,50,30,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
        GAME.stageInstance.addActor(table);
        GAME.stageInstance.draw();
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
                String xmlString = xmlMapper.writeValueAsString(new MainMenuControls("w", "s", "a", "d", "x", "escape"));

                File xmlOutput = new File("Settings.xml");
                FileWriter fileWriter = new FileWriter(xmlOutput);
                fileWriter.write(xmlString);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static void settingsDeserialize() {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            String readSettingsFile = new String(Files.readAllBytes(Paths.get("Settings.xml")));
            MainMenuControls deserializeMenu = xmlMapper.readValue(readSettingsFile, MainMenuControls.class);

            deserializeMenu.getMoveUp();
            deserializeMenu.getMoveDown();
            deserializeMenu.getMoveLeft();
            deserializeMenu.getMoveRight();
            deserializeMenu.getInteractAction();
            deserializeMenu.getOpenPauseMenu();
            /*
            System.out.println("Rebind Settings");
            System.out.println("Move up: " + deserializeMenu);
            System.out.println("Move down: " + deserializeMenu);
            System.out.println("Move left: " + deserializeMenu);
            System.out.println("Move right: " + deserializeMenu);
            System.out.println("Interact: " + deserializeMenu);
            System.out.println("Menu: " + deserializeMenu);
            System.out.println("Cursor: " + deserializeMenu);
             */
        } catch (IOException e) {
            // for now use auto generated
            e.printStackTrace();
            return;
        }
    }

}
