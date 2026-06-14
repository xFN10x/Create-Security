package dev.xplate.create_security.blocks.entity;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.*;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import dev.xplate.create_security.CSecurity;
import dev.xplate.create_security.blocks.SightSensor;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.Random;

public class SightSensorEntity extends SmartBlockEntity {

    public SightSensorScrollValueBehavior scrollVal;

    public SightSensorEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        scrollVal = new SightSensorScrollValueBehavior(Component.translatable("blocks.sight_sensor.distance"), this, new CenteredSideValueBoxTransform());
        scrollVal.requiresWrench();
        scrollVal.between(1, 50);
        scrollVal.setValue(15);
        behaviours.add(scrollVal);
        SightSensorBehavior sightSensorBehavior = new SightSensorBehavior(this);
        behaviours.add(sightSensorBehavior);
    }

    public static class SightSensorBehavior extends BlockEntityBehaviour {
        public static final BehaviourType<SightSensorBehavior> TYPE = new BehaviourType<>();

        public SightSensorBehavior(SmartBlockEntity be) {
            super(be);
        }

        @Override
        public BehaviourType<?> getType() {
            return TYPE;
        }

        @Override
        public void tick() {
            super.tick();
            Level level = getWorld();
            if (level == null) return;
            if (!(blockEntity instanceof SightSensorEntity)) return;
            SightSensorEntity sse = (SightSensorEntity) blockEntity;
            int size = sse.scrollVal.getValue();
            Vec3i Vsize = new Vec3i(size, size, size);
            AABB area = AABB.ofSize(getPos().getCenter(), size, size, size);
            List<Entity> entities = level.getEntities(null, area);
            for (Entity entity : entities) {
                    HitResult pick = entity.pick(50, 5, false);
                    if (pick.getType().equals(HitResult.Type.BLOCK)) {
                        BlockHitResult res = ((BlockHitResult) pick);
                        BlockPos hitLoc = res.getBlockPos();
                        Direction dir = res.getDirection();
                        BlockState found = level.getBlockState(hitLoc);
                        BlockPos thisPos = getPos();
                        //CSecurity.LOGGER.info("this: " + thisPos + " other: " + hitLoc);
                        if (hitLoc.equals(thisPos) && found.getValue(SightSensor.FACING).equals(dir)) {
                            level.addParticle(ParticleTypes.DUST_PLUME, true, hitLoc.getX() + level.random.nextFloat(), hitLoc.getY() + level.random.nextFloat(), hitLoc.getZ() + level.random.nextFloat(), 0,0,0);
                        }
                        //Outliner.getInstance().showLine("ray",entity.getPosition(2), hitLoc.getCenter());
                        //CSecurity.LOGGER.info("looking: " + found);
                    }
            }
        }
    }

    public static class SightSensorScrollValueBehavior extends ScrollValueBehaviour {

        public SightSensorScrollValueBehavior(Component label, SmartBlockEntity be, ValueBoxTransform slot) {
            super(label, be, slot);
        }

        @Override
        public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
            return new ValueSettingsBoard(
                    label,
                    50,
                    1,
                    CreateLang
                            .translatedOptions("generic.unit", "blocks"),
                    new ValueSettingsFormatter(set -> Component.translatable(set.value() + " blocks")));
        }
    }
}
