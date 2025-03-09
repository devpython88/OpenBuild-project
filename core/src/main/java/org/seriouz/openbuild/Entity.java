/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.badlogic.gdx.Gdx
 *  com.badlogic.gdx.audio.Sound
 *  com.badlogic.gdx.graphics.Texture
 *  com.badlogic.gdx.math.Vector2
 *  org.json.JSONArray
 *  org.json.JSONObject
 */
package org.seriouz.openbuild;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;
import org.seriouz.openbuild.managers.BlockManager;

public class Entity {
    public float cooldown;
    public int travelRange;
    public Texture texture;
    public String textureName;
    public int x;
    public int y;
    private Vector2 nextStop;
    public BlockManager blockManager;
    private float timeUntilNextMove;
    private float timeUntilNextStep;
    public List<Sound> sounds;
    private float delayTillSound;
    public String dataFile;

    public Entity(String textureName, Texture texture, JSONObject data, String dataFile, int x, int y, BlockManager blockManager) {
        this.textureName = textureName;
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.blockManager = blockManager;
        this.dataFile = dataFile;
        this.sounds = new ArrayList<Sound>();
        this.cooldown = data.getFloat("Cooldown");
        this.travelRange = data.getInt("Travel Range");
        this.timeUntilNextMove = 0.0f;
        this.timeUntilNextStep = 1.0f;
        this.delayTillSound = 30.0f;
        JSONArray tempSounds = data.getJSONArray("Sounds");
        for (Object tempSound : tempSounds) {
            if (!(tempSound instanceof String)) continue;
            this.sounds.add(Gdx.audio.newSound(Gdx.files.internal("resources/audio/" + String.valueOf(tempSound) + ".wav")));
        }
    }

    public void getRandomDirection() {
        int direction = new Random().nextInt(1, 8);
        int pixelCoordTravelRange = this.travelRange * 16;
        switch (direction) {
            case 1: {
                this.nextStop = new Vector2((float)this.x, (float)(this.y + pixelCoordTravelRange));
                break;
            }
            case 2: {
                this.nextStop = new Vector2((float)this.x, (float)(this.y - pixelCoordTravelRange));
                break;
            }
            case 3: {
                this.nextStop = new Vector2((float)(this.x + pixelCoordTravelRange), (float)this.y);
                break;
            }
            case 4: {
                this.nextStop = new Vector2((float)(this.x - pixelCoordTravelRange), (float)this.y);
                break;
            }
            case 5: {
                this.nextStop = new Vector2((float)(this.x + pixelCoordTravelRange), (float)(this.y - pixelCoordTravelRange));
                break;
            }
            case 6: {
                this.nextStop = new Vector2((float)(this.x - pixelCoordTravelRange), (float)(this.y - pixelCoordTravelRange));
                break;
            }
            case 7: {
                this.nextStop = new Vector2((float)(this.x + pixelCoordTravelRange), (float)(this.y + pixelCoordTravelRange));
                break;
            }
            case 8: {
                this.nextStop = new Vector2((float)(this.x - pixelCoordTravelRange), (float)(this.y + pixelCoordTravelRange));
            }
        }
    }

    public void roam() {
        if (this.nextStop == null || (float)this.x == this.nextStop.x && (float)this.y == this.nextStop.y && this.timeUntilNextMove <= 0.0f) {
            this.getRandomDirection();
            this.timeUntilNextMove = this.cooldown;
        }
        if (this.timeUntilNextMove > 0.0f) {
            this.timeUntilNextMove -= 0.1f;
            return;
        }
        if (this.timeUntilNextStep > 0.0f) {
            this.timeUntilNextStep -= 0.1f;
            return;
        }
        int nextX = this.x;
        int nextY = this.y;
        if (this.nextStop.x < (float)nextX) {
            nextX -= 16;
        }
        if (this.nextStop.x > (float)nextX) {
            nextX += 16;
        }
        if (this.nextStop.y < (float)nextY) {
            nextY -= 16;
        }
        if (this.nextStop.y > (float)nextY) {
            nextY += 16;
        }
        if (this.blockManager.blockExists(nextX, nextY)) {
            this.getRandomDirection();
            this.timeUntilNextMove = this.cooldown;
            return;
        }
        this.x = nextX;
        this.y = nextY;
        this.timeUntilNextStep = 10.0f;
    }

    public void handleSound() {
        if (this.delayTillSound > 0.0f) {
            this.delayTillSound -= 0.1f;
            return;
        }
        this.delayTillSound = new Random().nextFloat(40.0f, 57.0f);
        if (this.sounds.size() <= 1) {
            this.sounds.get(0).play();
            return;
        }
        int index = new Random().nextInt(0, this.sounds.size() - 1);
        this.sounds.get(index).play();
    }
}
