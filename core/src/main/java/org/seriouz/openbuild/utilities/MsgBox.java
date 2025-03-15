/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.badlogic.gdx.Gdx
 *  com.badlogic.gdx.scenes.scene2d.Actor
 *  com.badlogic.gdx.scenes.scene2d.Stage
 *  com.badlogic.gdx.scenes.scene2d.ui.Dialog
 *  com.badlogic.gdx.scenes.scene2d.ui.Label
 *  com.badlogic.gdx.scenes.scene2d.ui.Skin
 */
package org.seriouz.openbuild.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MsgBox {
    public static void box(String title, String message, Stage stage) {
        Skin skin = new Skin(Gdx.files.internal("resources/gdx/default.json"));
        Label label = new Label((CharSequence)message, skin);
        Dialog dialog = new Dialog(title, skin);
        dialog.getContentTable().add((Actor)label);
        dialog.button("Okay", (Object)false);
        dialog.show(stage);
    }
}
