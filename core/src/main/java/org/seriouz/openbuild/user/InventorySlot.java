package org.seriouz.openbuild.user;

import com.badlogic.gdx.graphics.Texture;

public class InventorySlot {
    public Texture getItem() {
        return item;
    }

    public String getItemName() {
        return itemName;
    }

    Texture item;

    public void setItem(Texture item) {
        this.item = item;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    String itemName;

    public InventorySlot(Texture item, String itemName) {
        this.item = item;
        this.itemName = itemName;
    }
}
