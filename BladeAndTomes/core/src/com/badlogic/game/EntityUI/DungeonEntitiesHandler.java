/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.badlogic.game.EntityUI;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.Random;

public class DungeonEntitiesHandler implements Disposable {

    BladeAndTomes game;
    PlayerEnitityUI player;
    Array<GoblinEnitityUI> goblins;
    SpriteBatch batch;
    boolean isInventoryVisible;

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

    public DungeonEntitiesHandler(BladeAndTomes game,
                                  Object dungeonLeft,
                                  Object dungeonRight,
                                  Object dungeonUP,
                                  Object dungeonDown,
                                  Object chest,
                                  boolean isPortalThere) {
        this.game = game;
        isInventoryVisible = false;
        load_assets();
        player = new PlayerEnitityUI((OrthographicCamera) game.stageInstance.getCamera(), game.batch,
                game.controls, 960, 540, 50, 50, game.moveUpAnimation,
                game.moveDownAnimation, game.moveLeftAnimation, game.moveRightAnimation);
        /*
         * Goblin Animations from Goblin Class
         * */
        idleTextureAtlas =  game.assets.get("AnimationFiles/goblinIdle.atlas");
        idleAnimation = new Animation<TextureRegion>(3/10f, idleTextureAtlas.getRegions());
        currentAnimation = idleAnimation;
        attackTextureAtlas =  game.assets.get("AnimationFiles/goblinAttack.atlas");
        attackAnimation = new Animation<TextureRegion>(1/15f, attackTextureAtlas.getRegions());
        moveDownTextureAtlas =  game.assets.get("AnimationFiles/goblinMoveDown.atlas");
        moveDownAnimation = new Animation<TextureRegion>(3/10f, moveDownTextureAtlas.getRegions());
        moveUpTextureAtlas =  game.assets.get("AnimationFiles/goblinMoveUp.atlas");
        moveUpAnimation = new Animation<TextureRegion>( 3/10f, moveUpTextureAtlas.getRegions());
        moveLeftTextureAtlas =  game.assets.get("AnimationFiles/goblinMoveLeft.atlas");
        moveLeftAnimation = new Animation<TextureRegion>(3/10f, moveLeftTextureAtlas.getRegions());
        moveRightTextureAtlas =  game.assets.get("AnimationFiles/goblinMoveRight.atlas");
        moveRightAnimation = new Animation<TextureRegion>(3/10f, moveRightTextureAtlas.getRegions());
        // Contains All goblins
        goblins = new Array<>();
        createGoblin();
        createGoblin();
        createGoblin();


    }
    public void load_assets() {

        if(!game.assets.contains("AnimationFiles/goblinIdle.atlas", TextureAtlas.class)){
            game.assets.load("AnimationFiles/goblinIdle.atlas", TextureAtlas.class);
        }
        if(!game.assets.contains("AnimationFiles/goblinAttack.atlas", TextureAtlas.class)){
            game.assets.load("AnimationFiles/goblinAttack.atlas", TextureAtlas.class);
        }
        if(!game.assets.contains("AnimationFiles/goblinMoveDown.atlas", TextureAtlas.class)){
            game.assets.load("AnimationFiles/goblinMoveDown.atlas", TextureAtlas.class);
        }
        if(!game.assets.contains("AnimationFiles/goblinMoveUp.atlas", TextureAtlas.class)){
            game.assets.load("AnimationFiles/goblinMoveUp.atlas", TextureAtlas.class);
        }
        if(!game.assets.contains("AnimationFiles/goblinMoveLeft.atlas", TextureAtlas.class)){
            game.assets.load("AnimationFiles/goblinMoveLeft.atlas", TextureAtlas.class);
        }
        if(!game.assets.contains("AnimationFiles/goblinMoveRight.atlas", TextureAtlas.class)){
            game.assets.load("AnimationFiles/goblinMoveRight.atlas", TextureAtlas.class);
        }
        if(!game.assets.contains("SkinAssets/GobHealth/gobHealth.json", Skin.class)){
            game.assets.load("SkinAssets/GobHealth/gobHealth.json", Skin.class,
                    new SkinLoader.SkinParameter("SkinAssets/GobHealth/gobHealth"+".atlas"));
        }
        game.assets.finishLoading();
    }
    public void render(float delta) {
        player.render(delta);
        checkPlayerEnterPortal();
        if (checkPlayerCollision()){
            player.resetToPreviousPosition();
            player.cantMoveInCurrentDirection = true;
        }
      //  portalBuilding.render();
        if(Gdx.input.isKeyJustPressed(game.controls.getOpenInventory())){
            isInventoryVisible =!isInventoryVisible;
            game.overlays.setHiddenTableVisibility(isInventoryVisible);
        }
        if(Gdx.input.isKeyPressed(game.controls.getMoveUp())||
                Gdx.input.isKeyPressed(game.controls.getMoveDown())
                || Gdx.input.isKeyPressed(game.controls.getMoveLeft())
                || Gdx.input.isKeyPressed(game.controls.getMoveRight()) )
            for (int i = 0; i < goblins.size; ++i)
                goblinPlayerTracker(i);
        renderGoblins(delta);

    }
    public void checkPlayerEnterPortal(){
//        if(player.isColliding(portalBuilding.Building_Rect)) {
//            game.stageInstance.clear();
//            BladeAndTomes.enterDungeon = true;
//            BladeAndTomes.exitDungeon = false;
//            game.setScreen(new Dungeon(game));
//        };
    }
    public boolean checkPlayerCollision() {

        for (int i = 0; i < goblins.size; ++i) {
            Rectangle goblinBig = new Rectangle(goblins.get(i).Entity_Rect.x,goblins.get(i).Entity_Rect.y,goblins.get(i).Entity_Rect.width-15,goblins.get(i).Entity_Rect.height-15);
            if (player.isColliding(goblinBig)) return true;
        }
//        if(player.isColliding(questBord.Building_Rect)||player.isColliding(buyerBuilding.Building_Rect)||
//                player.isColliding(sellerBuilding.Building_Rect)||player.isColliding(CyanTavernsBuilding.Building_Rect)||
//                player.isColliding(PurpleChapelsBuilding.Building_Rect)) return true;
//        else {
//            questBord.isPlayerNear = nearBuilding(questBord.Building_Rect);
//            buyerBuilding.isPlayerNear = nearBuilding(buyerBuilding.Building_Rect);
//            sellerBuilding.isPlayerNear = nearBuilding(sellerBuilding.Building_Rect);
//        }
        return false;
    }
    public boolean nearBuilding(Rectangle rectangle){
        Rectangle angle = new Rectangle(rectangle);
        angle.setPosition(angle.x,angle.y-32);
        angle.setSize(angle.width,angle.height+32);
        return player.Entity_Rect.overlaps(angle);
    }
    public String bounceBackOpposite(String direction){
        if(direction.equalsIgnoreCase("Up"))
            return "Down";
        else if(direction.equalsIgnoreCase("down"))
            return "UP";
        else if(direction.equalsIgnoreCase("left"))
            return "Right";
        else return "Left";
    }
    public void renderGoblins(float delta) {
        for (int i = 0; i < goblins.size; ++i)
            goblins.get(i).render(delta);
//        goblins.sort(new Comparator(){
//
//            @Override
//            public int compare(Object o1, Object o2) {
//                GoblinEnitityUI goblin1 = (GoblinEnitityUI) o1;
//                GoblinEnitityUI goblin2 = (GoblinEnitityUI) o2;
//
//                float deltaGoblinPlayer_X = goblin1.Entity_Rect.x - player.Entity_Rect.x;
//                float deltaGoblinPlayer_Y = goblin1.Entity_Rect.y - player.Entity_Rect.y;
//                // 64 pixel thrush hold from goblin to player
//                float nearThrushHold =  32;
//                // Check if enemy is near player within a given threshold in both x and y.
//                float nearPlayer_x_1 = (int)deltaGoblinPlayer_X/(int)nearThrushHold;
//                float nearPlayer_y_1 = deltaGoblinPlayer_Y/nearThrushHold;
//
//                 deltaGoblinPlayer_X = goblin2.Entity_Rect.x - player.Entity_Rect.x;
//                 deltaGoblinPlayer_Y = goblin2.Entity_Rect.y - player.Entity_Rect.y;
//                float nearPlayer_x_2 = (int)deltaGoblinPlayer_X/(int)nearThrushHold;
//                float nearPlayer_y_2 = deltaGoblinPlayer_Y/nearThrushHold;
//                if(!goblin1.playerDirection.equalsIgnoreCase(goblin2.playerDirection))
//                    if(goblin1.playerDirection.equalsIgnoreCase(player.playerDirection))
//                        return 1;
//                    else
//                        return -1;
//                else if(goblin1.playerDirection.equalsIgnoreCase("left")||goblin1.playerDirection.equalsIgnoreCase("right"))
//                    return (nearPlayer_x_1>nearPlayer_x_2)?1:-1;
//                else
//                    return (nearPlayer_y_1>nearPlayer_y_2)?1:-1;
//            }
//        });
    }
    public int  createGoblin() {
        GoblinEnitityUI goblin = new GoblinEnitityUI((OrthographicCamera) game.stageInstance.getCamera(), (SpriteBatch) game.stageInstance.getBatch(),
                game.controls, 700+100*goblins.size, 500, 45, 45, moveUpAnimation,
                moveDownAnimation, moveLeftAnimation, moveRightAnimation,attackAnimation,
                game.assets.get("SkinAssets/GobHealth/gobHealth.json", Skin.class),
                10);
        goblins.add(goblin);
        int goblinID = goblins.size - 1;
        return goblinID;
    }
    // The logic in this method was inspired from movement method in goblin class.
    public void goblinPlayerTracker(int goblinIndex){
        Rectangle goblin = goblins.get(goblinIndex).Entity_Rect;
        float deltaGoblinPlayer_X = goblin.x - player.Entity_Rect.x;
        float deltaGoblinPlayer_Y = goblin.y - player.Entity_Rect.y;
        // 64 pixel thrush hold from goblin to player
        float nearThrushHold =  32;
        // Check if enemy is near player within a given threshold in both x and y.
        boolean nearPlayer_x = (int)deltaGoblinPlayer_X/(int)nearThrushHold == 0;
        boolean nearPlayer_y = deltaGoblinPlayer_Y/nearThrushHold == 0;
        // Notifies to enemy if player is near for attack
        int order = (new Random().nextInt(2));
        goblins.get(goblinIndex).nearPlayer = nearPlayer_x && nearPlayer_y;
            if (!nearPlayer_x)
                if ((int) deltaGoblinPlayer_X / (int) nearThrushHold > 0) {
                    getGoblin(goblinIndex).moveLeft(goblins,goblinIndex);
                } else {
                    getGoblin(goblinIndex).moveRight(goblins,goblinIndex);
                }
            else if (!nearPlayer_y)
                if ((int) deltaGoblinPlayer_Y / (int) nearThrushHold > 0) {
                    getGoblin(goblinIndex).moveDown(goblins,goblinIndex);
                } else {
                    getGoblin(goblinIndex).moveUP(goblins,goblinIndex);
                }

        goblinCollision(goblinIndex);
        }
    public boolean goblinCollision(int currentEnemyId){
        Rectangle goblinPlayerCollisionRect = new Rectangle(goblins.get(currentEnemyId).Entity_Rect.x-15,goblins.get(currentEnemyId).Entity_Rect.y-15,
                goblins.get(currentEnemyId).Entity_Rect.width+15,goblins.get(currentEnemyId).Entity_Rect.height+15);
        Rectangle goblinPlayerCloseRangeRect = new Rectangle(goblins.get(currentEnemyId).Entity_Rect.x-20,goblins.get(currentEnemyId).Entity_Rect.y-20,
                goblins.get(currentEnemyId).Entity_Rect.width+20,goblins.get(currentEnemyId).Entity_Rect.height+20);
        Rectangle goblinGoblinCloseRangeRect = new Rectangle(goblins.get(currentEnemyId).Entity_Rect.x-25,goblins.get(currentEnemyId).Entity_Rect.y-25,
                goblins.get(currentEnemyId).Entity_Rect.width+25,goblins.get(currentEnemyId).Entity_Rect.height+25);
        if(player.isColliding(goblinPlayerCloseRangeRect))
            goblins.get(currentEnemyId).attackRange = true;
        else
            goblins.get(currentEnemyId).attackRange = false;
        if(player.isColliding(goblinPlayerCloseRangeRect))
         goblins.get(currentEnemyId).attack();

        if(player.isColliding(goblinPlayerCollisionRect)){
            goblins.get(currentEnemyId).nearPlayer = true;
            goblins.get(currentEnemyId).resetToPreviousPosition();
            if((player.isColliding(goblins.get(currentEnemyId).Entity_Rect)))
                goblins.get(currentEnemyId).bounceBack((player.playerDirection),1);
            return true;
        }
        else{
            goblins.get(currentEnemyId).nearPlayer = false;
        }
//        for (int i = 0; i < goblins.size; ++i)
//            if ((goblins.get(i).isColliding(goblinGoblinCloseRangeRect)&&i!=currentEnemyId)){
//
//////               Rectangle rect = goblins.get(i).Entity_Rect.fitOutside(goblins.get(currentEnemyId).Entity_Rect);
//////                goblins.get(currentEnemyId).updateCord();
//////                goblins.get( i).updateCord(rect);
////                Rectangle rectangle = new Rectangle();
////                Intersector.intersectRectangles(goblins.get(currentEnemyId).Entity_Rect, goblins.get(i).Entity_Rect,rectangle);
////             //   goblins.get(i).Entity_Rect.fitOutside(rectangle);goblins.get(currentEnemyId).Entity_Rect.fitOutside(rectangle);
//////                goblins.get(i).Entity_Rect
//////                goblins.get( i)  .updateCord(goblins.get(i).Entity_Rect.fitInside(rectangle));
////                goblins.get(i).updateCord(goblins.get(i).Entity_Rect.fitOutside(rectangle));
//
////                goblins.get(currentEnemyId).Entity_Rect.in
//                        goblins.get(currentEnemyId).resetToPreviousPosition();
//                        goblins.get(i).bounceBack( goblins.get(i).playerDirection,1);
//                        goblins.get(currentEnemyId).cantMoveInCurrentDirection = true;
//
//
//                    return true;
////                goblins.get(currentEnemyId).bounceBack();
//               // return true ;
////                goblins.get(currentEnemyId).resetToPreviousPosition();
////                goblins.get(currentEnemyId).bounceBack();
////                goblins.get(currentEnemyId).cantMoveInCurrentDirection = true;
//            }
        goblins.get(currentEnemyId).cantMoveInCurrentDirection = false;
        return false;
    }

    public GoblinEnitityUI getGoblin(int goblinID) {
        return goblins.get(goblinID);

    }
    public void createBuilding() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }


}
