package ScreenOverlayRework.Inventory;

import ScreenOverlayRework.Inventory.ItemUI.GeneralItemUI;
import ScreenOverlayRework.Inventory.ItemUI.MainItemsUI;
import ScreenOverlayRework.Inventory.ItemUI.QuestUI;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCBuyer;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCSeller;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import jdk.tools.jmod.Main;

import static com.badlogic.gdx.utils.Align.*;

public class InventoryUI implements Cloneable{
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

    private AssetManager mainItemsUIManager;
    private MainItemsUI main_Inventory;

    private AssetManager generalItemsUIManager;
    private GeneralItemUI generalItemUI;

    private AssetManager NPCSellerManager;
    private ScreenOverlayRework.Inventory.NPCInventoryUI.NPCSeller NPCSeller;

    private AssetManager NPCBuyerManager;
    private ScreenOverlayRework.Inventory.NPCInventoryUI.NPCBuyer NPCBuyer;

    private AssetManager questManager;
    private QuestUI questUI;


    TextButton InventoryButton;
    TextButton QuestButton;
    TextButton SkillButton;
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
//        game.stageInstance.addActor(table);


        this.dnd = dnd;
        slots = new Array<>();
        stack = new Stack();


        // Making Main Inventory UI
        mainItemsUIManager =new AssetManager();
        main_Inventory = new MainItemsUI(game,dnd,mainItemsUIManager,slots);
        table.addActor(main_Inventory.getTable());


        // Making General Item UI
        generalItemsUIManager =new AssetManager();
        generalItemUI = new GeneralItemUI(game,dnd,mainItemsUIManager,slots);
        HiddenInventorySlots = generalItemUI.getTable();

        // Making Hidden Tabs Buttons
        InventoryButton = new TextButton("Inventory", game.generalTextButtonStyle);
        QuestButton = new TextButton("Quest", game.generalTextButtonStyle);
        SkillButton = new TextButton("Skill", game.generalTextButtonStyle);
        makeHiddenInventory();


        // Making NPC Seller
        NPCSellerManager = new AssetManager();
        NPCSeller = new NPCSeller(game,dnd,mainItemsUIManager,slots);
        // Adding inventory tables

        NPCBuyerManager = new AssetManager();
        NPCBuyer = new NPCBuyer(game,dnd,mainItemsUIManager,slots);

        questManager = new AssetManager();
        questUI = new QuestUI(game,questManager);


        table.addActor(NPCBuyer.getTable());
        table.addActor(NPCSeller.getTable());
        table.addActor(HiddenInventoryTable);

        HiddenQuests.add(questUI.getTable());



    }


    // TODO THIS METHOD HANDLES ALL HIDEN INVENTORY COMPONENTS
    public void makeHiddenInventory() {
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
                HiddenSkill.setVisible(false);
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
        drawQuests();

        // HiddenSkill Inventory
        HiddenSkill.setDebug(true);
        HiddenSkill.defaults();
        stack.add(HiddenSkill);
        HiddenSkill.setVisible(false);

        stack.setBounds(25,25,500,500);
        HiddenInventoryTable.addActor(stack);
        drawSkills();

    }

    public void drawQuests() {

    }

    public void drawSkills() {
    }

    public void changeNPCBuyer(boolean val){

    }
    public Table changeNPCSeller(boolean val){
        return HiddenInventorySlots;
    }

    int counter = 0;
    public void setHiddenInventoryVisibility(boolean val){
        if (counter == 3)
            counter = 0;
        if(counter == 0){
            stack.add(HiddenInventorySlots);
            HiddenInventoryTable.setVisible(true);
            NPCBuyer.getTable().setVisible(false);
            NPCSeller.getTable().setVisible(false);
        } else if(counter == 1){
            NPCSeller.addActorStack(HiddenInventorySlots,200,240);
            NPCSeller.getTable().setVisible(true);
            HiddenInventoryTable.setVisible(false);
            NPCBuyer.getTable().setVisible(false);
        } else if(counter == 2){
            NPCBuyer.addActorStack(HiddenInventorySlots,200,240);
            NPCBuyer.getTable().setVisible(true);
            NPCSeller.getTable().setVisible(false);
            HiddenInventoryTable.setVisible(false);
        }
        counter++;


//        if(val){
////            NPCSeller.removeActorStack(HiddenInventorySlots);
//            stack.add(HiddenInventorySlots);
//        }
//        else{
//            stack.removeActor(HiddenInventorySlots);
//            HiddenInventoryTable.setVisible(val);
//            HiddenInventorySlots.setSize(10,75);
//
//
//        }
//        NPCSeller.getTable().setVisible(true);

    }
    public void removeListens(){
        for(int i = 0;i<slots.size;++i){
            dnd.removeSource((slots.get(i).getSourceLister()));
            dnd.removeTarget(slots.get(i).getTargetLister());
        }
    }

    public Table getUI(){
        return table;
    }

}
