package ScreenOverlayRework.Inventory.ItemUI.Quest;

import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class QuestDocument {
    private String difficulty;
    private Array<String> availableQuests;
    private int currentQuestIndex = 0;
    private boolean isRewardGold;
    private transient Random randomNumber;
    private int rewardAmount;
    private int kObj;
    private int kQuestObtainAmount;
    private transient int progressBarVal;
    private String info;


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


    /**
     * This constructor has all the available quests
     * @param difficulty the difficulty of quest: easy medium hard
     */
    public QuestDocument(String difficulty){
        this.difficulty = difficulty;
        availableQuests = new Array<>();
        randomNumber = new Random();
        rewardAmount = 0;
        kQuestObtainAmount = 0;
        // Available quests
        availableQuests.add("Kill Enemies");
        availableQuests.add("Open Chests");
        availableQuests.add("Trade Items with NPC");
        availableQuests.add("Explore Dungeon Rooms");
        availableQuests.add("Sell Items to NPC");
        availableQuests.add("Consume potions");
        availableQuests.add("Complete quests");
        availableQuests.add("Buy Items From NPC");
        availableQuests.add("Kill Enemies Using Close \n\t\t\t\t  Range Weapons");
        availableQuests.add("Kill Enemies Using Long \n\t\t\t\t  Range Weapons");
        availableQuests.add("Complete Level(s)");
        availableQuests.add("Earn Gold Through Levels");

        // Generates quest based on difficulty
        generateQuest(difficulty);
    }
    public void generateQuest(String diff){
        switch (diff){
            case "HARD":
                // Random Quest Picking
                currentQuestIndex = randomNumber.nextInt(availableQuests.size);
                info = availableQuests.get(currentQuestIndex);
                // Calculating Gold
                isRewardGold = (randomNumber.nextInt(2) == 1)? true:false;
                rewardAmount = (isRewardGold)?( randomNumber.nextInt(5)+15):(randomNumber.nextInt(2));
                ++rewardAmount;
                // Random Quest Obtain Amount
                kObj = randomNumber.nextInt(5)+7;
                kQuestObtainAmount = randomNumber.nextInt(4)+randomNumber.nextInt(4)+randomNumber.nextInt(4);
                if(isRewardGold)
                    rewardAmount+=kQuestObtainAmount;
                break;
            case "MEDIUM":
                // Random Quest Picking
                currentQuestIndex = randomNumber.nextInt(availableQuests.size);
                // Calculating Gold
                isRewardGold = true;
                rewardAmount = randomNumber.nextInt(5)+7;
                ++rewardAmount;
                // Random Quest Obtain Amount
                kObj = randomNumber.nextInt(5)+2;
                kQuestObtainAmount = randomNumber.nextInt(4)+randomNumber.nextInt(4);
                rewardAmount+=kQuestObtainAmount;
                break;
            case "EASY":
                // Random Quest Picking
                currentQuestIndex = randomNumber.nextInt(availableQuests.size);
                // Calculating Gold
                isRewardGold = true;
                rewardAmount = randomNumber.nextInt(5);
                ++rewardAmount;
                // Random Quest Obtain Amount
                kObj = randomNumber.nextInt(5)+1;
                kQuestObtainAmount = randomNumber.nextInt(4)+1;
                rewardAmount+=kQuestObtainAmount;
                break;
        }
        ++rewardAmount;
        ++kQuestObtainAmount;
        ++kObj;
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
         kInitialCloseRangeKills= kCurrentCloseRangeKills;
         kInitialLongRangeKills = kCurrentLongRangeKills;
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
                output = (kCurrentOpenedChests-kInitialOpenedChests)>=kObj;
                progressBarVal =  (kCurrentOpenedChests-kInitialOpenedChests);

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
                output = ((kCurrentLongRangeKills-kInitialLongRangeKills))>=kObj;
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
