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

    // The Screen Bounds
    final float MIN_WIDTH = 20;
    final float MIN_HEIGHT = 50;
    // To load assets onto ram;
    SpriteBatch batch;


    // To Track Entity's current and previous location
    Vector2 currentEntityPosition;
    Vector2 previousEntityPosition;

    float Entity_Width;
    float Entity_Height;

    // Movement Speed of Entity
    float Speed = 200f;

    // Entity's Area
    Rectangle Entity_Rect;
    Animation<TextureRegion> upAnimation;
    Animation<TextureRegion> downAnimation;
    Animation<TextureRegion> leftAnimation;
    Animation<TextureRegion> rightAnimation;
    Animation<TextureRegion> currentAnimation;

    String playerDirection;
    boolean isEntityMoving;
    OrthographicCamera camera;
    float elapsedTime;

    MainMenuControls controls;
    Rectangle window;
    boolean isPaused;

    public EntityUIBase(OrthographicCamera camera, SpriteBatch batch, MainMenuControls controls, float initialPlayerLocation_X, float initialPlayerLocation_Y, float Entity_Width, float Entity_Height, Animation<TextureRegion> upAnimation, Animation<TextureRegion> downAnimation, Animation<TextureRegion> leftAnimation, Animation<TextureRegion> rightAnimation) {

        this.upAnimation = upAnimation;
        this.downAnimation = downAnimation;
        this.leftAnimation = leftAnimation;
        this.rightAnimation = rightAnimation;
        this.camera = camera;
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



