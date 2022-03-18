package com.badlogic.game.screens;

import Keyboard_Mouse_Controls.MainMenuControls;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.fasterxml.jackson.xml.XmlMapper;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;

public class Settings extends ScreenAdapter {

    final BladeAndTomes GAME;

    /*String moUp;
    String moDown;
    String moLeft;
    String moRight;
    String interAction;
    String openMenu;*/


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
    TextField tradeKey;
    Label tradeLabel;
    TextField menuKey;
    Label menuLabel;
    TextField inventoryKey;
    Label inventoryLabel;
    TextField fightKey;
    Label fightLabel;
    Table volumeTable;
    Table table;


    // Option Dimensions and Location
    float optionSpace, optionWidth,optionHeight,optionLocX,optionLocY;

    private SpriteBatch batch;
    private MainMenuControls menuCont;
    public Settings(final BladeAndTomes game){
        this.GAME = game;
        batch = new SpriteBatch();
        menuCont = game.controls;

        optionSpace = 150; optionWidth = 256f; optionHeight = 128f; optionLocX = 800f; optionLocY = 760f;

        settingsMusicLabel = new Label("Music Volume",GAME.generalLabelStyle);
        settingsMusicLabel.setAlignment(1,2);
        //keybinding for Interact on settings page
        upLabel = new Label("Move Up", GAME.generalLabelStyle);
        upLabel.setAlignment(1,2);
        upKey = new TextField(Input.Keys.toString(menuCont.getMoveUp()), GAME.generalTextFieldStyle);
        upKey.setAlignment(1);
        upKey.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                setControl(upKey, keycode);
                menuCont.setMoveUp(keycode);
                return true;
            }
        });

                //keybinding for Attack on settings page
                downLabel = new Label("Move Down", GAME.generalLabelStyle);
        downLabel.setAlignment(1,2);
        downKey = new TextField(Input.Keys.toString(menuCont.getMoveDown()), GAME.generalTextFieldStyle);
        downKey.setMaxLength(1);
        downKey.setAlignment(1);
        downKey.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                setControl(downKey, keycode);
                menuCont.setMoveDown(keycode);
                return true;
            }
        });
        //keybinding for Inventory on settings page
        leftLabel = new Label("Move Left", GAME.generalLabelStyle);
        leftLabel.setAlignment(1,2);
        leftKey = new TextField(Input.Keys.toString(menuCont.getMoveLeft()), GAME.generalTextFieldStyle);
        leftKey.setMaxLength(1);
        leftKey.setAlignment(1);
        leftKey.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                setControl(leftKey, keycode);
                menuCont.setMoveLeft(keycode);
                return true;
            }
        });
        //keybinding for right movement on settings page
        rightLabel = new Label("Move Right", GAME.generalLabelStyle);
        rightLabel.setAlignment(1,2);
        rightKey = new TextField(Input.Keys.toString(menuCont.getMoveRight()), GAME.generalTextFieldStyle);
        rightKey.setMaxLength(1);
        rightKey.setAlignment(1);
        rightKey.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                setControl(rightKey, keycode);
                menuCont.setMoveRight(keycode);
                return true;
            }
        });
        //keybinding for Interact on settings page
        tradeLabel = new Label("Trade", GAME.generalLabelStyle);
        tradeLabel.setAlignment(1,2);
        tradeKey = new TextField(Input.Keys.toString(menuCont.getTradeMenu()), GAME.generalTextFieldStyle);
        tradeKey.setMaxLength(1);
        tradeKey.setAlignment(1);
        tradeKey.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                setControl(tradeKey, keycode);
                menuCont.setTradeMenu(keycode);
                return true;
            }
        });
        //keybinding for Menu on settings page
        menuLabel = new Label("Menu", GAME.generalLabelStyle);
        menuLabel.setAlignment(1,2);
        menuKey = new TextField(Input.Keys.toString(menuCont.getOpenPauseMenu()), GAME.generalTextFieldStyle);
        menuKey.setMaxLength(1);
        menuKey.setAlignment(1);
        menuKey.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                setControl(menuKey, keycode);
                menuCont.setOpenPauseMenu(keycode);
                return true;
            }
        });

        inventoryLabel = new Label("Inventory", GAME.generalLabelStyle);
        inventoryLabel.setAlignment(1,2);
        inventoryKey = new TextField(Input.Keys.toString(menuCont.getOpenInventory()), GAME.generalTextFieldStyle);
        inventoryKey.setMaxLength(1);
        inventoryKey.setAlignment(1);
        inventoryKey.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                setControl(inventoryKey, keycode);
                menuCont.setOpenInventory(keycode);
                return true;
            }
        });
        fightLabel = new Label("Combat", GAME.generalLabelStyle);
        fightLabel.setAlignment(1,2);
        fightKey = new TextField(Input.Keys.toString(menuCont.getFightAction()), GAME.generalTextFieldStyle);
        fightKey.setMaxLength(1);
        fightKey.setAlignment(1);
        fightKey.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                setControl(fightKey, keycode);
                menuCont.setFightAction(keycode);
                return true;
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
                //TODO: save keybind changes
                /*controls.setMoveUp(upKey.getText());
                controls.setMoveDown(downKey.getText());
                controls.setMoveLeft(leftKey.getText());
                controls.setMoveRight(rightKey.getText());
                controls.setOpenInventory(inventoryKey.getText());
                controls.setFightAction(fightKey.getText());
                controls.setTradeMenu(tradeKey.getText());
                controls.setOpenPauseMenu(menuKey.getText());*/
            }
        });
    }

    public String getKeyString(int key){
        return KeyEvent.getKeyText(key);
    }

    public MainMenuControls getMenuCont(){
        return menuCont;
    }

    public void setControl(TextField t, int keycode){
        String key = Input.Keys.toString(keycode);
        System.out.println(key);
        if(!t.getText().equals("")){
            //clear text and write new character
            t.setText("");
            t.setMaxLength(key.length());
            t.setText(key);
        } else {
            t.setMaxLength(key.length());
            t.setText(key);
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
        table.add(tradeLabel);
        table.add(tradeKey).left();
        table.add(menuLabel);
        table.add(menuKey).left();
        table.row();
        table.add(inventoryLabel);
        table.add(inventoryKey).left();
        table.add(fightLabel);
        table.add(fightKey).left();
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

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        GAME.stageInstance.clear();
    }

    public static void settingsSerialize() {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            try {
                String xmlString = xmlMapper.writeValueAsString(new MainMenuControls(Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.T, Input.Keys.ESCAPE, Input.Keys.E, Input.Keys.Q));

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
            deserializeMenu.getTradeMenu();
            deserializeMenu.getOpenPauseMenu();
            deserializeMenu.getOpenInventory();
            deserializeMenu.getFightAction();
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
