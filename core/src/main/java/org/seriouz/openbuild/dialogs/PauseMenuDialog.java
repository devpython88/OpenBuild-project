/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.badlogic.gdx.scenes.scene2d.ui.Dialog
 */
package org.seriouz.openbuild.dialogs;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import org.seriouz.openbuild.screens.GameScreen;

public class PauseMenuDialog {
    protected GameScreen gameScreen;
    protected Dialog dialog;
    public static final int RESULT_SAVE = 0;
    public static final int RESULT_OPEN = 1;
    public static final int RESULT_RESUME = 2;

    public PauseMenuDialog(GameScreen screen) {
        this.gameScreen = screen;
        this.gameScreen.paused = true;
        this.dialog = new Dialog("Pause Menu", screen.mainSkin){

            protected void result(Object object) {
                PauseMenuDialog.this.gameScreen.paused = false;
                if ((Integer)object != 2) {
                    if ((Integer)object == 0) {
                        PauseMenuDialog.this.gameScreen.saveWorldToFile();
                    }
                    if ((Integer)object == 1) {
                        PauseMenuDialog.this.gameScreen.openWorldFromFile();
                    }
                }
            }
        };
        this.dialog.setMovable(false);
        this.dialog.button("Save world", (Object)0);
        this.dialog.button("Open world", (Object)1);
        this.dialog.button("Resume", (Object)2);
        this.dialog.show(this.gameScreen.stage);
    }
}
