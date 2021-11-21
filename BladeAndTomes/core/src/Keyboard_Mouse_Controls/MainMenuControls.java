package Keyboard_Mouse_Controls;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class MainMenuControls extends InputListener {

    private int moveDown;
    private int moveUp;
    private int moveLeft;
    private int moveRight;
    private int openInventory;
    private int openPauseMenu;
    private int interactAction;
    private int viewParty;
    private int acceptOption;

    public MainMenuControls()
    {
        moveDown = Input.Keys.DOWN;
        moveLeft = Input.Keys.LEFT;
        moveRight = Input.Keys.RIGHT;
        moveUp = Input.Keys.UP;
        openInventory = Input.Keys.I;
        openPauseMenu = Input.Keys.ESCAPE;
        interactAction = Input.Keys.A;
        viewParty = Input.Keys.P;
        acceptOption = Input.Keys.ENTER;
    }

    public boolean keyDown(int i) {
        return true;
    }

    public boolean keyUp(int i) {
        return false;
    }

    public boolean keyTyped(char c) {
        return false;
    }

    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    public boolean scrolled(float v, float v1) {
        return false;
    }
}
