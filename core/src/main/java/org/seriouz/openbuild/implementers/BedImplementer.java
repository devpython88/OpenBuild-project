package org.seriouz.openbuild.implementers;

import com.badlogic.gdx.Game;
import org.seriouz.openbuild.Block;
import org.seriouz.openbuild.screens.GameScreen;
import org.seriouz.openbuild.screens.MessageScreen;

public class BedImplementer {
    public static void implement(Block host, GameScreen screen, Game game){
        game.setScreen(new MessageScreen(3f, "Sleeping...", game, screen));
    }
}
