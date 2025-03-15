/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.badlogic.gdx.graphics.Texture
 *  com.badlogic.gdx.graphics.g2d.SpriteBatch
 *  com.badlogic.gdx.graphics.g2d.TextureRegion
 */
package org.seriouz.openbuild.utilities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {
    private final TextureRegion[][] frames;
    public Texture sprite;
    public String spritePath;
    private final int ROWS;
    private final int COLS;
    private final float frameDur;
    private float timeUntilFrame;
    public int currentCol;
    public int currentRow;

    public Animator(String path, int cols, int rows, float frameDur) {
        this.spritePath = path;
        this.sprite = new Texture(this.spritePath.replace("+", "-"));
        this.COLS = cols;
        this.ROWS = rows;
        this.frameDur = frameDur;
        int tileWidth = 16;
        int tileHeight = 16;
        this.frames = TextureRegion.split((Texture)this.sprite, (int)tileWidth, (int)tileHeight);
    }

    public void draw(SpriteBatch spriteBatch, int x, int y, boolean loop) {
        if (this.timeUntilFrame <= 0.0f) {
            ++this.currentCol;
            this.timeUntilFrame = this.frameDur;
        } else {
            this.timeUntilFrame -= 0.1f;
        }
        if (this.currentCol > this.COLS - 1) {
            this.currentCol = loop ? 0 : this.COLS - 1;
        }
        TextureRegion region = this.frames[this.currentRow][this.currentCol];
        spriteBatch.draw(region, (float)x, (float)y);
    }

    public void dispose() {
        this.sprite.dispose();
    }
}
