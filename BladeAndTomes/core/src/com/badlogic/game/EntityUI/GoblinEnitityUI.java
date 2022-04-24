package com.badlogic.game.EntityUI;

import Keyboard_Mouse_Controls.MainMenuControls;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class GoblinEnitityUI extends EntityUIBase{
    public boolean nearPlayer;
    public boolean isMovingABlock;
    public int moveBlockArea = 72*72;
    Vector2 finalTime;
    public GoblinEnitityUI(OrthographicCamera camera, SpriteBatch batch, MainMenuControls controls, float initialPlayerLocation_X, float initialPlayerLocation_Y, float Entity_Width, float Entity_Height, Animation<TextureRegion> upAnimation, Animation<TextureRegion> downAnimation, Animation<TextureRegion> leftAnimation, Animation<TextureRegion> rightAnimation) {
        super(camera, batch, controls, initialPlayerLocation_X, initialPlayerLocation_Y, Entity_Width, Entity_Height, upAnimation, downAnimation, leftAnimation, rightAnimation);
        nearPlayer = false;
        isMovingABlock = false;
        finalTime = new Vector2();
//        Speed = moveBlockArea;
    }
    @Override
    public void render(float delta){
        elapsedTime+= Gdx.graphics.getDeltaTime()/4;
        isEntityMoving = true;
        batch.begin();
        batch.draw(currentAnimation.getKeyFrame(elapsedTime,true),currentEntityPosition.x,currentEntityPosition.y,Entity_Width,Entity_Height);
        Entity_Rect.setPosition(currentEntityPosition);
        isMovingABlock = (nearPlayer)? false:isMovingABlock;
        if(isMovingABlock){
            if(finalTime.x>= currentEntityPosition.x)
                isMovingABlock = false;
            currentEntityPosition.x-= Gdx.graphics.getDeltaTime()*Speed;
        }
        //isMovingABlock = false;
        batch.end();
    }
    @Override

    public boolean moveLeft() {
        if(currentEntityPosition.x>MIN_WIDTH&&!nearPlayer&& !isMovingABlock){
            previousEntityPosition.x = currentEntityPosition.x;
            playerDirection = "LEFT";
            currentAnimation = leftAnimation;
            currentEntityPosition.x-= Gdx.graphics.getDeltaTime()*Speed;
            finalTime.x = currentEntityPosition.x - Gdx.graphics.getDeltaTime()*moveBlockArea;
            finalTime.y = -1;
            isMovingABlock = true;
            return true;
        }
        return false;

    }
    @Override
    public boolean moveRight() {
        if(currentEntityPosition.x<WIDTH&&!nearPlayer&&!isMovingABlock){
            previousEntityPosition.x = currentEntityPosition.x;
            playerDirection = "RIGHT";
            currentAnimation = rightAnimation;
            currentEntityPosition.x+= Gdx.graphics.getDeltaTime()*Speed;
            finalTime.x = Gdx.graphics.getDeltaTime()*moveBlockArea;
            finalTime.y = Gdx.graphics.getDeltaTime();
            isMovingABlock = true;
            return true;
        }
        return false;

    }

    @Override

    public boolean moveUP(){
        if(currentEntityPosition.y<HEIGHT&&!nearPlayer&&!isMovingABlock){
            previousEntityPosition.y = currentEntityPosition.y;
            playerDirection = "UP";
            currentAnimation = upAnimation;
            currentEntityPosition.y+= Gdx.graphics.getDeltaTime()*Speed;
            finalTime.y = Gdx.graphics.getDeltaTime()*moveBlockArea;
            finalTime.x = Gdx.graphics.getDeltaTime();
            isMovingABlock = true;
            return true;
        }
        return false;

    }
    @Override
    public boolean moveDown(){
        if(currentEntityPosition.y>MIN_HEIGHT&&!nearPlayer&&!isMovingABlock){
            previousEntityPosition.y = currentEntityPosition.y;
            playerDirection = "DOWN";
            currentAnimation = downAnimation;
            currentEntityPosition.y-= Gdx.graphics.getDeltaTime()*Speed;
            finalTime.y = Gdx.graphics.getDeltaTime()*moveBlockArea;
            finalTime.x = Gdx.graphics.getDeltaTime();
            isMovingABlock = true;
            return true;

        }
        return false;
    }

    public void bounceBack(){
        float adderX = 0;
        float adderY = 0;
        if(playerDirection.equalsIgnoreCase("UP")){
            adderY -= 15;

        } else if(playerDirection.equalsIgnoreCase("Down")){
            adderY += 15;

        } else if(playerDirection.equalsIgnoreCase("Left")){
            adderX += 15;

        } else if(playerDirection.equalsIgnoreCase("Right")){
            adderX -= 15;
        }
        isMovingABlock = false;
        currentEntityPosition.y = previousEntityPosition.y + adderY;
        currentEntityPosition.x = previousEntityPosition.x + adderX;
    }
}
