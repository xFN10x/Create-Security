package dev.xplate.create_security.effects;

import dev.xplate.create_security.blocks.FiniraniumRelatedBlock;
import dev.xplate.create_security.misc.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EndSickness extends MobEffect {
    public EndSickness() {
        super(MobEffectCategory.HARMFUL, Utils.FiniraniumGrad.getAtPercent(.8f));
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return super.shouldApplyEffectTickThisTick(duration, amplifier);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        return super.applyEffectTick(livingEntity, amplifier);
    }
}
