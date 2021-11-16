package com.badlogic.game.creatures;

import java.lang.String;

public class Player extends Entity {

    //https://www.youtube.com/watch?v=5VyDsO0mFDU
    //Very helpful tutorial on enums by Margret Posch
    //TODO: Maybe make this a public enum in backbone?
    private enum classes {WARRIOR, CLERIC ,WIZARD};

    private int playerClass;
    private int physical;
    private int mental;
    private int social;
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
    public Player(int healthPoints, int armorPoints, int movement, int playerClass, String name)
    {
        super(healthPoints, armorPoints, movement);
        this.playerClass = playerClass;
        this.name = name;
    }

    public int getPlayerClass()
    {
        return playerClass;
    }

    public int getPhysical()
    {
        return physical;
    }

    public int getMental()
    {
        return mental;
    }

    public int getSocial()
    {
        return social;
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

    public void setPhysical(int physical) { this.physical = physical; }

    public void setMental(int mental) { this.mental = mental; }

    public void setSocial(int social) { this.social = social; }


}
