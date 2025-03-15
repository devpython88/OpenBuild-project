/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.badlogic.gdx.graphics.Texture
 */
package org.seriouz.openbuild.managers;

import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;
import org.seriouz.openbuild.utilities.Logger;

public class EntityTextureManager {
    HashMap<String, Texture> textureMap = new HashMap();

    public EntityTextureManager(String[] textures) {
        for (String texture : textures) {
            Logger.info(texture);
            this.textureMap.put(texture, new Texture("resources/entities/" + texture));
        }
    }

    public void dispose() {
        for (Map.Entry<String, Texture> entry : this.textureMap.entrySet()) {
            entry.getValue().dispose();
        }
    }
}
