/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.luaj.vm2.LuaValue
 */
package org.seriouz.openbuild.scripts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.luaj.vm2.LuaValue;
import org.seriouz.openbuild.Block;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.scripts.BlockScript;

public class ScriptManager {
    public List<String> scriptPaths;

    public ScriptManager(String scriptLookupDir) {
        this.reloadScriptPaths(scriptLookupDir);
    }

    private void reloadScriptPaths(String scriptLookupDir) {
        this.scriptPaths = new ArrayList<String>();
        File dir = new File(scriptLookupDir);
        assert (dir.exists());
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            this.scriptPaths.add(file.getAbsolutePath());
        }
    }

    public CompletableFuture<BlockScript> getBlockScript(String name, Block block, BlockManager blockManager) {
        return CompletableFuture.supplyAsync(() -> this.scriptPaths.parallelStream().map(scriptName -> {
            BlockScript script = new BlockScript((String)scriptName, block, blockManager);
            LuaValue hostBlock = script.globals.get("host_block");
            return hostBlock.isstring() && name.contains(hostBlock.tojstring()) ? script : null;
        }).filter(Objects::nonNull).findFirst().orElse(null));
    }
}
