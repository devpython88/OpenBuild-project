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
import org.seriouz.openbuild.utilities.Logger;

public class LogInfoFunction
extends OneArgFunction {
    public LuaValue call(LuaValue luaValue) {
        Logger.info(luaValue.tojstring());
        return NIL;
    }
}
