package dev.xplate.create_security.blocks.movement;

import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import dev.xplate.create_security.blocks.SightSensor;
import dev.xplate.create_security.blocks.entity.SightSensorEntity;
import dev.xplate.create_security.reg.SecurityBlocks;
import net.createmod.ponder.api.scene.VectorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.Optional;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import static dev.xplate.create_security.CSecurity.LOGGER;
import static dev.xplate.create_security.blocks.entity.SightSensorEntity.getRedstonePowerFromDistance;

public class SightSensorMovement implements MovementBehaviour {

    @Override
    public void tick(MovementContext context) {
        MovementBehaviour.super.tick(context);
        Level lev = context.world;
        if (lev == null || lev.isClientSide) return;
        ServerLevel level = (ServerLevel) lev;
        int size = 15;
        // times by 2 because the dimensions 15x15x15 in the center only has range of 7.5x7.5x7.5
        BlockPos thisPos = BlockPos.containing(context.position);
        Vec3 thisRealPos = context.position;
        AABB area = AABB.ofSize(thisPos.getCenter(), size * 2, size * 2, size * 2);
        List<Entity> entities = level.getEntities(null, area);
        BlockState me = context.state;
        Vec3 thisPosVec = thisPos.getCenter();

        boolean seeing = false;
        Vec3 seeingAt = Vec3.ZERO;
        Vec3 lookAngle = Vec3.ZERO;


//        for (int i = 1; i < size; i++) {
//            BlockState block = level.getBlockState(pos);
//            if (block.is(SecurityBlocks.SIGHT_SENSOR) && block.getValue(SightSensor.FACING).equals(me.getValue(SightSensor.FACING).getOpposite())) {
//                seeing = true;
//                seeingAt = pos.getCenter();
//                break;
//            } else if (block.isSolid()) break;
//        }

        //raycast from the sensor instead of from the entities
        if (!seeing)
            for (Entity ent : entities) {
                if (ent instanceof LivingEntity entity) {
                    /*if (entity.isDeadOrDying() && !me.getValue(SightSensor.SECRET)) {
                        for (int x = 0; x < 20; x++) {
                            for (int y = 0; y < 20; y++) {
                                for (int z = 0; z < 20; z++) {
                                    BlockPos pos = new BlockPos(x, y, z);
                                    BlockState block = level.getBlockState(pos);
                                    BlockState u = level.getBlockState(pos.above());
                                    BlockState d = level.getBlockState(pos.below());
                                    BlockState n = level.getBlockState(pos.north());
                                    BlockState w = level.getBlockState(pos.west());
                                    BlockState e = level.getBlockState(pos.east());
                                    BlockState s = level.getBlockState(pos.south());
                                    if (level.random.nextBoolean() && !block.canBeReplaced() && (
                                            !u.isAir() &&
                                                    !d.isAir() &&
                                                    !u.isAir() &&
                                                    !w.isAir() &&
                                                    !w.isAir() &&
                                                    !s.isAir() &&
                                                    !e.isAir()
                                    )) {
                                        level.setBlockAndUpdate(thisPos, me.setValue(SightSensor.SECRET, true));
                                        level.setBlockAndUpdate(pos, SecurityBlocks.THE_BLOCK.getDefaultState());
                                        level.playSound(null, pos, SoundEvents.AMBIENT_CAVE.value(), SoundSource.MASTER);
                                        return;
                                    }
                                }
                            }
                        }
                    }*/
                    AtomicBoolean wearingHead = new AtomicBoolean(false);
                    if (entity instanceof Player) {
                        ItemStack headItem = entity.getItemBySlot(EquipmentSlot.HEAD);
                        if (headItem.getItem() instanceof BlockItem bi) {
                            wearingHead.set(bi.getBlock().defaultBlockState().is(Tags.Blocks.SKULLS));
                        }
                    }
                    if (entity.isSpectator() || entity instanceof Warden || wearingHead.get()) {
                        continue;
                    }
                    // a lot of this is from chat gpt, but eeh
                    Vec3 look = entity.getViewVector(1).normalize();

                    Vec3 start = entity.getEyePosition();
                    Vec3 end = start.add(look.scale(size));

                    AABB sensorBox =
                            new AABB(thisRealPos.subtract(0.5, 0.5, 0.5),
                                    thisRealPos.add(0.5, 0.5, 0.5));

                    Optional<Vec3> hit =
                            sensorBox.clip(start, end);
                    Direction facing = Direction.getNearest(context.rotation.apply(Vec3.atLowerCornerOf(me.getValue(SightSensor.FACING).getNormal()).normalize()));
                    Direction entityFacing = Direction.getNearest(entity.getLookAngle());


                    if (hit.isPresent() && facing.equals(entityFacing.getOpposite())) {
                        seeing = true;
                        seeingAt = entity.getEyePosition();
                        lookAngle = entity.getLookAngle();
                    }

//
//
//                    Vec3 toBlock = thisRealPos.subtract(entity.getEyePosition())
//                            .normalize();
//                    Direction facing =
//                            context.state.getValue(SightSensor.FACING);
//
//                    Vec3 localFacing =
//                            Vec3.atLowerCornerOf(facing.getNormal());
//
//                    Vec3 worldFacing =
//                            context.rotation.apply(localFacing).normalize();
//                    double dot = look.dot(toBlock);
//                    if (isLookingAtFace(entity, worldFacing, 0.87)) {
//                        seeing = true;
//                        seeingAt = entity.getEyePosition();
//                        lookAngle = entity.getLookAngle();
//                    }
                }
            }

        if (seeing) {
            double dist = thisPosVec.distanceTo(seeingAt);
            int power = getRedstonePowerFromDistance(thisPos, seeingAt, size);

            if (!me.getValue(SightSensor.ACTIVE)) {
                level.playSound(null, thisPos, SoundEvents.VAULT_ACTIVATE, SoundSource.BLOCKS, 0.5f, 0.4f);
                level.playSound(null, thisPos, SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS, 0.5f, 0.4f);
            }
            context.state = me.setValue(SightSensor.ACTIVE, true);
            //level.setBlockAndUpdate(thisPos, me.setValue(SightSensor.POWER, power).setValue(SightSensor.ACTIVE, true));

            double rand = -(level.random.nextFloat() * (dist / 1.5));
            for (ServerPlayer player : level.players()) {
                Vec3 test = thisPosVec.add(lookAngle.multiply(rand, rand, rand));
                level.sendParticles(player, ParticleTypes.DRAGON_BREATH, true, test.x(), test.y(), test.z(), 1, 0.1, 0.1, 0.1, 0.01);
            }
            return;
        }

        if (me.getValue(SightSensor.ACTIVE)) {
            context.state = me.setValue(SightSensor.ACTIVE, false);
            //level.setBlockAndUpdate(thisPos, me.setValue(SightSensor.ACTIVE, false).setValue(SightSensor.POWER, 0));
            level.playSound(null, thisPos, SoundEvents.VAULT_DEACTIVATE, SoundSource.BLOCKS, 0.2f, 0.4f);
            level.playSound(null, thisPos, SoundEvents.ENDER_CHEST_CLOSE, SoundSource.BLOCKS, 0.2f, 0.4f);
        }
    }
}
