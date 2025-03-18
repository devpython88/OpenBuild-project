package org.seriouz.openbuild.builders;

import org.seriouz.openbuild.managers.BlockManager;

public class PlayerParamBuilder {
    public BlockManager blockManager;

    public PlayerParamBuilder(BlockManager blockManager) {
        this.blockManager = blockManager;
    }
}
