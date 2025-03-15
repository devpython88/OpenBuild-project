/*
 * Decompiled with CFR 0.152.
 */
package org.seriouz.openbuild.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.seriouz.openbuild.builders.BlockParameterBuilder;
import org.seriouz.openbuild.generators.Structure;
import org.seriouz.openbuild.managers.BlockManager;

public class StructureGenerator {
    public BlockManager blockManager;
    public List<String> structures;

    public StructureGenerator(BlockManager blockManager) {
        this.blockManager = blockManager;
        this.structures = new ArrayList<String>();
        this.reloadAllStructures();
    }

    public void reloadAllStructures() {
        File structuresDirectory = new File("resources/structures/");
        assert (structuresDirectory.exists());
        File[] files = structuresDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                String structureName = file.getName().replace(".obstructure", "");
                this.structures.add(structureName);
            }
        }
    }

    public void generate(String structureName, int minAmount, int maxAmount, int gridRad, int centerX, int centerY, BlockParameterBuilder builder) {
        if (!this.structures.contains(structureName)) {
            return;
        }
        if (minAmount > maxAmount) {
            return;
        }
        Random rn = new Random();
        Structure loader = new Structure(structureName, 0, 0, this.blockManager);
        int radius = gridRad * 16;
        for (int i = 0; i <= rn.nextInt(minAmount, maxAmount); ++i) {
            int x = rn.nextInt(centerX - radius, centerX + radius);
            int y = rn.nextInt(centerY - radius, centerY + radius);
            loader.centerX = x;
            loader.centerY = y;
            loader.convertToBlocks(builder);
        }
    }

    public void generateAllStructures(int minAmount, int maxAmount, int radius, int centerX, int centerY, BlockParameterBuilder builder) {
        for (String structure : this.structures) {
            if (structure.startsWith("_")) continue;
            new Thread(() -> {
                synchronized (this) {
                    this.generate(structure, minAmount, maxAmount, radius, centerX, centerY, builder);
                }
            }).start();
        }
    }
}
