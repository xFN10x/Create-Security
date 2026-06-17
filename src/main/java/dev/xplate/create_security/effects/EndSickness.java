package dev.xplate.create_security.effects;

import dev.xplate.create_security.blocks.FiniraniumRelatedBlock;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EndSickness extends MobEffect {
    public EndSickness() {
        super(MobEffectCategory.HARMFUL, FiniraniumRelatedBlock.finiraniumGrad.getAtPercent(0.5f));
    }

    
}
