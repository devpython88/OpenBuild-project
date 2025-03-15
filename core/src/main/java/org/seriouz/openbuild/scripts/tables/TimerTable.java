package org.seriouz.openbuild.scripts.tables;

import com.badlogic.gdx.Gdx;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

public class TimerTable extends LuaTable {
    private float max, timer;
    private LuaFunction callback;

    public TimerTable(float max, LuaFunction completed){
        this.timer = max;
        this.max = max;
        this.callback = completed;

        set("getTimer", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(timer);
            }
        });

        set("getMax", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(max);
            }
        });

        set("reset", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                timer = max;
                return NIL;
            }
        });

        set("update", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                if (timer <= 0f){
                    callback.call();
                    timer = max;
                    return NIL;
                }

                timer -= Gdx.graphics.getDeltaTime();
                return NIL;
            }
        });
    }
}
