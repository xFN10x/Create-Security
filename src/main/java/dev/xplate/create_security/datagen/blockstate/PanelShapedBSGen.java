package dev.xplate.create_security.datagen.blockstate;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xplate.create_security.blocks.SightSensor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public abstract class PanelShapedBSGen extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        Direction dir = state.getValue(SightSensor.FACING);
        return switch (dir) {
            case UP -> 0;
            case DOWN -> 180;
            default -> 90;
        };
    }

    @Override
    protected int getYRotation(BlockState state) {
        Direction dir = state.getValue(SightSensor.FACING);
        return switch (dir) {
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
            default -> 0;
        };
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return prov.models().getExistingFile(prov.modLoc(getModelPath(ctx, prov, state)));
    }

    public abstract <T extends Block> String getModelPath(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state);
}
