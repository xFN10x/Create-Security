package dev.xplate.create_security.reg;

import dev.xplate.create_security.effects.EndSickness;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dev.xplate.create_security.CSecurity.MODID;

public class SecurityEffects {
    protected static final DeferredRegister<MobEffect> REG = DeferredRegister.create(Registries.MOB_EFFECT, MODID);

    public static final DeferredHolder<MobEffect, EndSickness> END_SICKNESS = REG.register("end_sickness", EndSickness::new);

    public static void reg(IEventBus bus) {
        REG.register(bus);
    }

}
