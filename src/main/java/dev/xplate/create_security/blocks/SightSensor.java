package dev.xplate.create_security.blocks;

import com.simibubi.create.content.kinetics.flywheel.FlywheelRenderer;
import com.simibubi.create.foundation.block.IBE;
import dev.engine_room.flywheel.api.Flywheel;
import dev.xplate.create_security.blocks.entity.SightSensorEntity;
import dev.xplate.create_security.reg.SecurityBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.CubeVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SightSensor extends Block implements IBE<SightSensorEntity> {
    public static DirectionProperty FACING = BlockStateProperties.FACING;
    public static BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static IntegerProperty POWER = IntegerProperty.create("power", 0,15);

    public SightSensor(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.UP)
                .setValue(ACTIVE, false)
                .setValue(POWER, 0)
        );
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case DOWN -> Block.box(0,9,0,16,16,16);
            case UP -> Block.box(0,0,0,16,7,16);
            case NORTH -> Block.box(0,0,9,16,16,16);
            case SOUTH -> Block.box(0,0,0,16,16,7);
            case WEST -> Block.box(9,0,0,16,16,16);
            case EAST -> Block.box(0,0,0,7,16,16);
        };
    }



    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return direction == state.getValue(FACING) ? state.getValue(POWER) : 0;
    }

    @Override
    protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getCollisionShape(state, level, pos, context);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getVisualShape(state,level,pos,context);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction dir = context.getClickedFace();
        return defaultBlockState().setValue(FACING, dir);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, ACTIVE, POWER);
    }

    @Override
    public Class<SightSensorEntity> getBlockEntityClass() {
        return SightSensorEntity.class;
    }

    @Override
    public BlockEntityType<? extends SightSensorEntity> getBlockEntityType() {
        return SecurityBlockEntities.SIGHT_SENSOR_ENTITY.get();
    }
}
