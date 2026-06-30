package dev.xplate.create_security.blocks.entity.renders.contraption;

import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ActorVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import net.minecraft.world.level.BlockAndTintGetter;

public class LaserDiodeContrapRenderer extends ActorVisual {
    public LaserDiodeContrapRenderer(VisualizationContext visualizationContext, BlockAndTintGetter world, MovementContext context) {
        super(visualizationContext, world, context);
        
    }



    @Override
    protected void _delete() {

    }
}
