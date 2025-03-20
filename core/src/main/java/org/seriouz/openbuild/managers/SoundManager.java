/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.badlogic.gdx.Gdx
 *  com.badlogic.gdx.audio.Sound
 */
package org.seriouz.openbuild.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import org.seriouz.openbuild.utilities.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    public HashMap<String, Sound> soundHashMap = new HashMap();

    public SoundManager(String soundDir) {
        this.reloadAllSounds(soundDir);
    }

    public void reloadAllSounds(String soundDir) {
        File dir = new File(soundDir);
        assert (dir.exists());
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (!file.getName().endsWith(".wav")) continue;
            Logger.info(file.getName());
            this.soundHashMap.put(file.getName(), Gdx.audio.newSound(Gdx.files.absolute(file.getAbsolutePath())));
        }
    }

    public void play(String name) {
        this.soundHashMap.get(name + ".wav").play();
    }

    public void dispose() {
        for (Map.Entry<String, Sound> entry : this.soundHashMap.entrySet()) {
            entry.getValue().dispose();
        }
    }
}
