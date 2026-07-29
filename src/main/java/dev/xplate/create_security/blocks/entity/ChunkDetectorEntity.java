package dev.xplate.create_security.blocks.entity;

import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.xplate.create_security.blocks.entity.base.LoggableKineticBlockEntity;
import dev.xplate.create_security.blocks.entity.base.LoggingBehaviour;
import dev.xplate.create_security.datagen.CSSDataGen;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

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
            int range = 0; //radius
            BlockPos pos = getPos();
            int y = pos.getY();
            int centerX = SectionPos.blockToSectionCoord(pos.getX());
            int centerY = SectionPos.blockToSectionCoord(pos.getY());
            int centerZ = SectionPos.blockToSectionCoord(pos.getZ());
            Stream<SectionPos> stream = SectionPos.betweenClosedStream(centerX - range, centerY, centerZ - range, centerX + range, centerY, centerZ + range);
            stream.forEach(secPos -> {
                AABB aabb = new AABB(secPos.minBlockX(), -64, secPos.minBlockZ(), secPos.maxBlockX(), 256, secPos.maxBlockZ());
                List<Entity> entities = getLevel().getEntities((Entity) null, aabb, entity -> entity instanceof LivingEntity);
                //removes the old ones from the list, so the returning will be new
                ArrayList<Entity> newEntites = new ArrayList<>(entities);
                newEntites.removeAll(lastCheck);
                //removes the new ones from the list, so the returning will be old
                ArrayList<Entity> oldEntites = new ArrayList<>(lastCheck);
                oldEntites.removeAll(entities);
                
                if (!newEntites.isEmpty()) {
                    newEntites.forEach(entity -> {
                        if (range > 0)
                            attemptLog("entered chunk X:" + secPos.x() + " Y:" + secPos.y(), (LivingEntity) entity);
                        else
                            attemptLog("entered this chunk.", (LivingEntity) entity);
                    });
                }
                
                if (!oldEntites.isEmpty()) {
                    oldEntites.forEach(entity -> {
                        if (range > 0)
                            attemptLog("left chunk X:" + secPos.x() + " Y:" + secPos.y(), (LivingEntity) entity);
                        else
                            attemptLog("left this chunk.", (LivingEntity) entity);
                    });
                }

                lastCheck = entities;
            });
        }
    }
}
