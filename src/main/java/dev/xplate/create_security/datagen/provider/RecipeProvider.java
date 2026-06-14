package dev.xplate.create_security.datagen.provider;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import dev.xplate.create_security.reg.SecurityBlocks;
import dev.xplate.create_security.reg.SecurityItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public RecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, SecurityBlocks.SIGHT_SENSOR)
                .pattern("aoe")
                .pattern("rrr")

                .define('a', AllBlocks.ANDESITE_CASING)
                .define('o', Blocks.OBSIDIAN)
                .define('e', Items.ENDER_EYE)
                .define('r', Blocks.REDSTONE_WIRE)

                .unlockedBy("has_self", has(SecurityBlocks.SIGHT_SENSOR))
                .unlockedBy("has_ender_eye", has(Items.ENDER_EYE))

                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SecurityItems.KEYCARD)
                .requires(AllItems.STURDY_SHEET)
                .requires(AllItems.BRASS_NUGGET)
                .requires(AllItems.CARDBOARD)

                .unlockedBy("has_cardboard", has(AllItems.CARDBOARD))
                .unlockedBy("has_sturdy", has(AllItems.BRASS_NUGGET))

                .save(output);
    }
}
