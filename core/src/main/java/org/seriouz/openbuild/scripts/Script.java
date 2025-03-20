/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  org.luaj.vm2.Globals
 *  org.luaj.vm2.LuaValue
 *  org.luaj.vm2.lib.jse.JsePlatform
 */
package org.seriouz.openbuild.scripts;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.seriouz.openbuild.builders.ScriptBuilder;
import org.seriouz.openbuild.scripts.funcs.FunctionBuilder;
import org.seriouz.openbuild.scripts.funcs.MiscFunctions;
import org.seriouz.openbuild.scripts.tables.ui.ButtonTable;
import org.seriouz.openbuild.scripts.tables.ui.TextViewTable;
import org.seriouz.openbuild.utilities.Logger;
import org.seriouz.openbuild.scripts.funcs.InputFunctions;
import org.seriouz.openbuild.scripts.funcs.LogInfoFunction;
import org.seriouz.openbuild.scripts.tables.FileSystem;
import org.seriouz.openbuild.scripts.tables.StateCycleTable;
import org.seriouz.openbuild.scripts.tables.TimerTable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Script {
    public Globals globals;
    public String contents;
    public ScriptBuilder builder;

    public Script() {
    }

    public Script(String path, ScriptBuilder builder) {
        this.builder = builder;
        try {
            this.globals = JsePlatform.standardGlobals();
            this.contents = Files.readString(Path.of(path));
            this.bindMethods();
            this.globals.load(this.contents).call();
        } catch (IOException e) {
            Logger.info("Couldn't read script: " + path);
        }
    }

    public void bindMethods() {
        this.globals.set("logInfo", new LogInfoFunction());
        this.globals.set("isKeyHeld", new InputFunctions.GetKeyHeld());
        this.globals.set("isKeyPressed", new InputFunctions.GetKeyPressed());
        this.globals.set("playSound", MiscFunctions.playSound(builder.soundManager));

        // tables

        // tables/Timer

        initializeTimerTable();

        // tables/StateCycle

        initializeStateCycleTable();

        // tables/FileSystem

        globals.set("FileSystem", new FileSystem());

        // tables/UI

        initializeUNamespace();
    }

    private void initializeUNamespace() {
        LuaTable uiNamespace = new LuaTable();
        globals.set("UI", uiNamespace);

        LuaTable buttonTable = getButtonTable_UI();
        LuaTable textViewTable = getTextViewTable_UI();

        uiNamespace.set("TextView", textViewTable);
        uiNamespace.set("Button", buttonTable);
    }

    private LuaTable getTextViewTable_UI() {
        LuaTable textViewTable = new LuaTable();
        textViewTable.set("ready", FunctionBuilder.multiArg(args -> new TextViewTable(
            builder.mainSkin,
            builder.mainStage,
            args.arg(2).tofloat(),
            args.arg(3).tofloat(),
            args.arg(1).tojstring()
        )));
        return textViewTable;
    }

    private LuaTable getButtonTable_UI() {
        LuaTable buttonTable = new LuaTable();
        buttonTable.set("ready", FunctionBuilder.multiArg(varargs -> new ButtonTable(
            builder.mainSkin,
            builder.mainStage,
            varargs.arg(1).tojstring(),
            varargs.arg(4).checkfunction(),
            varargs.arg(2).tofloat(),
            varargs.arg(3).tofloat())));
        return buttonTable;
    }

    private void initializeStateCycleTable() {
        LuaTable cycleTable = new LuaTable();
        cycleTable.set("ready", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                List<String> states = new ArrayList<>();

                for (int i = 1; i <= varargs.narg(); i++){
                    states.add(varargs.arg(i).tojstring());
                }

                return new StateCycleTable(states);
            }
        });
        globals.set("StateCycle", cycleTable);
    }

    private void initializeTimerTable() {
        LuaTable timerTable = new LuaTable();
        timerTable.set("ready", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue luaValue, LuaValue luaValue1) {
                return new TimerTable(luaValue.tofloat(), luaValue1.checkfunction());
            }
        });
        globals.set("Timer", timerTable);
    }

    protected boolean hasEventFunc(String name) {
        return this.globals.get(name).isfunction();
    }

    protected void executeEventFunc(String name) {
        this.globals.get(name).call();
    }

    public void scriptLoaded() {
        if (this.hasEventFunc("scriptLoaded")) {
            this.executeEventFunc("scriptLoaded");
        }
    }

    public void scriptDestroyed() {
        if (this.hasEventFunc("scriptDestroyed")) {
            this.executeEventFunc("scriptDestroyed");
        }
    }

    public void update() {
        if (this.hasEventFunc("update")) {
            this.executeEventFunc("update");
        }
    }
}
