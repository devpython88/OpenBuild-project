package org.seriouz.openbuild.builders;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.seriouz.openbuild.managers.SoundManager;

public class ScriptBuilder {
    public Skin mainSkin;
    public Stage mainStage;
    public SoundManager soundManager;


    public ScriptBuilder(Skin mainSkin, Stage mainStage, SoundManager soundManager) {
        this.mainSkin = mainSkin;
        this.mainStage = mainStage;
        this.soundManager = soundManager;
    }
}
