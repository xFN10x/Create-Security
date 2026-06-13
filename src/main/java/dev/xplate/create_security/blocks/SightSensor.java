package dev.xplate.create_security.blocks;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllBlocks;
import dev.xplate.create_security.blocks.entity.SightSensorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

public class SightSensor extends BaseEntityBlock {
    public static DirectionProperty DIRECTION = DirectionProperty.create("direction");

    public SightSensor(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(DIRECTION, Direction.UP));
    }


    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(SightSensor::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SightSensorEntity(pos, state);
    }
}
