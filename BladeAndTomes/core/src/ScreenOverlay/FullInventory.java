package ScreenOverlay;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class FullInventory {
    final BladeAndTomes Game;
    float optionSpace, optionWidth,
            optionHeight, optionLocX,
            optionLocY;
    float inventoryWidth,
            inventoryHeight, inventoryLocX,
            inventoryLocY;
    boolean showInventory;
    private final Button[] inventorySloat;
    private final Button InventoryButton;
    private final Button QuestButton;
    private final Button SkillButton;
    private boolean previousShowInventory;
    private final int startIndexInventory;
    private final Label hiddenInventoryLabel;
    private int defaultInventorySelection;
    private final MainInventory parent;

    public FullInventory(final BladeAndTomes GAME, final MainInventory temp) {
        parent = temp;
        Game = GAME;
        inventoryWidth = 1000f;
        inventoryHeight = 700f;
        inventoryLocX = Game.WINDOWWIDTH - Game.WINDOWHIGHT - 200;
        inventoryLocY = Game.WINDOWHIGHT - inventoryHeight - 200;

        previousShowInventory = true;
        defaultInventorySelection = 0;
        optionWidth = 125f;
        optionSpace = optionWidth + 25;
        optionHeight = 125f;
        optionLocX = inventoryLocX + 25;
        optionLocY = 800f;
        inventorySloat = new TextButton[12];
        startIndexInventory = 7;
        for (int i = startIndexInventory; i < Game.inventory.mainInventoryItems.length; ++i)
            inventorySloat[i - 7] = new TextButton(Game.inventory.mainInventoryItems[i], Game.inventoryTextButtonStyle);

        showInventory = false;
        hiddenInventoryLabel = new Label("", Game.BaseLabelStyle2);


        InventoryButton = new TextButton("Inventory", Game.inventoryBaseStyle);
        QuestButton = new TextButton("Quest", Game.inventoryBaseStyle);
        SkillButton = new TextButton("Skills", Game.inventoryBaseStyle);


    }

    public void drawOptionButton() {
        float initialLocX = 680f, initialLocY = 1000;

        InventoryButton.setX(inventoryWidth - initialLocX + optionSpace);
        InventoryButton.setY(initialLocY - 25);
        InventoryButton.setWidth(inventoryWidth / 4);
        InventoryButton.setHeight(optionHeight - 25);

        QuestButton.setX(inventoryWidth - initialLocX + optionSpace * 3 + 25);
        QuestButton.setY(initialLocY - 25);
        QuestButton.setWidth(inventoryWidth / 4);
        QuestButton.setHeight(optionHeight - 25);

        SkillButton.setX(inventoryWidth - initialLocX + optionSpace * 6 - 50);
        SkillButton.setY(initialLocY - 25);
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

        System.out.println("Pressed");
        if (showInventory && showInventory != previousShowInventory) {
            handleStaticOverlays();
            showInvtory();
        } else if (!showInventory && showInventory != previousShowInventory)
            hideInvtory();

        if ((InventoryButton.isPressed() || defaultInventorySelection == 0) && showInventory)
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

    public void hideInvtory() {
        clearInventorySlots();
        InventoryButton.addAction(Actions.removeActor());
        SkillButton.addAction(Actions.removeActor());
        QuestButton.addAction(Actions.removeActor());

        hiddenInventoryLabel.addAction(Actions.removeActor());
    }
}
