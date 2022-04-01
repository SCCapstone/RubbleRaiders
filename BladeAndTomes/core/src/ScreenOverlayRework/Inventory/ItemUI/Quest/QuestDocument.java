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


    // Initial User Stats

    private int kInitialKills;
    private int kInitialOpenedChests;
    private int kInitialItemsBought;
    private int kInitialItemsSold;
    private int kInitialDungeonsExplored;
    private int kInitialCompleteQuests;
    private int kInitialPotionsConsumed;

    private int kInitialEarnGoldThroughQuest;
    private int kInitialCloseRangeKills;
    private int kInitialLongRangeKills;
    private int kInitialLevelCompleted;
    private int kInitialEarnGoldThroughLevels;






    public QuestDocument(){
        availableQuests = new Hashtable<>();
//        rewardAmount = 0;
//        kQuestObtainAmount = 0;

        availableQuests.put(0,"Kill Enemies");
        availableQuests.put(1,"Open Chests");
        availableQuests.put(2,"Trade Items with NPC");
        availableQuests.put(3,"Explore Dungeon Rooms");
        availableQuests.put(4,"Sell Items to NPC");
        availableQuests.put(5,"Consume potions");
        availableQuests.put(6,"Complete quests");
        availableQuests.put(7,"Buy Items From NPC");
        availableQuests.put(8,"Kill Enemies Using Close \n\t\t\t\t  Range Weapons");
        availableQuests.put(9,"Kill Enemies Using Long \n\t\t\t\t  Range Weapons");
        availableQuests.put(10,"Complete Level(s)");
        availableQuests.put(11,"Earn Gold Through Levels");
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
        availableQuests.put(5,"Consume potions");
        availableQuests.put(6,"Complete quests");
        availableQuests.put(7,"Buy Items From NPC");
        availableQuests.put(8,"Kill Enemies Using Close \n\t\t\t\t  Range Weapons");
        availableQuests.put(9,"Kill Enemies Using Long \n\t\t\t\t  Range Weapons");
        availableQuests.put(10,"Complete Level(s)");
        availableQuests.put(11,"Earn Gold Through\n\t\t\t\t  Levels");



        generateQuest(difficulty);
    }
    public void generateQuest(String diff){
        switch (diff){
            case "HARD":
                currentQuestIndex = randomNumber.nextInt(availableQuests.size());
                System.out.println(availableQuests.size()+"  "+currentQuestIndex);
                isRewardGold = (randomNumber.nextInt(2) == 1)? true:false;
                rewardAmount = (isRewardGold)?( randomNumber.nextInt(5)+15):(randomNumber.nextInt(2));
                ++rewardAmount;
                kObj = randomNumber.nextInt(5)+7;
                kQuestObtainAmount = randomNumber.nextInt(4)+randomNumber.nextInt(4)+randomNumber.nextInt(4);
                break;
            case "MEDIUM":
                currentQuestIndex = randomNumber.nextInt(availableQuests.size());
                isRewardGold = true;
                rewardAmount = randomNumber.nextInt(5)+7;
                ++rewardAmount;

                kObj = randomNumber.nextInt(5)+2;
                kQuestObtainAmount = randomNumber.nextInt(4)+randomNumber.nextInt(4);
                break;
            case "EASY":
                currentQuestIndex = randomNumber.nextInt(availableQuests.size());
                isRewardGold = true;
                rewardAmount = randomNumber.nextInt(5)+1;
                ++rewardAmount;

                kObj = randomNumber.nextInt(5)+1;
                kQuestObtainAmount = randomNumber.nextInt(4)+1;
                break;

        }
        progressBarVal = 0;
    }
    public void setInitialValues(
            int kCurrentKills,
            int kCurrentOpenedChests,
            int kCurrentItemsBought,
            int kCurrentItemsSold,
            int kCurrentDungeonsExplored,
            int kCurrentPotionsConsumed,
            int kCurrentCompleteQuests,
            int kCurrentEarnGoldThroughQuest,
            int kCurrentCloseRangeKills,
            int kCurrentLongRangeKills,
            int kCurrentLevelCompleted,
            int kCurrentEarnGoldThroughLevels
    ){
         kInitialKills = kCurrentKills;
         kInitialOpenedChests = kCurrentOpenedChests;
         kInitialItemsBought = kCurrentItemsBought;
         kInitialItemsSold = kCurrentItemsSold;
         kInitialDungeonsExplored = kCurrentDungeonsExplored;
         kInitialCompleteQuests = kCurrentCompleteQuests;
         kInitialPotionsConsumed = kCurrentPotionsConsumed;
         kInitialEarnGoldThroughQuest= kCurrentEarnGoldThroughQuest;
         kCurrentCloseRangeKills= kCurrentCloseRangeKills;
         kCurrentLongRangeKills = kCurrentLongRangeKills;
         kInitialLevelCompleted =  kCurrentLevelCompleted;
         kInitialEarnGoldThroughLevels= kCurrentEarnGoldThroughLevels;

    }
    public boolean getQuestStatus(){
        return true;
    }

    public boolean getQuestStatus( int kCurrentKills,
                                   int kCurrentOpenedChests,
                                   int kCurrentItemsBought,
                                   int kCurrentItemsSold,
                                   int kCurrentDungeonsExplored,
                                   int kCurrentPotionsConsumed,
                                   int kCurrentCompleteQuests,
                                   int kCurrentEarnGoldThroughQuest,
                                   int kCurrentCloseRangeKills,
                                   int kCurrentLongRangeKills,
                                   int kCurrentLevelCompleted,
                                   int kCurrentEarnGoldThroughLevels){

        boolean output = false;
        switch (currentQuestIndex){
            case 0:
                // Kill Enemies
                output = (kCurrentKills-kInitialKills)>=kObj;
                progressBarVal =   (kCurrentKills-kInitialKills);

                return output;
            case 1:
                // Open Chests
                output = (kCurrentOpenedChests-kCurrentOpenedChests)>=kObj;
                progressBarVal =  (kCurrentOpenedChests-kCurrentOpenedChests);

                return output;
            case 2:
                // Trade Items with NPC;
                output = ((kCurrentItemsSold-kInitialItemsSold)+(kCurrentItemsBought-kInitialItemsBought))>=kObj;
                progressBarVal =  ((kCurrentItemsSold-kInitialItemsSold)+(kCurrentItemsBought-kInitialItemsBought));
                return output;
            case 3:
                // Explore Dungeon Rooms
                output = (kCurrentDungeonsExplored-kInitialDungeonsExplored)>=kObj;
                progressBarVal =  (kCurrentDungeonsExplored-kInitialDungeonsExplored);
                return output;
            case 4:
                // Sell Items to NPC
                output = (kCurrentItemsSold-kInitialItemsSold)>=kObj;
                progressBarVal =  (kCurrentItemsSold-kInitialItemsSold);
                return output;
            case 5:
                // Consume potions
                output = ((kCurrentPotionsConsumed-kInitialPotionsConsumed))>=kObj;
                progressBarVal = ((kCurrentPotionsConsumed-kInitialPotionsConsumed));
                return output;
            case 6:
                // Complete quests
                output = ((kCurrentCompleteQuests-kInitialCompleteQuests))>=kObj;
                progressBarVal = ((kCurrentCompleteQuests-kInitialCompleteQuests));
                return output;
            case 7:
                // Buy Items From NPC
                output = ((kCurrentItemsBought-kInitialItemsBought))>=kObj;
                progressBarVal = ((kCurrentItemsBought-kInitialItemsBought));
                return output;
            case 8:
                // Kill Enemies Using Close  Range Weapons
                output = ((kCurrentCloseRangeKills-kInitialCloseRangeKills))>=kObj;
                progressBarVal = ((kCurrentCloseRangeKills-kInitialCloseRangeKills));
                return output;

            case 9:
                // Kill Enemies Using Long Range Weapons
                output = ((kCurrentLongRangeKills-kCurrentLongRangeKills))>=kObj;
                progressBarVal =  ((kCurrentLongRangeKills-kCurrentLongRangeKills));
                return output;
            case 10:
                // Complete Level(s)
                output = ((kCurrentLevelCompleted-kInitialLevelCompleted))>=kObj;
                progressBarVal = ((kCurrentLevelCompleted-kInitialLevelCompleted));
                return output;
            case 11:
                // Earn Gold Through Levels
                output = ((kCurrentEarnGoldThroughLevels-kInitialEarnGoldThroughLevels))>=kObj;
                progressBarVal = ((kCurrentEarnGoldThroughLevels-kInitialEarnGoldThroughLevels));
                return output;

            default:
                return false;
        }

    }
    public void updateQuestStatus(){

    }
    public String getQuestDescription(){
        return availableQuests.get(currentQuestIndex);
    }
    public int getRewardAmount(){
        return rewardAmount/2;
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
