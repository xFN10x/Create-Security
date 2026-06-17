package dev.xplate.create_security.mixins.rendering;

import dev.xplate.create_security.CSecurity;
import dev.xplate.create_security.CSecurityClient;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixins {

    @Inject(method = "resize", at = @At("TAIL"))
    public void resize(int width, int height, CallbackInfo ci) {
        if (CSecurityClient.googlesEffectHandler != null) CSecurityClient.googlesEffectHandler.resize(width, height);
    }
}
