package com.badlogic.game.creatures;

import com.badlogic.game.BladeAndTomes;

public class Entity {
    private static int armorPoints;

    //TODO: X Grid 21/22 Squares, Y Grid 8-12 Squares
    int GRID_X_SQUARE = 21;
    int GRID_Y_SQUARE = 12;
    public final int [] GRID_X = new int[21];
    public final int [] GRID_Y = new int[12];

    //Defines armor and hit points for creatures
    /**
     * Adding movement to this list also
     */
    protected int healthPoints, height, width, fullHealth;
    protected float movement;

    /**
     * Defines the generic attributes for all Entities.
     */
    public Entity()
    {
        this.healthPoints = 0;
        this.fullHealth = 0;
        this.armorPoints = 0;
        this.movement = .5f;
        this.height = 64;
        this.width = 64;

        int x_start = 264;
        int y_start = 152;
        for(int i = 0; i < GRID_X_SQUARE; i++) {
            GRID_X[i] = x_start*(i+1);
        }
        for(int i = 0; i < GRID_Y_SQUARE; i++) {
            GRID_Y[i] = y_start*(i+1);
        }
    }

    /**
     * Allows the user to define the generic attributes for all Entities.
     */
    public Entity(int healthPoints, int fullHealth, int armorPoints, float movement, int height, int width)
    {
        this.healthPoints = healthPoints;
        this.fullHealth = fullHealth;
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

    public int getFullHealth() { return fullHealth; }

    public float getMovement() { return movement; }

    public int getHeight() { return height; }

    public int getWidth() { return width; }

    public void setArmorPoints(int armorPoints) {
        this.armorPoints = armorPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public void setFullHealth(int fullHealth){ this.fullHealth = fullHealth; }

    public void setMovement(float movement) { this.movement = movement; }

    public void setHeight(int height) { this.height = height; }

    public void setWidth(int width) { this.width = width; }

}
