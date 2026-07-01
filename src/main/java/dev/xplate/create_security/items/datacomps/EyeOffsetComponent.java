package dev.xplate.create_security.items.datacomps;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;

public record EyeOffsetComponent(int offset) {
    public static final Codec<EyeOffsetComponent> CODEC = RecordCodecBuilder.create(o ->
            o.group(
                    Codec.INT.fieldOf("offset").forGetter(EyeOffsetComponent::offset)
                    )
                    .apply(o, EyeOffsetComponent::new));

    public static final StreamCodec<ByteBuf, EyeOffsetComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, EyeOffsetComponent::offset,
            EyeOffsetComponent::new
    );

    public EyeOffsetComponent add(int i, int min, int max) {
        int toSet = offset + i;
        return new EyeOffsetComponent(Mth.clamp(toSet, min, max));
    }
}
