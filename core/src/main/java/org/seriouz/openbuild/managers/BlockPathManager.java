/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.badlogic.gdx.graphics.Texture
 */
package org.seriouz.openbuild.managers;

import com.badlogic.gdx.graphics.Texture;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockPathManager {
    public List<String> blockPaths = new ArrayList<String>();
    public HashMap<String, Texture> blockMap = new HashMap();

    public BlockPathManager() {
        this.reloadTextures();
    }

    public void reloadTextures() {
        File blocksDir = new File("./resources/blocks");
        assert (blocksDir.exists());
        File[] files = blocksDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() || !file.getName().endsWith(".png")) continue;
                Texture texture = new Texture(file.getPath());
                String blockName = file.getName().replace(".png", "");
                this.blockMap.put(blockName, texture);
                this.blockPaths.add(blockName);
            }
        }
    }

    public List<String> getPaths() {
        return this.blockPaths;
    }

    public List<String> getPathsSafe() {
        ArrayList<String> safePaths = new ArrayList<String>();
        for (String path : this.blockPaths) {
            if (path.startsWith("_")) continue;
            safePaths.add(path);
        }
        return safePaths;
    }

    public void dispose() {
        for (Map.Entry<String, Texture> entry : this.blockMap.entrySet()) {
            entry.getValue().dispose();
        }
    }
}
