package com.badlogic.game;

public class Player extends Entity {

    //https://www.youtube.com/watch?v=5VyDsO0mFDU
    //Very helpful tutorial on enums by Margret Posch
    //TODO: Maybe make this a public enum in backbone?
    private enum PlayerClass {WIZARD, CLERIC, WARRIOR};

    private int playerClass;

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
    public Player(int healthPoints, int armorPoints, int playerClass)
    {
        super(healthPoints, armorPoints);
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

}
