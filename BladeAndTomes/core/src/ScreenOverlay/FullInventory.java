package ScreenOverlay;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.Random;

public class FullInventory {
    final BladeAndTomes Game;
    float optionSpace, optionWidth,
            optionHeight, optionLocX,
            optionLocY;
    float inventoryWidth,
            inventoryHeight, inventoryLocX,
            inventoryLocY;
    boolean showInventory;
    private  Button[] inventorySloat;
    private  Button[][] tradeSloat;

    private final Button InventoryButton;
    private final Button QuestButton;
    private final Button SkillButton;
    private final Button TradeScreen;
    private final Button CurrencyStats;


    private boolean previousShowInventory;
    private final int startIndexInventory;
    private final Label hiddenInventoryLabel;


    private int defaultInventorySelection;
    private final MainInventory parent;
    public boolean tradeScreen;
    public String[] availabletrades;
    public String[] currentTradeOptions;

    public boolean isTradesGenerated;
    


    public FullInventory(final BladeAndTomes GAME, final MainInventory temp) {
        parent = temp;
        Game = GAME;
        isTradesGenerated = false;
        inventoryWidth = 1000f;
        inventoryHeight = 700f;
        inventoryLocX = Game.WINDOWWIDTH - Game.WINDOWHIGHT - 400;
        inventoryLocY = Game.WINDOWHIGHT - inventoryHeight - 200;
        tradeScreen = false;
        previousShowInventory = true;
        defaultInventorySelection = 0;
        optionWidth = 125f;
        optionSpace = optionWidth + 25;
        optionHeight = 125f;
        optionLocX = inventoryLocX + 25;
        optionLocY = 600f;
        inventorySloat = new TextButton[12];
        startIndexInventory = 7;
        for (int i = startIndexInventory; i < Game.inventory.mainInventoryItems.length; ++i)
            inventorySloat[i - 7] = new TextButton(Game.inventory.mainInventoryItems[i], Game.inventoryTextButtonStyle);

        showInventory = false;
        hiddenInventoryLabel = new Label("", Game.BaseLabelStyle2);
        CurrencyStats = new TextButton("You Have \n"+Game.inventory.tradeCurrency+" Trade Tokens", Game.inventoryTextButtonStyle);

        InventoryButton = new TextButton("Inventory", Game.inventoryBaseStyle);
        TradeScreen = new TextButton("WELCOME TO NPC TRADES", Game.inventoryBaseStyle);

        QuestButton = new TextButton("Quest", Game.inventoryBaseStyle);
        SkillButton = new TextButton("Skills", Game.inventoryBaseStyle);

        availabletrades = new String[]{
                "Sword","Bow","Random\nTrade\nPoints","Cross\nBow","Whip","Battle\nAxe","Sickle","Flail","Light\nHammer","Trident","Throwing\nAxe","Dagger","Thor's\nHammer"
        };
        currentTradeOptions = new String[5];
        tradeSloat = new TextButton[4][5];




    }

    public void drawOptionButton() {
        float initialLocX = 680f, initialLocY = 1000;

        InventoryButton.setX(inventoryWidth - initialLocX + optionSpace);
        InventoryButton.setY(initialLocY - 250);
        InventoryButton.setWidth(inventoryWidth / 4);
        InventoryButton.setHeight(optionHeight - 25);

        QuestButton.setX(inventoryWidth - initialLocX + optionSpace * 3 + 25);
        QuestButton.setY(initialLocY - 250);
        QuestButton.setWidth(inventoryWidth / 4);
        QuestButton.setHeight(optionHeight - 25);

        SkillButton.setX(inventoryWidth - initialLocX + optionSpace * 6 - 50);
        SkillButton.setY(initialLocY - 250);
        SkillButton.setWidth(inventoryWidth / 4);
        SkillButton.setHeight(optionHeight - 25);

        Game.stageInstance.addActor(InventoryButton);
        Game.stageInstance.addActor(QuestButton);
        Game.stageInstance.addActor(SkillButton);

    }

    public void handleStaticOverlays() {

        hiddenInventoryLabel.setHeight(inventoryHeight);
        hiddenInventoryLabel.setWidth(inventoryWidth);
        hiddenInventoryLabel.setX(inventoryLocX);
        hiddenInventoryLabel.setY(inventoryLocY);
        Game.stageInstance.addActor(hiddenInventoryLabel);
    }

    public void showInvtory() {
        if(!tradeScreen)
        drawOptionButton();

        for (int i = 0; i < inventorySloat.length; ++i) {
            inventorySloat[i].hit(optionLocX, optionLocY * optionSpace, false);
            inventorySloat[i].setX(optionLocX + optionSpace * i - optionSpace * 3 * (i / 3));
            inventorySloat[i].setY(optionLocY - (i / 3) * 125);
            inventorySloat[i].setWidth(optionWidth);
            inventorySloat[i].setHeight(optionHeight);
            Game.stageInstance.addActor(inventorySloat[i]);
        }
    }

    public void update() {
        previousShowInventory = showInventory;
        showInventory = Gdx.input.isKeyJustPressed(Input.Keys.E) != showInventory;
        if (showInventory && showInventory != previousShowInventory) {
            handleStaticOverlays();
            showInvtory();
        } else if (!showInventory && showInventory != previousShowInventory && !tradeScreen)
            hideInvtory();

        if ((InventoryButton.isPressed() || defaultInventorySelection == 0) && showInventory  || tradeScreen )
            updateSlot();
        else {
            clearInventorySlots();
            defaultInventorySelection = (QuestButton.isPressed()) ? 1 : (SkillButton.isPressed()) ? 2 : 0;
        }

    }

    public void clearInventorySlots() {
        for (int i = 0; i < inventorySloat.length; ++i) {
            inventorySloat[i].addAction(Actions.removeActor());
        }
    }

    public void updateSlot() {
        for (int i = 0; i < inventorySloat.length; ++i) {
            final int finalI = i + 7;
            if (inventorySloat[i].isPressed()) {
                int[] ArrGame = Game.inventory.addSwap(finalI);
                if (ArrGame[0] >= 7) {
                    inventorySloat[ArrGame[0] - 7].addAction(Actions.removeActor());
                    inventorySloat[ArrGame[0] - 7] = new TextButton(Game.inventory.mainInventoryItems[ArrGame[0]], Game.inventoryTextButtonStyle);
                } else
                    parent.updateSingleSlot(ArrGame[0]);
                if (ArrGame[1] >= 7) {
                    inventorySloat[ArrGame[1] - 7].addAction(Actions.removeActor());
                    inventorySloat[ArrGame[1] - 7] = new TextButton(Game.inventory.mainInventoryItems[ArrGame[1]], Game.inventoryTextButtonStyle);
                } else
                    parent.updateSingleSlot(ArrGame[1]);
            }
        }
        showInvtory();
    }

    public void updateSingleSlot(int index) {
        if (previousShowInventory) {
            int temp = -7;
            if (index < 7)
                temp = 0;
            inventorySloat[index + temp].addAction(Actions.removeActor());
            inventorySloat[index + temp] = new TextButton(Game.inventory.mainInventoryItems[index], Game.inventoryTextButtonStyle);
        }
    }
    public void TradeScreen(){
        float initialLocX = 680f, initialLocY = 1000;
        TradeScreen.setX(inventoryWidth - initialLocX + optionSpace*3);
        TradeScreen.setY(initialLocY - 240);
        TradeScreen.setWidth(inventoryWidth / 4);
        TradeScreen.setHeight(optionHeight - 15);
        Game.stageInstance.addActor(TradeScreen);

        CurrencyStats.setX(optionLocX + optionSpace * 5 );
        CurrencyStats.setY(optionLocY+150);
        CurrencyStats.setWidth(optionWidth+50);
        CurrencyStats.setHeight(optionHeight);
        Game.stageInstance.addActor(CurrencyStats);


        for (int i = 0; i < 4; ++i) {
            for(int ii = 0; ii <4; ++ii) {
                tradeSloat[i][ii].setX(optionLocX + 110 * i + 500);
                tradeSloat[i][ii].setY(optionLocY+10 - 125*ii);
                tradeSloat[i][ii].setWidth(optionWidth-20);
                tradeSloat[i][ii].setHeight(optionHeight-20);
                Game.stageInstance.addActor(tradeSloat[i][ii]);
            }
        }


    }
    
    public void generateNPCTrades(){
        for (int i = 0; i <5;++i){
            int trade =new Random().nextInt(availabletrades.length);
            tradeSloat[1][i] = new TextButton(availabletrades[trade], Game.generalTextButtonStyle);
            currentTradeOptions[i] = availabletrades[trade];
        }
        for (int i = 0; i <5;++i){
            tradeSloat[0][i] = new TextButton("", Game.generalTextButtonStyle);
        }
        for (int i = 0; i <5;++i){
            tradeSloat[2][i] = new TextButton("Reset", Game.generalTextButtonStyle);
        }
        for (int i = 0; i <5;++i){
            tradeSloat[3][i] = new TextButton("Trade", Game.generalTextButtonStyle);
        }
    }


    public void clearTradeMenu(){
        for (int i = 0; i < 4; ++i) {
            for(int ii = 0; ii <5; ++ii) {
                tradeSloat[i][ii].addAction(Actions.removeActor());
            }
        }
    }
    public void hideInvtory() {
        clearInventorySlots();
        if(tradeSloat[0][0] != null)
        clearTradeMenu();

        InventoryButton.addAction(Actions.removeActor());
        SkillButton.addAction(Actions.removeActor());
        QuestButton.addAction(Actions.removeActor());
        TradeScreen.addAction(Actions.removeActor());
        CurrencyStats.addAction(Actions.removeActor());
        hiddenInventoryLabel.addAction(Actions.removeActor());
    }
    public void displayNPCTrade(){
        handleStaticOverlays();
        showInvtory();
        if(!isTradesGenerated){
        generateNPCTrades();
        isTradesGenerated =true;}
        TradeScreen();

    }
}
