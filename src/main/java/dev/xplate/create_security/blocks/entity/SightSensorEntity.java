package dev.xplate.create_security.blocks.entity;

import dev.xplate.create_security.reg.SecurityBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SightSensorEntity extends BlockEntity {
    public SightSensorEntity( BlockPos pos, BlockState blockState) {
        super(SecurityBlockEntities.SIGHT_SENSOR_ENTITY.get().get(), pos, blockState);
    }
}
