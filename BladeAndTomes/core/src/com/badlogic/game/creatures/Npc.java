package com.badlogic.game.creatures;

import com.badlogic.game.creatures.Entity;

public class Npc extends Entity {

    private enum npcClass {Trader, Companion, Enemy};

    private int npc;

    public Npc()
    {
        super();
    }

    public Npc(int healthPoints, int armorPoints, int movement, int npcClass)
    {
        super(healthPoints, armorPoints, movement);
        this.npc = npcClass;
    }

    public int getNpcClass()
    {
        return npc;
    }

    public void setNpcClass(int npcClass) { this.npc = npcClass; }

}
