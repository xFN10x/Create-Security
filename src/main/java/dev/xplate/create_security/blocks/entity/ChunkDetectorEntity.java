package dev.xplate.create_security.blocks.entity;

import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.xplate.create_security.blocks.entity.base.LoggableKineticBlockEntity;
import dev.xplate.create_security.blocks.entity.base.LoggingBehaviour;
import dev.xplate.create_security.datagen.CSSDataGen;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ChunkDetectorEntity extends LoggableKineticBlockEntity {
    public ChunkDetectorEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        ChunkDetectorBehaviour behav = new ChunkDetectorBehaviour(this);
        behaviours.add(behav);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return super.addToGoggleTooltip(tooltip, isPlayerSneaking, CSSDataGen.chunkDetectorHeading.getB());
    }

    @Override
    public float calculateStressApplied() {
        return 3;
    }

    public class ChunkDetectorBehaviour extends LoggingBehaviour {
        public static final BehaviourType<LoggingBehaviour> TYPE = new BehaviourType<>();
        private List<Entity> lastCheck = List.of();

        public ChunkDetectorBehaviour(LoggableKineticBlockEntity be) {
            super(be);
            setLazyTickRate(2);
        }

        @Override
        protected ServerLevel getLevel() {
            if (level instanceof ServerLevel slev)
                return slev;
            else return null;
        }

        @Override
        public BehaviourType<?> getType() {
            return TYPE;
        }

        @Override
        public void lazyTick() {
            if (getLevel() == null) return;
            int range; //radius
            float speed = getSpeed();
            if (!isSpeedRequirementFulfilled()) return;
            else {
                if (speed >= 256) range = 2;
                else if (speed >= 150) range = 1;
                else range = 0;
            }
            BlockPos pos = getPos();
            int y = pos.getY();
            int centerX = SectionPos.blockToSectionCoord(pos.getX());
            int centerZ = SectionPos.blockToSectionCoord(pos.getZ());
            int minX = centerX - range;
            int minZ = centerZ - range;
            int maxX = centerX + range;
            int maxZ = centerZ + range;
            List<Entity> entities = new ArrayList<>();
            AABB aabb = new AABB(
                    SectionPos.sectionToBlockCoord(minX), 
                    -64,
                    SectionPos.sectionToBlockCoord(minZ),

                    SectionPos.sectionToBlockCoord(maxX),
                    256,
                    SectionPos.sectionToBlockCoord(maxZ)
            );
            Outliner.getInstance().showAABB("fesds", aabb);
            entities.addAll(getLevel().getEntities((Entity) null, aabb, entity -> entity instanceof LivingEntity));
            //removes the old ones from the list, so the returning will be new
            ArrayList<Entity> newEntites = new ArrayList<>(entities);
            newEntites.removeAll(lastCheck);
            //removes the new ones from the list, so the returning will be old
            ArrayList<Entity> oldEntites = new ArrayList<>(lastCheck);
            oldEntites.removeAll(entities);

            if (!newEntites.isEmpty()) {
                newEntites.forEach(entity -> {
                    SectionPos secPos = SectionPos.of(entity);
                    for (ServerPlayer player : getLevel().players()) {
                        Vec3 test = entity.getPosition(0f).add(0,entity.getBbHeight()/2,0);
                        Vec3 vel = entity.getKnownMovement().normalize().scale(1);
                        getLevel().sendParticles(player, ParticleTypes.DRAGON_BREATH, true, test.x(), test.y(), test.z(), 40, vel.x, vel.y, vel.z, 0.05);
                        Vec3 blockPos = pos.getCenter();
                        getLevel().sendParticles(player, ParticleTypes.DRAGON_BREATH, true, blockPos.x, blockPos.y, blockPos.z, 30, .01, .5, .01, 0.05);
                    }
                    if (range > 0)
                        attemptLog("entered chunk X:" + secPos.x() + " Y:" + secPos.y(), (LivingEntity) entity);
                    else
                        attemptLog("entered this chunk.", (LivingEntity) entity);
                });
            }

            if (!oldEntites.isEmpty()) {
                oldEntites.forEach(entity -> {
                    SectionPos secPos = SectionPos.of(entity);
                    if (range > 0)
                        attemptLog("left chunk X:" + secPos.x() + " Y:" + secPos.y(), (LivingEntity) entity);
                    else
                        attemptLog("left this chunk.", (LivingEntity) entity);
                });
            }

            lastCheck = entities;
        }
    }
}
