/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.badlogic.gdx.graphics.g2d.SpriteBatch
 */
package org.seriouz.openbuild.properties;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.seriouz.openbuild.Block;
import org.seriouz.openbuild.utilities.Animator;

public class WaterProperties {
    public Animator animator;
    public Block host;

    public WaterProperties(Block host, String imageName) {
        this.host = host;
        this.animator = new Animator(imageName, 3, 1, 2.0f);
    }

    public void draw(SpriteBatch spriteBatch) {
        this.animator.draw(spriteBatch, this.host.x, this.host.y, true);
    }
}
