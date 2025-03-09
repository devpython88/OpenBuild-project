/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  box2dLight.PointLight
 *  box2dLight.RayHandler
 *  com.badlogic.gdx.Gdx
 *  com.badlogic.gdx.InputProcessor
 *  com.badlogic.gdx.Screen
 *  com.badlogic.gdx.audio.Music
 *  com.badlogic.gdx.graphics.Color
 *  com.badlogic.gdx.graphics.OrthographicCamera
 *  com.badlogic.gdx.graphics.Texture
 *  com.badlogic.gdx.graphics.g2d.Batch
 *  com.badlogic.gdx.graphics.g2d.BitmapFont
 *  com.badlogic.gdx.graphics.g2d.SpriteBatch
 *  com.badlogic.gdx.graphics.g2d.TextureRegion
 *  com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
 *  com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator$FreeTypeFontParameter
 *  com.badlogic.gdx.math.Vector2
 *  com.badlogic.gdx.physics.box2d.World
 *  com.badlogic.gdx.scenes.scene2d.Actor
 *  com.badlogic.gdx.scenes.scene2d.Stage
 *  com.badlogic.gdx.scenes.scene2d.ui.Dialog
 *  com.badlogic.gdx.scenes.scene2d.ui.SelectBox
 *  com.badlogic.gdx.scenes.scene2d.ui.Skin
 *  com.badlogic.gdx.scenes.scene2d.ui.TextField
 *  com.badlogic.gdx.utils.ScreenUtils
 */
package org.seriouz.openbuild.screens;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.ScreenUtils;
import org.seriouz.openbuild.CommandExecutioner;
import org.seriouz.openbuild.builders.BlockParameterBuilder;
import org.seriouz.openbuild.builders.WorldSaveParameterBuilder;
import org.seriouz.openbuild.dialogs.PauseMenuDialog;
import org.seriouz.openbuild.generators.RealtimeStructureGenerator;
import org.seriouz.openbuild.generators.Structure;
import org.seriouz.openbuild.generators.StructureGenerator;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.managers.EntityManager;
import org.seriouz.openbuild.managers.SoundManager;
import org.seriouz.openbuild.scripts.Script;
import org.seriouz.openbuild.scripts.ScriptManager;
import org.seriouz.openbuild.user.Player;
import org.seriouz.openbuild.user.WorldManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameScreen
    implements Screen {
    public final Skin mainSkin;
    public final Stage stage;
    private final Color torchColor;
    private final SpriteBatch batch;
    private final Player player;
    private final OrthographicCamera camera;
    private final OrthographicCamera uiCamera;
    private final BlockManager blockManager;
    private final Texture panelTexture;
    private final BlockParameterBuilder builder;
    private final StructureGenerator globalStructureGenerator;
    private final WorldManager worldManager;
    private final EntityManager entityManager;
    private final BitmapFont fontRenderer;
    private final RealtimeStructureGenerator realtimeStructureGenerator;
    private final SoundManager soundManager;
    private final World world = new World(new Vector2(0.0f, 0.0f), false);
    private final RayHandler rayHandler = new RayHandler(this.world);
    private final PointLight playerTorch;
    private final Music dayAmbience;
    private final Music nightAmbience;
    private final ScriptManager scriptManager = new ScriptManager("data/scripts/");
    public boolean paused;
    private Structure structure;
    private float lightIntensity;
    private float lightModifier;
    private boolean aDialogShown;
    private List<Script> scripts;

    public GameScreen(Skin skin) {

        scripts = new ArrayList<>();

        this.stage = new Stage();
        this.mainSkin = new Skin(Gdx.files.internal("resources/gdx/default.json"));
        this.blockManager = new BlockManager();
        this.builder = BlockParameterBuilder.build(this.rayHandler, this.blockManager, this.scriptManager);

        for (String scriptPath : this.scriptManager.scriptPaths) {
            Script script = new Script(scriptPath);
            if (script.globals.get("host_block").isstring()) continue;
            script.scriptLoaded();
            scripts.add(script);
        }

        this.batch = new SpriteBatch();
        this.player = new Player(this.round(this.getScreenCenterX(), 16), this.round(this.getScreenCenterY(), 16), "player.png");
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, (float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        this.camera.zoom = 0.5f;
        this.uiCamera = new OrthographicCamera();
        this.uiCamera.setToOrtho(false, (float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());


        this.panelTexture = new Texture("./resources/UI/panel.png");


        this.globalStructureGenerator = new StructureGenerator(this.blockManager);
        this.globalStructureGenerator.generateAllStructures(400, 600, 128, this.player.x, this.player.y, this.builder);


        this.entityManager = new EntityManager("data/entities", "resources/entities");
        this.entityManager.createEntity("Sheep", this.player.x, this.player.y, this.blockManager);


        this.worldManager = new WorldManager(this.blockManager, this.player, this.entityManager);


        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("resources/fonts/default.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
        parameter.color = Color.BLACK;


        this.fontRenderer = generator.generateFont(parameter);

        generator.dispose();

        this.realtimeStructureGenerator = new RealtimeStructureGenerator(this.globalStructureGenerator, this.player);

        this.soundManager = new SoundManager("resources/audio/");

        Gdx.input.setInputProcessor(this.stage);

        this.lightIntensity = 1.0f;

        this.lightModifier = 1.0E-4f;

        this.rayHandler.setAmbientLight(this.lightIntensity);

        this.torchColor = new Color(0.9f, 0.4f, 0.0f, 1.0f);

        this.playerTorch = new PointLight(this.rayHandler, 100, this.torchColor, 60.0f, (float) this.player.x, (float) this.player.y);

        this.playerTorch.setActive(false);

        this.playerTorch.setSoftnessLength(0.0f);

        this.dayAmbience = Gdx.audio.newMusic(Gdx.files.internal("resources/audio/Day Ambience.mp3"));

        this.nightAmbience = Gdx.audio.newMusic(Gdx.files.internal("resources/audio/Night Ambience.mp3"));

        this.dayAmbience.setLooping(true);

        this.nightAmbience.setLooping(true);

        this.dayAmbience.play();

        this.aDialogShown = false;

        this.paused = false;
    }

    public void render() {
        if (Gdx.input.isKeyJustPressed(111)) {
            new PauseMenuDialog(this);
        }
        if (!this.paused) {
            this.batch.setProjectionMatrix(this.camera.combined);
            this.batch.begin();


            this.update();

            ScreenUtils.clear(0.15f, 0.5f, 0.15f, 1.0f);

            this.player.handleBlocks(this.blockManager, this.builder);

            this.blockManager.updateCurrentSelected();

            this.camera.update();

            this.blockManager.handle();

            this.player.draw(this.batch);

            this.blockManager.drawBlocks(this.batch, this.player.x, this.player.y, this.soundManager);

            this.entityManager.draw(this.batch, this.player.x, this.player.y);


            this.batch.end();


            this.rayHandler.setCombinedMatrix(this.camera);
            this.rayHandler.updateAndRender();
        }
        this.batch.setProjectionMatrix(this.uiCamera.combined);
        this.batch.begin();

        this.stage.act(Gdx.graphics.getDeltaTime());

        Texture currentSelectedBlockTexture = this.blockManager.getBlockPathManager().blockMap.get(this.blockManager.currentSelected);

        this.batch.draw(this.panelTexture, 0.0f, 0.0f, (float) Gdx.graphics.getWidth(), 88.0f);

        this.fontRenderer.draw(this.batch, "Coords: " + this.player.x / 16 + " : " + this.player.y / 16, 0.0f, (float) (Gdx.graphics.getHeight() - 32));

        this.batch.draw(new TextureRegion(currentSelectedBlockTexture, 16, 16), 20.0f, 15.0f, 48.0f, 48.0f);

        this.stage.draw();

        this.batch.end();
    }

    private void update() {

        for (Script script : scripts){
            script.update();
        }



        if (Gdx.input.isKeyJustPressed(30)) {
            this.blockManager.blocks.clear();
        }
        this.playerTorch.setPosition((float) (this.player.x + this.player.getCenterX()), (float) (this.player.y + this.player.getCenterY()));
        if (this.lightIntensity < 0.1f) {
            this.lightModifier = -this.lightModifier;
        }
        if (this.lightIntensity > 1.0f) {
            this.lightModifier = -this.lightModifier;
        }
        if (this.lightIntensity < 0.5f && !this.nightAmbience.isPlaying()) {
            this.dayAmbience.stop();
            this.nightAmbience.play();
        }
        if (this.lightIntensity > 0.5f && this.lightIntensity < 0.6f) {
            this.dayAmbience.stop();
            this.nightAmbience.stop();
        }
        if (this.lightIntensity > 0.6f && !this.dayAmbience.isPlaying()) {
            this.nightAmbience.stop();
            this.dayAmbience.play();
        }
        this.lightIntensity -= this.lightModifier;
        this.rayHandler.setAmbientLight(this.lightIntensity);
        this.realtimeStructureGenerator.update(400, 600, 96, this.builder);
        if (Gdx.input.isKeyPressed(129) && Gdx.input.isKeyJustPressed(52) && !this.aDialogShown) {
            this.saveWorldToFile();
        }
        if (Gdx.input.isKeyPressed(129) && Gdx.input.isKeyJustPressed(31) && !this.aDialogShown) {
            this.openWorldFromFile();
        }
        if (Gdx.input.isKeyJustPressed(50) && !this.aDialogShown) {
            this.showCommandDialog();
        }
        if (Gdx.input.isKeyJustPressed(44) && !this.aDialogShown) {
            this.blockSelectDialog();
        }
        if (Gdx.input.isKeyJustPressed(34) && !this.aDialogShown) {
            this.playerTorch.setActive(!this.playerTorch.isActive());
            if (this.playerTorch.isActive()) {
                this.soundManager.play("Torch Lit");
                this.player.animator.currentRow = 1;
            } else {
                this.soundManager.play("Torch Blown");
                this.player.animator.currentRow = 0;
            }
        }
        if (!this.aDialogShown) {
            this.player.handleMovement(this.camera, this.blockManager);
        }
    }

    public void show() {
    }

    public void render(float v) {
        this.render();
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
        for (Script script : scripts){
            script.scriptDestroyed();
        }

        this.player.dispose();
        this.blockManager.dispose();
        this.panelTexture.dispose();
        this.entityManager.dispose();
        this.stage.dispose();
        this.mainSkin.dispose();
        this.soundManager.dispose();
        this.batch.dispose();
    }

    public int round(int num, int to) {
        return (num + to / 2) / to * to;
    }

    public int getScreenCenterX() {
        return Gdx.graphics.getWidth() / 2;
    }

    public int getScreenCenterY() {
        return Gdx.graphics.getHeight() / 2;
    }

    public void saveWorldToFile() {
        this.aDialogShown = true;
        final TextField worldField = new TextField("", this.mainSkin);
        Dialog dialog = new Dialog("Save world", this.mainSkin) {

            protected void result(Object object) {
                GameScreen.this.aDialogShown = false;
                if (!((Boolean) object).booleanValue()) {
                    return;
                }
                GameScreen.this.worldManager.saveAs(worldField.getText(), WorldSaveParameterBuilder.build(GameScreen.this.lightIntensity, GameScreen.this.playerTorch.isActive(), GameScreen.this.playerTorch));
            }
        };
        dialog.getContentTable().add((Actor) worldField).pad(10.0f);
        dialog.button("Save", true);
        dialog.button("Cancel", false);
        dialog.show(this.stage);
    }

    public void openWorldFromFile() {
        this.aDialogShown = true;
        ArrayList<String> worlds = new ArrayList<String>();
        File file = new File("data/saves");
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File world : files) {
            worlds.add(world.getName());
        }
        final SelectBox worldSelector = new SelectBox(this.mainSkin);
        worldSelector.setItems(worlds.toArray(new String[0]));
        Dialog dialog = new Dialog("Open world", this.mainSkin) {

            protected void result(Object object) {
                GameScreen.this.aDialogShown = false;
                if (!((Boolean) object).booleanValue()) {
                    return;
                }
                GameScreen.this.lightIntensity = GameScreen.this.worldManager.loadFrom((String) worldSelector.getSelected(), GameScreen.this.builder, WorldSaveParameterBuilder.build(GameScreen.this.lightIntensity, GameScreen.this.playerTorch.isActive(), GameScreen.this.playerTorch));
            }
        };
        dialog.getContentTable().add((Actor) worldSelector).pad(10.0f);
        dialog.button("Load", true);
        dialog.button("Cancel", false);
        dialog.show(this.stage);
    }

    public void showCommandDialog() {
        this.aDialogShown = true;
        final SelectBox commandSelector = new SelectBox(this.mainSkin);
        commandSelector.setItems(new String[]{"go", "summon"});
        final TextField commandArgs = new TextField("", this.mainSkin);
        Dialog dialog = new Dialog("Command Dialog", this.mainSkin) {

            protected void result(Object object) {
                GameScreen.this.aDialogShown = false;
                if (!((Boolean) object).booleanValue()) {
                    return;
                }
                CommandExecutioner.execute((String) commandSelector.getSelected(), commandArgs.getText().split(" "), GameScreen.this.player, GameScreen.this.entityManager, GameScreen.this.blockManager);
            }
        };
        dialog.setWidth(300.0f);
        dialog.getContentTable().add((Actor) commandSelector).pad(10.0f);
        dialog.getContentTable().add((Actor) commandArgs).pad(5.0f);
        dialog.button("Execute", true);
        dialog.button("Cancel", false);
        dialog.show(this.stage);
    }

    public void blockSelectDialog() {
        this.aDialogShown = true;
        final SelectBox selectBox = new SelectBox(this.mainSkin);
        selectBox.setItems(this.blockManager.getBlockPathManager().getPathsSafe().toArray(new String[0]));
        Dialog dialog = new Dialog("Select Block", this.mainSkin) {

            protected void result(Object object) {
                GameScreen.this.aDialogShown = false;
                if (!((Boolean) object).booleanValue()) {
                    return;
                }
                GameScreen.this.blockManager.selectedIndex = GameScreen.this.blockManager.getBlockPathManager().blockPaths.indexOf(selectBox.getSelected());
                GameScreen.this.blockManager.updateCurrentSelected();
            }
        };
        dialog.getContentTable().add((Actor) selectBox).pad(10.0f);
        dialog.button("Select", true);
        dialog.button("Cancel", false);
        dialog.show(this.stage);
    }
}
