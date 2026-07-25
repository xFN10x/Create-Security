package dev.xplate.create_security.blocks.base;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import dev.xplate.create_security.blocks.entity.ChunkDetectorEntity;
import dev.xplate.create_security.blocks.entity.base.LoggableKineticBlockEntity;
import dev.xplate.create_security.reg.SecurityItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Function;

public abstract class LoggableKineticBlock<T extends LoggableKineticBlockEntity> extends KineticBlock implements IBE<T>, IWrenchable {
    public LoggableKineticBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(SecurityItems.LOG)) {
            if (level instanceof ServerLevel slev) {
                T entity = (T) slev.getBlockEntity(pos);
                if (entity.isEmpty()) {
                    entity.setItem(0, stack);
                    return ItemInteractionResult.SUCCESS;
                } else 
                    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
