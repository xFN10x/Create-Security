package dev.xplate.create_security.datagen.provider;

import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import com.simibubi.create.content.kinetics.crusher.CrushingRecipe;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import dev.xplate.create_security.CSSecurity;
import dev.xplate.create_security.reg.SecurityBlocks;
import dev.xplate.create_security.reg.SecurityItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CSSCrushingRecipeProvider extends CrushingRecipeGen {
    public CSSCrushingRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String defaultNamespace) {
        super(output, registries, defaultNamespace);

        create("crush_finiranium", b ->
                b.duration(20 * 30)
                        .require(SecurityItems.FINIRANIUM)
                        .output(0.80f, SecurityItems.FINIRANIUM_DUST.get(), 2)
                        .output(0.50f, SecurityItems.FINIRANIUM_DUST.get(), 1)
                        .output(0.10f, SecurityItems.FINIRANIUM_DUST.get(), 1)
                        .output(0.15f, SecurityItems.FINIRANIUM.get())
        );
        create("crush_finiranium_block", b ->
                b.duration(20 * 45)
                        .require(SecurityBlocks.FINIRANIUM_BLOCK)
                        .output(SecurityItems.FINIRANIUM.get(), 7)
                        .output(0.60f, SecurityItems.FINIRANIUM.get(), 1)
                        .output(0.60f, SecurityItems.FINIRANIUM.get(), 1)
        );
        create("why_the_hell_would_you_crush_finiranium_goggles", b ->
                b.duration(20 * 5)
                        .require(SecurityItems.FINI_GOGGLES)
                        .output(0.40f, SecurityItems.FINIRANIUM_DUST.get(), 2)
                        .output(0.20f, SecurityItems.FINIRANIUM_DUST.get(), 1)
                        .output(0.20f, SecurityItems.FINIRANIUM_DUST.get(), 1)
                        .output(Items.STRING, 1)
                        .output(0.5f, Items.STRING, 1)
                        .output(0.5f, Items.STRING, 1)
        );
        create("crush_end_stone", b ->
                b.duration(20 * 5)
                        .require(Blocks.END_STONE)
                        .output(0.10f, SecurityItems.FINIRANIUM_DUST.get(), 1)
                        .output(0.05f, SecurityItems.FINIRANIUM_DUST.get(), 1)
                        .output(0.05f, SecurityItems.FINIRANIUM_DUST.get(), 1)
        );
        ore(Blocks.END_STONE, SecurityBlocks.FINIRANIUM_ORE::get, SecurityItems.FINIRANIUM::get, 2, 20 * 30);
    }

    @Override
    protected GeneratedRecipe create(Supplier<ItemLike> singleIngredient, UnaryOperator<StandardProcessingRecipe.Builder<CrushingRecipe>> transform) {
        return create(CSSecurity.MODID, singleIngredient, transform);
    }
}
