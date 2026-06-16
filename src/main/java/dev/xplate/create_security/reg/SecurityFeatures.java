package dev.xplate.create_security.reg;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.TrapezoidHeight;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;

import static dev.xplate.create_security.CSecurity.MODID;

public class SecurityFeatures {

    public static ResourceKey<ConfiguredFeature<?, ?>> regConfig(String id) {
        return ResourceKey.create(
                Registries.CONFIGURED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(MODID, id));
    }

    public static ResourceKey<PlacedFeature> regPlacement(String id) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(MODID, id)
        );
    }

    //taken from the feature class
    public static <C extends FeatureConfiguration, F extends Feature<C>> F regFeature(String string, F feature) {
        return Registry.register(BuiltInRegistries.FEATURE, string, feature);
    }

    public static final List<PlacementModifier> FINIRANIUM_FEATURE_PLACED_MODFIIERS = List.of(
            CountPlacement.of(1),
            BiomeFilter.biome(),
            RarityFilter.onAverageOnceEvery(4));

    public static final ResourceKey<ConfiguredFeature<?, ?>> FINIRANIUM_ORE_CONFIGURED = regConfig("finiranium_ore_config");
    public static final ResourceKey<PlacedFeature> FINIRANIUM_ORE_PLACED = regPlacement("finiranium_ore_placement");
    public static final OreFeature FINIRANIUM_ORE_FEATURE = regFeature("finiranium_ore", new OreFeature(OreConfiguration.CODEC));

    public static void configured(BootstrapContext<ConfiguredFeature<?, ?>> bootstrapContext) {
        bootstrapContext.register(FINIRANIUM_ORE_CONFIGURED,
                new ConfiguredFeature<>(
                        FINIRANIUM_ORE_FEATURE, new OreConfiguration(new BlockMatchTest(Blocks.END_STONE), SecurityBlocks.FINIRANIUM_ORE.getDefaultState(), 5, 0.5f))
                );
    }

    public static void placed(BootstrapContext<PlacedFeature> bootstrapContext) {
        bootstrapContext.register(FINIRANIUM_ORE_PLACED,
                new PlacedFeature(
                        bootstrapContext.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(FINIRANIUM_ORE_CONFIGURED),
                        FINIRANIUM_FEATURE_PLACED_MODFIIERS
                ));
    }

    public static void reg() {
    }
}
