package dev.xplate.create_security.datagen.provider;

import dev.xplate.create_security.reg.SecurityBiomeMods;
import dev.xplate.create_security.reg.SecurityDamageTypes;
import dev.xplate.create_security.reg.SecurityFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static dev.xplate.create_security.CSSecurity.MODID;

public class CSSGeneratedEntriesProvider extends DatapackBuiltinEntriesProvider {

    private static final DamageType endSicknessDamageType = new DamageType(SecurityDamageTypes.END_SICKNESS.location().getPath(),
            DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
            0.5f,
            DamageEffects.BURNING,
            DeathMessageType.DEFAULT);

    public CSSGeneratedEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, new RegistrySetBuilder()
                        .add(Registries.CONFIGURED_FEATURE, SecurityFeatures::configured)
                        .add(Registries.PLACED_FEATURE, SecurityFeatures::placed)
                        .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, SecurityBiomeMods::bootstrap)
                        .add(Registries.DAMAGE_TYPE, bootstrap -> {
                            // Use new DamageType() to create an in-code representation of a damage type.
                            // The parameters map to the values of the JSON file, in the order seen above.
                            // All parameters except for the message id and the exhaustion value are optional.
                            
                            bootstrap.register(SecurityDamageTypes.END_SICKNESS, endSicknessDamageType);
                        }),
                Set.of(MODID));
    }
}
