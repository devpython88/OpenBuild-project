package org.seriouz.openbuild.utilities;

import com.badlogic.gdx.Gdx;

public class Pointer {
    public static float getX(){
        return Gdx.input.getX();
    }

    public static float getY(){
        return (Gdx.input.getY() - Gdx.graphics.getHeight()) * -1;
    }

}
