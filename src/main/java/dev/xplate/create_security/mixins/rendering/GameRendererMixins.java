package dev.xplate.create_security.mixins.rendering;

import dev.xplate.create_security.CSecurity;
import dev.xplate.create_security.CSecurityClient;
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
        if (CSecurityClient.googlesEffectHandler.shouldRenderFiniraniumGoggles()) {
            CSecurityClient.googlesEffectHandler.postFilter.process(deltaTracker.getRealtimeDeltaTicks());
        }
    }
}
