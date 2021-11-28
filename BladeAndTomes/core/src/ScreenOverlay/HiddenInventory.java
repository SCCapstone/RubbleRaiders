package ScreenOverlay;

import com.badlogic.game.BladeAndTomes;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class HiddenInventory {
    final BladeAndTomes game;
    private Stage stage;
    public HiddenInventory(final BladeAndTomes GAME){
        game = GAME;
        stage = game.stageInstance;
    }
    public void makeInventory(){
//        stage.setDebugAll(true);
//        Table table = new Table();
////        // add items to first row
//        stage.addActor(table);
//
//        Label label1 = new Label("Acrobatics: " + game.player.getAcrobatics(), game.generalLabelStyle);
//        label1.setAlignment(1);
//        label1.setHeight(90);
//        label1.setX(200,1);
//        label1.setY(900,1);
//        table.add(label1);
//        table.pack();
////
//        table.row(); // start next row
//        table.add("Hellow");

//        Skin skin = new Skin();
////        List<String> inventory = new List<>(skin);
////        inventory.setItems("Axe","Text","Other");
//        skin.add("logo", new Texture("Text_Button_Down_State.jpg"));
//        Table table = new Table(skin);
//        table.setFillParent(true);
//
//        table.defaults();
//        table.add("Inventory");
//        table.add("Merchants").row();
//        table.add(inventory).expand().fill();

    }
}
