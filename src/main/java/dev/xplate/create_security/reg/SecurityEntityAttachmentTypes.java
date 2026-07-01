package dev.xplate.create_security.reg;

import com.mojang.serialization.Codec;
import dev.xplate.create_security.misc.EndSicknessSyncHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static dev.xplate.create_security.CSSecurity.MODID;

public class SecurityEntityAttachmentTypes {

    private final static DeferredRegister<AttachmentType<?>> REG = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Long>> END_SICKNESS_COUNTER = REG.register(
            "end_sickness_counter", () -> AttachmentType.builder(() -> 0L).sync(new EndSicknessSyncHandler()).serialize(Codec.LONG).build()
    );

    public static void reg(IEventBus bus) {
        REG.register(bus);
    }
}
