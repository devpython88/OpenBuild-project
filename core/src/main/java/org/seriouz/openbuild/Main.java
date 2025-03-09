/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.badlogic.gdx.Game
 */
package org.seriouz.openbuild;

import com.badlogic.gdx.Game;
import org.seriouz.openbuild.screens.MenuScreen;

public class Main
extends Game {
    public void create() {
        this.setScreen(new MenuScreen(this));
    }
}
