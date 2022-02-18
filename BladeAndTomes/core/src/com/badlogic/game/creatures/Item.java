package com.badlogic.game.creatures;

import jdk.jfr.Category;

import java.io.Serializable;

public class Item implements Serializable {
    private String name;
    private String description;
    private enum Category {armor, weapon, buff};
    private int slot;
    private int value;
    private Category itemType;

    public Item() {
        this.name = "Item name";
        this.description = "Some sort of Item here";
        this.slot = 1;
        this.value = 1;
    }

    public Item(String name, String description, Category category, int slot, int value) {
        this.name = name;
        this.description = description;
        this.itemType = category;
        this.slot = slot;
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getSlot() {
        return slot;
    }
    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

    public Category getItemType() {
        return itemType;
    }
    public void setItemType(Category input) {
        this.itemType = input;
    }

}
