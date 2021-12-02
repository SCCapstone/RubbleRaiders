package com.badlogic.game.creatures;

import java.util.ArrayList;

public class Inventory {
    public String[] mainInventoryItems;
    ArrayList<Integer> swap;
    public int tradeCurrency;
    public Inventory() {
        mainInventoryItems = new String[19];
        for (int i = 0; i < mainInventoryItems.length; ++i)
            mainInventoryItems[i] = "item " + (i + 1);
        swap = new ArrayList<>();
        tradeCurrency = 0;
    }
    // Put parsers here
    public void updateInventory() {

    }
    // put writer here
    public void saveInventory() {

    }

    public void npcSwapCurrency(int swapIndex, boolean  confirmSwap, int amount ){
        if(confirmSwap){
            mainInventoryItems[swapIndex] = "";
            tradeCurrency +=amount;
            swap.clear();
        }

    }

    public void npcSwapItem(int swapIndex, boolean  confirmSwap, String item ){
        if(confirmSwap){
            mainInventoryItems[swapIndex] = item;
            swap.clear();
        }

    }


    public int[] addSwap(int index) {
        swap.add(index);
        int[] returnArr;
        if (swap.size() == 2) {
            System.out.println(swap.get(0) + " : " + swap.get(1));
            returnArr = new int[]{swap.get(0), swap.get(1)};
            swapInventory(swap.get(0), swap.get(1));
            swap.clear();
        } else {
            returnArr = new int[]{index, index};
        }
        return returnArr;

    }

    public void swapInventory(int index_item1, int index_item2) {
        String item = mainInventoryItems[index_item1];
        mainInventoryItems[index_item1] = mainInventoryItems[index_item2];
        mainInventoryItems[index_item2] = item;
    }


}
