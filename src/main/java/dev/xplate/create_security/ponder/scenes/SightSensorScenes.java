package dev.xplate.create_security.ponder.scenes;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlock;
import com.simibubi.create.content.redstone.nixieTube.NixieTubeBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import dev.xplate.create_security.blocks.SightSensor;
import dev.xplate.create_security.blocks.entity.SightSensorEntity;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.ParrotElement;
import net.createmod.ponder.api.element.ParrotPose;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.ui.PonderUI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class SightSensorScenes {

    public static void basic(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("basic_sight_sensor", "The Sight Sensor");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layer(0), Direction.UP);

        BlockPos sightSensor = new BlockPos(3, 2, 2);

        scene.idleSeconds(1);
        scene.world().showIndependentSection(util.select().fromTo(4, 1, 2, 3, 3, 2), Direction.UP);

        scene.idleSeconds(1);

        scene.overlay().showText(20 * 3)
                .placeNearTarget()
                .text("The Sight Sensor can be used to detect when entities are looking at it.")
                .pointAt(util.vector().blockSurface(new BlockPos(3, 2, 2), Direction.EAST));

        scene.idleSeconds(3);

        scene.addKeyframe();

        final Vec3 birdPos;
        birdPos = new Vec3(0, 2, 2);
        ElementLink<ParrotElement> birb = scene.special().createBirb(birdPos, FlappyFacePOIParrot::new);
        scene.special().movePointOfInterest(sightSensor);

        scene.world().modifyBlock(sightSensor, s -> s.setValue(SightSensor.POWERED, true), false);
        Vec3 ssVec = sightSensor.getCenter();
        scene.effects().emitParticles(ssVec, (world, x, y, z) -> {
            Vec3 pos = ssVec.lerp(birdPos.add(0, 0.5, 0), world.random.nextDouble());
            world.addParticle(ParticleTypes.DRAGON_BREATH, pos.x, pos.y, pos.z, 0.01, 0.01, 0.01);
        }, 1, 100);

        scene.idleSeconds(3);

        BlockPos nixietube = new BlockPos(4, 2, 2);
        scene.effects().indicateRedstone(nixietube);
        scene.world().modifyBlock(nixietube.above(), s ->
                Blocks.AIR.defaultBlockState(), false);
        scene.world().modifyBlock(nixietube, s ->
                AllBlocks.NIXIE_TUBES.get(DyeColor.ORANGE).get()
                        .defaultBlockState()
                        .setValue(NixieTubeBlock.FACING, Direction.WEST), true);
        scene.world().modifyBlockEntityNBT(util.select().position(nixietube), NixieTubeBlockEntity.class, nbt -> nbt.putInt("RedstoneStrength",
                SightSensorEntity.getRedstonePowerFromDistance(sightSensor, birdPos, 15)));

        scene.idleSeconds(1);

        scene.rotateCameraY(45);
        scene.idleSeconds(1);
        scene.addKeyframe();

        scene.overlay().showText(20 * 3)
                .placeNearTarget()
                .text("It outputs a redstone signal depending on how far the entity is.")
                .pointAt(util.vector().blockSurface(nixietube, Direction.WEST));

        scene.idleSeconds(3);
        scene.rotateCameraY(-45);


        scene.addKeyframe();
        scene.idle(10);

        Vec3 birdMovement = new Vec3(-6, -0.5, 0);
        scene.special().moveParrot(birb, birdMovement, 20);

        scene.effects().emitParticles(ssVec, (world, x, y, z) -> {
            Vec3 pos = ssVec.lerp(birdPos.add(birdMovement).add(0,0.5,0), world.random.nextDouble());
            world.addParticle(ParticleTypes.DRAGON_BREATH, pos.x, pos.y, pos.z, 0.01, 0.01, 0.01);
        }, 1, 100);

        scene.idleSeconds(2);
        scene.rotateCameraY(45);

        scene.world().modifyBlockEntityNBT(util.select().position(nixietube), NixieTubeBlockEntity.class, nbt -> nbt.putInt("RedstoneStrength",
                SightSensorEntity.getRedstonePowerFromDistance(sightSensor, birdPos.add(birdMovement), 15)));

        scene.effects().indicateRedstone(nixietube);

        scene.idleSeconds(2);

        scene.addKeyframe();
        scene.rotateCameraY(45);

        ItemStack wrench = new ItemStack(AllItems.WRENCH.get());
        scene.overlay().showText(20 * 5)
                .placeNearTarget()
                .text("Right-click the back with a wrench to reverse the signal.")
                .pointAt(util.vector().blockSurface(sightSensor, Direction.WEST));

        scene.overlay().showControls(ssVec.add(.5,0,0), Pointing.RIGHT, 20 * 5).rightClick()
                        .withItem(wrench);

        scene.world().modifyBlock(sightSensor, s -> s.setValue(SightSensor.REVERSED, true), false);




        scene.idleSeconds(1);

        scene.effects().indicateRedstone(nixietube);
        scene.world().modifyBlockEntityNBT(util.select().position(nixietube), NixieTubeBlockEntity.class, nbt -> nbt.putInt("RedstoneStrength",
                15 - SightSensorEntity.getRedstonePowerFromDistance(sightSensor, birdPos.add(birdMovement), 15)));

        scene.idleSeconds(5);


        scene.addKeyframe();

        scene.overlay().showText(20 * 5)
                .placeNearTarget()
                .text("If you are holding a wrench, you can also change the range of the sight sensor.")
                .pointAt(util.vector().blockSurface(sightSensor, Direction.WEST));

        scene.overlay().showControls(ssVec.add(0,0,-0.5), Pointing.LEFT, 20 * 5).rightClick()
                .withItem(wrench);


        scene.idleSeconds(5);


        scene.markAsFinished();
        scene.idleSeconds(2);

        scene.special().moveParrot(birb, birdMovement.scale(-1), 20);

        scene.idle(10);

        scene.effects().indicateRedstone(nixietube);
        scene.world().modifyBlockEntityNBT(util.select().position(nixietube), NixieTubeBlockEntity.class, nbt -> nbt.putInt("RedstoneStrength",
                15 - SightSensorEntity.getRedstonePowerFromDistance(sightSensor, birdPos.add(birdMovement.scale(-1)), 15)));

        scene.rotateCameraY(-45);
    }

    public static class FlappyFacePOIParrot extends ParrotPose.FacePointOfInterestPose {
        @Override
        public void tick(PonderScene scene, Parrot entity, Vec3 location) {
            double length = entity.position()
                    .subtract(entity.xOld, entity.yOld, entity.zOld)
                    .length();
            entity.setOnGround(false);
            float f = (float) (PonderUI.ponderTicks % 100);
            entity.flapSpeed = Mth.sin(f) + 1;

            Vec3 p_200602_2_ = getFacedVec(scene);
            Vec3 Vector3d = location.add(entity.getEyePosition(0));
            double d0 = p_200602_2_.x - Vector3d.x;
            double d1 = p_200602_2_.y - Vector3d.y;
            double d2 = p_200602_2_.z - Vector3d.z;
            double d3 = Mth.sqrt((float) (d0 * d0 + d2 * d2));
            float targetPitch = Mth.wrapDegrees((float) -(Mth.atan2(d1, d3) * (double) (180F / (float) Math.PI)));
            float targetYaw = Mth.wrapDegrees((float) -(Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) + 90);

            entity.setXRot(AngleHelper.angleLerp(.4f, entity.getXRot(), targetPitch));
            entity.setYRot(AngleHelper.angleLerp(.4f, entity.getYRot(), targetYaw));
        }
    }
}
