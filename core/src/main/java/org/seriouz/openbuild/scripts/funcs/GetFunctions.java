/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.luaj.vm2.LuaValue
 *  org.luaj.vm2.lib.OneArgFunction
 *  org.luaj.vm2.lib.ZeroArgFunction
 */
package org.seriouz.openbuild.scripts.funcs;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.seriouz.openbuild.Block;
import org.seriouz.openbuild.managers.BlockPathManager;
import org.seriouz.openbuild.utilities.Logger;

public class GetFunctions {

    public static class CheckTypeFunction
        extends OneArgFunction {
        Block block;

        public CheckTypeFunction(Block block) {
            this.block = block;
        }

        public LuaValue call(LuaValue type) {
            return LuaValue.valueOf(switch (type.tojstring().toLowerCase()) {
                case "door" -> this.block.isDoor(this.block.imageName);
                case "bomb" -> this.block.isBomb(this.block.imageName);
                case "lamp" -> this.block.isLamp(this.block.imageName);
                default -> {
                    Logger.err("Unknown block type '" + type.tojstring() + "' (non case-sensitive).", "isBlockA");
                    yield false;
                }
            });
        }
    }

    public static class GetImageName
        extends ZeroArgFunction {
        Block block;

        public GetImageName(Block block) {
            this.block = block;
        }

        public LuaValue call() {
            return LuaValue.valueOf(BlockPathManager.getName(block.imageName));
        }
    }

    public static class GetImagePath
        extends ZeroArgFunction {
        Block block;

        public GetImagePath(Block block) {
            this.block = block;
        }

        public LuaValue call() {
            return LuaValue.valueOf(this.block.imageName);
        }
    }


    public static class GetCollision extends ZeroArgFunction {
        Block host;

        public GetCollision(Block host) {
            this.host = host;
        }

        @Override
        public LuaValue call() {
            return LuaValue.valueOf(host.collidable);
        }
    }
}
