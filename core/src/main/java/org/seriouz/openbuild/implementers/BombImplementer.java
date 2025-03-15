/*
 * Decompiled with CFR 0.152.
 */
package org.seriouz.openbuild.implementers;

import java.util.ArrayList;
import org.seriouz.openbuild.Block;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.managers.SoundManager;

public class BombImplementer {
    public static void implement(BlockManager blockManager, Block host, SoundManager soundManager) {
        soundManager.play(host.imageName.replace("./resources/blocks/", "").replace(".png", ""));
        blockManager.destroyBlock(host.x, host.y);
        ArrayList<Block> blocksToRemove = new ArrayList<Block>();
        ArrayList<Block> dynamitesToTrigger = new ArrayList<Block>();
        for (Block block : blockManager.blocks) {
            if (!(block.x == host.x + 16 && block.y == host.y
                || block.x == host.x - 16 && block.y == host.y
                || block.x == host.x && block.y == host.y - 16)
                && (block.x != host.x || block.y != host.y + 16)) continue;
            if (block.isBomb(block.imageName)) {
                dynamitesToTrigger.add(block);
                continue;
            }
            block.dispose();
            blocksToRemove.add(block);
        }
        for (Block block : dynamitesToTrigger) {
            block.interact(blockManager, soundManager);
        }
        blockManager.blocks.removeAll(blocksToRemove);
    }
}
