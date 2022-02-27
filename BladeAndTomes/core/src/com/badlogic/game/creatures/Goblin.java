package com.badlogic.game.creatures;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Goblin  extends Enemy{

    Texture enemyTex;
    public final Image enemyImage;
    public boolean isTurn;

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
    }

    /**
     * Handles movement for the goblin relative to the player character (DOES NOT REGISTER WALLS)
     * @return - Returns if movement was used or not considering turn based actions
     */
    public boolean movement() {
        float x_distance = enemyImage.getX() - player.playerIcon.getX();
        float y_distance = enemyImage.getY() - player.playerIcon.getY();

        if((x_distance >= -64 && x_distance <= 64) &&
                (y_distance >= -64 && y_distance <= 64)) {
            return false;
        }

        if(y_distance < -64) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX(), enemyImage.getY() + 64,0));
        }
        else if(y_distance > 64) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX(), enemyImage.getY() - 64,0));
        }

        if(x_distance < -64) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX() + 64, enemyImage.getY(),0));
        }
        else if(x_distance > 64) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX() - 64, enemyImage.getY(),0));
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
        enemyTex.dispose();
    }


}
