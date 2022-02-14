package com.badlogic.game.screens;

import ScreenOverlay.Events;
import ScreenOverlay.MainInventory;
import com.badlogic.game.creatures.Goblin;
import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Dungeon extends ScreenAdapter {

    final BladeAndTomes GAME;
    final int MOVE_DISTANCE;
    Image playerIcon;
    Texture background;
    Texture eventTex;

    Image eventImage;
    Image backgroundImage;
    private boolean inRoom;
    MainInventory inventory;
    float eventX, eventY, eventSizeX, eventSizeY, enemyX, enemyY;
    Goblin goblin;
    boolean isEnemyTurn;

    Events event;

    //Rectangle walkableArea;
    //Rectangle doorHitBox;
    //float xIcon, yIcon, xMove, yMove, xInter, yInter;

    int roomId;

    public Dungeon(final BladeAndTomes game) {

        this.GAME = game;
        MOVE_DISTANCE = 64;

        GAME.stageInstance.clear();

        //set background info
        //Dungeon background images taken from https://opengameart.org/content/set-of-background-for-dungeon-room
        //Author of images Kamigeek
        background = new Texture(Gdx.files.internal("MainDungeon.png"));
        backgroundImage = new Image(background);

        // Textures rendered in for our event
        // currently giving it a reasonable range to spawn into, and keeping it in dungeon 1
        eventTex = new Texture(Gdx.files.internal("GoldChest.jpg"));
        eventImage = new Image(eventTex);
        eventSizeX = 120f;
        eventSizeY = 120f;
        eventX = MathUtils.random(360, 1600);
        eventY = MathUtils.random(240, 960);

        // Thanks to Alex Farcer for providing the dimensions of the original background. I (Aidan) rescaled the
        // image so that it would properly fit within the confines of the background.
        backgroundImage.setSize(2000,1150);
        backgroundImage.setPosition(-25,-20);
        GAME.stageInstance.addActor(backgroundImage);

        // Currently having size as a set variable here want to move it to events class
        // This should keep it permanently in place through the dungeon right now
        eventImage.setSize(eventSizeX, eventSizeY);
        eventImage.setPosition(eventX, eventY);
        GAME.stageInstance.addActor(eventImage);


        // Used dimensions of the room as a reference point thanks to Alex Farcer for providing them later in code
        // (See render function)
        /*
        walkableArea = new Rectangle();
        walkableArea.setSize((int) backgroundImage.getWidth() - 3*MOVE_DISTANCE, (int) backgroundImage.getHeight() - 3*MOVE_DISTANCE);
        walkableArea.setCenter(walkableArea.getWidth()/2, walkableArea.getHeight()/2);
        walkableArea.setPosition(MOVE_DISTANCE*3, MOVE_DISTANCE*3);*/

        //Adds the player's icon to the stage.
        GAME.player.playerIcon.setPosition(GAME.stageInstance.getWidth()/2,GAME.stageInstance.getHeight()/2);
        GAME.player.moveSquare.setPosition(GAME.stageInstance.getWidth()/2,GAME.stageInstance.getHeight()/2);
        GAME.player.interactSquare.setPosition(GAME.stageInstance.getWidth()/2 - MOVE_DISTANCE,GAME.stageInstance.getHeight()/2 - MOVE_DISTANCE);
        GAME.stageInstance.addActor(GAME.player.playerIcon);
        GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);

        //Instances the player's inventory
        inventory = new MainInventory(GAME);


        goblin = new Goblin(GAME.player, GAME);
        isEnemyTurn = true;

    }

    @Override
    public void render(float delta) {

        // Thanks to user "centenond" on StackOverflow for pointing out a useful function on using rectangle to detect.
        // Co-Opted for use in our Project for creating walkable areas and loading zones.
        //https://stackoverflow.com/questions/61491889/how-to-detect-collisions-between-objects-in-libgdx
        /*if (!GAME.player.moveSquare.overlaps(walkableArea)) {
            if (GAME.player.playerIcon.getY() > (walkableArea.getY() + walkableArea.getHeight())) {
                GAME.player.playerIcon.setPosition(xIcon, yIcon - 3*MOVE_DISTANCE);
                GAME.player.moveSquare.setPosition(xMove, yMove - 3*MOVE_DISTANCE);
                GAME.player.interactSquare.setPosition(xInter, yInter - 3*MOVE_DISTANCE);
            }
            else if (GAME.player.playerIcon.getY() < walkableArea.getY()) {
                GAME.player.playerIcon.setPosition(xIcon, yIcon + 3*MOVE_DISTANCE);
                GAME.player.moveSquare.setPosition(xMove, yMove + 3*MOVE_DISTANCE);
                GAME.player.interactSquare.setPosition(xInter, yInter + 3*MOVE_DISTANCE);
            }

            if (GAME.player.playerIcon.getX() > (walkableArea.getX() + walkableArea.getWidth())) {
                GAME.player.playerIcon.setPosition(xIcon - 3*MOVE_DISTANCE, yIcon);
                GAME.player.moveSquare.setPosition(xMove - 3*MOVE_DISTANCE, yMove);
                GAME.player.interactSquare.setPosition(xInter - 3*MOVE_DISTANCE, yInter);
            }
            else if (GAME.player.playerIcon.getX() < walkableArea.getX()) {
                GAME.player.playerIcon.setPosition(xIcon + 3*MOVE_DISTANCE, yIcon);
                GAME.player.moveSquare.setPosition(xMove + 3*MOVE_DISTANCE, yMove);
                GAME.player.interactSquare.setPosition(xInter + 3*MOVE_DISTANCE, yInter);
            }
        }
        else {
            xIcon = GAME.player.playerIcon.getX();
            yIcon = GAME.player.playerIcon.getY();
            xMove = GAME.player.moveSquare.getX();
            yMove = GAME.player.moveSquare.getY();
            xInter = GAME.player.interactSquare.getX();
            yInter = GAME.player.interactSquare.getY();
        }*/


        //Simplifying render thanks to libGDX for their "Extending the Simple Game" Tutorial,
        //Specifically the advanced section on super.render() as well as the following section on the main
        //game screen
        //https://libgdx.com/dev/simple-game-extended/
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GAME.stageInstance.act(Gdx.graphics.getDeltaTime());
        GAME.stageInstance.draw();
        inventory.update();
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            int hitRoll = (int)(Math.random()*(20)+1);
            if (hitRoll >= goblin.getArmorPoints()) {
                goblin.damageTaken(GAME.player.getPhysical());
            }
            if((int)(GAME.player.moveSquare.getX() - goblin.getXCord())/200 == 0 &&(int)(GAME.player.moveSquare.getY() - goblin.getYCord())/100 ==0 && isEnemyTurn == false) {

                isEnemyTurn = true;
            }
            if(goblin.checkIfDead()) {
                goblin.enemyImage.remove();
            }
        }
        if((int)(GAME.player.moveSquare.getX() - goblin.getXCord())/200 == 0 &&(int)(GAME.player.moveSquare.getY() - goblin.getYCord())/100 ==0 && isEnemyTurn == true) {
            //goblin.movement();
            goblin.attackPlayer();
            inventory.updateHealth();
            isEnemyTurn = false;
        }

        /*if((GAME.player.moveSquare.getX() <= 3*MOVE_DISTANCE ||
                        GAME.player.moveSquare.getY() <= 3*MOVE_DISTANCE) ||
                (GAME.player.moveSquare.getX() > GAME.stageInstance.getWidth() - 3*MOVE_DISTANCE ||
                        GAME.player.moveSquare.getY() > GAME.stageInstance.getHeight() - 3*MOVE_DISTANCE))
        {
            GAME.player.playerIcon.setPosition();
        }*/

        //Two side rooms - one to the left and one to the right
        //Alex Facer programmed in hitbox for Dungeon Doors. Improved on it to distinguish different rooms in the dungeon.
        //I used the resolution sizes provided by Alex Facer and adjusted them to allow for the backgrounds to properly
        //fit as well as so the loading zones would be hit in the correct manner.
        if(roomId == 0 &&
                (GAME.player.playerIcon.getX() <= 3*MOVE_DISTANCE && GAME.player.playerIcon.getY() < 550 && GAME.player.playerIcon.getY() > 300) ||
                (GAME.player.playerIcon.getX() >= GAME.stageInstance.getWidth() - 3*MOVE_DISTANCE && GAME.player.playerIcon.getY() < 550 && GAME.player.playerIcon.getY() > 300))
        {
            if(GAME.player.playerIcon.getX() >= GAME.stageInstance.getWidth() - 3*MOVE_DISTANCE && GAME.player.playerIcon.getY() < 550 && GAME.player.playerIcon.getY() > 300) {
                roomId = 1;
            }
            else if(GAME.player.playerIcon.getX() <= 3*MOVE_DISTANCE && GAME.player.playerIcon.getY() < 550 && GAME.player.playerIcon.getY() > 300) {
                roomId = 2;
            }

            GAME.stageInstance.clear();

            backgroundImage.remove();
            eventImage.remove();
            //I (Aidan) Estimated the size of the room based on the estimations of the dimensions of Alex Facer
            backgroundImage = new Image(new Texture(Gdx.files.internal("SideDungeon.png")));
            // where -25, 20
            backgroundImage.setBounds(0, 0, 2000, 1150);
            GAME.stageInstance.addActor(backgroundImage);

            //GAME.stageInstance.getBatch().draw(eventTex,eventX,eventY);

            GAME.player.playerIcon.setPosition(960, MOVE_DISTANCE*3);
            GAME.stageInstance.addActor(GAME.player.playerIcon);
            GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);
        }
        else if (roomId != 0 &&
                (GAME.player.playerIcon.getX() >= 835 && GAME.player.playerIcon.getX() <= 1085) &&
                (GAME.player.playerIcon.getY() <= MOVE_DISTANCE && GAME.player.playerIcon.getY() >= 0))
        {
            inRoom = false;
            GAME.stageInstance.clear();

            backgroundImage.remove();

            backgroundImage = new Image(new Texture(Gdx.files.internal("MainDungeon.png")));
            backgroundImage.setPosition(-25, -20);
            backgroundImage.setSize(2000, 1150);

            eventImage = new Image(new Texture(Gdx.files.internal("GoldChest.jpg")));
            eventImage.setPosition(eventX, eventY);
            eventImage.setSize(eventSizeX, eventSizeY);


            GAME.stageInstance.addActor(backgroundImage);

            if(roomId == 1)
            {
                GAME.player.playerIcon.setPosition(GAME.stageInstance.getWidth() - 4*MOVE_DISTANCE, GAME.stageInstance.getHeight()/2);
                GAME.player.moveSquare.setPosition(GAME.stageInstance.getWidth() - 4*MOVE_DISTANCE, GAME.stageInstance.getHeight()/2);
                GAME.player.interactSquare.setPosition(GAME.stageInstance.getWidth() - 3*MOVE_DISTANCE, GAME.stageInstance.getHeight()/2 - MOVE_DISTANCE);
            }
            else if(roomId == 2)
            {
                GAME.player.playerIcon.setPosition(4*MOVE_DISTANCE, (GAME.stageInstance.getHeight()-MOVE_DISTANCE)/2);
                GAME.player.moveSquare.setPosition(4*MOVE_DISTANCE, (GAME.stageInstance.getHeight()-MOVE_DISTANCE)/2);
                GAME.player.interactSquare.setPosition(3*MOVE_DISTANCE, GAME.stageInstance.getHeight()/2 - MOVE_DISTANCE);
            }
            roomId = 0;
            GAME.stageInstance.addActor(GAME.player.playerIcon);
            GAME.stageInstance.addActor(eventImage);
            goblin.reAddActor();
            if((int)(GAME.player.moveSquare.getX()- goblin.getXCord())/200 == 0 &&(int)(GAME.player.moveSquare.getY()-goblin.getYCord())/100 ==0 && isEnemyTurn) {
                goblin.attackPlayer();
                inventory.updateHealth();
                isEnemyTurn = false;
            }
            GAME.stageInstance.setKeyboardFocus(GAME.player.playerIcon);
        }

        isCollisionHandled(GAME.player, GAME.stageInstance);
    }

    @Override
    public void resize(int width, int height) {
        GAME.stageInstance.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(GAME.stageInstance);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * This program basically makes sure that when the player is exploring the dungeon, that it utilizes a common
     * boundary point so that there is a reduction in memory usage
     * @param player - The player instance to be compared against the stage instance
     * @param stage - The stage instance that the program is comparing
     * @return - Returns True if the program is handled at that time.
     */
    public boolean isCollisionHandled(Player player, Stage stage)
    {
        if(player.playerIcon.getX() <= 2*MOVE_DISTANCE)
        {
            player.playerIcon.setPosition(player.playerIcon.getX() + MOVE_DISTANCE, player.playerIcon.getY());
            player.moveSquare.setPosition(player.moveSquare.getX() + MOVE_DISTANCE, player.moveSquare.getY());
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);
        }
        else if(player.playerIcon.getY() <= 1*MOVE_DISTANCE)
        {
            player.playerIcon.setPosition(player.playerIcon.getX(), player.playerIcon.getY() + MOVE_DISTANCE);
            player.moveSquare.setPosition(player.moveSquare.getX(), player.moveSquare.getY() + MOVE_DISTANCE);
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);
        }
        else if(player.playerIcon.getX() >= stage.getWidth() - 3*MOVE_DISTANCE)
        {
            player.playerIcon.setPosition(player.playerIcon.getX() - MOVE_DISTANCE, player.playerIcon.getY());
            player.moveSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY());
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);
        }
        else if(player.playerIcon.getY() >= stage.getHeight() - 2*MOVE_DISTANCE)
        {
            player.playerIcon.setPosition(player.playerIcon.getX(), player.playerIcon.getY() - MOVE_DISTANCE);
            player.moveSquare.setPosition(player.moveSquare.getX(), player.moveSquare.getY() - MOVE_DISTANCE);
            player.interactSquare.setPosition(player.moveSquare.getX() - MOVE_DISTANCE, player.moveSquare.getY() - MOVE_DISTANCE);
        }

        return true;
    }


}
