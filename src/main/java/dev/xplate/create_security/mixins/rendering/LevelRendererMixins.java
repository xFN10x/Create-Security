package dev.xplate.create_security.mixins.rendering;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.xplate.create_security.CSecurityClient;
import dev.xplate.create_security.misc.rendering.FiniraniumGogglesEntityBufferSource;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.*;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;

@Mixin(LevelRenderer.class)
public class LevelRendererMixins {

    @Shadow
    @Nullable
    private RenderTarget entityTarget;
    @Final
    @Shadow
    private RenderBuffers renderBuffers;

    @Inject(method = "onResourceManagerReload", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/LevelRenderer;initOutline()V",
            shift = At.Shift.AFTER))
    public void setupEntityRendering(ResourceManager resourceManager, CallbackInfo ci) {
        if (CSecurityClient.googlesEffectHandler != null) {
            entityTarget = CSecurityClient.googlesEffectHandler.postFilter.getTempTarget("entity");
        }
    }

    @Shadow
    private void renderEntity(
            Entity entity, double camX, double camY, double camZ, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource
    ) {
    }

    @Inject(method = "renderLevel", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/LevelRenderer;renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
            shift = At.Shift.AFTER))
    public void renderEntities(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci,
                               @Local() Entity entity, @Local(ordinal = 0) double camx, @Local(ordinal = 1) double camy, @Local(ordinal = 2) double camz, @Local PoseStack posestack, @Local TickRateManager tickratemanager, @Local LocalRef<MultiBufferSource.BufferSource> multibuffersource$buffersource) {
        if (CSecurityClient.googlesEffectHandler != null && CSecurityClient.googlesEffectHandler.shouldRenderFiniraniumGoggles()) {
            FiniraniumGogglesEntityBufferSource outlinebuffersource = new FiniraniumGogglesEntityBufferSource(multibuffersource$buffersource.get());
            float parttick = deltaTracker.getGameTimeDeltaPartialTick(!tickratemanager.isEntityFrozen(entity));
            this.renderEntity(entity, camx, camy, camz, parttick, posestack, outlinebuffersource);
        }
    }

    @Inject(method = "resize", at = @At("TAIL"))
    public void resize(int width, int height, CallbackInfo ci) {
        if (CSecurityClient.googlesEffectHandler != null) CSecurityClient.googlesEffectHandler.resize(width, height);
    }
}
