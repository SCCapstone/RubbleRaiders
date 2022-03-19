package com.badlogic.game.creatures;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class Enemy extends Entity{

    public String itemDrop;
    public Image enemyImage;
    protected Player player;

    public abstract boolean movement();
    public abstract int attackPlayer();

    public Enemy(int healthPoints, int fullHealth, int armorPoints, int movement, int height, int width, Player player) {
        super(healthPoints, fullHealth, armorPoints, movement, height, width);
        this.player = player;
    }

    /**
     * Damage keeps track of damage taken by the player, and if health is zero, proceeds to drop item
     * @param damage - Integer representing damage taken
     */
    public void damageTaken(int damage) {

        healthPoints = healthPoints - damage;
        if(this.getHealthPoints() <= 0) {
            dropItem();
        }

    }

    /**
     * Class that deals with dropping items upon death for winning
     */
    public void dropItem() {
        //TODO: Implement when items are ready
    }

    /*
    public void reAddActor() {
        enemyImage.setPosition(xCord, yCord);
        GAME.stageInstance.addActor(enemyImage);
    }*/

    /**
     * Get function that returns the state of the enemy, that being whether or not the enemy is alive
     * @return - returns state of the enemy whether dead or alive
     */
    public boolean checkIfDead() {return this.healthPoints <= 0;}

}
