package org.seriouz.openbuild.scripts.tables.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.seriouz.openbuild.scripts.funcs.FunctionBuilder;

public class ButtonTable extends LuaTable implements OBComponent {
    private Skin skin;
    private Stage stage;
    private String text;
    private LuaFunction callback;
    private TextButton textButton;
    private float x, y;

    public ButtonTable(Skin skin, Stage stage, String text, LuaFunction callback, float x, float y) {
        this.skin = skin;
        this.stage = stage;
        this.text = text;
        this.x = x;
        this.y = y;
        this.callback = callback;

        textButton = new TextButton(text, skin);
        reloadProperties();

        stage.addActor(textButton);


        addSetFunctions();
        addGetFunctions();
        addFunctions();
        addMiscellaneousFunctions();
    }

    private void reloadProperties() {
        textButton.setPosition(x, y);
        textButton.clearListeners();
        textButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                callback.call();
            }
        });
    }

    @Override
    public void addFunctions() {
        set("update", FunctionBuilder.zeroArg(() -> {
            reloadProperties();
            return NIL;
        }));
    }

    @Override
    public void addMiscellaneousFunctions() {
        set("setVisibility", FunctionBuilder.oneArg(arg -> {
            textButton.setVisible(arg.toboolean());
            return NIL;
        }));

        set("setFontSize", FunctionBuilder.oneArg(size -> {
            textButton.getLabel().setFontScale(size.tofloat() / 10);
            return NIL;
        }));
    }

    @Override
    public void addGetFunctions() {
        set("getText", FunctionBuilder.zeroArg(() -> LuaValue.valueOf(text)));
        set("getCallback", FunctionBuilder.zeroArg(() -> callback));
        set("getX", FunctionBuilder.zeroArg(() -> LuaValue.valueOf(x)));
        set("getY", FunctionBuilder.zeroArg(() -> LuaValue.valueOf(y)));
    }
    @Override
    public void addSetFunctions() {
        ButtonTable tempThis = this;
        set("setText", FunctionBuilder.oneArg(text -> {
            tempThis.text = text.tojstring();
            return NIL;
        }));

        set("setCallback", FunctionBuilder.oneArg(func -> {
            tempThis.callback = func.checkfunction();
            return NIL;
        }));

        set("setX", FunctionBuilder.oneArg(newX -> {
            tempThis.x = newX.tofloat();
            return NIL;
        }));

        set("setY", FunctionBuilder.oneArg(newY -> {
            tempThis.y = newY.tofloat();
            return NIL;
        }));

    }
}
