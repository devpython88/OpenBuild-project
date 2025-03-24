/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.badlogic.gdx.Gdx
 *  com.badlogic.gdx.graphics.Texture
 *  com.badlogic.gdx.graphics.g2d.SpriteBatch
 */
package org.seriouz.openbuild.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import org.seriouz.openbuild.Block;
import org.seriouz.openbuild.builders.BlockParameterBuilder;

public class BlockManager {
    public List<Block> blocks = new ArrayList<Block>();
    public String currentSelected;
    public int selectedIndex;
    BlockPathManager blockPathManager = new BlockPathManager();

    public BlockManager() {
        selectedIndex = 0;
        this.currentSelected = this.blockPathManager.blockPaths.get(this.selectedIndex);

        if (currentSelected.startsWith("_") || currentSelected.startsWith("-")) nextSelectedBlock();
    }

    public void drawBlocks(SpriteBatch batch, int playerX, int playerY, SoundManager soundManager) {
        int maxDrawDistance = 512;
        try {
            for (Block block : this.blocks) {
                if (block.x <= playerX - (maxDrawDistance + 16) || block.x >= playerX + maxDrawDistance || block.y <= playerY - maxDrawDistance || block.y >= playerY + maxDrawDistance) continue;
                block.handle(this, playerX, playerY, soundManager);
                block.draw(batch);
            }
        }
        catch (ConcurrentModificationException concurrentModificationException) {
            // empty catch block
        }
    }

    public void createBlock(int x, int y, Texture texture, String name, BlockParameterBuilder builder) {
        if (blockExists(x, y)){
            return;
        }

        Block block = new Block(x, y, name, texture, builder);
        this.blocks.add(block);
    }

    public void createBlock(int x, int y, BlockParameterBuilder builder) {
        this.createBlock(x, y, this.blockPathManager.get(this.currentSelected), this.currentSelected, builder);
    }

    public void destroyBlock(int x, int y) {
        for (int i = this.blocks.size() - 1; i >= 0; --i) {
            Block block = this.blocks.get(i);
            if (block.x != x || block.y != y) continue;
            block.dispose();
            this.blocks.remove(i);
        }
    }

    public boolean blockExists(int x, int y) {
        for (Block block : this.blocks) {
            if (block.x != x || block.y != y || !block.collidable) continue;
            return true;
        }
        return false;
    }

    public Block getBlock(int x, int y) {
        for (Block block : this.blocks) {
            if (block.x != x || block.y != y) continue;
            return block;
        }
        return null;
    }

    public void updateCurrentSelected() {
        this.currentSelected = this.blockPathManager.getPaths().get(this.selectedIndex);
    }

    public void dispose() {
        this.blockPathManager.dispose();
    }

    public BlockPathManager getBlockPathManager() {
        return this.blockPathManager;
    }

    public void handle() {
        if (Gdx.input.isKeyJustPressed(61)) {
            this.nextSelectedBlock();
        }
    }

    private void nextSelectedBlock() {
        ++this.selectedIndex;
        if (this.selectedIndex >= this.blockPathManager.blockPaths.size()) {
            this.selectedIndex = 0;
        }
        this.updateCurrentSelected();
        if (this.currentSelected.startsWith("_") || currentSelected.startsWith("-")) {
            this.nextSelectedBlock();
        }
    }


    public boolean isBlockInRange(Circle range, String filter){
        for (Block b : blocks){
            if (Intersector.overlaps(range, new Rectangle(b.x, b.y, 16, 16)) &&
                blockPathManager.getName(b.imageName).equals(filter)) return true;
        }
        return false;
    }

    public boolean isBlockInRange(Circle range, int radius){
        for (Block b : blocks){
            if (Intersector.overlaps(range, new Rectangle(b.x, b.y, 16, 16))) return true;
        }
        return false;
    }
}
