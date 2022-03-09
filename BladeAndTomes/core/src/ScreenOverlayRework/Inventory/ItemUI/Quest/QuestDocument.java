package ScreenOverlayRework.Inventory.ItemUI.Quest;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class QuestDocument {
    private AssetManager manager;
    private BladeAndTomes game;
    private Table table;
    private ProgressBar questProgressBar;
    private TextureAtlas questBarAtlas;
    private Skin questBarSkin;
    private TextButton remove_complete_button;

    private Stack stack;

    private int reward;
    private String questDescription;
    public QuestDocument(BladeAndTomes game,AssetManager manager){
        this.game = game;
        this.manager = manager;
        table = new Table();

        questBarAtlas = new TextureAtlas(Gdx.files.internal("SkinAssets/ProgressBar/healthbarUI.atlas"));
        questBarSkin = new Skin((Gdx.files.internal("SkinAssets/ProgressBar/healthbarUI.json")),questBarAtlas);
        questProgressBar = new ProgressBar(0,10,0.5f,false,questBarSkin);
        questProgressBar.setValue(game.player.getHealthPoints());

        remove_complete_button = new TextButton("Remove Quest",game.generalTextButtonStyle);
        stack = new Stack();
        createLabel();
    }

    public void createLabel(){
        table.add(stack).center();
        Label baseBackGround = new Label("",game.BaseLabelStyle2);
        baseBackGround.setColor(Color.LIGHT_GRAY);
        table.add( baseBackGround).size(500,150);
        Label noQuest = new Label("NO QUEST",game.BaseLabelStyle2);
        stack.add(noQuest);
    }
    public Table getTable(){
        return table;
    }
}
