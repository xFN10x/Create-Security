package dev.xplate.create_security.reg;

import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.xplate.create_security.blocks.entity.LazerDiodeEntity;
import dev.xplate.create_security.blocks.entity.SightSensorEntity;
import dev.xplate.create_security.blocks.entity.renders.LazerDiodeRenderer;

import static dev.xplate.create_security.CSecurity.REG;

public class SecurityBlockEntities {
    public static final BlockEntityEntry<SightSensorEntity> SIGHT_SENSOR_ENTITY = REG
            .blockEntity("sight_sensor_tile", SightSensorEntity::new)
            .validBlocks(SecurityBlocks.SIGHT_SENSOR)
            .renderer(() -> SmartBlockEntityRenderer::new)
            .register();

    public static final BlockEntityEntry<LazerDiodeEntity> LAZER_DIODE_ENTITY = REG
            .blockEntity("lazer_diode_tile", LazerDiodeEntity::new)
            .validBlocks(SecurityBlocks.LAZER_DIODE)
            .renderer(() -> LazerDiodeRenderer::new)
            .register();

    public static void reg() {}
}
