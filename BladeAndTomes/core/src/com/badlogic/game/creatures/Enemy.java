package com.badlogic.game.creatures;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public abstract class Enemy extends Entity {

    public String itemDrop;
    protected Player player;
    public Image enemyImage;
    protected float xCord, yCord;
    public InputListener playerInput;
    public BladeAndTomes GAME;

    public abstract void movement();

    public abstract void attackPlayer();

    public Enemy(int healthPoints, int armorPoints, int movement, int height, int width, Player player, BladeAndTomes GAME) {

        super(healthPoints, armorPoints, movement, height, width);
        this.player = player;
        this.GAME = GAME;

    }


    public void damageTaken(int damage) {

        healthPoints = healthPoints - damage;
        if (this.getHealthPoints() <= 0) {
            dropItem();
        }

    }

    public void dropItem() {


    }

    public void reAddActor() {
        enemyImage.setPosition(xCord, yCord);
        GAME.stageInstance.addActor(enemyImage);
    }

    public boolean checkIfDead() {
        return healthPoints <= 0;
    }

    public float getXCord() {
        return xCord;
    }

    public float getYCord() {
        return yCord;
    }

}
