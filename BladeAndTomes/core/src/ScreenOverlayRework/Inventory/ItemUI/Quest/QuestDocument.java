package ScreenOverlayRework.Inventory.ItemUI.Quest;

import java.util.Hashtable;
import java.util.Random;

public class QuestDocument {
    private String difficulty;
    private Hashtable<Integer,String> availableQuests;
    private int currentQuestIndex = 0;
    private boolean isRewardGold;
    private transient Random randomNumber;
    private int rewardAmount;
    private int kObj;
    private int kQuestObtainAmount;
    private transient int progressBarVal;

    public QuestDocument(){
        availableQuests = new Hashtable<>();
//        rewardAmount = 0;
//        kQuestObtainAmount = 0;
        availableQuests.put(0,"Kill Enemies");
        availableQuests.put(1,"Open Chests");
        availableQuests.put(2,"Trade Items with NPC");
        availableQuests.put(3,"Explore Dungeon Rooms");
        availableQuests.put(4,"Sell Items to NPC");
        availableQuests.put(5, "Descend to Level 2 of dungeon");
        availableQuests.put(6,"Complete skill events");
        availableQuests.put(7, "Complete easy quests");
        availableQuests.put(8, "Complete medium quests");
        availableQuests.put(9, "Complete hard quests");
        availableQuests.put(10,"Return to the Overworld");
        availableQuests.put(11, "Upgrade skills");
        availableQuests.put(12, "Equip Level 3 Armour");
        availableQuests.put(13, "Equip Level 3 weapons");
        availableQuests.put(14, "Consume potions");
    }
    public QuestDocument(String difficulty){
        this.difficulty = difficulty;
        availableQuests = new Hashtable<>();
        randomNumber = new Random();
        rewardAmount = 0;
        kQuestObtainAmount = 0;
        availableQuests.put(0,"Kill Enemies");
        availableQuests.put(1,"Open Chests");
        availableQuests.put(2,"Trade Items with NPC");
        availableQuests.put(3,"Explore Dungeon Rooms");
        availableQuests.put(4,"Sell Items to NPC");
        availableQuests.put(5, "Descend to Level 2 of dungeon");
        availableQuests.put(6,"Complete skill events");
        availableQuests.put(7, "Complete easy quests");
        availableQuests.put(8, "Complete medium quests");
        availableQuests.put(9, "Complete hard quests");
        availableQuests.put(10,"Return to the Overworld");
        availableQuests.put(11, "Upgrade skills");
        availableQuests.put(12, "Equip Level 3 Armour");
        availableQuests.put(13, "Equip Level 3 weapons");
        availableQuests.put(14, "Consume potions");
        generateQuest(difficulty);
    }
    public void generateQuest(String diff){
        switch (diff){
            case "HARD":
                currentQuestIndex = randomNumber.nextInt(availableQuests.size());
                System.out.println(availableQuests.size()+"  "+currentQuestIndex);
                isRewardGold = (randomNumber.nextInt(2) == 1)? true:false;
                rewardAmount = (isRewardGold)? randomNumber.nextInt(5)+15:randomNumber.nextInt(2)+1;
                kObj = randomNumber.nextInt(5)+7;
                kQuestObtainAmount = randomNumber.nextInt(4)+randomNumber.nextInt(4)+randomNumber.nextInt(4);
                break;
            case "MEDIUM":
                currentQuestIndex = randomNumber.nextInt(availableQuests.size());
                isRewardGold = true;
                rewardAmount = randomNumber.nextInt(5)+7;
                kObj = randomNumber.nextInt(5)+2;
                kQuestObtainAmount = randomNumber.nextInt(4)+randomNumber.nextInt(4);
                break;
            case "EASY":
                currentQuestIndex = randomNumber.nextInt(availableQuests.size());
                isRewardGold = true;
                rewardAmount = randomNumber.nextInt(5)+1;
                kObj = randomNumber.nextInt(5)+1;
                kQuestObtainAmount = randomNumber.nextInt(4)+1;
                break;

        }
        progressBarVal = 0;
    }

    public boolean getQuestStatus(){


        return true;
    }
    public void updateQuestStatus(){

    }
    public String getQuestDescription(){
        return availableQuests.get(currentQuestIndex);
    }
    public int getRewardAmount(){
        return rewardAmount;
    }
    public int getObjAmount(){
        return kObj;
    }
    public int getQuestObtainAmount(){
        return kQuestObtainAmount;
    }
    public String getDifficulty() {
        return difficulty;
    }
    public boolean getsRewardGold(){
        return isRewardGold;
    }
    public int getProgressBarVal(){
        return progressBarVal;

    }
}
