package org.seriouz.openbuild.builders;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ScriptBuilder {
    public Skin mainSkin;
    public Stage mainStage;

    public ScriptBuilder(Skin mainSkin, Stage mainStage) {
        this.mainSkin = mainSkin;
        this.mainStage = mainStage;
    }
}
