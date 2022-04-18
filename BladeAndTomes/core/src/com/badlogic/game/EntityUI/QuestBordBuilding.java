package com.badlogic.game.EntityUI;

import Keyboard_Mouse_Controls.MainMenuControls;
import ScreenOverlayRework.Inventory.NPCInventoryUI.TownHallQuestBoard;
import ScreenOverlayRework.OverlayManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;



public class QuestBordBuilding extends BuildingUIBase implements Disposable {
    // The Trade UI with trades
    private TownHallQuestBoard questBoard;
    // Building Trade Message
    private Label questBoard_Msg;
    // Boolean Var to check if player is near the building
    public boolean isPlayerNear;
    // Boolean Var indicates if player is in trade UI or not
    public boolean isPlayerTrading;
    // Keyboard Controls from user selection
    private MainMenuControls controls;
    // The current overlay manager containing all UI elements
    private OverlayManager overlayManager;
    // Current Stage
    Stage stage;
    /**
     * @param entrance - Where the player enters at. 1 = N, 2 = E, 3 = W, 4 = S
     */
    public QuestBordBuilding(float building_Width,
                             float building_Height,
                             float loc_X, float loc_Y,
                             Texture building,
                             SpriteBatch batch,
                             Stage stage,
                             TownHallQuestBoard questBoard,
                             MainMenuControls controls,
                             OverlayManager overlayManager,
                             Label.LabelStyle labelStyle) {
        super(building_Width, building_Height, loc_X, loc_Y, building, batch);

        // Sets Current Variables
        this.questBoard = questBoard;
        this.controls = controls;
        this.overlayManager = overlayManager;
        this.stage = stage;

        // Defines Message Label and Position
        questBoard_Msg = new Label("Press " + Input.Keys.toString(controls.getTradeMenu()) + " To Buy Quests",labelStyle );
        questBoard_Msg.setPosition(Building_Rect.x+Building_Rect.width,Building_Rect.y+Building_Rect.height);
        questBoard_Msg.setZIndex(0);
        stage.addActor(questBoard_Msg);

        // Setting Initial Variables Values
        isPlayerNear = false;
        isPlayerTrading = false;
    }
    // Renders All components
    @Override
    public void render(){
        // Draws Building based on given Texture and Cords
        drawBuilding();
        // Setting Message Visibility
        questBoard_Msg.setVisible(isPlayerNear&&!isPlayerTrading&&!overlayManager.isHiddenInventoryVisible);
        // Displaying Trade UI if player is near building
        Trade();
    }

    // Player Trading UI Display
    private void Trade() {
        if(isPlayerNear){
            if(Gdx.input.isKeyJustPressed(controls.getTradeMenu())){
                isPlayerTrading = !isPlayerTrading;
                overlayManager.setHiddenTableVisibility(false);
                overlayManager.setQuestBoardTradeVisibility(isPlayerTrading,questBoard);
                }
        } else{
            isPlayerTrading = false;
            overlayManager.setQuestBoardTradeVisibility(isPlayerTrading,questBoard);
        }
        if(Gdx.input.isKeyJustPressed(controls.getOpenInventory())){
            isPlayerTrading = false;
            overlayManager.setQuestBoardTradeVisibility(isPlayerTrading,questBoard);
        }
    }


    @Override
    public void dispose() {

    }
}
