package dev.xplate.create_security.reg;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static dev.xplate.create_security.CSSecurity.MODID;

public class SecuritySoundEvents {
    
    protected static final DeferredRegister<SoundEvent> REG = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MODID);
    
    public static final DeferredHolder<SoundEvent, SoundEvent> WARNING_SOUND = REG.register("finiranium_warning", SoundEvent::createVariableRangeEvent);

    public static void reg(IEventBus bus) {
        REG.register(bus);
    }
}
