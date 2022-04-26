package ScreenOverlayRework.Inventory;

import ScreenOverlayRework.Inventory.ItemUI.GeneralItemUI;
import ScreenOverlayRework.Inventory.ItemUI.MainItemsUI;
import ScreenOverlayRework.Inventory.ItemUI.QuestUI;
import ScreenOverlayRework.Inventory.ItemUI.SkillUI;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCBuyer;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCSeller;
import ScreenOverlayRework.Inventory.NPCInventoryUI.TownHallQuestBoard;
import ScreenOverlayRework.Inventory.TreasureChest.TreasureChestUI;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class InventoryUI implements Disposable {
    private BladeAndTomes game;
    private Table MainInventoryTable;
    private Table HiddenInventoryTable;
    private Table HiddenInventorySlots;
    private Table HiddenQuests;
    private Table HiddenSkill;
    private Table table;
    private Array<itemSlot> slots;
    private DragAndDrop dnd;
    private Stack stack;

    private AssetManager itemManager;
    private AssetManager mainItemsUIManager;
    public MainItemsUI main_Inventory;

    private GeneralItemUI generalItemUI;
    private QuestUI questUI;
    private SkillUI skillUIs;


    TextButton InventoryButton;
    TextButton QuestButton;
    TextButton SkillButton;
    TextButton Gold;
    public InventoryUI(BladeAndTomes GAME,DragAndDrop dnd) {
        game = GAME;
        // Tables used to draw slots
        MainInventoryTable = new Table();
        HiddenInventoryTable = new Table();
        HiddenInventorySlots = new Table();
        HiddenQuests = new Table();
        HiddenSkill = new Table();

        // Main Table
        table = new Table();

        this.dnd = dnd;
        slots = new Array<>();
        stack = new Stack();


        // Making Main Inventory UI
        mainItemsUIManager =game.assets;
        main_Inventory = new MainItemsUI(game,dnd,mainItemsUIManager,slots);
        table.addActor(main_Inventory.getTable());

        skillUIs = new SkillUI(game,mainItemsUIManager);


        // Making General Item UI
        generalItemUI = new GeneralItemUI(game,dnd,mainItemsUIManager,slots);
        HiddenInventorySlots = generalItemUI.getTable();

        // Making Hidden Tabs Buttons
        InventoryButton = new TextButton("Inventory", game.generalTextButtonStyle);
        QuestButton = new TextButton("Quest", game.generalTextButtonStyle);
        SkillButton = new TextButton("Skill", game.generalTextButtonStyle);

        itemManager = game.assets;
        if(!itemManager.isLoaded("InventoryItems/Other/StoneBaseLabel/StoneBase.json",Skin.class)){
        itemManager.load("InventoryItems/Other/StoneBaseLabel/StoneBase.json",Skin.class, (new SkinLoader.SkinParameter("InventoryItems/Other/StoneBaseLabel/StoneBase.atlas")));
        itemManager.finishLoading();}
        questUI = new QuestUI(game,mainItemsUIManager);

        makeHiddenInventory();
        table.addActor(HiddenInventoryTable);

        Gold = new TextButton("Gold: "+String.valueOf(game.player.getGold()),game.inventoryTextButtonStyle);
        Gold.setPosition( 775,750);
        Gold.setSize(125,50);
        Gold.getStyle().font.getData().setScale(0.7f);
        table.addActor(Gold);

    }


    /**
     *  TODO THIS METHOD HANDLES ALL MAKING OF HIDDEN INVENTORY COMPONENTS
      */
    public void makeHiddenInventory() {

        // Specifying Hidden Inventory's Location
        HiddenInventoryTable.setBackground(game.BaseLabelStyle2.background);
        HiddenInventoryTable.defaults();
        HiddenInventoryTable.setBounds(game.stageInstance.getWidth() * 0.4f, game.stageInstance.getHeight() * 0.2f,
                game.stageInstance.getWidth() * 0.3f, game.stageInstance.getHeight() * 0.57f);
        HiddenInventoryTable.top();

        // Creating The three options "Inventory , Quests and Skills" as Buttons
        // Giving those Three Listener to clear others when clicked
        InventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                HiddenInventorySlots.setVisible(true);
                HiddenQuests.setVisible(false);
                HiddenSkill.setVisible(false);

            }
        });
        QuestButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                HiddenInventorySlots.setVisible(false);
                HiddenQuests.setVisible(true);
                HiddenSkill.setVisible(false);
                drawQuests();

            }
        });
        SkillButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                HiddenInventorySlots.setVisible(false);
                HiddenQuests.setVisible(false);
                HiddenSkill.setVisible(true);
            }
        });

        // Adding three tabs to table and defining their locations
        Table optionTabs = new Table();
        optionTabs.top();
        optionTabs.add(InventoryButton).size(100, 100);
        optionTabs.add(QuestButton).size(100, 100);
        optionTabs.add(SkillButton).size(100, 100);
        HiddenInventoryTable.add(optionTabs);
        HiddenInventoryTable.row();

        // Drawing Hidden Inventory Slots
        HiddenInventorySlots.defaults();
        stack.add(HiddenInventorySlots);
        HiddenInventorySlots.setVisible(true);

        // HiddenQuests Inventory
        HiddenQuests.defaults();
        stack.add(HiddenQuests);
        HiddenQuests.setVisible(false);

        // HiddenSkill Inventory
        HiddenSkill.defaults();
        stack.add(HiddenSkill);
        HiddenSkill.setVisible(false);

        stack.setBounds(25,25,500,490);
        HiddenInventoryTable.addActor(stack);
        drawSkills();

    }

    /**
     * THIS MAKE A NEW INSTANCE OF TOWN HALL QUEST BOARD CLASS WITH LINKING IT TO THIS CLASS
     * @return NEW QUEST BOARD
     */
    public TownHallQuestBoard makeQuestBoard(){
        TownHallQuestBoard Board = new TownHallQuestBoard(game,mainItemsUIManager, questUI);
        return Board;
    }
    /**
     * THIS MAKE A NEW INSTANCE OF TREASURE CHEST CLASS WITH LINKING IT TO THIS CLASS
     * @return NEW TREASURE CHEST
     */
    public TreasureChestUI makeTreasureChest(){
        TreasureChestUI chestUI = new TreasureChestUI(game,dnd,mainItemsUIManager,slots);
        return chestUI;
    }
    /**
     * THIS MAKE A NEW INSTANCE OF NPC SELLER CLASS WITH LINKING IT TO THIS CLASS
     * @return NEW SELLER
     */
    public NPCSeller makeNPCSeller(){
        NPCSeller npc = new NPCSeller(game,dnd,mainItemsUIManager,slots);
        return npc;
    }
    /**
     * THIS MAKE A NEW INSTANCE OF NPCBUYER CLASS WITH LINKING IT TO THIS CLASS
     * @return NEW BUYER
     */
    public NPCBuyer makeNPCBuyer(){
        NPCBuyer npc=new NPCBuyer(game,dnd,mainItemsUIManager,slots);
        npc.getTable().setVisible(false);
        return npc;
    }

    /**
     * THIS METHOD IS RESPONSIBLE FOR DISPLAYING GIVEN CHEST ONTO THE SCREEN
     * THIS SETS ALL OTHER UI ELEMENTS OFF.
     * @param chestUI GIVEN CHEST
     */
    public void displayChest(TreasureChestUI chestUI){
        // This removes everything on screen if visible on adds chest ui
        if(chestUI.isTreasureChestVisible()){
            table.addActor(chestUI.getTable());
            HiddenInventorySlots.setVisible(true);
            HiddenQuests.setVisible(false);
            HiddenSkill.setVisible(false);
            HiddenInventoryTable.setVisible(false);
            chestUI.addActorStack(HiddenInventorySlots,200,240);
            chestUI.getTable().setVisible(true);
        }
        else{
            table.removeActor(chestUI.getTable());
            chestUI.getTable().setVisible(false);
            chestUI.getTable().removeActor(HiddenInventorySlots);
        }
    }

    /**
     * THIS METHOD IS RESPONSIBLE FOR DISPLAYING GIVEN QuestBoard ONTO THE SCREEN
     * THIS SETS ALL OTHER UI ELEMENTS OFF.
     * @param board GIVEN QUEST BOARD UI
     * @parm display visibility
     */
    public void displayQuestBoardTradeUI(boolean display, TownHallQuestBoard board){
        // This removes everything on screen if visible on adds quest board ui
        if(display){
            table.addActor(board.getTable());
            HiddenInventorySlots.setVisible(true);
            HiddenQuests.setVisible(false);
            HiddenSkill.setVisible(false);
            HiddenInventoryTable.setVisible(false);
            board.addActorStack(questUI.getTable(),-75,-10);
            board.getTable().setVisible(true);
        }
        else{
            table.removeActor(board.getTable());
            board.getTable().setVisible(false);
            board.getTable().removeActor(questUI.getTable());
        }
    }
    /**
     * THIS METHOD IS RESPONSIBLE FOR DISPLAYING GIVEN BUYER ONTO THE SCREEN
     * THIS SETS ALL OTHER UI ELEMENTS OFF.
     * @param npc GIVEN BUYER UI
     * @parm display visibility
     */
    public void Trade_Inventory_NPCBuyer(boolean display, NPCBuyer npc){
        // This removes everything on screen if visible on adds buyer ui
        if(display){
            table.addActor(npc.getTable());
            HiddenInventorySlots.setVisible(true);
            HiddenQuests.setVisible(false);
            HiddenSkill.setVisible(false);
            HiddenInventoryTable.setVisible(false);
            npc.addActorStack(HiddenInventorySlots,200,240);
            npc.getTable().setVisible(true);
        }
        else{
            table.removeActor(npc.getTable());
            npc.getTable().setVisible(false);
            npc.getTable().removeActor(HiddenInventorySlots);
        }
    }
    /**
     * THIS METHOD IS RESPONSIBLE FOR DISPLAYING GIVEN SELLER ONTO THE SCREEN
     * THIS SETS ALL OTHER UI ELEMENTS OFF.
     * @param npc GIVEN SELLER UI
     * @parm display visibility
     */
    public void Trade_Inventory_NPCSeller(boolean display, NPCSeller npc){
        if(display){
            table.addActor(npc.getTable());
            HiddenInventorySlots.setVisible(true);
            HiddenQuests.setVisible(false);
            HiddenSkill.setVisible(false);
            HiddenInventoryTable.setVisible(false);
            npc.addActorStack(HiddenInventorySlots,200,240);
            npc.getTable().setVisible(true);
        }
        else{
            table.removeActor(npc.getTable());
            npc.getTable().setVisible(false);
            npc.getTable().removeActor(HiddenInventorySlots);
        }
    }

    /**
     * THIS IS USED FOR DRAWING AVAILABLE QUESTS IN INVENTORY UI
     */
    public void drawQuests() {

        HiddenQuests.addActor(questUI.getTable());
    }


    /**
     * THIS IS USED FOR DRAWING SKILLS IN INVENTORY UI
     */
    public void drawSkills() {
        HiddenSkill.add(skillUIs.getTable());
    }

    /**
     * THIS IS A CONTINUES METHOD WHERE IT UPDATES VARIABLES OVER TIME
     */
    public void render(){
        // UPDATING QUEST BOARD ELEMENTS
        questUI.render();
        // UPDATING SKILL ELEMENTS
        skillUIs.render();
        // UPDATING GOLD IN INVENTORY
        Gold.setText("Gold: "+String.valueOf(game.player.getGold()));
    }

    /**
     * THIS METHOD IS USED TP TOGGLE THE VISIBLY OF HIDDEN INVENTORY
     * @param val TRUE TO DISPLAY THE INVENTORY OR FALSE TO TAKE IT OUT
     */
    public void setHiddenInventoryVisibility(boolean val){
        // This removes everything on screen if visible on adds Inventory ui

        if(val){
            HiddenInventorySlots.setVisible(true);
            HiddenQuests.setVisible(false);
            HiddenSkill.setVisible(false);
            stack.add(HiddenInventorySlots);
            drawQuests();
            HiddenInventoryTable.setVisible(true);
            Gold.setVisible(true);
        }
        else{
            HiddenInventoryTable.setVisible(false);
            Gold.setVisible(false);
        }
    }

    /**
     * @return THIS RETURNS A TABLE WHERE ALL HIDDEN INVENTORY ELEMENTS ARE DRAWN
     */
    public Table getUI(){
        return table;
    }
    @Override
    public void dispose() {

    }
}
