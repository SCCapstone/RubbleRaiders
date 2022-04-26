package ScreenOverlayRework.Inventory.ItemUI.Skill;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.concurrent.atomic.AtomicInteger;

public class SkillInterface {
    private String typeInfo;
    private AtomicInteger upgradeObj;
    private AssetManager assets;
    private Table table;
    private TextButton plus;
    private Label skillType;
    private Label currentAmount;
    private AtomicInteger avaiableTokens;

    public SkillInterface( BladeAndTomes game,
                          String typeInfo,
                          AtomicInteger upgradeObj, AtomicInteger avaiableTokens,
                          AssetManager assets){

        // Skill info and values
        this.typeInfo = typeInfo;
        this.upgradeObj = upgradeObj;
        this.assets = assets;
        this.avaiableTokens = avaiableTokens;

        // Table that contains all layouts and listeners
        table = new Table();
        // UI Components Init
        plus = new TextButton("Upgrade",game.generalTextButtonStyle);
        skillType = new Label("     "+typeInfo,game.BaseLabelStyle1);
        currentAmount = new Label("     Current Level: \n     \t"+String.valueOf(upgradeObj.get()),game.BaseLabelStyle1);

        // Draws Skills layout
        createSkillLayout();
        // Adds all necessary listeners
        addListener();


    }

    /**
     * Creates a layout for skill stats and upgrade
     */
    public void createSkillLayout(){

        table.add(skillType).size(125,70).spaceTop(50);
        skillType.setFontScale(.7f);
        table.add(currentAmount).size(160,70).space(10);
        currentAmount.setFontScale(.7f);
        table.add(plus).size(160,50).space(10).spaceTop(10);
        plus.getStyle().font.getData().setScale(0.65f);

    }

    /**
     * The method is a listener
     */
    public void addListener(){
        plus.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                plus.setText("Need "+String.valueOf(((int) avaiableTokens.get()/3 +1))+" Token(s)");
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                plus.setText("Upgrade");
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(avaiableTokens.get()>=(((int) avaiableTokens.get()/3 +1))){
                    avaiableTokens.getAndAdd(-((int) avaiableTokens.get()/3 +1));
                    upgradeObj.getAndAdd(1);
                }
            }
        });

    }

    public void render(){
        // updates skills tokens
        currentAmount.setText("     Current Level: \n     "+String.valueOf(upgradeObj.get()));
    }

    public Table getTable(){
        // returns skills table with layout
        return table;
    }
}
