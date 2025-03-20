package org.seriouz.openbuild.scripts.funcs;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.seriouz.openbuild.managers.SoundManager;
import org.seriouz.openbuild.utilities.Logger;

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
}
