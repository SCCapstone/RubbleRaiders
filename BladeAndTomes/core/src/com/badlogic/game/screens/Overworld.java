package com.badlogic.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
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
    Rectangle[] loadZone;


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

        loadZone = new Rectangle[] {
          new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle()
        };

        for(int i = 0; i<4; i++) {
            if(i%2==0) {
                loadZone[i].setSize(GAME.MOVE_DISTANCE, GAME.stageInstance.getHeight());
            }
            else {
                loadZone[i].setSize(GAME.stageInstance.getWidth(), GAME.MOVE_DISTANCE);
            }
            loadZone[i].setCenter(loadZone[i].getWidth()/2, loadZone[i].getHeight());
        }

        loadZone[0].setPosition(0,0);
        loadZone[1].setPosition(0,0);
        loadZone[2].setPosition(0, GAME.stageInstance.getHeight()- GAME.MOVE_DISTANCE);
        loadZone[3].setPosition(GAME.stageInstance.getWidth()- GAME.MOVE_DISTANCE, 0);

        //Reference page that referred to how to set up Keyboard Focus by the libGDX developers
        //https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/Stage.html#setKeyboardFocus-com.badlogic.gdx.scenes.scene2d.Actor-
        GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);

        //Adds the player's icon to the stage.
        GAME.stageInstance.addActor(GAME.player.playerIcon);
    }

    @Override
    public void render(float delta){

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        GAME.batch.begin();
        GAME.batch.draw(background, 0 ,0);
        GAME.batch.draw(tavern, (float) (Gdx.graphics.getWidth() * 0.75), (float) (Gdx.graphics.getHeight() * 0.75));
        GAME.batch.draw(marketStall, Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 5);
        GAME.batch.draw(barracks, (float) (Gdx.graphics.getWidth() * 0.75), Gdx.graphics.getHeight() / 4);
        GAME.batch.draw(chapel, Gdx.graphics.getWidth() / 4, (float) (Gdx.graphics.getHeight() * 0.75));
        GAME.batch.draw(questBoard, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        GAME.batch.draw(portal, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 8);
        GAME.batch.end();

        /*if((GAME.player.playerIcon.getX() <= MOVE_DISTANCE || GAME.player.playerIcon.getY() <= MOVE_DISTANCE) || (GAME.player.playerIcon.getX() > GAME.stageInstance.getWidth() - MOVE_DISTANCE ||
                GAME.player.playerIcon.getY() > GAME.stageInstance.getHeight() - MOVE_DISTANCE))
        {
            GAME.stageInstance.clear();
            dispose();
            GAME.setScreen(new Dungeon(GAME));
        }*/

        // Thanks to user "centenond" on StackOverflow for pointing out a useful function on using rectangle to detect.
        // Co-Opted for use in our Project for creating walkable areas and loading zones.
        //https://stackoverflow.com/questions/61491889/how-to-detect-collisions-between-objects-in-libgdx
        for(int i=0; i<loadZone.length; i++) {
            if(GAME.player.moveSquare.overlaps(loadZone[i])) {
                GAME.stageInstance.clear();
                dispose();
                GAME.setScreen(new Dungeon(GAME));
            }
        }

        //Simplifying render thanks to libGDX for their "Extending the Simple Game" Tutorial,
        //Specifically the advanced section on super.render() as well as the following section on the main
        //game screen
        //https://libgdx.com/dev/simple-game-extended/
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
