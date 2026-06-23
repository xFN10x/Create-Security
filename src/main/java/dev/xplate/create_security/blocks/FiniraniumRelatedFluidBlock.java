package dev.xplate.create_security.blocks;

import dev.xplate.create_security.misc.Utils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

public class FiniraniumRelatedFluidBlock extends LiquidBlock {

    private final long sickAmount;

    public FiniraniumRelatedFluidBlock(FlowingFluid fluid, Properties properties) {
        this(fluid,properties, 1L);
    }

    public FiniraniumRelatedFluidBlock(FlowingFluid fluid, Properties properties, long sickAmount) {
        super(fluid, properties);
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
        return Utils.createGradiant(Utils.FiniraniumGrad, this);
    }
}
