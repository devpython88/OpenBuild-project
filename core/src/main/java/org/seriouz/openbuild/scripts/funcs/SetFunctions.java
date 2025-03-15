/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.luaj.vm2.LuaValue
 *  org.luaj.vm2.lib.OneArgFunction
 */
package org.seriouz.openbuild.scripts.funcs;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.seriouz.openbuild.Block;
import org.seriouz.openbuild.utilities.Logger;
import org.seriouz.openbuild.managers.BlockManager;

public class SetFunctions {

    public static class SetImageName
    extends OneArgFunction {
        Block host;
        BlockManager blockManager;

        public SetImageName(Block host, BlockManager blockManager) {
            this.host = host;
            this.blockManager = blockManager;
        }

        public LuaValue call(LuaValue name) {
            String jstrName = name.tojstring();
            if (!this.blockManager.getBlockPathManager().blockMap.containsKey(jstrName)) {
                Logger.err("No matching block texture file found: " + jstrName, "setImageName");
                return NIL;
            }
            this.host.imageName = jstrName.replace(".png", "");
            this.host.image = this.blockManager.getBlockPathManager().get(jstrName);
            return NIL;
        }
    }



    public static class SetCollision extends OneArgFunction {
        Block host;

        public SetCollision(Block host) {
            this.host = host;
        }

        @Override
        public LuaValue call(LuaValue luaValue) {
            host.collidable = luaValue.toboolean();
            return NIL;
        }
    }
}
