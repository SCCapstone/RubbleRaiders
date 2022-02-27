package Xml;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.creatures.Player;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GameInfo  implements Serializable {

    // implement for save spots queue
    private static final long SerialVersionUID = 1;

    private final int CHARACTER_LIMIT = 4;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
    Date date = new Date();

    public Player currPlayer;

    public String[] playerNames;
    public String[] saveTime;

    public GameInfo() {
        playerNames = new String[CHARACTER_LIMIT];
        saveTime = new String[CHARACTER_LIMIT];
    }

    public void init() {
        for(int i =0; i < CHARACTER_LIMIT; i++) {
            playerNames[i] = "Empty";
            saveTime[i] = "Empty";
            // we will want this when updating it
            //saveTime[i] = dateFormat.format(new Date());
        }
    }

    public String[] getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(String[] playerNames) {
        this.playerNames = playerNames;
    }

    public String[] getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(String[] saveTime) {
        this.saveTime = saveTime;
    }
}
