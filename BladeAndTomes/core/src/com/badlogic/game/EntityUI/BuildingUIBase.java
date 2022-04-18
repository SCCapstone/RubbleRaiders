package com.badlogic.game.EntityUI;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class BuildingUIBase {

    final float Building_Width;
    final float Building_Height;
    final float Loc_X;
    final float Loc_Y;
    final Texture building;
    final SpriteBatch batch;
    final Rectangle Building_Rect;
    boolean isInventoryVisible;
    public BuildingUIBase(float building_Width,
                          float building_Height,
                          float loc_X,
                          float loc_Y,
                          Texture building,
                          SpriteBatch batch) {
        Building_Width = building_Width;
        Building_Height = building_Height;
        Loc_X = loc_X;
        Loc_Y = loc_Y;
        this.building = building;
        this.batch = batch;
        isInventoryVisible = false;
        Building_Rect = new Rectangle(Loc_X,Loc_Y,Building_Width,Building_Height);

    }

    public void render() {
    drawBuilding();
    }
    public void drawBuilding(){
        batch.begin();
        batch.draw(building,
                Building_Rect.x,
                Building_Rect.y,
                Building_Rect.width,
                Building_Rect.height);
        batch.end();
    }
    public Rectangle getBuilding_Rect(){
        return Building_Rect;
    }
}
