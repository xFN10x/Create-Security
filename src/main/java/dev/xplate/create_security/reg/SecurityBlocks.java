package dev.xplate.create_security.reg;

import com.simibubi.create.AllBlocks;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xplate.create_security.blocks.SightSensor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static dev.xplate.create_security.CSecurity.REG;

public class SecurityBlocks {
    public static BlockEntry<SightSensor> SIGHT_SENSOR = REG.block("sight_sensor", SightSensor::new)
            .initialProperties(AllBlocks.ANDESITE_CASING::get)
            .properties(p -> p.noOcclusion())
            .simpleItem()
            .defaultLoot()
            .addLayer(() -> RenderType::cutoutMipped)
            .lang("Sight Sensor")
            .blockstate((ctx, prov) -> {
                prov.directionalBlock(ctx.get(), prov.models().getExistingFile(prov.modLoc("block/sight_sensor")));
            })
            .register();

    public static void reg() {}
}
