/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  box2dLight.RayHandler
 */
package org.seriouz.openbuild.builders;

import box2dLight.RayHandler;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.managers.SoundManager;
import org.seriouz.openbuild.scripts.ScriptManager;

public class BlockParameterBuilder {
    public RayHandler rayHandler;
    public BlockManager blockManager;
    public ScriptManager scriptManager;
    public ScriptBuilder scriptBuilder;
    public SoundManager soundManager;
    public Sunlight sunlight;

    public BlockParameterBuilder(RayHandler rayHandler, BlockManager blockManager, ScriptManager scriptManager, ScriptBuilder scriptBuilder, Sunlight sunlight, SoundManager soundManager) {
        this.rayHandler = rayHandler;
        this.blockManager = blockManager;
        this.scriptManager = scriptManager;
        this.scriptBuilder = scriptBuilder;
        this.soundManager = soundManager;
        this.sunlight = sunlight;
    }










    // float vars arent referencable
    public static class Sunlight {
        public float v;

        public Sunlight(float v) {
            this.v = v;
        }
    }
}
