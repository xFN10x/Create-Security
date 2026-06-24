package dev.xplate.create_security.items;

import dev.xplate.create_security.misc.IEndSickining;
import dev.xplate.create_security.misc.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class FiniraniumRelatedBlockItem extends BlockItem implements IEndSickining {

    public FiniraniumRelatedBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public long sickAmount() {
        long amount = 10L;
        if (getBlock() instanceof IEndSickining) {
            amount = ((IEndSickining) getBlock()).sickAmount();
        }
        return amount;
    }

    @Override
    public Component getName(ItemStack stack) {
        return getGradName(this);
    }
}
