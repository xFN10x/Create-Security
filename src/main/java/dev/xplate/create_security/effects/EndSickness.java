package dev.xplate.create_security.effects;

import dev.xplate.create_security.blocks.FiniraniumRelatedBlock;
import dev.xplate.create_security.misc.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;

public class EndSickness extends MobEffect {
    public EndSickness() {
        super(MobEffectCategory.HARMFUL, Utils.FiniraniumGrad.getAtPercent(.8f));
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        float maxHealth = livingEntity.getMaxHealth();
        float targetHealth = maxHealth / (amplifier + 1.2f);
        float currentHealth = livingEntity.getHealth();
        if (currentHealth > targetHealth) {
            livingEntity.hurt(livingEntity.damageSources().dragonBreath(), amplifier + 1);
        }
        if (livingEntity instanceof Player plr) {
            FoodData foodData = plr.getFoodData();
            float currentSat = foodData.getSaturationLevel();
            foodData.setSaturation(currentSat - 1);
        }
        return true;
    }
}
