package com.badlogic.game.EntityUI;

import Keyboard_Mouse_Controls.MainMenuControls;
        import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCBuyer;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCSeller;
import ScreenOverlayRework.Inventory.NPCInventoryUI.TownHallQuestBoard;
        import ScreenOverlayRework.OverlayManager;
        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.Input;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.scenes.scene2d.Stage;
        import com.badlogic.gdx.scenes.scene2d.ui.Label;
        import com.badlogic.gdx.utils.Disposable;
public class MarketBulding extends BuildingUIBase implements Disposable {
    private NPCSeller seller;
    private Label questBoard_Msg;
    public boolean isPlayerNear;
    public boolean isPlayerTrading;
    private MainMenuControls controls;
    private OverlayManager overlayManager;
    Stage stage;

    public MarketBulding(float building_Width,
                            float building_Height,
                            float loc_X, float loc_Y,
                            Texture building,
                            SpriteBatch batch,
                            Stage stage,
                            NPCSeller seller,
                            MainMenuControls controls,
                            OverlayManager overlayManager,
                            Label.LabelStyle labelStyle) {
        super(building_Width, building_Height, loc_X, loc_Y, building, batch);
        this.seller = seller;
        questBoard_Msg = new Label("Press " + Input.Keys.toString(controls.getTradeMenu()) + " To See Trades",labelStyle );
        questBoard_Msg.setPosition(Building_Rect.x+Building_Rect.width/3,Building_Rect.y+Building_Rect.height/4);
        stage.addActor(questBoard_Msg);
        questBoard_Msg.setZIndex(0);
        isPlayerNear = false;
        isPlayerTrading = false;
        this.controls = controls;
        this.overlayManager = overlayManager;
        this.stage = stage;
    }
    // Renders All components
    @Override
    public void render(){
        drawBuilding();
        batch.begin();
        questBoard_Msg.setVisible(isPlayerNear&&!isPlayerTrading);
        batch.end();
        Trade();
    }

    // Player Trading UI Display
    private void Trade() {
        if(isPlayerNear){
            if(Gdx.input.isKeyJustPressed(controls.getTradeMenu())){
                isPlayerTrading = !isPlayerTrading;
                isInventoryVisible = false;
                overlayManager.setHiddenTableVisibility(false);
                overlayManager.NPCSellerInventory(isPlayerTrading,seller);
            }
        } else{
            isPlayerTrading = false;
            overlayManager.NPCSellerInventory(isPlayerTrading,seller);
        }
        if(Gdx.input.isKeyJustPressed(controls.getOpenInventory())){
            isPlayerTrading = false;
            overlayManager.NPCSellerInventory(false,seller);
        }
    }


    @Override
    public void dispose() {

    }
}
