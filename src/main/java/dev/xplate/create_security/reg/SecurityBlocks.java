package dev.xplate.create_security.reg;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xplate.create_security.blocks.SightSensor;
import dev.xplate.create_security.datagen.blockstate.SightSensorGenerator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

import static dev.xplate.create_security.CSecurity.REG;

public class SecurityBlocks {
    public static BlockEntry<SightSensor> SIGHT_SENSOR = REG.block("sight_sensor", SightSensor::new)
            .initialProperties(AllBlocks.ANDESITE_CASING::get)
            .properties(p -> p.noOcclusion())
            .simpleItem()
            .defaultLoot()
            .addLayer(() -> RenderType::cutoutMipped)
            .lang("Sight Sensor")
            .blockstate(new SightSensorGenerator()::generate)
            .register();

    public static void reg() {}
}
