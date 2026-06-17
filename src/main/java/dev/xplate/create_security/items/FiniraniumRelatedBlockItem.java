package dev.xplate.create_security.items;

import dev.xplate.create_security.misc.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class FiniraniumRelatedBlockItem extends BlockItem {

    public FiniraniumRelatedBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Utils.createGradiant(Utils.FiniraniumGrad, this);
    }
}
