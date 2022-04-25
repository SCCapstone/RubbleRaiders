package com.badlogic.game.EntityUI;

import Keyboard_Mouse_Controls.MainMenuControls;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerEnitityUI extends EntityUIBase{
    public boolean isPaused;
    public boolean cantMoveInCurrentDirection;
    public PlayerEnitityUI(OrthographicCamera camera,
                           SpriteBatch batch,
                           MainMenuControls controls,
                           float initialPlayerLocation_X,
                           float initialPlayerLocation_Y,
                           float Entity_Width,
                           float Entity_Height,
                           Animation<TextureRegion> upAnimation,
                           Animation<TextureRegion> downAnimation,
                           Animation<TextureRegion> leftAnimation,
                           Animation<TextureRegion> rightAnimation) {
        super(camera, batch, controls, initialPlayerLocation_X, initialPlayerLocation_Y, Entity_Width, Entity_Height, upAnimation, downAnimation, leftAnimation, rightAnimation);
        isPaused = false;
        cantMoveInCurrentDirection = false;
    }
    @Override
    public void render(float delta){
        isEntityMoving = false;
        elapsedTime += Gdx.graphics.getDeltaTime();
        if(!isPaused)
        if (Gdx.input.isKeyPressed(controls.getMoveUp()) && currentEntityPosition.y < HEIGHT && (!cantMoveInCurrentDirection&&playerDirection.equalsIgnoreCase("up")||!playerDirection.equalsIgnoreCase("up"))) {moveUP();cantMoveInCurrentDirection = false;}
        else if (Gdx.input.isKeyPressed(controls.getMoveDown()) && currentEntityPosition.y > MIN_HEIGHT&& (!cantMoveInCurrentDirection&&playerDirection.equalsIgnoreCase("down")||!playerDirection.equalsIgnoreCase("down"))) {moveDown();cantMoveInCurrentDirection = false;}
        else if (Gdx.input.isKeyPressed(controls.getMoveLeft()) && currentEntityPosition.x > MIN_WIDTH&& (!cantMoveInCurrentDirection&&playerDirection.equalsIgnoreCase("left")||!playerDirection.equalsIgnoreCase("left"))) {moveLeft();cantMoveInCurrentDirection = false;}
        else if ( Gdx.input.isKeyPressed(controls.getMoveRight()) && currentEntityPosition.x < WIDTH&& (!cantMoveInCurrentDirection&&playerDirection.equalsIgnoreCase("right")||!playerDirection.equalsIgnoreCase("right")))  {moveRight();cantMoveInCurrentDirection = false;}
        Entity_Rect.setPosition(currentEntityPosition);
        batch.begin();
        batch.draw(currentAnimation.getKeyFrame(elapsedTime, true), currentEntityPosition.x, currentEntityPosition.y, Entity_Width, Entity_Height);
        batch.end();
    }

}
