package Keyboard_Mouse_Controls;

import com.badlogic.game.creatures.Item;
import com.badlogic.game.screens.MainMenu;
import com.badlogic.gdx.Input;
import java.io.*;

// Needed to do some revamping of this class. Found it made the process a bit easier.
public class MainMenuControls {

    private int moveDown;
    private int moveUp;
    private int moveLeft;
    private int moveRight;
    private int tradeMenu;
    private int openPauseMenu;
    private int openInventory;
    private int fightAction;
    private int slotSelection[];
    private int item1;
    private int item2;
    private int item3;
    private int item4;

    public MainMenuControls() {
        this.moveUp = Input.Keys.UP;
        this.moveDown = Input.Keys.DOWN;
        this.moveLeft = Input.Keys.LEFT;
        this.moveRight = Input.Keys.RIGHT;
        this.tradeMenu = Input.Keys.T;
        this.openPauseMenu = Input.Keys.ESCAPE;
        this.openInventory = Input.Keys.E;
        this.fightAction = Input.Keys.Q;
        this.slotSelection = new int[]{
                Input.Keys.NUM_1,
                Input.Keys.NUM_2,
                Input.Keys.NUM_3,
                Input.Keys.NUM_4,
                Input.Keys.NUM_5,
        };
        this.item1 = Input.Keys.NUM_1;
        this.item2 = Input.Keys.NUM_2;
        this.item3 = Input.Keys.NUM_3;
        this.item4 = Input.Keys.NUM_4;
    }

    public MainMenuControls(int moveUp, int moveDown, int moveLeft, int moveRight,
                            int tradeMenu, int openPauseMenu, int openInventory, int fightAction, int item1, int item2, int item3, int item4) {
        this.moveUp = moveUp;
        this.moveDown = moveDown;
        this.moveLeft = moveLeft;
        this.moveRight = moveRight;
        this.tradeMenu = tradeMenu;
        this.openPauseMenu = openPauseMenu;
        this.openInventory = openInventory;
        this.fightAction = fightAction;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.slotSelection = new int[]{
                Input.Keys.NUM_1,
                Input.Keys.NUM_2,
                Input.Keys.NUM_3,
                Input.Keys.NUM_4,
                Input.Keys.NUM_5,
        };
    }
    // private String viewParty;
    // private String acceptOption
    public int getMoveDown() {
        return moveDown;
    }

    public void setMoveDown(int moveDown) {
        this.moveDown = moveDown;
    }

    public int getMoveUp() {
        return moveUp;
    }

    public void setMoveUp(int moveUp) {
        this.moveUp = moveUp;
    }

    public int getMoveLeft() {
        return moveLeft;
    }

    public void setMoveLeft(int moveLeft) {
        this.moveLeft = moveLeft;
    }

    public int getMoveRight() {
        return moveRight;
    }

    public void setMoveRight(int moveRight) {
        this.moveRight = moveRight;
    }

    public int getTradeMenu() {
        return tradeMenu;
    }

    public void setTradeMenu(int tradeMenu) {
        this.tradeMenu = tradeMenu;
    }

    public int getOpenPauseMenu() {
        return openPauseMenu;
    }

    public void setOpenPauseMenu(int openPauseMenu) {
        this.openPauseMenu = openPauseMenu;
    }

    public int getOpenInventory() { return openInventory; }

    public void setOpenInventory(int openInventory) { this.openInventory = openInventory; }

    public int getFightAction() { return fightAction; }

    public void setFightAction(int fightAction) { this.fightAction = fightAction; }

    public int getSelection(int index){
        return slotSelection[index];
    }

    public void setSelection(int index, int key){
        slotSelection[index] = key;
    }
    public int getItem1() {
        return item1;
    }

    public void setItem1(int item1) {
        this.item1 = item1;
    }

    public int getItem2() {
        return item2;
    }

    public void setItem2(int item2) {
        this.item2 = item2;
    }

    public int getItem3() {
        return item3;
    }

    public void setItem3(int item3) {
        this.item3 = item3;
    }

    public int getItem4() {
        return item4;
    }

    public void setItem4(int item4) {
        this.item4 = item4;
    }


}


