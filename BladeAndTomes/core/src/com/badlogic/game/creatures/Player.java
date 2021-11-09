package com.badlogic.game.creatures;

import java.lang.String;

public class Player extends Entity {

    //https://www.youtube.com/watch?v=5VyDsO0mFDU
    //Very helpful tutorial on enums by Margret Posch
    //TODO: Maybe make this a public enum in backbone?
    private enum classes {WARRIOR, CLERIC ,WIZARD};

    private int playerClass;
    private String name;

    /**
     * Default constructor for player entity
     */
    public Player()
    {
        super();
    }

    /**
     * Alternate constructor for player entity
     */
    public Player(int healthPoints, int armorPoints, String name, int playerClass)
    {
        super(healthPoints, armorPoints);
        this.name = name;
        this.playerClass = playerClass;
    }

    public int getPlayerClass()
    {
        return playerClass;
    }

    public void setPlayerClass(int playerClass)
    {
        this.playerClass = playerClass;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


}
