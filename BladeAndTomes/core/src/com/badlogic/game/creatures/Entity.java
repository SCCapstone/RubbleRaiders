package com.badlogic.game.creatures;

public class Entity {

    //Defines armor and hit points for creatures
    /**
     * Adding movement to this list also
     */
    private int healthPoints, armorPoints, movement;

    /**
     * Defines the generic attributes for all Entities.
     */
    public Entity()
    {
        this.healthPoints = 0;
        this.armorPoints = 0;
        this.movement = 0;
    }

    /**
     * Allows the user to define the generic attributes for all Entities.
     */
    public Entity(int healthPoints, int armorPoints, int movement)
    {
        this.healthPoints = healthPoints;
        this.armorPoints = armorPoints;
        this.movement = movement;
    }

    public int getArmorPoints() {
        return armorPoints;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getMovement() { return movement; }

    public void setArmorPoints(int armorPoints) {
        this.armorPoints = armorPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public void setMovement(int movement) { this.movement = movement; }

}
