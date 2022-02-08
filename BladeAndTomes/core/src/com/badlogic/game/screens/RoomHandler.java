package com.badlogic.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.Random;

public class RoomHandler {

    //Instantiates the random generator to be used within this project
    private Random generator = new Random();

    //Thanks to baeldung for creating a tutorial based on how enum values work,
    //including section 3, which is what the following enum is based on.
    //https://www.baeldung.com/java-enum-values
    enum ROOMS {
        N("NRoom.png"),
        E("ERoom.png"),
        W("WRoom.png"),
        S("SRoom.png"),
        NE("NERoom.png"),
        NW("NWRoom.png"),
        NS("NSRoom.png"),
        EW("EWRoom.png"),
        ES("ESRoom.png"),
        WS("WSRoom.png"),
        NEW("NEWRoom.png"),
        NES("NESRoom.png"),
        NWS("NWSRoom.png"),
        EWS("EWSRoom.png"),
        NEWS("NEWSRoom.png");

        private String filename;

        ROOMS(String filename) {
            this.filename = filename;
        }
    }

    //Denotes the starting position of the player relative to where the player is entering.
    private int charStart;
    private Room level;

    /**
     * Constructor for the "RoomHandler" or the linked list handler
     */
    public RoomHandler() {
        charStart = -1;
        this.level = new Room();
    }


    /**
     * Function that goes through a series of statements and random generation in order to retrieve
     * a certain room.
     * @param entrance - Indicates the entrance the player went into
     * @return - returns if generation was successful or not
     */
    public boolean generateRoom(int entrance) {
        int numOfDoors = generator.nextInt(4) + 1;

        switch(numOfDoors) {
            case 1:
                //TODO: Set room entrance
                break;

            case 2:
                //TODO: Set room and other room
                break;

            case 3:
                //TODO: Set room and other room
                break;

            case 4:
                //TODO: Set room and other room
                break;

            default:
                return false;
        }
        generateEvent();
        return true;
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
}
