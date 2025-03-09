/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  box2dLight.RayHandler
 */
package org.seriouz.openbuild.builders;

import box2dLight.RayHandler;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.scripts.ScriptManager;

public class BlockParameterBuilder {
    public RayHandler rayHandler;
    public BlockManager blockManager;
    public ScriptManager scriptManager;

    private BlockParameterBuilder(RayHandler rayHandler, BlockManager blockManager, ScriptManager scriptManager) {
        this.rayHandler = rayHandler;
        this.blockManager = blockManager;
        this.scriptManager = scriptManager;
    }

    public static BlockParameterBuilder build(RayHandler rayHandler, BlockManager blockManager, ScriptManager scriptManager) {
        return new BlockParameterBuilder(rayHandler, blockManager, scriptManager);
    }
}
