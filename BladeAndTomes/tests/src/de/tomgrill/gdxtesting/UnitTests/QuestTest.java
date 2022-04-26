package de.tomgrill.gdxtesting.UnitTests;

import ScreenOverlayRework.Inventory.ItemUI.Quest.QuestDocument;
import de.tomgrill.gdxtesting.GdxTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)

public class QuestTest {

    /**
     * This test will make sure quests are properly working
     */
    @Test
    public void Test_Quest() {
        for(int i = 0;i<1000;++i){
            QuestDocument doc = new QuestDocument("Easy");
            boolean questReward = doc.getRewardAmount()>doc.getObjAmount();
            Assert.assertTrue(!questReward);
            doc.setInitialValues(0,0,0,0,0,0,0,0,00,0,0,0);
            int progressBarVal_Before = doc.getProgressBarVal();
            doc.getQuestStatus(1,1,1,1,1,1,1,1,1,1,1,1);
            int progressBarVal_After = doc.getProgressBarVal();
            if(progressBarVal_After==progressBarVal_Before) {
                Assert.assertTrue(false);
            }
        }
    }
}

