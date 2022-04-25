package ScreenOverlayRework;


import ScreenOverlayRework.Health.Health;
import ScreenOverlayRework.Inventory.InventoryUI;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCBuyer;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCSeller;
import ScreenOverlayRework.Inventory.NPCInventoryUI.TownHallQuestBoard;
import ScreenOverlayRework.Inventory.TreasureChest.TreasureChestUI;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Disposable;

import static com.badlogic.gdx.utils.Align.*;

public class OverlayManager implements Disposable {
    // Universal Game class
    private final BladeAndTomes game;
    // A table where all inventory overlay are being drawn
    private final Table table;
    // Universal Drag and drop for item movement
    private final DragAndDrop dnd;
    // universal asset manager to handle all assets
    private final AssetManager manager;
    // A inventory UI class which manages ui elements
    public InventoryUI inventory;
    // Boolean Value needed by EntityUI to check hidden inventory visibility
    public boolean isHiddenInventoryVisible;
    // Testing counter
    int counter = 0;
    // The player health bar class
    private Health healthBar;

    /**
     * A constructor which initializes new values and sets universal to in current class.
     * This constructor makes an inventory ui and hides it until it is set to visible
     *
     * @param Game Universal game class which handles all variables for communication with other objects.
     */
    public OverlayManager(BladeAndTomes Game) {
        // Overall asset class where it is used to load all assets
        manager = Game.assets;
        // Universal game class Referred above
        game = Game;
        // Universal drag and drop class for item movement
        dnd = game.dnd;
        // table that holds all ui components with inventory
        table = new Table();
        // Setting table to default settings
        table.defaults();
        // Setting the table bounds to full screen
        table.setBounds(0, 0, Game.stageInstance.getWidth(), Game.stageInstance.getHeight());
        // Adding the table to stage as an actor
        game.stageInstance.addActor(table);
        // Hiding the hidden inventory until made visible
        isHiddenInventoryVisible = false;
        // When called it makes overlays
        makeOverlays();
    }

    /**
     * This method is responsible for making the calls to create all overlays
     */
    public void makeOverlays() {
        // Inventory
        inventory = new InventoryUI(game, dnd);
        // Setting alignment
        table.align(top | left).padLeft(10);
        // Adding inventory as actor to overlay table
        table.addActor(inventory.getUI());
        // Initially Hidden Table is not shown.
        setHiddenTableVisibility(game.showHiddenInventory);

        // Health Bar
        healthBar = new Health(game, manager, counter);
        // Setting alignment
        table.align(top | right).padLeft(10);
        // Adding healthBar as actor to overlay table
        table.addActor(healthBar.getHealthBar());
    }

    /** This method is responsible for hiding all overlays with inventory and health bar
     * @param value  values that indicate visibility of overlays: visible if true else not visible
     */
    public void setOverLayesVisibility(boolean value) {
        // Reading the table as actor to stage
        game.stageInstance.addActor(table);
        // Setting the visibility
        table.setVisible(value);

    }
    /**
     * This method is called to change the visibility of Buyer overlays.
     * @param val values that indicate visibility of npc: visible if true else not visible
     * @param npc the actual ui which is being changed
     */
    public void NPCBuyerInventory(boolean val, NPCBuyer npc) {
        // Calls inventory to change that npc's visibility
        inventory.Trade_Inventory_NPCBuyer(val, npc);
    }

    /**
     * This method is called to change the visibility of Seller overlays.
     * @param val values that indicate visibility of npc: visible if true else not visible
     * @param npc the actual ui which is being changed
     */
    public void NPCSellerInventory(boolean val, NPCSeller npc) {
        // Calls inventory to change that npc's visibility
        inventory.Trade_Inventory_NPCSeller(val, npc);

    }

    /**
     * This method is called to change the visibility of Quest-board.
     * @param val values that indicate visibility of quest board: visible if true else not visible
     * @param board the actual ui which is being changed
     */
    public void setQuestBoardTradeVisibility(boolean val, TownHallQuestBoard board) {
        // Calls inventory to change that ui visibility
        inventory.displayQuestBoardTradeUI(val, board);
    }

    /**
     * This method is called to change the visibility of HiddenInventory which has holds inventory, user quests and user skills
     * @param val values that indicate visibility of HiddenInventory: visible if true else not visible
     */
    public void setHiddenTableVisibility(boolean val) {
        // Setting the visible in inventory class
        game.showHiddenInventory = val;
        // Setting it in current class
        isHiddenInventoryVisible = val;
        // Applying the change
        inventory.setHiddenInventoryVisibility(val);
    }

    /**
     * This method is called to toggle the chests visibility
     * @param chest chest which is being toggled
     */
    public void displayChest(TreasureChestUI chest) {
        // Displaying the chest ui
        inventory.displayChest(chest);

    }

    /**
     * Generating a npc ui with their trades
     * @return buyer ui
     */
    public NPCBuyer generateNewNPCBuyer() {
        // returning the ui
        return inventory.makeNPCBuyer();
    }

    /**
     * Generating a npc ui with their trades
     * @return seller ui
     */
    public NPCSeller generateNewNPSeller() {
        // returning the ui
        return inventory.makeNPCSeller();
    }


    /**
     * Generating a quest board ui with the trades
     * @return  quest board ui with trades
     */
    public TownHallQuestBoard generateQuestBoard() {
        // returning the ui
        return inventory.makeQuestBoard();
    }

    /**
     * Generating a chest ui with new items
     * @return chest ui
     */
    public TreasureChestUI generateChest() {
        // returning the ui
        return inventory.makeTreasureChest();
    }


    /**
     * Updates health bar level
     */
    public void updateHealth() {
        // Updates healthBar Statues
        healthBar.update();
    }

    /**
     * This method is called every iteration of game and updates elements that are put in
     */
    public void render() {
        // Updates inventory
        inventory.render();
        // Updates helth bar status
        healthBar.update();
    }

    /**
     * Disposes everything with assets when this class goes outside scope
     * This is implemented to reduce memory usage
     */
    @Override
    public void dispose() {
        // Clears everything in table
        table.clear();
    }
}
