package dev.xplate.create_security.misc;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.ItemLike;

public interface IEndSickining {
    default long sickAmount() {return 10L;}
    
    default int sickRange() {return Math.toIntExact(sickAmount()/5);}

    default MutableComponent getGradName(ItemLike input) {
        return Utils.createGradiant(Utils.FiniraniumGrad, input);
    }
}
