package com.badlogic.game.EntityUI;

import Keyboard_Mouse_Controls.MainMenuControls;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class GoblinEnitityUI extends EntityUIBase{
    public boolean nearPlayer;
    public boolean isAttacking;
    public boolean attackRange;
    public boolean cantMoveInCurrentDirection;
    ProgressBar goblinHealthBar;
    Vector2 finalTime;
    // Walking Up Animation
    Animation<TextureRegion> attackAnimation;
    public String playerDirection;
    public GoblinEnitityUI(OrthographicCamera camera, SpriteBatch batch, MainMenuControls controls,
                           float initialPlayerLocation_X, float initialPlayerLocation_Y,
                           float Entity_Width, float Entity_Height, Animation<TextureRegion> upAnimation,
                           Animation<TextureRegion> downAnimation, Animation<TextureRegion> leftAnimation,
                           Animation<TextureRegion> rightAnimation, Animation<TextureRegion> attackAnimation,
                           Skin healthSkin, float fullHealth) {
        super(camera, batch, controls, initialPlayerLocation_X, initialPlayerLocation_Y, Entity_Width, Entity_Height, upAnimation, downAnimation, leftAnimation, rightAnimation);
        nearPlayer = false;
        isAttacking = false;
        finalTime = new Vector2();
        Speed = 150;
        this.attackAnimation = attackAnimation;
        this.goblinHealthBar = new ProgressBar(0, fullHealth, 0.5f, false, healthSkin);
        this.goblinHealthBar.setValue(fullHealth);
        this.goblinHealthBar.setColor(Color.GREEN);
        this.goblinHealthBar.setSize(Entity_Width,15);
        this.playerDirection = "down";
        cantMoveInCurrentDirection = false;
        attackRange = false;
      //  this.goblinHealthBar.setScale(20);
//        Speed = moveBlockArea;
    }
    @Override
    public void render(float delta){
        elapsedTime+= Gdx.graphics.getDeltaTime()/4;
        isEntityMoving = true;
        Speed = (new Random().nextInt(150)+50);
        batch.begin();
        if(isAttacking)
            if(currentAnimation.isAnimationFinished(elapsedTime)) {
//                currentAnimation = (playerDirection.equalsIgnoreCase("LEFT")) ? leftAnimation :
//                        (playerDirection.equalsIgnoreCase("RIGHT")) ? rightAnimation : (playerDirection.equalsIgnoreCase("UP")) ?
//                                upAnimation :
//                                downAnimation;
                isAttacking = false;

            }
        batch.draw(currentAnimation.getKeyFrame(elapsedTime,true),currentEntityPosition.x,currentEntityPosition.y,Entity_Width,Entity_Height);
        Entity_Rect.setPosition(currentEntityPosition);
        goblinHealthBar.setPosition(Entity_Rect.x,Entity_Rect.y-10);
       // goblinHealthBar.draw(batch,80);
        batch.end();
    }

    public void attack(){
        if(currentEntityPosition.x>MIN_WIDTH&&!isAttacking){
            currentAnimation = attackAnimation;
            isAttacking = true;
            System.out.println("setting");
        }

    }
    public void  updateCord(){
        currentEntityPosition.set(Entity_Rect.x,Entity_Rect.y);
    }
    public void  updateCord(Rectangle rct){
        currentEntityPosition.set(rct.x,rct.y);
    }
    public boolean moveLeft(Array<GoblinEnitityUI> goblins, int currentEnemyId) {
        if(currentEntityPosition.x>MIN_WIDTH&&!nearPlayer&&!isAttacking&& (!cantMoveInCurrentDirection&&playerDirection.equalsIgnoreCase("left")||!playerDirection.equalsIgnoreCase("left"))){
            Vector2 preprePos = new Vector2(previousEntityPosition.x,previousEntityPosition.y);
            previousEntityPosition.x = currentEntityPosition.x;
            currentEntityPosition.x-= Gdx.graphics.getDeltaTime()*Speed;
            Rectangle collisionGoblins = new Rectangle(currentEntityPosition.x,currentEntityPosition.y,Entity_Width,Entity_Height);
            for (int i = 0; i < goblins.size; ++i)
                if(i!=currentEnemyId)
                    if(goblins.get(i).isColliding(collisionGoblins)){
                        currentEntityPosition.x = previousEntityPosition.x;
                        currentEntityPosition.y = previousEntityPosition.y;
                        previousEntityPosition.x = preprePos.x;
                        previousEntityPosition.y = preprePos.y;
                        return false;
                    }
            currentAnimation = leftAnimation;
            cantMoveInCurrentDirection = false;
            playerDirection = "LEFT";

            return true;
        }
        return false;

    }
    public boolean moveRight(Array<GoblinEnitityUI> goblins, int currentEnemyId) {
        if(currentEntityPosition.x<WIDTH&&!nearPlayer&&!isAttacking&& (!cantMoveInCurrentDirection&&playerDirection.equalsIgnoreCase("right")||!playerDirection.equalsIgnoreCase("right"))){
            Vector2 preprePos = new Vector2(previousEntityPosition.x,previousEntityPosition.y);
            previousEntityPosition.x = currentEntityPosition.x;
            currentEntityPosition.x+= Gdx.graphics.getDeltaTime()*Speed;
            Rectangle collisionGoblins = new Rectangle(currentEntityPosition.x,currentEntityPosition.y,Entity_Width,Entity_Height);
            for (int i = 0; i < goblins.size; ++i)
                if(i!=currentEnemyId)
                    if(goblins.get(i).isColliding(collisionGoblins)){
                        currentEntityPosition.x = previousEntityPosition.x;
                        currentEntityPosition.y = previousEntityPosition.y;
                        previousEntityPosition.x = preprePos.x;
                        previousEntityPosition.y = preprePos.y;
                        return false;
                    }
            playerDirection = "RIGHT";
            currentAnimation = rightAnimation;
            cantMoveInCurrentDirection = false;
            return true;
        }
        return false;

    }


    public boolean moveUP(Array<GoblinEnitityUI> goblins, int currentEnemyId){
        if(currentEntityPosition.y<HEIGHT&&!nearPlayer&&!isAttacking&& (!cantMoveInCurrentDirection&&playerDirection.equalsIgnoreCase("up")||!playerDirection.equalsIgnoreCase("up"))){
            Vector2 preprePos = new Vector2(previousEntityPosition.x,previousEntityPosition.y);
            previousEntityPosition.y = currentEntityPosition.y;
            currentEntityPosition.y+= Gdx.graphics.getDeltaTime()*Speed;
            Rectangle collisionGoblins = new Rectangle(currentEntityPosition.x,currentEntityPosition.y,Entity_Width,Entity_Height);
            for (int i = 0; i < goblins.size; ++i)
                if(i!=currentEnemyId)
                    if(goblins.get(i).isColliding(collisionGoblins)){
                        currentEntityPosition.x = previousEntityPosition.x;
                        currentEntityPosition.y = previousEntityPosition.y;
                        previousEntityPosition.x = preprePos.x;
                        previousEntityPosition.y = preprePos.y;
                        return false;
                    }
            playerDirection = "UP";
            currentAnimation = upAnimation;
            cantMoveInCurrentDirection = false;
            return true;
        }
        return false;

    }
    public boolean moveDown(Array<GoblinEnitityUI> goblins, int currentEnemyId){
        if(currentEntityPosition.y>MIN_HEIGHT&&!nearPlayer&&!isAttacking&& (!cantMoveInCurrentDirection&&playerDirection.equalsIgnoreCase("down")||!playerDirection.equalsIgnoreCase("down"))){
            Vector2 preprePos = new Vector2(previousEntityPosition.x,previousEntityPosition.y);
            previousEntityPosition.y = currentEntityPosition.y;
            currentEntityPosition.y-= Gdx.graphics.getDeltaTime()*Speed;
            Rectangle collisionGoblins = new Rectangle(currentEntityPosition.x,currentEntityPosition.y,Entity_Width,Entity_Height);
            for (int i = 0; i < goblins.size; ++i)
                if(i!=currentEnemyId)
                    if(goblins.get(i).isColliding(collisionGoblins)){
                        currentEntityPosition.x = previousEntityPosition.x;
                        currentEntityPosition.y = previousEntityPosition.y;
                        previousEntityPosition.x = preprePos.x;
                        previousEntityPosition.y = preprePos.y;
                        return false;
                    }
            playerDirection = "DOWN";
            currentAnimation = downAnimation;
            cantMoveInCurrentDirection = false;
            return true;
        }
        return false;
    }
    @Override
    public boolean moveLeft() {
        if(currentEntityPosition.x>MIN_WIDTH&&!nearPlayer&&!isAttacking&& (!cantMoveInCurrentDirection&&playerDirection.equalsIgnoreCase("left")||!playerDirection.equalsIgnoreCase("left"))){
            previousEntityPosition.x = currentEntityPosition.x;
            playerDirection = "LEFT";
            currentAnimation = leftAnimation;
            currentEntityPosition.x-= Gdx.graphics.getDeltaTime()*Speed;
            cantMoveInCurrentDirection = false;
            return true;
        }
        return false;

    }
    @Override
    public boolean moveRight() {
        if(currentEntityPosition.x<WIDTH&&!nearPlayer&&!isAttacking&& (!cantMoveInCurrentDirection&&playerDirection.equalsIgnoreCase("right")||!playerDirection.equalsIgnoreCase("right"))){
            previousEntityPosition.x = currentEntityPosition.x;
            playerDirection = "RIGHT";
            currentAnimation = rightAnimation;
            currentEntityPosition.x+= Gdx.graphics.getDeltaTime()*Speed;
            cantMoveInCurrentDirection = false;
            return true;
        }
        return false;

    }

    @Override

    public boolean moveUP(){
        if(currentEntityPosition.y<HEIGHT&&!nearPlayer&&!isAttacking&& (!cantMoveInCurrentDirection&&playerDirection.equalsIgnoreCase("up")||!playerDirection.equalsIgnoreCase("up"))){
            previousEntityPosition.y = currentEntityPosition.y;
            playerDirection = "UP";
            currentAnimation = upAnimation;
            currentEntityPosition.y+= Gdx.graphics.getDeltaTime()*Speed;
            cantMoveInCurrentDirection = false;
            return true;
        }
        return false;

    }
    @Override
    public boolean moveDown(){
        if(currentEntityPosition.y>MIN_HEIGHT&&!nearPlayer&&!isAttacking&& (!cantMoveInCurrentDirection&&playerDirection.equalsIgnoreCase("down")||!playerDirection.equalsIgnoreCase("down"))){
            previousEntityPosition.y = currentEntityPosition.y;
            playerDirection = "DOWN";
            currentAnimation = downAnimation;
            currentEntityPosition.y-= Gdx.graphics.getDeltaTime()*Speed;
            cantMoveInCurrentDirection = false;
            return true;
        }
        return false;
    }

}
