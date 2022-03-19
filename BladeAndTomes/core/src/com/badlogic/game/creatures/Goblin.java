package com.badlogic.game.creatures;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Goblin  extends Enemy{

    Texture enemyTex;
    public final Image enemyImage;
    public boolean isTurn;
    public int prevX;
    public int prevY;
    private Skin healthSkin;
    private TextureAtlas healthAtlas;
    public ProgressBar healthBar;
    private Color color;

    /**
     * Paramaterized constuctor that is effectively the default constructor, which just instantiates a normal goblin
     * @param player - player instance so as to allow for the goblin to
     */
    public Goblin(Player player) {
        super(15, 15,15,15,64,64, player);
        enemyTex = new Texture(Gdx.files.internal("Goblin.png"));
        enemyImage = new Image(enemyTex);
        enemyImage.setSize(64,64);
        isTurn = false;
        color = new Color(Color.GREEN);

        // Goblin Health Bar based on Health.java by Anirudh Oruganti and had his help in using SkinComposer
        // To make a simple, small progress bar for the Goblins
        healthAtlas = new TextureAtlas(Gdx.files.internal("SkinAssets/GobHealth/gobHealth.atlas"));
        healthSkin = new Skin(Gdx.files.internal("SkinAssets/GobHealth/gobHealth.json"), healthAtlas);
        healthBar = new ProgressBar(0, fullHealth, 0.5f, false, healthSkin);
        healthBar.setValue(healthPoints);
        healthBar.setColor(color);
    }

    /**
     * Handles movement for the goblin relative to the player character (DOES NOT REGISTER WALLS)
     * @return - Returns if movement was used or not considering turn based actions
     */
    public boolean movement() {

        prevX = (int) this.enemyImage.getX();
        prevY = (int) this.enemyImage.getY();

        float x_distance = enemyImage.getX() - player.playerIcon.getX();
        float y_distance = enemyImage.getY() - player.playerIcon.getY();

        if((x_distance >= -64 && x_distance <= 64) &&
                (y_distance >= -64 && y_distance <= 64)) {
            return false;
        }

        if(y_distance < -64) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX(), enemyImage.getY() + 64,0));
            healthBar.setPosition(enemyImage.getX(), enemyImage.getY() + 64);
        }
        else if(y_distance > 64) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX(), enemyImage.getY() - 64,0));
            healthBar.setPosition(enemyImage.getX(), enemyImage.getY() - 64);
        }

        if(x_distance < -64) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX() + 64, enemyImage.getY(),0));
            healthBar.setPosition(enemyImage.getX() + 64, enemyImage.getY());
        }
        else if(x_distance > 64) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX() - 64, enemyImage.getY(),0));
            healthBar.setPosition(enemyImage.getX() - 64, enemyImage.getY());
        }

        return true;
    }

    /**
     * Handles Goblin attacks and codifying it into the goblin class.
     * @return - return damage done by the goblin's attack
     */
    public int attackPlayer() {
        int hitRoll = (int)(Math.random()*(20)+1);
        if (hitRoll >= player.getArmorPoints()) {
           return (int)(Math.random()*(3)+1);
        }
        else {
            return 0;
        }
    }

    /**
     * Disposes of enemy image and disposes the texture created by the enemyTex
     */
    public void remove() {
        enemyImage.remove();
        healthBar.remove();
        healthSkin.dispose();
        healthAtlas.dispose();
        enemyTex.dispose();
    }

    /**
     * Updates the goblin health everytime the goblin is hit. Functionality based off of Anirudh Oruganti's
     * implementation of the health bar for the player.
     */
    public void updateHealth() {
        if (healthPoints < fullHealth/2) {
            color.set(Color.RED);
        }
        else {
            color.set(Color.GREEN);
        }

        healthBar.setColor(color);
        healthBar.setValue(getHealthPoints());
    }


}
