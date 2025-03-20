package org.seriouz.openbuild.screens;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import org.seriouz.openbuild.Block;
import org.seriouz.openbuild.CommandExecutioner;
import org.seriouz.openbuild.builders.BlockParameterBuilder;
import org.seriouz.openbuild.builders.PlayerParamBuilder;
import org.seriouz.openbuild.builders.ScriptBuilder;
import org.seriouz.openbuild.builders.WorldSaveParameterBuilder;
import org.seriouz.openbuild.dialogs.PauseMenuDialog;
import org.seriouz.openbuild.generators.RealtimeStructureGenerator;
import org.seriouz.openbuild.generators.StructureGenerator;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.managers.EntityManager;
import org.seriouz.openbuild.managers.SoundManager;
import org.seriouz.openbuild.scripts.Script;
import org.seriouz.openbuild.scripts.ScriptManager;
import org.seriouz.openbuild.user.Player;
import org.seriouz.openbuild.user.WorldManager;
import org.seriouz.openbuild.utilities.BlockMerger;
import org.seriouz.openbuild.utilities.Commands;
import org.seriouz.openbuild.utilities.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameScreen
    implements Screen {
    public Skin mainSkin;
    public Stage stage;
    private SpriteBatch batch;
    private Player player;
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private BlockManager blockManager;
    private final Texture panelTexture;
    private BlockParameterBuilder builder;
    private WorldManager worldManager;
    private EntityManager entityManager;
    private BitmapFont fontRenderer;
    private RealtimeStructureGenerator realtimeStructureGenerator;
    private SoundManager soundManager;
    private final World world = new World(new Vector2(0.0f, 0.0f), false);
    private final RayHandler rayHandler = new RayHandler(this.world);
    private PointLight playerTorch;
    private Music dayAmbience;
    private Music nightAmbience;
    private final ScriptManager scriptManager = new ScriptManager("data/scripts/");
    public boolean paused;
    private BlockParameterBuilder.Sunlight lightIntensity;
    private float lightModifier;
    private boolean aDialogShown;
    private final List<Script> scripts;
    public ScriptBuilder scriptParamBuilder;
    private PlayerParamBuilder playerParamBuilder;

    public GameScreen(Game game) {
        scripts = new ArrayList<>();

        initializeWorldLighting();
        intializeStageAndSkin();
        initializeSounds();
        loadGlobalScripts();
        initializeBlockManager(game);
        intializeUser();

        this.panelTexture = new Texture("./resources/UI/panel.png");

        new Thread(this::initializeStructures).start();
        initializeEntities();
        initializeWorldManager();
        initializeTextRenderer();

        Gdx.input.setInputProcessor(this.stage);

        initializePlayerTorch();
        loadAmbienceSounds();
        initializeMisc();
    }

    private void initializeMisc() {
        this.aDialogShown = false;

        this.paused = false;
    }

    private void loadAmbienceSounds() {
        this.dayAmbience = Gdx.audio.newMusic(Gdx.files.internal("resources/audio/Day Ambience.mp3"));

        this.nightAmbience = Gdx.audio.newMusic(Gdx.files.internal("resources/audio/Night Ambience.mp3"));

        this.dayAmbience.setLooping(true);

        this.nightAmbience.setLooping(true);

        this.dayAmbience.play();
    }

    private void initializePlayerTorch() {
        Color torchColor = new Color(0.9f, 0.4f, 0.0f, 1.0f);

        this.playerTorch = new PointLight(this.rayHandler, 100, torchColor, 100.0f, (float) this.player.x, (float) this.player.y);

        this.playerTorch.setActive(false);

        this.playerTorch.setSoftnessLength(0.0f);
    }

    private void initializeWorldLighting() {
        this.lightIntensity = new BlockParameterBuilder.Sunlight(1f);

        this.lightModifier = 0.00005f;

        this.rayHandler.setAmbientLight(this.lightIntensity.v);
    }

    private void initializeSounds() {
        this.soundManager = new SoundManager("resources/audio/");
    }

    private void initializeTextRenderer() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("resources/fonts/default.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.BLACK;


        this.fontRenderer = generator.generateFont(parameter);



        generator.dispose();
    }

    private void initializeWorldManager() {
        this.worldManager = new WorldManager(this.blockManager, this.player, this.entityManager);
    }

    private void initializeEntities() {
        this.entityManager = new EntityManager("data/entities", "resources/entities");
    }

    private void initializeStructures() {
        StructureGenerator globalStructureGenerator = new StructureGenerator(this.blockManager);
        globalStructureGenerator.generateAllStructures(400, 600, 256, this.player.x, this.player.y, this.builder);


        this.realtimeStructureGenerator = new RealtimeStructureGenerator(globalStructureGenerator, this.player);
    }

    private void loadGlobalScripts() {
        scriptParamBuilder = new ScriptBuilder(mainSkin, stage, soundManager);

        loadScriptsFrom(scriptManager.scriptPaths);
    }

    private void loadScriptsFrom(List<String> paths) {
        for (String scriptPath : paths) {

            Script script = new Script(scriptPath, scriptParamBuilder);
            if (script.globals.get("host_block").isstring()) continue;
            script.scriptLoaded();
            scripts.add(script);
        }
    }

    private void initializeBlockManager(Game game) {
        this.blockManager = new BlockManager();
        this.builder = new BlockParameterBuilder(rayHandler, blockManager, scriptManager, scriptParamBuilder, soundManager, lightIntensity, this, game);
    }

    private void intializeStageAndSkin() {
        this.stage = new Stage();
        this.mainSkin = new Skin(Gdx.files.internal("resources/gdx/default.json"));
    }

    private void intializeUser() {
        this.batch = new SpriteBatch();
        playerParamBuilder = new PlayerParamBuilder(blockManager);
        this.player = new Player(this.round(this.getScreenCenterX(), 16), this.round(this.getScreenCenterY(), 16), "player.png", playerParamBuilder);
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, (float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());
        this.camera.zoom = 0.5f;
        this.uiCamera = new OrthographicCamera();
        this.uiCamera.setToOrtho(false, (float) Gdx.graphics.getWidth(), (float) Gdx.graphics.getHeight());

        updateCurrentInventorySlot();
    }

    public void render() {
        if (Gdx.input.isKeyJustPressed(111)) {
            new PauseMenuDialog(this);
        }

        if (!this.paused) {
            this.batch.setProjectionMatrix(this.camera.combined);
            this.batch.begin();

            ScreenUtils.clear(0.15f, 0.5f, 0.15f, 1.0f);

            updateMainStuff();
            renderMainStuff();

            this.batch.end();

            updateRayHandler();
        }

        this.batch.setProjectionMatrix(this.uiCamera.combined);
        this.batch.begin();

        handleCurrentSelectedTexture();
        drawCoords();
        handleStage();

        this.batch.end();
    }

    private void updateMainStuff() {
        this.update();
        this.player.handleBlocks(this.blockManager, this.builder);
        this.camera.update();
        this.blockManager.handle();
        this.player.getInventory().handle();

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB))
            updateCurrentInventorySlot();
    }

    private void updateCurrentInventorySlot() {
        blockManager.currentSelected = player.getInventory().getCurrentSlot().getItemName();
        blockManager.updateCurrentSelected();
        player.getInventory().getCurrentSlot().setItem(blockManager.getBlockPathManager().get(blockManager.currentSelected));
        player.getInventory().getCurrentSlot().setItemName(blockManager.currentSelected);
    }

    private void renderMainStuff() {
        this.player.draw(this.batch);
        this.blockManager.drawBlocks(this.batch, this.player.x, this.player.y, this.soundManager);
        this.entityManager.draw(this.batch, this.player.x, this.player.y);
    }

    private void updateRayHandler() {
        this.rayHandler.setCombinedMatrix(this.camera);
        this.rayHandler.updateAndRender();
    }

    private void handleStage() {
        this.stage.act(Gdx.graphics.getDeltaTime());
        this.stage.draw();
    }

    private void drawCoords() {
        this.fontRenderer.draw(this.batch, "Coords: " + this.player.x / 16 + " : " + this.player.y / 16, 0.0f, (float) (Gdx.graphics.getHeight() - 32));
    }

    private void handleCurrentSelectedTexture() {
        this.batch.draw(this.panelTexture, 0.0f, 0.0f, (float) Gdx.graphics.getWidth(), 88.0f);

        player.getInventory().draw(batch, 20, 20, 48, 48);
    }

    private void update() {
        handleLightIntensityAndAmbientSounds();
        handleDialogs();
        handleTorchSFX();
        handleMovement();

        if (Gdx.input.isKeyJustPressed(30)) {
            for (Block block : blockManager.blocks){
                block.dispose();
            }

            blockManager.blocks.clear();
        }

        for (Script script : scripts) script.update();
        this.playerTorch.setPosition((float) (this.player.x + this.player.getCenterX()), (float) (this.player.y + this.player.getCenterY()));
        if (realtimeStructureGenerator != null) this.realtimeStructureGenerator.update(400, 600, 64, this.builder);
    }

    private void handleMovement() {
        if (!this.aDialogShown) {
            this.player.handleMovement(this.camera, this.blockManager);
        }
    }

    private void handleTorchSFX() {

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
    }

    private void handleDialogs() {
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
        if (Gdx.input.isKeyJustPressed(37) && !aDialogShown){
            this.showMergeBlockDialog();
        }
    }

    private void handleLightIntensityAndAmbientSounds() {
        if (this.lightIntensity.v < 0.1f) {
            this.lightModifier = -this.lightModifier;
        }
        if (this.lightIntensity.v > 1.0f) {
            this.lightModifier = -this.lightModifier;
        }
        if (this.lightIntensity.v < 0.5f && !this.nightAmbience.isPlaying()) {
            this.dayAmbience.stop();
            this.nightAmbience.play();
        }
        if (this.lightIntensity.v > 0.5f && this.lightIntensity.v < 0.6f) {
            this.dayAmbience.stop();
            this.nightAmbience.stop();
        }
        if (this.lightIntensity.v > 0.6f && !this.dayAmbience.isPlaying()) {
            this.nightAmbience.stop();
            this.dayAmbience.play();
        }

        Color color = fontRenderer.getColor();
        color.r -= lightModifier;
        color.g -= lightModifier;
        color.b -= lightModifier;

        fontRenderer.setColor(color);

        this.lightIntensity.v -= this.lightModifier;
        this.rayHandler.setAmbientLight(this.lightIntensity.v);
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
                if (!(Boolean) object) {
                    return;
                }
                GameScreen.this.worldManager.saveAs(worldField.getText(), WorldSaveParameterBuilder.build(GameScreen.this.lightIntensity.v, GameScreen.this.playerTorch.isActive(), GameScreen.this.playerTorch));
            }
        };
        dialog.getContentTable().add((Actor) worldField).pad(10.0f);
        dialog.button("Save", true);
        dialog.button("Cancel", false);
        dialog.show(this.stage);
    }

    public void openWorldFromFile() {
        this.aDialogShown = true;
        ArrayList<String> worlds = new ArrayList<>();
        File file = new File("data/saves");
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File world : files) {
            worlds.add(world.getName());
        }
        SelectBox<String> worldSelector = new SelectBox<>(this.mainSkin);
        worldSelector.setItems(worlds.toArray(new String[0]));
        Dialog dialog = new Dialog("Open world", this.mainSkin) {

            protected void result(Object object) {
                GameScreen.this.aDialogShown = false;
                if (!(Boolean) object) {
                    return;
                }
                GameScreen.this.lightIntensity.v = GameScreen.this.worldManager.loadFrom(worldSelector.getSelected(), GameScreen.this.builder, WorldSaveParameterBuilder.build(GameScreen.this.lightIntensity.v, GameScreen.this.playerTorch.isActive(), GameScreen.this.playerTorch));
            }
        };
        dialog.getContentTable().add((Actor) worldSelector).pad(10.0f);
        dialog.button("Load", true);
        dialog.button("Cancel", false);
        dialog.show(this.stage);
    }

    public void showCommandDialog() {
        this.aDialogShown = true;
        SelectBox<String> commandSelector = new SelectBox<>(mainSkin);
        commandSelector.setItems(Commands.GLOBAL);

        final TextField commandArgs = new TextField("", this.mainSkin);
        stage.setKeyboardFocus(commandArgs);

        Dialog dialog = new Dialog("Command Dialog", this.mainSkin) {

            protected void result(Object object) {
                GameScreen.this.aDialogShown = false;
                if (!(Boolean) object) {
                    return;
                }
                CommandExecutioner.execute(commandSelector.getSelected(), commandArgs.getText().split(" "), GameScreen.this.player, GameScreen.this.entityManager, GameScreen.this.blockManager, GameScreen.this.builder);
            }
        };

        dialog.getContentTable().add((Actor) commandSelector).pad(10.0f);
        dialog.getContentTable().add((Actor) commandArgs).pad(5.0f);
        dialog.button("Execute", true);
        dialog.button("Cancel", false);
        dialog.show(this.stage);
    }

    public void blockSelectDialog() {
        this.aDialogShown = true;
        SelectBox<String> selectBox = new SelectBox<>(this.mainSkin);
        selectBox.setItems(this.blockManager.getBlockPathManager().getPathsSafe().toArray(new String[0]));
        Image imagePreviewWidget = new Image(new TextureRegionDrawable(blockManager.getBlockPathManager().get(selectBox.getSelected())));
        imagePreviewWidget.setSize(48, 48);
        imagePreviewWidget.setScaling(Scaling.fit);

        selectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                imagePreviewWidget.setDrawable(new TextureRegionDrawable(blockManager.getBlockPathManager().get(selectBox.getSelected())));
            }
        });


        Dialog dialog = new Dialog("Select Block", this.mainSkin) {

            protected void result(Object object) {
                GameScreen.this.aDialogShown = false;
                if (!(Boolean) object) {
                    return;
                }

                GameScreen.this.blockManager.selectedIndex = GameScreen.this.blockManager.getBlockPathManager().blockPaths.indexOf(selectBox.getSelected());
                updateCurrentInventorySlot();
            }
        };

        dialog.getContentTable().add((Actor) selectBox).pad(2.5f);
        dialog.getContentTable().row();
        dialog.getContentTable().add(imagePreviewWidget).size(48, 48);

        dialog.button("Select", true);
        dialog.button("Cancel", false);

        dialog.show(this.stage);
    }

    private void showMergeBlockDialog(){
        aDialogShown = true;

        Table mergeBlockTable = new Table(mainSkin);

        SelectBox<String> slot1 = new SelectBox<>(mainSkin);
        SelectBox<String> slot2 = new SelectBox<>(mainSkin);
        SelectBox<String> slot3 = new SelectBox<>(mainSkin);

        List<String> items = blockManager.getBlockPathManager().getPathsSafe();
        items.add("--");
        items.add("Player Torch");
        items.add("Fire");

        slot1.setItems(items.toArray(new String[0]));
        slot1.setSelected("--");

        slot2.setItems(items.toArray(new String[0]));
        slot2.setSelected("--");

        slot3.setItems(items.toArray(new String[0]));
        slot3.setSelected("--");

        mergeBlockTable.add(slot1).pad(10);
        mergeBlockTable.add(slot2).pad(10);
        mergeBlockTable.add(slot3).pad(10);

        // Dialog
        Dialog dialog = new Dialog("Merge Menu", mainSkin) {
            @Override
            protected void result(Object object) {
                aDialogShown = false;
                if (!(boolean) object) return;

                BlockMerger.merge(blockManager.getBlockPathManager(), slot1.getSelected(), slot2.getSelected(), slot3.getSelected());
            }
        };

        dialog.getContentTable().add(mergeBlockTable);
        dialog.button("Merge", true);
        dialog.button("Cancel", false);

        dialog.show(stage);
    }
}
