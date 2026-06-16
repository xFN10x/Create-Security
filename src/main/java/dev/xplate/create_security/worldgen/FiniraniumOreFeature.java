package dev.xplate.create_security.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class FiniraniumOreFeature extends OreFeature {
    public FiniraniumOreFeature(Codec<OreConfiguration> codec) {
        super(codec);
    }
}
