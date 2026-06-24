package dev.xplate.create_security.misc;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.ItemLike;

public interface IEndSickining {
    default long sickAmount() {return 10L;}

    default MutableComponent getGradName(ItemLike input) {
        return Utils.createGradiant(Utils.FiniraniumGrad, input);
    }
}
