package dev.xplate.create_security.blocks.base;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import dev.xplate.create_security.blocks.entity.base.LoggableKineticBlockEntity;
import dev.xplate.create_security.reg.SecurityItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public abstract class LoggableKineticBlock<T extends LoggableKineticBlockEntity> extends KineticBlock implements IBE<T>, IWrenchable {
    public LoggableKineticBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, @Nullable BlockHitResult hitResult) {
        if (level instanceof ServerLevel slev && player instanceof ServerPlayer splay && hand == InteractionHand.MAIN_HAND) {
            if (stack.is(SecurityItems.LOG)) {
                T entity = (T) slev.getBlockEntity(pos);
                if (entity.isEmpty()) {
                    entity.setItem(0, stack.copy(), splay);
                    stack.shrink(1);
                    return ItemInteractionResult.SUCCESS;
                } else
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            } else if (stack.isEmpty()) {
                T entity = (T) slev.getBlockEntity(pos);
                if (!entity.isEmpty()) {
                    player.addItem(entity.removeItem(0, 1, splay));
                    return ItemInteractionResult.SUCCESS;
                } else
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
