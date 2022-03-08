package Keyboard_Mouse_Controls;

import Xml.GameInfo;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.creatures.Player;
import com.badlogic.game.screens.CharacterCreation;
import com.badlogic.game.screens.MainMenu;
import com.badlogic.game.screens.Overworld;
import com.badlogic.gdx.Gdx;

import java.io.*;

public class SaveLoadGame implements Serializable {

    private static BladeAndTomes GAME;
    //final BladeAndTomes GAME;

    // this info is used for date of save, and other such things
    public static GameInfo Gi;

    // we will import our player class here
    public static Player Pl;

    public SaveLoadGame(BladeAndTomes game) {
        GAME = game;
    }

    public static void saveGameOne() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("GameSave.sav"));
            out.writeObject(Gi);
            out.writeObject(Pl);
            out.close();
        } catch(Exception e) {
            e.printStackTrace();
            //Gdx.app.exit();
        }
    }

    public static void saveGameTwo() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("GameSaveTwo.sav"));
            out.writeObject(Gi);
            out.writeObject(Pl);
            out.close();
        } catch(Exception e) {
            e.printStackTrace();
            //Gdx.app.exit();
        }
    }

    public static void saveGameThree() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("GameSaveThree.sav"));
            out.writeObject(Gi);
            out.writeObject(Pl);
            out.close();
        } catch(Exception e) {
            e.printStackTrace();
            //Gdx.app.exit();
        }
    }

    public static void saveGameFour() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("GameSaveFour.sav"));
            out.writeObject(Gi);
            out.writeObject(Pl);
            out.close();
        } catch(Exception e) {
            e.printStackTrace();
            //Gdx.app.exit();
        }
    }

    public static void LoadSaveOne() {
        try {
            if(!saveFileExists()) {
                init();
                return;
            }
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("GameSave.sav"));
            Gi = (GameInfo) in.readObject();
            Pl = (Player) in.readObject();
        } catch(Exception e) {
            e.printStackTrace();
            // Gdx.app.exit();
        }
        // GAME.setScreen(new Overworld(GAME));
    }

    public static void LoadSaveTwo() {
        try {
            if(!saveFileExists()) {
                init();
                return;
            }
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("GameSaveTwo.sav"));
            Gi = (GameInfo) in.readObject();
            Pl = (Player) in.readObject();
        } catch(Exception e) {
            e.printStackTrace();
            // Gdx.app.exit();
        }
        // GAME.setScreen(new Overworld(GAME));
    }

    public static void LoadSaveThree() {
        try {
            if(!saveFileExists()) {
                init();
                return;
            }
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("GameSaveThree.sav"));
            Gi = (GameInfo) in.readObject();
            Pl = (Player) in.readObject();
            GAME.setScreen(new Overworld(GAME));
        } catch(Exception e) {
            e.printStackTrace();
            // Gdx.app.exit();
        }
    }

    public static void LoadSaveFour() {
        try {
            if(!saveFileExists()) {
                init();
                return;
            }
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("GameSaveFour.sav"));
            Gi = (GameInfo) in.readObject();
            Pl = (Player) in.readObject();
            GAME.setScreen(new Overworld(GAME));
        } catch(Exception e) {
            e.printStackTrace();
            // Gdx.app.exit();
        }
    }

    public static boolean saveFileExists() {
        File g = new File("GameSave.sav");
        return g.exists();
    }

    public static void init() {
        Gi = new GameInfo();
        Gi.init();
        saveGameOne();
        GAME.setScreen(new CharacterCreation(GAME));
    }

    /*
    // we will instatiante the specific values we want for our save files.
    // Should be able to save the other values all here, but display them in our table fields
    private static String saveFile = "SAVED GAME";
    private String playerName = playerObj.getName();
    private int playerPhysical = playerObj.getPhysical();
    private int playerMental = playerObj.getMental();
    private int playerSocial = playerObj.getSocial();
    private int playerClass = playerObj.getPlayerClass();
    private int playerAcrobatics = playerObj.getAcrobatics();
    private int playerBruteForce = playerObj.getBruteforce();
    private int playerBarter = playerObj.getBarter();
    private int playerSpeech = playerObj.getSpeech();
    private int playerAwareness = playerObj.getAwareness();
    private int playerIntuition = playerObj.getIntuition();
    private static String = movedjdwoiasda

    // Break this part up for npc stuff
    private int npcName = NpcObj.getNpcClass();

    public static void Load() {

    }
     */

}
