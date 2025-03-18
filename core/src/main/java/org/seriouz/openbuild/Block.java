/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.badlogic.gdx.Gdx
 *  com.badlogic.gdx.graphics.Color
 *  com.badlogic.gdx.graphics.Texture
 *  com.badlogic.gdx.graphics.g2d.SpriteBatch
 */
package org.seriouz.openbuild;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.seriouz.openbuild.builders.BlockParameterBuilder;
import org.seriouz.openbuild.builders.ScriptBuilder;
import org.seriouz.openbuild.implementers.BombImplementer;
import org.seriouz.openbuild.implementers.DoorImplementer;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.managers.SoundManager;
import org.seriouz.openbuild.properties.DoorProperties;
import org.seriouz.openbuild.properties.LightSourceProperties;
import org.seriouz.openbuild.scripts.BlockScript;
import org.seriouz.openbuild.scripts.ScriptManager;
import org.seriouz.openbuild.utilities.Logger;

import java.util.Random;

public class Block {
    public int x;
    public int y;
    public boolean collidable;
    public Texture image;
    public String imageName;
    private DoorProperties doorProperties;
    private LightSourceProperties lsProperties;
    private BlockScript script;
    private float rotation;
    private ScriptBuilder scriptParamBuilder;
    private BlockParameterBuilder builder;

    public Block() {
    }

    public Block(int x, int y, String texturePath, Texture texture, BlockParameterBuilder builder) {
        this.scriptParamBuilder = builder.scriptBuilder;
        this.builder = builder;

        this.imageName = texturePath.replace("./resources/blocks/", "").replace(".png", "");

        this.x = Math.floorDiv(x + 8, 16) * 16;
        this.y = Math.floorDiv(y + 8, 16) * 16;
        this.rotation = 0;

        this.collidable = true;
        this.reloadScript(builder.blockManager, builder.scriptManager);

        if (!this.imageName.startsWith("./resources/blocks/")){
            this.imageName = "./resources/blocks/" + imageName;
        }

        if (!this.imageName.endsWith(".png")) {
            this.imageName = this.imageName + ".png";
        }

        if (this.isDoor(this.imageName)) {
            this.doorProperties = new DoorProperties(this.imageName.replace("blocks/", "blocks/_").replace(".png", " Open.png"),
                this.imageName);
        }

        if ((this.imageName.contains("Grass") && !imageName.contains("Block")) || this.imageName.contains("Pebbles") || this.imageName.contains("Flower")
        || imageName.contains("Leaves")){
            if (this.imageName.equals("Pebbles")){
                float randomRot = new Random().nextFloat(0, 360);
                rotation = (Math.round(randomRot) / 90f) * 90;
            }
            collidable = false;
        }

        if (this.isLamp(this.imageName)) {
            this.lsProperties = new LightSourceProperties(builder.rayHandler, this.imageName, 60,  90, Color.ORANGE, x, y);
        }

        if (imageName.contains("Torch Lamp")){
            this.lsProperties = new LightSourceProperties(builder.rayHandler,
                imageName,
                85, 125,
                Color.ORANGE,
                x, y);
        }

        if (imageName.contains("Dedris Core")){
            this.lsProperties = new LightSourceProperties(builder.rayHandler,
                imageName,
                900, 300,
                Color.SKY,
                x, y);
        }

        if (imageName.contains("Oral Dust")){
            this.lsProperties = new LightSourceProperties(builder.rayHandler,
                imageName,
                80, 60,
                new Color(0.321f, 0.011f, 0.988f, 1f),
                x, y);
        }

        if (this.imageName.contains("Water")) {
            this.collidable = false;
        }

        if (texture == null) {
            return;
        }

        this.image = texture;
    }

    private void reloadScript(BlockManager blockManager, ScriptManager scriptManager) {
        Block tempThis = this;
        scriptManager.getBlockScript(this.imageName, this, blockManager, scriptParamBuilder).thenAccept(script -> {
            if (script != null) {
                tempThis.script = script;
                tempThis.script.scriptLoaded();
                tempThis.script.blockLoaded();
            }
        });
        this.script = tempThis.script;
    }

    public void draw(SpriteBatch batch) {
        if (this.script != null) {
            this.script.update();
            this.script.blockDrawn();
        }
        if (this.lsProperties != null) {
            this.lsProperties.draw(batch);
            return;
        }

        if (lsProperties != null){
            if (builder.sunlight.v < 0.4f && imageName.contains("Oral Dust")) {
                lsProperties.light.setDistance(130);
            } else {
                lsProperties.light.setDistance(80);
            }
        }
        Sprite sprite = new Sprite(image);
        sprite.setPosition(x, y);
        sprite.setSize(image.getWidth(), image.getHeight());
        sprite.setRotation(rotation);

        sprite.draw(batch);
    }

    public void interact(BlockManager blockManager, SoundManager soundManager) {
        if (this.imageName.contains("Door")) {
            if (this.script != null) {
                this.script.interacted();
                return;
            }
            if (this.imageName.equals(this.doorProperties.opened)) {
                soundManager.play(this.doorProperties.opened.replace("./resources/blocks/", "").replace(".png", ""));
            } else {
                soundManager.play(this.doorProperties.closed.replace("./resources/blocks/", "").replace(".png", ""));
            }
            DoorImplementer.implement(this, blockManager, this.doorProperties.opened, this.doorProperties.closed);
        }
        if (this.imageName.contains("Dynamite")) {
            if (this.script != null) {
                this.script.interacted();
                return;
            }
            BombImplementer.implement(blockManager, this, soundManager);
        } else if (this.script != null) {
            this.script.interacted();
        }
    }

    public boolean isNear(int x2, int y2) {
        return this.x == x2 && this.y == y2 || this.x == x2 + 16 && this.y == y2 || this.x == x2 - 16 && this.y == y2 || this.x == x2 && this.y == y2 + 16 || this.x == x2 && this.y == y2 - 16;
    }

    public void handle(BlockManager blockManager, int handlerX, int handlerY, SoundManager soundManager) {
        if (Gdx.input.isKeyJustPressed(39) && this.isNear(handlerX, handlerY)) {
            this.interact(blockManager, soundManager);
        }
    }

    public void dispose() {
        if (imageName.contains("Dedris Core")) this.builder.soundManager.play("Dedris Core Falling Apart");

        if (this.script != null) {
            this.script.scriptDestroyed();
            this.script.blockDestroyed();
        }
        if (this.lsProperties != null) {
            this.lsProperties.light.setActive(false);
        }
    }

    public boolean isDoor(String name) {
        return name.contains("Door");
    }

    public boolean isLamp(String name) {
        return name.contains("Lamp Pole");
    }

    public boolean isBomb(String name) {
        return name.contains("Dynamite");
    }

    public boolean isElectricityTunnel(String name) {
        return name.contains("ElectricityTunnel");
    }

    public boolean isSwitch(String name) {
        return name.contains("Switch");
    }
}
