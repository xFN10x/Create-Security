package dev.xplate.create_security.datagen.blockstate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xplate.create_security.blocks.LazerDiode;
import dev.xplate.create_security.blocks.SightSensor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class LazerDiodeGenerator extends PanelShapedBSGen{
    @Override
    public <T extends Block> String getModelPath(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return state.getValue(LazerDiode.RECEIVER) ? "block/lazer_diode_alt" : "block/lazer_diode";
    }
}
