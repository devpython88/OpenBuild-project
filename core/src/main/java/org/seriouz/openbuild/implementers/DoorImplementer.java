/*
 * Decompiled with CFR 0.152.
 */
package org.seriouz.openbuild.implementers;

import org.seriouz.openbuild.Block;
import org.seriouz.openbuild.managers.BlockManager;

public class DoorImplementer {
    public static void implement(Block host, BlockManager blockManager, String spriteOpen, String spriteClosed) {
        if (host.imageName.equals(spriteClosed)) {
            host.imageName = spriteOpen;
            host.image = blockManager.getBlockPathManager().blockMap.get(host.imageName);
            host.collidable = false;
            return;
        }
        host.imageName = spriteClosed;
        host.image = blockManager.getBlockPathManager().blockMap.get(host.imageName);
        host.collidable = true;
    }
}
