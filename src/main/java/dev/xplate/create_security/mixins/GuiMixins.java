package dev.xplate.create_security.mixins;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixins {

    @Inject(method = "renderFood", at = @At())
    public void renderFood(GuiGraphics guiGraphics, Player player, int y, int x, CallbackInfo ci) {

    }
}
