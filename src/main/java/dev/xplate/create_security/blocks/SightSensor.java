package dev.xplate.create_security.blocks;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.block.IBE;
import dev.xplate.create_security.blocks.entity.SightSensorEntity;
import dev.xplate.create_security.reg.SecurityBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SightSensor extends Block implements IBE<SightSensorEntity>, IWrenchable {
    public static DirectionProperty FACING = BlockStateProperties.FACING;
    public static BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static IntegerProperty POWER = IntegerProperty.create("power", 0,15);
    public static BooleanProperty REVERSED = BooleanProperty.create("reversed");
    public static BooleanProperty SECRET = BooleanProperty.create("secret");

    public SightSensor(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.UP)
                .setValue(ACTIVE, false)
                .setValue(POWER, 0)
                .setValue(REVERSED, true)
                .setValue(SECRET, false)
        );
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (context.getClickedFace() == state.getValue(FACING).getOpposite()) {
            KineticBlockEntity.switchToBlockState(level, pos, state.setValue(REVERSED, !state.getValue(REVERSED)));
            level.playSound(null, context.getClickedPos(), SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 1, 0.5f);
        } else {
            BlockState rotated = getRotatedBlockState(state, context.getClickedFace());
            if (!rotated.canSurvive(level, context.getClickedPos()))
                return InteractionResult.PASS;

            KineticBlockEntity.switchToBlockState(level, pos, updateAfterWrenched(rotated, context));

            if (level.getBlockState(pos) != state)
                IWrenchable.playRotateSound(level, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state);
    }

    public static VoxelShape getShape(BlockState state) {
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
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return direction == state.getValue(FACING);
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        Integer pwr = state.getValue(POWER);
        return direction == state.getValue(FACING) ? (state.getValue(REVERSED) ? 15 - pwr : pwr) : 0;
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
        builder.add(FACING, ACTIVE, POWER, REVERSED, SECRET);
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
