package dev.xplate.create_security.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class PlayerMixins {


    @ModifyReturnValue(method = "getDisplayName", at = @At("RETURN"))
    public Component getNameMixin(Component original) {
        Player This = (Player)(Object)this;
        if (This.hasEffect(MobEffects.INVISIBILITY)) {
            return Component.literal("§k" + original.getString().replaceAll(".", "?")).withStyle(getObfuscatedStyle());
        }
        return original;
    }

    @Unique
    public Style getObfuscatedStyle() {
        return Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.invisiblePlayer")));
    }
}
