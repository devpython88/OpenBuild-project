/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.json.JSONArray
 *  org.json.JSONObject
 */
package org.seriouz.openbuild.generators;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.seriouz.openbuild.builders.BlockParameterBuilder;
import org.seriouz.openbuild.managers.BlockManager;

public class Structure {
    private BlockManager blockManager;
    public String structureFile;
    public int centerX;
    public int centerY;
    private JSONObject structureJson;

    public Structure(Object file, int centerX, int centerY, BlockManager blockManager) {
        this.blockManager = blockManager;
        this.centerX = centerX;
        this.centerY = centerY;
        try {
            String contents = Files.readString(Paths.get("resources", "structures", String.valueOf(file) + ".obstructure"));
            this.structureJson = new JSONObject(contents);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not load structure: " + String.valueOf(file));
        }
    }

    public void convertToBlocks(BlockParameterBuilder builder) {
        Random random;
        int result;
        int chance;
        if (this.structureJson == null) {
            return;
        }
        if (this.structureJson.has("Chance") && (chance = this.structureJson.getInt("Chance")) > 1 && (result = (random = new Random()).nextInt(1, chance)) != 1) {
            return;
        }
        if (this.structureJson.has("Blocks")) {
            JSONArray blocks = this.structureJson.getJSONArray("Blocks");
            for (int i = 0; i < blocks.length(); ++i) {
                JSONObject object = blocks.getJSONObject(i);
                int x = object.getInt("X") * 16;
                int y = object.getInt("Y") * 16;
                String id = object.getString("Id");
                this.blockManager.createBlock(x += this.centerX, y += this.centerY, this.blockManager.getBlockPathManager().blockMap.get(id), id, builder);
            }
        }
    }
}
