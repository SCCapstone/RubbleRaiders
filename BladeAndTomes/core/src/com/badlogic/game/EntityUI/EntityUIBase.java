package com.badlogic.game.EntityUI;

import Keyboard_Mouse_Controls.MainMenuControls;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class EntityUIBase extends InputAdapter {
    // The Screen Bounds
    final float WIDTH = 1850;
    final float HEIGHT = 1000;

    // The Minimum Screen Bounds, this could be used as an offset from the origin.
    final float MIN_WIDTH = 20;
    final float MIN_HEIGHT = 50;
    // An instance of Sprite Batch class to draw textures, usually only the moving entity
    SpriteBatch batch;

    // To Track Entity's current location
    Vector2 currentEntityPosition;
    // To Track Entity's previous location
    Vector2 previousEntityPosition;

    // Default Entity's Size
    float Entity_Width;
    float Entity_Height;

    // Movement Speed of Entity
    float Speed = 200f;

    // Entity's Area to track position and keep up its size.
    Rectangle Entity_Rect;

    // Walking Up Animation
    Animation<TextureRegion> upAnimation;
    // Walking Down Animation
    Animation<TextureRegion> downAnimation;
    // Walking Left Animation
    Animation<TextureRegion> leftAnimation;
    // Walking Right Animation
    Animation<TextureRegion> rightAnimation;
    // The enitie's current animation, this is used to keep the entity transitions from one to other or to keep entity moving.
    Animation<TextureRegion> currentAnimation;

    // This variable to used to keep up origination of the entity.
    String playerDirection;
    // A check flag to see if the entity is in moving state.
    boolean isEntityMoving;
    // Some elapsed time from Gdx
    float elapsedTime;
    // Keyboard Controls from user selection
    MainMenuControls controls;
    // An area of the window where everything is being display on.
    Rectangle window;
    // A Pause flag to make the character to stop moving.
    boolean isPaused;

    /**
     * This contractor is used to set basic variable for operating and controlling an entity.
     * @param camera The camera which is being used from the stage instance
     * @param batch   A Sprite Batch which is mainly used for drawing the player animation texture.
     * @param controls  Controls class which the user choose in setting screen. This class is located in "Keyboard_Mouse_Controls" directory.
     * @param initialPlayerLocation_X The initial descried location of that entity in terms of x-axis.
     * @param initialPlayerLocation_Y The initial descried location of that entity in terms of y-axis.
     * @param Entity_Width The Width of the entity.
     * @param Entity_Height The Height of the entity.
     * @param upAnimation The walking up animation of the entity.
     * @param downAnimation The walking down animation of the entity.
     * @param leftAnimation The walking left animation of the entity.
     * @param rightAnimation The walking right animation of the entity.
     */
    public EntityUIBase(OrthographicCamera camera, SpriteBatch batch, MainMenuControls controls, float initialPlayerLocation_X, float initialPlayerLocation_Y, float Entity_Width, float Entity_Height, Animation<TextureRegion> upAnimation, Animation<TextureRegion> downAnimation, Animation<TextureRegion> leftAnimation, Animation<TextureRegion> rightAnimation) {

        this.upAnimation = upAnimation;
        this.downAnimation = downAnimation;
        this.leftAnimation = leftAnimation;
        this.rightAnimation = rightAnimation;
        this.batch = batch;
        this.controls = controls;
        this.Entity_Width = Entity_Width;
        this.Entity_Height = Entity_Height;


        // Current and Previous Locations for the Entity
        currentEntityPosition = new Vector2(initialPlayerLocation_X, initialPlayerLocation_Y);
        previousEntityPosition = new Vector2(initialPlayerLocation_X, initialPlayerLocation_Y);
        window = new Rectangle(MIN_WIDTH, MIN_HEIGHT, WIDTH, HEIGHT);
        Entity_Rect = new Rectangle(currentEntityPosition.x, currentEntityPosition.y, Entity_Width, Entity_Height);
        playerDirection = "Down";
        currentAnimation = downAnimation;

        // For animations
        elapsedTime = 0;

        // Checks if pause screen is present
        isPaused = false;
    }

    /**
     * This method continuous ran every iteration of the game. This will need to be defined in the handler class.
     * This method usually gets override by the child. This method could be used to draw the entity on the screen.
     * @param delta THe input time difference from iteration to iteration.
     */
    public void render(float delta) {
        isEntityMoving = false;
        elapsedTime += Gdx.graphics.getDeltaTime();
        if (moveUP()) ;
        else if (moveDown()) ;
        else if (moveLeft()) ;
        else if (moveRight()) ;
        Entity_Rect.setPosition(currentEntityPosition);
        batch.begin();
        batch.draw(currentAnimation.getKeyFrame(elapsedTime, true), currentEntityPosition.x, currentEntityPosition.y, Entity_Width, Entity_Height);
        batch.end();
    }

    public boolean moveLeft() {
            previousEntityPosition.x = currentEntityPosition.x;
            playerDirection = "LEFT";
            elapsedTime = (isEntityMoving) ? 0 : elapsedTime;
            isEntityMoving = true;
            currentAnimation = leftAnimation;
            currentEntityPosition.x -= Gdx.graphics.getDeltaTime() * Speed;
            return true;
    }

    public boolean moveRight() {
            previousEntityPosition.x = currentEntityPosition.x;
            playerDirection = "RIGHT";
            elapsedTime = (isEntityMoving) ? 0 : elapsedTime;
            isEntityMoving = true;
            currentAnimation = rightAnimation;
            currentEntityPosition.x += Gdx.graphics.getDeltaTime() * Speed;
            return true;
    }
    public boolean moveUP() {

            previousEntityPosition.y = currentEntityPosition.y;
            playerDirection = "UP";
            elapsedTime = (isEntityMoving) ? 0 : elapsedTime;
            isEntityMoving = true;
            currentAnimation = upAnimation;
            currentEntityPosition.y += Gdx.graphics.getDeltaTime() * Speed;
            return true;

    }

    public boolean moveDown() {
            previousEntityPosition.y = currentEntityPosition.y;
            playerDirection = "DOWN";
            elapsedTime = (isEntityMoving) ? 0 : elapsedTime;
            isEntityMoving = true;
            currentAnimation = downAnimation;
            currentEntityPosition.y -= Gdx.graphics.getDeltaTime() * Speed;
            return true;
    }


    public void resetToPreviousPosition() {
        currentEntityPosition.y = previousEntityPosition.y;
        currentEntityPosition.x = previousEntityPosition.x;
    }

    public boolean isColliding(Rectangle rectangle) {
        return Entity_Rect.overlaps(rectangle);
    }

    public void dispose() {

    }
}



