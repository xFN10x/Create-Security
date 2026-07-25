package dev.xplate.create_security.blocks;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import dev.xplate.create_security.blocks.base.LoggableKineticBlock;
import dev.xplate.create_security.blocks.entity.ChunkDetectorEntity;
import dev.xplate.create_security.blocks.entity.LaserDiodeEntity;
import dev.xplate.create_security.reg.SecurityBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ChunkDetector extends LoggableKineticBlock<ChunkDetectorEntity> {
    public ChunkDetector(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public Class<ChunkDetectorEntity> getBlockEntityClass() {
        return ChunkDetectorEntity.class;
    }

    @Override
    public BlockEntityType<? extends ChunkDetectorEntity> getBlockEntityType() {
        return SecurityBlockEntities.CHUNK_DETECTOR_ENTITY.get();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.FAST;
    }
}
