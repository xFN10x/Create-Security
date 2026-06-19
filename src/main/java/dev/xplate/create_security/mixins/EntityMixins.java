package dev.xplate.create_security.mixins;

import dev.xplate.create_security.reg.SecurityItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixins {

    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    public void isInvisibleToPlayer(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (player.getItemBySlot(EquipmentSlot.HEAD).is(SecurityItems.FINI_GOGGLES)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
