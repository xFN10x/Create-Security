package dev.xplate.create_security.reg;

import com.mojang.serialization.Codec;
import dev.xplate.create_security.misc.EndSicknessSyncHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Map;

import static dev.xplate.create_security.CSSecurity.MODID;

public class SecurityAttachmentTypes {

    private final static DeferredRegister<AttachmentType<?>> REG = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Long>> END_SICKNESS_COUNTER = REG.register(
            "end_sickness_counter", () -> AttachmentType.builder(() -> 0L).sync(new EndSicknessSyncHandler()).serialize(Codec.LONG).build()
    );

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Map<BlockPos, Long>>> END_SICKNESS_COUNTER_CHUNK = REG.register(
            "end_sickness_counter_chunk", () -> AttachmentType.builder(() -> Map.<BlockPos, Long>of()).serialize(Codec.unboundedMap(BlockPos.CODEC, Codec.LONG)).build()
    );

    public static void reg(IEventBus bus) {
        REG.register(bus);
    }
}
