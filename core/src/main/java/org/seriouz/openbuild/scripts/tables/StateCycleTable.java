package org.seriouz.openbuild.scripts.tables;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.util.List;

public class StateCycleTable extends LuaTable {
    private boolean loop;
    List<String> states;
    int index;


    public StateCycleTable(List<String> states){
        this.states = states;
        this.index = 0;
        this.loop = true;
        StateCycleTable tempThis = this;

        set("getCurrent", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(states.get(tempThis.index));
            }
        });

        set("cycle", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                tempThis.index++;
                if (tempThis.index >= states.size()){
                    if (loop) {
                        tempThis.index = 0;
                        return NIL;
                    }

                    tempThis.index = states.size() - 1;
                }
                return NIL;
            }
        });

        set("setLoop", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue luaValue) {
                tempThis.loop = luaValue.toboolean();
                return NIL;
            }
        });
    }
}
