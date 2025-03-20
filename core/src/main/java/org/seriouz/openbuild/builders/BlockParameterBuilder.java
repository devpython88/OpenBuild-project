/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  box2dLight.RayHandler
 */
package org.seriouz.openbuild.builders;

import box2dLight.RayHandler;
import com.badlogic.gdx.Game;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.managers.SoundManager;
import org.seriouz.openbuild.screens.GameScreen;
import org.seriouz.openbuild.scripts.ScriptManager;

public class BlockParameterBuilder {
    public RayHandler rayHandler;
    public BlockManager blockManager;
    public ScriptManager scriptManager;
    public ScriptBuilder scriptBuilder;
    public SoundManager soundManager;
    public Sunlight sunlight;
    public GameScreen screen;
    public Game game;

    public BlockParameterBuilder(RayHandler rayHandler, BlockManager blockManager, ScriptManager scriptManager, ScriptBuilder scriptBuilder, SoundManager soundManager, Sunlight sunlight, GameScreen screen, Game game) {
        this.rayHandler = rayHandler;
        this.blockManager = blockManager;
        this.scriptManager = scriptManager;
        this.scriptBuilder = scriptBuilder;
        this.soundManager = soundManager;
        this.sunlight = sunlight;
        this.screen = screen;
        this.game = game;
    }

    public static class Sunlight {
        public float v;

        public Sunlight(float v) {
            this.v = v;
        }
    }
}
