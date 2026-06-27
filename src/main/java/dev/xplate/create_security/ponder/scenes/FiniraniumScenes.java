package dev.xplate.create_security.ponder.scenes;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import dev.xplate.create_security.misc.Utils;
import dev.xplate.create_security.reg.SecurityEffects;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.ParrotElement;
import net.createmod.ponder.api.element.ParrotPose;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.instruction.PonderInstruction;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class FiniraniumScenes {

    public static void endSickness(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("finiranium_end_sickness", "Finiranium: End Sickness");
        scene.configureBasePlate(0, 0, 5);
        scene.world().showSection(util.select().layers(0, 4), Direction.UP);
        scene.rotateCameraY(-90);

        Vec3 parrotPos = new Vec3(2.0, 2.0, 1.0);
        Vec3 center = new Vec3(2.5, 2.5, 2.5);

        scene.idle(10);
        scene.overlay().showText(100)
                .placeNearTarget()
                .text("Finiranium is a powerful mineral from the end; It can be found in End Midlands.");
        scene.idle(60);

        scene.overlay().showText(100)
                .placeNearTarget()
                .text("However, it is not safe to be around.");
        scene.idle(50);

        ElementLink<ParrotElement> parrot = scene.special().createBirb(parrotPos, ParrotPose.FlappyPose::new);
        scene.idle(10);
        scene.addKeyframe();

        scene.overlay().showText(100)
                .placeNearTarget()
                .text("Being around Finiranium products can build up End Sickness.");

        scene.idleSeconds(2);
        //i dont know why this doesn't work
        //scene.addInstruction(endSicknessInstruction(center));
        scene.idle(10);

        scene.effects().emitParticles(parrotPos, (world, x, y, z) -> {
            world.addParticle(
                    ColorParticleOption
                            .create(ParticleTypes.ENTITY_EFFECT, Utils.FiniraniumGrad.getAtPercent(1f))
                    , x, y, z, 0.01, 0.01, 0.01);
        }, 1, 400);

        scene.idleSeconds(1);
        scene.overlay().showText(100)
                .placeNearTarget()
                .text("End Sickness slowly drains your health as you remain around finiranium, so heed the warnings in the chat. ");
        scene.idleSeconds(3);
        scene.markAsFinished();
    }

    public static PonderInstruction endSicknessInstruction(Vec3 center) {
        return new PonderInstruction() {
            public boolean complete = false;

            @Override
            public boolean isComplete() {
                return complete;
            }

            @Override
            public void tick(PonderScene scene) {
                PonderLevel world = scene.getWorld();
                Vec3 size = center.scale(2);
                world.getEntityList().forEach(e -> {
                    if (e instanceof LivingEntity le) {
                        le.addEffect(new MobEffectInstance(SecurityEffects.END_SICKNESS));
                    }
                });
                complete = true;
            }
        };
    }

}
