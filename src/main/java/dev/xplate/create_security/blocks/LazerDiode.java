package dev.xplate.create_security.blocks;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import dev.xplate.create_security.blocks.entity.LaserDiodeEntity;
import dev.xplate.create_security.reg.SecurityBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LazerDiode extends KineticBlock implements IBE<LaserDiodeEntity>, IWrenchable {

    public static DirectionProperty FACING = BlockStateProperties.FACING;
    public static BooleanProperty RECEIVER = BooleanProperty.create("receiver");

    public LazerDiode(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.UP)
                .setValue(RECEIVER, false)
        );
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Boolean isLens = state.getValue(RECEIVER);

        KineticBlockEntity.switchToBlockState(level, pos, updateAfterWrenched(state.setValue(RECEIVER, !isLens), context));
        IWrenchable.playRotateSound(level, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
        builder.add(RECEIVER);
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.FAST;
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(FACING).getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(FACING).getOpposite() && !state.getValue(RECEIVER);
    }

    @Override
    public Class<LaserDiodeEntity> getBlockEntityClass() {
        return LaserDiodeEntity.class;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction dir = context.getClickedFace();
        return defaultBlockState().setValue(FACING, dir);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return getShape(state);
    }

    @Override
    public BlockEntityType<? extends LaserDiodeEntity> getBlockEntityType() {
        return SecurityBlockEntities.LAZER_DIODE_ENTITY.get();
    }

    public static VoxelShape getShape(BlockState state) {
        return switch (state.getValue(FACING)) {
            case DOWN -> Block.box(0,11,0,16,16,16);
            case UP -> Block.box(0,0,0,16,5,16);
            case NORTH -> Block.box(0,0,11,16,16,16);
            case SOUTH -> Block.box(0,0,0,16,16,5);
            case WEST -> Block.box(11,0,0,16,16,16);
            case EAST -> Block.box(0,0,0,5,16,16);
        };
    }
}
