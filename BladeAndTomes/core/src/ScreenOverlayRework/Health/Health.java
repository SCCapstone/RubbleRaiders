package ScreenOverlayRework.Health;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Health {
    private final BladeAndTomes game;
    private ProgressBar healthBar;
    private TextureAtlas healthBarAtlas;
    private Skin healthBarSkin;
    public Health(BladeAndTomes game)  {
        this.game = game;
        healthBarAtlas = new TextureAtlas(Gdx.files.internal("SkinAssets/ProgressBar/healthbarUI.atlas"));
        healthBarSkin = new Skin((Gdx.files.internal("SkinAssets/ProgressBar/healthbarUI.json")),healthBarAtlas);
        healthBar = new ProgressBar(0,10,1,false,healthBarSkin);
        healthBar.setPosition(500,500);
        healthBar.setValue(game.player.getHealthPoints());
    }
    public void update() {
        healthBar.setValue(game.player.getHealthPoints());
    }
    public ProgressBar getHealthBar() {
        return healthBar;
    }
    public void incrementHealth(){

    }
    public void decrementHealth(){

    }

}
