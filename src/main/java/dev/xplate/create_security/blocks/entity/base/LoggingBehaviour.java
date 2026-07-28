package dev.xplate.create_security.blocks.entity.base;

import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class LoggingBehaviour extends BlockEntityBehaviour {
    public final LoggableKineticBlockEntity blockEntity;
    
    public LoggingBehaviour(LoggableKineticBlockEntity be) {
        super(be);
        this.blockEntity = be;
    }
    
    protected abstract ServerLevel getLevel();
    
    public final void attemptLog(String message, LivingEntity target) {
            blockEntity.attemptLog(message,target, getLevel(), blockEntity.getBlockState().getBlock());
    }

    @Override
    public abstract BehaviourType<?> getType();
}
