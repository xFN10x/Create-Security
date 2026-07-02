package dev.xplate.create_security.datagen.provider;

import com.simibubi.create.api.data.recipe.CompactingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import dev.xplate.create_security.reg.SecurityBlocks;
import dev.xplate.create_security.reg.SecurityItems;
import dev.xplate.create_security.reg.SecurityFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class CSSCompactingRecipeProvider extends CompactingRecipeGen {
    public CSSCompactingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);

        create("create_finiranium_block_from_finiranium", b ->
                b.require(SecurityItems.FINIRANIUM).require(SecurityItems.FINIRANIUM).require(SecurityItems.FINIRANIUM)
                        .require(SecurityItems.FINIRANIUM).require(SecurityItems.FINIRANIUM).require(SecurityItems.FINIRANIUM)
                        .require(SecurityItems.FINIRANIUM).require(SecurityItems.FINIRANIUM).require(SecurityItems.FINIRANIUM)
                        .requiresHeat(HeatCondition.SUPERHEATED)
                        .require(SecurityFluids.LIQUID_FINIRANIUM.get(), 100)
                        .output(SecurityBlocks.FINIRANIUM_BLOCK)
        );
    }
}
