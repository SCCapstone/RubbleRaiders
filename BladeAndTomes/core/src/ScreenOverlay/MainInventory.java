package ScreenOverlay;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MainInventory {
    private Stage Game;
    TextButton.TextButtonStyle style;
    private Button inventorySloat[];
    float optionSpace, optionWidth,optionHeight,optionLocX,optionLocY;

    public MainInventory(Stage game, TextButton.TextButtonStyle Style){
        Game = game;
        style = Style;
        optionSpace = 150; optionWidth = 256f;
        optionHeight = 400f; optionLocX = 900f;
        optionLocY = 960f;

        inventorySloat = new TextButton[]{
                new TextButton("", style),
                new TextButton("", style),
                new TextButton("", style),
                new TextButton("", style),
                new TextButton("", style)
        };

    }
    public void update(){

    }
    public void show(){
        for(int i = 0; i <inventorySloat.length;++i) {
            inventorySloat[i].hit(optionLocX, optionLocY * optionSpace, true);
            inventorySloat[i].setX(optionLocX);
            inventorySloat[i].setY(optionLocY * optionSpace);
            inventorySloat[i].setWidth(optionWidth);
            inventorySloat[i].setHeight(optionHeight);
            Game.addActor(inventorySloat[i]);
        }
    }
    public Stage getInstance(){
        return null;}

}
