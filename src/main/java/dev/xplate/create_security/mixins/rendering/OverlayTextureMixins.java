package dev.xplate.create_security.mixins.rendering;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Mixin(OverlayTexture.class)
public class OverlayTextureMixins {
    @ModifyConstant(method = "<init>", constant = @Constant(intValue = -1308622593))
    public int makeColour1Purple(int constant, @Local(ordinal = 0) int y) {
        return y <= 1 ? new Color(245,40,255, 150).getRGB() : constant;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void debugOverlayTexture(CallbackInfo ci, @Local NativeImage nativeimage) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.createGraphics().drawImage(new ImageIcon(nativeimage.asByteArray()).getImage(),0,0,null);
        ImageIO.write(bufferedImage, "png", new File("test.png"));
        return;
    }
}
