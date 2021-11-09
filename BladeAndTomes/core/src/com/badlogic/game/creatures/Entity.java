package com.badlogic.game.creatures;

public class Entity {

    //Defines armor and hit points for creatures
    private int healthPoints, armorPoints;

    /**
     * Defines the generic attributes for all Entities.
     */
    public Entity()
    {
        this.healthPoints = 0;
        this.armorPoints = 0;
    }

    /**
     * Allows the user to define the generic attributes for all Entities.
     */
    public Entity(int healthPoints, int armorPoints)
    {
        this.healthPoints = healthPoints;
        this.armorPoints = armorPoints;
    }

    public int getArmorPoints() {
        return armorPoints;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setArmorPoints(int armorPoints) {
        this.armorPoints = armorPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

}
