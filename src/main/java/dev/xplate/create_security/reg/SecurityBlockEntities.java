package dev.xplate.create_security.reg;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import dev.xplate.create_security.blocks.entity.SightSensorEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.neoforged.neoforge.registries.DeferredHolder;

import static dev.xplate.create_security.CSecurity.REG;

public class SecurityBlockEntities {
    public static final BlockEntityBuilder<BlockEntity, Registrate> SIGHT_SENSOR_ENTITY = REG.blockEntity("sight_sensor_tile", SignBlockEntity::new);
    public static void reg() {}
}
