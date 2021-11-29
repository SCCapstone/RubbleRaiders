package com.badlogic.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Overworld extends ScreenAdapter {

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;
    Image playerIcon;
    SpriteBatch batch;
    Texture background;
    Texture chapel;
    Texture barracks;
    Texture questBoard;
    Texture portal;
    Texture marketStall;
    Texture tavern;
    BitmapFont font;


    // Helpful Collision Detection Tutorials (NOT IMPLEMENTED IN CODE YET)
    // TODO: IMPLEMENT THESE IN CODE
    //https://stackoverflow.com/questions/61491889/how-to-detect-collisions-between-objects-in-libgdx
    //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/math/Intersector.html

    public Overworld (final BladeAndTomes game) {

        this.GAME = game;

        MOVE_DISTANCE = 64;

        batch = new SpriteBatch();
        // Assets were obtained from below source
        // https://merchant-shade.itch.io/16x16-mini-world-sprites
        background = new Texture(Gdx.files.internal("OverworldBackground.jpg"));
        chapel = new Texture(Gdx.files.internal("Chapel.jpg"));
        barracks = new Texture(Gdx.files.internal("Barracks.jpg"));
        marketStall = new Texture(Gdx.files.internal("MarketBuilding.jpg"));
        portal = new Texture(Gdx.files.internal("PortalToDungeon.jpg"));
        questBoard = new Texture(Gdx.files.internal("Quests_Board.jpg"));
        tavern = new Texture(Gdx.files.internal("Tavern.jpg"));


        //TODO: Simplify all of this into Player class?
        //TODO: Move Player Icon Definitions to Backbone?
        playerIcon = new Image(new Texture(Gdx.files.internal("PlayerIcon.jpg")));
        playerIcon.setOrigin(playerIcon.getImageWidth()/2, playerIcon.getImageHeight()/2);
        playerIcon.setPosition(960, 600);

        // Thank you to libGDX.info editors for creating a helpful tutorial
        // on MoveActions as well as the libGDX creators for teaching pool-able actions
        // and InputListeners on their wiki.
        // https://libgdx.info/basic_action/
        // https://github.com/libgdx/libgdx/wiki/Scene2d
        playerIcon.addListener(new InputListener() {

            @Override
            public boolean keyDown(InputEvent event, int keycode)
            {
                int moveX = 0, moveY = 0;

                switch(keycode) {
                    case Input.Keys.UP:
                        playerIcon.addAction(Actions.moveTo(playerIcon.getX(), playerIcon.getY() + MOVE_DISTANCE,0));
                        moveY = MOVE_DISTANCE;
                        break;
                    case Input.Keys.DOWN:
                        playerIcon.addAction(Actions.moveTo(playerIcon.getX(), playerIcon.getY() - MOVE_DISTANCE,0));
                        moveY = -MOVE_DISTANCE;
                        break;
                    case Input.Keys.LEFT:
                        playerIcon.addAction(Actions.moveTo(playerIcon.getX() - MOVE_DISTANCE, playerIcon.getY(),0));
                        moveX = -MOVE_DISTANCE;
                        break;
                    case Input.Keys.RIGHT:
                        playerIcon.addAction(Actions.moveTo(playerIcon.getX() + MOVE_DISTANCE, playerIcon.getY(),0));
                        moveX = MOVE_DISTANCE;
                        break;
                    default:
                        return false;
                }

                checkOffMap(playerIcon.getX() + moveX, playerIcon.getY() + moveY);

                return true;
            }

            /**
             * Function that makes sure to moves the screen to the dungeon screen so that the player can leave the overworld
             * and adventure within the depths.
             */
            public void checkOffMap(float x, float y)
            {
                //TODO: Reduce compound statement into something easily runnable.
                if((x <= 128 || y <= 128) || (x > GAME.stageInstance.getWidth() - 128 || y > GAME.stageInstance.getHeight() - 128))
                {
                    GAME.stageInstance.clear();
                    dispose();
                    GAME.setScreen(new Dungeon(GAME));
                }
            }
        });

        //Reference page that referred to how to set up Keyboard Focus by the libGDX developers
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/Stage.html#setKeyboardFocus-com.badlogic.gdx.scenes.scene2d.Actor-
        GAME.stageInstance.setKeyboardFocus(playerIcon);

        //Adds the player's icon to the stage.
        GAME.stageInstance.addActor(playerIcon);
    }

    @Override
    public void render(float delta){

        //Simplifying render thanks to libGDX for their "Extending the Simple Game" Tutorial,
        //Specifically the advanced section on super.render() as well as the following section on the main
        //game screen
        //https://libgdx.com/dev/simple-game-extended/
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
        GAME.stageInstance.draw();

        GAME.batch.begin();
        GAME.batch.draw(background, 0 ,0);
        GAME.batch.draw(tavern, (float) (Gdx.graphics.getWidth() * 0.75), (float) (Gdx.graphics.getHeight() * 0.75));
        GAME.batch.draw(marketStall, Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 5);
        GAME.batch.draw(barracks, (float) (Gdx.graphics.getWidth() * 0.75), Gdx.graphics.getHeight() / 4);
        GAME.batch.draw(chapel, Gdx.graphics.getWidth() / 4, (float) (Gdx.graphics.getHeight() * 0.75));
        GAME.batch.draw(questBoard, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        GAME.batch.draw(portal, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 8);
        GAME.batch.end();
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
