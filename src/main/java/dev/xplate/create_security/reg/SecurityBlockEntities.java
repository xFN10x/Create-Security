package dev.xplate.create_security.reg;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;

import static dev.xplate.create_security.CSecurity.REG;

public class SecurityBlockEntities {
    public static final BlockEntityEntry<BlockEntity> SIGHT_SENSOR_ENTITY = REG.blockEntity("sight_sensor_tile", SignBlockEntity::new)
            .validBlocks(SecurityBlocks.SIGHT_SENSOR)
            .register();
    public static void reg() {}
}
