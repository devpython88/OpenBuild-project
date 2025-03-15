package org.seriouz.openbuild.scripts.tables.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.luaj.vm2.LuaTable;
import org.seriouz.openbuild.scripts.funcs.FunctionBuilder;

public class TextViewTable extends LuaTable implements OBComponent {
    private Label label;
    private float x, y;
    private String text;

    public TextViewTable(Skin skin, Stage stage, float x, float y, String text) {
        this.x = x;
        this.y = y;
        this.text = text;

        this.label = new Label(text, skin);
        reloadProperties();
        stage.addActor(label);

        addGetFunctions();
        addSetFunctions();
        addFunctions();
        addMiscellaneousFunctions();
    }

    @Override
    public void addSetFunctions() {

    }

    @Override
    public void addGetFunctions() {

    }

    @Override
    public void addFunctions() {

    }

    @Override
    public void addMiscellaneousFunctions() {
        set("setVisibility", FunctionBuilder.oneArg(arg -> {
            label.setVisible(arg.toboolean());
            return NIL;
        }));

        set("setFontSize", FunctionBuilder.oneArg(size -> {
            label.setFontScale(size.tofloat() / 10);
            return NIL;
        }));
    }


    private void reloadProperties(){
        label.setPosition(x, y);
        label.setText(text);
    }
}
