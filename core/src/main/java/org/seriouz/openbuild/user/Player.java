/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.badlogic.gdx.Gdx
 *  com.badlogic.gdx.graphics.OrthographicCamera
 *  com.badlogic.gdx.graphics.Texture
 *  com.badlogic.gdx.graphics.g2d.SpriteBatch
 *  com.badlogic.gdx.math.Vector3
 */
package org.seriouz.openbuild.user;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import org.seriouz.openbuild.builders.BlockParameterBuilder;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.utilities.Animator;
import org.seriouz.openbuild.utilities.Pointer;

public class Player {
    public int x;
    public int y;
    public Texture skin;
    public String skinPath;
    public Animator animator;
    private final float DELAY_BETWEEN_STEPS;
    private float timerForSteps;

    public Player() {
        this.DELAY_BETWEEN_STEPS = 1.0f;
    }

    public Player(int x, int y, String skinPath) {
        this.x = x;
        this.y = y;
        this.skinPath = skinPath;
        this.skin = new Texture("./resources/player/" + skinPath);
        this.animator = new Animator("resources/player/" + skinPath, 4, 2, 1.0f);
        this.DELAY_BETWEEN_STEPS = 1.0f;
        this.timerForSteps = 0.0f;
    }

    public int getCenterX() {
        return 8;
    }

    public int getCenterY() {
        return 8;
    }

    public void draw(SpriteBatch batch) {
        this.animator.draw(batch, this.x, this.y, true);
    }

    public void dispose() {
        this.skin.dispose();
    }

    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    public void handleMovement(OrthographicCamera camera, BlockManager blockManager) {
        this.timerForSteps -= 0.25f;

        Vector3 cameraPosition = camera.position;
        Vector3 targetPosition = new Vector3((float)this.x, (float)this.y, 0.0f);
        cameraPosition.lerp(targetPosition, 0.1f);
        camera.position.set(cameraPosition);

        if (!Gdx.input.isKeyPressed(59) && !Gdx.input.isKeyPressed(129)) {
            if (Gdx.input.isKeyJustPressed(51) && !this.hasCollision(0, 16, blockManager)) {
                this.move(0, 16);
            }
            if (Gdx.input.isKeyJustPressed(47) && !this.hasCollision(0, -16, blockManager)) {
                this.move(0, -16);
            }
            if (Gdx.input.isKeyJustPressed(29) && !this.hasCollision(-16, 0, blockManager)) {
                this.move(-16, 0);
            }
            if (Gdx.input.isKeyJustPressed(32) && !this.hasCollision(16, 0, blockManager)) {
                this.move(16, 0);
            }
        }
        if (Gdx.input.isKeyPressed(129)) {
            if (this.timerForSteps > 0.0f) {
                return;
            }
            if (Gdx.input.isKeyPressed(51) && !this.hasCollision(0, 16, blockManager)) {
                this.move(0, 16);
                this.timerForSteps = this.DELAY_BETWEEN_STEPS;
            }
            if (Gdx.input.isKeyPressed(47) && !this.hasCollision(0, -16, blockManager)) {
                this.move(0, -16);
                this.timerForSteps = this.DELAY_BETWEEN_STEPS;
            }
            if (Gdx.input.isKeyPressed(29) && !this.hasCollision(-16, 0, blockManager)) {
                this.move(-16, 0);
                this.timerForSteps = this.DELAY_BETWEEN_STEPS;
            }
            if (Gdx.input.isKeyPressed(32) && !this.hasCollision(16, 0, blockManager)) {
                this.move(16, 0);
                this.timerForSteps = this.DELAY_BETWEEN_STEPS;
            }
        }
    }

    public boolean hasCollision(int deltaX, int deltaY, BlockManager blockManager) {
        return blockManager.blockExists(this.x + deltaX, this.y + deltaY);
    }

    public void handleBlocks(BlockManager blockManager, BlockParameterBuilder builder) {
        if (Gdx.input.isKeyJustPressed(41) && !blockManager.blockExists(this.x, this.y)) {
            blockManager.createBlock(this.x, this.y, builder);
        }

        if (Gdx.input.isKeyPressed(59)) {
            if (Gdx.input.isKeyPressed(51)) {
                blockManager.destroyBlock(this.x, this.y + 16);
            }
            if (Gdx.input.isKeyPressed(47)) {
                blockManager.destroyBlock(this.x, this.y - 16);
            }
            if (Gdx.input.isKeyPressed(29)) {
                blockManager.destroyBlock(this.x - 16, this.y);
            }
            if (Gdx.input.isKeyPressed(32)) {
                blockManager.destroyBlock(this.x + 16, this.y);
            }
        }
    }
}
