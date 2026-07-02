package dev.xplate.create_security.datagen.provider;

import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import dev.xplate.create_security.CSSecurity;
import dev.xplate.create_security.reg.SecurityItems;
import dev.xplate.create_security.reg.SecurityFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CSSMixingRecipeProvider extends MixingRecipeGen {

    public CSSMixingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);
        create(() -> SecurityItems.FINIRANIUM_DUST, b ->
                b.duration(20 * 10)
                        .requiresHeat(HeatCondition.SUPERHEATED)
                        .output(SecurityFluids.LIQUID_FINIRANIUM.get(), 100)
        );
    }

    @Override
    protected GeneratedRecipe create(Supplier<ItemLike> singleIngredient, UnaryOperator<StandardProcessingRecipe.Builder<MixingRecipe>> transform) {
        return create(CSSecurity.MODID, singleIngredient, transform);
    }
}
