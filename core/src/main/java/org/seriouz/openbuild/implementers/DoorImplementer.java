/*
 * Decompiled with CFR 0.152.
 */
package org.seriouz.openbuild.implementers;

import org.seriouz.openbuild.Block;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.managers.BlockPathManager;
import org.seriouz.openbuild.utilities.Logger;

public class DoorImplementer {
    public static void implement(Block host, BlockManager blockManager, String spriteOpen, String spriteClosed) {
        if (host.imageName.contains(spriteClosed)) {
            host.imageName = spriteOpen;
            host.image = blockManager.getBlockPathManager().get(BlockPathManager.getName(spriteOpen));
            host.collidable = false;
        } else if (host.imageName.contains(spriteOpen)) {
            host.imageName = spriteClosed;
            host.image = blockManager.getBlockPathManager().get(BlockPathManager.getName(spriteClosed));
            host.collidable = true;
        }
    }
}
