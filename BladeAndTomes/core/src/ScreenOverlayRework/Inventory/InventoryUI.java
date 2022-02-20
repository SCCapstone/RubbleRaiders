package ScreenOverlayRework.Inventory;

import ScreenOverlay.HiddenInventory;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

import java.util.ArrayList;

import static com.badlogic.gdx.utils.Align.*;

public class InventoryUI {
    private BladeAndTomes game;
    private Table MainInventoryTable;
    private Table HiddenInventoryTable;
    private Table HiddenInventorySlots;
    private Table HiddenQuests;
    private Table HiddenSkill;


    private Array<itemSlot> slots;
    private DragAndDrop dnd;

    public InventoryUI(BladeAndTomes GAME) {

        game = GAME;


        // Tables used to draw slots
        MainInventoryTable = new Table();
        HiddenInventoryTable = new Table();
        HiddenInventorySlots = new Table();
        HiddenQuests = new Table();
        HiddenSkill = new Table();

        dnd = new DragAndDrop();
        slots = new Array<>();

        makeMainInventory();
        makeHiddenInventory();

        // Adding inventory tables
        game.stageInstance.addActor(MainInventoryTable);
        game.stageInstance.addActor(HiddenInventoryTable);

    }

    // TODO THIS METHOD DRAWS MAIN INVENTORY LACTATED TOP LEFT
    public void makeMainInventory() {
        // Specifying Main Inventory Location Top Left
        MainInventoryTable.setDebug(true);
        MainInventoryTable.defaults();
        MainInventoryTable.setBounds(0, 0, game.stageInstance.getWidth(), game.stageInstance.getHeight());
        MainInventoryTable.align(topLeft).padLeft(10);

        // Drawing those Slots on screen
        for (int i = 0; i < 5; ++i) {
            itemSlot temp = new itemSlot(game, dnd, i, "Any");
            temp.applySource();
            temp.applyTarget();
            slots.add(temp);
            MainInventoryTable.add(slots.get(i).getSlot()).size(100, 100);
        }
    }

    // TODO THIS METHOD HANDLES ALL HIDEN INVENTORY COMPONENTS
    public void makeHiddenInventory() {
        // Specifying Hidden Inventory's Location
        HiddenInventoryTable.setBackground(game.BaseLabelStyle2.background);
        HiddenInventoryTable.setDebug(true);
        HiddenInventoryTable.defaults();
        HiddenInventoryTable.setBounds(game.stageInstance.getWidth() * 0.4f, game.stageInstance.getHeight() * 0.2f, game.stageInstance.getWidth() * 0.3f, game.stageInstance.getHeight() * 0.57f);
        HiddenInventoryTable.top();

        // Creating The three options "Inventory , Quests and Skills" as Buttons
        // Giving those Three Listener to clear others when clicked

        TextButton InventoryButton = new TextButton("Inventory", game.generalTextButtonStyle);
        InventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                HiddenInventorySlots.setVisible(true);
                HiddenQuests.setVisible(false);
                HiddenSkill.setVisible(false);
            }
        });

        TextButton QuestButton = new TextButton("Quest", game.generalTextButtonStyle);
        QuestButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                HiddenInventorySlots.setVisible(false);
                HiddenQuests.setVisible(true);
                HiddenSkill.setVisible(false);
            }
        });

        TextButton SkillButton = new TextButton("Skill", game.generalTextButtonStyle);
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
        HiddenInventoryTable.add(HiddenInventorySlots);
        HiddenInventorySlots.setVisible(true);
        drawHiddenInventorySlots();

        // HiddenQuests Inventory
        HiddenQuests.setDebug(true);
        HiddenQuests.defaults();
        HiddenInventoryTable.add(HiddenQuests);
        HiddenQuests.setVisible(false);
        drawQuests();

        // HiddenSkill Inventory
        HiddenSkill.setDebug(true);
        HiddenSkill.defaults();
        HiddenInventoryTable.add(HiddenSkill);
        HiddenSkill.setVisible(false);
        drawSkills();

    }

    public void drawQuests() {

    }

    public void drawSkills() {

    }
    // TODO THIS METHOD DRAWS SLOTS
    public void drawHiddenInventorySlots() {

        // Drawing Hidden General Inventory Slots
        Table generalItems = new Table();
        generalItems.add(new Label("\tGeneral Items", game.generalLabelStyle)).size(150, 50).colspan(3);
        for (int i = 6; i < 15; ++i) {
            if (i % 3 == 0) {
                generalItems.row();
            }
            itemSlot temp = new itemSlot(game, dnd, i, "Any");
            temp.applySource();
            temp.applyTarget();
            dnd = temp.getDND();
            slots.add(temp);
            generalItems.add(slots.get(i - 1).getSlot()).size(100, 100);
        }
        HiddenInventorySlots.add(generalItems).colspan(6);
        HiddenInventorySlots.row();

        // Drawing Hidden Sell Item Inventory Slots

        Table SpellItems = new Table();
        SpellItems.align(top | center);
        SpellItems.add(new Label("\tSpell Items", game.generalLabelStyle)).size(125, 50).colspan(3);
        for (int i = 15; i < 17; ++i) {
            if (i % 3 == 0) {
                SpellItems.row();
            }
            itemSlot temp = new itemSlot(game, dnd, i, "Spell");
            temp.applySource();
            temp.applyTarget();
            dnd = temp.getDND();
            slots.add(temp);
            SpellItems.add(slots.get(i - 1).getSlot()).size(100, 100);
        }
        HiddenInventorySlots.add(SpellItems).top();

        // Drawing Hidden Armor Inventory Slots

        Table ArmorItems = new Table();
        ArmorItems.add(new Label("\tArmor Equipped", game.generalLabelStyle)).size(125, 50);
        ArmorItems.row();

        for (int i = 17; i < 18; ++i) {
            if (i % 3 == 0)
                SpellItems.row();

            itemSlot temp = new itemSlot(game, dnd, i, "Armor");
            temp.applySource();
            temp.applyTarget();
            dnd = temp.getDND();
            slots.add(temp);
            ArmorItems.add(slots.get(i - 1).getSlot()).size(100, 100);
        }
        HiddenInventorySlots.add(ArmorItems).colspan(10);

    }

}
