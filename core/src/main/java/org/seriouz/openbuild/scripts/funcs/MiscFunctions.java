package org.seriouz.openbuild.scripts.funcs;

import com.badlogic.gdx.math.Circle;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.seriouz.openbuild.Block;
import org.seriouz.openbuild.managers.BlockManager;
import org.seriouz.openbuild.managers.SoundManager;
import org.seriouz.openbuild.utilities.Logger;

import java.util.Random;

public class MiscFunctions {
    public static OneArgFunction playSound(SoundManager soundManager){
        return FunctionBuilder.oneArg(soundName -> {
            String jSoundName = soundName.tojstring();
            if (!soundManager.soundHashMap.containsKey(jSoundName + ".wav")){
                Logger.err("No sound found with name: " + jSoundName, "playSound");
                return LuaValue.NIL;
            }

            soundManager.play(jSoundName);

            return LuaValue.NIL;
        });
    }


    public static TwoArgFunction isBlockNearby(BlockManager blockManager, Block block){
        return FunctionBuilder.twoArg((blockName, radius) -> {
            Circle range = new Circle(block.x + 8, block.y + 8, radius.tofloat() * 16);

            return LuaValue.valueOf(blockManager.isBlockInRange(range, blockName.tojstring()));
        });
    }

    public static TwoArgFunction randomVal(){
        return FunctionBuilder.twoArg((origin, bound) -> LuaValue.valueOf(new Random().nextInt(origin.toint(), bound.toint())));
    }
}
