/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  box2dLight.PointLight
 */
package org.seriouz.openbuild.builders;

import box2dLight.PointLight;

public class WorldSaveParameterBuilder {
    public float lightIntensity;
    public boolean torchLit;
    public PointLight torch;

    private WorldSaveParameterBuilder(float lightIntensity, boolean torchLit, PointLight torch) {
        this.lightIntensity = lightIntensity;
        this.torchLit = torchLit;
        this.torch = torch;
    }

    public static WorldSaveParameterBuilder build(float lightIntensity, boolean torchLit, PointLight torch) {
        return new WorldSaveParameterBuilder(lightIntensity, torchLit, torch);
    }
}
