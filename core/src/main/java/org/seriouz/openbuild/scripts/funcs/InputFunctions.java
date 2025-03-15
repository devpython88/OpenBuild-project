/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.badlogic.gdx.Gdx
 *  org.luaj.vm2.LuaValue
 *  org.luaj.vm2.lib.OneArgFunction
 */
package org.seriouz.openbuild.scripts.funcs;

import com.badlogic.gdx.Gdx;
import java.util.HashMap;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.seriouz.openbuild.utilities.Logger;

public class InputFunctions {
    public static final HashMap<String, Integer> keyMap = new HashMap<String, Integer>(){
        {
            this.put("a", 29);
            this.put("b", 30);
            this.put("c", 31);
            this.put("d", 32);
            this.put("e", 33);
            this.put("f", 34);
            this.put("g", 35);
            this.put("h", 36);
            this.put("i", 37);
            this.put("j", 38);
            this.put("k", 39);
            this.put("l", 40);
            this.put("m", 41);
            this.put("n", 42);
            this.put("o", 43);
            this.put("p", 44);
            this.put("q", 45);
            this.put("r", 46);
            this.put("s", 47);
            this.put("t", 48);
            this.put("u", 49);
            this.put("v", 50);
            this.put("w", 51);
            this.put("x", 52);
            this.put("y", 53);
            this.put("z", 54);
            this.put("0", 7);
            this.put("1", 8);
            this.put("2", 9);
            this.put("3", 10);
            this.put("4", 11);
            this.put("5", 12);
            this.put("6", 13);
            this.put("7", 14);
            this.put("8", 15);
            this.put("9", 16);
            this.put("left shift", 59);
            this.put("left ctrl", 129);
            this.put("left alt", 57);
            this.put("right shift", 60);
            this.put("right ctrl", 130);
            this.put("right alt", 58);
            this.put("escape", 111);
            this.put("tab", 61);
            this.put("enter", 66);
            this.put("space", 62);
            this.put("win", 63);
            this.put("F1", 131);
            this.put("F2", 132);
            this.put("F3", 133);
            this.put("F4", 134);
            this.put("F5", 135);
            this.put("F6", 136);
            this.put("F7", 137);
            this.put("F8", 138);
            this.put("F9", 139);
            this.put("F10", 140);
            this.put("F11", 141);
            this.put("F12", 142);
            this.put("up", 19);
            this.put("down", 20);
            this.put("left", 21);
            this.put("right", 22);
        }
    };

    public static class GetKeyPressed
    extends OneArgFunction {
        public LuaValue call(LuaValue key) {
            if (!keyMap.containsKey(key.tojstring())) {
                Logger.err("The key could not be found: " + key.tojstring(), "GetKeyHeld");
                return LuaValue.valueOf((boolean)false);
            }
            return LuaValue.valueOf((boolean)Gdx.input.isKeyJustPressed(keyMap.get(key.tojstring()).intValue()));
        }
    }

    public static class GetKeyHeld
    extends OneArgFunction {
        public LuaValue call(LuaValue key) {
            if (!keyMap.containsKey(key.tojstring())) {
                Logger.err("The key could not be found: " + key.tojstring(), "GetKeyHeld");
                return LuaValue.valueOf((boolean)false);
            }
            return LuaValue.valueOf((boolean)Gdx.input.isKeyPressed(keyMap.get(key.tojstring()).intValue()));
        }
    }
}
