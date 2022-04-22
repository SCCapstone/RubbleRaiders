package com.badlogic.game.EntityUI;

import Keyboard_Mouse_Controls.MainMenuControls;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCSeller;
import ScreenOverlayRework.OverlayManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;

public class MarketBulding extends BuildingUIBase implements Disposable {
    // Boolean Var to check if player is near the building
    public boolean isPlayerNear;
    // Boolean Var indicates if player is in trade UI or not
    public boolean isPlayerTrading;
    // The Trade UI with trades
    private final NPCSeller seller;
    private final Label Market_Msg;
    // Keyboard Controls from user selection
    private final MainMenuControls controls;
    // The current overlay manager containing all UI elements
    private final OverlayManager overlayManager;
    // Current Stage
    private final Stage stage;

    /**
     * This constructor obtains all required values for inherited class and this class to block the building on map as well for interactions.
     * This constructor sets defaults values for boolean variables isPlayerNear and isPlayerTrading to false which later are changed from a handler of some sort.
     *
     * @param building_Width  The width of this building.
     * @param building_Height The height of this building.
     * @param loc_X           The zero coordinate of the building in terms of x-axis.
     * @param loc_Y           The zero coordinate of the building in terms of y-axis.
     * @param building        The Texture of this building for drawing purposes.
     * @param batch           A Sprite Batch which is mainly used for drawing the building texture.
     *                        *** Needs to be different from the stage else will cause issues with listeners. ***
     * @param stage           The main stage where all UI elements takes place.
     * @param seller          A NPC Seller class that contains all UI elements as well as trades and offerings. This class is located under ScreenOverlayRework.
     *                        This object has tradings that will only sell to the player.
     * @param controls        Controls class which the user choose in setting screen. This class is located in "Keyboard_Mouse_Controls" directory.
     * @param overlayManager  A manger which controls all UI elements with Trading, Inventory, and Chests.
     * @param labelStyle      A general Label style which is used to display any messages with the use of a label.
     */
    public MarketBulding(float building_Width, float building_Height, float loc_X, float loc_Y, Texture building, SpriteBatch batch, Stage stage, NPCSeller seller, MainMenuControls controls, OverlayManager overlayManager, Label.LabelStyle labelStyle) {
        super(building_Width, building_Height, loc_X, loc_Y, building, batch);
        // Defines Message Label and Position
        this.seller = seller;
        Market_Msg = new Label("Press " + Input.Keys.toString(controls.getTradeMenu()) + " To See Trades", labelStyle);
        Market_Msg.setPosition(Building_Rect.x + Building_Rect.width / 3, Building_Rect.y + Building_Rect.height / 4);
        stage.addActor(Market_Msg);
        Market_Msg.setZIndex(0);
        isPlayerNear = false;
        isPlayerTrading = false;
        this.controls = controls;
        this.overlayManager = overlayManager;
        this.stage = stage;
    }

    /**
     * @Functionality The purpose of this method is that it called every iteration of the game for drawing Textures.
     * As well as, checking if the player interacted with this building in any way.
     */
    @Override
    public void render() {
        // Draws Building based on given Texture and Cords
        drawBuilding();
        // Setting Message Visibility
        Market_Msg.setVisible(isPlayerNear && !isPlayerTrading);
        // Displaying Trade UI if player is near building
        Trade();
    }

    /**
     * @Functionality This method checks if the player is near the building using the "isPlayerNear" flag, if so, it will wait for key input and compares it with
     * interaction keycode from MainMenuControls class. In case both keys match it will "NOT ~" the current visibility state of Trading system.
     * This method also handles cases where user leaves building area or opens another system where trading will be affected.
     */
    private void Trade() {
        // Checks if the player is near the building
        if (isPlayerNear) {
            // Checks if the player's input matches with the one from MainMenuControls class
            if (Gdx.input.isKeyJustPressed(controls.getTradeMenu())) {
                // NOTs '~' the current visibility of this system.
                isPlayerTrading = !isPlayerTrading;
                // Making sure all other systems that would affect trading are off
                isInventoryVisible = false;
                overlayManager.setHiddenTableVisibility(false);
                // Setting the current system to display based on system's visibility

                overlayManager.NPCSellerInventory(isPlayerTrading, seller);
            }
        } // If the player is not near this class, it will turn off all interactions.
        else {
            isPlayerTrading = false;
            overlayManager.NPCSellerInventory(isPlayerTrading, seller);
        }
        // If the player opens Inventory, this will turn everything related to this class off.
        if (Gdx.input.isKeyJustPressed(controls.getOpenInventory())) {
            isPlayerTrading = false;
            overlayManager.NPCSellerInventory(false, seller);
        }
    }

    /**
     * @Functionality Disposes all things such as textures which are not managed by other classes.
     */
    @Override
    public void dispose() {

    }
}
