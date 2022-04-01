package ScreenOverlayRework.Inventory.ItemUI;

import ScreenOverlayRework.Inventory.ItemUI.Skill.SkillInterface;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import java.util.concurrent.atomic.AtomicInteger;

public class SkillUI {
    private Table table;
    private final BladeAndTomes game;
    private AssetManager assets;
    private Label tile;

    private Array<SkillInterface> skills;
    AtomicInteger test;

    public SkillUI(BladeAndTomes game, AssetManager mainItemsUIManager) {

        // The following block contains elements need to draw graphics
        this.game = game;
        this.assets = mainItemsUIManager;
        table = new Table();
        tile = new Label("",game.BaseLabelStyle1);

        // Skills
        skills = new Array<>();
        addSkillsToArray();
        drawSkillTable();


    }

    public void addSkillsToArray(){


        SkillInterface skill = new SkillInterface(game,
                "Acrobatics",
                game.player.acrobatics,
                game.player.tokens,
                game.assets);
        skills.add(skill);
        skill = new SkillInterface(game,
                "Bruteforce",
                game.player.bruteforce,
                game.player.tokens,
                game.assets);
        skills.add(skill);
        skill = new SkillInterface(game,
                "Speech",
                game.player.speech,
                game.player.tokens,
                game.assets);
        skills.add(skill);

        skill = new SkillInterface(game,
                "Barter",
                game.player.barter,
                game.player.tokens,
                game.assets);
        skills.add(skill);

        skill = new SkillInterface(game,
                "Awareness",
                game.player.awareness,
                game.player.tokens,
                game.assets);
        skills.add(skill);

        skill = new SkillInterface(game,
                "Intuition",
                game.player.intuition,
                game.player.tokens,
                game.assets);
        skills.add(skill);
    }
    public void drawSkillTable()
    {
        tile.setText("        Tokens: "+game.player.tokens);
        table.add(tile).size(150,50).top().row();
        for(int i = 0; i< skills.size;++i)
        table.add(skills.get(i).getTable()).row();
    }
    public void updateTokens(){
        tile.setText("        Tokens: "+game.player.tokens);
    }
    public void render(){
        updateTokens();
        for(int i = 0; i< skills.size;++i)
            skills.get(i).render();
    }

    public Table getTable(){
        return table;
    }
}
