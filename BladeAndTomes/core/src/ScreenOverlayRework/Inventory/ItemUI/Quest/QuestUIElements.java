package ScreenOverlayRework.Inventory.ItemUI.Quest;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;

public class QuestUIElements implements Disposable {
    protected transient AssetManager manager;
    protected transient BladeAndTomes game;


    protected Table table;
    protected transient Texture questTextureBase;
    protected transient ProgressBar questProgressBar;
    protected transient TextureAtlas questBarAtlas;
    protected transient Skin questBarSkin;
    protected transient TextButton remove_complete_button;
    protected transient Label.LabelStyle questBaseLabelStyle;
    protected Table stack;

    protected int reward;
    protected String questDescription;
    protected boolean isCompleted;
    protected boolean questAllocated;

    protected transient Label noQuest;
    protected transient Label rewardLabel;
    protected Table baseTable;

    protected transient ProgressBar complitionBar;
    protected transient Skin skinComplitionBar;
    protected int posX, posY;
    protected transient Label baseBackGround;

    protected Stack progressBarStack;
    protected float questHeight;
    protected float questWidth;

    protected QuestDocument quest;
    protected String rewardType;
    protected int index;
    public QuestUIElements(BladeAndTomes game,AssetManager manager,QuestDocument quest ,int posX, int posY, int  index){
        this.index = index;
        this.game = game;
        this.manager = manager;
        this.posX = posX;
        this.posY = posY;
        this.reward = 30;
        this.isCompleted = false;
        this.questAllocated =false;
        this.questDescription = "";
        this.questHeight = 110f;
        this.questWidth = 400f;
        this.quest = quest;


        manager.load("InventoryItems/Other/QuestBaseLabel/QuestBaseLabel.json",Skin.class, new SkinLoader.SkinParameter("InventoryItems/Other/QuestBaseLabel/QuestBaseLabel.atlas"));
        manager.load("InventoryItems/Other/QuestProgressBar/ProgressBar.json",Skin.class,  new SkinLoader.SkinParameter("InventoryItems/Other/QuestProgressBar/ProgressBar.atlas"));
        manager.load("InventoryItems/Other/QuestTextButton/QuestButton.json",Skin.class,  new SkinLoader.SkinParameter("InventoryItems/Other/QuestTextButton/QuestButton.atlas"));

        manager.finishLoading();
        questBarSkin = manager.get("InventoryItems/Other/QuestBaseLabel/QuestBaseLabel.json");
        noQuest = new Label("NO QUEST EQUIPPED",game.BaseLabelStyle2);

        table = new Table();
        stack = new Table();
        if(quest != null){
        reward = quest.getRewardAmount();
        rewardType = quest.getsRewardGold()? " Gold":" Skill Tokens";
        questAllocated = true;
        rewardLabel= new Label("Reward: \n"+String.valueOf(reward)+rewardType,game.BaseLabelStyle2);
        complitionBar = new ProgressBar(0,quest.getObjAmount(),1f,true,(Skin) manager.get("InventoryItems/Other/QuestProgressBar/ProgressBar.json"));


        }else{
            rewardLabel= new Label("",game.BaseLabelStyle2);
            complitionBar = new ProgressBar(0,1,1f,true,(Skin) manager.get("InventoryItems/Other/QuestProgressBar/ProgressBar.json"));
        }

        remove_complete_button = new TextButton("", (Skin) manager.get("InventoryItems/Other/QuestTextButton/QuestButton.json"));
        baseTable = new Table();
        progressBarStack = new Stack();
        baseBackGround = new Label("",questBarSkin);

        createLabel();
        addLabelActors();

        DisplayAndConditions();
        addQuestButtonSensors();

    }

    public void createLabel(){
        complitionBar.setScale(300,300);
        complitionBar.setColor(Color.YELLOW);
//        complitionBar.setValue(5);

        rewardLabel.setSize(0,0);
        remove_complete_button.setSize(questWidth*0.3f,questWidth*0.1f);
        noQuest.setSize(0,0);

        rewardLabel.setVisible(false);
        noQuest.setVisible(false);

        stack.setBounds(-questWidth*0.6f,-questWidth*0.12f,questWidth,questHeight);
        noQuest.setPosition(questWidth*0.4f,questHeight*0.4f);
        rewardLabel.setPosition(questWidth*0.2f,questHeight*0.2f);
        remove_complete_button.setPosition(-questWidth*0.45f,-questHeight*0.02f);
    }
    public void addLabelActors(){
        baseTable.add(baseBackGround).size(questWidth,questHeight);
        table.addActor(baseTable);
        baseTable.addActor(stack);
        stack.addActor(rewardLabel);
        stack.addActor(noQuest);
        baseTable.addActor(remove_complete_button);
        baseTable.add(complitionBar).size(10,100);
    }
    public void DisplayAndConditions(){
        if(!questAllocated){
            noQuest.setVisible(true);
            remove_complete_button.setText("Not Available");
            remove_complete_button.setVisible(false);
            complitionBar.setVisible(false);
        }
        else {
            noQuest.setVisible(false);

            questDescription = "\t\t\t\tDescription:\n\t\t\t\t - "+quest.getQuestDescription()+ ": "+String.valueOf(quest.getObjAmount())+"\n\t\t\t\t - Difficulty: "+quest.getDifficulty();
            baseBackGround.setText(questDescription);
            baseBackGround.setFontScale(0.7f);
            rewardLabel.setVisible(true);
            complitionBar.setVisible(true);
            remove_complete_button.setVisible(true);
            remove_complete_button.setText("Quest");
        }
    }
    public void addQuestButtonSensors(){

        remove_complete_button.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if(isCompleted)
                    remove_complete_button.setText("Claim");
                else
                    remove_complete_button.setText("Remove");
                super.enter(event, x, y, pointer, fromActor);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                remove_complete_button.setText("Quest");
                super.exit(event, x, y, pointer, toActor);
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(quest.getQuestStatus()&& isCompleted){
                    if(quest.getsRewardGold())
                    game.player.setGold(game.player.getGold()+reward);
                    else game.player.tokens.getAndAdd(reward);
                    game.player.kCompleteQuests+=1;
                    game.player.kEarnedGoldThroughQuest+=reward;
                }
                    baseBackGround.setText("");
                    rewardLabel.setVisible(false);
                    complitionBar.setVisible(false);
                    remove_complete_button.setVisible(false);
                    remove_complete_button.setText("");
                    noQuest.setVisible(true);
                    isCompleted = false;
                    questAllocated =false;
                    game.player.activeQuests.set(index,null);

                super.clicked(event, x, y);
            }
        });
    }

    public void render(){
        if(questAllocated){

    isCompleted = quest.getQuestStatus(
            game.player.kAssignations,
            game.player.kChestsOpened,
            game.player.kTradesNPCSeller ,
            game.player.kTradesNPCBuyer ,
            game.player.kDungeonsExplored,
            game.player.kUsedPositions,
            game.player.kCompleteQuests,
            game.player.kEarnedGoldThroughQuest,
            game.player.kCloseRangeKills,
            game.player.kLongRangeKills,
            game.player.kLevelsCompleted,
            game.player.kEarnedGoldThroughLevels
    );
            complitionBar.setValue(quest.getProgressBarVal());
            if(isCompleted)
                remove_complete_button.setText("Claim");

        }

    }

    public void updateNewQuest(QuestDocument quest){
        this.quest = quest;
        reward = quest.getRewardAmount();
        rewardType = quest.getsRewardGold()? " Gold":" Skill Tokens";
        rewardLabel.setText("Reward: \n"+String.valueOf(reward)+rewardType);
        questAllocated = true;
        complitionBar.setRange(0,quest.getObjAmount());
        DisplayAndConditions();
        addQuestButtonSensors();
    }

    public Table getTable(){
        table.setPosition(posX,posY);
        return table;
    }

    @Override
    public void dispose() {
        manager.dispose();
    }
}
