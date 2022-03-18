package ScreenOverlayRework.Health;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Disposable;

public class Health implements Disposable {
    private final BladeAndTomes game;
    private ProgressBar healthBar;
    private Skin healthBarSkin;
    private Table group;
    private Color change;
    private Label HealthLabel;
    private AssetManager manager;
    private String HealthBarPath;
    public Health(BladeAndTomes game, AssetManager manager, int counter)  {
        this.game = game;
        HealthBarPath = "SkinAssets/ProgressBar/healthbarUI";
        this.manager = manager;
        if(!manager.isLoaded(HealthBarPath+".json",Skin.class));{
        manager.load(HealthBarPath+".json",Skin.class,new SkinLoader.SkinParameter(HealthBarPath+".atlas"));
        manager.finishLoading();}
        healthBarSkin = manager.get(HealthBarPath+".json");
        healthBar = new ProgressBar(0,10,0.5f,false,healthBarSkin);
        healthBar.setValue(game.player.getHealthPoints());
        HealthLabel = new Label("Health"+String.valueOf(counter),game.generalLabelStyle);
        HealthLabel.setSize(0,0);

        group = new Table();
        group.setBounds(game.stageInstance.getWidth()/1.3f, game.stageInstance.getHeight()/2.3f,
                game.stageInstance.getWidth()/10f, game.stageInstance.getHeight());
        change = new Color();
        ColorFade();
        healthBar.setColor(change);
        healthBar.setSize(200,20);
        HealthLabel.setPosition(50,515);
        HealthLabel.setFontScale(1.1f);
        healthBar.setPosition(0,500);
        group.addActor(healthBar);
        group.addActor(HealthLabel);
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
    public Table getHealthBar() {
        return group;
    }


    public void incrementHealth(){
    }
    public void decrementHealth(){

    }
    public void clearAssetManger(){
        manager.unload("SkinAssets/ProgressBar/healthbarUI.json");
        manager.unload("SkinAssets/ProgressBar/healthbarUI.atlas");
        manager.clear();
    }

    @Override
    public void dispose() {
        manager.unload("SkinAssets/ProgressBar/healthbarUI.json");
        manager.unload("SkinAssets/ProgressBar/healthbarUI.atlas");
        manager.dispose();
    }

}
