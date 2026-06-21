package dev.xplate.create_security.misc;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;

import javax.annotation.Nullable;

public class EndSicknessSyncHandler implements AttachmentSyncHandler<Long> {
    @Override
    public void write(RegistryFriendlyByteBuf buf, Long attachment, boolean initialSync) {
        ByteBufCodecs.VAR_LONG.encode(buf, attachment);
    }

    @Override
    @Nullable
    public Long read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable Long previousValue) {
        if (previousValue == null)
            return 0L;
        else return ByteBufCodecs.VAR_LONG.decode(buf);
    }

    @Override
    public boolean sendToPlayer(IAttachmentHolder holder, ServerPlayer to) {
        // Return whether the holder data is synced to the given player client
        // The players checked are different depending on the attachment holder:
        // - Block entities: All players tracking the chunk the block entity is within
        // - Chunk: All players tracking the chunk
        // - Entity: All players tracking the current entity, includes the current player if they are the attachment holder
        // - Level: All players in the current dimension / level

        // Example:
        // Only send the attachment if they are the attachment holder
        return holder == to;
    }
}
