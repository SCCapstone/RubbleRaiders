package ScreenOverlayRework.Inventory;

import ScreenOverlayRework.Inventory.ItemUI.GeneralItemUI;
import ScreenOverlayRework.Inventory.ItemUI.MainItemsUI;
import ScreenOverlayRework.Inventory.ItemUI.QuestUI;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCBuyer;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCSeller;
import ScreenOverlayRework.Inventory.NPCInventoryUI.TownHallQuestBoard;
import ScreenOverlayRework.Inventory.TreasureChest.TreasureChestUI;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
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
    private MainItemsUI main_Inventory;

    private GeneralItemUI generalItemUI;
    private QuestUI questUI;


    TextButton InventoryButton;
    TextButton QuestButton;
    TextButton SkillButton;
    TownHallQuestBoard questBoard;
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


    }



    // TODO THIS METHOD HANDLES ALL HIDEN INVENTORY COMPONENTS
    public void makeHiddenInventory() {
        Skin skin = itemManager.get("InventoryItems/Other/StoneBaseLabel/StoneBase.json");
        Label label = new Label("",skin);

        // Specifying Hidden Inventory's Location
        HiddenInventoryTable.setBackground(game.BaseLabelStyle2.background);
        HiddenInventoryTable.setDebug(true);
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
                HiddenQuests.setDebug(true);
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
        HiddenInventorySlots.setDebug(true);
        HiddenInventorySlots.defaults();
        stack.add(HiddenInventorySlots);
        HiddenInventorySlots.setVisible(true);

        // HiddenQuests Inventory
        HiddenQuests.setDebug(true);
        HiddenQuests.defaults();
        stack.add(HiddenQuests);
        HiddenQuests.setVisible(false);

        // HiddenSkill Inventory
        HiddenSkill.setDebug(true);
        HiddenSkill.defaults();
        stack.add(HiddenSkill);
        HiddenSkill.setVisible(false);

        stack.setBounds(25,25,500,490);
        HiddenInventoryTable.addActor(stack);
        drawSkills();

    }
    public TownHallQuestBoard makeQuestBoard(){
        TownHallQuestBoard Board = new TownHallQuestBoard(game,mainItemsUIManager, questUI);
//        Board.addActorStack(questUI.getTable(),-75,-10);
//        table.addActor(Board.getTable());
        return Board;
    }
    public TreasureChestUI makeTreasureChest(){
        TreasureChestUI chestUI = new TreasureChestUI(game,dnd,mainItemsUIManager,slots);
        return chestUI;
    }
    public NPCSeller makeNPCSeller(){
        NPCSeller npc = new NPCSeller(game,dnd,mainItemsUIManager,slots);
        return npc;
    }
    public NPCBuyer makeNPCBuyer(){
        ScreenOverlayRework.Inventory.NPCInventoryUI.NPCBuyer npc=new NPCBuyer(game,dnd,mainItemsUIManager,slots);
        npc.getTable().setVisible(false);
        return npc;
    }
    public void displayChest(TreasureChestUI chestUI){
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

    public void displayQuestBoardTradeUI(boolean display, TownHallQuestBoard board){
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
    public void Trade_Inventory_NPCBuyer(boolean display, NPCBuyer npc){
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
    public void drawQuests() {
        HiddenQuests.addActor(questUI.getTable());
    }

    public void drawSkills() {
    }
    public void render(){
        questUI.render();
    }



    public void setHiddenInventoryVisibility(boolean val){

        if(val){
            HiddenInventorySlots.setVisible(true);
            HiddenQuests.setVisible(false);
            HiddenSkill.setVisible(false);
            stack.add(HiddenInventorySlots);
            drawQuests();
            HiddenInventoryTable.setVisible(true);
        }
        else{
            HiddenInventoryTable.setVisible(val);

        }
    }
    public void removeListens(){
        for(int i = 0;i<slots.size;++i){
            dnd.removeSource((slots.get(i).getSourceLister()));
            dnd.removeTarget(slots.get(i).getTargetLister());
        }
    }

    public void clearAssetManger(){
        for(String s: itemManager.getAssetNames())
            itemManager.unload(s);
        for(String s: mainItemsUIManager.getAssetNames())
            mainItemsUIManager.unload(s);

        itemManager.clear();
        mainItemsUIManager.clear();
    }

    public Table getUI(){
        return table;
    }

    @Override
    public void dispose() {
//        for(String s: itemManager.getAssetNames())
//            itemManager.unload(s);
//        for(String s: mainItemsUIManager.getAssetNames())
//            mainItemsUIManager.unload(s);
//        itemManager.dispose();
//        mainItemsUIManager.dispose();

    }
}
