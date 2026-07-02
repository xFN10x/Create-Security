package dev.xplate.create_security.datagen.provider;

import com.simibubi.create.api.data.recipe.FillingRecipeGen;
import dev.xplate.create_security.reg.SecurityFluids;
import dev.xplate.create_security.reg.SecurityItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class CSSFillingRecipeProvider extends FillingRecipeGen {
    public CSSFillingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);

        create("fill_goggles", b ->
                b.require(SecurityFluids.LIQUID_FINIRANIUM.get(), 1000)
                        .require(SecurityItems.EMPTY_FINI_GOGGLES.get())
                        .output(SecurityItems.FINI_GOGGLES));
    }
}
