/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.badlogic.gdx.audio.Sound
 *  com.badlogic.gdx.graphics.g2d.SpriteBatch
 *  org.json.JSONObject
 */
package org.seriouz.openbuild.managers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import org.seriouz.openbuild.Entity;
import org.seriouz.openbuild.utilities.Logger;

public class EntityManager {
    public EntityTextureManager entityTextureManager;
    public List<Entity> entities;
    public HashMap<String, JSONObject> entityData = new HashMap();

    public EntityManager(String entityDataDir, String entitySpriteDir) {
        this.entities = new ArrayList<Entity>();
        ArrayList<String> textures = new ArrayList<String>();
        File spriteDir = new File(entitySpriteDir);
        assert (spriteDir.exists());
        File[] sprites = spriteDir.listFiles();
        if (sprites != null) {
            for (File sprite : sprites) {
                if (!sprite.getName().endsWith(".png")) continue;
                textures.add(sprite.getName());
            }
        }
        String[] arrTextures = textures.toArray(new String[0]);
        this.entityTextureManager = new EntityTextureManager(arrTextures);
        File dataDir = new File(entityDataDir);
        assert (dataDir.exists());
        File[] dataFiles = dataDir.listFiles();
        if (dataFiles != null) {
            for (File dataFile : dataFiles) {
                if (!dataFile.getName().endsWith(".obentity")) continue;
                try {
                    JSONObject data = new JSONObject(Files.readString(Path.of(dataFile.getAbsoluteFile().toURI())));
                    this.entityData.put(dataFile.getName(), data);
                }
                catch (IOException e) {
                    Logger.err("Error while reading data file '" + dataFile.getName() + "'. " + e.getMessage());
                }
            }
        }
    }

    public void createEntity(String name, int x, int y, BlockManager blockManager) {
        String fixedName = name + ".png";
        this.entities.add(new Entity(fixedName, this.entityTextureManager.textureMap.get(fixedName), this.entityData.get(name + ".obentity"), "data/entities/" + name + ".obentity", x, y, blockManager));
    }

    public void draw(SpriteBatch batch, int playerX, int playerY) {
        int maxDrawDistance = 512;
        for (Entity entity : this.entities) {
            if (entity.x < playerX - maxDrawDistance || entity.x > playerX + maxDrawDistance || entity.y < playerY - maxDrawDistance || entity.y > playerY + maxDrawDistance) continue;
            entity.roam();
            entity.handleSound();
            batch.draw(entity.texture, (float)entity.x, (float)entity.y);
        }
    }

    public void dispose() {
        this.entityTextureManager.dispose();
        for (Entity entity : this.entities) {
            for (Sound sound : entity.sounds) {
                sound.dispose();
            }
        }
    }

    public boolean entityAt(int x, int y){
        Logger.info("" + x + " " + y);
        for (Entity entity : this.entities){
            if (entity.x == x && entity.y == y) return true;
        }
        return false;
    }

    public void hurtEntity(int x, int y){
        for (Entity entity : this.entities){
            if (entity.x == x && entity.y == y){
                entities.remove(entity);
                break;
            }
        }
    }
}
