package dev.xplate.create_security.reg;

import com.simibubi.create.Create;
import com.simibubi.create.infrastructure.worldgen.AllPlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static dev.xplate.create_security.CSecurity.MODID;

public class SecurityBiomeMods {

    //took this from create
    public static final ResourceKey<BiomeModifier>
            FINIRANIM = key("finiranium_ore");

    private static ResourceKey<BiomeModifier> key(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(MODID, name));
    }

    public static void bootstrap(BootstrapContext<BiomeModifier> ctx) {
        HolderGetter<Biome> biomeLookup = ctx.lookup(Registries.BIOME);

        HolderSet.Direct<Biome> IsEndMid = HolderSet.direct(biomeLookup.getOrThrow(Biomes.END_MIDLANDS));

        HolderGetter<PlacedFeature> featureLookup = ctx.lookup(Registries.PLACED_FEATURE);

        Holder<PlacedFeature> finiraniumOre = featureLookup.getOrThrow(SecurityFeatures.FINIRANIUM_ORE_PLACED);

        ctx.register(FINIRANIM, addOre(IsEndMid, finiraniumOre));
    }

    private static BiomeModifiers.AddFeaturesBiomeModifier addOre(HolderSet<Biome> biomes, Holder<PlacedFeature> feature) {
        return new BiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), GenerationStep.Decoration.UNDERGROUND_ORES);
    }
}
