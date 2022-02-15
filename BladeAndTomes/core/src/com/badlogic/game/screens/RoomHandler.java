package com.badlogic.game.screens;

import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.Random;

public class RoomHandler {

    //Instantiates the random generator to be used within this project
    private Random generator = new Random();

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
    private Stage stage;
    private Player player;
    private int levelNum;

    /**
     * Constructor for the "RoomHandler" or the linked list handler
     */
    public RoomHandler(Stage stage, Player player) {
        this.player = player;
        this.stage = stage;
        this.level = new Room();
        this.levelNum = 1;
    }

    /**
     * Function that handles dungeon movement for the player and how the player moves between rooms
     * Functionally based on the original code programmed for the dungeon walls as made by Alex Facer
     * @return - Returns if the process was successful or not.
     */
    public boolean movement() {

        // If the player enters a door, then take that player to the new room if that room exists
        if(level.X_VAL[0] - level.MOVE <= player.playerIcon.getX() &&
                level.X_VAL[0] + level.MOVE >= player.playerIcon.getX() &&
                level.Y_VAL[0] - level.MOVE <= player.playerIcon.getY()) {
            return moveIntoRoom(1);
        }
        else if(level.X_VAL[1] <= player.playerIcon.getX() &&
                level.Y_VAL[1] - level.MOVE <= player.playerIcon.getY() &&
                level.Y_VAL[1] + level.MOVE >= player.playerIcon.getY()) {
            return moveIntoRoom(2);
        }
        else if(level.X_VAL[2] <= player.playerIcon.getX() &&
                level.Y_VAL[2] - level.MOVE >= player.playerIcon.getY() &&
                level.Y_VAL[2] + level.MOVE <= player.playerIcon.getY()) {
            return moveIntoRoom(3);
        }
        else if(level.X_VAL[3] - level.MOVE <= player.playerIcon.getX() &&
                level.X_VAL[3] + level.MOVE >= player.playerIcon.getX() &&
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

        int exit = entrance == 1 ? 4 : entrance == 2 ? 3 : entrance == 3 ? 2 : 1;

        boolean isDoorThere = ((level.getRoomID() >> (exit-1)) & 1) == 1;

        //level = level.getDoorSingle(entrance);

        //If a door exists and room is not generated
        if(level.getDoorSingle(entrance) == null && isDoorThere) {
            generateRoom(entrance);
        }
        else if(level.getDoorSingle(entrance) == null) {
            return isCollisionHandled();
        }
        else if(level != null) {
            level = level.getDoorSingle(entrance);
        }

        int x_offset = exit == 2 ? -2*level.MOVE : exit == 3 ? 2*level.MOVE : 0;
        int y_offset = exit == 1 ? -2*level.MOVE : exit == 4 ? 2*level.MOVE : 0;

        player.playerIcon.setPosition(level.X_VAL[exit-1] + x_offset, level.Y_VAL[exit-1] + y_offset);
        stage.clear();
        level.getBackgroundImage().setSize(2000, 1150);
        level.getBackgroundImage().setPosition(-25, -20);
        stage.addActor(level.getBackgroundImage());
        stage.addActor(player.playerIcon);
        stage.setKeyboardFocus(player.playerIcon);

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

        //
        while(remainDoor != 0) {
            int location = generator.nextInt(4);
            temp = temp | 1 << location;
            if (id != temp) {
                id = temp;
                remainDoor--;
            }
        }

        //
        return id;
    }

    /**
     * Generates the event needed for the program to work
     * @return error code in case the
     */
    private int generateEvent() {
        if(generator.nextInt(12) > 7) {
            //TODO: Initiate combat here.
        }

        else if (generator.nextInt(12) > 2) {
            //TODO: Initiate event handle here.
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
        if(player.playerIcon.getX() <= 2*level.MOVE)
        {
            player.playerIcon.setPosition(player.playerIcon.getX() + level.MOVE, player.playerIcon.getY());
            player.moveSquare.setPosition(player.moveSquare.getX() + level.MOVE, player.moveSquare.getY());
            player.interactSquare.setPosition(player.moveSquare.getX() - level.MOVE, player.moveSquare.getY() - level.MOVE);
        }
        else if(player.playerIcon.getY() <= 1*level.MOVE)
        {
            player.playerIcon.setPosition(player.playerIcon.getX(), player.playerIcon.getY() + level.MOVE);
            player.moveSquare.setPosition(player.moveSquare.getX(), player.moveSquare.getY() + level.MOVE);
            player.interactSquare.setPosition(player.moveSquare.getX() - level.MOVE, player.moveSquare.getY() - level.MOVE);
        }
        else if(player.playerIcon.getX() >= stage.getWidth() - 3*level.MOVE)
        {
            player.playerIcon.setPosition(player.playerIcon.getX() - level.MOVE, player.playerIcon.getY());
            player.moveSquare.setPosition(player.moveSquare.getX() - level.MOVE, player.moveSquare.getY());
            player.interactSquare.setPosition(player.moveSquare.getX() - level.MOVE, player.moveSquare.getY() - level.MOVE);
        }
        else if(player.playerIcon.getY() >= stage.getHeight() - 2*level.MOVE)
        {
            player.playerIcon.setPosition(player.playerIcon.getX(), player.playerIcon.getY() - level.MOVE);
            player.moveSquare.setPosition(player.moveSquare.getX(), player.moveSquare.getY() - level.MOVE);
            player.interactSquare.setPosition(player.moveSquare.getX() - level.MOVE, player.moveSquare.getY() - level.MOVE);
        }

        return true;
    }
}
