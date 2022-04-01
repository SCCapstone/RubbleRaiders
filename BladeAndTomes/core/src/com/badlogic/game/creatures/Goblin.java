package com.badlogic.game.creatures;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    private final AssetManager manager = new AssetManager();

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
        enemyImage.setVisible(false);
        movement = .3f;

        manager.load("AnimationFiles/goblinIdle.atlas", TextureAtlas.class);
        manager.load("AnimationFiles/goblinAttack.atlas", TextureAtlas.class);
        manager.load("AnimationFiles/goblinMoveDown.atlas", TextureAtlas.class);
        manager.load("AnimationFiles/goblinMoveUp.atlas", TextureAtlas.class);
        manager.load("AnimationFiles/goblinMoveLeft.atlas", TextureAtlas.class);
        manager.load("AnimationFiles/goblinMoveRight.atlas", TextureAtlas.class);
        manager.finishLoading();
        idleTextureAtlas = manager.get("AnimationFiles/goblinIdle.atlas");
        idleAnimation = new Animation<TextureRegion>(1/2f, idleTextureAtlas.getRegions());
        currentAnimation = idleAnimation;
        attackTextureAtlas = manager.get("AnimationFiles/goblinAttack.atlas");
        attackAnimation = new Animation<TextureRegion>(1/3f, attackTextureAtlas.getRegions());
        moveDownTextureAtlas = manager.get("AnimationFiles/goblinMoveDown.atlas");
        moveDownAnimation = new Animation<TextureRegion>(movement/5f, moveDownTextureAtlas.getRegions());
        moveUpTextureAtlas = manager.get("AnimationFiles/goblinMoveUp.atlas");
        moveUpAnimation = new Animation<TextureRegion>(movement/5f, moveUpTextureAtlas.getRegions());
        moveLeftTextureAtlas = manager.get("AnimationFiles/goblinMoveLeft.atlas");
        moveLeftAnimation = new Animation<TextureRegion>(movement/5f, moveLeftTextureAtlas.getRegions());
        moveRightTextureAtlas = manager.get("AnimationFiles/goblinMoveRight.atlas");
        moveRightAnimation = new Animation<TextureRegion>(movement/5f, moveRightTextureAtlas.getRegions());
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

        if((x_distance >= -128 && x_distance <= 128) &&
                (y_distance >= -128 && y_distance <= 128)) {
            return false;
        }

        if(y_distance < -64) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX(), enemyImage.getY() + 64,.3f));
            moving = true;
            resetElapsedTime();
            runMoveUpAnimation();
            enemyImage.addAction(Actions.moveTo(enemyImage.getX(), enemyImage.getY() + 64,0));
            healthBar.setPosition(enemyImage.getX(), enemyImage.getY() + 64);
        }
        else if(y_distance > 64) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX(), enemyImage.getY() - 64,0));
            healthBar.setPosition(enemyImage.getX(), enemyImage.getY() - 64);
            enemyImage.addAction(Actions.moveTo(enemyImage.getX(), enemyImage.getY() - 64,.3f));
            moving = true;
            resetElapsedTime();
            runMoveDownAnimation();
        }

        if(x_distance < -64) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX() + 64, enemyImage.getY(),0));
            healthBar.setPosition(enemyImage.getX() + 64, enemyImage.getY());
            enemyImage.addAction(Actions.moveTo(enemyImage.getX() + 64, enemyImage.getY(),.3f));
            moving = true;
            resetElapsedTime();
            runMoveRightAnimation();
        }
        else if(x_distance > 64) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX() - 64, enemyImage.getY(),0));
            healthBar.setPosition(enemyImage.getX() - 64, enemyImage.getY());
            enemyImage.addAction(Actions.moveTo(enemyImage.getX() - 64, enemyImage.getY(),.3f));
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
        healthBar.remove();
        healthSkin.dispose();
        healthAtlas.dispose();
        enemyTex.dispose();
        idleTextureAtlas.dispose();
        moveDownTextureAtlas.dispose();
        moveUpTextureAtlas.dispose();
        moveLeftTextureAtlas.dispose();
        moveRightTextureAtlas.dispose();
        attackTextureAtlas.dispose();
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
