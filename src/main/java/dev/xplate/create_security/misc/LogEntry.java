package dev.xplate.create_security.misc;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xplate.create_security.reg.SecurityBlocks;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

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
    
    @Nonnull
    public Block getSourceBlock() {
        final Block returning;
        Either<ResourceKey<Block>, Block> unwrapped = blockSource.unwrap();
        Optional<Block> opt = unwrapped.right();
        BlockEntry<Block> defaultBlock = SecurityBlocks.THE_BLOCK;
        if (opt.isEmpty()) {
            Optional<ResourceKey<Block>> leftOpt = unwrapped.left();
            ResourceKey<Block> resKey = leftOpt.orElseGet(defaultBlock::getKey);
            return Objects.requireNonNullElse(BuiltInRegistries.BLOCK.get(resKey), defaultBlock.get());
        } else {
            returning = opt.orElseGet(defaultBlock);
            return returning;
        }
    }
    
    public @Nonnull BlockState getSourceBlockState() {
        return getSourceBlock().defaultBlockState();
    }

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
