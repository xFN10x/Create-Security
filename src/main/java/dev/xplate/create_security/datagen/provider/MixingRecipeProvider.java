package dev.xplate.create_security.datagen.provider;

import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import dev.xplate.create_security.reg.SecurityItems;
import dev.xplate.create_security.reg.SecurityLiquids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class MixingRecipeProvider extends MixingRecipeGen {

    public MixingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);
        create(() -> SecurityItems.FINIRANIUM_DUST, b ->
                b.duration(20 * 10)
                        .requiresHeat(HeatCondition.SUPERHEATED)
                        .output(SecurityLiquids.LIQUID_FINIRANIUM.get(), 100)
        );
    }
}
