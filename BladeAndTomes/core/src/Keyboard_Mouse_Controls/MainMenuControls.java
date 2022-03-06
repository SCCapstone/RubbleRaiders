package Keyboard_Mouse_Controls;

import com.badlogic.game.creatures.Item;
import com.badlogic.game.screens.MainMenu;
import com.badlogic.gdx.Input;
import java.io.*;

// Needed to do some revamping of this class. Found it made the process a bit easier.
public class MainMenuControls implements Serializable {

    private int moveDown;
    private int moveUp;
    private int moveLeft;
    private int moveRight;
    private int tradeMenu;
    private int openPauseMenu;
    private int openInventory;
    private int fightAction;

    public MainMenuControls() {
        this.moveUp = Input.Keys.UP;
        this.moveDown = Input.Keys.DOWN;
        this.moveLeft = Input.Keys.LEFT;
        this.moveRight = Input.Keys.RIGHT;
        this.tradeMenu = Input.Keys.T;
        this.openPauseMenu = Input.Keys.ESCAPE;
        this.openInventory = Input.Keys.E;
        this.fightAction = Input.Keys.Q;
    }


    public MainMenuControls(int moveUp, int moveDown, int moveLeft, int moveRight,
                            int tradeMenu, int openPauseMenu, int openInventory, int fightAction) {
        this.moveUp = moveUp;
        this.moveDown = moveDown;
        this.moveLeft = moveLeft;
        this.moveRight = moveRight;
        this.tradeMenu = tradeMenu;
        this.openPauseMenu = openPauseMenu;
        this.openInventory = openInventory;
        this.fightAction = fightAction;
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


}


