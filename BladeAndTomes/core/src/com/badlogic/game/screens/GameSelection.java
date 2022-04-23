package com.badlogic.game.screens;

import LoadAndSave.LoadSaveManager;
import Sounds.ButtonClickSound;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameSelection extends ScreenAdapter {

    final BladeAndTomes GAME;

    Texture background;
    Image backgroundImage;
    Table savedGames;
    TextButton game1;
    TextButton game2;
    TextButton game3;
    TextButton game4;
    TextButton loadBack;
    Label selectGameInfo;
    TextButton deleteFile1;
    TextButton deleteFile2;
    TextButton deleteFile3;
    TextButton deleteFile4;
    Window loadWindow;
    ButtonClickSound buttonSound;

    public GameSelection(final BladeAndTomes game){
        this.GAME = game;
        background = new Texture(Gdx.files.internal("OverworldBackground.jpg"));
        backgroundImage = new Image(background);
        GAME.stageInstance.addActor(backgroundImage);

        buttonSound = new ButtonClickSound();

        deleteFile1 = new TextButton("Delete\nGame 1", GAME.generalTextButtonStyle);
        deleteFile2 = new TextButton("Delete\nGame 2", GAME.generalTextButtonStyle);
        deleteFile3 = new TextButton("Delete\nGame 3", GAME.generalTextButtonStyle);
        deleteFile4 = new TextButton("Delete\nGame 4", GAME.generalTextButtonStyle);
        selectGameInfo = new Label("Please select a game file to continue or start", GAME.generalLabelStyle);
        selectGameInfo.setSize(540f, 75f);
        selectGameInfo.setPosition(930, 910, 1);
        selectGameInfo.setAlignment(1, 1);
        selectGameInfo.setFontScale(1.5f);

        loadWindow = new Window("", GAME.generalWindowStyle);
        //loadWindow.setBackground(new TextureRegionDrawable(new TextureRegion()));
        //loadWindow.setBackground();
        loadWindow.setSize(GAME.stageInstance.getWidth()/4,GAME.stageInstance.getHeight()/2);
        loadWindow.setPosition(GAME.stageInstance.getWidth()*0.35f, GAME.stageInstance.getHeight()*0.30f);
        loadBack = new TextButton("Back", GAME.generalTextButtonStyle);
        loadBack.setSize(200f,65f);
        loadBack.setPosition(810, 240);
        loadBack.setColor(Color.LIGHT_GRAY);
        loadBack.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                selectGameInfo.remove();
                loadBack.remove();
                savedGames.remove();
                loadWindow.remove();
                GAME.stageInstance.clear();
                GAME.setScreen(new MainMenu(GAME));
            }
        });
        savedGames = new Table();
        savedGames.setFillParent(true);
        savedGames.defaults();
        game1 = new TextButton(GAME.loadSaveManager.loadPlayer(0).getName().equals("") ?
                "Game 1" :
                GAME.loadSaveManager.loadPlayer(0).getName(), GAME.generalTextButtonStyle);
        game1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                selectGameInfo.remove();
                loadBack.remove();
                savedGames.remove();
                loadWindow.remove();
                GAME.stageInstance.clear();
                GAME.currentSaveIndex = 0;
                checkPlayer();
            }
        });
        deleteFile1.setSize(60f, 60f);
        deleteFile1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //delete game file 1
                GAME.loadSaveManager.savePlayer(new Player(), 0);
                deleteFile1.remove();
                game1.setText("Game 1");
            }
        });
        game2 = new TextButton(GAME.loadSaveManager.loadPlayer(1).getName().equals("") ? "Game 2" : GAME.loadSaveManager.loadPlayer(1).getName(), GAME.generalTextButtonStyle);
        game2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                selectGameInfo.remove();
                loadBack.remove();
                savedGames.remove();
                loadWindow.remove();
                GAME.currentSaveIndex = 1;
                GAME.stageInstance.clear();
                //Add load/save from Inventory branch
                checkPlayer();

            }});

                deleteFile2.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        //delete game file 2
                        GAME.loadSaveManager.savePlayer(new Player(), 1);
                        deleteFile2.remove();
                        game2.setText("Game 2");
                    }
                });
                game3 = new TextButton(GAME.loadSaveManager.loadPlayer(2).getName().equals("") ? "Game 3" : GAME.loadSaveManager.loadPlayer(2).getName(), GAME.generalTextButtonStyle);
                game3.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        selectGameInfo.remove();
                        loadBack.remove();
                        savedGames.remove();
                        loadWindow.remove();
                        GAME.stageInstance.clear();
                        GAME.currentSaveIndex = 2;
                        //Add load/save from Inventory branch
                        checkPlayer();
                    }
                });
                deleteFile3.setSize(65f, 65f);
                deleteFile3.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        //delete game file 3
                        GAME.loadSaveManager.savePlayer(new Player(), 2);
                        deleteFile3.remove();
                        game3.setText("Game 3");
                    }
                });
                game4 = new TextButton(GAME.loadSaveManager.loadPlayer(3).getName().equals("") ? "Game 4" : GAME.loadSaveManager.loadPlayer(3).getName(), GAME.generalTextButtonStyle);
                game4.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        selectGameInfo.remove();
                        loadBack.remove();
                        savedGames.remove();
                        loadWindow.remove();
                        GAME.stageInstance.clear();
                        GAME.currentSaveIndex = 3;
                        //Add load/save from Inventory branch
                        checkPlayer();
                    }
                });
                deleteFile4.setSize(65f, 65f);
                deleteFile4.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        //delete game file 4
                        GAME.loadSaveManager.savePlayer(new Player(), 3);
                        deleteFile4.remove();
                        game4.setText("Game 4");
                    }
                });

                //game selection table layout
                savedGames.add(game1).padBottom(10f).center().padRight(10f);
                if(!GAME.loadSaveManager.loadPlayer(0).getDefault()){
                    savedGames.add(deleteFile1);
                }
                savedGames.row();
                savedGames.add(game2).padBottom(10f).center().padRight(10f);
                if(!GAME.loadSaveManager.loadPlayer(1).getDefault()){
                    savedGames.add(deleteFile2);
                }
                savedGames.row();
                savedGames.add(game3).padBottom(10f).center().padRight(10f);
                if(!GAME.loadSaveManager.loadPlayer(2).getDefault()){
                    savedGames.add(deleteFile3);
                }
                savedGames.row();
                savedGames.add(game4).padBottom(10f).center().padRight(10f);
                if(!GAME.loadSaveManager.loadPlayer(3).getDefault()){
                    savedGames.add(deleteFile4);
                }

                loadWindow.addActor(savedGames);
                GAME.stageInstance.addActor(selectGameInfo);
                GAME.stageInstance.addActor(loadWindow);
                GAME.stageInstance.addActor(loadBack);

            }

            @Override
            public void show() {
                Gdx.input.setInputProcessor(GAME.stageInstance);
            }

            //Taken from MainMenu screen and edited
            @Override
            public void render(float delta) {


                //Simplifying render thanks to libGDX for their "Extending the Simple Game" Tutorial,
                //Specifically the advanced section on super.render() as well as the following section on the main
                //game screen
                //https://libgdx.com/dev/simple-game-extended/
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
                GAME.stageInstance.draw();
                // Torch Animation, Source: https://www.youtube.com/watch?v=vjgdX95HVrM
                if (Gdx.input.justTouched()) {
                    buttonSound.playClick();
                }

                //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                //GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
                //GAME.stageInstance.draw();

                // SHOULD RENDER IN A MOUSE OR SOME TYPE OF CURSOR FOR THE PERSON
            }

            @Override
            public void resize(int width, int height) {
                GAME.stageInstance.getViewport().update(width, height, true);
            }

            @Override
            public void hide() {
                Gdx.input.setInputProcessor(null);
            }

            @Override
            public void dispose() {
                super.dispose();
            }

            public void checkPlayer(){
                Player p = GAME.loadSaveManager.loadPlayer(GAME.currentSaveIndex);
                if(p.getDefault()){
                    GAME.setScreen(new CharacterCreation(GAME));
                } else {
                    GAME.setScreen(new Overworld(GAME));
                }
            }

        }