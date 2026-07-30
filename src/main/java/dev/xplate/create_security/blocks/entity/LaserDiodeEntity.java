package dev.xplate.create_security.blocks.entity;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.xplate.create_security.blocks.LaserDiode;
import dev.xplate.create_security.reg.SecurityBlocks;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.List;
import java.util.function.UnaryOperator;

public class LaserDiodeEntity extends KineticBlockEntity {

    LaserDiodeBehaviour behaviour;

    public LaserDiodeEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviour = new LaserDiodeBehaviour(this);
        behaviours.add(behaviour);
    }

    public float getMaxLength() {
        if (getBlockState().getValue(LaserDiode.RECEIVER) || behaviour.isOnContraption())
            return getPossibleMaxLength();
        else
            return getPossibleMaxLength() * Mth.abs(getSpeed() / 256);
    }


    public int getPossibleMaxLength() {
        if (getBlockState().getValue(LaserDiode.RECEIVER))
            return 256;
        else
            return 32;
    }

    public static Tuple<Float, HitResult> calcLength(Vec3 start, Vec3 dirNorm, Level level, int maxLength) {
        Vec3 end = start.add(
                dirNorm.multiply(maxLength, maxLength, maxLength));
        BlockHitResult hitBlockResult = level.clip(new ClipContext(
                start,
                end,
                ClipContext.Block.VISUAL,
                ClipContext.Fluid.WATER,
                CollisionContext.empty()
        ));
        //Outliner.getInstance().showLine(start.hashCode(), start, end);

        BlockPos hitBP = hitBlockResult.getBlockPos();
        BlockState hitBlock = level.getBlockState(hitBP);
        boolean hitOwn = hitBlock.is(SecurityBlocks.LASER_DIODE);
        Vec3 hitLoc = hitBlockResult.getLocation();
        float blockHitLength = (float) start.vectorTo(hitLoc).length() + (hitOwn ? 0.7f : 0.5f);

        AABB checkAABB = new AABB(start, end).inflate(0.01f).move(new Vec3(0,0.01,0));
        //Outliner.getInstance().showAABB(start.hashCode() + 34, checkAABB);
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

    public Vec3 getLaserStart() {
        Vec3 worldPos = worldPosition.getCenter();
        if (behaviour.isOnContraption())
            return behaviour.getContraptionOffset();
        else
            return worldPos;
    }

    public Vec3 getDir() {
        Vec3 dir = Vec3.atLowerCornerOf(getBlockState().getValue(LaserDiode.FACING).getNormal());
        if (behaviour.isOnContraption())
            return behaviour.getContrapDirOperator().apply(dir);
        else {
            return dir;
        }
    }


    public class LaserDiodeBehaviour extends BlockEntityBehaviour {

        private boolean hitting = false;
        private boolean onContraption = true;

        public UnaryOperator<Vec3> getContrapDirOperator() {
            return contrapDirOperator;
        }

        public void setContrapDirOperator(UnaryOperator<Vec3> contrapDirOperator) {
            this.contrapDirOperator = contrapDirOperator;
        }

        private UnaryOperator<Vec3> contrapDirOperator = v -> v;

        public Vec3 getContraptionOffset() {
            return contraptionOffset;
        }

        private Vec3 contraptionOffset = Vec3.ZERO;
        private boolean hittingRec = false;
        private float hitLength = 0;

        public boolean isHittingAnything() {
            return hitting;
        }

        public boolean isHittingReceiver() {
            return hittingRec;
        }

        public boolean laserActive() {
            return isSpeedRequirementFulfilled() || isOnContraption();
        }

        public boolean isOnContraption() {
            return onContraption;
        }

        public void setOnContraption(boolean onContraption, Vec3 contraptionOffset) {
            this.onContraption = onContraption;
            this.contraptionOffset = contraptionOffset;
        }


        public float getLength() {
            return hitLength;
        }

        public static final BehaviourType<LaserDiodeBehaviour> TYPE = new BehaviourType<>();

        public LaserDiodeBehaviour(SmartBlockEntity be) {
            super(be);
        }

        @Override
        public BehaviourType<?> getType() {
            return TYPE;
        }

        @Override
        public void tick() {
            if (level == null) return;
            setOnContraption(false, Vec3.ZERO);
            Tuple<Float, HitResult> calc = calcLength(getLaserStart(), getDir(), level, (int) getMaxLength());
            hitLength = calc.getA();
            HitResult hitRes = calc.getB();
            hitting = hitRes.getType() != HitResult.Type.MISS;
            if (hitRes instanceof BlockHitResult bhr) {
                BlockPos bp = bhr.getBlockPos();
                BlockState state = level.getBlockState(bp);
                if (state.is(SecurityBlocks.LASER_DIODE)) {
                    if (getBlockState().getValue(LaserDiode.RECEIVER) && !state.getValue(LaserDiode.RECEIVER)) {
                        switchToBlockState(level, getBlockPos(), getBlockState().setValue(LaserDiode.POWER, 15));
                        return;
                    } else if (!getBlockState().getValue(LaserDiode.RECEIVER) && state.getValue(LaserDiode.RECEIVER)) {
                        hittingRec = true;
                        return;
                    }
                }
            }
            hittingRec = false;
            switchToBlockState(level, getBlockPos(), getBlockState().setValue(LaserDiode.POWER, 0));
        }
    }
}
