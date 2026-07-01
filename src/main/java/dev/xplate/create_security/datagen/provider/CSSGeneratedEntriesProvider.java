package dev.xplate.create_security.datagen.provider;

import dev.xplate.create_security.reg.SecurityBiomeMods;
import dev.xplate.create_security.reg.SecurityFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static dev.xplate.create_security.CSSecurity.MODID;

public class CSSGeneratedEntriesProvider extends DatapackBuiltinEntriesProvider {
    public CSSGeneratedEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, new RegistrySetBuilder()
                        .add(Registries.CONFIGURED_FEATURE, SecurityFeatures::configured)
                        .add(Registries.PLACED_FEATURE, SecurityFeatures::placed)
                        .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, SecurityBiomeMods::bootstrap),
                Set.of(MODID));
    }
}
