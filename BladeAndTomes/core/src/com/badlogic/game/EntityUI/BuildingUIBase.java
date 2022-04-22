package com.badlogic.game.EntityUI;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class BuildingUIBase {

    // The Building Width variable.
    final float Building_Width;
    // The Building Height variable.
    final float Building_Height;
    // The Location of the building in X and Y Coordinates system. Usually,this is the location of the very first pixel in bottom left of the Texture.
    final float Loc_X;
    final float Loc_Y;
    // The Texture of the building, this is mainly used for drawing purposes only.
    final Texture building;
    // A SpriteBatch to draw textures.
    final SpriteBatch batch;
    // A Rectangle object to keep up the location as well as size of the building.
    final Rectangle Building_Rect;
    // Variable to see if inventory from overlays is visible
    boolean isInventoryVisible;

    /**
     * The purpose of this constructor is to get and set all default values for any building.
     *
     * @param building_Width  The width of this building.
     * @param building_Height The height of this building.
     * @param loc_X           The zero coordinate of the building in terms of x-axis.
     * @param loc_Y           The zero coordinate of the building in terms of y-axis.
     * @param building        The Texture of this building for drawing purposes.
     * @param batch           A Sprite Batch which is mainly used for drawing the building texture.
     *                        *** Needs to be separate one from the stage else will cause issues with listeners. ***
     */
    public BuildingUIBase(float building_Width, float building_Height, float loc_X, float loc_Y, Texture building, SpriteBatch batch) {
        Building_Width = building_Width;
        Building_Height = building_Height;
        Loc_X = loc_X;
        Loc_Y = loc_Y;
        this.building = building;
        this.batch = batch;
        isInventoryVisible = false;
        Building_Rect = new Rectangle(Loc_X, Loc_Y, Building_Width, Building_Height);

    }

    /**
     * @Functionality The purpose of this method is that it called every iteration of the game for drawing Textures.
     * This method usually gets override by the child.
     */
    public void render() {
        drawBuilding();
    }

    /**
     * @Functionality The purpose of this method is that it called every iteration of the game for drawing Textures using Sprite Batch.
     */
    public void drawBuilding() {
        batch.begin();
        batch.draw(building, Building_Rect.x, Building_Rect.y, Building_Rect.width, Building_Rect.height);
        batch.end();
    }
}
