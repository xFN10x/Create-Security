package dev.xplate.create_security.reg;

import dev.xplate.create_security.CSSecurity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class SecurityDamageTypes {

    public static final ResourceKey<DamageType> END_SICKNESS =
            ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(CSSecurity.MODID, "end_sickness"));
    
    public static void reg() {
        
    }
}
