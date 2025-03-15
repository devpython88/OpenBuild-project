/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  box2dLight.PointLight
 *  box2dLight.RayHandler
 *  com.badlogic.gdx.graphics.Color
 *  com.badlogic.gdx.graphics.g2d.SpriteBatch
 */
package org.seriouz.openbuild.properties;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.seriouz.openbuild.utilities.Animator;

public class LightSourceProperties {
    private int x;
    private int y;
    public PointLight light;
    public int intensity;
    public Color color;
    public Animator animator;

    public LightSourceProperties(RayHandler handler, String sprite, int distance, int intensity, Color color, int x, int y) {
        this.intensity = intensity;
        this.color = color;
        this.x = x;
        this.y = y;
        this.light = new PointLight(handler, intensity, color, (float)distance, (float)(x + 8), (float)(y + 8));
        this.light.setSoftnessLength(0.0f);
        this.animator = new Animator(sprite, 3, 1, 1.0f);
    }

    public void draw(SpriteBatch batch) {
        this.animator.draw(batch, this.x, this.y, true);
    }
}
