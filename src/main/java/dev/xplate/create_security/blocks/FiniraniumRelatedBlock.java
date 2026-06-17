package dev.xplate.create_security.blocks;

import dev.xplate.create_security.misc.Utils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;

import java.awt.*;

public class FiniraniumRelatedBlock extends Block {

    public FiniraniumRelatedBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MutableComponent getName() {
        return Utils.createGradiant(Utils.FiniraniumGrad, this);
    }
}
