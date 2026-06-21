package dev.xplate.create_security.blocks;

import dev.xplate.create_security.misc.Utils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;

public class FiniraniumRelatedBlock extends Block {

    public FiniraniumRelatedBlock(Properties properties) {
        super(properties);
    }

    /**
     * Returns an int, being what to add to the end sickness counter for a player close to it.
     */
    public long sickAmount() {
        return 1;
    }

    @Override
    public MutableComponent getName() {
        return Utils.createGradiant(Utils.FiniraniumGrad, this);
    }
}
