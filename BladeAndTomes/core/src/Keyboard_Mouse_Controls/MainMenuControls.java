package Keyboard_Mouse_Controls;

import com.badlogic.game.creatures.Item;
import com.badlogic.game.screens.MainMenu;
import com.badlogic.gdx.Input;
import java.io.*;

// Needed to do some revamping of this class. Found it made the process a bit easier.
public class MainMenuControls implements Serializable {

    private String moveDown;
    private String moveUp;
    private String moveLeft;
    private String moveRight;
    // private String openInventory;
    private String openPauseMenu;
    private String interactAction;

    public MainMenuControls() {
        this.moveUp = "w";
        this.moveDown = "s";
        this.moveLeft = "a";
        this.moveRight = "d";
        this.interactAction = "x";
        this.openPauseMenu = "escape";
    }

    public MainMenuControls(String moveUp, String moveDown, String moveLeft, String moveRight,
                            String interactAction, String openPauseMenu) {
        this.moveUp = "w";
        this.moveDown = "s";
        this.moveLeft = "a";
        this.moveRight = "d";
        this.interactAction = "x";
        this.openPauseMenu = "escape";
    }
    // private String viewParty;
    // private String acceptOption
    public String getMoveDown() {
        return moveDown;
    }

    public void setMoveDown(String moveDown) {
        this.moveDown = moveDown;
    }

    public String getMoveUp() {
        return moveUp;
    }

    public void setMoveUp(String moveUp) {
        this.moveUp = moveUp;
    }

    public String getMoveLeft() {
        return moveLeft;
    }

    public void setMoveLeft(String moveLeft) {
        this.moveLeft = moveLeft;
    }

    public String getMoveRight() {
        return moveRight;
    }

    public void setMoveRight(String moveRight) {
        this.moveRight = moveRight;
    }

    public String getOpenPauseMenu() {
        return openPauseMenu;
    }

    public void setOpenPauseMenu(String openPauseMenu) {
        this.openPauseMenu = openPauseMenu;
    }

    public String getInteractAction() {
        return interactAction;
    }

    public void setInteractAction(String interactAction) {
        this.interactAction = interactAction;
    }

}


