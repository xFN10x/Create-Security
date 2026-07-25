package dev.xplate.create_security.misc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

import java.time.Instant;

public record LogEntry(String message, LogTarget target, Holder<Block> blockSource, LogTime time) {
    public static final Codec<LogEntry> CODEC = RecordCodecBuilder.create(o ->
            o.group(
                            Codec.STRING.fieldOf("message").forGetter(LogEntry::message),
                            LogTarget.CODEC.fieldOf("target").forGetter(LogEntry::target),
                            BuiltInRegistries.BLOCK.holderByNameCodec().fieldOf("blockSource").forGetter(LogEntry::blockSource),
                            LogTime.CODEC.fieldOf("time").forGetter(LogEntry::time)
                    )
                    .apply(o, LogEntry::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, LogEntry> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, LogEntry::message,
            LogTarget.STREAM_CODEC, LogEntry::target,
            ByteBufCodecs.holderRegistry(Registries.BLOCK), LogEntry::blockSource,
            LogTime.STREAM_CODEC, LogEntry::time,
            LogEntry::new
    );

    public record LogTime(long rlSecond, long igt) {
        public static final Codec<LogTime> CODEC = RecordCodecBuilder.create(o ->
                o.group(
                                Codec.LONG.fieldOf("rlSecond").forGetter(LogTime::rlSecond),
                                Codec.LONG.fieldOf("igt").forGetter(LogTime::igt)
                        )
                        .apply(o, LogTime::new));
        public static final StreamCodec<ByteBuf, LogTime> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_LONG, LogTime::rlSecond,
                ByteBufCodecs.VAR_LONG, LogTime::igt,
                LogTime::new
        );

        public static LogTime now(ServerLevel level) {
            return new LogTime(Instant.now().getEpochSecond(), level.getDayTime());
        }
    }

    public record LogTarget(Component entityName, boolean isTargetPlayer, boolean isTargetInvisible, String plrUUID) {

        public LogTarget(String entityName, boolean isTargetPlayer, boolean isTargetInvisible, String plrUUID) {
            this(Component.literal(entityName), isTargetPlayer, isTargetInvisible, plrUUID);
        }

        public String strEntityName() {
            return entityName.getString();
        }

        public static final Codec<LogTarget> CODEC = RecordCodecBuilder.create(o ->
                o.group(
                                Codec.STRING.fieldOf("entityName").forGetter(LogTarget::strEntityName),
                                Codec.BOOL.fieldOf("isTargetPlayer").forGetter(LogTarget::isTargetPlayer),
                                Codec.BOOL.fieldOf("isTargetInvisible").forGetter(LogTarget::isTargetInvisible),
                                Codec.STRING.fieldOf("plrUUID").forGetter(LogTarget::plrUUID)
                        )
                        .apply(o, LogTarget::new));
        public static final StreamCodec<ByteBuf, LogTarget> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, LogTarget::strEntityName,
                ByteBufCodecs.BOOL, LogTarget::isTargetPlayer,
                ByteBufCodecs.BOOL, LogTarget::isTargetInvisible,
                ByteBufCodecs.STRING_UTF8, LogTarget::plrUUID,
                LogTarget::new
        );

        public static LogTarget of(LivingEntity entity) {
            return new LogTarget(entity.getDisplayName(), entity instanceof Player, entity.hasEffect(MobEffects.INVISIBILITY), entity instanceof Player plr ? plr.getGameProfile().getId().toString() : "");
        }
    }
}
