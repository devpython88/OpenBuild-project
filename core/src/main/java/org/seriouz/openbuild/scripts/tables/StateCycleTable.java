package org.seriouz.openbuild.scripts.tables;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.util.List;

public class StateCycleTable extends LuaTable {
    List<String> states;
    int index;


    public StateCycleTable(List<String> states){
        this.states = states;
        this.index = 0;
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
                    tempThis.index = 0;
                }
                return NIL;
            }
        });
    }
}
