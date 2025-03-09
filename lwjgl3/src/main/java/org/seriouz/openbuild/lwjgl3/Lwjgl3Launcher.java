/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.badlogic.gdx.ApplicationListener
 *  com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
 *  com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
 */
package org.seriouz.openbuild.lwjgl3;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.seriouz.openbuild.Main;
import org.seriouz.openbuild.lwjgl3.StartupHelper;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) {
            return;
        }
        Lwjgl3Launcher.createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application((ApplicationListener)new Main(), Lwjgl3Launcher.getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Open Build");
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setWindowedMode(1280, 720);
        configuration.setWindowIcon(new String[]{"libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png"});
        configuration.setResizable(false);
        return configuration;
    }
}
