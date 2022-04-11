package com.badlogic.game.screens;

import ScreenOverlayRework.Inventory.TreasureChest.TreasureChestUI;
import ScreenOverlayRework.OverlayManager;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.creatures.Goblin;
import com.badlogic.game.creatures.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.Random;

public class RoomHandler {

    //Instantiates the random generator to be used within this project
    private Random generator = new Random();
    public Goblin[] goblins = null;
    public int numOfGoblins;
    public final int MAX_GEN_LEVELS = 5;
    private int goblinsKilled;

    private int remainingGoblin;
    private int levelsGenerated;
    private int levelsExplored;

    private boolean exitAvailable;
    private boolean endOfLevel;
    public static boolean chestVisible;

    private float levelMultiplier;

    //private boolean roomLimit[][] = new boolean[10][10];
    private int X_VAL[];
    private int Y_VAL[];

    private boolean rangedFlag;
    private boolean magicFlag;
    private boolean meleeFlag;
    private int selectionIndex;
    private float x_distance;
    private float y_distance;

    float eventX, eventY, eventSizeX, eventSizeY;

    Texture eventTex;
    Image eventImage;

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
    private Image selectionImage;

    private TreasureChestUI chest;
    private final BladeAndTomes game;

    /**
     * Constructor for the "RoomHandler" or the linked list handler
     */
    public RoomHandler(Stage stage, final Player player, OverlayManager inventory, final BladeAndTomes game) {
        this.player = player;
        this.stage = stage;
        this.game = game;
        this.level = new Room();
        this.levelMultiplier = 1;
        this.levelNum = 1;
        this.exitAvailable = false;
        this.combatFlag = false;
        this.eventFlag = false;
        this.numOfGoblins = 0;
        this.levelsGenerated = 0;
        this.levelsExplored = 0;
        this.goblinsKilled = 0;
        //this.roomLimit[4][4] = true;
        this.remainingGoblin = numOfGoblins;
        this.rangedFlag = false;
        //this.roomLimit[4][5] = true;
        this.selectionImage = new Image(new Texture(Gdx.files.internal("selection.png")));

        //level.setStage(stage);
        player.playerIcon.setVisible(false);

        // Textures rendered in for our event
        // currently giving it a reasonable range to spawn into, and keeping it in dungeon 1
        eventTex = new Texture(Gdx.files.internal("GoldChest.jpg"));
        eventImage = new Image(eventTex);

        eventSizeX = 64f;
        eventSizeY = 64f;
        eventImage.setSize(eventSizeX, eventSizeY);

        //Determining distance and selections to open chest
        this.selectionIndex = 0;
        this.x_distance = 0;
        this.y_distance = 0;

        selectionImage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == game.controls.getMoveRight()) {
                    if(selectionIndex >= numOfGoblins-1) {
                        selectionIndex = 0;
                    }
                    else {
                        ++selectionIndex;
                    }

                    while(goblins[selectionIndex] == null) {
                        ++selectionIndex;
                        if(selectionIndex == numOfGoblins) {
                            selectionIndex = 0;
                        }
                    }

                    selectionImage.addAction(Actions.moveTo(goblins[selectionIndex].enemyImage.getX(), goblins[selectionIndex].enemyImage.getY()));

                }
                else if(keycode == game.controls.getMoveLeft()) {
                    if(selectionIndex <= 0) {
                        selectionIndex = numOfGoblins - 1;
                    }
                    else {
                        --selectionIndex;
                    }

                    while(goblins[selectionIndex] == null) {
                        --selectionIndex;
                        if(selectionIndex < 0) {
                            selectionIndex = numOfGoblins - 1;
                        }
                    }

                    selectionImage.addAction(Actions.moveTo(goblins[selectionIndex].enemyImage.getX(), goblins[selectionIndex].enemyImage.getY()));

                }

                x_distance = goblins[selectionIndex].enemyImage.getX() - player.playerIcon.getX();
                y_distance = goblins[selectionIndex].enemyImage.getY() - player.playerIcon.getY();

                return true;
            }
        });

        try {
            this.inventory = inventory;
        } catch (Exception e) {

        }
        //this.goblinTurn = false;

        //Arrays marking the approximate locations of the four doors
        X_VAL = new int[4];
        Y_VAL = new int[4];

        X_VAL[0] = (int) stage.getWidth()/2;
        X_VAL[1] = (int) stage.getWidth() - 3* level.MOVE;
        X_VAL[2] = level.MOVE*3;
        X_VAL[3] = (int) stage.getWidth()/2;

        Y_VAL[0] = (int) stage.getHeight() - 2*level.MOVE;
        Y_VAL[1] = (int) stage.getHeight()/2;
        Y_VAL[2] = (int) stage.getHeight()/2;
        Y_VAL[3] = level.MOVE*2;

        //Makes sure to start from the starter room and moves on from there
        generateLevelLayout();
    }

    /**
     * Continuously generates several levels so that it always ensures that the player can explore 10
     * Rooms before moving on to the next "level".
     *  - Where the door entrance the recursive function goes through is.
     */
    public void generateLevelLayout() {
        Room temp = this.level;
        for(int i = 0; i < 4; i++) {
            if (temp.getDoorSingle(i+1) == null &&
                    ((temp.getRoomID() >> (3-i)) & 1) == 1 ) {

                generateRoom(i+1, false, temp, false);

                temp = temp.getDoorSingle(i+1);

                levelsGenerated++;

                if(levelsGenerated >= MAX_GEN_LEVELS){
                    return;
                }
                generateLevelLayout();
            }
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
        if(!combatFlag && !eventFlag && X_VAL[0] - 2*level.MOVE <= player.playerIcon.getX() &&
                X_VAL[0] + 2*level.MOVE >= player.playerIcon.getX() &&
                Y_VAL[0] <= player.playerIcon.getY()) {
            return moveIntoRoom(1);
        }
        else if(!combatFlag && !eventFlag && X_VAL[1] <= player.playerIcon.getX() &&
                Y_VAL[1] - 2*level.MOVE <= player.playerIcon.getY() &&
                Y_VAL[1] + 1*level.MOVE >= player.playerIcon.getY()) {
            return moveIntoRoom(2);
        }
        else if(!combatFlag && !eventFlag && X_VAL[2] >= player.playerIcon.getX() &&
                Y_VAL[2] + 1*level.MOVE >= player.playerIcon.getY() &&
                Y_VAL[2] - 2*level.MOVE <= player.playerIcon.getY()) {
            return moveIntoRoom(3);
        }
        else if(!combatFlag && !eventFlag && X_VAL[3] - 2*level.MOVE <= player.playerIcon.getX() &&
                X_VAL[3] + 2*level.MOVE >= player.playerIcon.getX() &&
                Y_VAL[3] >= player.playerIcon.getY()) {
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
            generateRoom(entrance, true, level, true);
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
        int x_offset = exit == 2 ? game.GRID_X[20] : exit == 3 ? game.GRID_X[0] : game.GRID_X[10];
        int y_offset = exit == 1 ? game.GRID_Y[11] : exit == 4 ? game.GRID_Y[0] : game.GRID_Y[5];


        //Clears out the stage and re-adds everything
        player.playerIcon.clearActions();
        player.playerIcon.setPosition(x_offset, y_offset);
        stage.clear();
        //level.getBackgroundImage().setSize(2000, 1150);
        //level.getBackgroundImage().setPosition(-25, -20);
        //stage.addActor(level.getBackgroundImage());
        stage.addActor(player.playerIcon);
        stage.setKeyboardFocus(player.playerIcon);

        if (level.getMapID() == 10) {
            Image portal = new Image(new Texture(Gdx.files.internal("PortalToDungeon.jpg")));
            portal.setPosition(game.GRID_X[10], game.GRID_Y[5]);
            stage.addActor(portal);
        }

        //If generated combat, spawn the goblins
        if(level.getMapID() == 1) {
            combatFlag = true;
            spawnGoblin();
        }

        if(level.getMapID() == 2) {
            // Currently having size as a set variable here want to move it to events class
            // This should keep it permanently in place through the dungeon right now
            // Based off of Brent Able's simple code in Dungeon.Java where eventImage is the chest image
            eventImage.setPosition(level.getChestX(), level.getChestY());
            stage.addActor(eventImage);
        }

        //this.levelsExplored = level.getMapID() == 0 && !level.isLevelExplored() ? this.levelsExplored + 1 : this.levelsExplored;

        if(!level.isLevelExplored()) {
            this.levelsExplored++;
            player.kDungeonsExplored++;
            level.setIsLevelExplored(true);
        }

        //Makes sure to re-add inventory UI after movement
        //inventory.reAddInventory();
        try {
            //inventory.setOverLayesVisibility(true);
            if (Gdx.input.isKeyJustPressed(game.controls.getOpenInventory()))
                inventory.setHiddenTableVisibility(true);

            //Based off of Anirudh's suggestion to fix the inventory and allow for selection within the dungeon
            game.overlays = new OverlayManager(game);
            game.overlays.setOverLayesVisibility(true);
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
    private boolean generateRoom(int entrance, boolean moveIn, Room chamber, boolean isGenLevel) {

        // Creates placeholder for creating doubly linked list
        Room temp = chamber;

        //Create default room
        chamber.setDoorIndividual(new Room(), entrance);
        chamber = chamber.getDoorSingle(entrance);

        if(!moveIn) {
            switch (entrance) {
                case 1:
                    chamber.setRoomY(temp.getRoomX() + 1);
                    break;
                case 2:
                    chamber.setRoomX(temp.getRoomX() + 1);
                    break;
                case 3:
                    chamber.setRoomX(temp.getRoomX() - 1);
                    break;
                case 4:
                    chamber.setRoomY(temp.getRoomX() - 1);
                    break;
                default:
                    break;
            }
        }

        //Generate exit point based on entrance
        int exit = entrance == 1 ? 4 : entrance == 2 ? 3 :
                        entrance == 3 ? 2 : 1;

        //temp.setDoorIndividual(level, entrance);
        chamber.setDoorIndividual(temp, exit);

        //Randomly generate number of doors and set it for room
        int numOfDoors = levelsGenerated >= 10 ? generator.nextInt(4) + 1 : generator.nextInt(3) + 2;
        chamber.setNumOfDoors(numOfDoors);

        int baseID = 1 << (entrance - 1);

        //Using the number of doors in that room, find the appropriate roomID
        chamber.setRoomID(generateRoomID(baseID, numOfDoors));

        //For each loop dedicated to getting each different type of room
        ROOMS[] superRoom = ROOMS.values();
        for(ROOMS room : superRoom) {
            if(room.id == chamber.getRoomID()) {
                chamber.setBackgroundImage(room.filename);
            }
        }

        //generates event. Please see TODO
        generateEvent(chamber);

        //flags level to make sure that the level is actually supposed to be moved into
        if(isGenLevel) {
            level = chamber;
        }

        //Return true upon successful completion of room creation
        return true;
    }

    /**
     * Deconstructs the entire level in case of save or end of level, requiring a new level
     * @param EOL - Denotes End of the level
     */
    public void deconstruct(boolean EOL) {
        if (!EOL) {
            levelNum++;
            levelMultiplier += 5;
            levelsExplored = 0;
            level = new Room();
        }
        else {
            levelNum = 0;
            levelMultiplier = 3;
            endOfLevel = true;
        }

        player.kLevelsCompleted++;
        player.kEarnedGoldThroughLevels++;
    }

    /**
     * Simple room generator based on the number of doors within the room and the base ID
     * @param baseID - Given an id with exactly one door based on the entrance
     * @param numDoor - Number of doors within the actual room
     * @return
     */
    private int generateRoomID(int baseID, int numDoor) {

        //Checks to make sure that it's not a single room
        if(numDoor == 1 && levelsGenerated >= MAX_GEN_LEVELS) {
            return baseID;
        }

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
    private int generateEvent(Room chamber) {

        //Roll a d12 to determine if combat occurred
        int num = generator.nextInt(12) + 1;

        if(levelsExplored >= 5 && !exitAvailable) {
            chamber.setMapID(10);
            exitAvailable = true;
        }

        else if(levelsExplored >= 4 &&
        levelNum == 3) {
            // TODO: Create simple boss
        }

        //If above 8, declare that combat has occured
        else if(num > 7) {
            //combatFlag = true;
            chamber.setMapID(1);
        } //Generates events
        /*else if (num > 2) {

        }*/
        else if (num < 2) {
            //eventX = MathUtils.random(360, 1600);
            //eventY = MathUtils.random(240, 960);
            chamber.setMapID(2);
            chamber.setChestX(game.GRID_X[generator.nextInt(game.GRID_X_SQUARE)]);
            chamber.setChestY(game.GRID_Y[generator.nextInt(game.GRID_Y_SQUARE)]);
            try {
                chamber.setChest(game.overlays.generateChest());
            } catch (Exception e) {

            }
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
        if(player.playerIcon.getX() <= X_VAL[2])
        {
            player.playerIcon.setPosition(player.playerIcon.getX() + level.MOVE, player.playerIcon.getY());
            player.moveSquare.setPosition(player.moveSquare.getX() + level.MOVE, player.moveSquare.getY());
            player.interactSquare.setPosition(player.moveSquare.getX() - level.MOVE, player.moveSquare.getY() - level.MOVE);
        }
        else if(player.playerIcon.getY() <= Y_VAL[3])
        {
            player.playerIcon.setPosition(player.playerIcon.getX(), player.playerIcon.getY() + level.MOVE);
            player.moveSquare.setPosition(player.moveSquare.getX(), player.moveSquare.getY() + level.MOVE);
            player.interactSquare.setPosition(player.moveSquare.getX() - level.MOVE, player.moveSquare.getY() - level.MOVE);
        }
        else if(player.playerIcon.getX() >= X_VAL[1])
        {
            player.playerIcon.setPosition(player.playerIcon.getX() - level.MOVE, player.playerIcon.getY());
            player.moveSquare.setPosition(player.moveSquare.getX() - level.MOVE, player.moveSquare.getY());
            player.interactSquare.setPosition(player.moveSquare.getX() - level.MOVE, player.moveSquare.getY() - level.MOVE);
        }
        else if(player.playerIcon.getY() >= Y_VAL[0])
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
    public void spawnGoblin(){
        numOfGoblins = generator.nextInt(3) + 1;
        player.setNumGoblins(numOfGoblins);
        remainingGoblin = numOfGoblins;

        goblins = new Goblin[numOfGoblins];
        player.setGoblins(goblins);

        for(int i=0; i<numOfGoblins; i++) {
            //Creates a new goblin
            goblins[i] = new Goblin(player, game.GRID_X, game.GRID_Y);

            //goblins[i].enemyImage.setVisible(true);

            //Randomizes goblin positions based on "Grid"
            goblins[i].enemyImage.setPosition(game.GRID_X[generator.nextInt(game.GRID_X_SQUARE)],
                    game.GRID_Y[generator.nextInt(game.GRID_Y_SQUARE)]);
            goblins[i].prevX = (int) goblins[i].enemyImage.getX();
            goblins[i].prevY = (int) goblins[i].enemyImage.getY();

            //Adds goblins to the stage
            stage.addActor(goblins[i].enemyImage);
            goblins[i].healthBar.setPosition(goblins[i].enemyImage.getX(), goblins[i].enemyImage.getY()-12);
            goblins[i].healthBar.setSize(64, 12);
            stage.addActor(goblins[i].healthBar);
        }
    }

    /**
     * Based off of Anirudh Oruganti's code in Overworld.java to test the chest and make sure it works
     * Allows the user to open the chest and look at the contents inside of it.
     * @return - Makes sure that the program goes through the complete aspect of opening a chest
     */
    public boolean checkTouchChest() {
        x_distance = player.playerIcon.getX() - eventImage.getX();
        y_distance = player.playerIcon.getY() - eventImage.getY();

        //Based off of Anirudh Oruganti's code in Overworld for getting the chest to display
        //When treademenu key pressed, open/close chest and prevent/allow player movement
        if(Gdx.input.isKeyJustPressed(game.controls.getTradeMenu()) &&
                x_distance < 96 && x_distance > -96 &&
                y_distance < 96 && y_distance > -96) {
            level.getChest().setTreasureChestVisible(!level.getChest().isTreasureChestVisible());
            stage.setKeyboardFocus(level.getChest().isTreasureChestVisible() ? null : player.playerIcon);
            game.overlays.setHiddenTableVisibility(false);
            game.overlays.displayChest(level.getChest());
            player.kChestsOpened++;
            return true;
        } else {
            return false;
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

            //Distance between player and goblin
            float x_distance = goblins[i].enemyImage.getX() - player.playerIcon.getX();
            float y_distance = goblins[i].enemyImage.getY() - player.playerIcon.getY();

            //Pushes player back if player tries to walk into Goblin
            if(x_distance < 64 && x_distance > -64 &&
                    y_distance < 64 && y_distance > -64) {
                player.playerIcon.setPosition(player.getPrevX(), player.getPrevY());
            }

            /*
            int changeInX = (int) goblins[i].enemyImage.getX() - goblins[i].prevX;
            int changeInY = (int) goblins[i].enemyImage.getY() - goblins[i].prevY;

            if(goblins[i].enemyImage.getX() != (goblins[i].prevX + changeInX) ||
                goblins[i].enemyImage.getY() != (goblins[i].prevY + changeInY)) {
                goblins[i].enemyImage.setPosition(goblins[i].prevX + changeInX, goblins[i].prevY + changeInY);
            }*/

            // Goes through each goblin and makes sure to use their action
            for(int j = 0; j<numOfGoblins; j++) {

                //Movement directions
                int random_dir = generator.nextInt(5);

                //Skips over null values
                if(goblins[j] == null || goblins[i] == goblins[j]) {
                    continue;
                }

                //Checks to make sure the goblins do not collide with one another or the walls of the arena
                if ((goblins[i].enemyImage.getX() == goblins[j].enemyImage.getX() &&
                    goblins[i].enemyImage.getY() == goblins[j].enemyImage.getY()) ||
                    goblins[i].enemyImage.getX() <= X_VAL[2] ||
                    goblins[i].enemyImage.getX() >= X_VAL[1] ||
                    goblins[i].enemyImage.getY() >= Y_VAL[0] ||
                    goblins[i].enemyImage.getY() <= Y_VAL[3]) {

                    //Determines direction goblin moves if they run into/overlap their teammate
                    if(goblins[i].enemyImage.getX() + 64 <= X_VAL[1] &&
                        random_dir == 2) {
                        goblins[i].enemyImage.addAction(Actions.moveTo(goblins[i].prevX + 64, goblins[i].prevY, 0.3f));
                    }
                    else if(goblins[i].enemyImage.getX() - 64 <= X_VAL[2] &&
                            random_dir == 3) {
                        goblins[i].enemyImage.addAction(Actions.moveTo(goblins[i].prevX - 64, goblins[i].prevY, 0.3f));
                    }
                    else if(goblins[i].enemyImage.getY() + 64 <= Y_VAL[0] &&
                            random_dir == 1) {
                        goblins[i].enemyImage.addAction(Actions.moveTo(goblins[i].prevX, goblins[i].prevY + 64, 0.3f));
                    }
                    else if(goblins[i].enemyImage.getY() - 64 <= Y_VAL[3] &&
                            random_dir == 4) {
                        goblins[i].enemyImage.addAction(Actions.moveTo(goblins[i].prevX, goblins[i].prevY - 64, 0.3f));
                    }
                    goblins[i].healthBar.addAction(Actions.moveTo(goblins[i].enemyImage.getX(), goblins[i].enemyImage.getY(), 0.3f));
                }
            }
        }
    }

    /**
     * Get function that returns the number of goblins killed (by the player)
     * @return - returns the number of goblins killed
     */
    public int getGoblinsKilled() {
        return goblinsKilled;
    }

    public boolean isExitAvailable() {
        return exitAvailable;
    }

    public void setExitAvailability(boolean b) {
        exitAvailable = b;
    }

    public float getLevelMultiplier() {
        return levelMultiplier;
    }

    /**
     * Segregates and labels the portion of code that specifically handles ranged combat between the player and the
     * Enemy NPCs (in this version, Goblins)
     */
    public void handleRangeCombat() {

        //Checks to see if the player pressed the fight action and if all flags and player's weapon are set to true
        //Sets ranged flag true for the selection image to do it's thing
        if(Gdx.input.isKeyJustPressed(game.controls.getFightAction()) && !magicFlag
            && !meleeFlag && player.inventoryItems.get(game.currentInventorySelection).getRange() == 1) {
            rangedFlag = true;
        }

        //Works on resetting and working on making sure the selection is correct
        if(selectionIndex >= numOfGoblins) {
            selectionIndex = 0;
        }

        //Makes sure to block out other ranged flags
        if (rangedFlag && !magicFlag && !meleeFlag) {

            //Resets the selection index to keep it on a viable goblin
            if(goblins[selectionIndex] == null) {
                while(goblins[selectionIndex] == null) {
                    selectionIndex++;
                    if(selectionIndex >= numOfGoblins) {
                        selectionIndex = 0;
                    }
                }
            }

            //Sets up the keyboard for the selection image and allowing the player to move between goblins
            stage.setKeyboardFocus(selectionImage);
            selectionImage.setPosition(goblins[selectionIndex].enemyImage.getX(), goblins[selectionIndex].enemyImage.getY());
            stage.addActor(selectionImage);

            //Makes sure the enter input is legitimate and working for the ranged weapon
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !magicFlag && rangedFlag)  {
                player.isTurn = false;
                rangedFlag = false;

                //Hit Roll
                int hitRoll = generator.nextInt(20) + player.getAcrobatics()/2 + 1;
                if (hitRoll >= goblins[selectionIndex].getArmorPoints()) {
                    goblins[selectionIndex].damageTaken(player.inventoryItems.get(game.currentInventorySelection).getDamage());
                    goblins[selectionIndex].updateHealth();
                }

                //Sets player's turn to false
                player.isTurn = false;

                //Set all goblin turns to true
                for (int j = 0; j < numOfGoblins; j++) {
                    if (goblins[selectionIndex] == null) {
                        continue;
                    }
                    goblins[selectionIndex].isTurn = true;
                }

                //CHecks to see if the current goblin is dead and increments quest flag by one
                if(goblins[selectionIndex].getHealthPoints() <= 0) {
                    player.kLongRangeKills++;
                }

                //Removes Selection Combat and puts keyboard control back to player
                selectionImage.remove();
                stage.setKeyboardFocus(player.playerIcon);
            }
        }
    }

    /*
    public void handleMagicCombat() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.C) && player.getPlayerClass() >= 1
            && !rangedFlag && !meleeFlag && player.inventoryItems.get(game.currentInventorySelection).getCategory() == "spell") {
            magicFlag = true;
        }


        //TODO: Cast Spell
        //Scroll equipped is checked for contents
        //if self, then cast on self

        if (magicFlag && !rangedFlag) {
            //stage.setKeyboardFocus(selectionImage);
            //stage.addActor(selectionImage);
            magicFlag = false;
            player.isTurn = false;

            if(player.getPlayerClass() == 1) {
                player.setHealthPoints(player.getHealthPoints() + generator.nextInt(4) + player.getMental()/2 + 1);
                inventory.updateHealth();
            }

            if(player.getPlayerClass() == 2) {
                //TODO: CAST SPELL
            }
        }
    }*/

    public void handleMeleeCombat() {

        //Checks to see if the player pressed the fight action and if all flags and player's weapon are set to true
        if (!magicFlag && !rangedFlag && Gdx.input.isKeyJustPressed(game.controls.getFightAction())
            && !(player.inventoryItems.get(game.currentInventorySelection).getRange() == 1)){

            //Sets Melee Flag true for the selection image
            meleeFlag = true;

            //Sets up the selection index to prevent it from looping beyond the confines of the array
            if(selectionIndex >= numOfGoblins) {
                selectionIndex = 0;
            }

            //Loops through the array and sets the selection image to the first viable goblin
            if(goblins[selectionIndex] == null) {
                while(goblins[selectionIndex] == null) {
                    selectionIndex++;
                    if(selectionIndex == numOfGoblins) {
                        selectionIndex = 0;
                    }
                }
                x_distance = goblins[selectionIndex].enemyImage.getX() - player.playerIcon.getX();
                y_distance = goblins[selectionIndex].enemyImage.getY() - player.playerIcon.getY();
            }

            //Sets the selection image to the stage
            stage.setKeyboardFocus(selectionImage);
            selectionImage.setPosition(goblins[selectionIndex].enemyImage.getX(), goblins[selectionIndex].enemyImage.getY());
            stage.addActor(selectionImage);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) &&
                !magicFlag && !rangedFlag && meleeFlag) {
            if (x_distance < 96 && x_distance > -96 &&
                    y_distance < 96 && y_distance > -96) {
                int hitRoll = generator.nextInt(20) + player.getBruteforce()/2 + 1;
                if (hitRoll >= goblins[selectionIndex].getArmorPoints()) {
                    goblins[selectionIndex].damageTaken(player.getPhysical()/2 + 1 + player.inventoryItems.get(game.currentInventorySelection).getDamage());
                    goblins[selectionIndex].updateHealth();
                }

                for (int j = 0; j < numOfGoblins; j++) {
                    if (goblins[j] == null) {
                        continue;
                    }
                    goblins[j].isTurn = true;
                }
            }

            if(goblins[selectionIndex].getHealthPoints() <= 0) {
                player.kCloseRangeKills++;
            }

            player.isTurn = false;
            meleeFlag = false;

            selectionImage.remove();
            stage.setKeyboardFocus(player.playerIcon);
        }
    }

    /**
     * Major method that simplifies and works through combat with enemy creatures (those being goblins).
     */
    public void handleCombat() {

        //Handles player movement
        movement();

        //If player moves and it's no longer his turn, make sure to flag goblin's turn
        if(!player.isTurn) {
            for(int i = 0; i<numOfGoblins; i++) {
                if(goblins[i] == null) {
                    continue;
                }
                goblins[i].isTurn = true;
            }
        }

        //Handles goblin collision. May move to a better position
        collisionBodyHandler();

        handleRangeCombat();
        //handleMagicCombat();
        handleMeleeCombat();

        //Player attack based on Miller Banford's original code, but modified to check for squad of goblins
        //Handles player's attack and killing goblins if the attack is successful
        for(int i = 0; i<numOfGoblins; i++) {

            if(goblins[i] == null) {
                if(i + 1 == numOfGoblins) {
                    player.isTurn = true;
                }
                continue;
            }

            //handleMeleeCombat(i);

            //Removes goblins that were killed in players turn
            if (goblins[i].checkIfDead()) {
                goblins[i].remove();
                goblins[i] = null;
                player.setGoblins(null);
                remainingGoblin--;
                player.kAssassinations++;
                if(remainingGoblin == 0) {
                    //player.kAssassinations++;
                    combatFlag = false;
                    level.setMapID(0);
                    numOfGoblins = 0;
                    player.setNumGoblins(0);
                    levelsExplored++;
                    return;
                }
            }


            //Takes care of Goblin's turn
            if(!player.isTurn && goblins[i] != null){

                if(goblins[i].movement()) {
                    goblins[i].isTurn = false;
                }

                //Goblin Attack based on Miller Banford's original code
                if(goblins[i].isTurn && x_distance < 96 && x_distance > -96 &&
                        y_distance < 96 && y_distance > -96) {
                    player.setHealthPoints(player.getHealthPoints() - goblins[i].attackPlayer());

                    //Update's player's health in order to keep it going
                    try {
                        inventory.updateHealth();
                    }
                    catch (Exception e){
                    }

                    //Make's sure to mark of goblin's turn
                    goblins[i].isTurn = false;
                }
            }

            //Makes sure to end goblin's turn in order to allow for player to have a turn.
            //&& goblins[i].getCurrentAnimation().isAnimationFinished(game.elapsedTime)
            if(i + 1 == numOfGoblins) {
                player.isTurn = true;
                player.hasMoved = false;
            }
        }

    }
}
