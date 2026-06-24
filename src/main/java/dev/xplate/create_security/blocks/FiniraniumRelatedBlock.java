package dev.xplate.create_security.blocks;

import dev.xplate.create_security.misc.IEndSickining;
import dev.xplate.create_security.misc.Utils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;

public class FiniraniumRelatedBlock extends Block implements IEndSickining {

    private final long sickAmount;

    public FiniraniumRelatedBlock(Properties properties) {
        this(properties, 1L);
    }

    public FiniraniumRelatedBlock(Properties properties, long sickAmount) {
        super(properties);
        this.sickAmount = sickAmount;
    }

    /**
     * Returns an int, being what to add to the end sickness counter for a player close to it.
     */
    public long sickAmount() {
        return sickAmount;
    }

    @Override
    public MutableComponent getName() {
        return getGradName(this);
    }
}
