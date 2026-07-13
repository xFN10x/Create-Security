package dev.xplate.create_security.items;

import dev.xplate.create_security.misc.IEndSickining;
import dev.xplate.create_security.reg.SecurityItemComponents;
import dev.xplate.create_security.reg.SecuritySoundEvents;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class FiniraniumSensor extends Item {
    public FiniraniumSensor(Properties properties) {
        super(properties);
    }

    private long i = 0L;
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide() || !(entity instanceof LivingEntity le)) return;
        i++;
        int blockRange = 24;
        // 0 will be no finiranium
        // 9 will be finiranium close 
        BlockPos entityPos = BlockPos.containing(le.getPosition(.5f));
        AABB checkArea = AABB.ofSize(
                        Vec3.ZERO,
                        blockRange,blockRange,blockRange)
                .move(le.getPosition(1f));
        Outliner.getInstance().showAABB(hashCode(), checkArea);

        Stream<BlockState> stream = level.getBlockStates(checkArea);
        Stream<BlockState> finiraniumBlocks = stream.filter((bp) -> (bp.getBlock() instanceof IEndSickining));
        AtomicInteger count = new AtomicInteger();
        finiraniumBlocks.forEach(state -> {
            IEndSickining block = ((IEndSickining) state.getBlock());
            float divedAmount = block.sickAmount() / 20f;
            count.addAndGet(Mth.ceil(divedAmount));
        });
        if (i >= (level.tickRateManager().tickrate())) {
            i = 0;
            if (count.get() >= 9) {
                level.playSound(null, le.getX(), le.getY(), le.getZ(), SecuritySoundEvents.WARNING_SOUND.get(), SoundSource.BLOCKS, 1, 1);
            }
        }
        
        stack.set(SecurityItemComponents.FINIRANIUM_LEVEL.get(), (float) Math.min(count.get(), 9));
    }
}
