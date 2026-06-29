package dev.xplate.create_security.blocks.entity;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import dev.xplate.create_security.blocks.LazerDiode;
import dev.xplate.create_security.reg.SecurityBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.CollisionContext;

public class LaserDiodeEntity extends KineticBlockEntity {
    private boolean hitting = false;
    private float hitLength = 0;

    public LaserDiodeEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public boolean lazerActive() {
        return isSpeedRequirementFulfilled();
    }

    public float getMaxLength() {
        if (getBlockState().getValue(LazerDiode.RECEIVER))
            return getPossibleMaxLength();
        else
            return getPossibleMaxLength() * Mth.abs(getSpeed() / 256);
    }

    public boolean isHittingAnything() {
        return hitting;
    }

    public int getPossibleMaxLength() {

        if (getBlockState().getValue(LazerDiode.RECEIVER))
            return 256;
        else
            return 256 / 4;
    }

    @Override
    public void tick() {
        if (level == null) return;
        Tuple<Float, HitResult> calc = calcLength(getLazerStart(), getDir().getNormal(), level, (int) getMaxLength());
        hitLength = calc.getA();
        HitResult hitRes = calc.getB();
        hitting = hitRes.getType() != HitResult.Type.MISS;

        if (getBlockState().getValue(LazerDiode.RECEIVER) && hitRes instanceof BlockHitResult bhr) {
                BlockPos bp = bhr.getBlockPos();
                BlockState state = level.getBlockState(bp);
                if (state.is(SecurityBlocks.LAZER_DIODE) && !state.getValue(LazerDiode.RECEIVER)) {
                    switchToBlockState(level, getBlockPos(), getBlockState().setValue(LazerDiode.POWER, 15));
                    return;
                }
        }
        switchToBlockState(level, getBlockPos(), getBlockState().setValue(LazerDiode.POWER, 0));
    }

    public static Tuple<Float, HitResult> calcLength(Vec3 start, Vec3i dirNorm, Level level, int maxLength) {
        Vec3 end = start.add(
                Vec3.atLowerCornerOf(
                        dirNorm.multiply(maxLength)));
        BlockHitResult hitBlockResult = level.clip(new ClipContext(
                start,
                end,
                ClipContext.Block.VISUAL,
                ClipContext.Fluid.WATER,
                CollisionContext.empty()
        ));

        BlockPos hitBP = hitBlockResult.getBlockPos();
        BlockState hitBlock = level.getBlockState(hitBP);
        boolean hitOwn = hitBlock.is(SecurityBlocks.LAZER_DIODE);
        Vec3 hitLoc = hitBlockResult.getLocation();
        float blockHitLength = (float) start.vectorTo(hitLoc).length() + (hitOwn ? 0.7f : 0.5f);
        if (hitBlockResult.getType() == HitResult.Type.MISS) {
            return new Tuple<>(blockHitLength, hitBlockResult);
        }

        AABB checkAABB = new AABB(start, end).inflate(0.05f);
        //Outliner.getInstance().showAABB(start.hashCode(), checkAABB);
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(level, null, start, end, checkAABB, e -> !e.isSpectator());

        boolean entityCloser = false;
        float entityHitLength = 0;
        if (entityHitResult != null) {
            Vec3 entityPos = entityHitResult.getEntity().getBoundingBox().getCenter();
            entityHitLength = (float) start.vectorTo(entityPos).length() + 0.5f;
            entityCloser = entityHitLength < blockHitLength;
        }



        return new Tuple<>(!entityCloser ? blockHitLength : entityHitLength, entityCloser ? entityHitResult : hitBlockResult);
    }

    public Vec3 getLazerStart() {
        return worldPosition.getCenter();
    }

    public Direction getDir() {
        return getBlockState().getValue(LazerDiode.FACING);
    }

    public float getLength() {
        return hitLength;
    }
}
