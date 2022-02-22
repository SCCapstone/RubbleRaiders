package com.badlogic.game.creatures;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Goblin  extends Enemy{

    Texture enemyTex;
    public final Image enemyImage;

    public Goblin(Player player) {

        super(15,15,15,64,64, player);

        //this.GAME = GAME;
        enemyTex = new Texture(Gdx.files.internal("Goblin.png"));
        enemyImage = new Image(enemyTex);
        enemyImage.setOrigin(enemyImage.getImageWidth()/2, enemyImage.getImageHeight()/2);
        enemyImage.setSize(64,64);
        xCord = MathUtils.random(360, 1600);
        yCord = MathUtils.random(240, 960);
        enemyImage.setPosition(xCord, yCord);
        //GAME.stageInstance.addActor(enemyImage);

    }

    public void movement() {
        float x_distance = enemyImage.getX() - player.playerIcon.getX();
        float y_distance = enemyImage.getY() - player.playerIcon.getY();

        if(x_distance < 64 || x_distance > -64 || y_distance > -64 || y_distance < 64) {
            return;
        }
        else if(player.playerIcon.getY() > enemyImage.getY()) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX(), enemyImage.getY() + 64,0));
        }
        else if(player.playerIcon.getY() < enemyImage.getY()) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX(), enemyImage.getY() - 64,0));
        }
        else if(player.playerIcon.getX() > enemyImage.getX()) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX() + 64, enemyImage.getY(),0));
        }
        else if(player.playerIcon.getX() < enemyImage.getX()) {
            enemyImage.addAction(Actions.moveTo(enemyImage.getX() - 64, enemyImage.getY(),0));
        }

    }

    public int attackPlayer() {
        int hitRoll = (int)(Math.random()*(20)+1);
        if (hitRoll >= player.getArmorPoints()) {
           return (int)(Math.random()*(3)+1);
        }
        else {
            return 0;
        }
    }


}
