package org.seriouz.openbuild.scripts.tables;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.seriouz.openbuild.scripts.funcs.FunctionBuilder;
import org.seriouz.openbuild.utilities.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileSystem extends LuaTable {
    public FileSystem(){
        set("readFile", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue luaValue) {
                String fileContent = readFile(getTweakedPath(luaValue));
                return fileContent != null ? LuaValue.valueOf(fileContent) : NIL;
            }
        });

        set("writeToFile", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue luaValue, LuaValue luaValue1) {
                writeFile(getTweakedPath(luaValue), luaValue1.tojstring());
                return NIL;
            }
        });

        set("exists", FunctionBuilder.oneArg(val -> LuaValue.valueOf(Files.exists(Path.of(getTweakedPath(val))))));

        set("listFilesFrom", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue luaValue) {
                return listDirectory(getTweakedPath(luaValue));
            }
        });

        set("makeDir", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue luaValue) {
                makeDirectory(getTweakedPath(luaValue));
                return NIL;
            }
        });

        set("remove", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue luaValue) {
                remove(getTweakedPath(luaValue));
                return NIL;
            }
        });
    }

    private static String getTweakedPath(LuaValue luaValue) {
        return luaValue.tojstring().replace("~", "./data/scripts/");
    }

    private String readFile(String filePath) {
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e){
            Logger.err("File IO error: " + e, "readFile");
            return null;
        }
    }


    private void writeFile(String filePath, String fileContent){
        try {
            Files.writeString(Path.of(filePath), fileContent);
        } catch (IOException e){
            Logger.err("File IO error: " + e, "readFile");
        }
    }

    private LuaTable listDirectory(String name){
        File dir = new File(name);

        if (!dir.exists()) return null;

        if (!dir.isDirectory()) return null;

        File[] arrFiles = dir.listFiles();

        if (arrFiles == null) return null;

        List<String> files = new ArrayList<>();

        for (File file : arrFiles) files.add(file.getName());

        return arrayToTable(files);
    }

    public void makeDirectory(String path){
        try {
            Files.createDirectory(Path.of(path));
        } catch (IOException e) {
            Logger.err("Could not create directory, Error message: " + e, "makeDir");
        }
    }

    public void remove(String path){
        try {
            Files.delete(Path.of(path));
        } catch (IOException e) {
            Logger.err("Could not delete file/folder, Error message: " + e, "remove");
        }
    }



    private static LuaTable arrayToTable(List<String> list){
        LuaTable table = new LuaTable();

        for (int i = 0; i < list.size(); i++){
            table.set(i + 1, list.get(i));
        }

        return table;
    }
}
