package dev.xplate.create_security.items.propfuncs;

import dev.xplate.create_security.misc.IEndSickining;
import dev.xplate.create_security.reg.SecuritySoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class FiniraniumSensorPropertyFunction implements ItemPropertyFunction {

    private long i = 0L;

    @Override
    public float call(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity le, int seed) {
        if (level == null || le == null) return 0;
        i++;
        int blockRange = 12;
        // 0 will be no finiranium
        // 9 will be finiranium close 
        BlockPos entityPos = BlockPos.containing(le.getPosition(.5f));
        AABB checkArea = AABB.ofSize(
                        Vec3.ZERO,
                        blockRange, blockRange, blockRange)
                .move(entityPos);
        //Outliner.getInstance().showAABB(hashCode(), checkArea);

        Stream<BlockState> stream = level.getBlockStates(checkArea);
        Stream<BlockState> finiraniumBlocks = stream.filter((bp) -> (bp.getBlock() instanceof IEndSickining));
        AtomicInteger count = new AtomicInteger();
        finiraniumBlocks.forEach(state -> {
            IEndSickining block = ((IEndSickining) state.getBlock());
            float divedAmount = block.sickAmount() / 20f;
            count.addAndGet(Mth.ceil(divedAmount));
        });
        if (i >= Minecraft.getInstance().getFps() * 2L && !Minecraft.getInstance().isPaused()) {
            i = 0;
            if (count.get() >= 9 && le instanceof Player plr) {
                level.playSound(plr, le.getX(), le.getY(), le.getZ(), SecuritySoundEvents.WARNING_SOUND.get(), SoundSource.BLOCKS, 1, 1);
            }
        }
        return (long) Math.min(count.get(), 9);
    }
}
