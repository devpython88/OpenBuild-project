package org.seriouz.openbuild.utilities;

import org.seriouz.openbuild.managers.BlockPathManager;

public class BlockMerger {
    public static void merge(BlockPathManager blockPathManager, String slot1,
                             String slot2, String slot3){
        if (pattern(slot1, slot2, slot3,
            "Player Torch", "Lamp Pole", "--")){
            blockPathManager.changeBlock("-Torch Lamp", "+Torch Lamp");
        }

        if (pattern(slot1, slot2, slot3,
            "Dedris Flower", "Fire", "--")){
            blockPathManager.changeBlock("-Dedris Ash", "+Dedris Ash");
        }

        if (pattern(slot1, slot2, slot3,
            "+Dedris Ash", "+Torch Lamp", "--")){
            blockPathManager.changeBlock("-Dedris Core", "+Dedris Core");
        }

        if (pattern(slot1, slot2, slot3,
            "Oral Flower", "Fire", "--")){
            blockPathManager.changeBlock("-Oral Dust", "+Oral Dust");
        }
    }

    public static boolean pattern(String s1, String s2, String s3,
                                  String ts1, String ts2, String ts3){
        return s1.equals(ts1) && s2.equals(ts2) && s3.equals(ts3);
    }
}
