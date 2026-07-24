package dev.xplate.create_security.misc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xplate.create_security.items.datacomps.EyeOffsetComponent;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public record LogEntry(String message, String targetName, Boolean targetPlayer) {
    public static final Codec<LogEntry> CODEC = RecordCodecBuilder.create(o ->
            o.group(
                            Codec.STRING.fieldOf("message").forGetter(LogEntry::message),
                            Codec.STRING.fieldOf("targetName").forGetter(LogEntry::targetName),
                            Codec.BOOL.fieldOf("targetPlayer").forGetter(LogEntry::targetPlayer)
                    )
                    .apply(o, LogEntry::new));
    public static final StreamCodec<ByteBuf, LogEntry> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, LogEntry::message,
            ByteBufCodecs.STRING_UTF8, LogEntry::targetName,
            ByteBufCodecs.BOOL, LogEntry::targetPlayer,
            LogEntry::new
    );
}
