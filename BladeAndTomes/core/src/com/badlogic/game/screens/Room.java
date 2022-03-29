package com.badlogic.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Room {

    //Private variables denoting the attributes of a room
    private Room door[];
    private int numOfDoors;
    private int roomID;
    private int mapID;
    private Image background;
    private Texture backgroundText;
    private AssetManager assetManager;

    //Constants to be used for denoting location and movement
    final public int MOVE = 64;
    final public int X_VAL[] = {Gdx.graphics.getWidth()/2, Gdx.graphics.getWidth() - MOVE, MOVE*4, Gdx.graphics.getWidth()/2};
    final public int Y_VAL[] = {Gdx.graphics.getHeight(), Gdx.graphics.getHeight()/2,  Gdx.graphics.getHeight()/2, MOVE*2};

    /**
     * Public constructor for creating a room. Usually just a blank, default room
     */
    public Room() {

        //Thanks to user Tenfour04 on StackOverflow for introducing a way to easily dispose of assets.
        //https://stackoverflow.com/questions/38102499/libgdx-image-class-dispose
        assetManager = new AssetManager();
        assetManager.load("DungeonRooms/SRoom.png", Texture.class);
        assetManager.finishLoading();
        this.background = new Image(assetManager.get("DungeonRooms/SRoom.png", Texture.class));

        this.door = new Room[4];
        this.numOfDoors = 1;
        this.roomID = 1;
        this.mapID = 0;
    }

    /**
     * Paramaterized Constructor for the room
     * @param background - Image used for the room background
     * @param door - Link to the next rooms
     * @param numOfDoors - States the number of doors available
     * @param roomID - Gives the roomID of the area (based on NEWS)
     * @param mapID - Gives basic map id to identify special rooms
     */
    public Room(Image background, Room door[], int numOfDoors, int roomID, int mapID) {
        this.background = background;
        this.door = door;
        this.numOfDoors = numOfDoors;
        this.roomID = roomID;
        this.mapID = mapID;
    }


    /**
     * Sets the image background for the room
     * @param directory - the directory or location of image relative to the internal project
     */
    public void setBackgroundImage(String directory, Stage stage) {
        this.background.remove();

        //Thanks to user Tenfour04 on StackOverflow for introducing a way to easily dispose of assets.
        //https://stackoverflow.com/questions/38102499/libgdx-image-class-dispose
        assetManager.clear();
        assetManager.load(directory, Texture.class);
        assetManager.finishLoading();
        this.background = new Image(assetManager.get(directory, Texture.class));


        stage.addActor(this.background);
    }

    /**
     * Gets the mapID of the room
     * @return the map ID for referencing purposes
     */
    public int getMapID() {
        return this.mapID;
    }

    /**
     * Gets the links to the doors
     * @return
     */
    public Room[] getDoor() {
        return this.door;
    }

    /**
     * Gets only one of the links to the next room
     * @return
     */
    public Room getDoorSingle(int i) {
        return this.door[i-1];
    }

    /**
     * Gets the number of doors within a room
     * @return
     */
    public int getNumOfDoors () {
        return this.numOfDoors;
    }

    /**
     * Gets the id of a room for debugging and sprting purposes
     * @return
     */
    public int getRoomID () {
        return this.roomID;
    }

    public Image getBackgroundImage() {
        return this.background;
    }

    /**
     * Allows the user to set the full array for links to the room
     * @param door - The array of links for differing doors
     */
    public void setDoor(Room door[]) {
        this.door = door;
    }

    /**
     * This sets the individual link for the door array to allow for the assignment of new rooms
     * @param door - The link you want to insert into the array
     * @param i - This is the doorID that leads into the next room
     */
    public void setDoorIndividual(Room door, int i) {
        this.door[i-1] = door;
    }

    /**
     * This function sets the number of doors in a room.
     * @param numOfDoors - The number of doors in a room
     */
    public void setNumOfDoors(int numOfDoors) {
        this.numOfDoors = numOfDoors;
    }

    /**
     * Sets the roomID, which is based on a binary conversion of where doors are based on
     * the four cardinal directions: North, East, West, and South.
     * @param roomID - The identification of the room in terms of what configuration it is
     */
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    /**
     * This sets the mapID, which identifies if an event has occured, and if that event is combat or not
     * @param mapID - Gives the mapID to allow for defining if it has/has an event or not.
     */
    public void setMapID(int mapID) {
        this.mapID = mapID;
    }
}