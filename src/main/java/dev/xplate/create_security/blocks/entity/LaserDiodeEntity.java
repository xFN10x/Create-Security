package dev.xplate.create_security.blocks.entity;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import dev.xplate.create_security.blocks.LazerDiode;
import dev.xplate.create_security.reg.SecurityBlocks;
import net.createmod.catnip.outliner.Outliner;
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
        return getPossibleMaxLength() * Mth.abs(getSpeed() / 256);
    }

    public boolean isHittingAnything() {
        return hitting;
    }

    public int getPossibleMaxLength() {
        return 256 / 4;
    }

    @Override
    public void tick() {
        if (level == null) return;
        Tuple<Float, Boolean> calc = calcLength(getLazerStart(), getDir().getNormal(), level, (int) getMaxLength());
        hitLength = calc.getA();
        hitting = calc.getB();
    }

    public static Tuple<Float, Boolean> calcLength(Vec3 start, Vec3i dirNorm, Level level, int maxLength) {
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
        boolean hitRec = hitBlock.is(SecurityBlocks.LAZER_DIODE) && hitBlock.getValue(LazerDiode.RECEIVER);
        Vec3 hitLoc = hitBlockResult.getLocation();
        float blockHitLength = (float) start.vectorTo(hitLoc).length() + (hitRec ? 0.5f : 0);

        AABB checkAABB = new AABB(start, end).inflate(0.1f);
        //Outliner.getInstance().showAABB(start.hashCode(), checkAABB);
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(level, null, start, end, checkAABB, e -> !e.isSpectator());
        if (hitBlockResult.getType() == HitResult.Type.MISS && entityHitResult == null) {
            return new Tuple<>(blockHitLength, false);
        } else if (hitBlockResult.getType() != HitResult.Type.MISS && entityHitResult == null) {
            return new Tuple<>(blockHitLength, true);
        }
        Vec3 entityPos = entityHitResult.getEntity().getBoundingBox().getCenter();
        float entityHitLength = (float) start.vectorTo(entityPos).length();
        boolean entityCloser = entityHitLength < blockHitLength;

        return new Tuple<>(!entityCloser ? blockHitLength : entityHitLength, true);
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
