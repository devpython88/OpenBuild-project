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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.seriouz.openbuild.Logger;
import org.seriouz.openbuild.builders.BlockParameterBuilder;
import org.seriouz.openbuild.implementers.BombImplementer;
import org.seriouz.openbuild.implementers.DoorImplementer;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.managers.SoundManager;
import org.seriouz.openbuild.properties.DoorProperties;
import org.seriouz.openbuild.properties.LightSourceProperties;
import org.seriouz.openbuild.properties.WaterProperties;
import org.seriouz.openbuild.scripts.BlockScript;
import org.seriouz.openbuild.scripts.ScriptManager;

public class Block {
    public int x;
    public int y;
    public boolean collidable;
    public Texture image;
    public String imageName;
    private DoorProperties doorProperties;
    private LightSourceProperties lsProperties;
    private WaterProperties waterProperties;
    private BlockScript script;

    public Block() {
    }

    public Block(int x, int y, String texturePath, Texture texture, BlockParameterBuilder builder) {
        this.imageName = texturePath.replace("./resources/blocks/", "").replace(".png", "");

        this.x = Math.floorDiv(x + 8, 16) * 16;
        this.y = Math.floorDiv(y + 8, 16) * 16;

        this.collidable = true;
        this.reloadScript(builder.blockManager, builder.scriptManager);

        if (this.isDoor(this.imageName)) {
            this.doorProperties = new DoorProperties("_" + this.imageName + " Open", this.imageName);
        }

        if (this.isLamp(this.imageName)) {
            this.lsProperties = new LightSourceProperties(builder.rayHandler, this.imageName, 60, 90, Color.ORANGE, x, y);
        }

        if (this.imageName.contains("Water")) {
            this.waterProperties = new WaterProperties(this, this.imageName);
        }

        this.imageName = "./resources/blocks/" + this.imageName;
        if (texture == null) {
            return;
        }

        if (!this.imageName.endsWith(".png")) {
            this.imageName = this.imageName + ".png";
        }
        this.image = texture;
    }

    private void reloadScript(BlockManager blockManager, ScriptManager scriptManager) {
        Block tempThis = this;
        scriptManager.getBlockScript(this.imageName, this, blockManager).thenAccept(script -> {
            if (script != null) {
                tempThis.script = script;
            }
        });
        if (this.script != null) {
            this.script.scriptLoaded();
            this.script.blockLoaded();
        }
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
        if (this.waterProperties != null) {
            this.waterProperties.draw(batch);
            return;
        }
        batch.draw(this.image, (float) this.x, (float) this.y);
    }

    public void interact(BlockManager blockManager, SoundManager soundManager) {
        if (this.imageName.contains("Door")) {
            if (this.script != null) {
                this.script.interacted();
                return;
            }
            if (this.imageName.equals(this.doorProperties.opened)) {
                soundManager.play(this.doorProperties.opened);
            } else {
                soundManager.play(this.doorProperties.closed);
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
        return name.contains("Lamp");
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
