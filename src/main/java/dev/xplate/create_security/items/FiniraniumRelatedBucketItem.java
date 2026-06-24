package dev.xplate.create_security.items;

import dev.xplate.create_security.misc.IEndSickining;
import dev.xplate.create_security.misc.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;


public class FiniraniumRelatedBucketItem extends BucketItem implements IEndSickining {

    private final long sickAmount;

    public FiniraniumRelatedBucketItem(Fluid fluid, Properties properties) {
        this(fluid,properties, 1L);
    }
    public FiniraniumRelatedBucketItem(Fluid fluid, Properties properties, long sickAmount) {
        super(fluid,properties);
        this.sickAmount = sickAmount;
    }

    public long sickAmount() {
        return sickAmount;
    }

    @Override
    public Component getName(ItemStack stack) {
        return getGradName(this);
    }
}
