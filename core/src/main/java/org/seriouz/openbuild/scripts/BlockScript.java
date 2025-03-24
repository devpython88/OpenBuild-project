/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.luaj.vm2.LuaValue
 *  org.luaj.vm2.lib.OneArgFunction
 *  org.luaj.vm2.lib.ZeroArgFunction
 */
package org.seriouz.openbuild.scripts;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.seriouz.openbuild.Block;
import org.seriouz.openbuild.builders.ScriptBuilder;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.scripts.funcs.GetFunctions;
import org.seriouz.openbuild.scripts.funcs.MiscFunctions;
import org.seriouz.openbuild.scripts.funcs.SetFunctions;
import org.seriouz.openbuild.scripts.tables.TimerTable;

public class BlockScript
    extends Script {
    public Block host;

    public BlockScript(String path, Block host, BlockManager blockManager, ScriptBuilder scriptBuilder) {
        super(path, scriptBuilder);
        this.host = host;
        this.bindBlockMethods(blockManager);
    }

    public void blockLoaded() {
        if (this.hasEventFunc("blockLoaded")) {
            this.executeEventFunc("blockLoaded");
        }
    }

    public void blockDestroyed() {
        if (this.hasEventFunc("blockDestroyed")) {
            this.executeEventFunc("blockDestroyed");
        }
    }

    public void blockDrawn() {
        if (this.hasEventFunc("blockDrawn")) {
            this.executeEventFunc("blockDrawn");
        }
    }

    public void interacted() {
        if (this.hasEventFunc("interacted")) {
            this.executeEventFunc("interacted");
        }
    }

    public void bindBlockMethods(BlockManager blockManager) {

        this.globals.set("getImageName", new GetFunctions.GetImageName(this.host));
        this.globals.set("isBlockA", new GetFunctions.CheckTypeFunction(this.host));
        this.globals.set("getX", new ZeroArgFunction() {

            public LuaValue call() {
                return LuaValue.valueOf(BlockScript.this.host.x / 16);
            }
        });
        this.globals.set("getY", new ZeroArgFunction() {
            public LuaValue call() {
                return LuaValue.valueOf(BlockScript.this.host.y / 16);
            }
        });
        this.globals.set("getCollision", new GetFunctions.GetCollision(this.host));

        // MISC
        this.globals.set("isBlockNearby", MiscFunctions.isBlockNearby(blockManager, host));

        this.globals.set("setImageName", new SetFunctions.SetImageName(this.host, blockManager));
        this.globals.set("setCollision", new SetFunctions.SetCollision(this.host));
        this.globals.set("setX", new OneArgFunction() {

            public LuaValue call(LuaValue luaValue) {
                BlockScript.this.host.x = luaValue.toint() * 16;
                return NIL;
            }
        });
        this.globals.set("setY", new OneArgFunction() {

            public LuaValue call(LuaValue luaValue) {
                BlockScript.this.host.y = luaValue.toint() * 16;
                return NIL;
            }
        });
    }
}
