package dev.xplate.create_security.blocks.movement;

import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import dev.xplate.create_security.blocks.entity.LaserDiodeEntity;
import net.minecraft.world.level.Level;

public class LaserDiodeMovement implements MovementBehaviour {

    @Override
    public void tick(MovementContext context) {
        Level world = context.world;
        LaserDiodeEntity be = (LaserDiodeEntity) context.contraption.getBlockEntityClientSide(context.localPos);
        if (be == null) return;
        LaserDiodeEntity.LaserDiodeBehaviour behav = be.getBehaviour(LaserDiodeEntity.LaserDiodeBehaviour.TYPE);
        //Outliner.getInstance().showAABB(hashCode(), AABB.unitCubeFromLowerCorner(context.position));
        behav.setOnContraption(true, context.position);
        behav.setContrapDirOperator(context.rotation);


        //Tuple<Float, HitResult> res = LaserDiodeEntity.calcLength(start, be.getDir(), world, (int) be.getMaxLength());
        // welp, it doesn't seem i can actually make this work properly on contraptions...
        // maybe create will introduce some new apis at some point

    }
}
