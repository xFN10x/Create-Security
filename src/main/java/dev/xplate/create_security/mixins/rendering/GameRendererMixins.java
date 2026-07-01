package dev.xplate.create_security.mixins.rendering;

import dev.xplate.create_security.CSSecurityClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixins {


    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getMainRenderTarget()Lcom/mojang/blaze3d/pipeline/RenderTarget;", shift = At.Shift.BEFORE))
    public void renderFiniraniumGogglesEffect(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        if (CSSecurityClient.googlesEffectHandler.shouldRenderFiniraniumGoggles()) {
            CSSecurityClient.googlesEffectHandler.postFilter.process(deltaTracker.getRealtimeDeltaTicks());
        }
    }
}
