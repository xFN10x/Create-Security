package dev.xplate.create_security.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.xplate.create_security.CSSecurityConfigs;
import dev.xplate.create_security.config.CSSecServer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.RandomSource;
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
        CSSecServer sConfig = CSSecurityConfigs.server();
        if (This.hasEffect(MobEffects.INVISIBILITY) && sConfig.obfuscateInvisiblePlayerNames.get()) {
            if (sConfig.obfuscatedNamesRandomLength.get()) {
                original = Component.literal("?".repeat(RandomSource.create(hashCode()).nextIntBetweenInclusive(3,10)));
            } 
            return Component.literal("§k" + original.getString().replaceAll(".", "?")).withStyle(createSecurity$getObfuscatedStyle());
        }
        return original;
    }

    @Unique
    public Style createSecurity$getObfuscatedStyle() {
        return Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("chat.invisiblePlayer")));
    }
}
