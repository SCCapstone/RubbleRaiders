package ScreenOverlayRework.Health;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class Health {
    private final BladeAndTomes game;
    private ProgressBar healthBar;
    private TextureAtlas healthBarAtlas;
    private Skin healthBarSkin;
    private Group group;
    private Color change;
    public Health(BladeAndTomes game)  {
        this.game = game;
        healthBarAtlas = new TextureAtlas(Gdx.files.internal("SkinAssets/ProgressBar/healthbarUI.atlas"));
        healthBarSkin = new Skin((Gdx.files.internal("SkinAssets/ProgressBar/healthbarUI.json")),healthBarAtlas);
        healthBar = new ProgressBar(0,10,0.5f,false,healthBarSkin);
        healthBar.setValue(game.player.getHealthPoints());

        group = new Stack();
        group.setBounds(game.stageInstance.getWidth()/1.3f, game.stageInstance.getHeight()/2.3f,
                game.stageInstance.getWidth()/10f, game.stageInstance.getHeight());
        change = new Color();
        ColorFade();
        healthBar.setColor(change);
        group.addActor(healthBar);

    }

    public void ColorFade(){
        float red   = (game.player.getHealthPoints()) <= 5 ? (float) Math.pow(2, (game.player.getHealthPoints())) : 0;
        float green = (game.player.getHealthPoints()) > 5 ? (float) Math.pow(3, (game.player.getHealthPoints())) : 0;
        change.set(red,green ,0,0.5f);

    }
    public void update() {
        ColorFade();
        healthBar.setColor(change);
        healthBar.setValue(game.player.getHealthPoints());
    }
    public Group getHealthBar() {
        return group;
    }
    public void incrementHealth(){
    }
    public void decrementHealth(){

    }

}
