package org.seriouz.openbuild.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;

public class MessageScreen implements Screen {
    private float timer;

    private BitmapFont font;
    private SpriteBatch spriteBatch;
    private String msg;
    private int textWidth, textHeight;
    private Game game;
    private GameScreen screen;
    public MessageScreen(float lifetime, String msg, Game game, GameScreen screen){
        this.game = game;
        this.screen = screen;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("resources/fonts/default.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.WHITE;

        font = generator.generateFont(parameter);

        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, msg);

        textWidth = (int) layout.width;
        textHeight = (int) layout.height;

        timer = lifetime;
        spriteBatch = new SpriteBatch();
        this.msg = msg;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (timer <= 0f){
            game.setScreen(screen);
            return;
        }
        ScreenUtils.clear(0.2f, .2f, .2f, 1f);

        timer -= delta;

        spriteBatch.begin();

        int x = (Gdx.graphics.getWidth() / 2) - (textWidth / 2);
        int y = (Gdx.graphics.getHeight() / 2) - (textHeight / 2);

        font.draw(spriteBatch, msg, x, y);

        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();
        spriteBatch.dispose();
    }
}
