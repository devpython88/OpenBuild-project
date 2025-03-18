package org.seriouz.openbuild.user;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.seriouz.openbuild.managers.BlockPathManager;

import java.util.ArrayList;

public class Inventory {
    public ArrayList<InventorySlot> getSlots() {
        return slots;
    }

    private ArrayList<InventorySlot> slots;
    private Player host;
    private Texture emptySlotTexture;
    private int slot;

    public Inventory(Player host){
        this.host = host;
        slots = new ArrayList<>();

        Texture selectedTexture = getBlockPathManager().get(getBlockPathManager().blockPaths.get(0));
        emptySlotTexture = new Texture("resources/UI/Empty Slot.png");

        createSlots(selectedTexture);
    }

    private void createSlots(Texture selectedTexture) {
        slots.add(new InventorySlot(getBlockPathManager().get("Wood Log"), "Wood Log"));
        slots.add(new InventorySlot(getBlockPathManager().get("Stone Bricks"), "Stone Bricks"));
        slots.add(new InventorySlot(getBlockPathManager().get("Cut Wood"), "Cut Wood"));
        slots.add(new InventorySlot(getBlockPathManager().get("Door"), "Door"));
    }

    private BlockPathManager getBlockPathManager() {
        return host.paramBuilder.blockManager.getBlockPathManager();
    }

    public void draw(SpriteBatch batch, int startX, int startY, int w, int h){
        int spacing = 8;

        drawSlot(1, batch, startX, startY, spacing, w, h);
        drawSlot(2, batch, startX, startY, spacing, w, h);
        drawSlot(3, batch, startX, startY, spacing, w, h);
        drawSlot(4, batch, startX, startY, spacing, w, h);
    }

    private void drawSlot(int slotToDraw, SpriteBatch batch, int sX, int sY, int spacing, int w, int h){
        int x = sX + ((w + spacing) * slotToDraw);

        Texture slotTexture = slots.get(slotToDraw - 1).item;

        if (slotToDraw - 1 == slot){
            batch.draw(
                new TextureRegion(slotTexture, 16, 16),
                x, sY + 4, w, h
            );

            return;
        }

        batch.draw(new TextureRegion(slotTexture, 16, 16), x, sY, w, h);
    }

    public InventorySlot getCurrentSlot(){
        return slots.get(slot);
    }

    public void handle(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            slot = 0;
            updateSlotInformation();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            slot = 1;
            updateSlotInformation();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            slot = 2;
            updateSlotInformation();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)){
            slot = 3;
            updateSlotInformation();
        }
    }

    public void setSlot(int i, InventorySlot slot){
        slots.set(i, slot);
        updateSlotInformation();
    }

    private void updateSlotInformation() {
        host.paramBuilder.blockManager.currentSelected = getCurrentSlot().itemName;
        getCurrentSlot().setItem(getBlockPathManager().get(
            host.paramBuilder.blockManager.currentSelected
        ));

        getCurrentSlot().setItemName(
            host.paramBuilder.blockManager.currentSelected
        );
    }
}
