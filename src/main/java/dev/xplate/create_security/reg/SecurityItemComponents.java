package dev.xplate.create_security.reg;

import com.mojang.serialization.Codec;
import dev.xplate.create_security.CSSecurity;
import dev.xplate.create_security.items.datacomps.EyeOffsetComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SecurityItemComponents {
    protected static final DeferredRegister.DataComponents REG = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CSSecurity.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EyeOffsetComponent>> EYE_OFFSET = REG.registerComponentType("eye_offset", b ->
            b.persistent(EyeOffsetComponent.CODEC)
                    .networkSynchronized(EyeOffsetComponent.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> FINIRANIUM_LEVEL = REG.registerComponentType("finiranium_level", b ->
            b.persistent(Codec.FLOAT)
                    .networkSynchronized(ByteBufCodecs.FLOAT));

    public static void reg(IEventBus bus) {
        REG.register(bus);
    }
}
