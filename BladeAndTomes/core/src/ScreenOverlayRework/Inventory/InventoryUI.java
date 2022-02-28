package ScreenOverlayRework.Inventory;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
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
    private Label quest1;
    private Label quest2;
    private Label quest3;
    private Label quest4;
    private ProgressBar questProgress;
    private ProgressBar quest2Progress;
    private ProgressBar quest3Progress;
    private ProgressBar quest4Progress;
    private TextureAtlas questProgressAtlas;
    private Skin questProgressSkin;
    private Label tokenLabel;
    private TextButton physicalButton;
    private TextButton mentalButton;
    private TextButton socialButton;
    private TextButton acroButton;
    private TextButton bruteButton;
    private TextButton bartButton;
    private TextButton speechButton;
    private TextButton awareButton;
    private TextButton intuButton;
    private Table table;
    private Array<itemSlot> slots;
    private DragAndDrop dnd;

    public InventoryUI(BladeAndTomes GAME,DragAndDrop dnd) {

        game = GAME;
        questProgressAtlas = new TextureAtlas(Gdx.files.internal("SkinAssets/ProgressBar/healthbarUI.atlas"));
        questProgressSkin = new Skin((Gdx.files.internal("SkinAssets/ProgressBar/healthbarUI.json")),questProgressAtlas);
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

        makeMainInventory();
        makeHiddenInventory();

        // Adding inventory tables
        table.addActor(MainInventoryTable);
        table.addActor(HiddenInventoryTable);

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
        HiddenInventoryTable.setBounds(game.stageInstance.getWidth() * 0.4f, game.stageInstance.getHeight() * 0.2f,
                game.stageInstance.getWidth() * 0.3f, game.stageInstance.getHeight() * 0.57f);
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

        Stack stack = new Stack();

        // Drawing Hidden Inventory Slots
        HiddenInventorySlots.setDebug(true);
        HiddenInventorySlots.defaults();
        stack.add(HiddenInventorySlots);
        HiddenInventorySlots.setVisible(true);
        drawHiddenInventorySlots();

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
        //Sets up 4 random quests each time the game is run
        BladeAndTomes.setQuests();

        BladeAndTomes.quest q1 = BladeAndTomes.usedQuests.get(0);
        String q1Goal = q1.getGoal();
        int q1Val = q1.getValue();
        q1.setUsed();
        quest1 = new Label(q1Goal, game.generalLabelStyle);
        BladeAndTomes.quest q2 = BladeAndTomes.usedQuests.get(1);
        String q2Goal = q2.getGoal();
        int q2Val = q2.getValue();
        q2.setUsed();
        quest2 = new Label(q2Goal, game.generalLabelStyle);
        BladeAndTomes.quest q3 = BladeAndTomes.usedQuests.get(2);
        String q3Goal = q3.getGoal();
        int q3Val = q3.getValue();
        quest3 = new Label(q3Goal, game.generalLabelStyle);
        BladeAndTomes.quest q4 = BladeAndTomes.usedQuests.get(3);
        String q4Goal = q4.getGoal();
        int q4Val = q4.getValue();
        quest4 = new Label(q4Goal, game.generalLabelStyle);
        questProgress = new ProgressBar(0,q1Val,1,false,questProgressSkin);
        quest2Progress = new ProgressBar(0,q2Val,1,false, questProgressSkin);
        quest3Progress = new ProgressBar(0,q3Val,1,false,questProgressSkin);
        quest4Progress = new ProgressBar(0,q4Val,1,false,questProgressSkin);

        HiddenQuests.center();
        HiddenQuests.add(quest1);
        HiddenQuests.add(quest2);
        HiddenQuests.row();
        HiddenQuests.add(questProgress);
        HiddenQuests.add(quest2Progress);
        HiddenQuests.row();
        HiddenQuests.add(quest3);
        HiddenQuests.add(quest4);
        HiddenQuests.row();
        HiddenQuests.add(quest3Progress);
        HiddenQuests.add(quest4Progress);
    }

    public void drawSkills() {
        tokenLabel = new Label("Tokens: "+game.getTokens(), game.BaseLabelStyle2);
        tokenLabel.setFontScale(2);
        physicalButton = new TextButton("Physical: "+game.player.getPhysical(), game.generalTextButtonStyle);
        physicalButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if (game.getTokens() > 0) {
                    game.player.setPhysical(game.player.getPhysical()+1);
                    game.setTokens(game.getTokens()-1);
                    physicalButton.setText("Physical: "+game.player.getPhysical());
                    tokenLabel.setText("Tokens: "+game.getTokens());
                }
            }
        });
        mentalButton = new TextButton("Mental: "+game.player.getMental(), game.generalTextButtonStyle);
        mentalButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if(game.getTokens() > 0) {
                    game.player.setMental(game.player.getMental() + 1);
                    game.setTokens(game.getTokens() - 1);
                    mentalButton.setText("Mental: "+game.player.getMental());
                    tokenLabel.setText("Tokens: "+game.getTokens());
                }
            }
        });
        socialButton = new TextButton("Social: "+game.player.getSocial(), game.generalTextButtonStyle);
        socialButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if(game.getTokens() > 0) {
                    game.player.setSocial(game.player.getSocial() + 1);
                    game.setTokens(game.getTokens() - 1);
                    socialButton.setText("Social: "+game.player.getSocial());
                    tokenLabel.setText("Tokens: "+game.getTokens());
                }
            }
        });
        acroButton = new TextButton("Acrobatics: "+game.player.getAcrobatics(), game.generalTextButtonStyle);
        acroButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if(game.getTokens() > 0) {
                    game.player.setAcrobatics(game.player.getAcrobatics() + 1);
                    game.setTokens(game.getTokens() - 1);
                    acroButton.setText("Acrobatics: "+game.player.getAcrobatics());
                    tokenLabel.setText("Tokens: "+game.getTokens());
                }
            }
        });
        bruteButton = new TextButton("Brute Force: "+game.player.getBruteforce(), game.generalTextButtonStyle);
        bruteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if(game.getTokens() > 0) {
                    game.player.setBruteforce(game.player.getBruteforce() + 1);
                    game.setTokens(game.getTokens() - 1);
                    bruteButton.setText("Brute Force: "+game.player.getBruteforce());
                    tokenLabel.setText("Tokens: "+game.getTokens());
                }
            }
        });
        bartButton = new TextButton("Bartering: "+game.player.getBruteforce(), game.generalTextButtonStyle);
        bartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if(game.getTokens() > 0) {
                    game.player.setBarter(game.player.getBarter() + 1);
                    game.setTokens(game.getTokens() - 1);
                    bartButton.setText("Bartering: "+game.player.getBarter());
                    tokenLabel.setText("Tokens: "+game.getTokens());
                }
            }
        });
        speechButton = new TextButton("Speech: "+game.player.getSpeech(), game.generalTextButtonStyle);
        speechButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if(game.getTokens() > 0) {
                    game.player.setSpeech(game.player.getSpeech() + 1);
                    game.setTokens(game.getTokens() - 1);
                    speechButton.setText("Speech: "+game.player.getSpeech());
                    tokenLabel.setText("Tokens: "+game.getTokens());
                }
            }
        });
        awareButton = new TextButton("Awareness: "+game.player.getAwareness(), game.generalTextButtonStyle);
        awareButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if(game.getTokens() > 0) {
                    game.player.setAwareness(game.player.getAwareness() + 1);
                    game.setTokens(game.getTokens() - 1);
                    awareButton.setText("Awareness: "+game.player.getAwareness());
                    tokenLabel.setText("Tokens: "+game.getTokens());
                }
            }
        });
        intuButton = new TextButton("Intuition: "+game.player.getIntuition(), game.generalTextButtonStyle);
        intuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                if(game.getTokens() > 0) {
                    game.player.setIntuition(game.player.getIntuition() + 1);
                    game.setTokens(game.getTokens() - 1);
                    intuButton.setText("Intuition: "+game.player.getIntuition());
                    tokenLabel.setText("Tokens: "+game.getTokens());
                }
            }
        });

        HiddenSkill.add(tokenLabel).colspan(3);
        HiddenSkill.row();
        HiddenSkill.add(physicalButton);
        HiddenSkill.add(mentalButton);
        HiddenSkill.add(socialButton);
        HiddenSkill.row();
        HiddenSkill.add(acroButton);
        HiddenSkill.add(bruteButton);
        HiddenSkill.add(bartButton);
        HiddenSkill.row();
        HiddenSkill.add(speechButton);
        HiddenSkill.add(awareButton);
        HiddenSkill.add(intuButton);

    }
    // TODO THIS METHOD DRAWS SLOTS
    public void drawHiddenInventorySlots() {

        // Drawing Hidden General Inventory Slots
        Table generalItems = new Table();
        generalItems.add(new Label("\tGeneral Items", game.generalLabelStyle)).size(150, 50).colspan(3);
        for (int i = 5; i < 14; ++i) {

            itemSlot temp = new itemSlot(game, dnd, i, "Any");
            temp.applySource();
            temp.applyTarget();
            dnd = temp.getDND();
            slots.add(temp);
            if ((i+1) % 3 == 0) {
                generalItems.row();
            }
            generalItems.add(slots.get(i).getSlot()).size(100, 100);
        }
        HiddenInventorySlots.add(generalItems).colspan(6);
        HiddenInventorySlots.row();

        // Drawing Hidden Sell Item Inventory Slots

        Table SpellItems = new Table();
        SpellItems.align(top | center);
        SpellItems.add(new Label("\tSpell Items", game.generalLabelStyle)).size(125, 50).colspan(3);
        for (int i = 14; i < 16; ++i) {

            itemSlot temp = new itemSlot(game, dnd, i, "Spell");
            temp.applySource();
            temp.applyTarget();
            dnd = temp.getDND();
            slots.add(temp);
            if ((i+1) % 3 == 0) {
                SpellItems.row();
            }
            SpellItems.add(slots.get(i).getSlot()).size(100, 100);
        }
        HiddenInventorySlots.add(SpellItems).top();

        // Drawing Hidden Armor Inventory Slots

        Table ArmorItems = new Table();
        ArmorItems.add(new Label("\tArmor Equipped", game.generalLabelStyle)).size(125, 50);
        ArmorItems.row();

        for (int i = 16; i < 17; ++i) {


            itemSlot temp = new itemSlot(game, dnd, i, "Armor");
            temp.applySource();
            temp.applyTarget();
            dnd = temp.getDND();
            slots.add(temp);
            if ((i+1) % 3 == 0)
                SpellItems.row();
            ArmorItems.add(slots.get(i).getSlot()).size(100, 100);
        }
        HiddenInventorySlots.add(ArmorItems).colspan(10);

    }
    public void setHiddenInventoryVisibility(boolean val){
        HiddenInventoryTable.setVisible(val);
    }
    public boolean getHiddenInventoryVisibility(){
        return HiddenInventoryTable.isVisible();
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
