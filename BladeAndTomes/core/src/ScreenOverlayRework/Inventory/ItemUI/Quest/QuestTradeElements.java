package ScreenOverlayRework.Inventory.ItemUI.Quest;

import ScreenOverlayRework.Inventory.ItemUI.QuestUI;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class QuestTradeElements extends QuestUIElements{
    private QuestUI questUI;
    public QuestTradeElements(BladeAndTomes game, AssetManager manager, QuestDocument quest, int posX, int posY, int index, QuestUI questUI) {
        super(game, manager, quest, posX, posY, index);
        this.questUI =questUI;
        BuyerDisplayAndConditions();
    }


    @Override
    public void DisplayAndConditions() {
        /*nothing*/
    }

    @Override
    public void addQuestButtonSensors() {
        /*nothing*/

    }

    public void BuyerDisplayAndConditions(){
        questDescription = "\t\t\t\tDescription:\n\t\t\t\t - "+quest.getQuestDescription()+ ": "+String.valueOf(quest.getObjAmount())+"\n\t\t\t\t - Difficulty: "+quest.getDifficulty();
        baseBackGround.setText(questDescription);
        baseBackGround.setFontScale(0.7f);
        rewardLabel.setVisible(true);
        complitionBar.setVisible(false);
        remove_complete_button.setVisible(true);
        remove_complete_button.setText("Buy: "+String.valueOf(quest.getQuestObtainAmount())+" Gold");
        remove_complete_button.getLabel().setFontScale(0.7f);
        remove_complete_button.addListener(new ClickListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if(!game.player.activeQuests.contains(null,true)){
                    remove_complete_button.getLabel().setFontScale(0.5f);
                    remove_complete_button.setText("Quest Limit Reached");}
                else{
                    remove_complete_button.setText(String.valueOf(quest.getQuestObtainAmount())+" Gold");
                    if(game.player.getGold()<quest.getQuestObtainAmount()){
                        remove_complete_button.getLabel().setFontScale(0.5f);
                        remove_complete_button.setText("Need More Gold!");}}

                super.enter(event, x, y, pointer, fromActor);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                remove_complete_button.getLabel().setFontScale(0.7f);

                remove_complete_button.setText("Buy: "+String.valueOf(quest.getQuestObtainAmount())+" Gold");
                super.exit(event, x, y, pointer, toActor);
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(game.player.getGold()>=quest.getQuestObtainAmount()&&game.player.activeQuests.contains(null,true)){
                    int newGold =  game.player.getGold()-quest.getQuestObtainAmount();
                    game.player.setGold(newGold);
                    for (int i =0;i<game.player.activeQuests.size;++i)
                        if(game.player.activeQuests.get(i) == null){
                            game.player.activeQuests.set(i,quest);
                            questUI.updateIndex(i);
                            break;
                        }
                    }
                super.clicked(event, x, y);
            }
        });

    }

}
