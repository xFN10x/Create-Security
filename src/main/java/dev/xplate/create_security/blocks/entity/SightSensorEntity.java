package dev.xplate.create_security.blocks.entity;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.*;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.CreateLang;
import dev.xplate.create_security.blocks.SightSensor;
import dev.xplate.create_security.reg.SecurityBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

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
            Level lev = getWorld();
            if (lev == null || lev.isClientSide) return;
            ServerLevel level = (ServerLevel) lev;
            if (!(blockEntity instanceof SightSensorEntity sse)) return;
            int size = sse.scrollVal.getValue();
            // times by 2 because the dimensions 15x15x15 in the center only has range of 7.5x7.5x7.5
            AABB area = AABB.ofSize(getPos().getCenter(), size * 2, size * 2, size * 2);
            List<Entity> entities = level.getEntities(null, area);

            BlockState me = level.getBlockState(getPos());
            BlockPos thisPos = getPos();

            for (Entity entity : entities) {
                HitResult pick = entity.pick(50, 1, false);
                if (pick.getType().equals(HitResult.Type.BLOCK)) {
                    BlockHitResult res = ((BlockHitResult) pick);
                    BlockPos hitLoc = res.getBlockPos();
                    Direction dir = res.getDirection();
                    //CSecurity.LOGGER.info("this: " + thisPos + " other: " + hitLoc);
                    if (hitLoc.equals(thisPos) && me.is(SecurityBlocks.SIGHT_SENSOR) && me.getValue(SightSensor.FACING).equals(dir)) {
                        if (!me.getValue(SightSensor.ACTIVE)) {
                            level.playSound(null, thisPos, SoundEvents.VAULT_ACTIVATE, SoundSource.BLOCKS, 0.5f, 0.4f);
                            level.playSound(null, thisPos, SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS, 0.5f, 0.4f);
                        }

                        double dist = thisPos.getCenter().distanceTo(entity.position());
                        int power = getRedstonePowerFromDistance(thisPos, entity.position(), size);

                        level.setBlockAndUpdate(thisPos, me.setValue(SightSensor.POWER, power).setValue(SightSensor.ACTIVE, true));

                        double rand = -(level.random.nextFloat() * (dist / 1.5));
                        for (ServerPlayer player : level.players()) {
                            Vec3 lookAngle = entity.getLookAngle();
                            Vec3 test = thisPos.getCenter().add(lookAngle.multiply(rand,rand,rand));
                            level.sendParticles(player, ParticleTypes.DRAGON_BREATH, true, test.x(), test.y(), test.z(), 1, 0.1,0.1,0.1,0.01);
                        }
                        //CSecurity.LOGGER.info("dist: {}", dist);
                        return;
                    }
                    //Outliner.getInstance().showLine("ray",entity.getPosition(2), hitLoc.getCenter());
                    //CSecurity.LOGGER.info("looking: " + found);
                }
            }
            if (me.getValue(SightSensor.ACTIVE)) {
                level.setBlockAndUpdate(thisPos, me.setValue(SightSensor.ACTIVE, false).setValue(SightSensor.POWER, 0));
                level.playSound(null, thisPos, SoundEvents.VAULT_DEACTIVATE, SoundSource.BLOCKS, 0.2f, 0.4f);
                level.playSound(null, thisPos, SoundEvents.ENDER_CHEST_CLOSE, SoundSource.BLOCKS, 0.2f, 0.4f);
            }
        }
    }

    public static int getRedstonePowerFromDistance(BlockPos thisPos, Vec3 entityPos, int range) {
        double dist = thisPos.getCenter().distanceTo(entityPos);
        double distPercent = dist / range;
        return Math.clamp((int) (15 * distPercent), 0, 15);
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
