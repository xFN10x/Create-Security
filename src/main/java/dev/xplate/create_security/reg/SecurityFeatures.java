package dev.xplate.create_security.reg;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

import static dev.xplate.create_security.CSSecurity.MODID;

public class SecurityFeatures {

    public static final DeferredRegister<Feature<?>> FEATURE_REG = DeferredRegister.create(Registries.FEATURE, MODID);

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
    public static <C extends FeatureConfiguration, F extends Feature<C>> DeferredHolder<Feature<?>, F> regFeature(String string, F feature) {
        return FEATURE_REG.register(string, () -> feature);
    }

    public static final List<PlacementModifier> FINIRANIUM_FEATURE_PLACED_MODFIIERS = List.of(
            CountPlacement.of(2),
            InSquarePlacement.spread(),
            HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(40))
    );

    public static final ResourceKey<ConfiguredFeature<?, ?>> FINIRANIUM_ORE_CONFIGURED = regConfig("finiranium_ore");
    public static final ResourceKey<PlacedFeature> FINIRANIUM_ORE_PLACED = regPlacement("finiranium_ore");
    public static final DeferredHolder<Feature<?>, OreFeature> FINIRANIUM_ORE_FEATURE = regFeature("finiranium_ore", new OreFeature(OreConfiguration.CODEC));

    public static void configured(BootstrapContext<ConfiguredFeature<?, ?>> bootstrapContext) {
        bootstrapContext.register(FINIRANIUM_ORE_CONFIGURED,
                new ConfiguredFeature<>(
                        FINIRANIUM_ORE_FEATURE.get(), new OreConfiguration(new BlockMatchTest(Blocks.END_STONE), SecurityBlocks.FINIRANIUM_ORE.getDefaultState(), 5, 0.5f))
                );
    }

    public static void placed(BootstrapContext<PlacedFeature> bootstrapContext) {
        bootstrapContext.register(FINIRANIUM_ORE_PLACED,
                new PlacedFeature(
                        bootstrapContext.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(FINIRANIUM_ORE_CONFIGURED),
                        FINIRANIUM_FEATURE_PLACED_MODFIIERS
                ));
    }

    public static void reg(IEventBus bus) {
        FEATURE_REG.register(bus);
    }
}
