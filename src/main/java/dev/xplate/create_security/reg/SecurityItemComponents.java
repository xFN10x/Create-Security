package dev.xplate.create_security.reg;

import dev.xplate.create_security.CSecurity;
import dev.xplate.create_security.items.datacomps.EyeOffsetComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SecurityItemComponents {
    protected static final DeferredRegister.DataComponents REG = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CSecurity.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EyeOffsetComponent>> EYE_OFFSET = REG.registerComponentType("eye_offset", b ->
            b.persistent(EyeOffsetComponent.CODEC)
                    .networkSynchronized(EyeOffsetComponent.STREAM_CODEC));

    public static void reg(IEventBus bus) {
        REG.register(bus);
    }
}
