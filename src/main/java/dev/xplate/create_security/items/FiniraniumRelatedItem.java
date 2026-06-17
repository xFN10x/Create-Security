package dev.xplate.create_security.items;

import dev.xplate.create_security.misc.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;


public class FiniraniumRelatedItem extends Item {
    public FiniraniumRelatedItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Utils.createGradiant(Utils.FiniraniumGrad, this);
    }
}
