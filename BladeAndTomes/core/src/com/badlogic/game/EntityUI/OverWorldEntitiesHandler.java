package com.badlogic.game.EntityUI;

import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCBuyer;
import ScreenOverlayRework.Inventory.NPCInventoryUI.NPCSeller;
import ScreenOverlayRework.Inventory.NPCInventoryUI.TownHallQuestBoard;
import com.badlogic.game.BladeAndTomes;
import com.badlogic.game.screens.Dungeon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;


public class OverWorldEntitiesHandler implements Disposable {
    BladeAndTomes game;
    PlayerEnitityUI player;
    Array<GoblinEnitityUI> goblins;
    QuestBordBuilding questBord;
    SpriteBatch batch;
    OrthographicCamera camera;
    TownHallQuestBoard quests;
    TownHallBuilding buyerBuilding;
    NPCBuyer buyer;
    NPCSeller seller;
    MarketBulding sellerBuilding;
    BuildingUIBase portalBuilding;
    BuildingUIBase CyanTavernsBuilding;
    BuildingUIBase PurpleChapelsBuilding;
    boolean isInventoryVisible;

    public OverWorldEntitiesHandler(BladeAndTomes game) {
        this.game = game;
        isInventoryVisible = false;
        load_assets();
        quests = game.overlays.generateQuestBoard();
        buyer = game.overlays.generateNewNPCBuyer();
        seller = game.overlays.generateNewNPSeller();
        camera = (OrthographicCamera) game.stageInstance.getCamera();
        player = new PlayerEnitityUI((OrthographicCamera) game.stageInstance.getCamera(), game.batch, game.controls, 960, 540, 50, 50, game.moveUpAnimation, game.moveDownAnimation, game.moveLeftAnimation, game.moveRightAnimation);

        // Contains All goblins
        goblins = new Array<>();

        questBord = new QuestBordBuilding(game.assets.get("Maps/Quest_Board.png", Texture.class).getWidth(), game.assets.get("Maps/Quest_Board.png", Texture.class).getHeight(), 925, 675, game.assets.get("Maps/Quest_Board.png", Texture.class), game.batch, game.stageInstance, quests, game.controls, game.overlays, game.BaseLabelStyle2);
        buyerBuilding = new TownHallBuilding(game.assets.get("Maps/Barracks_Range_Melee.png", Texture.class).getWidth(), game.assets.get("Maps/Barracks_Range_Melee.png", Texture.class).getHeight(), 125, 740, game.assets.get("Maps/Barracks_Range_Melee.png", Texture.class), game.batch, game.stageInstance, buyer, game.controls, game.overlays, game.BaseLabelStyle2);
        sellerBuilding = new MarketBulding(game.assets.get("Maps/Market_Revamped.png", Texture.class).getWidth(), game.assets.get("Maps/Market_Revamped.png", Texture.class).getHeight(), 715, 320, game.assets.get("Maps/Market_Revamped.png", Texture.class), game.batch, game.stageInstance, seller, game.controls, game.overlays, game.BaseLabelStyle2);
        portalBuilding = new BuildingUIBase(game.assets.get("Maps/Portal_Revamped.png", Texture.class).getWidth(), game.assets.get("Maps/Portal_Revamped.png", Texture.class).getHeight(), 925, 100, game.assets.get("Maps/Portal_Revamped.png", Texture.class), game.batch);
        CyanTavernsBuilding = new BuildingUIBase(game.assets.get("Maps/CyanTaverns_Revamped.png", Texture.class).getWidth(), game.assets.get("Maps/CyanTaverns_Revamped.png", Texture.class).getHeight(), 1450, 800, game.assets.get("Maps/CyanTaverns_Revamped.png", Texture.class), game.batch);
        PurpleChapelsBuilding = new BuildingUIBase(game.assets.get("Maps/PurpleChapels_Revamped.png", Texture.class).getWidth(), game.assets.get("Maps/PurpleChapels_Revamped.png", Texture.class).getHeight(), 675, 800, game.assets.get("Maps/PurpleChapels_Revamped.png", Texture.class), game.batch);
    }

    public void load_assets() {
        if (!game.assets.contains("Maps/Quest_Board.png", Texture.class)) {
            game.assets.load("Maps/Quest_Board.png", Texture.class);
        }
        if (!game.assets.contains("Maps/Barracks_Range_Melee.png", Texture.class)) {
            game.assets.load("Maps/Barracks_Range_Melee.png", Texture.class);
        }
        if (!game.assets.contains("Maps/Market_Revamped.png", Texture.class)) {
            game.assets.load("Maps/Market_Revamped.png", Texture.class);
        }
        if (!game.assets.contains("Maps/Portal_Revamped.png", Texture.class)) {
            game.assets.load("Maps/Portal_Revamped.png", Texture.class);
        }
        if (!game.assets.contains("Maps/CyanTaverns_Revamped.png", Texture.class)) {
            game.assets.load("Maps/CyanTaverns_Revamped.png", Texture.class);
        }
        if (!game.assets.contains("Maps/PurpleChapels_Revamped.png", Texture.class)) {
            game.assets.load("Maps/PurpleChapels_Revamped.png", Texture.class);
        }
        game.assets.finishLoading();
    }

    public void render(float delta) {
        player.render(delta);
        camera.update();
        checkPlayerEnterPortal();
        if (checkPlayerCollision()) {
            player.resetToPreviousPosition();
            player.cantMoveInCurrentDirection = true;
        }

        CyanTavernsBuilding.render();
        portalBuilding.render();
        questBord.render();
        buyerBuilding.render();
        sellerBuilding.render();
        PurpleChapelsBuilding.render();
        if (Gdx.input.isKeyJustPressed(game.controls.getOpenInventory())) {
            isInventoryVisible = !isInventoryVisible;
            game.overlays.setHiddenTableVisibility(isInventoryVisible);
        }
    }

    public void checkPlayerEnterPortal() {
        if (player.isColliding(portalBuilding.Building_Rect)) {
            game.stageInstance.clear();
            BladeAndTomes.enterDungeon = true;
            BladeAndTomes.exitDungeon = false;
            game.setScreen(new Dungeon(game));
        }
    }

    public boolean checkPlayerCollision() {
        for (int i = 0; i < goblins.size; ++i) {
            Rectangle goblinBig = new Rectangle(goblins.get(i).Entity_Rect.x, goblins.get(i).Entity_Rect.y, goblins.get(i).Entity_Rect.width - 15, goblins.get(i).Entity_Rect.height - 15);
            if (player.isColliding(goblinBig)) return true;
        }
        if (player.isColliding(questBord.Building_Rect) || player.isColliding(buyerBuilding.Building_Rect) || player.isColliding(sellerBuilding.Building_Rect) || player.isColliding(CyanTavernsBuilding.Building_Rect) || player.isColliding(PurpleChapelsBuilding.Building_Rect))
            return true;
        else {
            questBord.isPlayerNear = nearBuilding(questBord.Building_Rect);
            buyerBuilding.isPlayerNear = nearBuilding(buyerBuilding.Building_Rect);
            sellerBuilding.isPlayerNear = nearBuilding(sellerBuilding.Building_Rect);
        }
        return false;
    }

    public boolean nearBuilding(Rectangle rectangle) {
        Rectangle angle = new Rectangle(rectangle);
        angle.setPosition(angle.x, angle.y - 32);
        angle.setSize(angle.width, angle.height + 32);
        return player.Entity_Rect.overlaps(angle);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
