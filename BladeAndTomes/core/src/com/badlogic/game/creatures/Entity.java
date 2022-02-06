package com.badlogic.game.creatures;

import com.badlogic.game.BladeAndTomes;

public class Entity {

    //Defines armor and hit points for creatures
    /**
     * Adding movement to this list also
     */
    private int healthPoints, armorPoints, movement, height, width;

    /**
     * Defines the generic attributes for all Entities.
     */
    public Entity()
    {
        this.healthPoints = 0;
        this.armorPoints = 0;
        this.movement = 0;
        this.height = 64;
        this.width = 64;
    }

    /**
     * Allows the user to define the generic attributes for all Entities.
     */
    public Entity(int healthPoints, int armorPoints, int movement, int height, int width)
    {
        this.healthPoints = healthPoints;
        this.armorPoints = armorPoints;
        this.movement = movement;
        this.height = height;
        this.width = width;
    }

    public int getArmorPoints() {
        return armorPoints;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getMovement() { return movement; }

    public int getHeight() { return height; }

    public int getWidth() { return width; }

    public void setArmorPoints(int armorPoints) {
        this.armorPoints = armorPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public void setMovement(int movement) { this.movement = movement; }

    public void setHeight(int height) { this.height = height; }

    public void setWidth(int width) { this.width = width; }

}
