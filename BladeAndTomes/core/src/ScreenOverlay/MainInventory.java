package ScreenOverlay;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MainInventory {
    private final BladeAndTomes Game;
    private final Button[] inventorySloat;
    TextButton.TextButtonStyle style;
    float optionSpace, optionWidth, optionHeight, optionLocX, optionLocY;
    int swapIndex1, swapIndex2;
    Label Health, Map, QuestArea;
    FullInventory hiddenInventory;

    public MainInventory(BladeAndTomes game) {
        swapIndex2 = -999;
        swapIndex1 = -999;
        Game = game;
        optionWidth = 100f;
        optionSpace = optionWidth;
        optionHeight = 100f;
        optionLocX = 0f;
        optionLocY = Game.WINDOWHIGHT - optionHeight;

        inventorySloat = new TextButton[]{
                new TextButton(Game.inventory.mainInventoryItems[0], Game.inventoryTextButtonStyle),
                new TextButton(Game.inventory.mainInventoryItems[1], Game.inventoryTextButtonStyle),
                new TextButton(Game.inventory.mainInventoryItems[2], Game.inventoryTextButtonStyle),
                new TextButton(Game.inventory.mainInventoryItems[3], Game.inventoryTextButtonStyle),
                new TextButton(Game.inventory.mainInventoryItems[4], Game.inventoryTextButtonStyle),
                new TextButton(Game.inventory.mainInventoryItems[5], Game.inventoryTextButtonStyle),
                new TextButton(Game.inventory.mainInventoryItems[6], Game.inventoryTextButtonStyle)
        };
        Map = new Label("MAP", Game.BaseLabelStyle1);
        Map.setAlignment(1);
        Map.setHeight(225);
        Map.setWidth(225);
        Map.setX(Game.WINDOWWIDTH - 225);
        Map.setY(Game.WINDOWHIGHT - 115, 1);

        Game.stageInstance.addActor(Map);
        Health = new Label("Health: " + Game.player.getHealthPoints(), Game.HealthLabelStyle);
        Health.setAlignment(1);
        Health.setHeight(50);
        Health.setWidth(225);
        Health.setX(Game.WINDOWWIDTH - 455);
        Health.setY(Game.WINDOWHIGHT - 60, 1);
        Game.stageInstance.addActor(Health);
        hiddenInventory = new FullInventory(Game, this);


    }
    public void showNPCTradeScreen(boolean doTrade){
        if(doTrade){
            hiddenInventory.tradeScreen = true;
            hiddenInventory.isTradesGenerated = false;
            hiddenInventory.displayNPCTrade();
        }
        else if(!doTrade && hiddenInventory.tradeScreen) {
            hiddenInventory.tradeScreen = false;
            hiddenInventory.isTradesGenerated = true;

//            hiddenInventory.clearInventorySlots();
            hiddenInventory.hideInvtory();
        }

    }

    // Put the xml parser here
    public void update() {

        hiddenInventory.update();

        for (int i = 0; i < inventorySloat.length; ++i) {
            final int finalI = i;
            if (inventorySloat[i].isPressed()) {
                int[] ArrGame = Game.inventory.addSwap(finalI);
                if (ArrGame[0] < 7) {
                    inventorySloat[ArrGame[0]] = new TextButton(Game.inventory.mainInventoryItems[ArrGame[0]], Game.inventoryTextButtonStyle);
                } else {
                    hiddenInventory.updateSingleSlot(ArrGame[0]);
                }
                if (ArrGame[1] < 7) {
                    inventorySloat[ArrGame[1]] = new TextButton(Game.inventory.mainInventoryItems[ArrGame[1]], Game.inventoryTextButtonStyle);
                } else {
                    hiddenInventory.updateSingleSlot(ArrGame[1]);
                }
            }

        }
        show();

    }

    public void updateSingleSlot(int index) {
        inventorySloat[index] = new TextButton(Game.inventory.mainInventoryItems[index], Game.inventoryTextButtonStyle);
        show();

    }


    public void show() {
        for (int i = 0; i < inventorySloat.length; ++i) {
            inventorySloat[i].hit(optionLocX, optionLocY * optionSpace, false);
            inventorySloat[i].setX(optionLocX + optionSpace * i);
            inventorySloat[i].setY(optionLocY);
            inventorySloat[i].setWidth(optionWidth);
            inventorySloat[i].setHeight(optionHeight);
            Game.stageInstance.addActor(inventorySloat[i]);
        }
    }

    public void reAddInventory() {
        Game.stageInstance.addActor(Map);
        Game.stageInstance.addActor(Health);
    }

    public void updateHealth() {
        Health.setText("Health: " + Game.player.getHealthPoints());
    }

}
