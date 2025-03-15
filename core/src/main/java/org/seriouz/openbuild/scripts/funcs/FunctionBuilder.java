package org.seriouz.openbuild.scripts.funcs;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public class FunctionBuilder {

    @FunctionalInterface
    public interface NoArg {
        public LuaValue call();
    }

    @FunctionalInterface
    public interface TwoArg {
        public LuaValue call(LuaValue a, LuaValue b);
    }

    @FunctionalInterface
    public interface OneArg {
        public LuaValue call(LuaValue a);
    }

    @FunctionalInterface
    public interface MultiArg {
        public LuaValue call(Varargs varargs);
    }


    public static ZeroArgFunction zeroArg(NoArg func){
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return func.call();
            }
        };
    }

    public static OneArgFunction oneArg(OneArg func){
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue a) {
                return func.call(a);
            }
        };
    }

    public static TwoArgFunction twoArg(TwoArg func){
        return new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue a, LuaValue b) {
                return func.call(a, b);
            }
        };
    }

    public static VarArgFunction multiArg(MultiArg func){
        return new VarArgFunction() {
            @Override
            public LuaValue invoke(Varargs varargs) {
                return func.call(varargs);
            }
        };
    }

}
