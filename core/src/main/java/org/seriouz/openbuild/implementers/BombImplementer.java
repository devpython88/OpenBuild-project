/*
 * Decompiled with CFR 0.152.
 */
package org.seriouz.openbuild.implementers;

import java.util.ArrayList;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import org.seriouz.openbuild.Block;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.managers.SoundManager;

public class BombImplementer {
    public static void implement(BlockManager blockManager, Block host, SoundManager soundManager) {
        soundManager.play(host.imageName.replace("./resources/blocks/", "").replace(".png", ""));
        blockManager.destroyBlock(host.x, host.y);
        ArrayList<Block> blocksToRemove = new ArrayList<Block>();
        ArrayList<Block> dynamitesToTrigger = new ArrayList<Block>();

        Circle interactionCircle = new Circle(host.x, host.y, 60);

        for (Block block : blockManager.blocks){
            if (Intersector.overlaps(interactionCircle, new Rectangle(block.x, block.y, 16, 16))){
                if (block.isBomb(block.imageName)){
                    dynamitesToTrigger.add(block);
                    continue;
                }

                blocksToRemove.add(block);
            }
        }

        for (Block dynamiteToTrigger : dynamitesToTrigger){
            dynamiteToTrigger.interact(blockManager, soundManager);
        }

        for (Block block : blocksToRemove){
            block.dispose();
        }

        blockManager.blocks.removeAll(blocksToRemove);
    }
}
