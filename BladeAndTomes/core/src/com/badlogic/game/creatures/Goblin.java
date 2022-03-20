package com.badlogic.game.creatures;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Goblin  extends Enemy{

    Texture enemyTex;
    public final Image enemyImage;
    public boolean isTurn;

    private TextureAtlas idleTextureAtlas;
    private transient Animation<TextureRegion> idleAnimation;
    private TextureAtlas attackTextureAtlas;
    private transient Animation<TextureRegion> attackAnimation;
    private TextureAtlas moveDownTextureAtlas;
    private transient Animation<TextureRegion> moveDownAnimation;
    private TextureAtlas moveUpTextureAtlas;
    private transient Animation<TextureRegion> moveUpAnimation;
    private TextureAtlas moveLeftTextureAtlas;
    private transient Animation<TextureRegion> moveLeftAnimation;
    private TextureAtlas moveRightTextureAtlas;
    private transient Animation<TextureRegion> moveRightAnimation;
    private transient Animation<TextureRegion> currentAnimation;
    public float elapsedTime;
    public boolean isAttacking = false;
    public boolean moving = false;

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

        idleTextureAtlas = new TextureAtlas(Gdx.files.internal("AnimationFiles/goblinIdle.atlas"));
        idleAnimation = new Animation<TextureRegion>(1/2f, idleTextureAtlas.getRegions());
        currentAnimation = idleAnimation;
        attackTextureAtlas = new TextureAtlas(Gdx.files.internal("AnimationFiles/goblinAttack.atlas"));
        attackAnimation = new Animation<TextureRegion>(1/3f, attackTextureAtlas.getRegions());
        moveDownTextureAtlas = new TextureAtlas(Gdx.files.internal("AnimationFiles/goblinMoveDown.atlas"));
        moveDownAnimation = new Animation<TextureRegion>(1/5f, moveDownTextureAtlas.getRegions());
        moveUpTextureAtlas = new TextureAtlas(Gdx.files.internal("AnimationFiles/goblinMoveUp.atlas"));
        moveUpAnimation = new Animation<TextureRegion>(1/5f, moveUpTextureAtlas.getRegions());
        moveLeftTextureAtlas = new TextureAtlas(Gdx.files.internal("AnimationFiles/goblinMoveLeft.atlas"));
        moveLeftAnimation = new Animation<TextureRegion>(1/5f, moveLeftTextureAtlas.getRegions());
        moveRightTextureAtlas = new TextureAtlas(Gdx.files.internal("AnimationFiles/goblinMoveRight.atlas"));
        moveRightAnimation = new Animation<TextureRegion>(1/5f, moveRightTextureAtlas.getRegions());
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
            enemyImage.addAction(Actions.moveTo(enemyImage.getX(), enemyImage.getY() + 64,1));
            moving = true;
            resetElapsedTime();
            runMoveUpAnimation();
        }
        else if(y_distance > 64) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX(), enemyImage.getY() - 64,1));
            moving = true;
            resetElapsedTime();
            runMoveDownAnimation();
        }

        if(x_distance < -64) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX() + 64, enemyImage.getY(),1));
            moving = true;
            resetElapsedTime();
            runMoveRightAnimation();
        }
        else if(x_distance > 64) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX() - 64, enemyImage.getY(),1));
            moving = true;
            resetElapsedTime();
            runMoveLeftAnimation();
        }

        return true;
    }

    /**
     * Handles Goblin attacks and codifying it into the goblin class.
     * @return - return damage done by the goblin's attack
     */
    public int attackPlayer() {
        isAttacking = true;
        resetElapsedTime();
        runAttackAnimation();
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

    public void runAnimation(BladeAndTomes GAME) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        if(currentAnimation.isAnimationFinished(elapsedTime)){
            currentAnimation = idleAnimation;
            isAttacking = false;
            moving = false;
        }
        GAME.batch.draw(currentAnimation.getKeyFrame(elapsedTime, true), enemyImage.getX(), enemyImage.getY());
    }

    public void runAttackAnimation() { currentAnimation = attackAnimation; }
    public void runMoveDownAnimation() { currentAnimation = moveDownAnimation; }
    public void runMoveUpAnimation() { currentAnimation = moveUpAnimation; }
    public void runMoveLeftAnimation() { currentAnimation = moveLeftAnimation; }
    public void runMoveRightAnimation() { currentAnimation = moveRightAnimation; }
    public void resetElapsedTime() { elapsedTime = 0; }


}
