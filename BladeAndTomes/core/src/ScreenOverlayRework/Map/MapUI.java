package ScreenOverlayRework.Map;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MapUI {
    private Label mapBorderUI;
    private Table group;
    private BladeAndTomes game;
    private int MapWidth, MapHeight;
    public MapUI(BladeAndTomes game){
        this.game = game;

        mapBorderUI = new Label("           Map",game.BaseLabelStyle1);
        group = new Table();
        group.defaults();

        MapWidth = 300; MapHeight = 300;
//        mapBorderUI.setSize();
        group.add(mapBorderUI);

        group.setBounds(game.stageInstance.getWidth()/2.3f, game.stageInstance.getHeight()/2.7f,
                game.stageInstance.getWidth(), game.stageInstance.getHeight());
    }
    public Table getMapUI(){
        return group;
    }
}
