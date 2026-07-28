package dev.xplate.create_security.blocks.entity;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.xplate.create_security.blocks.entity.base.LoggableKineticBlockEntity;
import dev.xplate.create_security.datagen.CSSDataGen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ChunkDetectorEntity extends LoggableKineticBlockEntity {
    public ChunkDetectorEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return super.addToGoggleTooltip(tooltip, isPlayerSneaking, CSSDataGen.chunkDetectorHeading.getB());
    }
}
