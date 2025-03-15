/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.badlogic.gdx.Game
 *  com.badlogic.gdx.Gdx
 *  com.badlogic.gdx.InputProcessor
 *  com.badlogic.gdx.Screen
 *  com.badlogic.gdx.graphics.Color
 *  com.badlogic.gdx.graphics.g2d.Batch
 *  com.badlogic.gdx.graphics.g2d.BitmapFont
 *  com.badlogic.gdx.graphics.g2d.SpriteBatch
 *  com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
 *  com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator$FreeTypeFontParameter
 *  com.badlogic.gdx.scenes.scene2d.Actor
 *  com.badlogic.gdx.scenes.scene2d.EventListener
 *  com.badlogic.gdx.scenes.scene2d.InputEvent
 *  com.badlogic.gdx.scenes.scene2d.Stage
 *  com.badlogic.gdx.scenes.scene2d.ui.Skin
 *  com.badlogic.gdx.scenes.scene2d.ui.TextButton
 *  com.badlogic.gdx.scenes.scene2d.utils.ClickListener
 *  com.badlogic.gdx.utils.ScreenUtils
 */
package org.seriouz.openbuild.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuScreen
implements Screen {
    public Game game;
    public Skin skin;
    public BitmapFont bitmapFont;
    public TextButton playButton;
    public Stage stage;
    private final SpriteBatch batch;

    public MenuScreen(final Game game) {
        this.game = game;
        this.skin = new Skin(Gdx.files.internal("resources/gdx/default.json"));
        this.batch = new SpriteBatch();
        this.playButton = new TextButton("Play!", this.skin);
        this.playButton.setWidth(200.0f);
        this.playButton.setPosition((float)Gdx.graphics.getWidth() / 2.0f - this.playButton.getWidth() / 2.0f, (float)Gdx.graphics.getHeight() / 2.0f);
        this.playButton.addListener(new ClickListener(){

            public void clicked(InputEvent event, float x, float y) {
                game.setScreen((Screen)new GameScreen());
            }
        });
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("resources/fonts/default.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.GREEN;
        parameter.size = 36;
        this.bitmapFont = generator.generateFont(parameter);
        this.stage = new Stage();
        this.stage.addActor(this.playButton);
        Gdx.input.setInputProcessor(this.stage);
    }

    public void show() {
    }

    public void render(float v) {
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1.0f);
        this.batch.begin();
        this.stage.act(v);
        this.stage.draw();
        this.bitmapFont.draw(this.batch, "Open Build", this.playButton.getX() - 40.0f, this.playButton.getY() + 100.0f);
        this.batch.end();
    }

    public void resize(int i, int i1) {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void hide() {
    }

    public void dispose() {
        this.skin.dispose();
        this.bitmapFont.dispose();
        this.stage.dispose();
        this.batch.dispose();
    }
}
