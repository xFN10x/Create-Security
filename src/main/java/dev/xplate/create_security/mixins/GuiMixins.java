package dev.xplate.create_security.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.xplate.create_security.reg.SecurityEffects;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.xplate.create_security.CSSecurity.res;

@Mixin(Gui.class)
public class GuiMixins {

    @Inject(method = "renderFood", at = @At(
            value = "INVOKE",
            shift = At.Shift.BEFORE,
            by = 1,
            target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V"
    ))
    public void renderFood(GuiGraphics guiGraphics, Player player, int y, int x, CallbackInfo ci, @Local(ordinal = 0) LocalRef<ResourceLocation> res0, @Local(ordinal = 1) LocalRef<ResourceLocation> res1, @Local(ordinal = 2) LocalRef<ResourceLocation> res2) {
        // res 0 is empty
        // res 1 is half
        // res 2 is full
        if (player.hasEffect(SecurityEffects.END_SICKNESS)) {
            res0.set(res("hud/end_sick_food_empty"));
            res1.set(res("hud/end_sick_food_half"));
            res2.set(res("hud/end_sick_food_full"));
        }
    }
}
