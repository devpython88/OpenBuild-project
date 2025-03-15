/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.badlogic.gdx.graphics.Texture
 *  org.json.JSONArray
 *  org.json.JSONObject
 */
package org.seriouz.openbuild.user;

import com.badlogic.gdx.graphics.Texture;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.seriouz.openbuild.Block;
import org.seriouz.openbuild.Entity;
import org.seriouz.openbuild.builders.BlockParameterBuilder;
import org.seriouz.openbuild.builders.WorldSaveParameterBuilder;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.managers.EntityManager;
import org.seriouz.openbuild.user.Player;

public class WorldManager {
    public BlockManager blockManager;
    public EntityManager entityManager;
    public Player player;

    public WorldManager(BlockManager blockManager, Player player, EntityManager entityManager) {
        this.blockManager = blockManager;
        this.player = player;
        this.entityManager = entityManager;
    }

    public void saveAs(String name, WorldSaveParameterBuilder builder) {
        JSONArray blocksJson = new JSONArray();
        for (Block block : this.blockManager.blocks) {
            JSONObject blockInfo = new JSONObject();
            blockInfo.put("Id", (Object)block.imageName);
            blockInfo.put("X", block.x / 16);
            blockInfo.put("Y", block.y / 16);
            blocksJson.put((Object)blockInfo);
        }
        JSONArray entityJson = new JSONArray();
        for (Entity entity : this.entityManager.entities) {
            JSONObject entityInfo = new JSONObject();
            entityInfo.put("Data", (Object)entity.dataFile);
            entityInfo.put("X", entity.x / 16);
            entityInfo.put("Y", entity.y / 16);
            entityJson.put((Object)entityInfo);
        }
        JSONObject playerJson = new JSONObject();
        playerJson.put("Skin Id", (Object)this.player.skinPath);
        playerJson.put("X", this.player.x);
        playerJson.put("Y", this.player.y);
        playerJson.put("Torch lit", builder.torchLit);


        JSONArray unlocked = new JSONArray();
        for (String path : blockManager.getBlockPathManager().blockPaths){
            if (path.startsWith("+")){
                unlocked.put(path);
            }
        }

        playerJson.put("Unlocked", unlocked);

        JSONObject envJson = new JSONObject();
        envJson.put("Sunlight", builder.lightIntensity);
        try {
            if (!new File("data/saves/" + name).exists()) {
                Files.createDirectory(Paths.get("data", "saves", name), new FileAttribute[0]);
            }
            this.writeJsonToFile("player", name, playerJson);
            this.writeJsonToFile("blocks", name, blocksJson);
            this.writeJsonToFile("entities", name, entityJson);
            this.writeJsonToFile("environment", name, envJson);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not save world.\n" + e.getMessage(), "Error", 0);
        }
    }

    private void writeJsonToFile(String name, String saveName, JSONObject json) throws IOException {
        Files.write(Paths.get("data", "saves", saveName, name + ".json"), json.toString(4).getBytes(), new OpenOption[0]);
    }

    private void writeJsonToFile(String name, String saveName, JSONArray json) throws IOException {
        Files.write(Paths.get("data", "saves", saveName, name + ".json"), json.toString(4).getBytes(), new OpenOption[0]);
    }

    private JSONObject readJsonFromFile(String name, String saveName) throws IOException {
        return new JSONObject(Files.readString(Paths.get("data", "saves", saveName, name + ".json")));
    }

    private JSONArray readJsonFromFileArr(String name, String saveName) throws IOException {
        return new JSONArray(Files.readString(Paths.get("data", "saves", saveName, name + ".json")));
    }

    public float loadFrom(String dirName, BlockParameterBuilder builder, WorldSaveParameterBuilder saveParameterBuilder) {
        int x;
        int i;
        File dir = new File(Paths.get("data", "saves", dirName).toAbsolutePath().toString());
        if (!dir.exists()) {
            JOptionPane.showMessageDialog(null, "The specified world does not exist", "Error", 0);
            return 0.0f;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            JOptionPane.showMessageDialog(null, "Could not list files from the world directory", "Error", 0);
            return 0.0f;
        }
        JSONArray blocksJson = null;
        JSONArray entitiesJson = null;
        JSONObject playerJson = null;
        JSONObject envJson = null;
        try {
            for (File file : files) {
                if (file.getName().equals("blocks.json")) {
                    blocksJson = this.readJsonFromFileArr("blocks", dirName);
                }
                if (file.getName().equals("player.json")) {
                    playerJson = this.readJsonFromFile("player", dirName);
                }
                if (file.getName().equals("entities.json")) {
                    entitiesJson = this.readJsonFromFileArr("entities", dirName);
                }
                if (!file.getName().equals("environment.json")) continue;
                envJson = this.readJsonFromFile("environment", dirName);
            }
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "IOException: " + e.getMessage(), "Error", 0);
        }
        if (blocksJson == null) {
            JOptionPane.showMessageDialog(null, "Unable to read blocks", "Error", 0);
            return saveParameterBuilder.lightIntensity;
        }
        if (entitiesJson == null) {
            JOptionPane.showMessageDialog(null, "Unable to read entities", "Error", 0);
            return saveParameterBuilder.lightIntensity;
        }
        if (playerJson == null) {
            JOptionPane.showMessageDialog(null, "Unable to read player info", "Error", 0);
            return saveParameterBuilder.lightIntensity;
        }
        this.blockManager.blocks.clear();
        this.entityManager.entities.clear();

        this.player.x = playerJson.getInt("X");
        this.player.y = playerJson.getInt("Y");
        this.player.skinPath = playerJson.getString("Skin Id");
        this.player.skin = new Texture("resources/player/" + this.player.skinPath);

        if (playerJson.has("Unlocked")) { // maintain compatiblity
            JSONArray unlocked = playerJson.getJSONArray("Unlocked");
            for (i = 0; i < unlocked.length(); i++){
                String v = unlocked.getString(i);
                blockManager.getBlockPathManager().changeBlock(v.replace("+", "-"), v);
            }
        }

        if (playerJson.getBoolean("Torch lit")) {
            this.player.animator.currentRow = 1;
            saveParameterBuilder.torch.setActive(true);
        }
        if (envJson != null) {
            saveParameterBuilder.lightIntensity = envJson.getFloat("Sunlight");
            builder.rayHandler.setAmbientLight(envJson.getFloat("Sunlight"));
        }
        for (i = 0; i < blocksJson.length(); ++i) {
            JSONObject blockInfo = blocksJson.getJSONObject(i);
            x = blockInfo.getInt("X") * 16;
            int y = blockInfo.getInt("Y") * 16;
            String id = blockInfo.getString("Id").replace("./resources/blocks/", "").replace(".png", "");
            this.blockManager.createBlock(x, y, this.blockManager.getBlockPathManager().get(id), id, builder);
        }
        for (i = 0; i < entitiesJson.length(); ++i) {
            JSONObject entityInfo = entitiesJson.getJSONObject(i);
            x = entityInfo.getInt("X") * 16;
            int y = entityInfo.getInt("Y") * 16;
            String dataFile = entityInfo.getString("Data");
            String[] paths = dataFile.split("/");
            String entityName = paths[paths.length - 1].replace(".obentity", "");
            this.entityManager.createEntity(entityName, x, y, this.blockManager);
        }
        return saveParameterBuilder.lightIntensity;
    }
}
