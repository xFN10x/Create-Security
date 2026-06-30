package dev.xplate.create_security.blocks.movement;

import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import dev.xplate.create_security.blocks.entity.LaserDiodeEntity;

public class LazerDiodeMovement implements MovementBehaviour {

    @Override
    public void tick(MovementContext context) {
        LaserDiodeEntity be = (LaserDiodeEntity) context.contraption.getBlockEntityClientSide(context.localPos);
        if (be == null) return;
        LaserDiodeEntity.LaserDiodeBehaviour behav = be.getBehaviour(LaserDiodeEntity.LaserDiodeBehaviour.TYPE);
        behav.setOnContraption(true, context.position);
        behav.setContrapDirOperator(context.rotation);
    }
}
