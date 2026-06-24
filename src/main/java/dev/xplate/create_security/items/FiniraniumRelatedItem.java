package dev.xplate.create_security.items;

import dev.xplate.create_security.misc.IEndSickining;
import dev.xplate.create_security.misc.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;


public class FiniraniumRelatedItem extends Item implements IEndSickining {
    private final long sickAmount;

    public FiniraniumRelatedItem(Item.Properties properties, long endSicknessAmount) {
        super(properties);
        this.sickAmount = endSicknessAmount;
    }

    public FiniraniumRelatedItem(Item.Properties properties) {
        super(properties);
        this.sickAmount = 10L;
    }

    @Override
    public long sickAmount() {
        return sickAmount;
    }

    @Override
    public Component getName(ItemStack stack) {
        return getGradName(this);
    }
}
