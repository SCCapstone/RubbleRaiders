package com.badlogic.game;

public class Npc extends Entity {

    private enum NpcClass {Trader, Companion, Enemy};

    private int Npc;

    public Npc()
    {
        super();
    }

    public Npc(int healthPoints, int armorPoints, int movement, int playerClass)
    {
        super(healthPoints, armorPoints, int movement);
        this.NpcClass = NpcClass;
    }

    public int getNpcClass()
    {
        return NpcClass;
    }

    public void setNpcClass(int NpcClass)
    {
        this.NpcClass = NpcClass;
    }

}
