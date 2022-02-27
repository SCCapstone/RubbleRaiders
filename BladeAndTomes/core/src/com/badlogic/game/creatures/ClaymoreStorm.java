package com.badlogic.game.creatures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class ClaymoreStorm  extends Enemy{

    Texture enemyTex;
    public final Image enemyImage;

    public ClaymoreStorm(final Player player) {

        super(1000000000, 1000000000,1000000000,15,64,64,player);
        enemyTex = new Texture(Gdx.files.internal("Goblin.png"));
        enemyImage = new Image(enemyTex);
        enemyImage.setOrigin(enemyImage.getImageWidth()/2, enemyImage.getImageHeight()/2);
        enemyImage.setSize(64,64);
    }

    public boolean movement() {

        if(true) {
            return false;
        }
        else if(player.playerIcon.getImageY() > enemyImage.getImageY()) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX(), enemyImage.getY() + 64,0));
        }
        else if(player.playerIcon.getImageY() < enemyImage.getImageY()) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX(), enemyImage.getY() - 64,0));
        }
        else if(player.playerIcon.getImageX() > enemyImage.getImageX()) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX() + 64, enemyImage.getY(),0));
        }
        else if(player.playerIcon.getImageX() < enemyImage.getImageX()) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX() - 64, enemyImage.getY(),0));
        }
        return true;
    }

    public int attackPlayer() {
        int hitRoll = (int)(Math.random()*(20)+1000000000);
        if (hitRoll >= player.getArmorPoints()) {
            return (int)(Math.random()*(100000-10000)+100000);
        }
        else {
            return 0;
        }
    }


}
