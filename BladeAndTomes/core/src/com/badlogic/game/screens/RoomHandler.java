package com.badlogic.game.screens;

import ScreenOverlay.MainInventory;
import ScreenOverlayRework.OverlayManager;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.creatures.Goblin;
import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.Random;

public class RoomHandler {

    //Instantiates the random generator to be used within this project
    private Random generator = new Random();
    private boolean goblinTurn;
    public Goblin[] goblins = null;
    public int numOfGoblins;
    public int GRID_X_SQUARE = 21;
    public int GRID_Y_SQUARE = 12;
    public final int[] GRID_X = new int[GRID_X_SQUARE];
    public final int[] GRID_Y = new int[GRID_Y_SQUARE];
    private int remainingGoblin;
    private Texture backgroundText;

    //Thanks to baeldung.com for creating a tutorial based on how enum values work,
    //including section 3, which is what the following enum is based on.
    //https://www.baeldung.com/java-enum-values
    enum ROOMS {
        N("DungeonRooms/NRoom.png", 8),
        E("DungeonRooms/ERoom.png", 4),
        W("DungeonRooms/WRoom.png", 2),
        S("DungeonRooms/SRoom.png", 1),
        NE("DungeonRooms/NERoom.png", 12),
        NW("DungeonRooms/NWRoom.png", 10),
        NS("DungeonRooms/NSRoom.png", 9),
        EW("DungeonRooms/EWRoom.png", 6),
        ES("DungeonRooms/ESRoom.png", 5),
        WS("DungeonRooms/WSRoom.png", 3),
        NEW("DungeonRooms/NEWRoom.png", 14),
        NES("DungeonRooms/NESRoom.png", 13),
        NWS("DungeonRooms/NWSRoom.png", 11),
        EWS("DungeonRooms/EWSRoom.png", 7),
        NEWS("DungeonRooms/NEWSRoom.png", 15);

        private String filename;
        private int id;

        ROOMS(String filename, int id) {
            this.filename = filename;
            this.id = id;
        }
    }

    //Denotes the starting position of the player relative to where the player is entering.
    public Room level;
    public boolean combatFlag;
    public boolean eventFlag;
    private Stage stage;
    private Player player;
    private int levelNum;
    private OverlayManager inventory;

    /**
     * Constructor for the "RoomHandler" or the linked list handler
     */
    public RoomHandler(Stage stage, Player player, OverlayManager inventory) {
        this.player = player;
        this.stage = stage;
        this.level = new Room();
        this.levelNum = 1;
        this.combatFlag = false;
        this.eventFlag = false;
        this.numOfGoblins = 0;
        this.remainingGoblin = numOfGoblins;
        try{
        this.inventory = inventory;}catch (Exception e){

        }
        this.goblinTurn = false;

        int x_start = 264;
        int y_start = 152;
        for(int i = 0; i < GRID_X_SQUARE; i++) {
            GRID_X[i] = x_start + 64*(i+1);
        }
        for(int i = 0; i < GRID_Y_SQUARE; i++) {
            GRID_Y[i] = y_start + 64*(i+1);
        }
        goblins = new Goblin[0];
    }

    /**
     * Function that handles dungeon movement for the player and how the player moves between rooms
     * Functionally based on the original code programmed for the dungeon walls as made by Alex Facer
     * @return - Returns if the process was successful or not.
     */
    public boolean movement() {

        // If the player enters a door, then take that player to the new room if that room exists
        if(!combatFlag && !eventFlag && level.X_VAL[0] <= player.playerIcon.getX() &&
                level.X_VAL[0] + 3*level.MOVE >= player.playerIcon.getX() &&
                level.Y_VAL[0] <= player.playerIcon.getY()) {
            return moveIntoRoom(1);
        }
        else if(!combatFlag && !eventFlag && level.X_VAL[1] <= player.playerIcon.getX() &&
                level.Y_VAL[1] <= player.playerIcon.getY() &&
                level.Y_VAL[1] + 3*level.MOVE >= player.playerIcon.getY()) {
            return moveIntoRoom(2);
        }
        else if(!combatFlag && !eventFlag && level.X_VAL[2] >= player.playerIcon.getX() &&
                level.Y_VAL[2] + 2*level.MOVE >= player.playerIcon.getY() &&
                level.Y_VAL[2] <= player.playerIcon.getY()) {
            return moveIntoRoom(3);
        }
        else if(!combatFlag && !eventFlag && level.X_VAL[3] <= player.playerIcon.getX() &&
                level.X_VAL[3] + 3*level.MOVE >= player.playerIcon.getX() &&
                level.Y_VAL[3] >= player.playerIcon.getY()) {
            return moveIntoRoom(4);
        }

        // Makes sure player does not leave bounds
        else {
            return isCollisionHandled();
        }
    }

    /**
     * A function that handles moving into a room, whether it be generating a new room or
     * entering a room already determined by the program
     * @param entrance - Where the player enters at. 1 = N, 2 = E, 3 = W, 4 = S
     * @return - Whether or not the operation was successful
     */
    private boolean moveIntoRoom(int entrance) {
        //Moves onto next room
        //Room temp = level;

        combatFlag = false;
        eventFlag = false;

        //Determines player exit if successful
        int exit = entrance == 1 ? 4 : entrance == 2 ? 3 : entrance == 3 ? 2 : 1;

        //Determine if a door exists at the point specified
        boolean isDoorThere = ((level.getRoomID() >> (exit-1)) & 1) == 1;

        //If a door exists and room is not generated
        if(level.getDoorSingle(entrance) == null && isDoorThere) {
            generateRoom(entrance);
        }

        //Else if there is no door there, handle collision with wall
        else if(level.getDoorSingle(entrance) == null) {
            return isCollisionHandled();
        }

        //If the door does exist but the level does not, generate it
        else if(level != null) {
            level = level.getDoorSingle(entrance);
        }

        //Determines exit point of the player
        int x_offset = exit == 2 ? GRID_X[20] : exit == 3 ? GRID_X[0] : GRID_X[10];
        int y_offset = exit == 1 ? GRID_Y[11] : exit == 4 ? GRID_Y[0] : GRID_Y[5];


        //Clears out the stage and re-adds everything
        player.playerIcon.setPosition(x_offset, y_offset);
        stage.clear();
        level.getBackgroundImage().setSize(2000, 1150);
        level.getBackgroundImage().setPosition(-25, -20);
        stage.addActor(level.getBackgroundImage());
        stage.addActor(player.playerIcon);
        stage.setKeyboardFocus(player.playerIcon);

        //If generated combat, spawn the goblins
        if(combatFlag) {
            spawnGoblin();
        }

        //Makes sure to re-add inventory UI after movement
//        inventory.reAddInventory();
        try {
            inventory.setOverLayesVisibility(true);
            if (Gdx.input.isKeyJustPressed(Input.Keys.E))
                inventory.setHiddenTableVisibility(true);
        }catch (Exception e){

        }

        return true;
    }

    /**
     * Function that goes through a series of statements and random generation in order to retrieve
     * a certain room.
     * @param entrance - Indicates the entrance the player went into
     * @return - returns if generation was successful or not
     */
    private boolean generateRoom(int entrance) {

        // Creates placeholder for creating doubly linked list
        Room temp = level;

        //Create default room
        level.setDoorIndividual(new Room(), entrance);
        level = level.getDoorSingle(entrance);


        //Generate exit point based on entrance
        int exit = entrance == 1 ? 4 : entrance == 2 ? 3 :
                        entrance == 3 ? 2 : 1;

        //temp.setDoorIndividual(level, entrance);
        level.setDoorIndividual(temp, exit);

        //Randomly generate number of doors and set it for room
        int numOfDoors = generator.nextInt(4) + 1;
        level.setNumOfDoors(numOfDoors);

        int baseID = 1 << (entrance - 1);

        //Using the number of doors in that room, find the appropriate roomID
        level.setRoomID(generateRoomID(baseID, numOfDoors));

        //For each loop dedicated to getting each different type of room
        ROOMS[] superRoom = ROOMS.values();
        for(ROOMS room : superRoom) {
            if(room.id == level.getRoomID()) {
                level.setBackgroundImage(room.filename, stage);
            }
        }

        //generates event. Please see TODO
        generateEvent();

        //Return true upon successful completion of room creation
        return true;
    }

    /**
     * Deconstructs the entire level in case of save or end of level, requiring a new level
     * @param EOL - Denotes End of the level
     */
    public void deconstruct(boolean EOL) {
        if (EOL) {
            levelNum++;
            level = new Room();
        }
        else {
            //TODO: Save current level to Save file, including all explored and unexplored rooms
        }
    }

    /**
     * Simple room generator based on the number of doors within the room and the base ID
     * @param baseID - Given an id with exactly one door based on the entrance
     * @param numDoor - Number of doors within the actual room
     * @return
     */
    private int generateRoomID(int baseID, int numDoor) {

        //Checks to make sure that it's not a single room
        if(numDoor == 1)
            return baseID;

        //Initializing internal function variables
        int id = baseID;
        int temp = baseID;
        int remainDoor = numDoor - 1;

        //Generates a room id based on the locations of the doors and shifts bits accordingly
        while(remainDoor != 0) {
            int location = generator.nextInt(4);
            temp = temp | 1 << location;
            if (id != temp) {
                id = temp;
                remainDoor--;
            }
        }

        //Returns the room id of the Room
        return id;
    }

    /**
     * Generates the event needed for the program to work
     * @return error code in case the
     */
    private int generateEvent() {

        //Roll a d12 to determine if combat occurred
        int num = generator.nextInt(12) + 1;

        //If above 8, declare that combat has occured
        if(num > 8) {
            combatFlag = true;
        }

        //Generates events
        else if (num > 2) {
            eventFlag = true;
        }
        return 0;
    }

    /**
     * This program basically makes sure that when the player is exploring the dungeon, that it utilizes a common
     * boundary point so that there is a reduction in memory usage
     * @return - Returns True if the program is handled at that time.
     */
    public boolean isCollisionHandled()
    {
        if(player.playerIcon.getX() <= level.X_VAL[2])
        {
            player.playerIcon.setPosition(player.playerIcon.getX() + level.MOVE, player.playerIcon.getY());
            player.moveSquare.setPosition(player.moveSquare.getX() + level.MOVE, player.moveSquare.getY());
            player.interactSquare.setPosition(player.moveSquare.getX() - level.MOVE, player.moveSquare.getY() - level.MOVE);
        }
        else if(player.playerIcon.getY() <= level.Y_VAL[3])
        {
            player.playerIcon.setPosition(player.playerIcon.getX(), player.playerIcon.getY() + level.MOVE);
            player.moveSquare.setPosition(player.moveSquare.getX(), player.moveSquare.getY() + level.MOVE);
            player.interactSquare.setPosition(player.moveSquare.getX() - level.MOVE, player.moveSquare.getY() - level.MOVE);
        }
        else if(player.playerIcon.getX() >= level.X_VAL[1])
        {
            player.playerIcon.setPosition(player.playerIcon.getX() - level.MOVE, player.playerIcon.getY());
            player.moveSquare.setPosition(player.moveSquare.getX() - level.MOVE, player.moveSquare.getY());
            player.interactSquare.setPosition(player.moveSquare.getX() - level.MOVE, player.moveSquare.getY() - level.MOVE);
        }
        else if(player.playerIcon.getY() >= level.Y_VAL[0])
        {
            player.playerIcon.setPosition(player.playerIcon.getX(), player.playerIcon.getY() - level.MOVE);
            player.moveSquare.setPosition(player.moveSquare.getX(), player.moveSquare.getY() - level.MOVE);
            player.interactSquare.setPosition(player.moveSquare.getX() - level.MOVE, player.moveSquare.getY() - level.MOVE);
        }

        return true;
    }

    /**
     * Method Spawns Goblins to be used in the moveIntoRoom() function.
     */
    public void spawnGoblin() {
        numOfGoblins = generator.nextInt(3) + 1;
        remainingGoblin = numOfGoblins;

        goblins = new Goblin[numOfGoblins];

        for(int i=0; i<numOfGoblins; i++) {

            //Creates a new goblin
            goblins[i] = new Goblin(player);

            //Randomizes goblin positions based on "Grid"
            goblins[i].enemyImage.setPosition(GRID_X[generator.nextInt(GRID_X_SQUARE)],
                    GRID_Y[generator.nextInt(GRID_Y_SQUARE)]);

            //Adds goblins to the stage
            stage.addActor(goblins[i].enemyImage);
        }
    }

    public Goblin[] getGoblins() { return goblins; }

    /**
     * Checks to make sure the player is not going into the goblin's space and moves the player to be in front of the
     * goblin if so.
     */
    public void collisionBodyHandler() {
        for(int i = 0; i<numOfGoblins; i++) {
            if(goblins[i] == null) {
                continue;
            }
            float x_distance = goblins[i].enemyImage.getX() - player.playerIcon.getX();
            float y_distance = goblins[i].enemyImage.getY() - player.playerIcon.getY();
            if(x_distance < 64 && x_distance > -64 &&
                    y_distance < 64 && y_distance > -64) {
                player.playerIcon.setPosition(player.playerIcon.getX() - level.MOVE, player.playerIcon.getY());
            }
        }
    }

    /**
     * Major method that simplifies and works through combat with enemy creatures (those being goblins).
     */
    public void handleCombat() {
        movement();

        if(!player.isTurn) {
            for(int i = 0; i<numOfGoblins; i++) {
                if(goblins[i] == null) {
                    continue;
                }
                goblins[i].isTurn = true;
            }
        }

        collisionBodyHandler();

        //Player attack based on Miller Banford's original code, but modified to check for squad of goblins
        for(int i = 0; i<numOfGoblins; i++) {
            if(goblins[i] == null) {
                continue;
            }
            float x_distance = goblins[i].enemyImage.getX() - player.playerIcon.getX();
            float y_distance = goblins[i].enemyImage.getY() - player.playerIcon.getY();
            if (Gdx.input.isKeyJustPressed(Input.Keys.Q) &&
                    x_distance < 96 && x_distance > -96 &&
                    y_distance < 96 && y_distance > -96 &&
                    player.isTurn) {
                int hitRoll = generator.nextInt(20) + 1;
                if (hitRoll >= goblins[i].getArmorPoints()) {
                    goblins[i].damageTaken(player.getPhysical());
                }
                player.isTurn = false;

                for (int j = 0; j < numOfGoblins; j++) {
                    if (goblins[i] == null) {
                        continue;
                    }
                    goblins[i].isTurn = true;
                }
            }

            if (goblins[i].checkIfDead()) {
                goblins[i].remove();
                goblins[i] = null;
                remainingGoblin--;
                if(remainingGoblin == 0) {
                    combatFlag = false;
                    numOfGoblins = 0;
                    return;
                }
            }

            if(!player.isTurn && goblins[i] != null){

                if(goblins[i].movement()) {
                    if(i + 1 == numOfGoblins) {
                        player.isTurn = true;
                    }
                    goblins[i].isTurn = false;
                }

                //Goblin Attack based on Miller Banford's original code
                if(goblins[i].isTurn && x_distance < 96 && x_distance > -96 &&
                        y_distance < 96 && y_distance > -96) {
                    player.setHealthPoints(player.getHealthPoints() - goblins[i].attackPlayer());
                    try{
                    inventory.updateHealth();}
                    catch (Exception e){

                    }
                    goblins[i].isTurn = false;
                    if(i + 1 == numOfGoblins) {
                        player.isTurn = true;
                    }
                }
            }
        }

    }
}
