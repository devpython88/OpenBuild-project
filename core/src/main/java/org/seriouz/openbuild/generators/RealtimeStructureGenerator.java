/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.badlogic.gdx.math.Vector2
 */
package org.seriouz.openbuild.generators;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import org.seriouz.openbuild.builders.BlockParameterBuilder;
import org.seriouz.openbuild.generators.StructureGenerator;
import org.seriouz.openbuild.user.Player;

public class RealtimeStructureGenerator {
    public List<Vector2> centers = new ArrayList<Vector2>();
    public Player player;
    public StructureGenerator structureGenerator;

    public RealtimeStructureGenerator(StructureGenerator structureGenerator, Player player) {
        this.centers.add(new Vector2((float)player.x, (float)player.y));
        this.structureGenerator = structureGenerator;
        this.player = player;
    }

    public void update(int min, int max, int radius, BlockParameterBuilder builder) {
        int distance = (radius * 16) * 2;
        Vector2 newCenter = new Vector2((float)this.player.x, (float)this.player.y);
        for (Vector2 center : this.centers) {
            if (!((float)this.player.x >= center.x - (float)distance) || !((float)this.player.x <= center.x + (float)distance) || !((float)this.player.y >= center.y - (float)distance) || !((float)this.player.y <= center.y + (float)distance)) continue;
            return;
        }
        this.centers.add(newCenter);
        this.structureGenerator.generateAllStructures(min, max, radius, this.player.x, this.player.y, builder);
    }
}
