package com.badlogic.game.screens;

import Keyboard_Mouse_Controls.MainMenuControls;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
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
    Label item1;
    TextField item1Key;
    Label item2;
    TextField item2Key;
    Label item3;
    TextField item3Key;
    Label item4;
    TextField item4Key;
    Label item5;
    TextField item5Key;
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

        //image
        //https://www.freepik.com/vectors/dungeon
        // Dungeon vector created by upklyak - www.freepik.com
        Texture background = new Texture(Gdx.files.internal("Settings.jpg"));
        backgroundImage = new Image(background);
        backgroundImage.setSize(GAME.stageInstance.getWidth(), GAME.stageInstance.getHeight());
        GAME.stageInstance.addActor(backgroundImage);

        //label and slider for changing music volume
        settingsMusicLabel = new Label("Music Volume",GAME.generalLabelStyle);
        settingsMusicLabel.setAlignment(1,2);
        //libGDX documentation on how Slider works as well as UseOf.org example by libGDX on application of sliders by libGDX team
        //and curators of UseOf.org
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Slider.html#isOver--
        //http://useof.org/java-open-source/com.badlogic.gdx.scenes.scene2d.ui.Slider
        settingsMusicSlider = new Slider(0f, 1f, 0.05f, false ,GAME.generalSliderStyle);
        settingsMusicSlider.setButton(0);
        settingsMusicSlider.setVisualPercent(GAME._bgmusic.getVolume());
        settingsMusicSlider.setWidth(500);
        settingsMusicSlider.getStyle().knob.setMinWidth(50);
        settingsMusicSlider.getStyle().knobDown.setMinWidth(50);
        //sets the music volume based on slider value
        settingsMusicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                GAME._bgmusic.setVolume(settingsMusicSlider.getVisualValue());
            }
        });

        //Below are all the labels and text fields to allow user to change keybinds for certain
        //game mechanics

        upLabel = new Label("Move Up", GAME.generalLabelStyle);
        upLabel.setAlignment(1,2);
        upKey = new TextField(Input.Keys.toString(menuCont.getMoveUp()), GAME.generalTextFieldStyle);
        upKey.setAlignment(1);
        upKey.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if(event.getPointer() == 0){
                setControl(upKey, keycode);
                menuCont.setMoveUp(keycode);
                event.setPointer(-1);
                }
                return false;
            }

        });
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
        tradeLabel = new Label("Trading\n& Chests", GAME.generalLabelStyle);
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
        menuLabel = new Label("Pause", GAME.generalLabelStyle);
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

        item1 = new Label("Item Slot 1", GAME.generalLabelStyle);
        item1.setAlignment(1, 2);
        item1Key = new TextField(Input.Keys.toString(menuCont.getSelection(0)), GAME.generalTextFieldStyle);
        item1 = new Label("Item Slot 1", GAME.generalLabelStyle);
        item1.setAlignment(1, 2);
        item1Key = new TextField(Input.Keys.toString(menuCont.getItem1()), GAME.generalTextFieldStyle);
        item1Key.setMaxLength(1);
        item1Key.setAlignment(1);
        item1Key.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                setControl(item1Key, keycode);
                menuCont.setSelection(0, keycode);
                menuCont.setItem1(keycode);
                return true;
            }
        });

        item2 = new Label("Item Slot 2", GAME.generalLabelStyle);
        item2.setAlignment(1, 2);
        item2Key = new TextField(Input.Keys.toString(menuCont.getSelection(1)), GAME.generalTextFieldStyle);
        item2Key = new TextField(Input.Keys.toString(menuCont.getItem2()), GAME.generalTextFieldStyle);
        item2Key.setMaxLength(1);
        item2Key.setAlignment(1);
        item2Key.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                setControl(item2Key, keycode);
                menuCont.setSelection(1, keycode);
                menuCont.setItem2(keycode);
                return true;
            }
        });

        item3 = new Label("Item Slot 3", GAME.generalLabelStyle);
        item3.setAlignment(1, 2);
        item3Key = new TextField(Input.Keys.toString(menuCont.getSelection(2)), GAME.generalTextFieldStyle);
        item3Key = new TextField(Input.Keys.toString(menuCont.getItem3()), GAME.generalTextFieldStyle);
        item3Key.setMaxLength(1);
        item3Key.setAlignment(1);
        item3Key.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                setControl(item3Key, keycode);
                menuCont.setSelection(2, keycode);
                menuCont.setItem3(keycode);
                return true;
            }
        });

        item4 = new Label("Item Slot 4", GAME.generalLabelStyle);
        item4.setAlignment(1, 2);
        item4Key = new TextField(Input.Keys.toString(menuCont.getSelection(3)), GAME.generalTextFieldStyle);
        item4Key = new TextField(Input.Keys.toString(menuCont.getItem4()), GAME.generalTextFieldStyle);
        item4Key.setMaxLength(1);
        item4Key.setAlignment(1);
        item4Key.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                setControl(item4Key, keycode);
                menuCont.setSelection(3, keycode);

                return true;
            }
        });
        item5 = new Label("Item Slot 5", GAME.generalLabelStyle);
        item5.setAlignment(1, 2);
        item5Key = new TextField(Input.Keys.toString(menuCont.getSelection(4)), GAME.generalTextFieldStyle);
        item5Key.setMaxLength(1);
        item5Key.setAlignment(1);
        item5Key.addListener(new InputListener(){
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                setControl(item5Key, keycode);
                menuCont.setSelection(4, keycode);
                return true;
            }
        });

        //button to exit settings screen and return to main menu
        settingsQuitOption = new TextButton("Exit Settings", GAME.generalTextButtonStyle);
        settingsQuitOption.setPosition(25f,GAME.stageInstance.getHeight()-125);
        settingsQuitOption.setSize(150f,100f);
        settingsQuitOption.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                //Back to main menu screen
                dispose();
                GAME.loadSaveManager.saveSettings(menuCont);
                GAME.setScreen(new MainMenu(game));
            }
        });
    }

    public void setControl(TextField t, int keycode){
        String key = Input.Keys.toString(keycode);
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

        //set up table that will contain all labels and text fields for changing controls
        table = new Table();
        table.defaults();
        table.setBounds(775,400,500,GAME.stageInstance.getHeight());
        table.setSize(GAME.stageInstance.getWidth()*0.2f,GAME.stageInstance.getHeight()*0.25f);
        //adding all labels and text fields to table in order
        table.add(settingsMusicLabel);
        table.add(settingsMusicSlider).colspan(3).width(table.getWidth()+43);
        table.row().padTop(10f);
        table.add(upLabel).padBottom(8f);
        table.add(upKey).left().padBottom(8f).padRight(8f);
        table.add(downLabel).padBottom(8f);
        table.add(downKey).left().padBottom(8f).padRight(8f);
        table.add(leftLabel).padBottom(8f);
        table.add(leftKey).left().padBottom(8f).padRight(8f);
        table.add(rightLabel).padBottom(8f);
        table.add(rightKey).left().padBottom(8f).padRight(8f);
        table.row();
        table.add(tradeLabel).padBottom(8f);
        table.add(tradeKey).left().padBottom(8f).padRight(8f);
        table.add(menuLabel).padBottom(8f);
        table.add(menuKey).left().padBottom(8f).padRight(8f);
        table.add(inventoryLabel).padBottom(8f);
        table.add(inventoryKey).left().padBottom(8f).padRight(8f);
        table.add(fightLabel).padBottom(8f);
        table.add(fightKey).left().padBottom(8f).padRight(8f);
        table.row();
        table.add(item1);
        table.add(item1Key).left().padRight(8f);
        table.add(item2);
        table.add(item2Key).left().padRight(8f);
        table.add(item3);
        table.add(item3Key).left().padRight(8f);
        table.add(item4);
        table.add(item4Key).left().padRight(8f);
        table.add(item5);
        table.add(item5Key).left();
        table.row().padTop(10f);
        GAME.stageInstance.addActor(settingsQuitOption);

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


}
